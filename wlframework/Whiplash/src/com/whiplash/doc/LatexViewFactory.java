package com.whiplash.doc;

import javax.swing.text.*;

/**
 * Creates views for Latex documents.
 * @author Matthias Thimm
 */
public class LatexViewFactory implements ViewFactory {

	/** Whether the created views support line wrap. */
	private boolean lineWrap = true;
	
	/** Creates a new view factory that creates views that
	 * support line wrap (if the given parameter is true)
	 * or not (otherwise).
	 * @param lineWrap whether to support line wrap.
	 */
	public LatexViewFactory(boolean lineWrap){
		this.lineWrap = lineWrap;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.text.ViewFactory#create(javax.swing.text.Element)
	 */
	@Override
	public View create(Element arg0) {
		if(this.lineWrap)
			return new LatexWrappedView(arg0);
		return  new LatexPlainView(arg0);
	}

}
