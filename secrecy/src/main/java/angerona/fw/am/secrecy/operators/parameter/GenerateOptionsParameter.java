package angerona.fw.am.secrecy.operators.parameter;

import javax.management.AttributeNotFoundException;

import angerona.fw.Agent;
import angerona.fw.Perception;
import angerona.fw.error.ConversionException;
import angerona.fw.operators.parameter.GenericOperatorParameter;
import angerona.fw.operators.parameter.OperatorPluginParameter;

/**
 * Class encoding the input parameter for the GenerateOptionsOperator.
 * @author Tim Janus
 */
public class GenerateOptionsParameter extends OperatorPluginParameter {

	/** the last received perception */
	protected Perception perception;
	
	public GenerateOptionsParameter() {}
	
	/**
	 * Ctor: Generates a GenerateOptionsParameter data-structure with the following parameters:
	 * @param agent			The agent containing the desires and beliefs.
	 * @param perception	the last received perception
	 */
	public GenerateOptionsParameter(Agent agent, Perception perception) {
		super(agent);
		this.perception = perception;
	}
	
	/** @return the last received perception */
	public Perception getPerception() {
		return perception;
	}
	
	@Override
	public void fromGenericParameter(GenericOperatorParameter gop) 
		throws ConversionException, AttributeNotFoundException {
		super.fromGenericParameter(gop);
		
		Object obj = gop.getParameter("perception");
		if(obj != null) {
			if(! (obj instanceof Perception)) {
				throw conversionException("perception", Perception.class);
			}
			this.perception= (Perception)obj;
		}
	}
}
