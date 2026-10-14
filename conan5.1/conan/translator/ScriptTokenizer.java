package conan.translator;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Thin wrapper around {@link StreamTokenizer} configured for the ConAn script
 * syntax. Commands start with the {@link #CONTROL} character; everything else is
 * treated as words, quoted literals or (optionally) end-of-line markers.
 */
final class ScriptTokenizer {

    static final String CONTROL = "#";
    static final String EOL = "TT_EOL";

    private static final String NL = System.lineSeparator();

    private final StreamTokenizer st;
    private String current;

    ScriptTokenizer(Reader reader) {
        st = new StreamTokenizer(reader);
        configure();
    }

    private void configure() {
        st.ordinaryChars('0', '9');
        st.ordinaryChars('/', '/');
        st.ordinaryChar('\'');
        st.ordinaryChar('"');
        st.quoteChar('\'');
        st.quoteChar('"');
        if (!CONTROL.equals("!")) st.wordChars('!', '!');
        if (!CONTROL.equals("$")) st.wordChars('$', '$');
        if (!CONTROL.equals("#")) st.wordChars('#', '#');
        st.wordChars('%', '&');
        st.wordChars('(', '.');
        st.wordChars('0', '~');
        st.eolIsSignificant(false);
        st.slashSlashComments(true);
        st.slashStarComments(true);
    }

    /** Reads the next token; returns {@code null} at end of input. */
    String next() throws IOException {
        current = null;
        int type = st.nextToken();
        if (type != StreamTokenizer.TT_EOF) {
            if (type == StreamTokenizer.TT_EOL) {
                current = EOL;
            } else {
                current = (st.sval == null ? String.valueOf((char) st.ttype) : st.sval);
            }
        }
        return current;
    }

    /** True when the last token read was the control character or end of input. */
    boolean atControl() {
        return current == null || current.equals(CONTROL);
    }

    /** Re-applies the quotes the tokenizer stripped from the last token, if it was quoted. */
    String requote(String token) {
        char type = (char) st.ttype;
        if (type == '\'' || type == '"') {
            return type + token + type;
        }
        return token;
    }

    void eolSignificant(boolean flag) {
        st.eolIsSignificant(flag);
    }

    void ordinary(char... chars) {
        for (char c : chars) st.ordinaryChar(c);
    }

    void word(char... chars) {
        for (char c : chars) st.wordChars(c, c);
    }

    /**
     * Reads raw Java code up to the next control character, re-quoting literals
     * and turning line ends into platform line separators.
     */
    String readCode() throws IOException {
        StringBuilder code = new StringBuilder();
        st.eolIsSignificant(true);
        while (true) {
            String tok = next();
            if (atControl()) break;
            if (tok.equals(EOL)) tok = NL;
            code.append(requote(tok)).append(' ');
        }
        st.eolIsSignificant(false);
        return code.toString();
    }

    /** Reads words until the next control character. */
    List<String> readWords() throws IOException {
        List<String> words = new ArrayList<String>();
        while (true) {
            String tok = next();
            if (atControl()) break;
            if (!tok.equals("")) words.add(tok);
        }
        return words;
    }

    /**
     * Having just read {@code <}, reads an identifier and the closing {@code >}.
     * Returns the identifier, or {@code null} if the closing bracket is missing.
     */
    String readIdAfterOpenBracket() throws IOException {
        String id = next();
        String close = next();
        return ">".equals(close) ? id : null;
    }
}
