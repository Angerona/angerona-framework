package com.github.kreatures.core.operators.parameter;

import javax.management.AttributeNotFoundException;

import com.github.kreatures.core.Agent;
import com.github.kreatures.core.KReaturesAtom;
import com.github.kreatures.core.error.ConversionException;
import com.github.kreatures.core.logic.Beliefs;
import com.github.kreatures.core.util.Utility;

/**
 * This class represents input-parameter for operators which evaluate 
 * informations like perceptions, actions or plan elements. The 
 * UpdateBeliefs and the Violates Operator uses this parameter type.
 * 
 * @author Tim Janus
 */
public class EvaluateParameter extends OperatorPluginParameter {
	
	/** the action applied before proofing for violation */
	private KReaturesAtom information;
	
	/** the beliefs which are used as basic */
	private Beliefs beliefs;
	
	/** Default Ctor: Used for dynamic instantiation */
	public EvaluateParameter() {}
	
	/**
	 * Simple Ctor: Uses a reference to the real beliefs of the given agent
	 * For planning in the future use the other ctor.
	 * @param agent		Reference to the agent who wants to check for violation.
	 * @param intent	The intention of the agent which needs a check. This might be
	 * 					an action or a complete plan.
	 */
	public EvaluateParameter(Agent agent, KReaturesAtom intent) {
		this(agent, (Beliefs)agent.getBeliefs(), intent);
	}
	
	/**
	 * Ctor: Uses a copy of the given beliefs.
	 * @param agent
	 * @param beliefs
	 * @param intent
	 */
	public EvaluateParameter(Agent agent, Beliefs beliefs, KReaturesAtom intent) {
		super(agent);
		this.information = intent;
		this.beliefs = (Beliefs)beliefs;
	}
	
	/** @return the beliefs of the agent */
	public Beliefs getBeliefs() {
		return beliefs;
	}
	
	/** @return the action applied before proofing for violation */
	public KReaturesAtom getAtom() {
		return information;
	}
	
	@Override
	public void fromGenericParameter(GenericOperatorParameter gop) 
		throws ConversionException, AttributeNotFoundException {
		super.fromGenericParameter(gop);
		
		// Get the used beliefs
		Object obj = gop.getParameterRequired("beliefs");
		if(! (obj instanceof Beliefs)) {
			throw conversionException("beliefs", Beliefs.class);
		}
		this.beliefs = (Beliefs)obj;
		
		// Get the information to evaluate: It might be null in case no perception
		// is received in the cycle:
		obj = gop.getParameter("information");
		if(obj != null && ! (obj instanceof KReaturesAtom)) {
			throw conversionException("information", KReaturesAtom.class);
		}
		this.information = (KReaturesAtom)obj;
	}
	
	@Override 
	public boolean equals(Object other) {
		if(!(other instanceof EvaluateParameter))
			return false;
		
		EvaluateParameter co = (EvaluateParameter)other;
		
		if(!super.equals(co))								return false;
		if(!Utility.equals(information, co.information))	return false;
		if(!Utility.equals(beliefs, co.beliefs))			return false;
		
		return true;
	}
	
	@Override 
	public int hashCode() {
		return super.hashCode() + 
				(information != null ? information.hashCode() : 0) +
				(beliefs != null ? beliefs.hashCode() : 0);
	}
}
