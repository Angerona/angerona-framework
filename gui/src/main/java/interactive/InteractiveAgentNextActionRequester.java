package interactive;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;

import angerona.fw.AngeronaEnvironment;
import angerona.fw.NextActionRequester;

public class InteractiveAgentNextActionRequester implements NextActionRequester {

	private AngeronaEnvironment environment;
	
	public InteractiveAgentNextActionRequester(AngeronaEnvironment environment){
		this.environment = environment;
	}
	@Override
	public synchronized boolean request() {
		final InteractiveBarMVPComponent barMVPComponent= new InteractiveBarMVPComponent(environment, Thread.currentThread());
			javax.swing.SwingUtilities.invokeLater(new Runnable(){

				@Override
				public void run() {
					JFrame frame = ((InteractiveBar) barMVPComponent.getPanel()).getFrame();
					frame.setVisible(true);
				}
			});
		try {
			wait();
		} catch (InterruptedException e) {
			// expected interupt, do nothing
		}
		
		return barMVPComponent.getHasAction();
	}

}
