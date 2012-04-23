package angerona.fw.reflection;

import angerona.fw.Perception;
import angerona.fw.error.InvokeException;
import angerona.fw.serialize.Statement;

/**
 * Visitor implementation for updating the belief bases.
 * @author Tim Janus
 */
public class UpdateBeliefbaseVisitor extends ContextVisitor{

	@Override
	protected void runImpl(Statement st) throws InvokeException {
		Perception p = this.getParameter(st.getParameter("perception"));
		this.getSelf().updateBeliefs(p);
	}

}
