package angerona.fw.reflection;

import java.util.Map;

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
		Object params = getParameter("operators");
		if(! (params instanceof Map<?, ?>)) {
			
		}
	}
}
