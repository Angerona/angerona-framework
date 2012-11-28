package angerona.fw.reflection;

import angerona.fw.Perception;
import angerona.fw.error.InvokeException;
import angerona.fw.logic.Beliefs;
import angerona.fw.serialize.Statement;

/**
 * Visitor implementation for updating the belief base of an Agent.
 * @author Tim Janus
 */
public class UpdateBeliefbaseVisitor extends ContextVisitor{

	@Override
	protected void runImpl(Statement st) throws InvokeException {
		Perception p = this.getParameter(st.getParameter("perception"));
		Beliefs b = this.getParameter(st.getParameter("beliefs"));
		this.getSelf().updateBeliefs(p, b);
	}

}
