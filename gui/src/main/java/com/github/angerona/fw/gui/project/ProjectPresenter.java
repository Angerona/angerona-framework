package com.github.angerona.fw.gui.project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

import com.github.angerona.fw.AngeronaProject;
import com.github.angerona.fw.error.AngeronaException;
import com.github.angerona.fw.gui.AngeronaGUIDataStorage;
import com.github.angerona.fw.gui.base.Presenter;
import com.github.angerona.fw.gui.project.ProjectView.UserObjectFactory;
import com.github.angerona.fw.gui.util.CollectionMonitor.CollectionMonitorListener;
import com.github.angerona.fw.gui.util.DefaultUserObjectWrapper;
import com.github.angerona.fw.gui.util.UserObjectWrapper;
import com.github.angerona.fw.serialize.Resource;
import com.github.angerona.fw.serialize.SimulationConfiguration;

/**
 * The presenter wires the AngeronaProject with a view and delegates
 * the view events to the project.
 * 
 * @author Tim Janus
 */
public class ProjectPresenter 
	extends Presenter<AngeronaProject, ProjectView> 
	implements ActionListener, CollectionMonitorListener {

	/** the file chooser used to load new resources */
	private JFileChooser fileChooser = new JFileChooser();
	
	/**
	 * Ctor: needs the model and view 
	 * @param model	The Angerona project acting as data model
	 * @param view	A class containing Form and Controls which form a view for the Angerona project.
	 */
	public ProjectPresenter(AngeronaProject model, ProjectView view) {
		setModel(model);
		setView(view);
		
		// only allow to open xml files.
		fileChooser.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				return "*.xml";
			}
			
			@Override
			public boolean accept(File file) {
				if(file.isDirectory())
					return true;
				
				return file.getAbsolutePath().endsWith(".xml");
			}
		});
		setCurrentDirectory(new File("."));
	}
	
	/**
	 * Change the directory used by the file chooser for loading new resources.
	 * @param dir	This must be an directory and it must not be null.
	 */
	public void setCurrentDirectory(File dir) {
		if(dir == null || !dir.isDirectory())
			throw new IllegalArgumentException();
		fileChooser.setCurrentDirectory(dir);
	}
	
	@Override
	protected void forceUpdate() {
		view.onProjectChange(model);
	}

	@Override
	protected void wireViewEvents() {
		model.addMapObserver(view);
		
		view.getAddButton().addActionListener(this);
		view.getRemoveButton().addActionListener(this);
		view.setResourcesUserObjectFactory(new ResourceUserObjectFactory());
		view.getResourceCollectionController().addListener(this);
	}

	@Override
	protected void unwireViewEvents() {
		model.removeMapObserver(view);
		
		view.getAddButton().removeActionListener(this);
		view.getRemoveButton().removeActionListener(this);
		view.setResourcesUserObjectFactory(null);
		view.getResourceCollectionController().removeListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == view.getRemoveButton()) {
			onRemove();
		} else if(e.getSource() == view.getAddButton()) {
			fileChooser.showOpenDialog(view.getAddButton());
			
			File selFile = fileChooser.getSelectedFile();
			if(selFile != null) {
				try {
					model.loadFile(selFile);
				} catch (IOException|AngeronaException ex) {
					String msg = "Cannot load: '" + selFile.getAbsolutePath() + 
							"' cause: '" + ex.getMessage() + "'.";
					// TODO: Move in helper class
					JTextArea txtArea = new JTextArea();
					txtArea.setColumns(30);
					txtArea.setLineWrap( true );
					txtArea.setWrapStyleWord( true );
					txtArea.append(msg);
					txtArea.setSize(txtArea.getPreferredSize().width, 1);
					
					JOptionPane.showMessageDialog(view.getRootComponent(), txtArea, 
							"Cannot load!", JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}

	@Override
	public void invokeRemove(JComponent sender) {
		onRemove();
	}
	
	/**
	 * Is called if the selected resources of a project shall be removed. 
	 */
	private void onRemove() {
		for(Resource res: view.getResourceCollectionController().getSelectedUserObjectsOfType(Resource.class)) {
			model.removeResource(res);
		}
	}
	
	/**
	 * Base class for Resource user object wrapper. It uses the Resource name
	 * as name in the UI.
	 * @author Tim Janus
	 *
	 * @param <T>	Type of the Resource
	 */
	private class ResUserObject<T extends Resource> extends DefaultUserObjectWrapper {
		public ResUserObject(T uo) {
			super(uo);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public T getUserObject() {
			return (T)super.getUserObject();
		}
		
		@Override
		public String toString() {
			return getUserObject().getName();
		}
	}
	
	/**
	 * A user object wrapper for simulation configurations, it loads the 
	 * simulation configuration if it is activated.
	 * 
	 * @author Tim Janus
	 */
	private class SimUserObject extends ResUserObject<SimulationConfiguration> {
		public SimUserObject(SimulationConfiguration sc) {
			super(sc);
		}
		@Override
		public void onActivated() {
			AngeronaGUIDataStorage.get().getSimulationControl().setSimulation(getUserObject());
		}
	}
	
	/**
	 * The ResourceUserObjectFactory returns user object wrappers for the given
	 * Resources. It determines the type of the user object wrapper by instanceof
	 * checks of the given Resource.
	 * 
	 * @author Tim Janus
	 */
	private class ResourceUserObjectFactory implements UserObjectFactory {

		@Override
		public UserObjectWrapper createUserObject(Resource res) {
			if(res instanceof SimulationConfiguration) {
				return new SimUserObject((SimulationConfiguration)res);
			} else if(res instanceof Resource) {
				return new ResUserObject<Resource>((Resource)res);
			}
			
			return null;
		}
		
	}
}
