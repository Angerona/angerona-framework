package angerona.fw.reflection;

import net.sf.tweety.Formula;
import angerona.fw.error.InvokeException;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.serialize.SkillConfiguration.Statement;

/**
 * Visitor implementation for using the policy control operator.
 * @author Tim Janus
 */
public class PolicyControlVisitor extends ContextVisitor{

	@Override
	protected void runImpl(Statement st) throws InvokeException {
		String subjectId = getParameter(st.getParameterMap().get("subjectId"));		
		AngeronaAnswer aa = getParameter(st.getParameterMap().get("truthAnswer"));		
		Formula question = getParameter(st.getParameterMap().get("question"));
		
		AngeronaAnswer reval = getSelf().performPolicyControl(subjectId, aa, question);
		setOutName(st.getOutName(), reval);
	}

}
