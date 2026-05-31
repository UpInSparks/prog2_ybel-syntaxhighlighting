package highlighting.presets;

import highlighting.regex.Token;
import java.util.List;
import java.util.regex.Pattern;

public final class MiniJavaTokens {

public static List<Token> defaultTokens() {
        return List.of(

            // JavaDoc
            Token.of(
                Pattern.compile("/\\*\\*.*?\\*/", Pattern.DOTALL),
                MiniJavaColours.JAVADOC_COMMENT_COLOUR
            ),

            // block comment
            Token.of(
                Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL),
                MiniJavaColours.LINE_COMMENT_COLOUR
            ),

            // comment
            Token.of(
                Pattern.compile("//[^\\n\\r]*"),
                MiniJavaColours.LINE_COMMENT_COLOUR
            ),

            // string " .... "
            Token.of(
                Pattern.compile("\"(?:[^\"\\\\]|\\\\.)*\""),
                MiniJavaColours.STRING_LITERAL_COLOUR
            ),
            // ' .... '
            Token.of(
                Pattern.compile("'(?:[^'\\\\]|\\\\.)'"),
                MiniJavaColours.STRING_LITERAL_COLOUR   // gleiche Farbe wie Strings
            ),
            // annotation
            Token.of(
                Pattern.compile("@\\w+"),
                MiniJavaColours.ANNOTATION_COLOUR
            ),
            // Keywords
            Token.of(
                Pattern.compile(
                    "\\b(package|import|class|interface|enum|extends|implements|"
                    + "public|private|static|abstract|"
                    + "return|null|"
                    + "if|else|while|for"
                    + "int|long|double|float|boolean|char|byte|void|"
                    + "true|false)\\b"
                ),
                MiniJavaColours.KEYWORD_COLOUR
            ),
            // numbers
            Token.of(
                Pattern.compile("\\b\\d+\\b"),
                MiniJavaColours.KEYWORD_COLOUR
            )

        );
    }
}
