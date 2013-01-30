package angerona.fw.gui.view;

import javax.swing.JPanel;

import angerona.fw.internal.Entity;

public abstract class EntityView<T extends Entity> extends JPanel implements View {
	/** kill warning */
	private static final long serialVersionUID = -5753200356361676742L;

	protected T ref;
	
	public void setObservedEntity(T entity) {
		ref = entity;
	}
	
	public T getObservedEntity() {
		return ref;
	}
	
	public void setObservedEntity(Object obj) {
		setObservedEntity((T) obj);
	}
	
	public abstract Class<? extends T> getObservedType();
}
