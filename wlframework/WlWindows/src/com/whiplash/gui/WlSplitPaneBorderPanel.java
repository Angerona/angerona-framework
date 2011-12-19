package com.whiplash.gui;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.*;

/**
 * This class models a panel with a border layout where each location N,S,W,E,C can be
 * changed in size by split pane dividers. 
 * @author Matthias Thimm
 *
 */
public class WlSplitPaneBorderPanel extends JPanel {

	/** For serialization. */
	private static final long serialVersionUID = 1L;

	/** The split panes. */
	private JSplitPane northCenterPane, centerSouthPane, westCenterPane, centerEastPane;
	/** The components. */
	private Component north = null, center = null, south = null, east = null, west = null;
	
	/** Creates a new panel. */
	public WlSplitPaneBorderPanel(){
		// init split panes
		this.northCenterPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
		this.northCenterPane.setBorder(null);
		this.northCenterPane.setDividerSize(1);
		this.centerSouthPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
		this.centerSouthPane.setBorder(null);
		this.centerSouthPane.setDividerSize(1);
		this.westCenterPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		this.westCenterPane.setBorder(null);
		this.westCenterPane.setDividerSize(1);
		this.centerEastPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		this.centerEastPane.setBorder(null);
		this.centerEastPane.setDividerSize(1);
		// do some layouting
		this.setLayout(new BorderLayout(0,0));
		
		this.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				resizeComponents();
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	/** Specifies whether the component at the given location is allowed to
	 * be resized.
	 * @param constraints one of the BorderLayout constraints.
	 * @param value whether resize should be enabled.
	 */
	public void setResizeEnabled(Object constraints, boolean value){
		if(constraints.equals(BorderLayout.NORTH))
			if(value)
				this.northCenterPane.setDividerSize(1);
			else this.northCenterPane.setDividerSize(0);
		//TODO go on
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Container#add(java.awt.Component, java.lang.Object)
	 */
	public void add(Component comp, Object constraints){
		boolean valid = false;
		if(constraints.equals(BorderLayout.NORTH)){
			this.north = comp;			
			valid = true;
		}else if(constraints.equals(BorderLayout.SOUTH)){
			this.south = comp;
			valid = true;
		}else if(constraints.equals(BorderLayout.WEST)){
			this.west = comp;
			valid = true;
		}else if(constraints.equals(BorderLayout.EAST)){
			this.east = comp;
			valid = true;
		}else if(constraints.equals(BorderLayout.CENTER)){
			this.center = comp;
			valid = true;
		}
		if(!valid) throw new IllegalArgumentException("Unrecognized constraints: " + constraints);
		this.layoutComponents();
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Container#remove(java.awt.Component)
	 */
	public void remove(Component comp){
		if(this.north == comp)
			this.north = null;
		if(this.south == comp)
			this.south = null;
		if(this.west == comp)
			this.west = null;
		if(this.east == comp)
			this.east = null;
		if(this.center == comp)
			this.center = null;
		this.layoutComponents();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getMinimumSize()
	 */
	public Dimension getMinimumSize(){
		int width = 0;
		int height = 0;
		if(this.west != null){
			width += this.west.getMinimumSize().width;
			height = this.west.getMinimumSize().height;
		}
		if(this.center != null){
			width += this.center.getMinimumSize().width;
			if(height < this.center.getMinimumSize().height)
				height = this.center.getMinimumSize().height;
		}
		if(this.east != null){
			width += this.east.getMinimumSize().width;
			if(height < this.east.getMinimumSize().height)
				height = this.east.getMinimumSize().height;
		}
		if(this.south != null){
			if(this.south.getMinimumSize().width > width)
				width = this.south.getMinimumSize().width;
			height += this.south.getMinimumSize().height;
		}	
		if(this.north != null){
			if(this.north.getMinimumSize().width > width)
				width = this.north.getMinimumSize().width;
			height += this.north.getMinimumSize().height;
		}
		return new Dimension(width,height);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	public Dimension getPreferredSize(){
		int width = 0;
		int height = 0;
		if(this.west != null){
			width += this.west.getPreferredSize().width;
			height = this.west.getPreferredSize().height;
		}
		if(this.center != null){
			width += this.center.getPreferredSize().width;
			if(height < this.center.getPreferredSize().height)
				height = this.center.getPreferredSize().height;
		}
		if(this.east != null){
			width += this.east.getPreferredSize().width;
			if(height < this.east.getPreferredSize().height)
				height = this.east.getPreferredSize().height;
		}
		if(this.south != null){
			if(this.south.getPreferredSize().width > width)
				width = this.south.getPreferredSize().width;
			height += this.south.getPreferredSize().height;
		}	
		if(this.north != null){
			if(this.north.getPreferredSize().width > width)
				width = this.north.getPreferredSize().width;
			height += this.north.getPreferredSize().height;
		}
		return new Dimension(width,height);
	}
	
	private void resizeComponents() {
		// TODO: Make configable. Save the manual sizes...
		int centerWidth 	= getWidth();
		int centerHeight	= getHeight();
		
		if(west != null && center != null) {
			centerWidth -= west.getMinimumSize().width;
			westCenterPane.setDividerLocation(west.getMinimumSize().width);
		}
		if(center != null && east != null) {
			centerWidth -= east.getMinimumSize().width;
			centerEastPane.setDividerLocation(centerWidth);
		}
		if(north != null && center != null) {
			centerHeight -= north.getMinimumSize().height;
			northCenterPane.setDividerLocation(north.getMinimumSize().height);
		}
		if(center != null && south != null) {
			centerHeight -= south.getMinimumSize().height;
			centerSouthPane.setDividerLocation(centerHeight);
		}
		repaint();
	}
	
	/** Lays out the components. */
	private void layoutComponents(){
		super.removeAll();
		Component comp = null;
		
		if(this.east != null)
			comp = this.east;
		if(this.center != null){
			if(comp != null){
				this.centerEastPane.setRightComponent(comp);
				this.centerEastPane.setLeftComponent(this.center);
				comp = this.centerEastPane;
			}else comp = this.center;
		}
		if(this.west != null){
			if(comp != null){
				this.westCenterPane.setRightComponent(comp);
				this.westCenterPane.setLeftComponent(this.west);				
				comp = this.westCenterPane;
			}else comp = this.west;
		}
		if(this.south != null){
			if(comp != null){
				this.centerSouthPane.setTopComponent(comp);
				this.centerSouthPane.setBottomComponent(this.south);		
				comp = this.centerSouthPane;
			}else comp = this.south;
		}
		if(this.north != null){
			if(comp != null){
				this.northCenterPane.setBottomComponent(comp);
				this.northCenterPane.setTopComponent(this.north);				
				comp = this.northCenterPane;
			}else comp = this.north;
		}
		
		if(comp != null)
			super.add(comp, BorderLayout.CENTER);	
		
		resizeComponents();
	}
	
	/** Returns the component at the given location.
	 * @param constraints one of the BorderLayout constraints.
	 * @return the component at the given location.
	 */
	public Component getComponentAt(Object constraints){
		if(constraints.equals(BorderLayout.NORTH))
			return this.north;
		if(constraints.equals(BorderLayout.SOUTH))
			return this.south;
		if(constraints.equals(BorderLayout.WEST))
			return this.west;
		if(constraints.equals(BorderLayout.EAST))
			return this.east;
		if(constraints.equals(BorderLayout.CENTER))
			return this.center;
		throw new IllegalArgumentException("Unrecognized constraints: " + constraints);
	}
	
	public static void main(String[] args){
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		WlSplitPaneBorderPanel panel = new WlSplitPaneBorderPanel();
		JPanel panel1 = new JPanel(new BorderLayout(0,0));
		panel1.setBorder(null);
		panel1.setBackground(Color.red);
		panel1.add(new JLabel("1"));
		JPanel panel2 = new JPanel(new BorderLayout(0,0));
		panel2.setBorder(null);
		panel2.setBackground(Color.BLUE);
		panel2.add(new JLabel("2"));
		JPanel panel3 = new JPanel(new BorderLayout(0,0));
		panel3.setBorder(null);
		panel3.setBackground(Color.GREEN);
		panel3.add(new JLabel("3"));
		JPanel panel4 = new JPanel(new BorderLayout(0,0));
		panel4.setBorder(null);
		panel4.setBackground(Color.MAGENTA);
		panel4.add(new JLabel("4"));
		JPanel panel5 = new JPanel(new BorderLayout(0,0));
		panel5.setBorder(null);
		panel5.setBackground(Color.PINK);
		panel5.add(new JLabel("5"));
		panel.add(panel1, BorderLayout.EAST);
		panel.add(panel2, BorderLayout.WEST);
		panel.add(panel3, BorderLayout.SOUTH);
		panel.add(panel4, BorderLayout.NORTH);
		panel.add(panel5, BorderLayout.CENTER);
		panel.setResizeEnabled(BorderLayout.NORTH, false);
		frame.add(panel,BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
}
