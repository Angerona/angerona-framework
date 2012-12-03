package angerona.fw.reflection;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.serialize.SerializeHelper;

@Root(name="value")
public class Value  {

	/** reference to the logging facility */
	private static Logger LOG = LoggerFactory.getLogger(Value.class);
	
	public static final String CONTEXT_REFERENCE_TYPE = "_REF_";
	
	/** A string containing the value for the assignment, it might be a variable starting with '$' */
	@Attribute(name="value")
	private String value;
	
	/** A string containing the type of the assignment value. */
	@Attribute(name="type")
	private String typeString;
	
	/** the type of the value as java cls */
	private Class<?> type;
	
	private Object valueObject;
	
	public Value(String value) throws ClassNotFoundException {
		this(value, String.class.getName());
	}
	
	public Value(@Attribute(name="value") String value, 
			@Attribute(name="type") String type) 
					throws ClassNotFoundException {
		this.typeString = type == null ? "string" : type;
		this.value = value;
		
		if(!value.startsWith("$")) {
			LOG.debug("Value created with type: '{}'", typeString);
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
			typeString = CONTEXT_REFERENCE_TYPE;
			LOG.debug("Value class provides a reference in the Context");
		}
	}
	
	public Object getValue() {
		return getValue(null);
	}
	
	public String getType() {
		return typeString;
	}
	
	public Object getValue(Context context) {
		// Check for cached value.
		if(valueObject != null)
			return valueObject;
		
		if(typeString.equals(CONTEXT_REFERENCE_TYPE)) {
			// Context (REF) does not use the cache 
			if(context == null) {
				// TODO
			}
			Object value = context.get(this.value.substring(1));
			return value;
		} else if(type == String.class) {
			valueObject = value;
			return valueObject;
		} else if(type == Integer.class) {
			valueObject = Integer.parseInt(value);
		} else if(type == Boolean.class) {
			valueObject = Boolean.parseBoolean(value);
		} else if(type == Float.class) {
			valueObject = Float.parseFloat(value);
		} else if(type == Double.class) {
			valueObject = Double.parseDouble(value);
		} else {
			valueObject = SerializeHelper.loadXml(type, value);
		}
		return valueObject;
	}
}
