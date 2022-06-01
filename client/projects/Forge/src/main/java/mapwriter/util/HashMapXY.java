package mapwriter.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class HashMapXY<T, V> extends LinkedHashMap<T, Map<T, V>> {
	public void put(T x, T y, V value) {
		if (super.containsKey(y)) {
			super.get(y).put(x, value);
		} else {
			Map<T, V> map = new LinkedHashMap<T, V>();
			map.put(x, value);
			super.put(y, map);
		}
	}

	public V get(T x, T y) {
		return (super.get(y) == null) ? null : super.get(y).get(x);
	}
	
	public V removeValue(T x, T y) {
		return super.containsKey(y) ? super.get(y).remove(x) : null;
	}
	
	//I mean, I guess this will work.
	public Collection<V> valuez() {
		Collection<V> values = new LinkedList<V>();
		
		for(Map<T, V> fractions : super.values()) {
			values.addAll(fractions.values());
		}
		
		return values;
	}
}