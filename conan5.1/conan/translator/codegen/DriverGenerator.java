package conan.translator.codegen;

import conan.translator.model.ElementType;
import conan.translator.model.MonitorSpec;
import conan.translator.model.TestSequence;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Emits the Java source of the test driver.
 *
 * <p>Layout of the generated file:
 * <ol>
 *   <li>header: imports, {@code #top} code, {@code ConanGlobalSetup}, driver class opening</li>
 *   <li>one {@code sequenceN()} method per test sequence</li>
 *   <li>{@code main()} and the driver class closing brace</li>
 *   <li>one {@code ConanThreadN} class per sequence</li>
 *   <li>one {@code ConanSetupN} class per sequence</li>
 * </ol>
 * Sequence methods are streamed as they are built; thread classes are buffered
 * because they must follow {@code main()}.
 */
public final class DriverGenerator {

    private static final String NL = System.lineSeparator();

    private final PrintWriter out;
    private final List<String> threadClasses = new ArrayList<String>();
    private final List<String> setupClasses = new ArrayList<String>();

    public DriverGenerator(PrintWriter out) {
        this.out = out;
    }

    public void writeHeader(String driverName, String topCode, String globalSharedCode) {
        out.println(
                "import roast.*;" + NL
                + "import conan.runtime.*;" + NL + NL
                + "// START TOP CODE" + NL
                + topCode + NL
                + "// END TOP CODE" + NL + NL
                + "class ConanGlobalSetup extends ConanSetup {" + NL
                + globalSharedCode + "}" + NL + NL
                + "public class " + driverName + " {" + NL + NL);
    }

    /**
     * Emits {@code sequenceN()} and buffers the matching {@code ConanThreadN}
     * and {@code ConanSetupN} classes.
     *
     * @param n        sequence number (1-based, contiguous)
     * @param seq      threads, steps, collections and shared block of the sequence
     * @param monitor  component under test ({@link MonitorSpec#NONE} if absent)
     * @param tickTime tick length in ms
     */
    public void writeSequence(int n, TestSequence seq, MonitorSpec monitor, int tickTime) {
        writeSequenceMethod(n, seq, monitor, tickTime);
        threadClasses.add(buildThreadClass(n, seq, monitor));
        setupClasses.add(buildSetupClass(n, seq));
    }

    private void writeSequenceMethod(int n, TestSequence seq, MonitorSpec monitor, int tickTime) {
        List<String> threads = seq.threads;

        out.println("    public static void sequence" + n + "() {" + NL + NL);
        out.println("        " + monitor.className + " " + monitor.variable + " = new "
                + monitor.className + monitor.constructorArgs + ";" + NL);
        out.println("        MonitorClock clock = new MonitorClock();" + NL
                + "        MonitorTimer timer = new MonitorTimer(clock, "
                + seq.ticks + ", " + tickTime + ");" + NL
                + "        ConanThread.clear();" + NL
                + "        ConanSetup" + n + " shared = new ConanSetup" + n + "();" + NL
                + "        shared.setup();" + NL + NL);

        String monitorArg = monitor.isPresent() ? monitor.variable + "," : "";
        for (int i = 0; i < threads.size(); ++i) {
            String name = threads.get(i);
            out.println("        ConanThread " + name + " = new ConanThread" + n + " ("
                    + monitorArg + "clock," + i + "," + "\"" + name + "\", shared);");
            out.println("        " + name + ".start();");
            out.println("        ConanThread.add(\"" + name + "\"," + name + ");");
        }
        out.println("        timer.start();" + NL
                + "        try {" + NL
                + "            timer.join();");
        out.println("        } catch (InterruptedException e) {}");
        out.println("        ConanThread[] threads = {" + String.join(",", threads) + "};");

        out.println("        boolean deadlocked = ConanHelper.checkDeadlock(threads"
                + "," + seq.deadlockTick + "," + n + ",clock.lastTick());");
        out.println("        if (!deadlocked) ConanHelper.checkExpectedDeadlock("
                + seq.deadlockTick + "," + n + ");");
        out.println("        shared.teardown();");
        out.println("    }" + NL);
    }

    private String buildThreadClass(int n, TestSequence seq, MonitorSpec monitor) {
        StringBuilder sb = new StringBuilder();
        sb.append("class ConanThread").append(n).append(" extends ConanThread {").append(NL).append(NL);
        sb.append("    ConanSetup").append(n).append(' ').append(seq.sharedName).append(';').append(NL);
        if (monitor.isPresent()) {
            sb.append("    ").append(monitor.className).append(' ').append(monitor.variable).append(';').append(NL);
        }

        // arrays, then list (vector) declarations, then a static block filling the lists
        for (ElementType t : ElementType.values()) {
            for (Map.Entry<String, List<String>> e : seq.arrays.get(t).entrySet()) {
                sb.append("    private static ").append(t.javaName).append("[] ").append(e.getKey())
                  .append(" = new ").append(t.javaName).append("[] {")
                  .append(String.join(",", e.getValue())).append("};").append(NL);
            }
        }
        for (ElementType t : ElementType.values()) {
            for (String name : seq.lists.get(t).keySet()) {
                sb.append("    private static ConanVector ").append(name)
                  .append(" = new ConanVector();").append(NL);
            }
        }
        sb.append(NL).append("    // static constructor").append(NL);
        sb.append("    static {").append(NL);
        for (ElementType t : ElementType.values()) {
            for (Map.Entry<String, List<String>> e : seq.lists.get(t).entrySet()) {
                for (String value : e.getValue()) {
                    sb.append("        ").append(e.getKey()).append(".add((").append(t.javaName)
                      .append(')').append(value).append(");").append(NL);
                }
                sb.append(NL);
            }
        }
        sb.append("        LinkedOption.setSelected(\"").append(seq.selectedOption).append("\");").append(NL);
        sb.append("    }").append(NL).append(NL);

        // constructor
        sb.append("    ConanThread").append(n).append(" (")
          .append(monitor.isPresent() ? monitor.className + " monitor," : "")
          .append("MonitorClock clock, int id, String name, ConanSetup").append(n)
          .append(" shared) {").append(NL)
          .append("        super(name);").append(NL)
          .append("        this.clock = clock;").append(NL)
          .append("        this.").append(seq.sharedName).append(" = shared;").append(NL);
        if (monitor.isPresent()) {
            sb.append("        this.").append(monitor.variable).append(" = monitor;").append(NL);
        }
        sb.append("        conan_Id = id;").append(NL);
        sb.append("        LinkedOption.reset();").append(NL);
        sb.append("    }").append(NL).append(NL);

        sb.append("    public Thread thread(String id) { return ConanThread.getThread(id); }")
          .append(NL).append(NL);

        // run(): one switch case per thread, steps in script order
        sb.append("    public void run() {").append(NL).append(NL);
        sb.append("        switch(conan_Id) {").append(NL).append(NL);
        for (int i = 0; i < seq.threads.size(); ++i) {
            String name = seq.threads.get(i);
            sb.append("            case ").append(i).append(':').append(NL);
            for (TestSequence.Step step : seq.steps) {
                if (!step.thread.equals(name)) continue;
                sb.append("                clock.await(").append(step.tick).append(");").append(NL)
                  .append("                conan_LastTick = clock.time();").append(NL);
                if (step.option != null) {
                    // braces so the option guards the whole thread block, not just its first statement
                    sb.append("                if (LinkedOption.compareSelected(\"")
                      .append(step.option).append("\")) {").append(NL);
                    sb.append("                ").append(step.code).append(NL);
                    sb.append("                }").append(NL);
                } else {
                    sb.append("                ").append(step.code).append(NL);
                }
            }
            sb.append("                break;").append(NL);
        }
        sb.append("            default: break;").append(NL)
          .append("        }").append(NL)
          .append("    }").append(NL)
          .append("}").append(NL);
        return sb.toString();
    }

    private static String buildSetupClass(int n, TestSequence seq) {
        String head = "class ConanSetup" + n + " extends ConanGlobalSetup {";
        return seq.sharedBody == null ? head + "}" : head + NL + seq.sharedBody + NL + "}";
    }

    /** Emits {@code main()}, closes the driver class, then the buffered helper classes. */
    public void writeFooter(int sequenceCount) {
        out.println("    public static void main(String[] args) {");
        for (int i = 1; i <= sequenceCount; ++i) {
            out.println("        sequence" + i + "();");
        }
        out.println("        Roast.printCounts();");
        out.println("        System.out.println(\"***** Liveness "
                + "errors: \"+ ConanHelper.deadlockEvents());");
        out.println("        System.exit(1);");
        out.println("    }");
        out.println("}" + NL + NL);

        for (String c : threadClasses) out.println(c);
        for (String c : setupClasses) out.println(c);
    }
}
