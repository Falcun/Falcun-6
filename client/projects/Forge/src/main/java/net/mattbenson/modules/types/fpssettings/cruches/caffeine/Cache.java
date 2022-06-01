package net.mattbenson.modules.types.fpssettings.cruches.caffeine;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Cache<K, V> {
	private final static long MAX_ALIVE_TIME = 60 * 1000L;
	
	private Map<K, V> map;
	private long maxSize = -1;
	private long lastGarbageClean;
	
	private CacheWriter<? super K, ? super V> listener;
	
	public Cache<K, V> invalidateAll(List<K> obfuscated) {
		for(K k : obfuscated) {
			if(obfuscated.contains(k)) {
				V value = map.get(k);
				listener.delete(k, value, RemovalCause.PURGE_LIST);
				map.remove(k);
			}
		}
		
		return getSelf();
	}
	
	public Cache<K, V> invalidateAll() {
		for(Entry<K, V> entry : map.entrySet()) {
			listener.delete(entry.getKey(), entry.getValue(), RemovalCause.PURGE_ALL);
		}
		
		map.clear();
		return getSelf();
	}

	public V getIfPresent(K key) {
		long time = System.currentTimeMillis();
		
		if(time - lastGarbageClean > MAX_ALIVE_TIME) {
			invalidateAll();
			lastGarbageClean = time;
		}
		
		return map.getOrDefault(key, null);
	}

	public Cache<K, V> put(K key, V value) {
		map.put(key, value);
		listener.write(key, value);
		
		if(map.size() > maxSize) {
			Entry<K, V> first = map.entrySet().iterator().next();
			listener.delete(first.getKey(), first.getValue(), RemovalCause.TOO_MANY);
			map.remove(first.getKey());
		}
		
		return getSelf();
	}
	
	public <KK extends K, VV extends V> Cache<KK, VV> writer(CacheWriter<? super KK, ? super VV> listener) {
		this.listener = (CacheWriter<K, V>) listener;
		return getSelf();
	}

	public Cache<K, V> maximumSize(long maxSize) {
		this.maxSize = maxSize;
		return getSelf();
	}

	public Cache<K, V> build() {
		map = new LinkedHashMap<K, V>();
		return getSelf();
	}
	
	private <KK extends K, VV extends V> Cache<KK, VV> getSelf() {
		return (Cache<KK, VV>) this;
	}
}
