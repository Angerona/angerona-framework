package angerona.fw.reflection;

import angerona.fw.error.AngeronaException;
import net.sf.tweety.logicprogramming.nlp.syntax.NLPRule;

/**
 * 
 * @author Tim Janus
 */
public class NLPRuleVariable extends BaseVariable<NLPRule>{

	/** Default Ctor: Used for dynamic instantiation */
	public NLPRuleVariable() {}
	
	public NLPRuleVariable(NLPRule instance) {
		super(instance);
	}
	
	@Override
	protected NLPRule createInstanceFromString(String content)
			throws AngeronaException {
		return new NLPRule();
	}

}
