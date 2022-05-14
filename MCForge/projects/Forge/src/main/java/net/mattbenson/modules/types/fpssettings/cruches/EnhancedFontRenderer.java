package net.mattbenson.modules.types.fpssettings.cruches;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.mattbenson.modules.types.fpssettings.cruches.caffeine.Cache;
import net.mattbenson.modules.types.fpssettings.cruches.caffeine.CacheWriter;
import net.mattbenson.modules.types.fpssettings.cruches.caffeine.Caffeine;
import net.mattbenson.modules.types.fpssettings.cruches.caffeine.RemovalCause;
import net.minecraft.client.renderer.GLAllocation;

public final class EnhancedFontRenderer implements Enhancement {
	private static final List<EnhancedFontRenderer> instances = new ArrayList<>();

	private final List<StringHash> obfuscated = new ArrayList<>();

	private final Map<String, Integer> stringWidthCache = new HashMap<>();

	private final Queue<Integer> glRemoval = new ConcurrentLinkedQueue<>();

	private final Cache<StringHash, CachedString> stringCache = Caffeine.newBuilder().writer(new RemovalListener())
			.maximumSize(5000L).build();

	public EnhancedFontRenderer() {
		instances.add(this);
	}

	public static List<EnhancedFontRenderer> getInstances() {
		return instances;
	}

	public String getName() {
		return "Enhanced Font Renderer";
	}

	public void tick() {
		this.stringCache.invalidateAll(this.obfuscated);
		this.obfuscated.clear();
	}

	public int getGlList() {
		Integer poll = this.glRemoval.poll();
		return (poll == null) ? GLAllocation.generateDisplayLists(1) : poll.intValue();
	}

	public Queue<Integer> getGlRemoval() {
		return this.glRemoval;
	}

	public void invalidate() {
		this.stringCache.invalidateAll();
	}

	public CachedString get(StringHash key) {
		return (CachedString) this.stringCache.getIfPresent(key);
	}

	public void cache(StringHash key, CachedString value) {
		this.stringCache.put(key, value);
	}
	
	public Map<String, Integer> getStringWidthCache() {
		return this.stringWidthCache;
	}

	public void invalidateAll() {
		this.stringCache.invalidateAll();
	}

	public List<StringHash> getObfuscated() {
		return this.obfuscated;
	}

	private class RemovalListener implements CacheWriter<StringHash, CachedString> {
		private RemovalListener() {
		}

		public void write(StringHash key, CachedString value) {
		}

		public void delete(StringHash key, CachedString value, RemovalCause cause) {
			if (value == null)
				return;
			EnhancedFontRenderer.this.glRemoval.add(Integer.valueOf(value.getListId()));
		}
	}
}
