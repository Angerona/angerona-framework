package angerona.fw.mary;
//This file should be added to the git register so I don't have to change it when switching branches

import java.util.LinkedList;
import java.util.List;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.AngeronaPluginAdapter;
import angerona.fw.example.operators.MaryIntentionUpdateOperator;
import angerona.fw.example.operators.WeakeningViolatesOperator;
import angerona.fw.operators.BaseOperator;

/**
 *
 */
@PluginImplementation
public class Plugin extends AngeronaPluginAdapter {

	/**
	 * @todo move the mary operators to mary project
	 */
	@Override
	public List<Class<? extends BaseOperator>> getOperators() {
		List<Class<? extends BaseOperator>> reval = new LinkedList<>();
		reval.add(SubgoalGenerationOperator.class);
		reval.add(WeakeningViolatesOperator.class);
		reval.add(MaryIntentionUpdateOperator.class);
		return reval;
	}

}
