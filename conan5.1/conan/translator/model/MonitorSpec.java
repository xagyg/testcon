package conan.translator.model;

/** The component under test declared by {@code #cut} / {@code #monitor}. */
public final class MonitorSpec {

    /** Spec used when a sequence has no monitor; generates no monitor field. */
    public static final MonitorSpec NONE = new MonitorSpec("", "", "");

    public final String variable;
    public final String className;
    public final String constructorArgs;

    public MonitorSpec(String variable, String className, String constructorArgs) {
        this.variable = variable;
        this.className = className;
        this.constructorArgs = constructorArgs;
    }

    public boolean isPresent() {
        return !className.isEmpty();
    }
}
