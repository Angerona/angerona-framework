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

}
