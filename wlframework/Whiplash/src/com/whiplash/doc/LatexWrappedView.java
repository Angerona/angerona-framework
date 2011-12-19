package com.whiplash.doc;

import java.awt.*;
import java.util.*;
import java.util.regex.*;

import javax.swing.text.*;

/**
 * Instances of this class are views with line wrap for Latex documents.
 * @author Matthias Thimm
 */
public class LatexWrappedView extends WrappedPlainView {
	
    /** Creates a new view for the given element.
	 * @param arg0 some element.
	 */
	public LatexWrappedView(Element arg0) {
		super(arg0,true);
		this.getDocument().putProperty(PlainDocument.tabSizeAttribute, 2);
	}

	/** Retrieves the largest integer i with i < offs and
	 * the character at offset i in the given document is "\n".
	 * @param doc some document
	 * @param offs some offset
	 * @return the offset of the position or "-1" if there is none.
	 * @throws BadLocationException 
	 */
	private int getLastLinebreak(Document doc, int offs) throws BadLocationException{
		String text = doc.getText(0, offs - 1);
		return text.lastIndexOf("\n");		 
	}

	/* (non-Javadoc)
	 * @see javax.swing.text.WrappedPlainView#drawUnselectedText(java.awt.Graphics, int, int, int, int)
	 */
	@Override
    protected int drawUnselectedText(Graphics graphics, int x, int y, int p0, int p1) throws BadLocationException {
        Document doc = this.getDocument();
        String text = doc.getText(p0, p1 - p0);
        SortedMap<Integer, Integer> startMap = new TreeMap<Integer, Integer>();
        SortedMap<Integer, Color> colorMap = new TreeMap<Integer, Color>();
        if(p0 > 0){
        	if(!doc.getText(p0-1, 1).equals("\n")){
        		// check whether there was a comment in the previous line
        		int idx = this.getLastLinebreak(doc, p0 - 1);        		
       			String lastLine = doc.getText(idx+1, p0-idx-1);        			
       			if(lastLine.startsWith("%") || Pattern.compile("[^\\\\]%").matcher(lastLine).find()){
       				int finalLineBreak = text.indexOf("\n");
       				if(finalLineBreak == -1)
       					finalLineBreak = text.length();
       				startMap.put(0, finalLineBreak);
       				colorMap.put(0, LatexSyntaxHighlighting.COMMENT_COLOR);
       			}        			        		
        	}        	
        }
        Segment segment = this.getLineBuffer();        
        // Match all regexes on this snippet, store positions
        for (Map.Entry<Pattern, Color> entry : LatexSyntaxHighlighting.patternColors.entrySet()) {
            Matcher matcher = entry.getKey().matcher(text);
            while (matcher.find()) {            	
                startMap.put(matcher.start(1), matcher.end());
                colorMap.put(matcher.start(1), entry.getValue());                
            }
        }        
        int i = 0;
        // Colour the parts
        for (Map.Entry<Integer, Integer> entry : startMap.entrySet()) {
        	int start = entry.getKey();
            int end = entry.getValue();            
            if(i >= end) continue;
            if (i < start) {
                graphics.setColor(Color.black);
                doc.getText(p0 + i, start - i, segment);
                x = Utilities.drawTabbedText(segment, x, y, graphics, this, i);
            }
            graphics.setColor(colorMap.get(start));
            i = end;
            doc.getText(p0 + start, i - start, segment);
            x = Utilities.drawTabbedText(segment, x, y, graphics, this, start);
        }
        // Paint possible remaining text black
        if (i < text.length()) {
            graphics.setColor(Color.black);
            doc.getText(p0 + i, text.length() - i, segment);
            x = Utilities.drawTabbedText(segment, x, y, graphics, this, i);
        }
        return x;
    }	
}
