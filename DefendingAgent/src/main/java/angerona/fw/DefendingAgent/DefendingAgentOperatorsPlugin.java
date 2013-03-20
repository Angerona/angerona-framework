package angerona.fw.DefendingAgent;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.internal.OperatorPluginAdapter;

@PluginImplementation
public class DefendingAgentOperatorsPlugin extends OperatorPluginAdapter {

	@Override
	protected void registerOperators() {
		registerOperator(angerona.fw.DefendingAgent.operators.def.GenerateOptionsOperator.class);
		registerOperator(angerona.fw.DefendingAgent.operators.def.SubgoalGenerationOperator.class);
		registerOperator(angerona.fw.DefendingAgent.operators.def.UpdateBeliefsOperator.class);
		
	}

}
