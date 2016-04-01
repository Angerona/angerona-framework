package com.github.kreatures.gui.base;

import javax.swing.JPanel;

import bibliothek.gui.dock.DefaultDockable;

import com.github.kreatures.gui.docking.DecoratorLibrary;
import com.github.kreatures.core.internal.Entity;

public abstract class EntityViewComponent 
	extends JPanel 
	implements ViewComponent {
	/** kill warning */
	private static final long serialVersionUID = -5753200356361676742L;

	protected Entity ref;
	
	public void setObservedEntity(Entity entity) {
		ref = entity;
	}
	
	public Entity getObservedEntity() {
		return ref;
	}
	
	@Override
	public JPanel getPanel() {
		return this;
	}
	
	public abstract void init();

	public abstract void cleanup();
	
	public abstract Class<? extends Entity> getObservedType();
	
	@Override
	public void decorate(DefaultDockable dockable) {
		DecoratorLibrary.closeDecorator.decorate(dockable);
		if(ref != null) {
			dockable.setTitleText(ref.getClass().getSimpleName());
		}
	}
}
