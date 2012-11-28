package angerona.fw.reflection;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Tim Janus
 */
public class Context {
	
	/** map containing all the objects saved by the key-string name */
	private Map<String, Object> objects;
	
	public Context() {
		objects = new HashMap<String, Object>();
	}
	
	public Context(Context other) {
		this.objects = new HashMap<String, Object>(other.objects);
	}
	
	/**
	 * Gets the object saved with the given name.
	 * @param name	the name of the object.
	 * @return 	the object with the given name. If the name has a dot in it:
	 * 			the name is split at the first dot and the substr before the dot identifies another
	 * 			Context which can save more values.
	 */
	public Object get(String name) {
		if(name.contains(".")) {
			String [] pair = name.split("\\.", 2);
			Context c = (Context)objects.get(pair[0]);
			return c != null ? c.get(pair[1]) : null;
		}
		
		if(!objects.containsKey(name))
			return null;
		return objects.get(name);
	}

	/**
	 * Set the value of the parameter with the given name to obj.
	 * If the parameter exists it is replaced if not it is created.
	 * @param name	The name of the parameter
	 * @param obj	The object representing the parameter value.
	 */
	public void set(String name, Object obj) {
		if(name.contains(".")) {
			String [] pair = name.split("\\.", 2);
			if(objects.containsKey(pair[0]))
				; // TODO: Throw exception.
			Context c = (Context)objects.get(pair[0]);
			c.set(pair[1], obj);
		} else {
			objects.put(name, obj);
		}
	}
	
	/**
	 * Attach the given context under the given name.
	 * @param name		The name of the parameter in this context representing the new context.
	 * @param context	Reference to the new context which is added to this context as parameter.
	 */
	public void attachContext(String name, Context context) {
		Object ob = get(name);
		if(ob != null && !ob.equals(context))
			;// TODO: Throw Exception.
		set(name, context);
	}
	
	/**
	 * Detachs the context with the given name from this context.
	 * @param name	The name of the context to detach.
	 * @return	true if the context was detached false if the context was not found.
	 */
	public boolean detachContext(String name) {
		Object obj = get(name);
		if(obj == null)					return false;
		if(!(obj instanceof Context))	return false;
		objects.remove(obj);
		return true;
	}
	
	/**
	 * Deletes all objects which were registered to this context.
	 */
	public void clear() {
		objects.clear();
	}
	
	@Override
	public String toString() {
		return toString(new LinkedList<Context>(), 0);
	}
	
	/**
	 * Helper method: helps to generate a recursively string represeentation of the context
	 * @param visited a collection with Contexts which were already visited (no circles)
	 * @param depth the actual depth of the context.
	 * @return	A string representing the given context (looks like a tree).
	 */
	private String toString(List<Context> visited, int depth) {
		visited.add(this);
		
		String pre = "";
		for(int i=0;i<depth;++i)
			pre += "-";
		
		String reval = "";
		for(String str : objects.keySet()) {
			Object obj = objects.get(str);
			if(obj instanceof Context && !visited.contains(obj)) {
				Context next = (Context)obj;
				reval += pre+str+"\n";
				reval += next.toString(visited, depth+1);
			}
		}
		
		for(String str : objects.keySet()) {
			Object obj = objects.get(str);
			if(!(obj instanceof Context)) {
				reval += pre + str + "\n";
			}
		}
		
		return reval;
	}
}
