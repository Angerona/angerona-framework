package angerona.fw.reflection;

import angerona.fw.Action;
import angerona.fw.PerceptionFactory;
import angerona.fw.error.InvokeException;
import angerona.fw.logic.base.Beliefs;
import angerona.fw.serialize.SkillConfiguration.Statement;

/**
 * This visitor implements the send action operation for agents using
 * the SendAction xml element.
 * @author Tim Janus
 */
public class SendActionVisitor extends ContextVisitor {
	
	private Beliefs beliefs;
	
	private PerceptionFactory factory;
	
	private boolean realRun;
	
	private boolean violates = false;
	
	public SendActionVisitor(PerceptionFactory factory, boolean realRun, Beliefs beliefs) {
		if(factory == null)
			throw new IllegalArgumentException("factory must not null!");
		
		this.factory = factory;
		this.realRun = realRun;
	}
	
	public boolean violates() {
		return violates;
	}
	
	@Override
	protected void runImpl(Statement statement) throws InvokeException {
		Action reval = (Action) factory.generateFromElement(
				statement.getInnerElement(), context);
		
		if(realRun)
			getSelf().performAction(reval);
		else
			violates = getSelf().performThought(beliefs, reval);
		this.setOutName(statement.getOutName(), reval);
	}

}
