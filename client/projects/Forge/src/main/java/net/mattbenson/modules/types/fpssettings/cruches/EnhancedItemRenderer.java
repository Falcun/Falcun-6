package net.mattbenson.modules.types.fpssettings.cruches;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.mattbenson.modules.types.fpssettings.cruches.caffeine.Cache;
import net.mattbenson.modules.types.fpssettings.cruches.caffeine.CacheWriter;
import net.mattbenson.modules.types.fpssettings.cruches.caffeine.Caffeine;
import net.mattbenson.modules.types.fpssettings.cruches.caffeine.RemovalCause;
import net.minecraft.client.renderer.GLAllocation;

public class EnhancedItemRenderer implements Enhancement {
	private static final List<EnhancedItemRenderer> instances = new ArrayList<>();

	private final Queue<Integer> glRemoval = new ConcurrentLinkedQueue<>();

	private final Cache<ItemHash, Integer> itemCache = Caffeine.newBuilder().maximumSize(5000L)
			.writer(new RemovalListener()).build();

	public String getName() {
		return "Enhanced Item Renderer";
	}

	public void invalidateAll() {
		this.itemCache.invalidateAll();
	}

	public int getGlList() {
		Integer poll = this.glRemoval.poll();
		return (poll == null) ? GLAllocation.generateDisplayLists(1) : poll.intValue();
	}

	public static List<EnhancedItemRenderer> getInstances() {
		return instances;
	}

	public Cache<ItemHash, Integer> getItemCache() {
		return this.itemCache;
	}

	private class RemovalListener implements CacheWriter<ItemHash, Integer> {
		private RemovalListener() {
		}

		public void write(ItemHash key, Integer value) {
		}

		@Override
		public void delete(ItemHash key, Integer value, RemovalCause arg2) {
			if (value == null)
				return;
			EnhancedItemRenderer.this.glRemoval.add(value);
		}

	}
}
