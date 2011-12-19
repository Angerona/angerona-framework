package com.whiplash.doc;

import javax.swing.text.*;

/**
 * The editor kit for latex documents (handles syntax highlighting)
 * @author Matthias Thimm
 */
public class LatexEditorKit extends StyledEditorKit {

	/** The content type for Latex documents. */
	public static final String LATEX_CONTENT_TYPE = "text/latex";
	
	/** For serialization.  */
	private static final long serialVersionUID = 1L;

	/** The view factory of this editor kit. */
	private ViewFactory latexViewFactory;

    /** Creates a new editor kit.
     * @param linewrap whether to support line wrap. */
    public LatexEditorKit(boolean linewrap) {
    	this.latexViewFactory = new LatexViewFactory(linewrap);
    }
    
    /* (non-Javadoc)
     * @see javax.swing.text.StyledEditorKit#getViewFactory()
     */
    @Override
    public ViewFactory getViewFactory() {
        return this.latexViewFactory;
    }

    /* (non-Javadoc)
     * @see javax.swing.text.DefaultEditorKit#getContentType()
     */
    @Override
    public String getContentType() {
        return LatexEditorKit.LATEX_CONTENT_TYPE;
    }

}
