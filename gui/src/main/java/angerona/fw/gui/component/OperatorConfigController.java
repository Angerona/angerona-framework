package angerona.fw.gui.component;

import java.util.Map;

import angerona.fw.BaseOperator;

public class OperatorConfigController<T extends BaseOperator> {
	private OperatorConfig<T> model;
	
	public OperatorConfigController() {
	}
	
	public OperatorConfigController(OperatorConfig<T> model) {
		setModel(model);
	}
	
	public void setModel(OperatorConfig<T> model) {
		this.model = model;
	}
	
	void selectOperator(String clsName) {
		for(T op : model.getSelectableOperators()) {
			if(op.getClass().getSimpleName().equals(clsName)) {
				model.setSelectedOperator(op);
				break;
			}
		}
	}
	
	
	public void changeParameter(String name, String value) {
		Map<String, String> params = model.getParameters();
		params.put(name,  value);
		model.setParameters(params);
	}
	
	public void removeParameter(String name) {
		Map<String, String> params = model.getParameters();
		if(params.remove(name) != null) {
			model.setParameters(params);
		}
	}
	
	
	void resetOperator() {
		model.setSelectedOperator((model.getDefaultOperator()));
		resetParameters();
	}
	
	void resetParameters() {
		model.setParameters(model.getOriginalParameters());
	}
}
