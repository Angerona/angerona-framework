package com.github.angerona.fw.gui.base;

import com.github.angerona.fw.util.Model;

/**
 * An abstract base class defining general methods of a presenter in the MVP pattern 
 * like setModel() and setView(). It wires view as a listener of the model and forces 
 * a view update if the model/view is set.
 * @author Tim Janus
 *
 * @param <M>	Type of the Model
 * @param <V>	Type of the View
 */
public abstract class Presenter<M extends Model, V extends View> {
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
			breakModelViewLink();
		}
		this.model = model;
		if(this.model != null && view != null) {
			createModelViewLinkAndUpdate();
		}
	}

	public void createModelViewLinkAndUpdate() {
		this.model.addPropertyObserver(view);
		this.model.addMapObserver(view);
		wireViewEvents();
		forceUpdate();
	}

	public void breakModelViewLink() {
		this.model.removePropertyObserver(view);
		this.model.removeMapObserver(view);
		unwireViewEvents();
	}
	
	/**
	 * Changes the view mediated by the presenter, unregisters old view from model,
	 * registers new view to model and wires/unwires the user input events to the presenter.
	 * @param view	The new view.
	 */
	public void setView(V view) {
		if(this.view != null && model != null) {
			breakModelViewLink();
		}
		this.view = view;
		if(this.view != null && model != null) {
			createModelViewLinkAndUpdate();
		}
	}
	
	protected abstract void forceUpdate();
	
	protected abstract void wireViewEvents();
	
	protected abstract void unwireViewEvents();
}
