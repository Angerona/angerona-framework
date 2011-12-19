package com.whiplash.control;

import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import com.explodingpixels.macwidgets.*;
import com.whiplash.config.*;
import com.whiplash.doc.*;
import com.whiplash.event.*;
import com.whiplash.gui.*;

/**
 * This class implements a tree model for the file explorer.
 * @author Matthias Thimm
 */
public class WlFileExplorerTreeModel implements WlDocumentControllerListener, ActionListener {

	/** The top-level file nodes of this model. */
	private List<WlFileTreeNode> topLevelNodes;
	/** The source list model that is used for display. */
	private SourceListModel sourceListModel;
		
	/** The GUI controller. */
	private WlMultiTextGuiController guiController;
	
	/** The file explorer component. */
	private WlFileExplorer fileExplorer;
	
	/** Creates a new tree model.
	 * @param fileExplorer some file explorer.
	 * @param guiController a GUI controller. */
	public WlFileExplorerTreeModel(WlFileExplorer fileExplorer, WlMultiTextGuiController guiController){
		this.topLevelNodes = new LinkedList<WlFileTreeNode>();
		this.sourceListModel = new SourceListModel();		
		this.guiController = guiController;
		this.fileExplorer = fileExplorer;
		// init tree		
		for(File file: WlMteConfigurationOptions.CONF_FILE_EXPLORER_FILES.getValue()){
	    	WlFileTreeNode node = new WlFileTreeNode(file,false,false,this);
	    	int idx = this.getIndexToMaintainAlphabeticalOrder(this.topLevelNodes, node);
	    	this.topLevelNodes.add(idx,node);
	    	this.sourceListModel.addTopLevelItem(node,idx);
	    }		
	}
	
	/** Returns the correct index to insert the given node in order to maintain
	 * alphabetical order.
	 * @param nodes some nodes.
	 * @param node a node.
	 * @return an index.
	 */
	private int getIndexToMaintainAlphabeticalOrder(List<WlFileTreeNode> nodes, WlFileTreeNode node){
		int idx = 0;
		for(WlFileTreeNode n: nodes)
			if(n.getFile().getName().compareTo(node.getFile().getName()) > 0)
				return idx;
			else idx++;
		return idx;
	}
	
	/**
	 * @return the source list model.
	 */
	public SourceListModel getSourceListModel(){
		return this.sourceListModel;
	}
		
	/** Returns the file tree node corresponding to the given file.
	 * @param file some file
	 * @return a file tree node or null.
	 */
	public WlFileTreeNode getNodeForFile(File file){
		for(WlFileTreeNode node: this.topLevelNodes)
			if(node.getFile().equals(file)){
				return node;
			}
		return null;
	}
	
	/** Sets the node's attribute for "opened" corresponding to the given file
	 * to the given value. 
	 * @param file some file.
	 * @param value some boolean.
	 */
	private void setOpened(File file, boolean value){
		if(this.getNodeForFile(file) != null){			
			this.getNodeForFile(file).setOpened(value);			
		}
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.event.WlDocumentControllerListener#documentOpened(com.whiplash.event.DocumentControllerEvent)
	 */
	@Override
	public void documentOpened(DocumentControllerEvent e) {
		if(!WlMteConfigurationOptions.CONF_FILE_EXPLORER_FILES.getValue().contains(e.getDocument().getFile())){
			WlMteConfigurationOptions.CONF_FILE_EXPLORER_FILES.addValue(e.getDocument().getFile());
			WlFileTreeNode node = new WlFileTreeNode(e.getDocument().getFile(),true,e.getDocument().isStained(),this);
			int idx = this.getIndexToMaintainAlphabeticalOrder(this.topLevelNodes, node);
	    	this.topLevelNodes.add(idx,node);
	    	this.sourceListModel.addTopLevelItem(node,idx);
		}else this.setOpened(e.getDocument().getFile(), true);		
	}

	/* (non-Javadoc)
	 * @see com.whiplash.event.WlDocumentControllerListener#documentSaved(com.whiplash.event.DocumentControllerEvent)
	 */
	@Override
	public void documentSaved(DocumentControllerEvent e) {
		if(!WlMteConfigurationOptions.CONF_FILE_EXPLORER_FILES.getValue().contains(e.getDocument().getFile())){
			WlMteConfigurationOptions.CONF_FILE_EXPLORER_FILES.addValue(e.getDocument().getFile());
			WlFileTreeNode node = new WlFileTreeNode(e.getDocument().getFile(),false,e.getDocument().isStained(),this);
	    	int idx = this.getIndexToMaintainAlphabeticalOrder(this.topLevelNodes, node);
	    	this.topLevelNodes.add(idx,node);
	    	this.sourceListModel.addTopLevelItem(node,idx);	    	
		}
		this.setOpened(e.getDocument().getFile(), true);
		File oldFile = (File) e.getUserData();
		if(oldFile != null && !oldFile.equals(e.getDocument().getFile())){
			this.getNodeForFile(oldFile).setStained(false);
			this.getNodeForFile(oldFile).setOpened(false);
			// if the old file was previously selected in the file explorer select the new file.
			if(this.fileExplorer.getSelected().equals(this.getNodeForFile(oldFile)))
				this.fileExplorer.setSelected(this.getNodeForFile(e.getDocument().getFile()));
		}
	}

	/* (non-Javadoc)
	 * @see com.whiplash.event.WlDocumentControllerListener#documentStainedStatusChanged(com.whiplash.event.DocumentControllerEvent)
	 */
	public void documentStainedStatusChanged(DocumentControllerEvent e){
		WlFileTreeNode node = this.getNodeForFile(e.getDocument().getFile());
		if(node != null)
			node.setStained(e.getDocument().isStained());
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.event.WlDocumentControllerListener#documentClosed(com.whiplash.event.DocumentControllerEvent)
	 */
	@Override
	public void documentClosed(DocumentControllerEvent e) {
		if(!WlMteConfigurationOptions.CONF_FILE_EXPLORER_FILES.getValue().contains(e.getDocument().getFile())){
			if(!(e.getDocument().getFile() instanceof NullFile)){
				WlMteConfigurationOptions.CONF_FILE_EXPLORER_FILES.addValue(e.getDocument().getFile());
				WlFileTreeNode node = new WlFileTreeNode(e.getDocument().getFile(),false,e.getDocument().isStained(),this);
				int idx = this.getIndexToMaintainAlphabeticalOrder(this.topLevelNodes, node);
		    	this.topLevelNodes.add(idx,node);
		    	this.sourceListModel.addTopLevelItem(node,idx);
			}else{
				WlFileTreeNode node = this.getNodeForFile(e.getDocument().getFile());
				this.topLevelNodes.remove(node);	    	    	
		    	this.sourceListModel.removeTopLevelItem(node);
			}
		}else{
			this.setOpened(e.getDocument().getFile(), false);
			this.getNodeForFile(e.getDocument().getFile()).setStained(false);
		}
	}

	/* (non-Javadoc)
	 * @see com.whiplash.event.WlDocumentControllerListener#documentCreated(com.whiplash.event.DocumentControllerEvent)
	 */
	@Override
	public void documentCreated(DocumentControllerEvent e) {
		WlFileTreeNode node = new WlFileTreeNode(e.getDocument().getFile(),false,e.getDocument().isStained(),this);
		int idx = this.getIndexToMaintainAlphabeticalOrder(this.topLevelNodes, node);
    	this.topLevelNodes.add(idx,node);
    	this.sourceListModel.addTopLevelItem(node,idx);
    	this.setOpened(e.getDocument().getFile(), true);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		WlFileTreeNode treeNode = (WlFileTreeNode) ((JButton)arg0.getSource()).getClientProperty(WlFileTreeNode.PROPERTY_TREENODE);
		if(treeNode.isOpened()){
			if(this.guiController.closeFile(treeNode.getFile())){
				if(!(treeNode.getFile() instanceof NullFile)) treeNode.setOpened(false);
			}
		}else{
			if(WlMteConfigurationOptions.CONF_FILE_EXPLORER_FILES.getValue().contains(treeNode.getFile())){
				WlMteConfigurationOptions.CONF_FILE_EXPLORER_FILES.removeValue(treeNode.getFile());
				this.topLevelNodes.remove(treeNode);	    	    	
		    	this.sourceListModel.removeTopLevelItem(treeNode);
			}
		}		
	}

}
