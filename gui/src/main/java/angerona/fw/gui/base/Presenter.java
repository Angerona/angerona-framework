package angerona.fw.gui.base;

import java.beans.PropertyChangeListener;

public abstract class Presenter<M extends Model, V extends PropertyChangeListener> {
	protected M model;
	
	protected V view;
	
	public void setModel(M model) {
		if(this.model != null && view != null) {
			this.model.removePropertyChangeListener(view);
		}
		this.model = model;
		if(this.model != null && view != null) {
			forceUpdate();
			this.model.addPropertyChangeListener(view);
		}
	}
	
	public void setView(V view) {
		if(this.view != null && model != null) {
			unwireViewEvents();
			this.model.removePropertyChangeListener(this.view);
		}
		this.view = view;
		if(this.view != null && model != null) {
			wireViewEvents();
			forceUpdate();
			this.model.addPropertyChangeListener(this.view);
		}
	}
	
	protected abstract void forceUpdate();
	
	protected abstract void wireViewEvents();
	
	protected abstract void unwireViewEvents();
}
