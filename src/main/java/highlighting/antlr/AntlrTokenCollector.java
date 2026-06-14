package highlighting.antlr;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.MiniJavaColours;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

public class AntlrTokenCollector extends SyntaxHighlighter {

    @Override
    public List<HighlightRegion> collectMatches(String text) {
        List<HighlightRegion> result = new ArrayList<>();

        // Eingabetext -> CharStream -> MiniJavaLexer
        org.antlr.v4.runtime.CharStream input = CharStreams.fromString(text);
        MiniJavaLexer lexer = new MiniJavaLexer(input);

        // -1 = alle Kanäle inkl. HIDDEN (Kommentare)
        CommonTokenStream tokens = new CommonTokenStream(lexer, -1);
        tokens.fill();

        Token prevToken = null;

        for (Token token : tokens.getTokens()) {
            if (token.getType() == Token.EOF) continue;

            int start = token.getStartIndex();
            int end   = token.getStopIndex() + 1; // halb-offen [start, end)
            int type  = token.getType();

            // Kommentare (HIDDEN-Kanal)
            if (type == MiniJavaLexer.LINE_COMMENT || type == MiniJavaLexer.BLOCK_COMMENT) {
                result.add(new HighlightRegion(start, end, MiniJavaColours.COMMENT));

            } else if (type == MiniJavaLexer.JAVADOC_COMMENT) {
                result.add(new HighlightRegion(start, end, MiniJavaColours.JAVADOC_COMMENT));

                // String- und Char-Literale
            } else if (type == MiniJavaLexer.STRING_LITERAL || type == MiniJavaLexer.CHAR_LITERAL) {
                result.add(new HighlightRegion(start, end, MiniJavaColours.STRING));

                // Annotationen: AT-Token merken, Region erst beim IDENTIFIER schließen
            } else if (type == MiniJavaLexer.AT) {
                prevToken = token;
                continue;

            } else if (type == MiniJavaLexer.IDENTIFIER
                && prevToken != null
                && prevToken.getType() == MiniJavaLexer.AT) {
                // '@' + IDENTIFIER -> eine gemeinsame Annotation Region
                result.add(new HighlightRegion(prevToken.getStartIndex(), end, MiniJavaColours.ANNOTATION));
                prevToken = null;
                continue;

                // Keyword
            } else if (isKeyword(type)) {
                result.add(new HighlightRegion(start, end, MiniJavaColours.KEYWORD));
            }

            // AT ohne folgendes IDENTIFIER verwerfen
            if (prevToken != null && prevToken.getType() == MiniJavaLexer.AT) {
                prevToken = null;
            }
            prevToken = token;
        }

        return result;
    }

    // normalize und resolveConflicts werden nicht überschrieben:
    // Der ANTLR-Lexer arbeitet strikt links-nach-rechts (longest-match),
    // Überlappungen sind strukturell ausgeschlossen.

    private boolean isKeyword(int type) {
        return type == MiniJavaLexer.PACKAGE
            || type == MiniJavaLexer.IMPORT
            || type == MiniJavaLexer.CLASS
            || type == MiniJavaLexer.PUBLIC
            || type == MiniJavaLexer.PRIVATE
            || type == MiniJavaLexer.FINAL
            || type == MiniJavaLexer.RETURN
            || type == MiniJavaLexer.NULL
            || type == MiniJavaLexer.NEW
            || type == MiniJavaLexer.IF
            || type == MiniJavaLexer.ELSE
            || type == MiniJavaLexer.WHILE
            || type == MiniJavaLexer.EXTENDS
            || type == MiniJavaLexer.IMPLEMENTS;
    }
}
