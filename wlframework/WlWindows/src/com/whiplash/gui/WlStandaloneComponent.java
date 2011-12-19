package com.whiplash.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.event.*;

import com.whiplash.gui.events.*;

/**
 * This class represents stand alone components, i.e. components that are not
 * bundled in tabbed panes.
 * @author Matthias Thimm
 */
public abstract class WlStandaloneComponent extends WlComponent implements MouseInputListener {

	/** For serialization. */
	private static final long serialVersionUID = 1L;

	/** For dragging the component. */
	private WlDragTools dragTools;
	
	/** The parent window. */
	private WlWindow window;
	
	public WlStandaloneComponent(){
		super();
		this.addMouseMotionListener(this);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.gui.WlComponent#getWindow()
	 */
	public WlWindow getWindow(){
		return this.window;
	}
	
	/** Sets the window of this component.
	 * @param window some window.
	 */
	protected void setWindow(WlWindow window){
		this.window = window;
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.gui.WlComponent#requestClosing()
	 */
	public boolean requestClosing(){
		if(this.window != null)
			return this.window.removeStandaloneComponent(this);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.gui.WlComponent#getTitle()
	 */
	@Override
	public abstract String getTitle();

	/* (non-Javadoc)
	 * @see com.whiplash.gui.WlComponent#doPaint(java.awt.Graphics)
	 */
	protected void doPaint(Graphics g){}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);
		if(this.dragTools != null && this.dragTools.isDragging()){			
			// remove drag window
			this.dragTools.clean();
			// prepare information for component move event
			WlComponent component =  this;
			WlWindow origin = this.getWindow();
			WlWindow destination = this.getWindow();			
			if(this.getWindowSet().isOutsideAnyWindow(e.getLocationOnScreen())){
				// create new window
				WlWindow previousWindow = this.getWindow();
				this.window.removeStandaloneComponent(this);			
				WlWindow window = previousWindow.getWindowSet().createWindow(previousWindow.getWindowTitle(),e.getLocationOnScreen());
				window.setTitle(this.getTitle());
				window.addWlComponent(component,BorderLayout.CENTER);
				window.setPreferredSize(component.getSize());
				window.pack();
				window.setVisible(true);
				destination = window;
			}else{
				WlWindow window = this.getWindowSet().getWindowOfLocation(e.getLocationOnScreen());
				destination = window;
				String borderLocation = window.getBorderOfLocation(e.getLocationOnScreen());
				if(borderLocation == null){
					// do nothing					
				}else{
					this.window.removeStandaloneComponent(this);
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
			this.getWindowSet().fireWlComponentDraggedEvent(new WlComponentDraggedEvent(component,origin,destination));
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		if(this.dragTools == null)
			this.dragTools = new WlDragTools(this.getWindowSet(),false,true);
		if(!this.dragTools.isVisible()){
			Image totalImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics totalGraphics = totalImage.getGraphics();            
            this.paintComponents(totalGraphics);
            Image scaledImage = totalImage.getScaledInstance(this.getWidth()/2, this.getHeight()/2, Image.SCALE_DEFAULT);
			this.dragTools.show(scaledImage, this.getTitle(), new Point(e.getLocationOnScreen().x + 10, e.getLocationOnScreen().y + 10));
		}
		this.dragTools.mouseDragged(e,this);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent e) { }
}
