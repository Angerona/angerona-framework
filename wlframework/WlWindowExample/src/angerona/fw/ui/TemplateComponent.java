package angerona.fw.ui;

import com.whiplash.gui.WlComponent;

public class TemplateComponent extends WlComponent {

	/** kill warning */
	private static final long serialVersionUID = -5921649962118515928L;
	
	private String title;
	
	public TemplateComponent(String title) {
		this.title = title;
	}
	
	@Override
	public String getTitle() {
		return title;
	}

}
