package angerona.fw.operators.parameter;

import javax.management.AttributeNotFoundException;

import angerona.fw.Agent;
import angerona.fw.AngeronaAtom;
import angerona.fw.error.ConversionException;
import angerona.fw.logic.Beliefs;
import angerona.fw.operators.GenericOperatorParameter;

/**
 * This class represents input-parameter for operators which evaluate 
 * informations like perceptions, actions or plan elements. The 
 * UpdateBeliefs and the Violates Operator uses this parameter type.
 * 
 * @author Tim Janus
 */
public class EvaluateParameter extends OperatorPluginParameter {
	
	/** the action applied before proofing for violation */
	private AngeronaAtom information;
	
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
	public EvaluateParameter(Agent agent, AngeronaAtom intent) {
		this(agent, (Beliefs)agent.getBeliefs(), intent);
	}
	
	/**
	 * Ctor: Uses a copy of the given beliefs.
	 * @param agent
	 * @param beliefs
	 * @param intent
	 */
	public EvaluateParameter(Agent agent, Beliefs beliefs, AngeronaAtom intent) {
		super(agent);
		this.information = intent;
		this.beliefs = (Beliefs)beliefs;
	}
	
	/** @return the beliefs of the agent */
	public Beliefs getBeliefs() {
		return beliefs;
	}
	
	/** @return the action applied before proofing for violation */
	public AngeronaAtom getAtom() {
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
		if(obj != null && ! (obj instanceof AngeronaAtom)) {
			throw conversionException("information", AngeronaAtom.class);
		}
		this.information = (AngeronaAtom)obj;
	}
}
