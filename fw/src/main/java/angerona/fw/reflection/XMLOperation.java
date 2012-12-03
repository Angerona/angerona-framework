package angerona.fw.reflection;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import angerona.fw.error.InvokeException;

@Root(name="operation")
public class XMLOperation extends XMLCommando {
	@Attribute(name="type")
	private String type;
	
	public XMLOperation(@Attribute(name="type") String type) {
		this.type = type;
	}

	@Override
	protected void executeInternal() throws InvokeException {
		// TODO find operator, execute operator, save result
		
	}
}
