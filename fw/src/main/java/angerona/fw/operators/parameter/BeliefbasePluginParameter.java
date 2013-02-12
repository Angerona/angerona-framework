package angerona.fw.operators.parameter;

import angerona.fw.BaseBeliefbase;
import angerona.fw.error.ConversionException;
import angerona.fw.operators.GenericOperatorParameter;
import angerona.fw.operators.OperatorStack;
import angerona.fw.report.ReportPoster;

/**
 * Base class for input parameter of operators of the belief base plugin. 
 * At this point in the hierarchy it is obvious that the caller is a 
 * belief base. Therefore the class implements access methods to receive
 * the caller as belief base and implements a basic version of the
 * fromGenericParameter method which reads the caller from the generic 
 * parameter.
 * 
 * @author Tim Janus
 */
public class BeliefbasePluginParameter implements OperatorParameter {
	/** the caller of the operator */
	private BaseBeliefbase caller;

	/** Default Ctor: used for dynamic instatiation */
	public BeliefbasePluginParameter() {}
	
	/** Ctor: Used to generate parameter structures in code */
	public BeliefbasePluginParameter(BaseBeliefbase owner) {
		this.caller = owner;
	}
	
	/** @return the belief base instance which is the caller of the operator */
	public BaseBeliefbase getBeliefBase() {
		return caller;
	}
	
	/**
	 * Reads the caller from the generic parameter. The caller is cast to a BaseBeliefbase
	 * instance and saved in this object. Subclasses shall override this method to request more
	 * parameters from the generic parameter structure. The override method has to call the super
	 * method.
	 */
	@Override
	public void fromGenericParameter(GenericOperatorParameter input)
			throws ConversionException {
		if(! (input.getCaller() instanceof BaseBeliefbase) ) {
			throw new ConversionException(GenericOperatorParameter.class, this.getClass(),
					new ClassCastException("Cannot cast owner to base beliefbase."));
		}
		this.caller = (BaseBeliefbase)input.getCaller();
	}

	@Override
	public void visit(ReportPoster op) {
		// does nothing yet...
	}

	@Override
	public OperatorStack getCaller() {
		return caller;
	}
}
