package com.github.kreatures.core.util;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MapObservableTest {
	
	private static final int OP_PUT = 1;
	private static final int OP_REMOVE = 2;
	private static final int OP_CLEAR = 3;
	
	private static class MockMapObserver implements MapObserver {
		
		int op = 0;
		
		String name;
		
		Map<?, ?> changes;
		
		String key;
		
		public void reset() {
			this.name = null;
			this.key = null;
			this.changes = null;
			this.op = 0;
		}

		@Override
		public <K, V> void onPut(String mapName, Map<K, V> changes) {
			op = OP_PUT;
			name = mapName;
			this.changes = changes;
		}

		@Override
		public <K> void onRemove(String mapName, K key) {
			op = OP_REMOVE;
			name = mapName;
			this.key = (String)key;
		}

		@Override
		public void onClear(String mapName) {
			op = OP_CLEAR;
			name = mapName;
		}
		
	}
	
	@Test
	public void singleChanges() {
		ObservableMap<String, String> map = new ObservableMap<>("map1");
		MockMapObserver observer = new MockMapObserver();
		
		map.addMapObserver(observer);
		map.put("name", "Test");
		
		assertEquals("map1", observer.name);
		assertEquals(OP_PUT, observer.op);
		assertEquals(1, observer.changes.size());
		assertEquals(true, observer.changes.keySet().contains("name"));
		assertEquals(true, observer.changes.values().contains("Test"));
		observer.reset();
		
		map.remove("notfound");
		assertEquals(null, observer.name);
		
		map.remove("name");
		assertEquals("map1", observer.name);
		assertEquals(OP_REMOVE, observer.op);
		assertEquals("name", observer.key);
		observer.reset();
	}
	
	@Test
	public void multiChanges() {
		Map<String, String> helper = new HashMap<String, String>();
		helper.put("firstname", "Alice");
		helper.put("lastname", "Beck");
		
		MockMapObserver observer = new MockMapObserver();
		ObservableMap<String, String> map = new ObservableMap<String, String>("map2");
		map.addMapObserver(observer);
		
		map.putAll(helper);
		assertEquals("map2", observer.name);
		assertEquals(2, observer.changes.size());
		assertEquals(helper, observer.changes);
		assertEquals(OP_PUT, observer.op);
		observer.reset();
		
		helper.put("email", "alice.beck@udo.edu");
		map.put("email", "alice.beck@udo.edu");
		observer.reset();
		
		map.clear();
		assertEquals("map2", observer.name);
		assertEquals(0, map.size());
		assertEquals(OP_CLEAR, observer.op);
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void badObserverKey() {
		MapObserver observer = new MapObserver() {

			@Override
			public <K, V> void onPut(String mapName, Map<K, V> changes) {
				// throws an exception cause the changes map is unmodifable
				changes.put(null, null);
				
			}

			@Override
			public <K> void onRemove(String mapName, K key) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onClear(String mapName) {
				// TODO Auto-generated method stub
				
			}
		};
		ObservableMap<String, String> map = new ObservableMap<String, String>("map3");
		map.addMapObserver(observer);
		
		map.put("name", "Alice Beck");
	}
}
