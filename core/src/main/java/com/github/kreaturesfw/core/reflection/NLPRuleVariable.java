package com.github.kreaturesfw.core.reflection;

import com.github.kreaturesfw.core.error.AngeronaException;

import net.sf.tweety.lp.nlp.syntax.NLPRule;

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
