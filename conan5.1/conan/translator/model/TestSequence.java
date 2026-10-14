package conan.translator.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Everything collected for one {@code #begin ... #end} test sequence. */
public final class TestSequence {

    /** One {@code #thread} block: code run by a thread at a given tick. */
    public static final class Step {
        public final String thread;
        public final int tick;
        public final String code;
        /** Trace option guarding this step, or {@code null}. */
        public final String option;

        Step(String thread, int tick, String code, String option) {
            this.thread = thread;
            this.tick = tick;
            this.code = code;
            this.option = (option == null || option.isEmpty()) ? null : option;
        }
    }

    /** Thread blocks in script order. */
    public final List<Step> steps = new ArrayList<Step>();
    /** Distinct thread names in order of first appearance. */
    public final List<String> threads = new ArrayList<String>();

    /** {@code #<type>List} declarations, per element type, in script order. */
    public final Map<ElementType, Map<String, List<String>>> lists = newTypeMap();
    /** {@code #<type>Array} declarations, per element type, in script order. */
    public final Map<ElementType, Map<String, List<String>>> arrays = newTypeMap();

    /** Number of ticks (the tick index of the last tick block). */
    public int ticks;
    /** Tick at which a deadlock is expected, or 0. */
    public int deadlockTick;
    /** Counter used to name anonymous thread blocks. */
    public int anonymousThreads;
    /** Name of the shared-object field seen by thread code. */
    public String sharedName;
    /** Body of this sequence's {@code #shared} block, or {@code null} if none. */
    public String sharedBody;
    /** Trace option preselected with {@code #selectOption}. */
    public String selectedOption = "";
    /** Component under test declared inside this sequence, or {@code null}. */
    public MonitorSpec monitor;

    public TestSequence(String sharedName) {
        this.sharedName = sharedName;
    }

    public void addStep(String thread, String code, String option) {
        steps.add(new Step(thread, ticks, code, option));
        if (!threads.contains(thread)) threads.add(thread);
    }

    private static Map<ElementType, Map<String, List<String>>> newTypeMap() {
        Map<ElementType, Map<String, List<String>>> m =
                new EnumMap<ElementType, Map<String, List<String>>>(ElementType.class);
        for (ElementType t : ElementType.values()) {
            m.put(t, new LinkedHashMap<String, List<String>>());
        }
        return m;
    }
}
