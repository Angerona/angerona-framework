package interactive;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import angerona.fw.gui.base.Presenter;
import angerona.fw.gui.simctrl.SimulationControlModel.SimulationState;
import angerona.fw.serialize.SimulationConfiguration;

/**
 * This class is responsible to wire a SimulationControlModel with a
 * SimulationControlView.
 * 
 * @author Tim Janus
 */
public class InteractivePresenter 
	extends Presenter<InteractiveModelAdapter, InteractiveBar>
	implements ActionListener {
	
	/** Default Ctor: The user has to call setModel() and setView(). */
	public InteractivePresenter() {}
	
	/** 
	 * Ctor: Invokes setModel() and setView()
	 * @param model	The used model.
	 * @param view	The used view.
	 */
	public InteractivePresenter(InteractiveModelAdapter model, InteractiveBar view) {
		setModel(model);
		setView(view);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {

	}

	@Override
	protected void forceUpdate() {
	}

	@Override
	protected void wireViewEvents() {
	}

	@Override
	protected void unwireViewEvents() {
	}
}
