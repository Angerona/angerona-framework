package com.whiplash.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import com.whiplash.control.*;
import com.whiplash.doc.*;

/**
 * This class represents a popup (context) menu for editor panes showing a 
 * text document.
 * @author Matthias Thimm
 *
 */
public class WlTextDocumentPopupMenu extends JPopupMenu implements WlActionTrigger, PopupMenuListener {
	
	/** For serialization.  */
	private static final long serialVersionUID = 1L;
	
	/** Menu items regarding the encoding. */
	private java.util.List<JCheckBoxMenuItem> encodingItems;
	/** The editor pane this popup menu is associated with. */
	private WlEditorPane<?> editorPane;
	/** A map from action commands to the corresponding menu item. */
	private Map<String,JMenuItem> menuItems;
	/** the soft line wrap item. */
	private JCheckBoxMenuItem lineWrapItem;
	
	/** Creates a new popup menu for the given action listener and editor pane.
	 * @param action listener some action listener.
	 * @param editorPane some editor pane.
	 */
	public WlTextDocumentPopupMenu(ActionListener actionListener, WlEditorPane<?> editorPane){
		super();		
		this.editorPane = editorPane;
		this.encodingItems = new LinkedList<JCheckBoxMenuItem>();
		this.addPopupMenuListener(this);
		this.menuItems = new HashMap<String,JMenuItem>();
		this.add(new JMenuItem(WlEditMenuItems.undoAction));
		this.add(new JMenuItem(WlEditMenuItems.redoAction));
		this.addSeparator();
		this.add(new JMenuItem(WlEditMenuItems.cutAction));
		this.add(new JMenuItem(WlEditMenuItems.copyAction));
		this.add(new JMenuItem(WlEditMenuItems.pasteAction));
		this.add(new JMenuItem(WlEditMenuItems.selectAllAction));
		this.addSeparator();
		JMenu encodingMenu = WlEditMenuItems.getEncodingMenu(actionListener, this.encodingItems);		
		this.menuItems.put(GuiActionCommands.ACTION_CHANGE_ENCODING, encodingMenu);
		this.add(encodingMenu);
		this.addSeparator();
		this.add(new JMenuItem(WlEditMenuItems.fontBiggerAction));
		this.add(new JMenuItem(WlEditMenuItems.fontSmallerAction));
		this.addSeparator();
		this.lineWrapItem = new JCheckBoxMenuItem(WlEditMenuItems.toggleLineWrap);		
		this.add(this.lineWrapItem);
	}

	/* (non-Javadoc)
	 * @see com.whiplash.gui.WlActionTrigger#setEnabled(java.lang.String, boolean, java.lang.Object)
	 */
	@Override
	public void setEnabled(String actionCommand, boolean value, Object obj){
		if(this.menuItems.containsKey(actionCommand))
			this.menuItems.get(actionCommand).setEnabled(value);
		// if "change encoding" is enabled update menu items
		if(actionCommand.equals(GuiActionCommands.ACTION_CHANGE_ENCODING) && value && obj.equals(this.editorPane)){
			// mark the correct current encoding of the document.		
			TextFileDocument doc = (TextFileDocument) this.editorPane.getDocument();
			for(JCheckBoxMenuItem menuItem: this.encodingItems)				
				if(WlCharset.forName(menuItem.getText()).getCharset().equals(doc.getEncoding()))
					menuItem.setSelected(true);
				else menuItem.setSelected(false);	
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent)
	 */
	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		Component component = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
		if(component instanceof JTextComponent)
			WlEditMenuItems.setLastFocusedTextComponent((JTextComponent)component);		
		WlEditMenuItems.updateEnablement();		
		this.lineWrapItem.setSelected((Boolean)WlEditMenuItems.toggleLineWrap.getValue(WlEditMenuItems.CLIENT_PROPERTY_LINE_WRAP));
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent)
	 */
	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

	/* (non-Javadoc)
	 * @see javax.swing.event.PopupMenuListener#popupMenuCanceled(javax.swing.event.PopupMenuEvent)
	 */
	@Override
	public void popupMenuCanceled(PopupMenuEvent e) {}

}