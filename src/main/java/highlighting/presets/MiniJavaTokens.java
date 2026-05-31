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
                MiniJavaColours.JAVADOC_COMMENT
            ),
            // block comment
            Token.of(
                Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL),
                MiniJavaColours.COMMENT
            ),
            // line comment
            Token.of(
                Pattern.compile("//[^\\n\\r]*"),
                MiniJavaColours.COMMENT
            ),
            // string " .... "
            Token.of(
                Pattern.compile("\"(?:[^\"\\\\]|\\\\.)*\""),
                MiniJavaColours.STRING
            ),
            // char ' .... '
            Token.of(
                Pattern.compile("'(?:[^'\\\\]|\\\\.)'"),
                MiniJavaColours.STRING
            ),
            // annotation
            Token.of(
                Pattern.compile("@\\w+"),
                MiniJavaColours.ANNOTATION
            ),
            // keywords
            Token.of(
                Pattern.compile(
                    "\\b(package|import|class|interface|enum|extends|implements|"
                    + "public|private|static|abstract|"
                    + "return|null|"
                    + "if|else|while|for|"
                    + "int|long|double|float|boolean|char|byte|void|"
                    + "true|false)\\b"
                ),
                MiniJavaColours.KEYWORD
            ),
            // numbers
            Token.of(
                Pattern.compile("\\b\\d+\\b"),
                MiniJavaColours.NUMBER
            )
        );
    }
}
