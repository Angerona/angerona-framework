package angerona.fw.gui.base;

import javax.swing.JPanel;

import bibliothek.gui.dock.DefaultDockable;

import angerona.fw.gui.docking.DecoratorLibrary;
import angerona.fw.internal.Entity;

public abstract class EntityViewComponent<T extends Entity> extends JPanel implements ViewComponent {
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
		setObservedEntity((Entity) obj);
	}
	
	@Override
	public JPanel getPanel() {
		return this;
	}
	
	public abstract void init();

	public abstract void cleanup();
	
	public abstract Class<? extends T> getObservedType();
	
	@Override
	public void decorate(DefaultDockable dockable) {
		DecoratorLibrary.closeDecorator.decorate(dockable);
		if(ref != null) {
			dockable.setTitleText(ref.toString());
		}
	}
}
