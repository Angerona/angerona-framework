package com.whiplash.gui.laf;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.*;

/**
 * This class provides several methods that control the
 * look and feel for an application.
 * @author Matthias Thimm
 */
public abstract class WlLookAndFeel {

	/** Background color for tabs in background. */
	public final static Color COLOR_FOCUS_BACKGROUND = new Color(0.94f,0.94f,0.94f);
	/** Background color for tabs in foreground. */
	public final static Color COLOR_FOCUS_FOREGROUND = new Color(0.97f,0.97f,0.97f);
	/** Background color for active tabs. */
	public final static Color COLOR_FOCUS_ACTIVE = Color.white;
	/** Color for line borders. */
	public final static Color COLOR_LINE_BORDER = new Color(0.4f,0.4f,0.4f);
	/** Color for the outer line borders. */
	public final static Color COLOR_OUTER_LINE_BORDER = new Color(0.85f,0.85f,0.85f);
	/** The default background color of the line numbers area in the editor pane. */
	public final static Color DEFAULT_LINENUMBERSAREA_BACKGROUND_COLOR = new Color(0.98f,0.98f,0.98f);
	/** The default color for thin lines dividing two components. */
	public final static Color DEFAULT_THINLINE_COLOR = new Color(0.8f,0.8f,0.8f);
	/** Background color for background filler. */
	public final static Color COLOR_BACKGROUND_FILLER = Color.white;
	/** Default background color. */
	public final static Color COLOR_BACKGROUND_DEFAULT = new Color(0.913f,0.913f,0.913f);
	
	/**
	 * @return The inner border used for components
	 * (mainly to have space between the contents and the outer border).
	 */
	public static Border innerComponentBorder(){
		return null;// BorderFactory.createEmptyBorder(5, 5, 5, 5);
	}
	
	/**
	 *  @return The border for the preferences tool bar.
	 */
	public static Border preferencesToolbarBorder(){
		return BorderFactory.createMatteBorder(0, 0, 1, 0, WlLookAndFeel.COLOR_LINE_BORDER);
	}
	
	/** Creates a new borderless button in the Whiplash style.
	 * @param title the title of the button (used in tool tips).
	 * @param image the image for the button when hovered.
	 * @param imageShaded the standard image for the button.
	 * @param actionListener the action listener that should be informed on clicking the button.
	 * @param actionCommand the action command of the button.
	 */
	public static JButton createBorderlessWlButton(String title, Icon image, Icon imageShaded, ActionListener actionListener, String actionCommand){
		JButton button = new JButton(imageShaded);
		button.setUI(new BasicButtonUI());
		button.setContentAreaFilled(false);
		button.setFocusable(false);
		button.setRolloverEnabled(true);
		button.setBorder(BorderFactory.createEtchedBorder());
		button.setBorderPainted(false);
		button.setToolTipText(title);
		button.setRolloverIcon(image);
		button.setActionCommand(actionCommand);
		button.addActionListener(actionListener);
		return button;
	}
	
	/** Creates a new tool bar button in the Whiplash style.
	 * @param title the title of the button (used in tool tips).
	 * @param image the image for the button when hovered.
	 * @param imageShaded the standard image for the button.
	 * @param actionListener the action listener that should be informed on clicking the button.
	 * @param actionCommand the action command of the button.
	 */
	public static JButton createToolbarWlButton(String title, Icon image, Icon imageShaded, ActionListener actionListener, String actionCommand){
		JButton button = new JButton(imageShaded);
		button.setUI(new BasicButtonUI());
		button.setContentAreaFilled(false);
		button.setFocusable(false);
		button.setRolloverEnabled(true);
		button.setBorder(BorderFactory.createEtchedBorder());
		button.setBorderPainted(false);
		button.setToolTipText(title);
		button.setRolloverIcon(image);
		button.setActionCommand(actionCommand);
		button.addActionListener(actionListener);
		return button;
	}

	/** The border for tabbed panes.
	 * @return the border for tabbed panes.
	 */
	public static Border getTabbedPaneBorder(){
		return null;//BorderFactory.createLineBorder(WlLookAndFeel.COLOR_OUTER_LINE_BORDER);
	}
	
	/** Paints the border for a fancy tabbed pane tab
	 * (with the given location and orientation) to
	 * the given graphics.
	 * @param g some graphics.
	 * @param orientation one of SwingConstants.VERTICAL or SwingConstants.HORIZONTAL.
	 * @param location one of SwingConstants.EAST or SwingConstants.WEST.
	 */
	public static void paintTabBorder(Graphics g, int orientation, int location){
		g.setColor(WlLookAndFeel.COLOR_LINE_BORDER);
		if(orientation == SwingConstants.VERTICAL){		
			if(location == SwingConstants.EAST){
				g.drawLine(0, 0, 0, g.getClipBounds().height);
				g.setColor(WlLookAndFeel.COLOR_OUTER_LINE_BORDER);
				g.drawLine(1, 0, 1, g.getClipBounds().height);
				g.setColor(WlLookAndFeel.COLOR_BACKGROUND_DEFAULT);
				g.fillRect(1, 0, g.getClipBounds().width-1,g.getClipBounds().height);
			}else{
				//g.drawLine(0, 0, 0, g.getClipBounds().height);
			}
		}else{
			//g.drawLine(0, 0, g.getClipBounds().width-7,0);
			g.setColor(WlLookAndFeel.COLOR_OUTER_LINE_BORDER);
			g.drawLine(g.getClipBounds().width, 0,g.getClipBounds().width-1 , 0);
			
		}
	}	
}
