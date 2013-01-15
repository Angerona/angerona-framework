package angerona.fw.am.secrecy.operators.parameter;

import angerona.fw.PlanComponent;
import angerona.fw.error.ConversionException;
import angerona.fw.operators.GenericOperatorParameter;
import angerona.fw.operators.parameter.OperatorPluginParameter;
import angerona.fw.report.ReportPoster;

/**
 * These are the parameters used by a Planer.
 * @author Tim Janus
 */
public class PlanParameter extends OperatorPluginParameter {

	/** the actual working goals */
	private PlanComponent actualPlan;

	public PlanParameter() {}
	
	/**
	 * Ctor: Generation the PlanerParameter data-structure with the following parameters:
	 * @param actualPlan	the high level plan of the agent
	 * @param forbidden		list of forbidden actions (forbidden skills)
	 */
	public PlanParameter(ReportPoster operator, PlanComponent actualPlan) {
		super(actualPlan.getAgent(), operator);
		this.actualPlan = actualPlan;
	}
	
	public PlanComponent getActualPlan() {
		return actualPlan;
	}
	
	@Override
	public void fromGenericParameter(GenericOperatorParameter gop) 
			throws ConversionException {
		super.fromGenericParameter(gop);
		Object plan = gop.getParameter("plan");
		if(!(plan instanceof PlanComponent)) {
			throw new ConversionException(GenericOperatorParameter.class, this.getClass(),
					new ClassCastException("Cannot cast plan to PlanComponent."));
		}
		this.actualPlan = (PlanComponent)plan;
	}
}
