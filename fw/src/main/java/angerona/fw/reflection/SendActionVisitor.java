package angerona.fw.reflection;

import angerona.fw.Action;
import angerona.fw.error.InvokeException;
import angerona.fw.internal.PerceptionFactory;
import angerona.fw.logic.Beliefs;
import angerona.fw.logic.ViolatesResult;
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
	
	private boolean realRun;
	
	private ViolatesResult violates = null;
	
	public SendActionVisitor(PerceptionFactory factory, boolean realRun, Beliefs beliefs) {
		if(factory == null)
			throw new IllegalArgumentException("factory must not null!");
		
		this.beliefs = beliefs;
		this.factory = factory;
		this.realRun = realRun;
	}
	
	public ViolatesResult violates() {
		return violates;
	}

	
	@Override
	protected void runImpl(Statement statement) throws InvokeException {
		Action reval = (Action) factory.generateFromDataObject((PerceptionDO)statement.getComplexInfo(), context);

		if(realRun)
			getSelf().performAction(reval);
		else
		{
			violates = getSelf().performThought(beliefs, reval);
			beliefs = violates.getBeliefs();
		}
		this.setReturnValueIdentifier(statement.getReturnValueIdentifier(), reval);
	}

}
