package conan.translator;

import conan.translator.codegen.DriverGenerator;
import conan.translator.model.ElementType;
import conan.translator.model.MonitorSpec;
import conan.translator.model.TestSequence;

import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Reads a (Roast-preprocessed) ConAn script and drives {@link DriverGenerator}.
 *
 * <p>Block nesting is tracked with a depth counter:
 * {@code #begin}=1, {@code #tick}/{@code #shared}=2, {@code #thread}/{@code #option}=3+.
 * Top-level blocks ({@code #conditions}, {@code #top}, global {@code #shared}) also
 * open one level.
 */
public final class ScriptParser {

    /** What the main loop should do after a command has been handled. */
    private enum Flow { NEXT, SKIP_RECORD, STOP }

    private final ScriptTokenizer tok;
    private final DriverGenerator gen;
    private final Diagnostics diag;
    private final String driverName;
    /** Monitor class given on the command line; overrides the script's classes. */
    private final String monitorOverride;

    // ---- script-wide state ------------------------------------------------
    private final Map<String, String> conditions = new LinkedHashMap<String, String>();
    private final Set<String> testedConditions = new HashSet<String>();
    private String topCode = "";
    private String globalSharedCode = "";
    private String globalSharedName = "shared";
    private int tickTime = 500;
    /** Monitor declared outside a sequence; applies to all following sequences. */
    private MonitorSpec defaultMonitor;
    private boolean overrideWarned;
    private int scriptSequences;
    private int generatedSequences;

    // ---- per-sequence / block state -------------------------------------------
    /** Sequence being parsed, or {@code null} outside {@code #begin ... #end}. */
    private TestSequence current;
    /** Option of the enclosing {@code #option} block, or "" if none. */
    private String blockOption = "";
    private boolean inTick, headerWritten, sequenceHasTick;
    private int depth, optionSavepoint;
    private String lastCommand = "";

    public ScriptParser(Reader in, DriverGenerator gen, Diagnostics diag,
                 String driverName, String monitorOverride) {
        this.tok = new ScriptTokenizer(in);
        this.gen = gen;
        this.diag = diag;
        this.driverName = driverName;
        this.monitorOverride = monitorOverride;
    }

    /** Parses the whole script, emitting header and sequences as it goes. */
    public void parse() throws IOException {
        String first = tok.next();
        if (!tok.atControl()) {
            diag.syntax("invalid command: " + first);
            return;
        }

        while (true) {
            boolean tickMultiplier = false;

            // anything between commands must be a control char (or "*" after #tick)
            while (!tok.atControl()) {
                String stray = tok.next();
                if (tok.atControl()) break;
                if (stray.equals("*") && lastCommand.equals("tick")) {
                    tickMultiplier = true;
                    break;
                }
                diag.syntax("invalid command: " + stray);
                if (current != null && !inTick) diag.info("Are you missing a #shared block?");
                return;
            }

            String command = tok.next();
            if (command == null) return;

            if (command.equals("begin")) diag.info("Sequence: " + (scriptSequences + 1));
            diag.info("  command: " + command + (tickMultiplier ? " (tick multiplier)" : ""));

            Flow flow = tickMultiplier && !isMonitorOrTick(command)
                    ? handleTickMultiplier(command)
                    : dispatch(command);
            if (flow == Flow.STOP) return;
            if (flow == Flow.NEXT) lastCommand = command;
        }
    }

    private static boolean isMonitorOrTick(String command) {
        return command.equals("monitor") || command.equals("cut") || command.equals("tick");
    }

    private Flow dispatch(String command) throws IOException {
        if (command.equals("monitor") || command.equals("cut")) return handleMonitor();
        if (command.equals("tick")) return handleTick();
        if (command.equals("ticktime")) return handleTickTime();
        if (command.endsWith("List")) return handleCollection(command, "List", false);
        if (command.endsWith("Array")) return handleCollection(command, "Array", true);
        if (command.equals("option")) return handleOption();
        if (command.equals("thread")) return handleThread();
        if (command.equals("top")) return handleTop();
        if (command.equals("conditions")) return handleConditions();
        if (command.equals("test")) return handleTest();
        if (command.equals("selectOption")) return handleSelectOption();
        if (command.equals("deadlock")) return handleDeadlock();
        if (command.equals("begin")) return handleBegin();
        if (command.equals("shared") || command.equals("fixture")) return handleShared();
        if (command.equals("end")) return handleEnd();
        if (command.equals("exit")) return Flow.STOP;
        return error(command);
    }

    // ---- command handlers -----------------------------------------------------

    /** {@code #cut var Class(args)}: inside a sequence applies to it, otherwise to all that follow. */
    private Flow handleMonitor() throws IOException {
        if (!monitorOverride.isEmpty() && !overrideWarned) {
            diag.warn("Command line monitor overrides monitor specified in ConAn script");
            overrideWarned = true;
        }
        tok.ordinary(')', '(');
        String variable = tok.next();
        if (!tok.atControl()) {
            String className = tok.next();
            if (!tok.atControl()) {
                String ctor = tok.readCode().trim();
                if (ctor.isEmpty()) ctor = "()";
                if (!monitorOverride.isEmpty()) className = monitorOverride;
                MonitorSpec spec = new MonitorSpec(variable, className, ctor);
                if (current != null) current.monitor = spec;
                else defaultMonitor = spec;
            }
        }
        tok.word(')', '(');
        return Flow.NEXT;
    }

    private Flow handleTick() {
        tok.ordinary('*');
        if (current == null) return error("#begin missing");
        inTick = true;
        blockOption = "";
        sequenceHasTick = true;
        if (++depth != 2) return error("#end missing");
        current.ticks++;
        return Flow.NEXT;
    }

    /** {@code #tick * N}: N consecutive empty ticks (the first was already counted). */
    private Flow handleTickMultiplier(String count) {
        depth--;
        try {
            current.ticks += Integer.parseInt(count) - 1;
        } catch (NumberFormatException e) {
            return error("invalid number of ticks");
        }
        inTick = false;
        return Flow.NEXT;
    }

    private Flow handleTickTime() throws IOException {
        String value = tok.next();
        try {
            tickTime = Integer.parseInt(value);
            return Flow.NEXT;
        } catch (NumberFormatException e) {
            return error("invalid #ticktime: " + value);
        }
    }

    private Flow handleCollection(String command, String suffix, boolean isArray) throws IOException {
        String kind = isArray ? "Array" : "List";
        if (current == null || inTick) return error("invalid placement of " + kind + " command");
        ElementType type = ElementType.fromJavaName(command.substring(0, command.indexOf(suffix)));
        if (type == null) return error("invalid " + kind.toLowerCase() + " type");
        return parseCollection(type, isArray) ? Flow.NEXT : Flow.STOP;
    }

    private Flow handleOption() throws IOException {
        if (!inTick) return error("#tick missing");
        optionSavepoint = depth;
        if (++depth < 3) return error("#end missing");
        blockOption = tok.next();
        if (tok.atControl()) return error("#option needs an option name");
        return Flow.NEXT;
    }

    private Flow handleThread() throws IOException {
        if (!inTick) return error("#tick missing");
        if (++depth < 3) return error("#end missing");
        return parseThread() ? Flow.NEXT : Flow.STOP;
    }

    private Flow handleTop() throws IOException {
        if (headerWritten) diag.warn("#top after the first #begin is ignored");
        ++depth;
        topCode = tok.readCode();
        return Flow.NEXT;
    }

    private Flow handleConditions() throws IOException {
        if (current != null) return error("invalid placement of #conditions block");
        ++depth;
        parseConditions();
        return Flow.NEXT;
    }

    private Flow handleTest() throws IOException {
        if (current == null || inTick) return error("invalid placement of #test block");
        testedConditions.addAll(tok.readWords());
        return Flow.NEXT;
    }

    private Flow handleSelectOption() throws IOException {
        if (current == null || inTick) return error("invalid placement of #selectOption");
        String opt = tok.next();
        if (tok.atControl()) return error("#selectOption needs an option name");
        current.selectedOption = opt;
        return Flow.NEXT;
    }

    private Flow handleDeadlock() {
        if (current == null) return error("invalid placement of #deadlock");
        current.deadlockTick = current.ticks;
        return Flow.NEXT;
    }

    private Flow handleBegin() {
        ++scriptSequences;
        if (++depth != 1) return error("#end missing");
        current = new TestSequence(globalSharedName);
        blockOption = "";
        if (!headerWritten) {
            gen.writeHeader(driverName, topCode, globalSharedCode);
            headerWritten = true;
        }
        return Flow.NEXT;
    }

    private Flow handleShared() throws IOException {
        if (!headerWritten && depth == 0) {
            ++depth;
            return parseGlobalShared() ? Flow.NEXT : Flow.STOP;
        }
        if (current == null || inTick) return error("invalid placement of #shared block");
        if (++depth != 2) return error("#end missing");
        return parseSequenceShared() ? Flow.NEXT : Flow.STOP;
    }

    private Flow handleEnd() {
        --depth;
        if (depth == 1) {
            tok.word('*');
            inTick = false;
        }
        if (depth == optionSavepoint && optionSavepoint > 0) {
            optionSavepoint = 0;
            blockOption = "";
        }
        if (depth > 0) return Flow.SKIP_RECORD;

        if (current != null) {
            if (sequenceHasTick) {
                generateSequence();
            } else {
                diag.warn("sequence " + scriptSequences + " has no #tick blocks and was skipped");
            }
            current = null;
            sequenceHasTick = false;
        }
        return Flow.NEXT;
    }

    private Flow error(String message) {
        diag.syntax(message);
        return Flow.STOP;
    }

    // ---- block parsers --------------------------------------------------------

    private void parseConditions() throws IOException {
        tok.eolSignificant(true);
        String id = "";
        StringBuilder desc = new StringBuilder();
        while (true) {
            String s = tok.next();
            if (tok.atControl() || s.equals(ScriptTokenizer.EOL)) {
                if (!id.isEmpty()) {
                    conditions.put(id, desc.toString());
                    id = "";
                    desc.setLength(0);
                }
                if (tok.atControl()) break;
                continue;
            }
            if (id.isEmpty()) id = s;
            else desc.append(s).append(' ');
        }
        tok.eolSignificant(false);
    }

    /**
     * Reads an optional {@code <name>} followed by code up to the next command.
     * Returns {@code {name, null, code}} when named, {@code {null, firstToken, rest}}
     * when unnamed, {@code {null, null, null}} if the block is empty, or {@code null}
     * if the closing bracket is missing.
     */
    private String[] readNamedBlock(String missingBracketMessage) throws IOException {
        tok.ordinary('>', '<');
        try {
            String first = tok.next();
            if (tok.atControl()) return new String[] { null, null, null };
            String name = null;
            String leading = null;
            if (first.equals("<")) {
                name = tok.readIdAfterOpenBracket();
                if (name == null) {
                    diag.syntax(missingBracketMessage);
                    return null;
                }
            } else {
                leading = first;
            }
            tok.word('>', '<');
            return new String[] { name, leading, tok.readCode() };
        } finally {
            tok.word('>', '<');
        }
    }

    private static String joinCode(String[] block) {
        return (block[1] == null ? "" : block[1]) + " " + block[2];
    }

    /** Global {@code #shared [<name>] code #end}, before the first sequence. */
    private boolean parseGlobalShared() throws IOException {
        String[] block = readNamedBlock("invalid global identifier. missing >.");
        if (block == null) return false;
        if (block[0] != null) globalSharedName = block[0];
        if (block[2] != null) globalSharedCode = joinCode(block);
        return true;
    }

    /** Per-sequence {@code #shared [<name>] code #end}. */
    private boolean parseSequenceShared() throws IOException {
        String[] block = readNamedBlock("invalid shared identifier. missing >.");
        if (block == null) return false;
        if (block[0] != null) current.sharedName = block[0];
        if (block[2] != null) current.sharedBody = joinCode(block);
        return true;
    }

    /** {@code #<type>List [<name>] v1 v2 ...} or {@code #<type>Array ...}. */
    private boolean parseCollection(ElementType type, boolean isArray) throws IOException {
        tok.ordinary('>', '<');
        List<String> values = new ArrayList<String>();
        String name;
        try {
            String first = tok.next();
            if ("<".equals(first)) {
                name = tok.readIdAfterOpenBracket();
                if (name == null) {
                    diag.syntax("invalid identifier. missing >.");
                    return false;
                }
            } else {
                if (!tok.atControl()) values.add(tok.requote(first));
                name = type.javaName + (isArray ? "Array" : "List");
            }
        } finally {
            tok.word('>', '<');
        }

        if (!tok.atControl()) {
            while (true) {
                String s = tok.next();
                if (tok.atControl()) break;
                s = tok.requote(s);
                if (!s.isEmpty()) values.add(s);
            }
        }
        if (values.isEmpty()) {
            diag.syntax("no entries specified. Zero length " + (isArray ? "array." : "list."));
            return false;
        }
        (isArray ? current.arrays : current.lists).get(type).put(name, values);
        return true;
    }

    /** {@code #thread [<name>] code #end}. */
    private boolean parseThread() throws IOException {
        String[] block = readNamedBlock("invalid thread identifier. missing >.");
        if (block == null) return false;
        String name = block[0] != null ? block[0] : "_thr_" + (++current.anonymousThreads);
        String code = block[2] == null ? "" : block[1] == null ? block[2] : block[1] + " " + block[2];
        current.addStep(name, code, blockOption);
        return true;
    }

    // ---- generation -------------------------------------------------------------

    private void generateSequence() {
        int n = ++generatedSequences;
        MonitorSpec monitor = current.monitor != null ? current.monitor : defaultMonitor;
        if (monitor == null) {
            diag.syntax("no monitor defined for test sequence " + n);
            monitor = MonitorSpec.NONE;
        }
        gen.writeSequence(n, current, monitor, tickTime);
    }

    /** Writes {@code main()} and helper classes, then reports untested conditions. */
    public void finish() {
        if (current != null || depth > 0) diag.warn("script ended inside an unclosed block (#end missing)");
        gen.writeFooter(generatedSequences);
        for (String cond : conditions.keySet()) {
            if (!testedConditions.contains(cond)) {
                diag.warn("Condition " + cond + " does not appear in a test sequence");
            }
        }
    }
}
