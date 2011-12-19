package com.whiplash.gui;

import java.awt.*;
import java.io.*;

import com.explodingpixels.macwidgets.*;
import com.whiplash.control.*;
import com.whiplash.doc.*;
import com.whiplash.gui.events.*;
import com.whiplash.res.*;

/**
 * A file explorer lists all recently opened files.
 * @author Matthias Thimm
 */
public class WlFileExplorer extends WlStandaloneComponent implements WlComponentListener, SourceListSelectionListener {
	
	/** For serialization.  */
	private static final long serialVersionUID = 1L;
		
	/** The tree model. */
	private WlFileExplorerTreeModel treeModel;
	/** The source list component */
	private SourceList sourceList;
	
	/** The GUI controller of this file explorer. */
	private WlMultiTextGuiController guiController;
	
	/** Creates a new file explorer.
	 * @param guiController the GUI controller of this file explorer.
	 */
	public WlFileExplorer(WlMultiTextGuiController guiController){
		super();
		this.guiController = guiController;
		this.treeModel = new WlFileExplorerTreeModel(this,guiController);		
		this.setMinimumSize(new Dimension(200,200));
		this.setPreferredSize(new Dimension(350,800));
		// init gui components
		this.setLayout(new BorderLayout(0,0));		
		this.setBorder(null);//BorderFactory.createLineBorder(WlLookAndFeel.DEFAULT_THINLINE_COLOR, 1));
		this.sourceList = new SourceList(this.treeModel.getSourceListModel());
		this.sourceList.addSourceListSelectionListener(this);
		this.add(this.sourceList.getComponent());
		this.sourceList.addMouseInputListener(this);
		// the following is a workaround to fix a bug that the source list does not expand properly.
		/*SourceListItem item1 = new SourceListItem("Item 1");
		SourceListItem item2 = new SourceListItem("Item 2");
		this.treeModel.getSourceListModel().addTopLevelItem(item1);
		this.treeModel.getSourceListModel().addItemToItem(item2, item1);
		this.treeModel.getSourceListModel().removeItemFromItem(item2, item1);
		this.treeModel.getSourceListModel().removeTopLevelItem(item1);*/		
	}
	
	/** Returns the tree model.
	 * @return the tree model.
	 */
	public WlFileExplorerTreeModel getModel(){
		return this.treeModel;
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.gui.WlComponent#focusGained()
	 */
	@Override
	protected void focusGained() {	
		this.sourceList.getComponent().repaint();
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.gui.WlComponent#getTitle()
	 */
	@Override
	public String getTitle() {
		// check resource manager
		if(!WlResourceManager.hasDefaultResourceManager())			
			throw new RuntimeException("No default resource manager set.");
		return WlResourceManager.getDefaultResourceManager().getLocalizedText(WlText.FILE_EXPLORER_TITLE);
	}
	
	/** Returns the selected file tree node.
	 * @return the selected file tree node.
	 */
	public WlFileTreeNode getSelected(){
		return (WlFileTreeNode) this.sourceList.getSelectedItem();
	}
	
	/** Sets the selected file tree node.
	 * @param node some file tree node.
	 */
	public void setSelected(WlFileTreeNode node){
		this.sourceList.setSelectedItem(node);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.gui.events.WlComponentListener#componentChangedTitle(com.whiplash.gui.events.WlComponentTitleChangedEvent)
	 */
	@Override
	public void componentChangedTitle(WlComponentTitleChangedEvent e) { }

	/* (non-Javadoc)
	 * @see com.whiplash.gui.events.WlComponentListener#componentGainedFocus(com.whiplash.gui.events.WlComponentFocusGainedEvent)
	 */
	@Override
	public void componentGainedFocus(WlComponentFocusGainedEvent e) {
		if(e.getComponent() instanceof WlEditorPane<?>){
			WlEditorPane<?> editorPane = (WlEditorPane<?>) e.getComponent();
			TextFileDocument doc = (TextFileDocument)editorPane.getDocument();
			File file = doc.getFile();
			if(this.treeModel.getNodeForFile(file) != null){
				this.sourceList.setSelectedItem(this.treeModel.getNodeForFile(file));
			}
		}		
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.gui.events.WlComponentListener#componentLostFocus(com.whiplash.gui.events.WlComponentFocusLostEvent)
	 */
	@Override
	public void componentLostFocus(WlComponentFocusLostEvent e) { }

	/* (non-Javadoc)
	 * @see com.whiplash.gui.events.WlComponentListener#componentDragged(com.whiplash.gui.events.WlComponentDraggedEvent)
	 */
	@Override
	public void componentDragged(WlComponentDraggedEvent e) { }

	/* (non-Javadoc)
	 * @see com.whiplash.gui.events.WlComponentListener#componentClosed(com.whiplash.gui.events.WlComponentClosedEvent)
	 */
	@Override
	public void componentClosed(WlComponentClosedEvent e) { }

	/* (non-Javadoc)
	 * @see com.whiplash.gui.events.WlComponentListener#componentActionEnablementChanged(com.whiplash.gui.events.WlComponentActionEnablementChangedEvent)
	 */
	@Override
	public void componentActionEnablementChanged(WlComponentActionEnablementChangedEvent e) {
		// TODO Auto-generated method stub		
	}
	
	/* (non-Javadoc)
	 * @see com.explodingpixels.macwidgets.SourceListSelectionListener#sourceListItemSelected(com.explodingpixels.macwidgets.SourceListItem)
	 */
	@Override
	public void sourceListItemSelected(SourceListItem item) {
		if(item instanceof WlFileTreeNode){
			File file = ((WlFileTreeNode)item).getFile();
			if(this.guiController.isOpened(file)){				
				this.guiController.focusFile(file);
			}
			else{
				this.guiController.openFile(file);		
			}
		}		
	}
}
