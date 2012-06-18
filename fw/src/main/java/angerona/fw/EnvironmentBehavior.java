package angerona.fw;

public interface EnvironmentBehavior {
	void sendActions();
	
	void receivePerceptions();
	
	boolean runOneTick();
	
	void run();
	
	boolean isAngeronaReady();
	
	boolean isSimulationReady();
}
