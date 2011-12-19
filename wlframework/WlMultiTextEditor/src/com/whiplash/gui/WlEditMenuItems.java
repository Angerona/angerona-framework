package com.whiplash.gui;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

import com.whiplash.control.*;
import com.whiplash.doc.*;
import com.whiplash.res.*;

/**
 * This class provides for edit menu items that are shared by the menu bar
 * and the document popup menus.
 * @author Matthias Thimm
 */
public abstract class WlEditMenuItems {

	/** Key for the line wrap property. */
	protected static String CLIENT_PROPERTY_LINE_WRAP = "LINE-WRAP-PROPERTY";
	
	/** The shared actions. */
	private static final String[] ACTIONS = {
		GuiActionCommands.ACTION_COPY,
		GuiActionCommands.ACTION_CUT,
		GuiActionCommands.ACTION_PASTE,
		GuiActionCommands.ACTION_UNDO,
		GuiActionCommands.ACTION_REDO,
		GuiActionCommands.ACTION_SELECT_ALL,
		GuiActionCommands.ACTION_FONT_BIGGER,
		GuiActionCommands.ACTION_FONT_SMALLER,
		GuiActionCommands.ACTION_TOGGLE_LINE_WRAP
	};
	/** Keystrokes for the shared actions. */
	private static final KeyStroke[] KEY_STROKES = {
		KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
		KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
		KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
		KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
		KeyStroke.getKeyStroke(KeyEvent.VK_Z, (java.awt.event.InputEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())),
		KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
		KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
		KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
		null
	};
	/** Texts for the shared actions. */
	private static final WlText[] TEXTS = {
		WlText.COPY,
		WlText.CUT,
		WlText.PASTE,
		WlText.UNDO,
		WlText.REDO,
		WlText.SELECT_ALL,
		WlText.FONT_BIGGER,
		WlText.FONT_SMALLER,
		WlText.LINE_WRAP
	};
	
	/** The Gui controller*/
	private static WlMultiTextGuiController guiController;
	
	/** The copy action. */
	public static Action copyAction;
	/** The cut action. */
	public static Action cutAction;
	/** The paste action. */
	public static Action pasteAction;
	/** The undo action. */
	public static Action undoAction;
	/** The redo action. */
	public static Action redoAction;
	/** The select-all action. */
	public static Action selectAllAction;
	/** The bigger action. */
	public static Action fontBiggerAction;
	/** The smaller action. */
	public static Action fontSmallerAction;
	/** The line wrap action. */
	public static Action toggleLineWrap;
	
	/** for the workaround to get cut/copy/paste to work. */
	private static JTextComponent textComponent = null;
	
	/** Inits edit actions for the given Gui controller.
	 * @param guiController some Gui controller.
	 */
	public static void init(WlMultiTextGuiController guiController){
		WlEditMenuItems.guiController = guiController;
		WlEditMenuItems.copyAction = WlEditMenuItems.getAction(GuiActionCommands.ACTION_COPY, WlEditMenuItems.guiController);
		WlEditMenuItems.cutAction = WlEditMenuItems.getAction(GuiActionCommands.ACTION_CUT, WlEditMenuItems.guiController);
		WlEditMenuItems.pasteAction = WlEditMenuItems.getAction(GuiActionCommands.ACTION_PASTE, WlEditMenuItems.guiController);
		WlEditMenuItems.undoAction = WlEditMenuItems.getAction(GuiActionCommands.ACTION_UNDO, WlEditMenuItems.guiController);
		WlEditMenuItems.redoAction = WlEditMenuItems.getAction(GuiActionCommands.ACTION_REDO, WlEditMenuItems.guiController);
		WlEditMenuItems.selectAllAction = WlEditMenuItems.getAction(GuiActionCommands.ACTION_SELECT_ALL, WlEditMenuItems.guiController);
		WlEditMenuItems.fontBiggerAction = WlEditMenuItems.getAction(GuiActionCommands.ACTION_FONT_BIGGER, WlEditMenuItems.guiController);
		WlEditMenuItems.fontSmallerAction = WlEditMenuItems.getAction(GuiActionCommands.ACTION_FONT_SMALLER, WlEditMenuItems.guiController);
		WlEditMenuItems.toggleLineWrap = WlEditMenuItems.getAction(GuiActionCommands.ACTION_TOGGLE_LINE_WRAP, WlEditMenuItems.guiController);
	}
	
	/** Sets the last focused text component
	 * @param textComponent
	 */
	protected static void setLastFocusedTextComponent(JTextComponent textComponent){
		WlEditMenuItems.textComponent = textComponent;
	}
	
	/** Returns the last focused text component.
	 * @return the last focused text component.
	 */
	public static JTextComponent getLastFocusedTextComponent(){
		return WlEditMenuItems.textComponent;
	}
	
	/** Updates the enablement of edit actions. */
	public static void updateEnablement(){
		// check enablement of cut/copy/paste (this is done only when the menu is selected 
		// due to performance issues and not in the GUI controller)
		Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		WlEditMenuItems.pasteAction.setEnabled(systemClipboard.getContents(null) != null && systemClipboard.isDataFlavorAvailable(DataFlavor.stringFlavor));
		Component component;
		if(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() instanceof JTextComponent){
			component = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
		}else component = WlEditMenuItems.textComponent;
		boolean enableCutCopy = false;
		boolean enableUndo = false;
		boolean enableRedo = false;
		boolean enableSelectAll = false;
		boolean enableFontSizeUpdate = false;
		boolean lineWrapSelected = true;
		if(component instanceof JTextComponent){
			enableSelectAll = true;
			JTextComponent textComponent = (JTextComponent) component;
			if(textComponent.getSelectedText() != null && !textComponent.getSelectedText().equals(""))
				enableCutCopy = true;			
		}
		if(WlEditMenuItems.guiController.getFocusedComponent() != null && WlEditMenuItems.guiController.getFocusedComponent() instanceof WlEditorPane<?> ){
			WlEditorPane<?> editorPane = (WlEditorPane<?>) WlEditMenuItems.guiController.getFocusedComponent();
			enableUndo = editorPane.getUndoManager().canUndo();
			enableRedo = editorPane.getUndoManager().canRedo();
			enableFontSizeUpdate = true;
			lineWrapSelected = editorPane.getLineWrap();
		}
		WlEditMenuItems.undoAction.setEnabled(enableUndo);
		WlEditMenuItems.redoAction.setEnabled(enableRedo);
		WlEditMenuItems.copyAction.setEnabled(enableCutCopy);
		WlEditMenuItems.cutAction.setEnabled(enableCutCopy);
		WlEditMenuItems.selectAllAction.setEnabled(enableSelectAll);
		WlEditMenuItems.fontBiggerAction.setEnabled(enableFontSizeUpdate);
		WlEditMenuItems.fontSmallerAction.setEnabled(enableFontSizeUpdate);
		WlEditMenuItems.toggleLineWrap.putValue(WlEditMenuItems.CLIENT_PROPERTY_LINE_WRAP, lineWrapSelected);
	}
	
	/** Returns the index of the given action command in the local
	 * action commands array.
	 * @param actionCommand some action command.
	 * @return an index or -1 iff the command could not be found.
	 */
	private static int indexOf(String actionCommand){
		for(int i = 0; i < WlEditMenuItems.ACTIONS.length; i++)
			if(actionCommand.equals(WlEditMenuItems.ACTIONS[i]))
				return i;
		return -1;
	}
	
	/** Constructs a menu for changing the encoding of a text document.
	 * @param actionListener some action listener
	 * @param encodingItems this list is filled with the encoding items.
	 * @return a menu for changing the encoding of a text document.
	 */
	public static JMenu getEncodingMenu(ActionListener actionListener, java.util.List<JCheckBoxMenuItem> encodingItems){
		JMenu encodingMenu = new JMenu(GuiActionCommands.getActionName(GuiActionCommands.ACTION_CHANGE_ENCODING));
		for(WlCharset charset: WlCharset.values()){
			JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(charset.getName());
			encodingItems.add(menuItem);
			menuItem.setActionCommand(GuiActionCommands.ACTION_CHANGE_ENCODING);
			menuItem.addActionListener(actionListener);
			encodingMenu.add(menuItem);			
		}
		return encodingMenu;
	}
	
	/** Returns an action for the given action command.
	 * @param actionListener some action listener
	 * @return an action for the given action.
	 */
	private static Action getAction(String actionCommand, final ActionListener actionListener){
		// check resource manager
		if(!WlResourceManager.hasDefaultResourceManager())			
			throw new RuntimeException("No default resource manager set.");
		WlResourceManager resourceManager = WlResourceManager.getDefaultResourceManager();
		Action action = new AbstractAction(){
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) { actionListener.actionPerformed(e); }			
		};
		action.putValue(Action.ACTION_COMMAND_KEY, actionCommand);
		action.putValue(Action.NAME, resourceManager.getLocalizedText(WlEditMenuItems.TEXTS[WlEditMenuItems.indexOf(actionCommand)]));
		if(WlEditMenuItems.KEY_STROKES[WlEditMenuItems.indexOf(actionCommand)] != null)
			action.putValue(Action.ACCELERATOR_KEY, WlEditMenuItems.KEY_STROKES[WlEditMenuItems.indexOf(actionCommand)]);
		return action;
	}
}
