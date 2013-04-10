package angerona.fw.gui.base;

import angerona.fw.util.Model;
import angerona.fw.util.PropertyObserver;

/**
 * An abstract base class defining general methods of a presenter like setModel()
 * and setView(). It wires view as a listener of the model and forces a view update
 * if the model/view is set.
 * @author Tim Janus
 *
 * @param <M>	Type of the Model
 * @param <V>	Type of the View
 */
public abstract class Presenter<M extends Model, V extends PropertyObserver> {
	/** the data model */
	protected M model;
	
	/** the view representing the data model and showing controls to change the data model */
	protected V view;
	
	/**
	 * Changes the model mediated by the presenter, unwires and wires
	 * view with the model and forces a view update.
	 * @param model	The new model
	 */
	public void setModel(M model) {
		if(this.model != null && view != null) {
			this.model.removePropertyObserver(view);
		}
		this.model = model;
		if(this.model != null && view != null) {
			forceUpdate();
			this.model.addPropertyObserver(view);
		}
	}
	
	/**
	 * Changes the view mediated by the presenter, unregisters old view from model,
	 * registers new view to model and wires/unwires the user input events to the presenter.
	 * @param view	The new view.
	 */
	public void setView(V view) {
		if(this.view != null && model != null) {
			unwireViewEvents();
			this.model.removePropertyObserver(this.view);
		}
		this.view = view;
		if(this.view != null && model != null) {
			wireViewEvents();
			forceUpdate();
			this.model.addPropertyObserver(this.view);
		}
	}
	
	protected abstract void forceUpdate();
	
	protected abstract void wireViewEvents();
	
	protected abstract void unwireViewEvents();
}
