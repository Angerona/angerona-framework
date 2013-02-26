package angerona.fw.example;

import java.util.HashMap;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.example.gui.ExampleBeliefbaseComponent;
import angerona.fw.gui.UIPlugin;
import angerona.fw.gui.view.View;

@PluginImplementation
public class ExampleUIPlugin implements UIPlugin {

	@Override
	public Map<String, Class<? extends View>> getUIComponents() {
		Map<String, Class<? extends View>> reval = new HashMap<String, Class<? extends View>>();
		reval.put("Dummy Beliefbase Extension", ExampleBeliefbaseComponent.class);
		return reval;
	}

}
