package angerona.fw.operators.parameter;

import angerona.fw.Agent;
import angerona.fw.AngeronaAtom;
import angerona.fw.error.ConversionException;
import angerona.fw.logic.Beliefs;
import angerona.fw.operators.GenericOperatorParameter;
import angerona.fw.report.ReportPoster;

public class EvaluateParameter extends OperatorPluginParameter {
	
	/** the action applied before proofing for violation */
	private AngeronaAtom information;
	
	/** the beliefs which are used as basic */
	private Beliefs beliefs;
	
	public EvaluateParameter() {}
	
	/**
	 * Simple Ctor: Uses a reference to the real beliefs of the given agent
	 * For planning in the future use the other ctor.
	 * @param agent		Reference to the agent who wants to check for violation.
	 * @param intent	The intention of the agent which needs a check. This might be
	 * 					an action or a complete plan.
	 */
	public EvaluateParameter(Agent agent, ReportPoster operator, AngeronaAtom intent) {
		this(agent, operator, (Beliefs)agent.getBeliefs(), intent);
	}
	
	/**
	 * Ctor: Uses a copy of the given beliefs.
	 * @param agent
	 * @param beliefs
	 * @param intent
	 */
	public EvaluateParameter(Agent agent, ReportPoster operator, Beliefs beliefs, AngeronaAtom intent) {
		super(agent, operator);
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
		throws ConversionException {
		super.fromGenericParameter(gop);
		Object obj = gop.getParameter("beliefs");
		if(! (obj instanceof Beliefs)) {
			throwException("beliefs", obj, Beliefs.class);
		}
		this.beliefs = (Beliefs)obj;
		
		obj = gop.getParameter("information");
		if(obj != null && !(obj instanceof AngeronaAtom)) {
			throwException("information", obj, AngeronaAtom.class);
		}
		this.information = (AngeronaAtom)obj;
	}
}
