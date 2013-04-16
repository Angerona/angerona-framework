package angerona.fw.gui.project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import angerona.fw.AngeronaProject;
import angerona.fw.gui.AngeronaGUIDataStorage;
import angerona.fw.gui.base.Presenter;
import angerona.fw.gui.project.ProjectView.ResourceListener;
import angerona.fw.serialize.Resource;
import angerona.fw.serialize.SimulationConfiguration;

/**
 * The presenter wires the AngeronaProject with a view and delegates
 * the view events to the project.
 * @author Tim Janus
 */
public class ProjectPresenter 
	extends Presenter<AngeronaProject, ProjectView> 
	implements ActionListener, ResourceListener {

	/** the file chooser used to load new resources */
	private JFileChooser fileChooser = new JFileChooser();
	
	/** the currently selected resource */
	private Resource selectedResource;
	
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
		
		view.getLoadButton().addActionListener(this);
		view.getRemoveButton().addActionListener(this);
		view.setResourceListener(this);
	}

	@Override
	protected void unwireViewEvents() {
		model.removeMapObserver(view);
		
		view.getLoadButton().removeActionListener(this);
		view.getRemoveButton().removeActionListener(this);
		view.setResourceListener(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == view.getRemoveButton() &&
				selectedResource != null) {
			model.removeResource(selectedResource);
		} else if(e.getSource() == view.getLoadButton()) {
			fileChooser.showOpenDialog(view.getLoadButton());
			
			File selFile = fileChooser.getSelectedFile();
			if(selFile != null) {
				try {
					model.loadFile(selFile);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	@Override
	public void resourceActivated(Resource resource) {
		if(resource instanceof SimulationConfiguration) {
			AngeronaGUIDataStorage.get().getSimulationControl().setSimulation(
					(SimulationConfiguration) resource);
		}
	}

	@Override
	public void resourceSelected(Resource resource) {
		selectedResource = resource;
	}

}
