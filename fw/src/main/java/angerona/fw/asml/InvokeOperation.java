package angerona.fw.asml;

import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import angerona.fw.error.InvokeException;
import angerona.fw.operators.OperatorCallWrapper;
import angerona.fw.operators.OperatorCaller;
import angerona.fw.operators.OperatorProvider;
import angerona.fw.operators.parameter.GenericOperatorParameter;
import angerona.fw.reflection.Value;
import angerona.fw.serialize.SerializeHelper;

/**
 * The InvokeOperation ASML command calls an operator which implements an operation
 * which is identified by a unique name. The parameters for the operator invocation
 * are given as a string map. An identifier to save the output of the operator can
 * also be given using the output element. 
 * 
 * The current implementation uses the preferred operator of the OperatorSet.
 * @todo Give the ASML script writer the ability to choose the operator.
 * 
 * @author Tim Janus
 *
 */
@Root(name="operation")
public class InvokeOperation extends ASMLCommand {
	
	/** the unique name of the operation type */
	@Attribute(name="type")
	private String type;
	
	/** a map containing name value pairs representing the parameters for the operation invocation */
	@ElementMap(name="param", attribute=true, entry="param", inline=true, key="name", value="value", required=false)
	private Map<String, Value> parameters;
	
	@ElementMap(name="setting", attribute=true, entry="setting", inline=true, key="name", value="value", required=false)
	private Map<String, String> additionalSettings;

	/** the name for the output using context.get(output) returns the variable calculated by the operation */
	@Element(name="output", required=false)
	private String output;
	
	public InvokeOperation(@Attribute(name="type") String type) {
		this(type, new HashMap<String, Value>());
	}
	
	public InvokeOperation(
			@Attribute(name="type") String type,
			@ElementMap(name="param", attribute=true, entry="param", inline=true, key="name", value="value", required=false) 
			Map<String, Value> params) {
		this(type, params, null);
	}
	
	public InvokeOperation(
			@Attribute(name="type") String type,
			@ElementMap(name="param", attribute=true, entry="param", inline=true, key="name", value="value", required=false) 
			Map<String, Value> params,
			@Element(name="output") String output) {
		this(type, params, output, new HashMap<String, String>());
	}
	
	public InvokeOperation(
			@Attribute(name="type") String type,
			@ElementMap(name="param", attribute=true, entry="param", inline=true, key="name", value="value", required=false) 
			Map<String, Value> params,
			@Element(name="output") String output,
			Map<String, String> settings) {
		this.type = type;
		this.parameters = params;
		this.output = output;
		this.additionalSettings = settings;
	}

	@Override
	protected void executeInternal() throws InvokeException {
		OperatorProvider operations = getParameter("operators");
			
		// Find operator:
		OperatorCallWrapper ocw = operations.getPreferedByType(type);
		if(ocw == null) {
			throw InvokeException.internalError("Operation type '" + type + "' not found.", 
					this.getContext());
		}
		
		// Find caller:
		OperatorCaller self = getParameter("self");
		GenericOperatorParameter gop = new GenericOperatorParameter(self);
		
		// prepare parameters:
		for(String key : parameters.keySet()) {
			Value value = parameters.get(key);
			value.setContext(getContext());
			gop.setParameter(key, value.getValue());
		}
		
		for(String key : additionalSettings.keySet()) {
			String value = additionalSettings.get(key);
			gop.setSetting(key, value);
		}
		
		// call operation and save result:
		Object out = ocw.process(gop);
		if(out != null && output != null) {
			getContext().set(output, out);
		}
	}
	
	public static void main(String [] args) throws ClassNotFoundException {
		InvokeOperation op = new InvokeOperation("TestType", new HashMap<String, Value>(), "out");
		op.parameters.put("beliefs", new Value("$beliefs"));
		op.parameters.put("perception", new Value("$in.perception"));
		
		SerializeHelper.outputXml(op, System.out);
	}
}
