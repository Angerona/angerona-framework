package com.github.kreatures.core.reflection;

import net.sf.tweety.lp.nlp.syntax.NLPRule;

import com.github.kreatures.core.error.KReaturesException;

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
			throws KReaturesException {
		return new NLPRule();
	}

}
