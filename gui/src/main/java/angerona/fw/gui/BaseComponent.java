package angerona.fw.gui;

import com.whiplash.gui.WlComponent;

public class BaseComponent extends WlComponent {

	/** kill warning */
	private static final long serialVersionUID = -1482323833112551669L;

	private String name;
	
	public BaseComponent(String name) {
		this.name = name;
	}
	
	@Override
	public String getTitle() {
		return name;
	}
	
	/**
	 * This method returns the type of object which is shown by this component
	 * @return 	null if the component is not bounded to a special type (like a beliefbase type (asp graphs)) otherwise the class
	 * *		object describing the shown object.
	 * TODO: Decide to move this into a subclass?
	 */
	public Class<?> getViewedObject() {
		return null;
	}

}
