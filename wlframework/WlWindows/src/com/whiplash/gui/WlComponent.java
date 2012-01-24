package com.whiplash.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.whiplash.gui.events.*;
import com.whiplash.gui.laf.*;
import com.whiplash.util.*;

/**
 * This class represents the common ancestor for GUI components.
 * @author Matthias Thimm
 */
public abstract class WlComponent extends JPanel implements MouseListener {
	
	/** For serialization */
	private static final long serialVersionUID = 1L;

	/** The parent tabbed pane of this component. */
	private FancyTabbedPane parent = null;
	
	/** The tab associated with this component. */
	private FancyTabbedPaneTab tab;
	
	/** An application wide unique id. */
	private String id;
	
	/** An optional popup menu.*/
	private JPopupMenu popupMenu = null;
	
	/**
	 * Creates a new component.
	 */
	public WlComponent(){
		super();
		this.id = IdTools.getNextIdAsString();
		this.addMouseListener(this);		
		this.setBorder(WlLookAndFeel.innerComponentBorder());
		this.tab = new FancyTabbedPaneTab(this);
		this.tab.addMouseListener(this);
	}
	
	/** Returns the tab of this component.
	 * @return the tab of this component.
	 */
	protected FancyTabbedPaneTab getTab(){
		return this.tab;
	}
	
	/** Sets the popup menu for this component.
	 * @param menu a popup menu.
	 */
	public void setPopupMenu(JPopupMenu menu){
		this.popupMenu = menu;
	}
	
	/** Returns the application wide unique id of this component.
	 * @return the application wide unique id of this component.
	 */
	public String getId(){
		return this.id;
	}
	
	/** Returns the window set this component belongs to.
	 * @return the window set this component belongs to
	 * (or "null" if this component has no window set).
	 */
	public WlWindowSet getWindowSet(){
		if(this.getWindow() != null)
			return this.getWindow().getWindowSet();
		return null;
	}

	/** Returns the window this component belongs to.
	 * @return the window this component belongs to
	 * (or "null" if this component has no window).
	 */
	public WlWindow getWindow(){
		if(this.parent != null)
			return this.parent.getWindow();
		return null;
	}
	
	/**
	 * Returns the title of the component which is used for tab labels.
	 * @return the title of the component which is used for tab labels.
	 */
	public abstract String getTitle();
	
	/** Causes the listeners of this component to re-read this components title. */
	public void refreshTitle(){
		this.getWindowSet().fireWlComponentChangedTitleEvent(new WlComponentTitleChangedEvent(this));
	}
	
	/** Causes the listeners of this component to check the enable status of actions. */
	protected void refrestActionEnablement(){
		this.getWindowSet().fireWlComponentActionEnablementEvent(new WlComponentActionEnablementChangedEvent(this));
	}
	
	/** Checks whether this component may be closed
	 * (but does not closes it).
	 * @return "true" iff this component may be closed.
	 */
	public boolean mayClose(){
		return this.getWindowSet().requestClosing(this);		
	}
	
	/** 
	 * Closes this component (first asks closer controllers)
	 */
	public boolean requestClosing(){
		if(this.parent != null)
			return this.parent.closeComponent(this,false);
		return true;
	}
	
	/** Forces this component to close (closer controllers are not asked). */
	public void forceClose(){
		this.parent.closeComponent(this, true);
	}
	
	/** This method is called when the component has been closed in order
	 * to perform some cleanup operations. */
	protected void componentClosed(){ }
	
	/**
	 * Sets the parent tabbed pane of this component (possibly null).
	 * @param parent a fancy tabbed pane or "null" (if this component has no parent).
	 */
	protected void setParentPane(FancyTabbedPane parent){
		this.parent = parent;
	}
	
	/**
	 * Returns the parent tabbed pane of this component.
	 * @return the parent tabbed pane of this component.
	 */
	protected FancyTabbedPane getParentPane(){
		return this.parent;
	}
	
	/** This method is called when this component gains focus. */
	protected void focusGained() {		
		this.setBackground(WlLookAndFeel.COLOR_FOCUS_ACTIVE);
		this.requestFocus();
		for(Component c : this.getComponents()) {
			if(c instanceof FancyTabbedPane) {
				FancyTabbedPane ftp = (FancyTabbedPane)c;
				ftp.focusGained();
			} else if(c instanceof WlComponent) {
				WlComponent wc = (WlComponent)c;
				wc.focusGained();
			}
		}
		if(this.getWindowSet() != null)
			this.getWindowSet().fireWlComponentGainedFocusEvent(new WlComponentFocusGainedEvent(this));
	}
	
	/** This method is called when this component loses focus. */
	protected void focusLost() {	
		this.setBackground(WlLookAndFeel.COLOR_FOCUS_FOREGROUND);
		for(Component c : this.getComponents()) {
			if(c instanceof FancyTabbedPane) {
				FancyTabbedPane ftp = (FancyTabbedPane)c;
				ftp.focusLost();
			} else if(c instanceof WlComponent) {
				WlComponent wc = (WlComponent)c;
				wc.focusLost();
			}
		}
		if(this.parent != null && this.parent.getWindow() != null)
			this.parent.getWindow().getWindowSet().fireWlComponentFocusLostEvent(new WlComponentFocusLostEvent(this));
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);		
		this.doPaint(g);
	}	
	
	/** Does some extra painting.
	 * @param g some graphics.
	 */
	protected void doPaint(Graphics g){
		g.setColor(WlLookAndFeel.COLOR_LINE_BORDER);
		// draw line border		
		//g.drawLine(0, 0, 0, g.getClipBounds().height-1);
		//g.drawLine(g.getClipBounds().width-1, 0, g.getClipBounds().width-1, g.getClipBounds().height-1);
		//g.drawLine(0, g.getClipBounds().height-1, g.getClipBounds().width-1, g.getClipBounds().height-1);
		// for the upper edge get position of this component's tab (or draw a straight line if this component has no parent)
		if(this.parent == null)
			g.drawLine(0, 0, g.getClipBounds().width-1,0);
		else{
			g.drawLine(0, 0, this.tab.getLocation().x,0);
			g.drawLine(this.tab.getLocation().x + this.tab.getSize().width - 2, 0, g.getClipBounds().width-1, 0);
		}
	}

	/** Triggers the popup menu of this component.
	 * @param e some mouse event.
	 */
	private void triggerPopupMenu(MouseEvent e){
		if (e.isPopupTrigger() && this.popupMenu != null)
            this.popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if(this.parent != null)
			this.parent.activateTab(this.tab);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		this.triggerPopupMenu(e);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		this.triggerPopupMenu(e);
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
}
