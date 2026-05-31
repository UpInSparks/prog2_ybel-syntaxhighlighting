package highlighting.presets;

import java.awt.*;

public final class MiniJavaColours {
    // Originale Konstanten
    public static final Color STRING_LITERAL_COLOUR   = Color.ORANGE;
    public static final Color CHAR_LITERAL_COLOUR     = Color.MAGENTA;
    public static final Color KEYWORD_COLOUR          = Color.BLUE;
    public static final Color ANNOTATION_COLOUR       = new Color(128, 0, 128);
    public static final Color LINE_COMMENT_COLOUR     = new Color(0, 128, 0);
    public static final Color BLOCK_COMMENT_COLOUR    = new Color(0, 160, 0);
    public static final Color JAVADOC_COMMENT_COLOUR  = new Color(0, 160, 128);
    public static final Color NUMBER_COLOUR           = new Color(100, 100, 200);

    // Aliase – werden von MiniJavaTokens und den Tests erwartet
    public static final Color JAVADOC_COMMENT = JAVADOC_COMMENT_COLOUR;
    public static final Color COMMENT         = LINE_COMMENT_COLOUR;
    public static final Color STRING          = STRING_LITERAL_COLOUR;
    public static final Color ANNOTATION      = ANNOTATION_COLOUR;
    public static final Color KEYWORD         = KEYWORD_COLOUR;
    public static final Color NUMBER          = NUMBER_COLOUR;
}}
