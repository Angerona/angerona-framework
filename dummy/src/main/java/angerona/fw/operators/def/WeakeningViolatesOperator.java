package angerona.fw.operators.def;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseBeliefbase;
import angerona.fw.comm.Answer;
import angerona.fw.comm.DetailQueryAnswer;
import angerona.fw.logic.ConfidentialKnowledge;
import angerona.fw.logic.Secret;
import angerona.fw.logic.SecrecyStrengthPair;
import angerona.fw.logic.asp.AspBeliefbase;
import angerona.fw.operators.parameter.ViolatesParameter;

public class WeakeningViolatesOperator extends ViolatesOperator {
	static final double INFINITY = 1000.0;
	
	@Override
	protected Boolean processInt(ViolatesParameter param) {
		this.weakenings = processIntAndWeaken(param);
		return false;
	}
	protected List<SecrecyStrengthPair> processIntAndWeaken(ViolatesParameter param)
	{
		Logger LOG = LoggerFactory.getLogger(DetailViolatesOperator.class);
		List<SecrecyStrengthPair> secretList = new LinkedList<SecrecyStrengthPair>(); 
		secretList.add(new SecrecyStrengthPair(null, INFINITY));
		return secretList;
	}
}
