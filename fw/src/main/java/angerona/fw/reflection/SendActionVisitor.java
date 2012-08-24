package angerona.fw.reflection;

import angerona.fw.Action;
import angerona.fw.Agent;
import angerona.fw.error.InvokeException;
import angerona.fw.internal.PerceptionFactory;
import angerona.fw.listener.ActionProcessor;
import angerona.fw.logic.Beliefs;
import angerona.fw.serialize.Statement;
import angerona.fw.serialize.perception.PerceptionDO;

/**
 * This visitor implements the send action operation for agents using
 * the SendAction xml element.
 * @author Tim Janus
 */
public class SendActionVisitor extends ContextVisitor {
	
	private Beliefs beliefs;
	
	private PerceptionFactory factory;
	
	private ActionProcessor actionListener;
	
	private Agent ag;
	
	public SendActionVisitor(PerceptionFactory factory, ActionProcessor actionListener, Beliefs beliefs, Agent ag) {
		if(factory == null)
			throw new IllegalArgumentException("factory must not null!");
		
		this.beliefs = beliefs;
		this.factory = factory;
		this.actionListener = actionListener;
		this.ag = ag;
	}
	
	@Override
	protected void runImpl(Statement statement) throws InvokeException {
		Action reval = (Action) factory.generateFromDataObject((PerceptionDO)statement.getComplexInfo(), context);
		actionListener.performAction(reval, ag, beliefs);
		this.setReturnValueIdentifier(statement.getReturnValueIdentifier(), reval);
	}

}
