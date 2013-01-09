package angerona.fw.reflection;

import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import angerona.fw.BaseOperator;
import angerona.fw.OperatorProvider;
import angerona.fw.error.InvokeException;
import angerona.fw.operators.GenericOperatorParameter;
import angerona.fw.operators.OperatorVisitor;
import angerona.fw.serialize.SerializeHelper;

@Root(name="operation")
public class XMLOperation extends XMLCommando {
	/** the operation type name */
	@Attribute(name="type")
	private String type;
	
	/** a map containing name value pairs representing the parameters for the operation invocation */
	@ElementMap(name="param", attribute=true, entry="param", inline=true, key="name", value="value", required=false)
	private Map<String, String> parameters;

	/** the name for the output using context.get(output) returns the variable calculated by the operation */
	@Element(name="output", required=false)
	private String output;
	
	public XMLOperation(@Attribute(name="type") String type) {
		this(type, new HashMap<String, String>(), null);
	}
	
	public XMLOperation(
			@Attribute(name="type") String type,
			@ElementMap(name="param", attribute=true, entry="param", inline=true, key="name", value="value", required=false) 
			Map<String, String> params) {
		this(type, params, null);
	}
	
	public XMLOperation(
			@Attribute(name="type") String type,
			@ElementMap(name="param", attribute=true, entry="param", inline=true, key="name", value="value", required=false) 
			Map<String, String> params,
			@Element(name="output") String output) {
		this.type = type;
		this.parameters = params;
		this.output = output;
	}

	@Override
	protected void executeInternal() throws InvokeException {
		OperatorProvider operations = getParameter("operators");
			
		// Find operator:
		BaseOperator op = operations.getPreferedByType(type);
		if(op == null) {
			throw InvokeException.internalError("Operation type '" + type + "' not found.", 
					this.getContext());
		}
		
		// Find caller:
		OperatorVisitor self = getParameter("self");
		GenericOperatorParameter gop = new GenericOperatorParameter(self);
		
		// prepare parameters:
		for(String key : parameters.keySet()) {
			String value = parameters.get(key);
			Value v = null;
			try {
				v = new Value(value);
				v.setContext(getContext());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			gop.setParameter(key, v.getValue());
		}
		
		// call operation and save result:
		Object out = op.process(gop);
		if(out != null && output != null) {
			getContext().set(output, out);
		}
	}
	
	public static void main(String [] args) {
		XMLOperation op = new XMLOperation("TestType", new HashMap<String, String>(), "out");
		op.parameters.put("beliefs", "$beliefs");
		op.parameters.put("perception", "$in.perception");
		
		SerializeHelper.outputXml(op, System.out);
	}
}
