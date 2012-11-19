package angerona.fw.gui.component;

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
		
	}
	
	void resetOperator() {
		model.setSelectedOperator((model.getDefaultOperator()));
		resetParameters();
	}
	
	void resetParameters() {
		model.setParameters(model.getParameters());
	}
}
