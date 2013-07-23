package interactive;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import angerona.fw.InteractiveAgent;
import angerona.fw.Observer;
import angerona.fw.comm.Query;
import angerona.fw.comm.Revision;
import angerona.fw.comm.SpeechAct;
import angerona.fw.error.AngeronaException;
import angerona.fw.gui.base.Presenter;
import angerona.fw.reflection.FolFormulaVariable;

/**
 * This class is responsible to wire a InteractiveModelAdapter with a
 * InteractiveBar.
 * 
 * @author Pia Wierzoch
 */
public class InteractivePresenter 
	extends Presenter<InteractiveModelAdapter, InteractiveBar>
	implements ActionListener, Observer {
	
	/** Default Ctor: The user has to call setModel() and setView(). */
	public InteractivePresenter() {}
	
	private InteractiveAgent a;
	/** 
	 * Ctor: Invokes setModel() and setView()
	 * @param model	The used model.
	 * @param view	The used view.
	 */
	public InteractivePresenter(InteractiveModelAdapter model, InteractiveBar view) {
		setModel(model);
		setView(view);
		this.a = model.getAgent();
		a.register(this);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		
		if(ev.getSource() == view.getActionButton()) {
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
				a.performAction(act);
				a.setAction(true);
			}
		}else{// FinischButton was pressed
			a.setHasPerception(false);
			a.setAction(true);
		}
	}

	@Override
	protected void forceUpdate() {
	}

	@Override
	protected void wireViewEvents() {
		view.getActionButton().addActionListener(this);
		view.getFinButton().addActionListener(this);
	}

	@Override
	protected void unwireViewEvents() {
		view.getActionButton().removeActionListener(this);
		view.getFinButton().removeActionListener(this);
	}

	@Override
	public void update() {
		JOptionPane.showMessageDialog(view, "Please insert an Action or press the Finish Button!");
	}
}
