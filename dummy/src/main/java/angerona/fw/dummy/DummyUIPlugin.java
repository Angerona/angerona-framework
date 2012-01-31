package angerona.fw.dummy;

import java.util.HashMap;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.gui.UIComponent;
import angerona.fw.gui.UIPlugin;
import angerona.fw.gui.dummy.DummyBeliefbaseComponent;

@PluginImplementation
public class DummyUIPlugin implements UIPlugin {

	@Override
	public Map<String, Class<? extends UIComponent>> getUIComponents() {
		Map<String, Class<? extends UIComponent>> reval = new HashMap<String, Class<? extends UIComponent>>();
		reval.put("Dummy Beliefbase Extension", DummyBeliefbaseComponent.class);
		return reval;
	}

}
