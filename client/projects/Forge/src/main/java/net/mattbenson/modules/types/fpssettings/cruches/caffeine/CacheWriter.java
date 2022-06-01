package net.mattbenson.modules.types.fpssettings.cruches.caffeine;

public interface CacheWriter<K, V> {
	void write(K key, V value);
	void delete(K key, V value, RemovalCause cause);
}