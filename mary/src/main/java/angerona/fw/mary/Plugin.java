package angerona.fw.mary;
//This file should be added to the git register so I don't have to change it when switching branches

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.internal.OperatorPluginAdapter;

/**
 *
 */
@PluginImplementation
public class Plugin extends OperatorPluginAdapter {

	@Override
	protected void registerOperators() {
		registerOperator(angerona.fw.mary.SubgoalGenerationOperator.class);
		registerOperator(angerona.fw.example.operators.WeakeningViolatesOperator.class);
		registerOperator(angerona.fw.example.operators.MaryIntentionUpdateOperator.class);
	}

}
