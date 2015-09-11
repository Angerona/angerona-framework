package com.github.angerona.fw.internal;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.comp.Presentable;
import com.github.angerona.fw.gui.base.EntityViewComponent;
import com.github.angerona.fw.gui.base.ViewComponent;
import com.github.angerona.fw.gui.view.PresentableView;

public class ViewComponentFactory {
	/** reference to logger implementation */
	private Logger LOG = LoggerFactory.getLogger(ViewComponentFactory.class);
	
	/** map containing an Entity as key mapping to all the views showing the entity */
	private Map<Entity, List<ViewComponent>> registeredViewsByEntity = new HashMap<Entity, List<ViewComponent>>();
	
	public <T extends ViewComponent> T createViewComponent(Class<T> cls) {
		try {
			if(!UIPluginInstatiator.getInstance().getViewMap().values().contains(cls)) {
				LOG.warn("The class '{}' is not registered as by the UIPluginInstatiator.", cls.getName());
			}
			return cls.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
	}
	
	/**
	 * Creates (but not add) a view for the given AgentComponent. 
	 * @param comp	Reference to the component which should be showed in the new view.
	 * @return	reference to the created view. null if no view for the AgentComponent is
	 * 			registered or an error occured.
	 */
	public EntityViewComponent createViewForEntityComponent(Entity comp) {
		for (Class<? extends EntityViewComponent> cls : UIPluginInstatiator.getInstance().getEntityViewMap().values()) {
			EntityViewComponent view = null;
			try {
				view = cls.newInstance();
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(view == null)
				return null;
			
			if (comp.getClass().equals(view.getObservedType())) {
				EntityViewComponent newly = createEntityView(cls, comp);
				return newly;
			}
		}
		
		// added simple view for Presentable
		if (comp instanceof Presentable) {
			return createEntityView(PresentableView.class, comp);
		}
		
		LOG.warn("Cannot find UI-View for '{}'", 
				comp.getClass().getName());
		return null;
	}
	
	/**
	 * creates and initialized an UI View.
	 * @param cls class information about the UI component which should be created.
	 * @param toObserve	reference to the object the UI component should observe (might be null if no direct mapping between observed object and UI component can be given)
	 * @return a new instance of UIComponent which is ready to use.
	 */
	public <E extends Entity, T extends EntityViewComponent> 
	T createEntityView(Class<? extends T> cls, E toObserve) {
		T reval;
		try {
			reval = cls.newInstance();
			if(toObserve != null) {
				Entity ent = (Entity) toObserve;
				reval.setObservedEntity(ent);
				if(!registeredViewsByEntity.containsKey(toObserve)) {
					registeredViewsByEntity.put(toObserve, new LinkedList<ViewComponent>());
				}
				registeredViewsByEntity.get(toObserve).add(reval);
			}
			reval.init();
			return reval;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ViewComponent> T getBaseViewObservingEntity(Entity ent) {
		if(registeredViewsByEntity.containsKey(ent)) {
			return (T) registeredViewsByEntity.get(ent).get(0);
		}
		return null;
	}
	
	private static ViewComponentFactory mInstance;
	
	public static ViewComponentFactory get() {
		if(mInstance == null) {
			mInstance = new ViewComponentFactory();
		}
		return mInstance;
	}
}
