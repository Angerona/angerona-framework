package com.whiplash.gui;

import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

import org.jpedal.*;
import org.jpedal.exception.*;

/**
 * This component is for displaying PDF documents.
 * @author Matthias Thimm
 */
public class WlPdfPreview extends WlPreview {

    /** For serialization.  */
	private static final long serialVersionUID = 1L;
	
	public WlPdfPreview(){
		super();
		try {
			PdfDecoder decoder = new PdfDecoder();
			decoder.useHiResScreenDisplay(true);
			//decoder.openPdfFile("SOME PDF FILE");
			this.setLayout(new BorderLayout());
			BufferedImage img = decoder.getPageAsImage(1);
			//panel.getGraphics().
			JLabel label = new JLabel(new ImageIcon( img ));			
			this.add(label);
			
		} catch (PdfException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.gui.WlComponent#getTitle()
	 */
	@Override
	public String getTitle() {
		return "test";
	}
	
}
