package angerona.fw.dummy;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.internal.OperatorPluginAdapter;
import angerona.fw.operators.def.GenerateOptionsOperator;
import angerona.fw.operators.def.IntentionUpdateOperator;
import angerona.fw.operators.def.SubgoalGenerationOperator;
import angerona.fw.operators.def.UpdateBeliefsOperator;
import angerona.fw.operators.def.ViolatesOperator;

/**
 * The dummy operator plugin is an example/test plugin for the Angerona framework.
 * The classes provided by this plugin only implement some dummy functionality to test
 * the program flow of the Angerona framework.
 * @author Tim Janus
 */
@PluginImplementation
public class DummyOperatorPlugin extends OperatorPluginAdapter {
	
	@Override
	protected void registerOperators() {
		registerOperator(SubgoalGenerationOperator.class);
		registerOperator(ViolatesOperator.class);
		registerOperator(GenerateOptionsOperator.class);
		registerOperator(IntentionUpdateOperator.class);
		
		// TODO: Move the following to fw
		registerOperator(UpdateBeliefsOperator.class);
	}

}
