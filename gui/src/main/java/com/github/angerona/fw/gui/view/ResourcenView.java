package com.github.angerona.fw.gui.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import bibliothek.gui.dock.DefaultDockable;

import com.github.angerona.fw.gui.base.ViewComponent;
import com.github.angerona.fw.gui.controller.TreeControllerAdapter;

/**
 * 
 * @deprecated
 * @todo replace using MVP pattern.
 */
public class ResourcenView extends JPanel implements ViewComponent {

	/** kick warning */
	private static final long serialVersionUID = -8021405489946274962L;

	private TreeControllerAdapter controller;
	
	private JTree tree;
	
	public ResourcenView(TreeControllerAdapter controller) {
		setController(controller);
		if(controller == null) {
			throw new IllegalArgumentException("The controller has to be set before initilization");
		}
		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(tree), BorderLayout.CENTER);
		tree.setRootVisible(false);
	}
	
	public void setController(TreeControllerAdapter controller) {
		if(controller == null)
			throw new IllegalArgumentException("Controller must not be null.");
		this.controller = controller;
		tree = controller.getTree();
	}

	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	public void decorate(DefaultDockable dockable) {
		dockable.setTitleText("Resourcen");
	}
}
