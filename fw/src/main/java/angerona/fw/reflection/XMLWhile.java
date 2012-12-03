package angerona.fw.reflection;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import angerona.fw.error.InvokeException;

/**
 * Implements a while construct for DAML.
 * @author Tim Janus
 *
 */
@Root(name="while")
public class XMLWhile extends XMLCommandoSequence {
	
	@Attribute(name="condition")
	private Condition condition;
	
	private int iterations;
	
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
