package com.explodingpixels.border;

import com.explodingpixels.widgets.WindowUtils;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class FocusStateMatteBorder extends MatteBorder {

	//ADDED BY MT
	private static final long serialVersionUID = 1L;
	
    private final Color fFocusedColor;

    private final Color fUnfocusedColor;

    private final JComponent fComponentToTrackFocusOf;

    public FocusStateMatteBorder(int top, int left, int bottom, int right,
                                 Color focusedColor, Color unfocusedColor,
                                 JComponent componentToTrackFocusOf) {
        super(top, left, bottom, right, focusedColor);
        fFocusedColor = focusedColor;
        fUnfocusedColor = unfocusedColor;
        fComponentToTrackFocusOf = componentToTrackFocusOf;
        updateColor(true);
        WindowUtils.installWeakWindowFocusListener(fComponentToTrackFocusOf, createWindowFocusListener());
    }

    private void updateColor(boolean focused) {
        color = focused ? fFocusedColor : fUnfocusedColor;
        fComponentToTrackFocusOf.repaint();
    }

    private WindowFocusListener createWindowFocusListener() {
        return new WindowFocusListener() {
            public void windowGainedFocus(WindowEvent e) {
                updateColor(true);
            }

            public void windowLostFocus(WindowEvent e) {
                updateColor(false);
            }
        };
    }

}
