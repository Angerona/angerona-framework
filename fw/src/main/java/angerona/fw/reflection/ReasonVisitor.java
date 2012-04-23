package angerona.fw.reflection;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.error.InvokeException;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.BaseBeliefbase;
import angerona.fw.serialize.Statement;

/**
 * Visitor implementation for using the reasoning operator.
 * @author Tim Janus
 */
public class ReasonVisitor extends ContextVisitor {

	@Override
	protected void runImpl(Statement st) throws InvokeException {
		BaseBeliefbase bb = getParameter(st.getParameter("beliefbase"));		
		FolFormula question = getParameter((st.getParameter("sentence")));
		
		AngeronaAnswer reval = bb.reason(question);
		setReturnValueIdentifier(st.getReturnValueIdentifier(), reval);
	}
}
