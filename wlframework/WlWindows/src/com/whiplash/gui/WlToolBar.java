package com.whiplash.gui;

import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import com.explodingpixels.macwidgets.*;
import com.whiplash.gui.laf.*;

/**
 * This class implements a tool bar for WlWindows.
 * @author Matthias Thimm
 */
public class WlToolBar extends UnifiedToolBar implements MouseInputListener, WlActionTrigger {
	
	/** The parent window of this tool bar. */
	private WlWindow parent = null;

	/** For dragging this tool bar. */
	private WlDragTools dragTools = null;
	
	/** The action listener of this tool bar. */
	private ActionListener actionListener;
	
	/** A map from action commands to the corresponding button. */
	private Map<String,JButton> buttons;
	
	/** Creates a new tool bar.
	 * @param actionListener an action listener. */
	public WlToolBar(ActionListener actionListener){
		super();		
		this.actionListener = actionListener;
		this.buttons = new HashMap<String,JButton>();
		//this.setFloatable(false);
		//this.setRollover(true);
		//this.setAlignmentX(Component.LEFT_ALIGNMENT);		
		this.getComponent().addMouseListener(this);
		this.getComponent().addMouseMotionListener(this);
	}
	
	/** Sets the parent window of this tool bar. 
	 * @param parent a window.
	 */
	protected void setParentWindow(WlWindow parent){
		this.parent = parent;		
	}	
	
	/** Adds a separator. */
	public void addSeparator(){
		//TODO
	}
	
	/** Adds a new button to this tool bar with the given properties.
	 * @param title the title of the button (used in tool tips).
	 * @param image the image for the button when hovered.
	 * @param imageShaded the standard image for the button.
	 * @param actionCommand the action command of the button.
	 */
	public void addButton(String title, Icon image, Icon imageShaded, String actionCommand){
		JButton button = WlLookAndFeel.createToolbarWlButton(title, image, imageShaded, this.actionListener, actionCommand);
		button.putClientProperty("JButton.buttonType", "textured");
		this.addComponentToLeft(button);	
		this.buttons.put(actionCommand, button);
		//this.setMaximumSize(new Dimension(10000,10000));
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.gui.WlActionTrigger#setEnabled(java.lang.String, boolean, java.lang.Object)
	 */
	@Override
	public void setEnabled(String actionCommand, boolean value, Object obj){
		if(this.buttons.containsKey(actionCommand))
			this.buttons.get(actionCommand).setEnabled(value);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) { }

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) { }

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if(this.dragTools != null && this.dragTools.isDragging()){
			// remove drag window
			this.dragTools.clean();
			if(this.parent.getWindowSet().isOutsideAnyWindow(e.getLocationOnScreen())){
				// create new window			
				WlWindow window = this.parent.getWindowSet().createWindow(this.parent.getWindowTitle(),e.getLocationOnScreen());
				this.parent.removeToolBar(this);
				window.addToolBar(this);			
				//window.setPreferredSize(this.getSize());
				window.refreshMinimumSize();
				window.pack();
				window.setVisible(true);
			}else{
				WlWindow window = this.parent.getWindowSet().getWindowOfLocation(e.getLocationOnScreen());
				if(window != this.parent){
					this.parent.removeToolBar(this);						
					window.addToolBar(this);
					window.refreshMinimumSize();
				}
			}			
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) { }

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		/*if(this.parent != null){
			if(this.dragTools == null)
				this.dragTools = new WlDragTools(this.parent.getWindowSet(),false,false);
			if(!this.dragTools.isVisible()){
				Image totalImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
	            Graphics totalGraphics = totalImage.getGraphics();            
	            this.paintComponents(totalGraphics);	            
				this.dragTools.show(totalImage, "", new Point(e.getLocationOnScreen().x + 10, e.getLocationOnScreen().y + 10));
			}
			this.dragTools.mouseDragged(e,this);
		}*/
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent e) { }
	
}
