package angerona.fw.operators.parameter;

import angerona.fw.error.ConversionException;
import angerona.fw.operators.GenericOperatorParameter;
import angerona.fw.operators.OperatorVisitor;
import angerona.fw.report.ReportPoster;

/**
 * 
 * @author Tim Janus
 */
public interface OperatorParameter {
	
	OperatorVisitor getCaller();
	
	/**
	 * This method is called before the operator parameter is used by the operator op.
	 * Thus allows the parameter to know its operator
	 * @param op
	 */
	void visit(ReportPoster op);
	
	/** 
	 * This method converts the given GenericOperatorParameter into a more
	 * user friendly version which has type information.
	 * The information is saved in the object instance which implements the
	 * OperatorParameter interface.
	 * @param input		The generic operator parameter data structure
	 * @throws ConversionException
	 */
	void fromGenericParameter(GenericOperatorParameter input) throws ConversionException;
}
