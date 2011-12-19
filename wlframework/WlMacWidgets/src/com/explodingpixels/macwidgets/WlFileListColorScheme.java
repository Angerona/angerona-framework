package com.explodingpixels.macwidgets;

import java.awt.*;

/**
 * A color scheme for file lists. 
 * @author Matthias Thimm
 */
public class WlFileListColorScheme extends SourceListStandardColorScheme {

    /* (non-Javadoc)
     * @see com.explodingpixels.macwidgets.SourceListStandardColorScheme#getActiveBackgroundColor()
     */
	@Override
    public Color getActiveBackgroundColor() {
        return Color.white;
    }

    /* (non-Javadoc)
     * @see com.explodingpixels.macwidgets.SourceListStandardColorScheme#getInactiveBackgroundColor()
     */
	@Override
    public Color getInactiveBackgroundColor() {
        return new Color(0.97f,0.97f,0.97f);
    }

}
