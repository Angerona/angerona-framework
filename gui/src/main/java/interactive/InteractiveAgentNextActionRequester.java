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
	public boolean request() {
		final InteractiveBarMVPComponent barMVPComponent= new InteractiveBarMVPComponent(environment);
		try {
			javax.swing.SwingUtilities.invokeAndWait(new Runnable(){

				@Override
				public void run() {
					JFrame frame = ((InteractiveBar) barMVPComponent.getPanel()).getFrame();
					frame.setVisible(true);
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return barMVPComponent.getHasAction();
	}

}
