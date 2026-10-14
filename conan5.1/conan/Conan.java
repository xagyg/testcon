// Author: Brad Long
// Original: 26 Sep 2001. Restructured into parser / generator classes.

package conan;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import conan.translator.Diagnostics;
import conan.translator.ScriptParser;
import conan.translator.codegen.DriverGenerator;

/**
 * ConAn translator entry point.
 *
 * <pre>
 *   java conan.Conan [DriverName [MonitorClass]] &lt; script
 * </pre>
 *
 * Reads a Roast-preprocessed ConAn script from standard input and writes the
 * test driver to {@code DriverName.java} (default {@code MonitorDriver}).
 * If {@code MonitorClass} is given it replaces the class named in every
 * {@code #cut}/{@code #monitor} command. Exits with status 1 on syntax errors.
 *
 * <p>Package layout:
 * <ul>
 *   <li>{@code conan} - this entry point</li>
 *   <li>{@code conan.runtime} - classes the generated drivers run on (clock, threads, helpers)</li>
 *   <li>{@code conan.translator} - script parsing and diagnostics</li>
 *   <li>{@code conan.translator.model} - parsed sequences, monitors, element types</li>
 *   <li>{@code conan.translator.codegen} - writes the driver source</li>
 * </ul>
 */
public final class Conan {

    private Conan() {}

    public static void main(String[] args) {
        String driverName = args.length == 0 ? "MonitorDriver" : args[0];
        String monitorOverride = args.length < 2 ? "" : args[1];

        Diagnostics diag = new Diagnostics(System.err);
        boolean failed = false;
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileOutputStream(driverName + ".java"));
            ScriptParser parser = new ScriptParser(
                    new BufferedReader(new InputStreamReader(System.in)),
                    new DriverGenerator(out), diag, driverName, monitorOverride);

            parser.parse();

            if (!diag.hasErrors()) {
                parser.finish();
            } else {
                diag.info("Syntax Errors: " + diag.syntaxErrorCount());
                failed = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            failed = true;
        } finally {
            if (out != null) out.close();
        }
        // non-zero exit lets build scripts stop before compiling a broken driver
        if (failed) System.exit(1);
    }
}
