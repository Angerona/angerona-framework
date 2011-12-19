package com.whiplash.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.*;
import javax.swing.event.*;

import com.explodingpixels.macwidgets.*;
import com.whiplash.gui.events.*;
import com.whiplash.gui.laf.*;
import com.whiplash.res.*;

/** Represents the tab of a component.
 * @author Matthias Thimm
 */
public class FancyTabbedPaneTab extends JPanel implements ActionListener, MouseInputListener {
	
	/** A border line panel used for the layout of the tabs */
	private class BorderLinePanel extends JPanel{
		/** For serialization. */
		private static final long serialVersionUID = 1L;
		/** The orientation of the border line. */
		private int orientation;
		/** The location of the border line (if vertical). */
		private int location;		
		
		/** Creates a new border line panel with the given orientation
		 * @param orientation one of SwingConstants.HORIZONTAL, SwingConstants.VERTICAL.
		 * @param location one of SwingConstants.EAST, SwingConstants.WEST 
		 */
		public BorderLinePanel(int orientation, int location){
			super();
			this.setBorder(null);			
			this.orientation = orientation;
			this.location = location;
			if(this.orientation == SwingConstants.HORIZONTAL)
				this.setPreferredSize(new Dimension(1,1));
			else if(this.location == SwingConstants.WEST)
				this.setPreferredSize(new Dimension(1,1));
			else this.setPreferredSize(new Dimension(2,4));
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.JComponent#paint(java.awt.Graphics)
		 */
		public void paint(Graphics g){
			WlLookAndFeel.paintTabBorder(g, this.orientation, this.location);
		}		
	}
	
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	/** Tab is in background */
	public static final int FOCUS_BACKGROUND = 0;
	/** Tab is in foreground but not active */
	public static final int FOCUS_FOREGROUND = 1;
	/** Tab is active */
	public static final int FOCUS_ACTIVE = 2;
	
	/** Action command for closing a tab. */
	private static final String ACTION_CLOSE_TAB = "CLOSE-TAB";
	/** The title label of the tab. */
	private JLabel titleLabel;
	/** The component of this tab. */
	private WlComponent component;
	/** The focus state of the tab. */
	private int focusState;
	/** The center panel of the tab which contains the label and close button. */
	private JPanel centerPanel;
	
	/** For dragging the component belonging to the tab */
	private WlDragTools dragTools;
		
	/**
	 * Creates a new tab with the given title;
	 * @param component the component this tab belongs to.
	 */
	protected FancyTabbedPaneTab(WlComponent component){
		super();
		this.component = component;
		this.focusState = FancyTabbedPaneTab.FOCUS_ACTIVE;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		// check resource manager
		if(!WlResourceManager.hasDefaultResourceManager())			
			throw new RuntimeException("No default resource manager set.");
		WlResourceManager resourceManager = WlResourceManager.getDefaultResourceManager();
		// init components
		this.titleLabel = MacWidgetFactory.makeEmphasizedLabel(new JLabel(),Color.black,Color.black,new Color(0, 0, 0, 0));//new JLabel(this.component.getTitle());
		this.titleLabel.setFont(UIManager.getFont("Label.font").deriveFont(11.0f).deriveFont(Font.BOLD));
		JButton closeButton = WlLookAndFeel.createBorderlessWlButton(resourceManager.getLocalizedText(WlText.CLOSE), resourceManager.getIcon(WlIcon.CLOSE_ACTIVE, WlIconSize.SIZE_16x16), resourceManager.getIcon(WlIcon.CLOSE_ACTIVE, WlIconSize.SIZE_16x16), this, FancyTabbedPaneTab.ACTION_CLOSE_TAB);
		// lay out components
		this.setLayout(new BorderLayout(0,0));
		this.setBorder(null);
		this.centerPanel = new JPanel(new BorderLayout(5,0));
		this.centerPanel.setBorder(null);
		this.centerPanel.add(this.titleLabel,BorderLayout.CENTER);
		this.centerPanel.add(closeButton,BorderLayout.EAST);
		Dimension dim = new Dimension(2,0);
		this.centerPanel.add(new Box.Filler(dim,dim,dim),BorderLayout.NORTH);
		this.centerPanel.add(new Box.Filler(dim,dim,dim),BorderLayout.SOUTH);
		this.centerPanel.add(new Box.Filler(new Dimension(0,0),new Dimension(0,0),new Dimension(0,0)),BorderLayout.WEST);
		this.add(centerPanel,BorderLayout.CENTER);	
		this.add(new BorderLinePanel(SwingConstants.HORIZONTAL,-1),BorderLayout.NORTH);
		this.add(new BorderLinePanel(SwingConstants.VERTICAL,SwingConstants.WEST),BorderLayout.WEST);
		this.add(new BorderLinePanel(SwingConstants.VERTICAL,SwingConstants.EAST),BorderLayout.EAST);		
	}
	
	/** Returns the component of this tab.
	 * @return the component of this tab.
	 */
	protected WlComponent getComponent(){
		return this.component;
	}
	
	/**
	 * Sets the focus of this tab, one of FOCUS_BACKGROUND, FOCUS_FOREGROUND,
	 * FOCUS_ACTIVE.
	 * @param focus one of FOCUS_BACKGROUND, FOCUS_FOREGROUND,
	 * 	FOCUS_ACTIVE.
	 */
	protected void setFocus(int focus){
		this.focusState = focus;	
		this.repaint();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g){
		if(this.focusState == FancyTabbedPaneTab.FOCUS_ACTIVE)
			this.centerPanel.setBackground(WlLookAndFeel.COLOR_FOCUS_ACTIVE);
		else if(this.focusState == FancyTabbedPaneTab.FOCUS_BACKGROUND)
			this.centerPanel.setBackground(WlLookAndFeel.COLOR_FOCUS_BACKGROUND);
		else this.centerPanel.setBackground(WlLookAndFeel.COLOR_FOCUS_FOREGROUND);
		super.paint(g);				
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {		
		if(e.getActionCommand().equals(FancyTabbedPaneTab.ACTION_CLOSE_TAB))
			this.component.requestClosing();
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
			// prepare information for component move event
			WlComponent component =  this.component;
			WlWindow origin = this.component.getWindow();
			WlWindow destination = this.component.getWindow();			
			if(this.component.getWindowSet().isOutsideAnyWindow(e.getLocationOnScreen())){
				// create new window
				WlWindow previousWindow = this.component.getParentPane().getWindow();
				this.component.getParentPane().doCloseComponent(this.component);				
				WlWindow window = previousWindow.getWindowSet().createWindow(previousWindow.getWindowTitle(),e.getLocationOnScreen());
				window.addWlComponent(component,BorderLayout.CENTER);
				window.setPreferredSize(component.getSize());
				window.pack();
				window.setVisible(true);
				destination = window;
			}else{
				WlWindow window = this.component.getWindowSet().getWindowOfLocation(e.getLocationOnScreen());
				destination = window;
				String borderLocation = window.getBorderOfLocation(e.getLocationOnScreen());
				if(borderLocation == null){
					FancyTabbedPane pane = window.getPaneOfLocation(e.getLocationOnScreen());
					// check special case: component to be added is already in this pane
					// and the only component
					if(pane == this.component.getParentPane() && this.component.getParentPane().getNumberOfTabs() == 1)
						// do nothing
						return;
					if(pane != null){
						int idx = pane.getIndexOfLocation(e.getLocationOnScreen());
						if(idx != -1){							
							this.component.getParentPane().doCloseComponent(this.component);
							pane.addWlComponent(component, idx);						
						}					
					}
				}else{
					this.component.getParentPane().doCloseComponent(this.component);
					window.addWlComponent(component, borderLocation);
					// if moving a component from the center to one of the borders the window
					// is removed from the window set on closing the component. We have to add it again.
					if(!window.getWindowSet().contains(window)){
						window.getWindowSet().add(window);
						window.setVisible(true);
					}
				}				
			}
			// inform window set that a component has been moved
			this.component.getWindowSet().fireWlComponentDraggedEvent(new WlComponentDraggedEvent(component,origin,destination));
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
		if(this.dragTools == null)
			this.dragTools = new WlDragTools(this.component.getWindowSet(),true,true);
		if(!this.dragTools.isVisible()){
			Image totalImage = new BufferedImage(this.component.getWidth(), this.component.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics totalGraphics = totalImage.getGraphics();            
            this.component.paintComponents(totalGraphics);
            Image scaledImage = totalImage.getScaledInstance(this.component.getWidth()/2, this.component.getHeight()/2, Image.SCALE_DEFAULT);
			this.dragTools.show(scaledImage, this.component.getTitle(), new Point(e.getLocationOnScreen().x + 10, e.getLocationOnScreen().y + 10));
		}
		this.dragTools.mouseDragged(e,this.component);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent e) { }
	
	/** Refreshes the title of this tab. */
	public void refreshTitle() {
		this.titleLabel.setText(this.component.getTitle());		
		this.component.getParentPane().repaint();
	}
}