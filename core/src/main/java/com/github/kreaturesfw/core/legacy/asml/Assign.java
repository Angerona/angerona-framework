package com.github.kreaturesfw.core.legacy.asml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.error.InvokeException;
import com.github.kreaturesfw.core.reflection.Context;
import com.github.kreaturesfw.core.reflection.Value;

/**
 * Implements an assignment command for ASML. It assigns the given value to the
 * given name which acts as identifier in the context.
 * 
 * @author Tim Janus
 */
@Root(name = "assign")
public class Assign extends ASMLCommand {

	/** reference to the logging facility */
	private static Logger LOG = LoggerFactory.getLogger(Assign.class);

	/** The name of the context variable which saves the value of the assignment */
	@Attribute(name = "name")
	private String name;

	/** The value which has to be saved by the assignment */
	@Attribute(name = "value")
	private Value value;

	public Assign(@Attribute(name = "name") String name,
			@Attribute(name = "value") Value value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * Sets the context of the assign command and the context of the
	 * value which is assigned by the command.
	 */
	@Override
	protected void setContext(Context context) {
		super.setContext(context);
		if (value != null)
			value.setContext(context);
	}

	@Override
	protected void executeInternal() throws InvokeException {
		setParameter(name, value.getValue());
		LOG.trace("XMLAssign: Set '{}' to '{}'", name, value);
	}

}
