package angerona.fw.dummy;

import java.util.HashMap;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.gui.UIPlugin;
import angerona.fw.gui.dummy.DummyBeliefbaseComponent;
import angerona.fw.gui.view.BaseView;

@PluginImplementation
public class DummyUIPlugin implements UIPlugin {

	@Override
	public Map<String, Class<? extends BaseView>> getUIComponents() {
		Map<String, Class<? extends BaseView>> reval = new HashMap<String, Class<? extends BaseView>>();
		reval.put("Dummy Beliefbase Extension", DummyBeliefbaseComponent.class);
		return reval;
	}

}
