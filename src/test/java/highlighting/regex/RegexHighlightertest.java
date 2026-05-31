package highlighting.regex;

import static org.junit.jupiter.api.Assertions.*;

import highlighting.core.HighlightRegion;
import highlighting.presets.MiniJavaColours;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegexHighlighterTest {

  private RegexHighlighter highlighter;

  @BeforeEach
  void setUp() {
    highlighter = new RegexHighlighter();
  }

  // helper: run the full pipeline
  private List<HighlightRegion> highlight(String text) {
    return highlighter.computeRegions(text);
  }

  // collectMatches

  @Test
  void collectMatches_emptyText_returnsEmptyList() {
    // Given an empty string
    // When matches are collected
    List<HighlightRegion> result = highlighter.collectMatches("");
    // Then no regions are returned
    assertTrue(result.isEmpty());
  }

  @Test
  void collectMatches_noTokenMatches_returnsEmptyList() {
    // Given plain text with no MiniJavatokens
    // When matches are collected
    List<HighlightRegion> result = highlighter.collectMatches("foo bar baz");
    // Then no regions are returned
    assertTrue(result.isEmpty());
  }

  @Test
  void collectMatches_singleKeyword_returnsOneRegion() {
    // Given a text with exactly one keyword
    // When matches are collected
    List<HighlightRegion> result = highlighter.collectMatches("class");
    // Then exactly one region with colour KEYWORD is returned
    assertEquals(1, result.size());
    assertEquals(MiniJavaColours.KEYWORD, result.get(0).colour());
  }

  // resolveConflicts

  @Test
  void resolveConflicts_noOverlap_keepsBothRegions() {
    // Given two non-overlapping regions [0,5) and [5,10)
    List<HighlightRegion> input =
        List.of(
            new HighlightRegion(0, 5, MiniJavaColours.KEYWORD),
            new HighlightRegion(5, 10, MiniJavaColours.NUMBER));
    // When conflicts are resolved
    List<HighlightRegion> result = highlighter.resolveConflicts(input);
    // Then both regions are kept
    assertEquals(2, result.size());
  }

  @Test
  void resolveConflicts_overlapping_keepsFirstDiscardsSecond() {
    // Given two overlapping regions [0,10) and [3,7) – first wins
    List<HighlightRegion> input =
        List.of(
            new HighlightRegion(0, 10, MiniJavaColours.COMMENT),
            new HighlightRegion(3, 7, MiniJavaColours.KEYWORD));
    // When conflicts are resolved
    List<HighlightRegion> result = highlighter.resolveConflicts(input);
    // Then only the first (comment) region survives
    assertEquals(1, result.size());
    assertEquals(MiniJavaColours.COMMENT, result.get(0).colour());
  }

  // computeRegions

  @Test
  void pipeline_keywordInsideLineComment_onlyCommentRegionSurvives() {
    // Given a line comment containing the keyword "int"
    String text = "// int x";
    // When the full pipeline runs
    List<HighlightRegion> result = highlight(text);
    // Then only the comment region survives (keyword is inside the comment)
    assertTrue(
        result.stream().allMatch(r -> r.colour() == MiniJavaColours.COMMENT),
        "only COMMENT regions expected; keyword should be discarded");
  }

  @Test
  void pipeline_javadocNotSplitByBlockCommentToken() {
    // Given a JavaDoc comment (also matchable by the block-comment token)
    String text = "/** doc */";
    // When the full pipeline runs
    List<HighlightRegion> result = highlight(text);
    // Then only one region exists and it is JAVADOC_COMMENT, not plain COMMENT
    assertEquals(1, result.size());
    assertEquals(MiniJavaColours.JAVADOC_COMMENT, result.get(0).colour());
  }

  @Test
  void pipeline_emptyText_returnsEmptyList() {
    // Given an empty string
    // When the full pipeline runs
    List<HighlightRegion> result = highlight("");
    // Then no regions are returned
    assertTrue(result.isEmpty());
  }
}
