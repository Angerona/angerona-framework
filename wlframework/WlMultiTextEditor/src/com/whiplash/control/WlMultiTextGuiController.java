package com.whiplash.control;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.text.*;

import com.whiplash.config.*;
import com.whiplash.doc.*;
import com.whiplash.event.*;
import com.whiplash.gui.*;
import com.whiplash.gui.events.*;
import com.whiplash.util.*;

/**
 * The main gui controller for a multi-text editor.
 * @author Matthias Thimm
 */
public class WlMultiTextGuiController implements WlCloserController, ActionListener, WlComponentListener, WlWindowListener, WlDocumentControllerListener {

	/** The document controllers associated with this gui controller. The keys of the map
	 * are the files that are accepted by the given controller. */
	private java.util.List<WlDocumentController<?>> documentControllers;
	/** The default document controller (which handles all files not accepted by another controller). */
	private WlDocumentController<?> defaultDocumentController;
	/** The standard document controller (set by the application). */
	private WlDocumentController<?> standardDocumentController;
	
	/** The window set for the gui. */
	private WlWindowSet windowSet;
	/** The menu bar for the gui. */
	private WlMenuBar menuBar;
	/** Keeps track of all action trigger in the gui. */
	private java.util.List<WlActionTrigger> actionTrigger;
	
	/** The currently focused component. */
	private WlComponent focusedComponent = null;
	/** The properties for the gui, see GuiProperty for default values. */
	private Hashtable<GuiProperty,Object> properties;
	
	/** The preferences window. */
	private WlPreferencesWindow preferencesWindow;
	/** The file explorer. */
	private WlFileExplorer fileExplorer;
	
	/** Creates a new gui controller. */
	public WlMultiTextGuiController(){
		this.documentControllers = new LinkedList<WlDocumentController<?>>();
		this.defaultDocumentController = new WlDocumentController<PlainTextFileDocument>(new PlainTextFileDocumentFactory(),"");
		this.defaultDocumentController.addDocumentControllerListener(this);
		this.standardDocumentController = this.defaultDocumentController;
		// init preferences window
		this.preferencesWindow = new WlPreferencesWindow();
		// init file explorer
		this.fileExplorer = new WlFileExplorer(this);
		// register to edit actions
		WlEditMenuItems.init(this);
		// if this system is a Mac OS:
		// - enable the menu bar to be attached to the Mac OS menu bar.
		// - register handlers for Mac OS events
		if(OsTools.isMacOS()){
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			// Generate and register the OSXAdapter, passing it a hash of all the methods we wish to
            // use as delegates for various com.apple.eawt.ApplicationListener methods
			try{
            OSXAdapter.setQuitHandler(this, getClass().getDeclaredMethod("handleQuit", (Class[])null));
            OSXAdapter.setAboutHandler(this, getClass().getDeclaredMethod("handleAbout", (Class[])null));
            OSXAdapter.setPreferencesHandler(this, getClass().getDeclaredMethod("handlePreferences", (Class[])null));
            OSXAdapter.setFileHandler(this, getClass().getDeclaredMethod("handleOpenFile", new Class[] { String.class }));
			}catch(NoSuchMethodException e){
				// this should not happen
				throw new RuntimeException(e);
			}
		}
		this.menuBar = new WlMenuBar(this);
		this.actionTrigger = new LinkedList<WlActionTrigger>();
		this.actionTrigger.add(this.menuBar);
		this.windowSet = new WlWindowSet(this.menuBar);
		this.windowSet.addWlComponentListener(this);
		this.windowSet.addWlComponentListener(this.fileExplorer);
		this.windowSet.addWlWindowListener(this);		
		this.windowSet.addCloserController(this);
		this.menuBar.setWindowSet(this.windowSet);
		this.properties = new Hashtable<GuiProperty,Object>();
	}
	
	/** Sets the property identified by key (which has to be one mentioned
	 * in GuiProperty) to the given value.
	 * @param key one in GuiProperty 
	 * @param value the value
	 * @return the previous value of the key if there is one or "null".
	 */
	public Object setProperty(GuiProperty key, Object value){		
		return this.properties.put(key, value);
	}
	
	/** Returns the value of the given key or null iff there is none.
	 * @param key one in GuiProperty.
	 * @return the value of the key.
	 */
	public Object getProperty(GuiProperty key){
		return this.properties.get(key);
	}
	
	/** Perform some initialization on the window set etc. */
	@SuppressWarnings("unchecked")
	public void init(){
		// init windows
		WlWindow window = this.windowSet.createWindow((String) GuiProperty.APPLICATION_NAME.value(this.properties));
		//TODO the following is not very satisfactory
		window.setDisposeWhenEmpty(BorderLayout.CENTER, false);
		WlToolBar standardToolBar = new WlToolBar(this);		
		java.util.List<String> actions = (java.util.List<String>)GuiProperty.STANDARD_TOOLBAR_BUTTONS.value(this.properties);
		for(String s: actions)
			if(s == null) standardToolBar.addSeparator();
			else standardToolBar.addButton(GuiActionCommands.getActionName(s), GuiActionCommands.getActiveIcon(s), GuiActionCommands.getInactiveIcon(s), s);		
		window.addToolBar(standardToolBar);
		this.actionTrigger.add(standardToolBar);		
		// open an empty document
		TextFileDocument doc = this.standardDocumentController.newDocument(WlCharset.defaultCharset().getCharset());
		WlEditorPane<TextFileDocument> editorPane = new WlEditorPane<TextFileDocument>(doc);
		this.addToDefaultEditorPaneLocation(editorPane);
		WlTextDocumentPopupMenu popupMenu = new WlTextDocumentPopupMenu(this, editorPane);
		editorPane.setPopupMenu(popupMenu);
		this.actionTrigger.add(popupMenu);
		// add file explorer
		this.windowSet.get(0).addWlComponent(this.fileExplorer, BorderLayout.WEST);		
		window.pack();
		window.zoom();		
	}
	
	/** Sets all windows in the window set to visible. */
	public void show(){
		for(WlWindow window: windowSet){			
			window.setVisible(true);
		}
	}
	
	/** Adds the given document controller to this gui's controllers.
	 * @param controller a document controller.
	 */
	public void addDocumentController(WlDocumentController<?> controller){
		this.documentControllers.add(controller);
		controller.addDocumentControllerListener(this);
	}
	
	/** Removes the given document controller from this gui's controllers.
	 * @param documentController a document controller.
	 * @return "true" if the removal was successful.
	 */
	public boolean removeDocumentController(WlDocumentController<?> documentController){
		if(this.standardDocumentController.equals(documentController))
			this.standardDocumentController = this.defaultDocumentController;
		documentController.removeDocumentControllerListener(this);
		return this.documentControllers.remove(documentController);
	}
	
	/** Sets the standard document controller. It is added to this controller's
	 * document controller when not already present.
	 * @param controller a document controller.
	 */
	public void setStandardDocumentController(WlDocumentController<?> controller){
		this.standardDocumentController = controller;
		this.documentControllers.add(controller);
		controller.addDocumentControllerListener(this);
	}


	/** Determines the document controller which handles the document of the given editor pane.
	 * @param editorPane an editor pane.
	 * @return the document controller which handles the document of the given editor pane (or null if there is none).
	 */
	protected WlDocumentController<?> getHandlingDocumentController(WlEditorPane<?> editorPane){
		for(WlDocumentController<?> documentController: this.documentControllers)
			if(documentController.isHandling(editorPane.getDocument()))
				return documentController;				
		if(this.defaultDocumentController.isHandling(editorPane.getDocument()))
			return this.defaultDocumentController;
		return null;
	}
	
	/** Determines the document controller which handles the given file.
	 * @param file some file.
	 * @return the document controller which handles the given file (or null if there is none).
	 */
	protected WlDocumentController<?> getHandlingDocumentController(File file){
		for(WlDocumentController<?> documentController: this.documentControllers)
			if(documentController.isHandling(file))
				return documentController;				
		if(this.defaultDocumentController.isHandling(file))
			return this.defaultDocumentController;
		return null;
	}
	
	/** Determines the document controller which accepts the given file.
	 * @param file some file.
	 * @return the document controller which accepts the given file.
	 */
	protected WlDocumentController<?> getAcceptingDocumentController(File file){
		for(WlDocumentController<?> documentController: this.documentControllers)
			if(documentController.accepts(file))
				return documentController;
		return this.defaultDocumentController;
	}
	
	/** Adds the given editor pane to the default location for newly opened editor panes.
	 * @param editorPane an editor pane
	 */
	private void addToDefaultEditorPaneLocation(WlEditorPane<?> editorPane){
		//TODO: make the following better
		if(this.windowSet.isEmpty())
			// this cannot happen
			throw new RuntimeException("No window visible but I should add a new editor pane.");			
		this.windowSet.get(0).addWlComponent(editorPane, BorderLayout.CENTER);
	}
	
	/** Returns the focused component.
	 * @return the focused component.
	 */
	public WlComponent getFocusedComponent(){
		return this.focusedComponent;
	}
	
	/** Gives the first editor pane viewing the given file focus (if the current focused component
	 * is an editor pane that views the file this methods does nothing).
	 * @param file some file.
	 */
	public void focusFile(File file){
		if(this.focusedComponent != null && this.focusedComponent instanceof WlEditorPane<?>){
			if(((TextFileDocument)((WlEditorPane<?>)this.focusedComponent).getDocument()).getFile().equals(file)){
				return;
			}
		}
		TextFileDocument doc = this.getAcceptingDocumentController(file).getDocumentForFile(file);
		Collection<WlEditorPane<?>> editorPanes = doc.getEditorPanes();
		if(!editorPanes.isEmpty())
			this.windowSet.activateComponent(editorPanes.iterator().next());
	}
	
	/** Checks whether the given file is currently opened.
	 * @param file some file.
	 * @return "true" iff the given file is currently opened.
	 */
	public boolean isOpened(File file){
		return this.getHandlingDocumentController(file) != null;		
	}
	
	/** Closes all editor panes showing the given file.
	 * @param file some file.
	 * @return "true" if the closure was successful.
	 */
	public boolean closeFile(File file){
		Set<WlWindow> windows = new HashSet<WlWindow>(this.windowSet);
		for(WlWindow window: windows)
			for(WlComponent comp: window.getWlComponents()){
				if(comp instanceof WlEditorPane<?>){
					WlEditorPane<?> editorPane = (WlEditorPane<?>) comp;					
					if(((TextFileDocument)editorPane.getDocument()).getFile() != null && ((TextFileDocument)editorPane.getDocument()).getFile().equals(file))
						if(!editorPane.requestClosing())
							return false;					
				}
			}				
		return true;
	}
	
	/** Opens the given file.
	 * @param file some file.
	 */
	public void openFile(File file){
		TextFileDocument doc = this.getAcceptingDocumentController(file).openDocument(file, WlCharset.defaultCharset().getCharset(), null);
		if(doc != null){
			WlEditorPane<TextFileDocument> editorPane = new WlEditorPane<TextFileDocument>(doc);
			WlTextDocumentPopupMenu popupMenu = new WlTextDocumentPopupMenu(this, editorPane);
			editorPane.setPopupMenu(popupMenu);
			this.actionTrigger.add(popupMenu);
			this.addToDefaultEditorPaneLocation(editorPane);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(GuiActionCommands.ACTION_CLOSE_FILE)){
			// if the focused component is not an editor pane do nothing
			if(this.focusedComponent != null && this.focusedComponent instanceof WlEditorPane<?>)
				this.focusedComponent.requestClosing();			
		}else if(e.getActionCommand().equals(GuiActionCommands.ACTION_NEW_FILE)){
			// open an empty document
			TextFileDocument doc = this.standardDocumentController.newDocument(WlCharset.defaultCharset().getCharset());
			WlEditorPane<TextFileDocument> editorPane = new WlEditorPane<TextFileDocument>(doc);
			WlTextDocumentPopupMenu popupMenu = new WlTextDocumentPopupMenu(this, editorPane);
			editorPane.setPopupMenu(popupMenu);
			this.actionTrigger.add(popupMenu);			
			this.addToDefaultEditorPaneLocation(editorPane);
		}else if(e.getActionCommand().equals(GuiActionCommands.ACTION_OPEN_FILE)){
			File file = WlDialog.showOpenFileDialog(null);
			if(file != null)
				this.openFile(file);
		}else if(e.getActionCommand().equals(GuiActionCommands.ACTION_SAVE_AS_FILE)){
			// if the focused component is not an editor pane do nothing
			if(this.focusedComponent != null && this.focusedComponent instanceof WlEditorPane<?>){
				WlEditorPane<?> editorPane = (WlEditorPane<?>) this.focusedComponent;
				WlDocumentController<?> handlingController = this.getHandlingDocumentController(editorPane);				
				File file = WlDialog.showSaveFileDialog(editorPane, handlingController.getExtension());
				handlingController.saveDocument((TextFileDocument)editorPane.getDocument(), file, editorPane);				
			}
		}else if(e.getActionCommand().equals(GuiActionCommands.ACTION_SAVE_FILE)){
			// if the focused component is not an editor pane do nothing
			if(this.focusedComponent != null && this.focusedComponent instanceof WlEditorPane<?>){
				WlEditorPane<?> editorPane = (WlEditorPane<?>) this.focusedComponent;
				WlDocumentController<?> handlingController = this.getHandlingDocumentController(editorPane);
				handlingController.saveDocument((TextFileDocument)editorPane.getDocument(), editorPane);					
			}
		}else if(e.getActionCommand().equals(GuiActionCommands.ACTION_CHANGE_ENCODING)){
			if(this.focusedComponent != null && this.focusedComponent instanceof WlEditorPane<?>){
				WlEditorPane<?> editorPane = (WlEditorPane<?>) this.focusedComponent;
				WlDocumentController<?> documentController = this.getHandlingDocumentController(editorPane);
				TextFileDocument oldDocument = (TextFileDocument)editorPane.getDocument();
				if(oldDocument.getFile() == null)
					oldDocument.setEncoding(WlCharset.forName(((JMenuItem)e.getSource()).getText()).getCharset());					
				else{
					int result = WlDialog.showChangeEncoding(this.focusedComponent);				
					if(result == WlDialog.RELOAD_ACTION){
						documentController.removeDocument(oldDocument);
						TextFileDocument newDocument = documentController.openDocument(oldDocument.getFile(), WlCharset.forName(((JMenuItem)e.getSource()).getText()).getCharset(), editorPane);
						newDocument.addDocumentListener(editorPane);
						editorPane.setDocument(newDocument);
					}else if(result == WlDialog.SET_ACTION){
						oldDocument.setEncoding(WlCharset.forName(((JMenuItem)e.getSource()).getText()).getCharset());					
					}
				}
			}
		}else if(e.getActionCommand().equals(EditorPaneActionCommands.ACTION_FIND)){
			if(this.focusedComponent != null && this.focusedComponent instanceof WlEditorPane<?>){
				WlEditorPane<?> editorPane = (WlEditorPane<?>) this.focusedComponent;
				editorPane.find();
			}
		}else if(e.getActionCommand().equals(EditorPaneActionCommands.ACTION_FIND_NEXT)){
			if(this.focusedComponent != null && this.focusedComponent instanceof WlEditorPane<?>){
				WlEditorPane<?> editorPane = (WlEditorPane<?>) this.focusedComponent;
				editorPane.findNext();
			}
		}else if(e.getActionCommand().equals(GuiActionCommands.ACTION_COPY)){
			Component component;
			if(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() instanceof JTextComponent){
				component = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
			}else component = WlEditMenuItems.getLastFocusedTextComponent();
			if(component instanceof JTextComponent)
				((JTextComponent)component).copy();
		}else if(e.getActionCommand().equals(GuiActionCommands.ACTION_CUT)){
			Component component;
			if(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() instanceof JTextComponent){
				component = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
			}else component = WlEditMenuItems.getLastFocusedTextComponent();
			if(component instanceof JTextComponent)
				((JTextComponent)component).cut();
		}else if(e.getActionCommand().equals(GuiActionCommands.ACTION_PASTE)){
			Component component;
			if(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() instanceof JTextComponent){
				component = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
			}else component = WlEditMenuItems.getLastFocusedTextComponent();
			if(component instanceof JTextComponent)
				((JTextComponent)component).paste();
		}else if(e.getActionCommand().equals(GuiActionCommands.ACTION_SELECT_ALL)){
			Component component;
			if(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() instanceof JTextComponent){
				component = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
			}else component = WlEditMenuItems.getLastFocusedTextComponent();
			if(component instanceof JTextComponent)
				((JTextComponent)component).selectAll();
		}else if(e.getActionCommand().equals(GuiActionCommands.ACTION_UNDO)){
			if(this.focusedComponent != null && this.focusedComponent instanceof WlEditorPane<?>)
				if(((WlEditorPane<?>)this.focusedComponent).getUndoManager().canUndo())
					((WlEditorPane<?>)this.focusedComponent).getUndoManager().undo();
		}else if(e.getActionCommand().equals(GuiActionCommands.ACTION_REDO)){
			if(this.focusedComponent != null && this.focusedComponent instanceof WlEditorPane<?>)
				if(((WlEditorPane<?>)this.focusedComponent).getUndoManager().canRedo())
					((WlEditorPane<?>)this.focusedComponent).getUndoManager().redo();
		}else if(e.getActionCommand().equals(GuiActionCommands.ACTION_CLEAR_RECENT_HISTORY)){
			WlMteConfigurationOptions.CONF_RECENT_FILES.clear();
		}else if(e.getActionCommand().equals(GuiActionCommands.ACTION_OPEN_RECENT_FILE)){
			if(e.getSource() instanceof JMenuItem){
				JMenuItem menuItem = (JMenuItem) e.getSource();
				// retrieve the file
				Object obj = menuItem.getClientProperty(WlMenuBar.FILEOFOPENRECENTACTION);
				if(obj != null && obj instanceof File)
				this.openFile((File)obj);				
			}
		}else if(e.getActionCommand().equals(GuiActionCommands.ACTION_SHOW_FILE_EXPLORER)){
			if(e.getSource() instanceof JCheckBoxMenuItem){
				JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
				if(this.fileExplorer.getWindow() != null){
					if(this.fileExplorer.requestClosing())
						item.setState(false);
				}else{					
					// TODO this should be made better
					this.windowSet.get(0).addWlComponent(this.fileExplorer, BorderLayout.WEST);	
					item.setState(true);
				}
			}
		}else if(e.getActionCommand().equals(GuiActionCommands.ACTION_MINIMZE_WINDOW)){
			if(this.windowSet.getFocusedWindow() != null)
				this.windowSet.getFocusedWindow().setState(Frame.ICONIFIED);
		}else if(e.getActionCommand().equals(GuiActionCommands.ACTION_MINIMZE_WINDOW_ALL)){
			for(WlWindow window: this.windowSet)
				window.setState(Frame.ICONIFIED);
		}else if(e.getActionCommand().equals(GuiActionCommands.ACTION_ZOOM_WINDOW)){
			if(this.windowSet.getFocusedWindow() != null)
				this.windowSet.getFocusedWindow().zoom();
		}else if(e.getActionCommand().equals(GuiActionCommands.ACTION_WINDOWS_TO_FRONT)){
			WlWindow focusedWindow = this.windowSet.getFocusedWindow();
			for(WlWindow window: this.windowSet)
				window.toFront();
			focusedWindow.toFront();
		}else if(e.getActionCommand().equals(GuiActionCommands.ACTION_FONT_BIGGER)){
			if(this.focusedComponent != null && this.focusedComponent instanceof WlEditorPane<?>)
				((WlEditorPane<?>)this.focusedComponent).changeFontSize(true);
		}else if(e.getActionCommand().equals(GuiActionCommands.ACTION_FONT_SMALLER)){
			if(this.focusedComponent != null && this.focusedComponent instanceof WlEditorPane<?>)
				((WlEditorPane<?>)this.focusedComponent).changeFontSize(false);
		}else if(e.getActionCommand().equals(GuiActionCommands.ACTION_TOGGLE_LINE_WRAP)){
			if(this.focusedComponent != null && this.focusedComponent instanceof WlEditorPane<?>)
				((WlEditorPane<?>)this.focusedComponent).toggleLineWrap();
		}
		// update action enablement (some actions might to be applicable any more)
		this.updateActionEnablement();
	}

	/** Appropriately disables/enables actions in menu and tool bars to the 
	 * currently focused component. */
	private void updateActionEnablement(){
		boolean enOpenRecent = WlMteConfigurationOptions.CONF_RECENT_FILES.getValue().size() > 0;
		boolean enCloseFile = this.focusedComponent instanceof WlEditorPane<?>;
		boolean enSaveFile = (this.focusedComponent instanceof WlEditorPane<?>) && ((TextFileDocument)((WlEditorPane<?>)this.focusedComponent).getDocument()).isStained(); 
		boolean enSaveAsFile = this.focusedComponent instanceof WlEditorPane<?>;
		boolean enEncoding = this.focusedComponent instanceof WlEditorPane<?>;
		boolean enFind = this.focusedComponent instanceof WlEditorPane<?>;
		boolean enFindNext = this.focusedComponent instanceof WlEditorPane<?> && !((WlEditorPane<?>)this.focusedComponent).getSearchText().equals("");		
		for(WlActionTrigger actionTrigger: this.actionTrigger){
			actionTrigger.setEnabled(GuiActionCommands.ACTION_OPEN_RECENT_FILE, enOpenRecent, null);
			actionTrigger.setEnabled(GuiActionCommands.ACTION_CLOSE_FILE, enCloseFile, null);
			actionTrigger.setEnabled(GuiActionCommands.ACTION_SAVE_FILE, enSaveFile, null);
			actionTrigger.setEnabled(GuiActionCommands.ACTION_SAVE_AS_FILE, enSaveAsFile, null);
			actionTrigger.setEnabled(GuiActionCommands.ACTION_CHANGE_ENCODING, enEncoding, this.focusedComponent);
			actionTrigger.setEnabled(EditorPaneActionCommands.ACTION_FIND, enFind, null);
			actionTrigger.setEnabled(EditorPaneActionCommands.ACTION_FIND_NEXT, enFindNext, null);		
		}				
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.gui.events.WlComponentListener#componentActionEnablementChanged(com.whiplash.gui.events.WlComponentActionEnablementChangedEvent)
	 */
	public void componentActionEnablementChanged(WlComponentActionEnablementChangedEvent e){
		this.updateActionEnablement();
	}
	
	/** update "window modified" dot for Mac OS X windows. */
	protected void updateWindowModifiedForMacOsX(){
		for(WlWindow window: this.windowSet){
			boolean isModified = false;
			for(WlComponent component: window.getWlComponents()){
				if(component instanceof WlEditorPane<?>){
					if(((TextFileDocument)((WlEditorPane<?>)component).getDocument()).isStained()){
						isModified = true;
						break;
					}							
				}
			}
			window.getRootPane().putClientProperty("Window.documentModified", isModified);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.control.WlCloserController#requestClosing(com.whiplash.gui.WlComponent)
	 */
	@Override
	public boolean requestClosing(WlComponent component) {
		if(component instanceof WlEditorPane<?>){
			WlDocumentController<?> handlingController = this.getHandlingDocumentController(((WlEditorPane<?>) component));
			if(((TextFileDocument)((WlEditorPane<?>)component).getDocument()).isStained())
				this.windowSet.activateComponent(component);	
			// close document only if there is exactly one editor
			// pane watching the document
			if(((WlEditorPane<?>)component).getDocument().getEditorPanes().size() != 1)					
				return true;
			if(!handlingController.mayCloseDocument((TextFileDocument)((WlEditorPane<?>) component).getDocument(), component))
				return false;
			// if the component can be closed but has unsaved changes remove the document in any case
			else if(((TextFileDocument)((WlEditorPane<?>) component).getDocument()).isStained())
						handlingController.removeDocument(((TextFileDocument)((WlEditorPane<?>) component).getDocument()));			
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.control.WlCloserController#requestClosing(com.whiplash.gui.WlWindow)
	 */
	@Override
	public boolean requestClosing(WlWindow window) {
		// collect all editor panes to close
		// the ones that should not be saved and close them anyway.
		java.util.List<WlEditorPane<?>> editorPanes = new LinkedList<WlEditorPane<?>>();
		for(WlComponent component: window.getWlComponents())
			if(component instanceof WlEditorPane<?>){
				if(!component.mayClose()){
					for(WlEditorPane<?> editorPane: editorPanes)
						if(this.getHandlingDocumentController(editorPane) == null)
							editorPane.forceClose();
					return false;
				}
				editorPanes.add((WlEditorPane<?>) component);
			}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.whiplash.gui.events.WlComponentListener#componentChangedTitle(com.whiplash.gui.events.WlComponentTitleChangedEvent)
	 */
	@Override
	public void componentChangedTitle(WlComponentTitleChangedEvent e) {
		if(e.getComponent() == this.focusedComponent){
			// refresh window title
			e.getComponent().getWindow().setTitle(e.getComponent().getTitle());
			// refresh enablement of actions
			this.updateActionEnablement();
		}
		// for Mac OS X: update "window modified" dot
		if(OsTools.isMacOS()) this.updateWindowModifiedForMacOsX();
	}

	/* (non-Javadoc)
	 * @see com.whiplash.gui.events.WlComponentListener#componentGainedFocus(com.whiplash.gui.events.WlComponentFocusGainedEvent)
	 */
	@Override
	public void componentGainedFocus(WlComponentFocusGainedEvent e) {
		this.focusedComponent = e.getComponent();		
		// refresh window title if an editor pane gained focus
		if(e.getComponent() instanceof WlEditorPane<?>)
			e.getComponent().getWindow().setTitle(e.getComponent().getTitle());
		// refresh enablement of actions
		this.updateActionEnablement();
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.gui.events.WlComponentListener#componentLostFocus(com.whiplash.gui.events.WlComponentFocusLostEvent)
	 */
	@Override
	public void componentLostFocus(WlComponentFocusLostEvent e){
		this.focusedComponent = null;
		// Note: The title of the corresponding window has not to changed
		// as only a focus gain of another editor pane might change that.
		// refresh enablement of actions
		this.updateActionEnablement();
	}

	/* (non-Javadoc)
	 * @see com.whiplash.gui.events.WlComponentListener#componentMoved(com.whiplash.gui.events.WlComponentDraggedEvent)
	 */
	@Override
	public void componentDragged(WlComponentDraggedEvent e) {
		// Check whether the drag left a window without any component
		// (in that case the title of the window has to be set to default)
		if(e.getOrigin() != e.getDestination()){
			if(!(e.getOrigin().getFocusedComponent() instanceof WlEditorPane<?>)){
				e.getOrigin().setTitle("");				
			}
			// for Mac OS X: update "window modified" dot
			if(OsTools.isMacOS()) this.updateWindowModifiedForMacOsX();
		}
	}

	/* (non-Javadoc)
	 * @see com.whiplash.gui.events.WlComponentListener#componentClosed(com.whiplash.gui.events.WlComponentClosedEvent)
	 */
	@Override
	public void componentClosed(WlComponentClosedEvent e) {
		// for Mac OS X: update "window modified" dot
		if(OsTools.isMacOS()) this.updateWindowModifiedForMacOsX();
		// check the title of the corresponding window
		if(e.getWindow() != null && !(e.getWindow().getFocusedComponent() instanceof WlEditorPane<?>))
			e.getWindow().setTitle("");		
		// if the closed component was an editor pane remove the document from its document controller
		// but do this only if there is no other editor pane showing the document
		if(e.getComponent() instanceof WlEditorPane<?>){
			WlEditorPane<?> editorPane = (WlEditorPane<?>)e.getComponent();
			if(this.getHandlingDocumentController(editorPane) != null)
				if(((TextFileDocument)editorPane.getDocument()).getEditorPanes().size() == 0)
					this.getHandlingDocumentController(editorPane).removeDocument((TextFileDocument)editorPane.getDocument());
		}
		// if file explorer has been closed update menu item
		if(e.getComponent() == this.fileExplorer)
			this.menuBar.setCheckedFileExplorer(false);		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.apple.eawt.ApplicationAdapter#handleQuit(com.apple.eawt.ApplicationEvent)
	 */
	public boolean handleQuit()  {
		for(WlWindow window: this.windowSet)
			if(!window.mayClose())
				return false;	
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.apple.eawt.ApplicationAdapter#handlePreferences(com.apple.eawt.ApplicationEvent)
	 */
	public void handlePreferences() {
		this.preferencesWindow.setLocationRelativeTo(this.windowSet.getFocusedWindow());
		this.preferencesWindow.setVisible(true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.apple.eawt.ApplicationAdapter#handleAbout(com.apple.eawt.ApplicationEvent)
	 */
	public void handleAbout() {
		//TODO
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.apple.eawt.ApplicationAdapter#handleOpenFile(com.apple.eawt.ApplicationEvent)
	 */
	public void handleOpenFile(String path) {
		File file = new File(path);
		TextFileDocument doc = this.getAcceptingDocumentController(file).openDocument(file, WlCharset.defaultCharset().getCharset(), null);
		WlEditorPane<TextFileDocument> editorPane = new WlEditorPane<TextFileDocument>(doc);
		WlTextDocumentPopupMenu popupMenu = new WlTextDocumentPopupMenu(this, editorPane);
		editorPane.setPopupMenu(popupMenu);
		this.actionTrigger.add(popupMenu);
		this.addToDefaultEditorPaneLocation(editorPane);
	}

	/* (non-Javadoc)
	 * @see com.whiplash.gui.events.WlWindowListener#windowClosed(com.whiplash.gui.events.WlWindowClosedEvent)
	 */
	@Override
	public void windowClosed(WlWindowClosedEvent e) {
		// if no window is open any more dispose all windows.
		if(this.windowSet.isEmpty()){
			this.preferencesWindow.setVisible(false);
			this.preferencesWindow.dispose();
		}
	}

	/* (non-Javadoc)
	 * @see com.whiplash.event.WlDocumentControllerListener#documentOpened(com.whiplash.event.DocumentControllerEvent)
	 */
	@Override
	public void documentOpened(DocumentControllerEvent e) {
		// update recent files
		if(e.getDocument().getFile() != null)
			WlMteConfigurationOptions.CONF_RECENT_FILES.addValue(e.getDocument().getFile());		
		// forward event to file explorer model
		this.fileExplorer.getModel().documentOpened(e);
	}

	/* (non-Javadoc)
	 * @see com.whiplash.event.WlDocumentControllerListener#documentSaved(com.whiplash.event.DocumentControllerEvent)
	 */
	@Override
	public void documentSaved(DocumentControllerEvent e) {
		// update recent files
		if(e.getDocument().getFile() != null)
			WlMteConfigurationOptions.CONF_RECENT_FILES.addValue(e.getDocument().getFile());
		// forward event to file explorer model
		this.fileExplorer.getModel().documentSaved(e);
		// check whether the save operation changed the responsible document controller
		WlDocumentController<?> controller = this.getAcceptingDocumentController(e.getDocument().getFile());
		if(e.getController() != controller){
			e.getController().removeDocument(e.getDocument(),false);
			TextFileDocument doc = controller.openDocument(e.getDocument().getFile(), e.getDocument().getEncoding(), null);
			for(WlEditorPane<?> editorPane: e.getDocument().getEditorPanes())
				editorPane.setDocument(doc);
			return;
		}		
	}

	/* (non-Javadoc)
	 * @see com.whiplash.event.WlDocumentControllerListener#documentStainedStatusChanged(com.whiplash.event.DocumentControllerEvent)
	 */
	public void documentStainedStatusChanged(DocumentControllerEvent e){
		// forward event to file explorer model
		this.fileExplorer.getModel().documentStainedStatusChanged(e);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.event.WlDocumentControllerListener#documentClosed(com.whiplash.event.DocumentControllerEvent)
	 */
	@Override
	public void documentClosed(DocumentControllerEvent e) {
		// forward event to file explorer model
		this.fileExplorer.getModel().documentClosed(e);
	}

	/* (non-Javadoc)
	 * @see com.whiplash.event.WlDocumentControllerListener#documentCreated(com.whiplash.event.DocumentControllerEvent)
	 */
	@Override
	public void documentCreated(DocumentControllerEvent e) {
		// forward event to file explorer model
		this.fileExplorer.getModel().documentCreated(e);
	}
}
