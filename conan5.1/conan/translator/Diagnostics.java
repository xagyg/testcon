package conan.translator;

import java.io.PrintStream;

/** Collects syntax errors and warnings raised while translating a ConAn script. */
public final class Diagnostics {

    private final PrintStream err;
    private int syntaxErrors;

    public Diagnostics(PrintStream err) {
        this.err = err;
    }

    public void syntax(String message) {
        syntaxErrors++;
        err.println("Syntax error: " + message);
    }

    public void warn(String message) {
        err.println("Warning: " + message);
    }

    public void info(String message) {
        err.println(message);
    }

    public int syntaxErrorCount() {
        return syntaxErrors;
    }

    public boolean hasErrors() {
        return syntaxErrors > 0;
    }
}
