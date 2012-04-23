package angerona.fw.reflection;

import net.sf.tweety.Formula;
import angerona.fw.error.InvokeException;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.serialize.Statement;

/**
 * Visitor implementation for using the policy control operator.
 * @author Tim Janus
 */
public class PolicyControlVisitor extends ContextVisitor{

	@Override
	protected void runImpl(Statement st) throws InvokeException {
		String subjectId = getParameter(st.getParameter("subjectId"));		
		AngeronaAnswer aa = getParameter(st.getParameter("truthAnswer"));		
		Formula question = getParameter(st.getParameter("question"));
		
		AngeronaAnswer reval = getSelf().performPolicyControl(subjectId, aa, question);
		setReturnValueIdentifier(st.getReturnValueIdentifier(), reval);
	}

}
