package angerona.fw.reflection;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.error.InvokeException;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.serialize.SkillConfiguration.Statement;

/**
 * Visitor implementation for using the reasoning operator.
 * @author Tim Janus
 */
public class ReasonVisitor extends ContextVisitor {

	@Override
	protected void runImpl(Statement st) throws InvokeException {
		BaseBeliefbase bb = getParameter(st.getParameterMap().get("beliefbase"));		
		FolFormula question = getParameter((st.getParameterMap().get("formula")));
		
		AngeronaAnswer reval = bb.reason(question);
		setOutName(st.getOutName(), reval);
	}
}
