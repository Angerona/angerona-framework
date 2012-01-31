package angerona.fw.gui;

import javax.naming.OperationNotSupportedException;

import com.whiplash.gui.WlComponent;

/**
 * Base class for Angerona UI Components.
 * 
 * There are several UI Components already defined in the Angerona GUI Extension library. Nevertheless
 * Plugins have the ability to extends this Component and register them to the Angerona GUI Extension.
 * 
 * @see UIPlugin
 * @author Tim Janus
 */
public abstract class UIComponent extends WlComponent {

	/** kill warning */
	private static final long serialVersionUID = -1482323833112551669L;

	/** the title of the UIComponent tab. */
	private String title;
	
	@Override
	public String getTitle() {
		return title;
	}
	
	/**
	 * changes the title of the tab lable of the UI Component.
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
		try {
			this.getTab().refreshTitle();
		} catch(NullPointerException ex) {
			System.err.println("Title change failed");
		}
	}
	
	/**
	 * initialization method must be called after the constructor
	 */
	public void init() {
		this.title = "TBD";
	}
	
	public abstract String getComponentTypeName();
	
	/**
	 * This method returns the type of object which is shown by this component
	 * @return 	null if the component is not bounded to a special type (like a beliefbase type (asp graphs)) otherwise the class
	 * *		object describing the shown object.
	 * TODO: Decide to move this into a subclass?
	 */
	public Class<?> getObservationObjectType() {
		return null;
	}

	/**
	 * Sets the object which is observed by the UI component.
	 * @param observationObject
	 * @throws OperationNotSupportedException if the UI component is not bound to a specfic object.
	 */
	public void setObservationObject(Object observationObject) throws OperationNotSupportedException {
		throw new OperationNotSupportedException();
	}
}
