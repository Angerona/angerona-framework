package interactive;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.github.angerona.fw.InteractiveAgent;
import com.github.angerona.fw.comm.Query;
import com.github.angerona.fw.comm.Revision;
import com.github.angerona.fw.comm.SpeechAct;
import com.github.angerona.fw.error.AngeronaException;
import com.github.angerona.fw.gui.base.Presenter;
import com.github.angerona.fw.reflection.FolFormulaVariable;

/**
 * This class is responsible to wire a InteractiveModelAdapter with a
 * InteractiveBar.
 * 
 * @author Pia Wierzoch
 */
public class InteractivePresenter 
	extends Presenter<InteractiveModelAdapter, InteractiveBar>
	implements ActionListener, WindowListener{
	
	/** Default Ctor: The user has to call setModel() and setView(). */
	public InteractivePresenter() {}
	
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
		view.getFrame().addWindowListener(this);
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
				a.setHasPerception(true);
				JFrame frame = ((InteractiveBar) view).getFrame();
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		}else{// FinischButton was pressed
			a.setHasPerception(false);
			JFrame frame = ((InteractiveBar) view).getFrame();
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
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
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		caller.interrupt();
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
