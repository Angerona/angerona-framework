package interactive;

import javax.swing.JFrame;

import com.github.angerona.fw.AngeronaEnvironment;
import com.github.angerona.fw.NextActionRequester;
import com.github.angerona.fw.gui.AngeronaWindow;

public class InteractiveAgentNextActionRequester implements NextActionRequester {

	private AngeronaEnvironment environment;

	
	public InteractiveAgentNextActionRequester(AngeronaEnvironment environment){
		this.environment = environment;
	}
	@Override
	public synchronized void request() {
		AngeronaWindow.get().getMainWindow().setEnabled(false);
		final InteractiveBarMVPComponent barMVPComponent= new InteractiveBarMVPComponent(environment, Thread.currentThread());
			javax.swing.SwingUtilities.invokeLater(new Runnable(){

				@Override
				public void run() {
					JFrame frame = ((InteractiveBar) barMVPComponent.getPanel()).getFrame();
					frame.setVisible(true);
				}
			});
		try {
			while(((InteractiveBar) barMVPComponent.getPanel()).getFrame().isDisplayable()){
				wait();
			}			
		} catch (InterruptedException e) {
			// expected interrupt, do nothing
		}
		AngeronaWindow.get().getMainWindow().setEnabled(true);
		//return barMVPComponent.getHasAction();
	}

}
