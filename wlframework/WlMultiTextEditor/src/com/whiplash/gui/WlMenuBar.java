package com.whiplash.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import com.whiplash.config.*;
import com.whiplash.control.*;
import com.whiplash.doc.*;
import com.whiplash.res.*;

/**
 * This is the main menu bar for the editor.
 * 
 * @author Matthias Thimm
 */
public class WlMenuBar extends JMenuBar implements WlActionTrigger, MenuListener, ActionListener {
	
	/** The key for the client property of menu item referring to a file. */
	public static final String FILEOFOPENRECENTACTION = "FILEOFOPENRECENTACTION";
	/** Action command for focusing some window. */
	private static final String ACTION_WINDOW_TO_FRONT = "WINDOW-TO-FRONT";
	/** The key for the client property of menu item referring to a window. */
	private static final String WINDOWTOFRONT = "WINDOW-TO-FRONT";

	/** For serialization. */
	private static final long serialVersionUID = 1L;

	/** The action listener assigned to items of this menu bar. */
	private ActionListener actionListener;
	/** The window set this menu bar belongs to. */
	private WlWindowSet windowSet;
	/** Menu items regarding the encoding. */
	private java.util.List<JCheckBoxMenuItem> encodingItems;
	/** Menu for "edit". */
	private JMenu editMenu;
	/** Menu for "open recent". */
	private JMenu recentMenu;
	/** Menu for "text". */
	private JMenu textMenu;
	/** Menu for "view". */
	private JMenu viewMenu;
	/** clear history item. */
	private JMenuItem clearHistoryItem;
	/** Menu for "windows". */
	private JMenu windowMenu;
	/** the file explorer item. */
	private JCheckBoxMenuItem fileExplorerItem;
	/** the soft line wrap item. */
	private JCheckBoxMenuItem lineWrapItem;
	
	/** A map from action commands to the corresponding menu item. */
	private Map<String,JMenuItem> menuItems;
	
	/** The list of displayed windows. */
	private java.util.List<JCheckBoxMenuItem> windows;
	
	/**
	 * Creates a new menu for the given action listener.
	 * @param actionListener an action listener.
	 */
	public WlMenuBar(ActionListener actionListener){
		super();
		this.actionListener = actionListener;
		this.menuItems = new HashMap<String,JMenuItem>();
		this.encodingItems = new LinkedList<JCheckBoxMenuItem>();
		this.windows = new LinkedList<JCheckBoxMenuItem>();
		// add menu structure
		// check resource manager
		if(!WlResourceManager.hasDefaultResourceManager())			
			throw new RuntimeException("No default resource manager set.");
		WlResourceManager resourceManager = WlResourceManager.getDefaultResourceManager();
		// add file menu
		JMenu fileMenu = new JMenu(resourceManager.getLocalizedText(WlText.FILE));
		this.addMenuItemToMenu(fileMenu, resourceManager.getLocalizedText(WlText.NEW), GuiActionCommands.ACTION_NEW_FILE, actionListener, KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		this.addMenuItemToMenu(fileMenu, resourceManager.getLocalizedText(WlText.OPEN_DOTS), GuiActionCommands.ACTION_OPEN_FILE, actionListener, KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		this.recentMenu = new JMenu(resourceManager.getLocalizedText(WlText.OPENRECENT));
		this.recentMenu.addMenuListener(this);
		this.recentMenu.setActionCommand(GuiActionCommands.ACTION_OPEN_RECENT_FILE);
		this.recentMenu.addActionListener(actionListener);
		this.menuItems.put(GuiActionCommands.ACTION_OPEN_RECENT_FILE, this.recentMenu);
		this.clearHistoryItem = new JMenuItem(resourceManager.getLocalizedText(WlText.CLEARHISTORY));
		this.clearHistoryItem.setActionCommand(GuiActionCommands.ACTION_CLEAR_RECENT_HISTORY);
		this.clearHistoryItem.addActionListener(actionListener);
		this.menuItems.put(GuiActionCommands.ACTION_CLEAR_RECENT_HISTORY, this.clearHistoryItem);
		fileMenu.add(this.recentMenu);
		fileMenu.addSeparator();
		this.addMenuItemToMenu(fileMenu, resourceManager.getLocalizedText(WlText.CLOSE), GuiActionCommands.ACTION_CLOSE_FILE, actionListener, KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.addSeparator();
		this.addMenuItemToMenu(fileMenu, resourceManager.getLocalizedText(WlText.SAVE), GuiActionCommands.ACTION_SAVE_FILE, actionListener, KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		this.addMenuItemToMenu(fileMenu, resourceManager.getLocalizedText(WlText.SAVEAS_DOTS), GuiActionCommands.ACTION_SAVE_AS_FILE, actionListener, KeyStroke.getKeyStroke(KeyEvent.VK_S, (java.awt.event.InputEvent.SHIFT_MASK | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()))));
		this.add(fileMenu);
		// add edit menu
		this.editMenu = new JMenu(resourceManager.getLocalizedText(WlText.EDIT));		
		this.editMenu.add(new JMenuItem(WlEditMenuItems.undoAction));
		this.editMenu.add(new JMenuItem(WlEditMenuItems.redoAction));
		this.editMenu.addSeparator();
		this.editMenu.add(new JMenuItem(WlEditMenuItems.cutAction));
		this.editMenu.add(new JMenuItem(WlEditMenuItems.copyAction));
		this.editMenu.add(new JMenuItem(WlEditMenuItems.pasteAction));
		this.editMenu.add(new JMenuItem(WlEditMenuItems.selectAllAction));
		this.editMenu.addSeparator();
		JMenu encodingMenu = WlEditMenuItems.getEncodingMenu(actionListener, this.encodingItems);
		this.menuItems.put(GuiActionCommands.ACTION_CHANGE_ENCODING, encodingMenu);
		this.editMenu.add(encodingMenu);
		this.add(this.editMenu);
		this.editMenu.addMenuListener(this);
		// add edit menu
		this.textMenu = new JMenu(resourceManager.getLocalizedText(WlText.TEXT));
		this.addMenuItemToMenu(this.textMenu, resourceManager.getLocalizedText(WlText.FIND), EditorPaneActionCommands.ACTION_FIND, actionListener, KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		this.addMenuItemToMenu(this.textMenu, resourceManager.getLocalizedText(WlText.FINDNEXT), EditorPaneActionCommands.ACTION_FIND_NEXT, actionListener, KeyStroke.getKeyStroke(KeyEvent.VK_G, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		this.textMenu.addSeparator();
		this.lineWrapItem = new JCheckBoxMenuItem(WlEditMenuItems.toggleLineWrap);
		this.textMenu.add(this.lineWrapItem);
		this.add(this.textMenu);
		this.textMenu.addMenuListener(this);
		// add view menu
		this.viewMenu = new JMenu(resourceManager.getLocalizedText(WlText.VIEW));
		this.viewMenu.add(new JMenuItem(WlEditMenuItems.fontBiggerAction));
		this.viewMenu.add(new JMenuItem(WlEditMenuItems.fontSmallerAction));
		this.viewMenu.addSeparator();
		this.fileExplorerItem = new JCheckBoxMenuItem(resourceManager.getLocalizedText(WlText.FILE_EXPLORER_TITLE));
		this.fileExplorerItem.setState(true);
		this.fileExplorerItem.addActionListener(actionListener);
		this.fileExplorerItem.setActionCommand(GuiActionCommands.ACTION_SHOW_FILE_EXPLORER);
		this.viewMenu.add(this.fileExplorerItem);
		this.add(this.viewMenu);
		this.viewMenu.addMenuListener(this);
		// add window menu
		this.windowMenu = new JMenu(resourceManager.getLocalizedText(WlText.WINDOW));
		this.addMenuItemToMenu(this.windowMenu, resourceManager.getLocalizedText(WlText.MINIMIZE_WINDOW), GuiActionCommands.ACTION_MINIMZE_WINDOW, actionListener, KeyStroke.getKeyStroke(KeyEvent.VK_M, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		this.addMenuItemToMenu(this.windowMenu, resourceManager.getLocalizedText(WlText.MINIMIZE_WINDOW_ALL), GuiActionCommands.ACTION_MINIMZE_WINDOW_ALL, actionListener, KeyStroke.getKeyStroke(KeyEvent.VK_M, (java.awt.event.InputEvent.ALT_DOWN_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		this.addMenuItemToMenu(this.windowMenu, resourceManager.getLocalizedText(WlText.ZOOM), GuiActionCommands.ACTION_ZOOM_WINDOW, actionListener, null);		
		this.windowMenu.addSeparator();
		this.addMenuItemToMenu(this.windowMenu, resourceManager.getLocalizedText(WlText.BRING_ALL_TO_FRONT), GuiActionCommands.ACTION_WINDOWS_TO_FRONT, actionListener, null);
		this.windowMenu.addSeparator();
		this.windowMenu.addMenuListener(this);
		this.add(this.windowMenu);
		// add help menu
		JMenu helpMenu = new JMenu(resourceManager.getLocalizedText(WlText.HELP));
		//TODO make the following more general
		this.addMenuItemToMenu(helpMenu, "Whiplash " + resourceManager.getLocalizedText(WlText.HELP), GuiActionCommands.ACTION_HELP, actionListener, null);
		this.add(helpMenu);
	}
	
	/** Sets the window set of this menu bar.
	 * @param windowSet some window set.
	 */
	public void setWindowSet(WlWindowSet windowSet){
		this.windowSet = windowSet;
	}
	
	/** Sets the state of the file explorer item.
	 * @param state a boolean.
	 */
	public void setCheckedFileExplorer(boolean state){
		this.fileExplorerItem.setState(state);
	}
	
	/** Auxiliary methods, creates a new menu item with the given parameters
	 * and adds it to the given menu.
	 * @param menu a menu
	 * @param label a string.
	 * @param actionCommand the action command string.
	 * @param actionListener an action listener.
	 * @param accelerator an optional mnemonic.
	 * @return the menu item just created.
	 */
	private JMenuItem addMenuItemToMenu(JMenu menu, String label, String actionCommand, ActionListener actionListener, KeyStroke accelerator){		
		JMenuItem menuItem = new JMenuItem(label);
		menuItem.setActionCommand(actionCommand);
		menuItem.addActionListener(actionListener);
		if(accelerator != null)
			menuItem.setAccelerator(accelerator);
		this.menuItems.put(actionCommand, menuItem);
		menu.add(menuItem);
		return menuItem;
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.gui.WlActionTrigger#setEnabled(java.lang.String, boolean, java.lang.Object)
	 */
	@Override
	public void setEnabled(String actionCommand, boolean value, Object obj){
		if(this.menuItems.containsKey(actionCommand))
			this.menuItems.get(actionCommand).setEnabled(value);
		// if "change encoding" is enabled update menu items
		if(actionCommand.equals(GuiActionCommands.ACTION_CHANGE_ENCODING) && value){			
			// mark the correct current encoding of the document.		
			TextFileDocument doc = (TextFileDocument) ((WlEditorPane<?>)obj).getDocument();
			for(JCheckBoxMenuItem menuItem: this.encodingItems)				
				if(WlCharset.forName(menuItem.getText()).getCharset().equals(doc.getEncoding()))
					menuItem.setSelected(true);
				else menuItem.setSelected(false);	
		}
	}

	/** Refreshes the content of the "open recent" menu. */
	private void refreshRecent(){
		this.recentMenu.removeAll();
		java.util.List<File> current = WlMteConfigurationOptions.CONF_RECENT_FILES.getValue();
		for(File file: current){
			JMenuItem menuItem = new JMenuItem(file.getName() + " (" + file.getParent() + ")");
			menuItem.setActionCommand(GuiActionCommands.ACTION_OPEN_RECENT_FILE);
			menuItem.addActionListener(this.actionListener);
			menuItem.putClientProperty(WlMenuBar.FILEOFOPENRECENTACTION, file);
			this.recentMenu.add(menuItem);
		}
		this.recentMenu.addSeparator();
		this.recentMenu.add(this.clearHistoryItem);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.MenuListener#menuSelected(javax.swing.event.MenuEvent)
	 */
	@Override
	public void menuSelected(MenuEvent e) {		
		if(e.getSource() == this.editMenu)
			WlEditMenuItems.updateEnablement();
		else if(e.getSource() == this.textMenu){
			WlEditMenuItems.updateEnablement();
			this.lineWrapItem.setSelected((Boolean)WlEditMenuItems.toggleLineWrap.getValue(WlEditMenuItems.CLIENT_PROPERTY_LINE_WRAP));
		}else if(e.getSource() == this.recentMenu)
			this.refreshRecent();		
		else if(e.getSource() == this.windowMenu){
			// clear old window entries
			for(JMenuItem menuItem: this.windows)
				this.windowMenu.remove(menuItem);
			// populate
			for(WlWindow window: this.windowSet){
				JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(window.getWindowTitleSupplement()); 
				menuItem.setActionCommand(WlMenuBar.ACTION_WINDOW_TO_FRONT);
				menuItem.addActionListener(this);
				if(window == windowSet.getFocusedWindow())
					menuItem.setState(true);
				this.windowMenu.add(menuItem);
				menuItem.putClientProperty(WlMenuBar.WINDOWTOFRONT, window);
				this.windows.add(menuItem);
			}
			
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.MenuListener#menuDeselected(javax.swing.event.MenuEvent)
	 */
	@Override
	public void menuDeselected(MenuEvent e) { }

	/* (non-Javadoc)
	 * @see javax.swing.event.MenuListener#menuCanceled(javax.swing.event.MenuEvent)
	 */
	@Override
	public void menuCanceled(MenuEvent e) { }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getActionCommand().equals(WlMenuBar.ACTION_WINDOW_TO_FRONT)){
			((WlWindow)((JMenuItem)arg0.getSource()).getClientProperty(WlMenuBar.WINDOWTOFRONT)).toFront();
			((WlWindow)((JMenuItem)arg0.getSource()).getClientProperty(WlMenuBar.WINDOWTOFRONT)).requestFocus();
		}
		
	}

}
