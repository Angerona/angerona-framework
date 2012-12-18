package angerona.fw.reflection;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import angerona.fw.error.InvokeException;

/**
 * Implements a while construct for ASML.
 * @author Tim Janus
 *
 */
@Root(name="while")
public class XMLWhile extends XMLCommandoSequence {
	
	@Attribute(name="condition")
	private Condition condition;
	
	/** the number of iterations the while loop has performed so far */
	private int iterations = 0;
	
	public XMLWhile(@Attribute(name="condition") Condition condition) {
		this.condition = condition;
	}
	
	@Override
	protected void executeInternal() throws InvokeException {
		condition.setContext(getContext());
		while(condition.evaluate()) {
			super.executeInternal();
			++iterations;
		}
	}
	
	public int getIterations() {
		return iterations;
	}
}
