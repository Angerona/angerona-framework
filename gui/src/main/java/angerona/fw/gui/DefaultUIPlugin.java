package angerona.fw.gui;

import java.util.HashMap;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.AngeronaPluginAdapter;
import angerona.fw.gui.base.ViewComponent;
import angerona.fw.gui.view.DesiresView;
import angerona.fw.gui.view.PlanView;
import angerona.fw.gui.view.ReportView;
import angerona.fw.gui.view.ResourcenView;

@PluginImplementation
public class DefaultUIPlugin extends AngeronaPluginAdapter implements UIPlugin {

	@Override
	public Map<String, Class<? extends ViewComponent>> getUIComponents() {
		Map<String, Class<? extends ViewComponent>> reval = new HashMap<String, Class<? extends ViewComponent>>();
		reval.put("Report-View", ReportView.class);
		reval.put("Resourcen", ResourcenView.class);
		reval.put("Plan", PlanView.class);
		reval.put("Desires", DesiresView.class);
		return reval;
	}

}
