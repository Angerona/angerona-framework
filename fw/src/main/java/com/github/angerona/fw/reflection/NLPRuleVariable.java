package com.github.angerona.fw.reflection;

import net.sf.tweety.logicprogramming.nlp.syntax.NLPRule;

import com.github.angerona.fw.error.AngeronaException;

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
