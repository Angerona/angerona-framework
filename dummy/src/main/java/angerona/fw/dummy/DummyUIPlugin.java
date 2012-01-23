package angerona.fw.dummy;

import java.util.HashMap;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.gui.BaseComponent;
import angerona.fw.gui.UIPlugin;
import angerona.fw.gui.dummy.DummyBeliefbaseComponent;

@PluginImplementation
public class DummyUIPlugin implements UIPlugin {

	@Override
	public Map<String, Class<? extends BaseComponent>> getUIComponents() {
		Map<String, Class<? extends BaseComponent>> reval = new HashMap<String, Class<? extends BaseComponent>>();
		reval.put("Dummy Beliefbase Extension", DummyBeliefbaseComponent.class);
		return reval;
	}

}
