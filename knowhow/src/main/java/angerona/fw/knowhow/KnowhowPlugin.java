package angerona.fw.knowhow;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.asp.AspPlugin;
import angerona.fw.gui.UIComponent;
import angerona.fw.gui.UIPlugin;
import angerona.fw.logic.base.BaseBeliefbase;

@PluginImplementation
public class KnowhowPlugin extends AspPlugin implements UIPlugin {

	public Map<String, Class<? extends UIComponent>> getUIComponents() {
		Map<String, Class<? extends UIComponent>> reval = new HashMap<String, Class<? extends UIComponent>>();
		return reval;
	}

	public List<Class<? extends BaseBeliefbase>> getSupportedBeliefbases() {
		List<Class<? extends BaseBeliefbase>> reval = new LinkedList<Class<? extends BaseBeliefbase>>();
		reval.addAll(super.getSupportedBeliefbases());
		reval.add(KBBeliefbase.class);
		return reval;
	}

}
