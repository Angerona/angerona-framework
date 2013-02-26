package angerona.fw.example;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.example.operators.GenerateOptionsOperator;
import angerona.fw.example.operators.IntentionUpdateOperator;
import angerona.fw.example.operators.SubgoalGenerationOperator;
import angerona.fw.example.operators.UpdateBeliefsOperator;
import angerona.fw.example.operators.ViolatesOperator;
import angerona.fw.internal.OperatorPluginAdapter;

/**
 * The dummy operator plugin is an example/test plugin for the Angerona framework.
 * The classes provided by this plugin only implement some dummy functionality to test
 * the program flow of the Angerona framework.
 * @author Tim Janus
 */
@PluginImplementation
public class ExampleOperatorPlugin extends OperatorPluginAdapter {
	
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
