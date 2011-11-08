package angerona.fw;

/**
 * Abstract base class for processes
 * 
 * TODO: extend a process which runs a list of operators.
 * @author Tim Janus
 */
public abstract class Process implements Runnable {

	/** the name of the process */
	private String name;
	
	public Process(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public abstract void run();
	
}
