package angerona.fw.gui;

import java.util.HashMap;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.gui.view.View;
import angerona.fw.gui.view.SecrecyView;
import angerona.fw.gui.view.DesiresView;
import angerona.fw.gui.view.PlanView;
import angerona.fw.gui.view.ReportView;
import angerona.fw.gui.view.ResourcenView;

@PluginImplementation
public class DefaultUIPlugin implements UIPlugin {

	@Override
	public Map<String, Class<? extends View>> getUIComponents() {
		Map<String, Class<? extends View>> reval = new HashMap<String, Class<? extends View>>();
		reval.put("Report-View", ReportView.class);
		reval.put("Resourcen", ResourcenView.class);
		reval.put("Confidential-Knowledge", SecrecyView.class);
		reval.put("Plan", PlanView.class);
		reval.put("Desires", DesiresView.class);
		return reval;
	}

}
