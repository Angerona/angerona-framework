package interactive;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import angerona.fw.Agent;
import angerona.fw.comm.Query;
import angerona.fw.comm.Revision;
import angerona.fw.comm.SpeechAct;
import angerona.fw.error.AngeronaException;
import angerona.fw.gui.base.Presenter;
import angerona.fw.logic.ScriptingComponent;
import angerona.fw.reflection.FolFormulaVariable;

/**
 * This class is responsible to wire a InteractiveModelAdapter with a
 * InteractiveBar.
 * 
 * @author Pia Wierzoch
 */
public class InteractivePresenter 
	extends Presenter<InteractiveModelAdapter, InteractiveBar>
	implements ActionListener {
	
	/** Default Ctor: The user has to call setModel() and setView(). */
	public InteractivePresenter() {}
	
	
	private Agent a;
	/** 
	 * Ctor: Invokes setModel() and setView()
	 * @param model	The used model.
	 * @param view	The used view.
	 */
	public InteractivePresenter(InteractiveModelAdapter model, InteractiveBar view) {
		setModel(model);
		setView(view);
		this.a = model.getAgent();
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		String action = view.getActionType().getSelectedItem().toString();
		String receiver = view.getReceiver().getSelectedItem().toString();
		String text = view.getTextField().getText();
		
		
		SpeechAct act = null;
		
		
		if(action.equals("Query")){
			try {
				act = new Query(a.getName(), receiver, new FolFormulaVariable((new FolFormulaVariable()).createInstanceFromString(text)));
			} catch (AngeronaException e) {
				JOptionPane.showMessageDialog(view.getActionButton(), "Could not parse action, please check for syntax errors.", "Parser error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}else{
			try {
				act = new Revision(a.getName(), receiver, new FolFormulaVariable((new FolFormulaVariable()).createInstanceFromString(text)));
			} catch (AngeronaException e) {
				JOptionPane.showMessageDialog(view.getActionButton(), "Could not parse action, please check for syntax errors.", "Parser error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		if(act != null){
			//TODO desire scriptingProcessing must be create
			a.getComponent(ScriptingComponent.class).add(act);
		}
	}

	@Override
	protected void forceUpdate() {
	}

	@Override
	protected void wireViewEvents() {
		view.getActionButton().addActionListener(this);
	}

	@Override
	protected void unwireViewEvents() {
		view.getActionButton().removeActionListener(this);
	}
}
