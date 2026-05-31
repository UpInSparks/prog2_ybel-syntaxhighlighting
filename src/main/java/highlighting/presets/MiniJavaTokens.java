package highlighting.presets;

import highlighting.regex.Token;
import java.util.List;
import java.util.regex.Pattern;

public final class MiniJavaTokens {

  // TODO (Phase I+II: RegexHighlighter/ScanningHighlighter)
  // TODO: Define the MiniJava tokens used by the highlighters. Each token is a mapping from a
  // regular expression to a colour (and, if applicable, a specific matching group). The order of
  // tokens in this list determines their relative priority during highlighting. One example token
  // definition is provided below; define the remaining tokens in an analogous way.

  // Basic token set for MiniJava. Extend this list with further tokens as needed (e.g. identifiers,
  // numeric literals, operators, brackets, whitespace), following the same pattern. Each token is
  // defined by a regular expression and a colour. Optionally, a specific capturing group within the
  // pattern can be selected as the "highlighted" region.
public static List<Token> defaultTokens() {
        return List.of(

            // JavaDoc
            Token.of(
                Pattern.compile("/\\*\\*.*?\\*/", Pattern.DOTALL),
                MiniJavaColours.JAVADOC_COMMENT
            );

            // block comment
                Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL),
                MiniJavaColours.COMMENT
            );

            // comment
            Token.of(
                Pattern.compile("//[^\\n\\r]*"),
                MiniJavaColours.COMMENT
            );

            // string " .... "
            Token.of(
                Pattern.compile("\"(?:[^\"\\\\]|\\\\.)*\""),
                MiniJavaColours.STRING
            );
            // ' .... '
            Token.of(
                Pattern.compile("'(?:[^'\\\\]|\\\\.)'"),
                MiniJavaColours.STRING   // gleiche Farbe wie Strings
            );
            // annotation
            Token.of(
                Pattern.compile("@\\w+"),
                MiniJavaColours.ANNOTATION
            );
            // Keywords
            Token.of(
                Pattern.compile(
                    "\\b(package|import|class|interface|enum|extends|implements|"
                    + "public|private|static|abstract|"
                    + "return|null|"
                    + "if|else|while|for"
                    + "int|long|double|float|boolean|char|byte|void|"
                    + "true|false)\\b"
                );
                MiniJavaColours.KEYWORD
            );
            // numbers
            Token.of(
                Pattern.compile("\\b\\d+\\b"),
                MiniJavaColours.NUMBER
            )

        );
    }
}
