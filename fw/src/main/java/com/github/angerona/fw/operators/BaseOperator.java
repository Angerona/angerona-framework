package com.github.angerona.fw.operators;

import com.github.angerona.fw.operators.parameter.GenericOperatorParameter;
import com.github.angerona.fw.operators.parameter.OperatorParameter;
import com.github.angerona.fw.report.ReportPoster;
import com.github.angerona.fw.util.Pair;

/**
 * Interface for all operators implementing operation types. All operators
 * need the ability to save and change parameter in a generic way and to
 * get and change the owner of the base operator.
 * 
 * @author Tim Janus
 */
public interface BaseOperator extends ReportPoster {
		
	/**
	 * 
	 * @return	The operation type implemented by the operator.
	 * 			as a pair containing the unique name of the
	 * 			operation type and the Class information of the
	 * 			base class for operators implementing this 
	 * 			operation type.
	 */
	Pair<String, Class<?>> getOperationType();
	
	
	Object process(GenericOperatorParameter gop);
	
	Object process(OperatorParameter castParam);
}
