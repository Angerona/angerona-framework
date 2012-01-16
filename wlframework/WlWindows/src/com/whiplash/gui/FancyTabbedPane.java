package com.whiplash.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.whiplash.gui.events.*;
import com.whiplash.gui.laf.*;
import com.whiplash.res.*;

/**
 * This class implements a fancy alternative to the JTabbedPane.
 * @author Matthias Thimm
 */
public class FancyTabbedPane extends JPanel implements MouseListener, ComponentListener, ActionListener {
	
	/** The action that is been performed when clicking the "more" button */
	public final static String ACTION_MORE = "ACTION-MORE"; 
	
	/** For serialization. */
	private static final long serialVersionUID = 1L;
		
	/** The parent of this tabbed pane. */
	private WlWindow parent;
	
	/** The more button (visible if more tabs are present than can be shown) */
	private JButton moreButton;
	/** Whether the "more" button is visible */
	private boolean moreButtonVisible = false;
	/** The popup menu for the more button */
	private JPopupMenu moreMenu;
	
	/** The main panel for this tabbed pane. */
	private JPanel mainPanel;
	/** The upper pane used for the tabs. */
	private JPanel upperPane;
	/** The lower pane used for displaying the actual content */
	private JPanel lowerPane;
	/** The currently activated tab */
	private FancyTabbedPaneTab activeTab;	
	
	/** The (ordered) list of tabs */
	private List<FancyTabbedPaneTab> tabs;
	
	/** Whether this pane should be disposed when empty. */
	private boolean disposeWhenEmpty;
	
	/** Creates a new empty fancy tabbed pane that gets
	 * disposed when empty.
	 * @param parent the parent of this tabbed pane.*/
	public FancyTabbedPane(WlWindow parent){
		this(parent,true);
	}
	
	/** Creates a new empty fancy tabbed pane.
	 * @param parent the parent of this tabbed pane.
	 * @param disposeWhenEmpty whether this pane is disposed of
	 * when empty.
	 */
	public FancyTabbedPane(WlWindow parent, boolean disposeWhenEmpty){
		super();		
		this.disposeWhenEmpty = disposeWhenEmpty;
		this.parent = parent;
		// init tabs
		this.tabs = new LinkedList<FancyTabbedPaneTab>();
		// init layout 
		this.setLayout(new BorderLayout(0,0));		
		this.setBackground(WlLookAndFeel.COLOR_BACKGROUND_DEFAULT);
		this.mainPanel = new JPanel(new BorderLayout(0,0));
		this.mainPanel.setBorder(WlLookAndFeel.getTabbedPaneBorder());
		this.mainPanel.setBackground(WlLookAndFeel.COLOR_BACKGROUND_DEFAULT);
		this.upperPane = new JPanel();
		this.upperPane.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		this.upperPane.setBackground(WlLookAndFeel.COLOR_BACKGROUND_DEFAULT);
		this.lowerPane = new JPanel();
		this.lowerPane.setLayout(new CardLayout());
		this.lowerPane.setBackground(WlLookAndFeel.COLOR_BACKGROUND_DEFAULT);
		this.add(this.mainPanel,BorderLayout.CENTER);
		this.mainPanel.add(this.upperPane,BorderLayout.NORTH);
		this.mainPanel.add(this.lowerPane,BorderLayout.CENTER);
		// to react on resize events (requires rearranging tabs)
		this.upperPane.addComponentListener(this);
		// add mouse listener (used when clicked somewhere in the pane to activate the active tab)
		this.addMouseListener(this);
		// check resource manager
		if(!WlResourceManager.hasDefaultResourceManager())			
			throw new RuntimeException("No default resource manager set.");
		WlResourceManager resourceManager = WlResourceManager.getDefaultResourceManager();
		// init "more" button
		this.moreButton = WlLookAndFeel.createBorderlessWlButton(resourceManager.getLocalizedText(WlText.SHOW_MORE), resourceManager.getIcon(WlIcon.MORE_ACTIVE, WlIconSize.SIZE_16x16), resourceManager.getIcon(WlIcon.MORE_INACTIVE, WlIconSize.SIZE_16x16), this, FancyTabbedPane.ACTION_MORE);
		this.moreMenu = new JPopupMenu();
	}	
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getMinimumSize()
	 */
	public Dimension getMinimumSize(){
		int width = 0;
		int height = 0;
		for(FancyTabbedPaneTab tab: this.tabs){
			if(tab.getMinimumSize().height + tab.getComponent().getMinimumSize().height > height)
				height = tab.getMinimumSize().height + tab.getComponent().getMinimumSize().height;
			if(tab.getMinimumSize().width > width)
				width = tab.getMinimumSize().width;
			if(tab.getComponent().getMinimumSize().width > width)
				width = tab.getComponent().getMinimumSize().width;				
		}
		return new Dimension(width,height);
	}
	
	/** Sets the "dispose when empty" flag.
	 * @param value a boolean.
	 */
	protected void setDisposeWhenEmpty(boolean value){
		this.disposeWhenEmpty = value;
	}
	
	/** Returns the "dispose when empty" flag.
	 * @return value a boolean.
	 */
	protected boolean getDisposeWhenEmpty(){
		return this.disposeWhenEmpty;
	}

	/** Returns the components of this pane.
	 * @return the components of this pane.
	 */
	protected List<WlComponent> getWlComponents(){
		List<WlComponent> components = new LinkedList<WlComponent>();
		for(FancyTabbedPaneTab tab: this.tabs)
			components.add(tab.getComponent());
		return components;
	}
	
	/** Adds the given WlComponent to this tabbed pane
	 * (as last component).
	 * @param comp a WlComponent
	 */
	public void addWlComponent(WlComponent comp){	
		// add mouse listener in order to activate the component
		// when the tab or the component is clicked
		comp.getTab().addMouseListener(this);
		comp.addMouseListener(this);
		// temporarily remove "more" button to rearrange tabs
		if(this.moreButtonVisible)
			this.upperPane.remove(this.moreButton);
		// add tab
		this.upperPane.add(comp.getTab());
		if(this.moreButtonVisible)
			this.upperPane.add(this.moreButton);
		// add actual component
		this.lowerPane.add(comp,comp.getId());
		// keep a record of the tab 
		this.tabs.add(comp.getTab());		
		// set the parent of the component
		comp.setParentPane(this);		
		// activate tab
		this.activateTab(comp.getTab());
		// maybe some resizing/repainting is necessary
		this.componentResized(null);
		comp.refreshTitle();
		this.repaint();
	}
	
	public void removeAllWlComponents() {
		this.upperPane.removeAll();
		this.lowerPane.removeAll();
		this.tabs.clear();
		this.repaint();
	}
	
	/** Adds the given WlComponent to this tabbed pane at the
	 * specified index. 
	 * @param comp a WlComponent
	 * @param idx an index.
	 */
	protected void addWlComponent(WlComponent comp, int idx){		
		if(idx < this.tabs.size()){
			// add mouse listener in order to activate the component
			// when the tab or the component is clicked
			comp.getTab().addMouseListener(this);
			comp.addMouseListener(this);
			int tabsSize = this.tabs.size();
			for(int i = idx; i < tabsSize;i++)
				this.upperPane.remove(this.tabs.get(i));
			if(this.moreButtonVisible)
				this.upperPane.remove(this.moreButton);
			// add tab
			this.upperPane.add(comp.getTab());
			for(int i = idx; i < tabsSize; i++)
				this.upperPane.add(this.tabs.get(i));
			if(this.moreButtonVisible)
				this.upperPane.add(this.moreButton);
			// add actual component
			this.lowerPane.add(comp,comp.getId());
			// keep a record of the tab
			this.tabs.add(idx, comp.getTab());		
			// set the parent of the component
			comp.setParentPane(this);		
			// activate tab
			this.activateTab(comp.getTab());
			// maybe some resizing/repainting is necessary
			this.componentResized(null);
			comp.refreshTitle();
			this.repaint();
		}else this.addWlComponent(comp);
	}
	
	/** Returns the number of tabs in this pane.
	 * @return the number of tabs in this pane.
	 */
	protected int getNumberOfTabs(){
		return this.tabs.size();
	}
	
	/**
	 * Refreshes the minimum size of this tabbed pane.
	 */
	private void refreshMinimumSize(){
		if(this.activeTab != null)
			this.setMinimumSize(new Dimension(this.activeTab.getMinimumSize().width+20, this.getMinimumSize().height));
		if(this.parent != null)
			this.parent.refreshMinimumSize();
	}

	/**
	 * Closes the given component. Checks first whether the component allows to be closed.
	 * @param component a component.
	 * @param forceClose force close.
	 * @return "true" if the component was closed.
	 */
	protected boolean closeComponent(WlComponent component, boolean forceClose){
		// check if the component allows to be closed
		if(forceClose || this.parent.getWindowSet().requestClosing(component)){
			if(this.parent.isActivePane(this) && this.activeTab != null && this.activeTab.equals(component.getTab()))
				this.parent.getWindowSet().fireWlComponentFocusLostEvent(new WlComponentFocusLostEvent(component));
			this.doCloseComponent(component);
			// remove this pane as mouse listener of the tab and the component
			component.removeMouseListener(this);
			component.getTab().removeMouseListener(this);
			// inform component that it has been closed 
			component.componentClosed();
			// inform window set that a component was closed
			this.parent.getWindowSet().fireWlComponentClosedEvent(new WlComponentClosedEvent(component, this.getWindow()));
			return true;
		}
		return false;
	}
	
	/**
	 * Closes the given component. Doesn't check whether the component allows to be closed.
	 * @param component a component
	 */
	protected void doCloseComponent(WlComponent component){	
		// remove this pane as parent of the component
		component.setParentPane(null);
		// remove component and tab
		this.upperPane.remove(component.getTab());
		this.lowerPane.remove(component);
		int idx = this.tabs.indexOf(component.getTab());
		this.tabs.remove(component.getTab());
		// activate the next tab (if the given tab was the active one)
		if(this.activeTab == component.getTab()){			
			if(this.tabs.isEmpty())
				this.activeTab = null;
			else if(idx < this.tabs.size())
				this.activateTab(this.tabs.get(idx));
			else this.activateTab(this.tabs.get(idx-1));			
		}
		// if this tabbed pane is empty check if it should be removed from parent.
		if(this.tabs.isEmpty()){
			if(this.parent.removePane(this))
				return;
		}
		// refresh minimum size
		this.refreshMinimumSize();
		this.componentResized(null);
		// do a repaint
		this.repaint();		
	}
	
	/** Activates the given tab, i.e. puts it and its component
	 * to the foreground.
	 * @param tab a tab
	 */
	protected void activateTab(FancyTabbedPaneTab tab){		
		for(FancyTabbedPaneTab aTab: this.tabs)
			if(aTab != tab)
				aTab.setFocus(FancyTabbedPaneTab.FOCUS_BACKGROUND);
		tab.setFocus(FancyTabbedPaneTab.FOCUS_ACTIVE);
		if(this.activeTab != tab){
			this.activeTab = tab;
			CardLayout cl = (CardLayout) this.lowerPane.getLayout();
			cl.show(this.lowerPane, tab.getComponent().getId());
		}
		this.parent.requestFocus(this);
		// refresh minimum size
		this.refreshMinimumSize();	
		this.componentResized(null);
		// do a repaint
		this.validate();	
		this.repaint();
	}
	
	/**
	 * Checks whether the given point lies inside a tab
	 * and returns the corresponding index of that tab
	 * (or -1 if this is not the case)
	 * @param p a point.
	 * @return an int.
	 */
	protected int getIndexOfLocation(Point p){
		for(int i = 0; i < this.tabs.size(); i++)
			if(this.tabs.get(i).isVisible() && WlWindowSet.isInComponent(p, this.tabs.get(i)))
				return i;
		// if the point is inside the upper pane => last index
		if(WlWindowSet.isInComponent(p, this.upperPane))
			return this.tabs.size()+1;
		if(this.tabs.isEmpty() && WlWindowSet.isInComponent(p, this))
			return 0;
		return -1;
	}
	
	/**
	 * Checks whether the given point lies inside a tab
	 * and returns the corresponding tab, or (if the point
	 * lies in the upper pane) the upper pane, or (if there are
	 * no tabs) the whole pane (or "null" if neither is the case)
	 * @param p a point.
	 * @return a tab.
	 */
	protected JPanel getTabOfLocation(Point p){
		if(this.tabs.isEmpty())
			return this;
		for(int i = 0; i < this.tabs.size(); i++)
			if(this.tabs.get(i).isVisible() && WlWindowSet.isInComponent(p, this.tabs.get(i)))
				return this.tabs.get(i);
		if(WlWindowSet.isInComponent(p, this.upperPane))
			return this.upperPane;
		return null;
	}
	
	/** Returns the active component of this pane (or "null" if there is none).
	 * @return the active component of this pane (or "null" if there is none).
	 */
	public WlComponent getFocusedComponent(){
		if(this.activeTab != null)
			return this.activeTab.getComponent();
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getComponent() instanceof FancyTabbedPaneTab)
			this.activateTab((FancyTabbedPaneTab)e.getComponent());
		else if(this.activeTab != null)
			this.activateTab(this.activeTab);		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) { }

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) { }

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) { }

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) { }
	
	/** This method is called when this component gains focus. */
	public void focusGained() {	
		if(this.activeTab != null){
			this.activeTab.setFocus(FancyTabbedPaneTab.FOCUS_ACTIVE);
			this.activeTab.getComponent().focusGained();
		}
	}
	
	/** This method is called when this component loses focus. */
	public void focusLost() {	
		if(this.activeTab != null){
			this.activeTab.setFocus(FancyTabbedPaneTab.FOCUS_FOREGROUND);			
			this.activeTab.repaint();
			this.activeTab.getComponent().focusLost();
		}
	}
	
	/** Returns the window this pane lies in.
	 * @return the window this pane lies in.
	 */
	public WlWindow getWindow(){
		return this.parent;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentResized(ComponentEvent e) {
		int width = 0;
		int actualWidth = this.getSize().width;
		boolean addMoreButton = false;
		this.moreMenu.removeAll();
		// the active tab should always be visible
		if(this.activeTab != null){
			this.activeTab.setVisible(true);
			width += this.activeTab.getWidth();
		}		
		for(FancyTabbedPaneTab tab: this.tabs){
			if(tab != this.activeTab){
				if(width + tab.getWidth() + this.moreButton.getWidth() + 4 < actualWidth){
					tab.setVisible(true);
					width += tab.getWidth();
				}else{
					tab.setVisible(false);
					addMoreButton = true;
					final FancyTabbedPaneTab theTab = tab;
					Action itemAction = new AbstractAction(){						
						private static final long serialVersionUID = 1L;
						@Override
						public void actionPerformed(ActionEvent e) {
							activateTab(theTab);
						}						
					};
					itemAction.putValue(Action.NAME, theTab.getComponent().getTitle());
					JMenuItem item = new JMenuItem(itemAction);					
					this.moreMenu.add(item);
				}
			}
		}	
		if(addMoreButton && !this.moreButtonVisible){
			this.upperPane.add(this.moreButton);
			this.moreButtonVisible = true;
		}else if(!addMoreButton && this.moreButtonVisible){
			this.upperPane.remove(this.moreButton);
			this.moreButtonVisible = false;
		}		
		// repaint actual active component to ensure the
		// line border to be drawn correctly
		if(this.activeTab != null)
			this.activeTab.getComponent().repaint();		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentMoved(ComponentEvent e) { }

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentShown(ComponentEvent e) { }

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentHidden(ComponentEvent e) { }

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(FancyTabbedPane.ACTION_MORE)){
			this.moreMenu.show(this.moreButton, 0, this.moreButton.getHeight());
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	/*public void paint(Graphics g){		
		super.paint(g);
		g.setColor(WlLookAndFeel.COLOR_OUTER_LINE_BORDER);
		int startX = 0;
		for(FancyTabbedPaneTab tab: this.tabs)
			if(tab.isVisible())
				startX += tab.getWidth();
		g.drawLine(startX+1, 1, g.getClipBounds().width-2, 1);
		g.drawLine(g.getClipBounds().width-2, 1, g.getClipBounds().width-2, this.upperPane.getHeight());		
	}*/

}
