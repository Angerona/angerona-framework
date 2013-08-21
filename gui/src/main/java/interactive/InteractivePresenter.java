package interactive;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import angerona.fw.InteractiveAgent;
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
	implements ActionListener{
	
	/** Default Ctor: The user has to call setModel() and setView(). */
	public InteractivePresenter() {}
	
	private boolean hasAction;
	private Thread caller;
	
	private InteractiveAgent a;
	/** 
	 * Ctor: Invokes setModel() and setView()
	 * @param model	The used model.
	 * @param view	The used view.
	 */
	public InteractivePresenter(InteractiveModelAdapter model, InteractiveBar view, Thread caller) {
		setModel(model);
		setView(view);
		this.a = model.getAgent();
		this.caller = caller;
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
				this.hasAction = true;
				JFrame frame = ((InteractiveBar) view).getFrame();
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				caller.interrupt();
			}
		}else{// FinischButton was pressed
			this.hasAction = false;
			JFrame frame = ((InteractiveBar) view).getFrame();
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			caller.interrupt();
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
	
	public boolean getHasAction(){
		return hasAction;
	}
}
