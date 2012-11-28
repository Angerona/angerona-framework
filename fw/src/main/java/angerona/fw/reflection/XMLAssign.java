package angerona.fw.reflection;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.error.InvokeException;
import angerona.fw.serialize.SerializeHelper;

@Root(name="assign")
public class XMLAssign extends XMLCommando {

	/** reference to the logging facility */
	private static Logger LOG = LoggerFactory.getLogger(XMLAssign.class);
	
	@Attribute(name="name")
	private String name;
	
	@Attribute(name="value")
	private String value;
	
	@Attribute(name="type")
	private String typeString;
	
	private Class<?> type;
	
	public XMLAssign(@Attribute(name="name") String name, 
			@Attribute(name="value") String value,
			@Attribute(name="type") String type) throws ClassNotFoundException {
		this.name = name;
		this.typeString = type == null ? "string" : type;
		this.value = value;
		
		if(!value.startsWith("$")) {
			LOG.debug("XMLAssign created with type: '{}'", typeString);
			if(typeString.equalsIgnoreCase("bool") || 
					typeString.equalsIgnoreCase("boolean")) {
				this.type = Boolean.class;
			} else if(typeString.equalsIgnoreCase("int") || 
					typeString.equalsIgnoreCase("interger")) {
				this.type = Integer.class;
			} else if(typeString.equalsIgnoreCase("float")) {
				this.type = Float.class;
			} else if (typeString.equalsIgnoreCase("double")) {
				this.type = Double.class;
			} else if(typeString.equalsIgnoreCase("string")) {
				this.type = String.class;
			} else {
				this.type = Class.forName(type);
			}
		} else {
			typeString = "REF";
			LOG.debug("XMLAssign changes a reference in the Context");
		}
	}
	
	@Override
	protected void executeInternal() throws InvokeException {
		if(typeString.equals("REF")) {
			Object value = getParameter(this.value.substring(1));
			setParameter(name, value);
		} else if(type == String.class) {
			setParameter(name, value);
		} else if(type == Integer.class) {
			setParameter(name, Integer.parseInt(value));
		} else if(type == Boolean.class) {
			setParameter(name, Boolean.parseBoolean(value));
		} else if(type == Float.class) {
			setParameter(name, Float.parseFloat(value));
		} else if(type == Double.class) {
			setParameter(name, Double.parseDouble(value));
		} else {
			Object obj = SerializeHelper.loadXml(type, value);
			setParameter(name, obj);
		}
	}

}
