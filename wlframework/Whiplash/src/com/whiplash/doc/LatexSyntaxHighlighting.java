package com.whiplash.doc;

import java.awt.*;
import java.util.*;
import java.util.regex.*;

/**
 * This class provides some common functionality regarding syntax highlighting 
 * for Latex views.
 * @author Matthias Thimm
 */
public abstract class LatexSyntaxHighlighting {

	/** Maps patterns to colors. */
	protected static HashMap<Pattern, Color> patternColors;
	
	/** The comment pattern. */
	protected static String COMMENT_PATTERN = "[^\\\\](%.*$)";
	protected static String COMMENT_PATTERN2 = "^(%.*$)";
	/** The math delimiter pattern. */
	protected static String MATHDELIMITER_PATTERN = "[^\\\\](\\$)";
	protected static String MATHDELIMITER_PATTERN2 = "^(\\$)";
	/** The command pattern. */
	protected static String COMMAND_PATTERN = "(\\\\(([a-zA-Z]+)|([^\\d])))";
	/** protected curly bracket pattern. */
	private static String CURLYBRACKET_PATTERN = "([{]|[}])";
	
	/** The comment color. */
	protected static Color COMMENT_COLOR = new Color(254,59,23);
	/** The math delimiter color. */
	protected static Color MATHDELIMITER_COLOR = new Color(0,144,50);
	/** The command color. */
	protected static Color COMMAND_COLOR = new Color(34,60,251);
	/** The curly bracket color. */
	protected static Color CURLYBRACKET_COLOR = new Color(0,144,50);
	
    static {
        // NOTE: the order is important!
        patternColors = new HashMap<Pattern, Color>();
        patternColors.put(Pattern.compile(COMMENT_PATTERN), LatexSyntaxHighlighting.COMMENT_COLOR);
        patternColors.put(Pattern.compile(COMMENT_PATTERN2), LatexSyntaxHighlighting.COMMENT_COLOR);
        patternColors.put(Pattern.compile(MATHDELIMITER_PATTERN), LatexSyntaxHighlighting.MATHDELIMITER_COLOR);
        patternColors.put(Pattern.compile(MATHDELIMITER_PATTERN2), LatexSyntaxHighlighting.MATHDELIMITER_COLOR);
        patternColors.put(Pattern.compile(COMMAND_PATTERN), LatexSyntaxHighlighting.COMMAND_COLOR);
        patternColors.put(Pattern.compile(CURLYBRACKET_PATTERN), LatexSyntaxHighlighting.CURLYBRACKET_COLOR);
    }
}
