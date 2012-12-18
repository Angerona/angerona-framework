package angerona.fw.reflection;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.error.InvokeException;

/**
 * Implements an assignment command for ASML.
 * 
 * @author Tim Janus
 */
@Root(name="assign")
public class XMLAssign extends XMLCommando {

	/** reference to the logging facility */
	private static Logger LOG = LoggerFactory.getLogger(XMLAssign.class);
	
	/** The name of the context variable which saves the value of the assignment */
	@Attribute(name="name")
	private String name;
	
	@Element(name="value")
	private Value value;
	
	public XMLAssign(String name, String value, String type) throws ClassNotFoundException {
		this.name = name;
		this.value = new Value(value, type);
	}
	
	public XMLAssign(@Attribute(name="name") String name,
			@Element(name="value") Value value) {
		this.name = name;
		this.value = value;
	}
	
	@Override
	protected void executeInternal() throws InvokeException {
		setParameter(name, value.getValue(getContext()));
		LOG.trace("XMLAssign: Set '{}' to '{}'", name, value);
	}

}
