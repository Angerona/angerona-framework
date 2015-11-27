package com.github.kreaturesfw.gui;

import java.util.HashMap;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.github.kreaturesfw.core.KReaturesPluginAdapter;
import com.github.kreaturesfw.gui.base.ViewComponent;
import com.github.kreaturesfw.gui.interactive.InteractiveBarMVPComponent;
import com.github.kreaturesfw.gui.project.ProjectTreeMVPComponent;
import com.github.kreaturesfw.gui.report.ReportTreeMVP;
import com.github.kreaturesfw.gui.simctrl.SimulationControlBarMVPComponent;
import com.github.kreaturesfw.gui.view.DesiresView;
import com.github.kreaturesfw.gui.view.PlanView;
import com.github.kreaturesfw.gui.view.ReportView;

@PluginImplementation
public class DefaultUIPlugin extends KReaturesPluginAdapter implements UIPlugin {

	@Override
	public Map<String, Class<? extends ViewComponent>> getUIComponents() {
		Map<String, Class<? extends ViewComponent>> reval = new HashMap<String, Class<? extends ViewComponent>>();
		reval.put("Report-View", ReportView.class);
		reval.put("Report-View (Experimental)", ReportTreeMVP.class);
		reval.put("Project", ProjectTreeMVPComponent.class);
		reval.put("Simulation-Control-Bar", SimulationControlBarMVPComponent.class);
		//reval.put("Resourcen", ResourcenView.class);
		reval.put("Plan", PlanView.class);
		
		reval.put("Desires", DesiresView.class);
		reval.put("Interactive Agent", InteractiveBarMVPComponent.class);
		return reval;
	}

}
