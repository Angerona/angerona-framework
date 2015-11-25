package com.github.kreaturesfw.core;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * A simple type-safe cache interface using simple getter, setter and 
 * management methods. It uses a priority mechanism which gives those cache
 * entries a higher priority which were accessed earlier than the cache
 * entries with a lower priority. The parameter minSize and maxSize are used
 * to configure how big a cache map can get and how far it shrinks if the
 * cache is optimized for memory.
 * 
 * @author Tim Janus
 *
 * @param <TKey>	The type of the key. It is important that the methods 
 * 					equals() and hashCode() are implemented correctly for
 * 					this type.
 * 					
 * @param <TValue>	The type of the values saved in the cache.
 */
public class Cache<TKey, TValue> {
	/** the hash map acting as cache data storage */
	private Map<TKey, TValue> cacheMap = new HashMap<>();
	
	/** 
	 * a dequeue saving the priority of the cache keys. The keys at the
	 * front of the dequeue have higher priority than the keys at the end.
	 */
	private Deque<TKey> lastAccess = new LinkedList<>();
	
	/** 
	 * the minimal size of the cache decides how strong the cache shrinks
	 * when optimize cache is called.
	 */
	private int minSize = 10;
	
	private int maxSize = 30;
	
	/**
	 * Receives the value object in the cache for the given key
	 * @param key	the key object
	 * @return		The value object saved in the cache or null if no
	 * 				cache object exists.
	 */
	public TValue getCacheValue(TKey key){
		TValue reval = cacheMap.get(key);
		if(reval != null) {
			updateAccess(key);
		}
		return reval;
	}
	
	/**
	 * Sets the given value in the cache under the provided key parameter.
	 * If the key already exists in the cache its value object is overriden.
	 * @param key		The key of the new cache value
	 * @param value		The value object of the new cache value
	 * @return
	 */
	public TValue setCacheValue(TKey key, TValue value) {
		updateAccess(key);
		return cacheMap.put(key, value);
	}
	
	/**
	 * Removes the value of the given key from the cache.
	 * @param key	The key of the cache value.
	 * @return true if the key exists and is removed false otherwise.
	 */
	public boolean removeCacheValue(TKey key) {
		boolean reval = cacheMap.remove(key) != null;
		if(reval) {
			lastAccess.remove(key);
		}
		return reval;
	}
	
	/**
	 * optimize the cache by removing cache elements with the lowest 
	 * priority as long as the cache is bigger than minSize.
	 */
	public void optimizeCache() {
		// shrink the last access dequeue to minSize
		while(lastAccess.size() > minSize) {
			lastAccess.removeLast();
		}
		
		// remove all cache entries which are not part of the last access dequeue
		for(TKey key : cacheMap.keySet()) {
			if(!(lastAccess.contains(key))) {
				cacheMap.remove(key);
			}
		}
	}
	
	/**
	 * Removes everything from the cache
	 */
	public void clearCache() {
		lastAccess.clear();
		cacheMap.clear();
	}
	
	/**
	 * Sets the minSize of the cache. When optimizing the cache the size
	 * of the cacheMap will shrink to minSize.
	 * @param minSize	The minSize of the cache
	 */
	public void setMinSize(int minSize) {
		this.minSize = minSize;
	}
	
	/**
	 * Sets the maxSize of the cache. If a cache entry is added and the cache 
	 * is bigger than maxSize then the entry with the lowest priority to remain
	 * in the cache is removed.
	 * @param maxSize
	 */
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
	
	/**
	 * Helper method: Sorts the last access dequeue. After the method the last access
	 * dequeue contains the key parameter as first element and therefore gives it the
	 * highest priority.
	 * @param key	The key which was access and therefore gets the highest priority.
	 */
	private void updateAccess(TKey key) {
		lastAccess.remove(key);
		lastAccess.addFirst(key);
		while(lastAccess.size() > maxSize) {
			lastAccess.removeLast();
		}
	}
}
