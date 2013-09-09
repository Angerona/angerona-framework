package com.github.angerona.fw.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * class helps to create "process" contexts for object instances.
 * It use reflection to generate a parameter map.
 * @author Tim Janus
 */
public class ContextFactory {
	
	/**
	 * Creates a new Context object for the given object. The context
	 * can be used for dynamic execution of commando chains.
	 * @param obj	Instance of the object which will be represent by the context object.
	 * @return 		A reference to the newly created context object.
	 */
	public static Context createContext(Object obj) {
		Context context = new Context();
		
		Class<?> c= obj.getClass();
		context.set("self", obj);
		for(Method m : c.getMethods()) {
			String name = m.getName();
			if(m.getParameterTypes().length == 0 && name.startsWith("get") && !name.equals("getContext")) {
				String paramname = name.substring(3, 4).toLowerCase() + name.substring(4); 
				try {
					context.set(paramname, m.invoke(obj));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return context;
	}
}
