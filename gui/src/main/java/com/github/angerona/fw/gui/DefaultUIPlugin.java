package com.github.angerona.fw.gui;

import interactive.InteractiveBarMVPComponent;

import java.util.HashMap;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.github.angerona.fw.AngeronaPluginAdapter;
import com.github.angerona.fw.gui.base.ViewComponent;
import com.github.angerona.fw.gui.project.ProjectTreeMVPComponent;
import com.github.angerona.fw.gui.report.ReportTreeMVP;
import com.github.angerona.fw.gui.simctrl.SimulationControlBarMVPComponent;
import com.github.angerona.fw.gui.view.DesiresView;
import com.github.angerona.fw.gui.view.PlanView;
import com.github.angerona.fw.gui.view.ReportView;

@PluginImplementation
public class DefaultUIPlugin extends AngeronaPluginAdapter implements UIPlugin {

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
