package angerona.fw.internal;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.gui.view.View;

public class ViewFactory {
	/** reference to logger implementation */
	private Logger LOG = LoggerFactory.getLogger(ViewFactory.class);
	
	/** map containing an Entity as key mapping to all the views showing the entity */
	private Map<Entity, List<View>> registeredViewsByEntity = new HashMap<Entity, List<View>>();
	
	/**
	 * Creates (but not add) a view for the given AgentComponent. 
	 * @param comp	Reference to the component which should be showed in the new view.
	 * @return	reference to the created view. null if no view for the AgentComponent is
	 * 			registered or an error occured.
	 */
	public View createViewForEntityComponent(Entity comp) {
		for (Class<? extends View> cls : UIPluginInstatiator.getInstance().getViewMap().values()) {
			View view = null;
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
				View newly = createEntityView(cls, comp);
				return newly;
			}
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
	public <E extends Entity, T extends View> T createEntityView(Class<? extends T> cls, E toObserve) {
		T reval;
		try {
			reval = cls.newInstance();
			if(toObserve != null) {
				reval.setObservedEntity(toObserve);
				if(!registeredViewsByEntity.containsKey(toObserve)) {
					registeredViewsByEntity.put(toObserve, new LinkedList<View>());
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
	public <T extends View> T getBaseViewObservingEntity(Entity ent) {
		if(registeredViewsByEntity.containsKey(ent)) {
			return (T) registeredViewsByEntity.get(ent).get(0);
		}
		return null;
	}
	
	private static ViewFactory mInstance;
	
	public static ViewFactory getInstance() {
		if(mInstance == null) {
			mInstance = new ViewFactory();
		}
		return mInstance;
	}
}
