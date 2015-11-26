package com.github.kreaturesfw.gui.interactive;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;

import com.github.kreaturesfw.core.Angerona;
import com.github.kreaturesfw.core.legacy.AngeronaEnvironment;
import com.github.kreaturesfw.core.legacy.NextActionRequester;

public class InteractiveAgentNextActionRequester implements NextActionRequester {

	private AngeronaEnvironment environment;

	public InteractiveAgentNextActionRequester(AngeronaEnvironment environment){
		this.environment = environment;
	}
	
	@Override
	public synchronized void request() {
	
		final InteractiveBarMVPComponent barMVPComponent= new InteractiveBarMVPComponent(environment, Thread.currentThread());
		try {
			javax.swing.SwingUtilities.invokeAndWait(new Runnable(){

				@Override
				public void run() {
					JFrame frame = ((InteractiveBar) barMVPComponent.getPanel()).getFrame();
					frame.setVisible(true);
				}
			});
		} catch (InvocationTargetException e1) {
			Angerona.getInstance().onError("Interactive User-Input Internal Error", e1.getMessage());
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			Angerona.getInstance().onError("Interactive User-Input interruped", e1.getMessage());
			e1.printStackTrace();
		}
			
		try {
			while(((InteractiveBar) barMVPComponent.getPanel()).getFrame().isDisplayable()) {
				wait();
			}			
		} catch (InterruptedException e) {
			// expected interrupt, do nothing
		}
	}

}
