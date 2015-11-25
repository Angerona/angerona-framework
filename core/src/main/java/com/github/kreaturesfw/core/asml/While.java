package com.github.kreaturesfw.core.asml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import com.github.kreaturesfw.core.error.InvokeException;
import com.github.kreaturesfw.core.reflection.Condition;

/**
 * Implements a while construct for ASML by extending the CommandSequence
 * and using a condition.
 * 
 * @author Tim Janus
 */
@Root(name="while")
public class While extends CommandSequence {
	
	/** the condition which is proofed before every execution of the while loop body */
	@Attribute(name="condition", required=true)
	protected Condition condition;
	
	/** the number of iterations the while loop has performed so far */
	protected int iterations = 0;
	
	public While(@Attribute(name="condition", required=true) Condition condition) {
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
	
	/** @return the number of loop iterations performed so far. */
	public int getIterations() {
		return iterations;
	}
}
