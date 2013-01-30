package angerona.fw.gui.view;

import angerona.fw.gui.UIPlugin;

/**
 * Base class for Angerona UI Views.
 * 
 * There are several UI Views already defined in the Angerona GUI Extension library. Nevertheless
 * Plugins have the ability to extends this class and register them to the Angerona GUI Extension as
 * new views.
 * 
 * @see UIPlugin
 * @author Tim Janus
 */
public interface View {

	void init();

	void cleanup();

	Class<?> getObservedType();
	
	void setObservedEntity(Object entity);
	
	Object getObservedEntity();
}
