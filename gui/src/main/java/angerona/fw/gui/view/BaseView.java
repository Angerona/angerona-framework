package angerona.fw.gui.view;

import javax.swing.JPanel;

public abstract class BaseView extends JPanel implements View {

	/** kill warning */
	private static final long serialVersionUID = 2840739083262276527L;

	@Override
	public abstract void init();

	@Override
	public abstract void cleanup();

	@Override
	public Class<?> getObservedType() {
		return null;
	}

	@Override
	public void setObservedEntity(Object entity) {
	}

	@Override
	public Object getObservedEntity() {
		return null;
	}

}
