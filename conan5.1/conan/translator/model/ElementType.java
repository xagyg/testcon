package conan.translator.model;

/**
 * Element types allowed in {@code #<type>List} and {@code #<type>Array} commands.
 * Declaration order is the order in which collections are emitted.
 */
public enum ElementType {
    INT("int"),
    LONG("long"),
    SHORT("short"),
    DOUBLE("double"),
    FLOAT("float"),
    BYTE("byte"),
    CHAR("char"),
    STRING("String");

    public final String javaName;

    ElementType(String javaName) {
        this.javaName = javaName;
    }

    /** Returns the type with the given Java name, or {@code null} if unsupported. */
    public static ElementType fromJavaName(String name) {
        for (ElementType t : values()) {
            if (t.javaName.equals(name)) return t;
        }
        return null;
    }
}
