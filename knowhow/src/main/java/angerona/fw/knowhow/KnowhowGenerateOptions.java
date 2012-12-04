package angerona.fw.knowhow;

import angerona.fw.operators.def.GenerateOptionsOperator;
import angerona.fw.operators.parameter.GenerateOptionsParameter;

/**
 * 
 * @author Tim Janus
 */
public class KnowhowGenerateOptions extends GenerateOptionsOperator {

	@Override
	protected Integer processInt(GenerateOptionsParameter param) {
		return super.fetchVariables(param);
	}

}
