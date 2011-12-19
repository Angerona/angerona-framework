package com.explodingpixels.widgets;

import com.explodingpixels.painter.ImagePainter;
import com.explodingpixels.swingx.EPPanel;

import java.awt.Dimension;
import java.awt.Image;

public class ImageBasedJComponent extends EPPanel {

	//ADDED BY MT
	private static final long serialVersionUID = 1L;
	
	private final ImagePainter fPainter;

    public ImageBasedJComponent(Image image) {
        fPainter = new ImagePainter(image);
        setBackgroundPainter(fPainter);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(fPainter.getImage().getWidth(null),
                fPainter.getImage().getHeight(null));
    }
}
