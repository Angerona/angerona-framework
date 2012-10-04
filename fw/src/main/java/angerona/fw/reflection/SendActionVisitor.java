package angerona.fw.reflection;

import angerona.fw.Action;
import angerona.fw.Agent;
import angerona.fw.error.InvokeException;
import angerona.fw.listener.ActionProcessor;
import angerona.fw.logic.Beliefs;
import angerona.fw.serialize.Statement;

/**
 * This visitor implements the send action operation for agents using
 * the SendAction xml element.
 * @author Tim Janus
 */
public class SendActionVisitor extends ContextVisitor {
	
	private Beliefs beliefs;
		
	private ActionProcessor actionListener;
	
	private Agent ag;
	
	public SendActionVisitor(ActionProcessor actionListener, Beliefs beliefs, Agent ag) {
		this.beliefs = beliefs;
		this.actionListener = actionListener;
		this.ag = ag;
	}
	
	@Override
	protected void runImpl(Statement statement) throws InvokeException {
		if(!(statement.getComplexInfo() instanceof Action))
			throw new IllegalArgumentException("The given statement has no action as complex info.");
		
		Action reval = (Action) statement.getComplexInfo();
		actionListener.performAction(reval, ag, beliefs);
		this.setReturnValueIdentifier(statement.getReturnValueIdentifier(), reval);
	}

}
