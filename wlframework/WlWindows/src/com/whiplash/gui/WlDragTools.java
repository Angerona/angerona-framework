package com.whiplash.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.explodingpixels.macwidgets.*;
import com.whiplash.gui.laf.*;
import com.whiplash.res.*;

/**
 * This class contains some auxiliary functionalities for components that can be dragged.
 * @author Matthias Thimm
 */
public class WlDragTools {

	/** Constant indicating a current drag outside any window. */
	private static final int DRAG_FRAME_OUTSIDE = 1;
	/** Constant indicating a current drag on east border. */
	private static final int DRAG_FRAME_EAST = 2;
	/** Constant indicating a current drag on west border. */
	private static final int DRAG_FRAME_WEST = 3;
	/** Constant indicating a current drag on south border. */
	private static final int DRAG_FRAME_SOUTH = 4;
	/** Constant indicating a current drag on a tab. */
	private static final int DRAG_FRAME_TAB = 5;
	/** Constant indicating a current drag on an invalid location. */
	private static final int DRAG_FRAME_INVALID = 6;
	
	/** Window that is being displayed during dragging. */
	private JWindow dragWindow = null;
	/** Window that is being displayed during dragging. */
	private JWindow dragWindow2 = null;
	/** Window that is being displayed during dragging. */
	private JWindow dragFrameWindow = null;
		
	/** The current dragging location for the frame window. */
	private int dragFrameType = -1;
	/** The current dragging location for the frame window (if it's a tab or an upper pane). */
	private JPanel dragFramedTabOrUpperPane = null;
	
	/** The window set this drag tools refer to. */
	private WlWindowSet windowSet;
	
	/** Whether the dragged component is a WlComponent, in this case the dragging windows
	 * gets an additional tab containing the title of the component.  */
	private boolean isWlComponent;
	
	/** Whether to draw a frame window indicating the new location if mouse is released.*/
	private boolean drawFrameWindow;
	
	/** Creates new drag tools for the given window set.
	 * @param windowSet a window set.
	 * @param isWlComponent whether the dragged component is a WlComponent, in this case the dragging windows
	 * gets an additional tab containing the title of the component.
	 */
	public WlDragTools(WlWindowSet windowSet, boolean isWlComponent, boolean drawFrameWindow){
		this.windowSet = windowSet;
		this.isWlComponent = isWlComponent;
		this.drawFrameWindow = drawFrameWindow;
	}
	
	/** Checks whether there is currently a dragging operation (this is an alias for isVisible()).
	 * @return "true" iff there is an ongoing dragging operation.
	 */
	public boolean isDragging(){
		return this.isVisible();
	}
	
	/** Removes the drag windows from the screen. */
	public void clean(){
		if(this.dragWindow != null){
			this.dragWindow.setVisible(false);
			this.dragWindow.dispose();
			this.dragWindow = null;
		}
		if(this.dragWindow2 != null){
			this.dragWindow2.setVisible(false);
			this.dragWindow2.dispose();
			this.dragWindow2 = null;
		}
		if(this.dragFrameWindow != null){
			this.dragFrameWindow.setVisible(false);
			this.dragFrameWindow.dispose();
			this.dragFrameWindow = null;
		}
	}
	
	/** Checks whether the drag windows are visible.
	 * @return "true" iff the drag windows are visible.
	 */
	public boolean isVisible(){
		return this.dragWindow != null || this.dragWindow2 != null || this.dragFrameWindow != null;
	}
	
	/** Shows the appropriate dragging windows on the screen.
	 * @param scaledImage a (scaled) image of the to be shown window.
	 * @param title the title of the component (plays no role if the dragged component is not a WlComponent).
	 * @param locationOnScreen the location on the screen where the dragging window is to be shown.
	 */
	public void show(Image scaledImage, String title, Point locationOnScreen){
		if(!this.isVisible()){
			this.dragWindow2 = new JWindow();
			JPanel emptyPanel2 = new JPanel();			
			emptyPanel2.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
			emptyPanel2.add(new JLabel(new ImageIcon(scaledImage)));			
			this.dragWindow2.getContentPane().add(emptyPanel2);
			this.dragWindow2.setLocation(locationOnScreen);
			if(this.isWlComponent){
				this.dragWindow = new JWindow();					
				JLabel label = MacWidgetFactory.makeEmphasizedLabel(new JLabel(title),Color.black,Color.black,new Color(0, 0, 0, 0));//new JLabel(this.component.getTitle());
				label.setFont(UIManager.getFont("Label.font").deriveFont(11.0f).deriveFont(Font.BOLD));
				JPanel emptyPanel = new JPanel();
				emptyPanel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
				emptyPanel.add(label);
				this.dragWindow.setBackground(WlLookAndFeel.COLOR_FOCUS_ACTIVE);
				this.dragWindow.getContentPane().add(emptyPanel);
				this.dragWindow.pack();
				this.dragWindow.setLocation(locationOnScreen);
				this.dragWindow.setVisible(true);
				this.dragWindow2.setBackground(WlLookAndFeel.COLOR_FOCUS_ACTIVE);
				this.dragWindow2.setLocation(new Point(locationOnScreen.x, locationOnScreen.y + this.dragWindow.getHeight()));				
			}
			this.dragWindow2.pack();
			this.dragWindow2.setVisible(true);
		}
	}
	
	/**
	 * Drags the dragging window over the screen corresponding the the given mouse event.
	 * @param e a mouse event.
	 * @param component the component that is to be dragged.
	 */
	public void mouseDragged(MouseEvent e, Component component) {
		if(this.isWlComponent){
			this.dragWindow.setLocation(new Point(e.getLocationOnScreen().x + 10, e.getLocationOnScreen().y + 10));
			this.dragWindow2.setLocation(new Point(e.getLocationOnScreen().x + 10, e.getLocationOnScreen().y + 10 + this.dragWindow.getHeight()));
		}else this.dragWindow2.setLocation(new Point(e.getLocationOnScreen().x + 10, e.getLocationOnScreen().y + 10));
		
		if(this.drawFrameWindow){	
			if(this.windowSet.isOutsideAnyWindow(e.getLocationOnScreen())){
				if(this.dragFrameWindow == null || this.dragFrameType != WlDragTools.DRAG_FRAME_OUTSIDE){
					if(this.dragFrameWindow != null){		
						this.dragFrameWindow.setVisible(false);
						this.dragFrameWindow.dispose();
						this.dragFrameWindow = null;
					}
					this.dragFrameType = WlDragTools.DRAG_FRAME_OUTSIDE;				
					this.dragFrameWindow = new JWindow();
					this.dragFrameWindow.setPreferredSize(component.getSize());
					this.dragFrameWindow.setBackground(new Color(0,0,0,0));
					JPanel framePanel = new JPanel();
					framePanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
					this.dragFrameWindow.getContentPane().setLayout(new BorderLayout());
					this.dragFrameWindow.getContentPane().add(framePanel,BorderLayout.CENTER);
					this.dragFrameWindow.pack();
					this.dragFrameWindow.setLocation(e.getLocationOnScreen());
					this.dragFrameWindow.setVisible(true);				
				}
				this.dragFrameWindow.setLocation(e.getLocationOnScreen());
			}else{
				WlWindow window = this.windowSet.getWindowOfLocation(e.getLocationOnScreen());
				String borderLocation = window.getBorderOfLocation(e.getLocationOnScreen());
				if(borderLocation == null){			
					FancyTabbedPane pane = window.getPaneOfLocation(e.getLocationOnScreen());
					if(pane != null){
						JPanel tabOrUpperPane = pane.getTabOfLocation(e.getLocationOnScreen());
						if(tabOrUpperPane != null){
							if(this.dragFrameWindow == null || this.dragFrameType != WlDragTools.DRAG_FRAME_TAB || this.dragFramedTabOrUpperPane != tabOrUpperPane){
								if(this.dragFrameWindow != null){		
									this.dragFrameWindow.setVisible(false);
									this.dragFrameWindow.dispose();
									this.dragFrameWindow = null;
								}
								this.dragFrameType = WlDragTools.DRAG_FRAME_TAB;
								this.dragFramedTabOrUpperPane = tabOrUpperPane;
								this.dragFrameWindow = new JWindow();
								this.dragFrameWindow.setPreferredSize(new Dimension(tabOrUpperPane.getWidth()-1,tabOrUpperPane.getHeight()));
								this.dragFrameWindow.setBackground(new Color(0,0,0,0));
								JPanel framePanel = new JPanel();
								framePanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
								this.dragFrameWindow.getContentPane().setLayout(new BorderLayout());
								this.dragFrameWindow.getContentPane().add(framePanel,BorderLayout.CENTER);
								this.dragFrameWindow.pack();
								this.dragFrameWindow.setLocation(tabOrUpperPane.getLocationOnScreen());
								this.dragFrameWindow.setVisible(true);
							}
							this.dragFrameWindow.setLocation(tabOrUpperPane.getLocationOnScreen());
						}else{
							if(this.dragFrameWindow == null || this.dragFrameType != WlDragTools.DRAG_FRAME_INVALID){
								if(this.dragFrameWindow != null){		
									this.dragFrameWindow.setVisible(false);
									this.dragFrameWindow.dispose();
									this.dragFrameWindow = null;
								}
								this.dragFrameType = WlDragTools.DRAG_FRAME_INVALID;
								this.dragFrameWindow = new JWindow();
								this.dragFrameWindow.setBackground(new Color(0,0,0,0));
								// check resource manager
								if(!WlResourceManager.hasDefaultResourceManager())			
									throw new RuntimeException("No default resource manager set.");
								WlResourceManager resourceManager = WlResourceManager.getDefaultResourceManager();
								JLabel label = new JLabel(resourceManager.getIcon(WlIcon.INVALID, WlIconSize.SIZE_48x48));
								this.dragFrameWindow.getContentPane().add(label);
								this.dragFrameWindow.pack();
								this.dragFrameWindow.setLocationRelativeTo(this.dragWindow2);
								this.dragFrameWindow.setVisible(true);
								
							}
							this.dragFrameWindow.setLocationRelativeTo(this.dragWindow2);
						}					
					}else{
						if(this.dragFrameWindow == null || this.dragFrameType != WlDragTools.DRAG_FRAME_INVALID){
							if(this.dragFrameWindow != null){		
								this.dragFrameWindow.setVisible(false);
								this.dragFrameWindow.dispose();
								this.dragFrameWindow = null;
							}
							this.dragFrameType = WlDragTools.DRAG_FRAME_INVALID;
							this.dragFrameWindow = new JWindow();
							this.dragFrameWindow.setBackground(new Color(0,0,0,0));
							JLabel label = new JLabel(WlResourceManager.getDefaultResourceManager().getIcon(WlIcon.INVALID, WlIconSize.SIZE_48x48));
							this.dragFrameWindow.getContentPane().add(label);
							this.dragFrameWindow.pack();
							this.dragFrameWindow.setLocationRelativeTo(this.dragWindow2);
							this.dragFrameWindow.setVisible(true);
							
						}
						this.dragFrameWindow.setLocationRelativeTo(this.dragWindow2);
					}
				}else{
					Component referenceFrame = window;
					if(window.getCenterPane() != null)
						referenceFrame = window.getCenterPane();
					if(this.dragFrameWindow == null ||
						(borderLocation.equals(BorderLayout.EAST) && this.dragFrameType != WlDragTools.DRAG_FRAME_EAST) ||
						(borderLocation.equals(BorderLayout.WEST) && this.dragFrameType != WlDragTools.DRAG_FRAME_WEST) ||
						(borderLocation.equals(BorderLayout.SOUTH) && this.dragFrameType != WlDragTools.DRAG_FRAME_SOUTH)){
							if(this.dragFrameWindow != null){		
								this.dragFrameWindow.setVisible(false);
								this.dragFrameWindow.dispose();
								this.dragFrameWindow = null;
							}
							if(borderLocation.equals(BorderLayout.EAST))
								this.dragFrameType = WlDragTools.DRAG_FRAME_EAST;
							else if(borderLocation.equals(BorderLayout.WEST))
								this.dragFrameType = WlDragTools.DRAG_FRAME_WEST;
							else if(borderLocation.equals(BorderLayout.SOUTH))
								this.dragFrameType = WlDragTools.DRAG_FRAME_SOUTH;
							this.dragFrameWindow = new JWindow();
							if(borderLocation.equals(BorderLayout.EAST) || borderLocation.equals(BorderLayout.WEST))
								if(component instanceof WlStandaloneComponent)
									this.dragFrameWindow.setPreferredSize(new Dimension(80,window.currentHeightWithoutToolbar()));
								else this.dragFrameWindow.setPreferredSize(new Dimension(80,window.currentHeightEastCenterWest()));
							else 
								if(component instanceof WlStandaloneComponent)
									this.dragFrameWindow.setPreferredSize(new Dimension(window.getWidth(),80));
								else this.dragFrameWindow.setPreferredSize(new Dimension(window.getTabbedPanesPanel().getWidth(),80));
							this.dragFrameWindow.setBackground(new Color(0,0,0,0));
							JPanel framePanel = new JPanel();
							framePanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
							this.dragFrameWindow.getContentPane().setLayout(new BorderLayout());
							this.dragFrameWindow.getContentPane().add(framePanel,BorderLayout.CENTER);
							this.dragFrameWindow.pack();
							
							if(borderLocation.equals(BorderLayout.WEST)){							
								if(component instanceof WlStandaloneComponent)
									this.dragFrameWindow.setLocation(window.getLocationOnScreen().x,referenceFrame.getLocationOnScreen().y);
								else this.dragFrameWindow.setLocation(window.getTabbedPanesPanel().getLocationOnScreen().x,referenceFrame.getLocationOnScreen().y);
							}else if(borderLocation.equals(BorderLayout.EAST)){
								if(component instanceof WlStandaloneComponent)
									this.dragFrameWindow.setLocation(window.getLocationOnScreen().x+window.getWidth()-80,referenceFrame.getLocationOnScreen().y);
								else this.dragFrameWindow.setLocation(window.getTabbedPanesPanel().getLocationOnScreen().x+window.getTabbedPanesPanel().getWidth()-80,referenceFrame.getLocationOnScreen().y);
							}else if(component instanceof WlStandaloneComponent)
									this.dragFrameWindow.setLocation(window.getLocationOnScreen().x,window.getLocationOnScreen().y+window.getHeight()-80);
								else this.dragFrameWindow.setLocation(window.getTabbedPanesPanel().getLocationOnScreen().x,window.getTabbedPanesPanel().getLocationOnScreen().y+window.getTabbedPanesPanel().getHeight()-80);
							this.dragFrameWindow.setVisible(true);
					}
					if(borderLocation.equals(BorderLayout.WEST)){							
						if(component instanceof WlStandaloneComponent)
							this.dragFrameWindow.setLocation(window.getLocationOnScreen().x,referenceFrame.getLocationOnScreen().y);
						else this.dragFrameWindow.setLocation(window.getTabbedPanesPanel().getLocationOnScreen().x,referenceFrame.getLocationOnScreen().y);
					}else if(borderLocation.equals(BorderLayout.EAST)){
						if(component instanceof WlStandaloneComponent)
							this.dragFrameWindow.setLocation(window.getLocationOnScreen().x+window.getWidth()-80,referenceFrame.getLocationOnScreen().y);
						else this.dragFrameWindow.setLocation(window.getTabbedPanesPanel().getLocationOnScreen().x+window.getTabbedPanesPanel().getWidth()-80,referenceFrame.getLocationOnScreen().y);
					}else if(component instanceof WlStandaloneComponent)
							this.dragFrameWindow.setLocation(window.getLocationOnScreen().x,window.getLocationOnScreen().y+window.getHeight()-80);
						else this.dragFrameWindow.setLocation(window.getTabbedPanesPanel().getLocationOnScreen().x,window.getTabbedPanesPanel().getLocationOnScreen().y+window.getTabbedPanesPanel().getHeight()-80);
					this.dragFrameWindow.setVisible(true);			
				}			
			}
			if(this.dragFrameType != WlDragTools.DRAG_FRAME_INVALID){
				if(this.dragWindow != null) this.dragWindow.toFront();
				if(this.dragWindow2 != null) this.dragWindow2.toFront();
			}
		}
	}
}
