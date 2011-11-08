package angerona.fw.reflection;

import angerona.fw.Perception;
import angerona.fw.error.InvokeException;
import angerona.fw.serialize.SkillConfiguration;

/**
 * Visitor implementation for updating the belief bases.
 * @author Tim Janus
 */
public class UpdateBeliefbaseVisitor extends ContextVisitor{

	@Override
	protected void runImpl(SkillConfiguration.Statement st) throws InvokeException {
		Perception p = this.getParameter(st.getParameterMap().get("perception"));
		this.getSelf().updateBeliefs(p);
	}

}
