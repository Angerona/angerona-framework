package com.whiplash.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.whiplash.gui.events.*;
import com.whiplash.gui.laf.*;
import com.whiplash.util.*;

/**
 * A single window container, i.e. a separate application window.
 * @author Matthias Thimm, Tim Janus
 */
public class WlWindow extends JFrame implements WindowListener, WindowFocusListener {
	
	/**
	 * Implements a foreach callback for all panes in the WlWindow class
	 * @author Tim Janus
	 */
	private abstract class PANE_FOREACH {
		
		public PANE_FOREACH(Object... params) {
			for(int i=0; i<panes.length; ++i) {
				callback(panes[i], borderLayout[i], params);
			}
		}
		
		protected abstract void callback(FancyTabbedPane pane, String layout, Object... params);
	}
	
	/** The amount of pixels used to identify the border of this window. */
	public static final int BORDER_PADDING = 40;
	
	/*** For serialization.  */
	private static final long serialVersionUID = 1L;
	
	/** The window set this window belongs to. */
	private WlWindowSet windowSet;
	
	/** The main panel of this window. */
	private WlSplitPaneBorderPanel mainPanel;
	
	/** The panel for tabbed panes. */
	private WlSplitPaneBorderPanel tabbedPanesPanel;
	
	private static final int CENTER_PANE = 0;
	private static final int WEST_PANE = 1;
	private static final int EAST_PANE = 2;
	private static final int SOUTH_PANE = 3;
	private static final int PANE_COUNT = 4;
	
	private FancyTabbedPane [] panes = new FancyTabbedPane[PANE_COUNT];
	
	private String [] borderLayout = {BorderLayout.CENTER, 
			BorderLayout.WEST, 
			BorderLayout.EAST, 
			BorderLayout.SOUTH};
	
	/** The tool bar panel.*/
	private JPanel toolbarPanel = null;
	/** The list of tool bars. */
	private java.util.List<WlToolBar> toolBars = null;
	
	/** The currently active pane. */
	private FancyTabbedPane activePane = null;
	
	/** A map mapping a tabbed pane location to true/false,
	 * indicating whether the named pane should be disposed when
	 * empty. */
	private Map<String,Boolean> disposePanes;
	
	/** The title of this window */
	private String title;
	/** Some supplement for the title. */	
	private String titleSupplement;
	
	/** The size of this window in the user state (see Apple HIG).*/
	private Dimension userSize;
	/** The location of this window in the user state (see Apple HIG).*/
	private Point userLocation;
	
	/** Creates a new window with the given title.
	 * @param title a string.
	 * */
	public WlWindow(WlWindowSet windowSet, String title){
		super(title);
		this.windowSet = windowSet;
		this.title = title;		
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		// init default values for disposal of tabbed panes
		this.disposePanes = new HashMap<String,Boolean>();
		new PANE_FOREACH() {
			@Override
			public void callback(FancyTabbedPane pane, String layout, Object... params) {
				disposePanes.put(layout, true);
			}
		};
		// the layout is always a border layout.		
		this.tabbedPanesPanel = new WlSplitPaneBorderPanel();
		this.tabbedPanesPanel.setBorder(null);	//BorderFactory.createEmptyBorder(3, 3, 3, 3));
		this.mainPanel = new WlSplitPaneBorderPanel();
		this.mainPanel.setResizeEnabled(BorderLayout.NORTH, false);
		this.setLayout(new BorderLayout(0,0));		
		this.mainPanel.add(this.tabbedPanesPanel, BorderLayout.CENTER);
		this.add(this.mainPanel,BorderLayout.CENTER);
		// init tool bar panel
		this.toolbarPanel = new JPanel();
		this.toolbarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, WlLookAndFeel.COLOR_LINE_BORDER));
		this.toolbarPanel.setLayout(new BoxLayout(this.toolbarPanel,BoxLayout.PAGE_AXIS));
		this.mainPanel.add(this.toolbarPanel,BorderLayout.NORTH);
		this.toolBars = new java.util.LinkedList<WlToolBar>();
		// enable focus events
		this.addWindowFocusListener(this);
		this.enableEvents(AWTEvent.FOCUS_EVENT_MASK);		
		// add window listener (to catch window closing events)
		this.addWindowListener(this);
	}	
	
	/** Sets the "dispose when empty" value for the given
	 * pane.
	 * @param location some BorderLayout constraint.
	 * @param value whether the given pane should be disposed when empty.
	 */
	public void setDisposeWhenEmpty(String location, boolean value){
		this.disposePanes.put(location, value);
		new PANE_FOREACH(location, new Boolean(value)) {
			@Override
			public void callback(FancyTabbedPane pane, String layout, Object... params) {
				if((String)params[0] == layout && pane != null) 
					pane.setDisposeWhenEmpty((Boolean)params[1]);
			}
		};
	}

	/** Toggles between the standard location and size of the window
	 * and the user location and size. */
	public void zoom(){
		Dimension standardSize = new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width*2/3, Toolkit.getDefaultToolkit().getScreenSize().height*2/3);
		Point standardLocation = new Point(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - standardSize.width / 2, (Toolkit.getDefaultToolkit().getScreenSize().height / 2 - standardSize.height / 2) /2 );
		if(this.getSize().equals(standardSize) && this.getLocationOnScreen().equals(standardLocation)){
			this.setLocation(this.userLocation);
			this.setSize(this.userSize);
		}else{
			this.userSize = this.getSize();
			if(this.isShowing())				
				this.userLocation = this.getLocationOnScreen();			
			this.setLocation(standardLocation);
			this.setSize(standardSize);
		}
		this.validate();
	}
	
	/** Adds the given tool bar to this window.
	 * @param toolBar a tool bar.
	 */
	public void addToolBar(WlToolBar toolBar){
		this.toolbarPanel.setBorder(null);					
		this.toolBars.add(toolBar);
		this.toolbarPanel.add(toolBar.getComponent());
		toolBar.installWindowDraggerOnWindow(this);
		toolBar.setParentWindow(this);
	}
	
	/** Removes the given tool bar from this window.
	 * @param toolBar a tool bar.
	 */
	public void removeToolBar(WlToolBar toolBar){
		toolBar.setParentWindow(null);
		this.toolBars.remove(toolBar);
		this.toolbarPanel.remove(toolBar.getComponent());
		// check whether to close the window
		if(this.panes[CENTER_PANE] == null && this.toolBars.isEmpty()){
			this.closeWindow();
		}
		if(this.toolBars.isEmpty())
			this.toolbarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, WlLookAndFeel.COLOR_LINE_BORDER));
		// do a repaint
		this.validate();		
	}
	
	/** Returns the components of this window.
	 * @return the components of this window.
	 */
	public List<WlComponent> getWlComponents(){		
		List<WlComponent> components = new LinkedList<WlComponent>();
		new PANE_FOREACH(components) {
			@Override
			public void callback(FancyTabbedPane pane, String layout, Object... params) {
				@SuppressWarnings("unchecked")
				List<WlComponent> components = (List<WlComponent>)params[0];
				if(pane != null)
					components.addAll(pane.getWlComponents());
				Component c = mainPanel.getComponentAt(layout);
				if(c!= null && c instanceof WlComponent)
					components.add((WlComponent)c);
			}
		};
		return components;
	}
	
	/**
	 * Adds the given WlComponent to this WlWindow.
	 * @param comp a WlComponent
	 * @param constraints some BorderLayout constraints
	 */
	public void addWlComponent(WlComponent comp, Object constraints){		
		if(comp instanceof WlStandaloneComponent){
			if(BorderLayout.CENTER.equals(constraints)){
				if(this.getWlComponents().isEmpty()){
					this.mainPanel.add(comp, BorderLayout.CENTER);
					((WlStandaloneComponent)comp).setWindow(this);
					return;
				}else{
					//TODO
					throw new RuntimeException("IMPLEMENT ME");
				}
			}
			this.mainPanel.add(comp, constraints);
			((WlStandaloneComponent)comp).setWindow(this);
			this.refreshMinimumSize();
			return;	
		}		
		// if the center pane is empty always add the component there
		if(this.panes[CENTER_PANE] == null)	constraints = BorderLayout.CENTER;
		new PANE_FOREACH(this, constraints, comp) {
			@Override
			public void callback(FancyTabbedPane pane, String layout, Object... params) {
				WlWindow parent = (WlWindow)params[0];
				Object constraints = params[1];
				WlComponent comp = (WlComponent)params[2];
				if(!layout.equals(constraints))
					return;
				
				if(pane == null) {
					int index = -1;
					for(int i=0; i<borderLayout.length; ++i) {
						if(borderLayout[i].equals(layout)) {
							index = i;
							break;
						}
					}
					pane = panes[index] = new FancyTabbedPane(parent, disposePanes.get(layout));
					pane.addWlComponent(comp);
					tabbedPanesPanel.add(pane, layout);
				} else {
					pane.addWlComponent(comp);
				}
				activePane = pane;
			}
		};
		this.windowSet.fireWlComponentChangedTitleEvent(new WlComponentTitleChangedEvent(comp));
		// refresh minimum size
		this.refreshMinimumSize();
	}
	
	public void removeAllWlComponents() {
		for(int i=0; i<PANE_COUNT; ++i) {
			if(panes[i] != null) {
				panes[i].removeAllWlComponents();
			}
		}
	}
	
	/** Removes the given standalone component
	 * @param comp some component.
	 * @return "true" iff the removal was successful.
	 */
	protected boolean removeStandaloneComponent(WlStandaloneComponent comp){
		this.mainPanel.remove(comp);
		comp.setWindow(null);
		this.refreshMinimumSize();
		Boolean result = new Boolean(false);
		new PANE_FOREACH() {
			@Override
			public void callback(FancyTabbedPane pane, String layout, Object... params) {
				if(mainPanel.getComponentAt(layout) != null)
					params[0] = (Boolean)true;
			}
		};
		this.refreshMinimumSize();
		if(result) return true;
		this.closeWindow();
		return true;
	}
	
	/**
	 * Removes the given pane from this window's view.
	 * @param pane a tabbed pane.
	 * @return "true" iff the removal was successful.
	 */
	protected boolean removePane(FancyTabbedPane pane){
		boolean isCenterPane = false;
		for(int i=0; i<panes.length; ++i) {
			if(panes[i] == pane) {
				panes[i] = null;
				if(i == CENTER_PANE)
					isCenterPane = true;
			}
		}
		if(this.activePane == pane)
			this.activePane = null;		
		this.tabbedPanesPanel.remove(pane);
		// in case of the center pane being removed 
		// move one of the other panes (or close the window)
		boolean allEmpty = false;
		if(isCenterPane){
			int [] prio = {WEST_PANE, EAST_PANE, SOUTH_PANE};
			allEmpty = true;
			for(int i=0; i<prio.length; ++i) {
				FancyTabbedPane act = panes[prio[i]];
				if(act != null) {
					tabbedPanesPanel.remove(act);
					panes[CENTER_PANE] = act;
					panes[prio[i]] = null;
					tabbedPanesPanel.add(act, borderLayout[CENTER_PANE]);
					allEmpty = false;
					break;
				}
			}
		}
		// if removed pane should not be removed when empty re-assign the new center pane this property
		if(!pane.getDisposeWhenEmpty()){
			if(this.panes[CENTER_PANE] != null)
				this.panes[CENTER_PANE].setDisposeWhenEmpty(false);
			else{
				// re-add the pane if everything has been closed)
				this.panes[CENTER_PANE] = pane;
				this.tabbedPanesPanel.add(pane,BorderLayout.CENTER);
				this.activePane = pane;
			}			
		}
		if(pane.getDisposeWhenEmpty() && allEmpty){
			if(this.toolBars.isEmpty())
				this.closeWindow();			
		}
		this.refreshMinimumSize();
		this.repaint();
		return !pane.getDisposeWhenEmpty();
	}
	
	/**
	 * Refreshes the minimum size of this window
	 */
	protected void refreshMinimumSize(){
		int minWidth = 40;
		int minHeight = 28;
		for(WlToolBar toolBar: this.toolBars)
			minHeight += toolBar.getComponent().getMinimumSize().height;		
		if(this.panes[SOUTH_PANE] != null)
			minHeight += this.panes[SOUTH_PANE].getMinimumSize().height;
		int minEast = (this.panes[EAST_PANE] != null)?(this.panes[EAST_PANE].getMinimumSize().width):(0);
		int minCenter = (this.panes[CENTER_PANE] != null)?(this.panes[CENTER_PANE].getMinimumSize().width):(0);
		int minWest = (this.panes[WEST_PANE] != null)?(this.panes[WEST_PANE].getMinimumSize().width):(0);
		minWidth += minCenter + minEast + minWest;
		if(this.mainPanel.getComponentAt(BorderLayout.WEST) != null)
			minWidth += this.mainPanel.getComponentAt(BorderLayout.WEST).getMinimumSize().width;
		if(this.mainPanel.getComponentAt(BorderLayout.EAST) != null)
			minWidth += this.mainPanel.getComponentAt(BorderLayout.EAST).getMinimumSize().width;
		int minWidthToolBars = 0;
		for(WlToolBar toolBar: this.toolBars)
			minWidthToolBars = Math.max(minWidthToolBars, toolBar.getComponent().getMinimumSize().width);
		minWidth = Math.max(minWidth, minWidthToolBars);
		minHeight += this.minHeightEastCenterWest();
		if(this.mainPanel.getComponentAt(BorderLayout.SOUTH) != null)
			minHeight += this.mainPanel.getComponentAt(BorderLayout.SOUTH).getMinimumSize().height;
		this.setMinimumSize(new Dimension(minWidth,minHeight));
		// do a repaint
		this.validate();
	}
	
	/**
	 * Returns the minimum height of the central components
	 * east, center, and west.
	 * @return the minimum height of the central components
	 * east, center, and west.
	 */
	private int minHeightEastCenterWest(){
		int minEast = (this.panes[EAST_PANE] != null)?(this.panes[EAST_PANE].getMinimumSize().height):(0);
		int minCenter = (this.panes[CENTER_PANE] != null)?(this.panes[CENTER_PANE].getMinimumSize().height):(0);
		int minWest = (this.panes[WEST_PANE] != null)?(this.panes[WEST_PANE].getMinimumSize().height):(0);
		return Math.max(minEast, Math.max(minCenter, minWest));
	}
	
	/**
	 * Returns the current height of the central components
	 * east, center, and west.
	 * @return the current height of the central components
	 * east, center, and west.
	 */
	protected int currentHeightEastCenterWest(){
		int [] prio = {EAST_PANE, CENTER_PANE, WEST_PANE};
		for(int i=0; i<prio.length; ++i) {
			if(panes[prio[i]] != null)
				return panes[prio[i]].getHeight();
		}
		return 0;
	}
	
	protected int currentHeightWithoutToolbar(){
		int s = (this.mainPanel.getComponentAt(BorderLayout.NORTH) != null)?(this.mainPanel.getComponentAt(BorderLayout.NORTH).getHeight()):(0);
		return this.mainPanel.getHeight() - s;
	}
		
	/** Returns the tabbed panes panel.
	 * @return the tabbed panes panel.
	 */
	protected JPanel getTabbedPanesPanel(){
		return this.tabbedPanesPanel;
	}
	
	/** Removes focus from other panes and gives focus to the given pane.
	 * @param pane a tabbed pane.
	 */
	protected void requestFocus(FancyTabbedPane pane){
		for(int i=0; i<panes.length; ++i) {
			if(panes[i] != null && panes[i] != pane)
				panes[i].focusLost();
		}
		for(int i=0; i<panes.length; ++i) {
			if(panes[i] != null && panes[i] == pane)
				panes[i].focusGained();
		}			
		this.activePane = pane;
	}
	
	/** Returns the active component of this window (or "null" if there is none).
	 * @return the active component of this window (or "null" if there is none).
	 */
	public WlComponent getFocusedComponent(){
		if(this.activePane != null)
			return this.activePane.getFocusedComponent();
		return null;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowFocusListener#windowGainedFocus(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowGainedFocus(WindowEvent e) {
		if(activePane != null)
			activePane.focusGained();
		
		int [] order = {SOUTH_PANE, WEST_PANE, EAST_PANE};
		for(int i=0; i<order.length; ++i) {
			Component c = mainPanel.getComponentAt(borderLayout[order[i]]);
			if(c != null)
				((WlComponent)c).focusGained();
		}
		
		if(this.mainPanel.getComponentAt(BorderLayout.CENTER) != null)
			if(this.mainPanel.getComponentAt(BorderLayout.CENTER) instanceof WlStandaloneComponent)
			((WlComponent)this.mainPanel.getComponentAt(BorderLayout.CENTER)).focusGained();		
		
		if(OsTools.isMacOS())
			this.windowSet.requestMenuBar(this);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowFocusListener#windowLostFocus(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowLostFocus(WindowEvent e) {
		for(int i=0; i<panes.length; ++i) {
			if(panes[i] != null) panes[i].focusLost();
		}
		
		if(OsTools.isMacOS())
			this.setJMenuBar(null);
	}
	
	/** Returns the window set this window belongs to.
	 * @return the window set this window belongs to.
	 */
	protected WlWindowSet getWindowSet(){
		return this.windowSet;
	}
	
	/** Returns the center pane of this window.
	 * @return the center pane of this window.
	 */
	protected FancyTabbedPane getCenterPane(){
		return this.panes[CENTER_PANE];
	}
	
	/** Checks whether this window has no WlComponents.
	 * @return "true" if there are no WlComponents in this window.
	 */
	public boolean isEmpty(){
		return this.panes[CENTER_PANE] == null;
	}
	
	/** Checks whether the given pane is the active pane.
	 * @param pane some pane.
	 * @return "true" iff the given pane is the active pane.
	 */
	protected boolean isActivePane(FancyTabbedPane pane){
		return this.activePane != null && this.activePane.equals(pane);
	}
	
	/**
	 * Checks whether the given point is on a border of this
	 * window (within a margin of BORDER_PADDING) and returns the
	 * corresponding border in form of either
	 * BorderLayout.WEST, BorderLayout.EAST, or BorderLayout.SOUTH
	 * (or "null" if the point is not near a border).
	 * @param p a point
	 * @return one of BorderLayout.WEST, BorderLayout.EAST, or BorderLayout.SOUTH
	 * (or "null" if the point is not near a border).
	 */
	protected String getBorderOfLocation(Point p){
		if(!this.tabbedPanesPanel.isShowing())
			return null;
		if(!WlWindowSet.isInComponent(p, this.tabbedPanesPanel))
			return null;
		if(this.tabbedPanesPanel.getLocationOnScreen().y + this.tabbedPanesPanel.getHeight() - WlWindow.BORDER_PADDING <= p.getY())
			return BorderLayout.SOUTH;
		if(this.tabbedPanesPanel.getLocationOnScreen().x + WlWindow.BORDER_PADDING >= p.getX())
			return BorderLayout.WEST;
		if(this.tabbedPanesPanel.getLocationOnScreen().x + this.tabbedPanesPanel.getWidth() - WlWindow.BORDER_PADDING <= p.getX())
			return BorderLayout.EAST;		
		return null;
	}
	
	/** 
	 * Checks whether the given point is inside some tabbed pane
	 * and returns the given pane (or null if this is not the case).
	 * @param p a point.
	 * @return a tabbed pane or "null".
	 */
	protected FancyTabbedPane getPaneOfLocation(Point p){
		for(int i=0; i<panes.length; ++i) {
			if(panes[i] != null && WlWindowSet.isInComponent(p, panes[i]))
				return panes[i];
		}
		return null;
	}

	/** Closes this window. */
	private void closeWindow(){
		this.windowSet.remove(this);
		this.setVisible(false);
		this.dispose();		
		this.windowSet.fireWlWindowClosedEvent(new WlWindowClosedEvent(this));
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Frame#setTitle(java.lang.String)
	 */
	public void setTitle(String title){
		this.titleSupplement = title;
		if(title.equals(""))
			super.setTitle(this.title);		
		else super.setTitle(this.title + " - " + title);
	}
	
	/** Returns the title of this window.
	 * @return the title of this window.
	 */
	public String getWindowTitle(){
		return this.title;
	}
	
	/** Returns the title supplement of this window.
	 * @return the title supplement of this window.
	 */
	public String getWindowTitleSupplement(){
		return this.titleSupplement;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowOpened(WindowEvent e) { }
	
	/** Checks whether this window may be closed
	 * (but does not closes it).
	 * @return "true" iff this window may be closed.
	 */
	public boolean mayClose(){
		return this.windowSet.requestClosing(this);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		if(!this.windowSet.requestClosing(this))
			return;
		this.closeWindow();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosed(WindowEvent e) {
		for(WlComponent comp: this.getWlComponents())
			comp.componentClosed();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowIconified(WindowEvent e) { }

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeiconified(WindowEvent e) { }

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowActivated(WindowEvent e) { }

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeactivated(WindowEvent e) { }


}
