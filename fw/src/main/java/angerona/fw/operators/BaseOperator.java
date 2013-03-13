package angerona.fw.operators;

import java.util.Map;

import angerona.fw.report.ReportPoster;
import angerona.fw.util.Pair;

/**
 * Interface for all operators implementing operation types. All operators
 * need the ability to save and change parameter in a generic way and to
 * get and change the owner of the base operator.
 * 
 * @author Tim Janus
 */
public interface BaseOperator extends ReportPoster {
	
	/**
	 * Change the user-parameters used by the operator. 
	 * @param parameters
	 */
	void setParameters(Map<String, String> parameters);
	
	/**
	 * @return 	a map from parameter names to values in a generic string
	 * 			representation.
	 */
	Map<String, String> getParameters();
	
	/**
	 * Gets the parameter with the given name. If the parameter does not
	 * exists the given def parameter is returned.
	 * @param name	The name of the parameter to return
	 * @param def	The default value of the parameter if the parameter does
	 * 				not exists yet.
	 * @return		A string representing the value of the parameter
	 */
	String getParameter(String name, String def);
	
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
}
