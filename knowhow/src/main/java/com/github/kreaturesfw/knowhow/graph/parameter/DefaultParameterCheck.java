package com.github.kreaturesfw.knowhow.graph.parameter;

import java.util.List;
import java.util.Stack;

import com.github.kreaturesfw.knowhow.graph.ActionAdapter;
import com.github.kreaturesfw.knowhow.graph.GraphNode;

/**
 * A parameter check visitor for {@link GraphNode} that checks if the
 * parameters claimed by a child node can be provided by the parent
 * node.
 * 
 * @todo add a parameter checker that also proofs if the signature
 * 			of the actions (in leaf nodes) is fulfilled by
 * 			the parameters of the knowhow. See also {@link ActionAdapter}.
 * 
 * @author Tim Janus
 */
public class DefaultParameterCheck implements ParameterCheckVisitor {

	private String error = "";
	
	private int errorCount = 1;
	
	private Stack<GraphNode> stackTrace = new Stack<>();
	
	@Override
	public boolean checkParameterMapping(GraphNode node,
			List<Parameter> parameters) {
		stackTrace.push(node);
		
		List<Parameter> curParams = node.getParameters();
		if(parameters.size() != curParams.size()) {
			for(Parameter cur : curParams) {
				if(cur.isVariable()) {
					error += "<" + errorCount + "> No mapping for '" + 
							cur.toString() + "' of '" + node + "'.\n";
					
					finishError();
				}
			}
		} else {
			for(int i=0; i<curParams.size(); ++i) {
				Parameter p1 = parameters.get(i);
				Parameter p2 = curParams.get(i);
				if(p1.getType() != p2.getType()) {
					error += "<" + errorCount + "> Type mismatch: Type '" + p1.getType() + "' of '" + p1 + ":" + 
							stackTrace.peek().toString() + "' does not match type '" + p2.getType() + "' of " + p2 + ":" +
							node.toString() + "\n";
					
					finishError();
				}
			}
		}
		
		for(GraphNode child : node.getChildren()) {
			checkParameterMapping(child, curParams);
		}
		
		stackTrace.pop();
		
		int reval = errorCount;
		if(stackTrace.isEmpty())
			errorCount = 1;
		return reval == 1;
	}
	
	public String getError() {
		return error;
	}
	
	private void finishError() {
		error += "Stacktrace:\n\t";
		for(int i=stackTrace.size() - 1; i>=0; --i) {
			GraphNode stackNode = stackTrace.get(i);
			error += stackNode.toString() + "\n\t";
		}
		errorCount += 1;
	}
}
