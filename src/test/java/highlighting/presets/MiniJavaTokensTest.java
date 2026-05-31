package highlighting.presets;

import highlighting.regex.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;

class MiniJavaTokensTest {

    private List<Token> tokens;

    @BeforeEach
    void setUp() {
        tokens = MiniJavaTokens.defaultTokens();
    }

    private Token firstMatchingToken(String input) {
        for (Token token : tokens) {
            if (token.pattern().matcher(input).find()) return token;
        }
        return null;
    }

    @Test
    void javadocComment_isHighlightedAsJavadoc() {
        // Given a JavaDoc comment
        String input = "/** This is a JavaDoc comment */";

        // When the first matching token is determined
        Token match = firstMatchingToken(input);

        // Then it should be highlighted as JAVADOC_COMMENT
        assertNotNull(match);
        assertEquals(MiniJavaColours.JAVADOC_COMMENT, match.colour());
    }

    @Test
    void blockComment_isHighlightedAsComment() {
        // Given a block comment
        String input = "/* this is a block comment */";

        // When the first matching token is determined
        Token match = firstMatchingToken(input);

        // Then it should be highlighted as COMMENT
        assertNotNull(match);
        assertEquals(MiniJavaColours.COMMENT, match.colour());
    }

    @Test
    void lineComment_isHighlightedAsComment() {
        // Given a line comment
        String input = "// this is a comment";

        // When the first matching token is determined
        Token match = firstMatchingToken(input);

        // Then it should be highlighted as COMMENT
        assertNotNull(match);
        assertEquals(MiniJavaColours.COMMENT, match.colour());
    }

    @Test
    void stringLiteral_isHighlightedAsString() {
        // Given a double-quoted string
        String input = "\"hello world\"";

        // When the first matching token is determined
        Token match = firstMatchingToken(input);

        // Then it should be highlighted as STRING
        assertNotNull(match);
        assertEquals(MiniJavaColours.STRING, match.colour());
    }

    @Test
    void charLiteral_isHighlightedAsString() {
        // Given a char literal
        String input = "'a'";

        // When the first matching token is determined
        Token match = firstMatchingToken(input);

        // Then it should be highlighted as STRING
        assertNotNull(match);
        assertEquals(MiniJavaColours.STRING, match.colour());
    }

    @Test
    void annotation_isHighlightedAsAnnotation() {
        // Given an annotation
        String input = "@Override";

        // When the first matching token is determined
        Token match = firstMatchingToken(input);

        // Then it should be highlighted as ANNOTATION
        assertNotNull(match);
        assertEquals(MiniJavaColours.ANNOTATION, match.colour());
    }

    @Test
    void keyword_isHighlightedAsKeyword() {
        // Given a keyword
        String input = "class";

        // When the first matching token is determined
        Token match = firstMatchingToken(input);

        // Then it should be highlighted as KEYWORD
        assertNotNull(match);
        assertEquals(MiniJavaColours.KEYWORD, match.colour());
    }

    @Test
    void number_isHighlightedAsNumber() {
        // Given a numeric literal
        String input = "42";

        // When the first matching token is determined
        Token match = firstMatchingToken(input);

        // Then it should be highlighted as NUMBER
        assertNotNull(match);
        assertEquals(MiniJavaColours.NUMBER, match.colour());
    }

    @Test
    void defaultTokens_listIsNotEmpty() {
        // Given the default token list
        // When its size is checked
        // Then it must contain at least one token
        assertFalse(tokens.isEmpty());
    }

    // =========================================================================
    // Position / multiplicity tests (keyword token used as representative)
    // =========================================================================

    private Token keywordToken() {
        return tokens.stream()
                .filter(t -> t.colour() == MiniJavaColours.KEYWORD)
                .findFirst()
                .orElseThrow();
    }

    @Test
    void keyword_matchesAtStartOfText() {
        // Given a text where the keyword appears right at the beginning
        String input = "class Foo {}";

        // When the pattern is applied
        Matcher m = keywordToken().pattern().matcher(input);

        // Then a match is found at index 0
        assertTrue(m.find());
        assertEquals(0, m.start());
        assertEquals("class", m.group());
    }

    @Test
    void keyword_matchesInMiddleAndAtEndOfText() {
        // Given a text where keywords appear in the middle and at the end
        String input = "Foo extends Bar implements Baz { void";

        // When all matches are collected
        Matcher m = keywordToken().pattern().matcher(input);
        List<String> matches = new ArrayList<>();
        while (m.find()) matches.add(m.group());

        // Then "extends"/"implements" (middle) and "void" (end) are all found
        assertTrue(matches.contains("extends"));
        assertTrue(matches.contains("implements"));
        assertTrue(matches.contains("void"));
    }

    @Test
    void keyword_multipleMatchesInSameText() {
        // Given a text containing several keywords
        String input = "public static int x; private boolean flag;";

        // When all matches are collected
        Matcher m = keywordToken().pattern().matcher(input);
        List<String> matches = new ArrayList<>();
        while (m.find()) matches.add(m.group());

        // Then all five keywords are found
        assertEquals(5, matches.size());
        assertTrue(matches.containsAll(List.of("public", "static", "int", "private", "boolean")));
    }

    @Test
    void keyword_noMatchInPlainText() {
        // Given a text that contains no MiniJava keywords
        String input = "foo bar baz";

        // When the pattern is applied
        Matcher m = keywordToken().pattern().matcher(input);

        // Then no match is found
        assertFalse(m.find());
    }
}