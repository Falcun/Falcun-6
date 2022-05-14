package net.mattbenson.modules.types.fpssettings.cruches.caffeine;

public class Caffeine {
	public static<K, V> Cache<K, V> newBuilder() {
		return new Cache<K, V>();
	}
}
