package angerona.fw.reflection;

import java.io.StringReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.parser.ASMLParser;
import angerona.fw.parser.ParseException;
import angerona.fw.serialize.SerializeHelper;

public class Value  {

	/** reference to the logging facility */
	private static Logger LOG = LoggerFactory.getLogger(Value.class);
	
	public static final String CONTEXT_REFERENCE_TYPE = "_REF_";
	
	/** A string containing the value for the assignment, it might be a variable starting with '$' */
	private String value;
	
	/** A string containing the type of the assignment value. */
	private String typeString;
	
	/** the type of the value as java cls */
	private Class<?> type;
	
	private Object valueObject;
	
	private Context context;
	
	/**
	 * Ctor: Using DAML parser to parse a string containing the value.
	 * @param value
	 * @throws ClassNotFoundException
	 */
	public Value(String value) throws ClassNotFoundException {
		ASMLParser parser = new ASMLParser(new StringReader(value));
		Value v = null;
		try {
			v = parser.value();
		} catch (ParseException e) {
			LOG.warn("Cannot parse: '{}' - Using String as default type.", value);
			v = new Value(value, String.class.getName());
		}
		this.value = v.value;
		this.typeString = v.typeString;
		this.type = v.type;
		this.valueObject = v.valueObject;
	}
	
	public Value(String value, String type) throws ClassNotFoundException {
		this.typeString = type == null ? "string" : type;
		this.value = value;
		
		if(!value.startsWith("$")) {
			LOG.trace("Value created with type: '{}'", typeString);
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
			LOG.trace("Value class provides a reference in the Context");
		}
	}
	
	public String getType() {
		return typeString;
	}
	
	public Object getValue() {
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
			if(value.equalsIgnoreCase("null")) {
				valueObject = null;
			} else {
				valueObject = value;
			}
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
			valueObject = SerializeHelper.loadXmlTry(type, value);
		}
		return valueObject;
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
