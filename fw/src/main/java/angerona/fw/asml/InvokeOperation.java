package angerona.fw.asml;

import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import angerona.fw.OperatorCaller;
import angerona.fw.OperatorProvider;
import angerona.fw.error.InvokeException;
import angerona.fw.operators.BaseOperator;
import angerona.fw.operators.GenericOperatorParameter;
import angerona.fw.reflection.Value;
import angerona.fw.serialize.SerializeHelper;

/**
 * The InvokeOperation ASML command calls an operator which implements an operation
 * which is identified by a unique name. The parameters for the operator invocation
 * are given as a string map. An identifier to save the output of the operator can
 * also be given using the output element. 
 * 
 * The current implementation uses the preferred operator of the OperatorSet.
 * TODO: Give the script writer the ability to choose the operator.
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

	/** the name for the output using context.get(output) returns the variable calculated by the operation */
	@Element(name="output", required=false)
	private String output;
	
	public InvokeOperation(@Attribute(name="type") String type) {
		this(type, new HashMap<String, Value>(), null);
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
		OperatorCaller self = getParameter("self");
		GenericOperatorParameter gop = new GenericOperatorParameter(self);
		
		// prepare parameters:
		for(String key : parameters.keySet()) {
			Value value = parameters.get(key);
			value.setContext(getContext());
			gop.setParameter(key, value.getValue());
		}
		
		// call operation and save result:
		Object out = op.process(gop);
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
