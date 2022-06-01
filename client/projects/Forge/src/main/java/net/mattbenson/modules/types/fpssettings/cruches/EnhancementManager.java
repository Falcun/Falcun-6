package net.mattbenson.modules.types.fpssettings.cruches;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EnhancementManager {
	private static final EnhancementManager instance = new EnhancementManager();

	private final Map<Class<? extends Enhancement>, Enhancement> enhancementMap = new HashMap<>();

	public EnhancementManager() {
		this.enhancementMap.put(EnhancedFontRenderer.class, new EnhancedFontRenderer());
		this.enhancementMap.put(EnhancedItemRenderer.class, new EnhancedItemRenderer());
	}

	public void tick() {
		for (Map.Entry<Class<? extends Enhancement>, Enhancement> entry : this.enhancementMap.entrySet())
			((Enhancement) entry.getValue()).tick();
	}
	
	public <T extends Enhancement> T getEnhancement(Class<T> enhancement) {
		return (T) this.enhancementMap.get(enhancement);
	}

	public Collection<Enhancement> getEnhancements() {
		return this.enhancementMap.values();
	}

	public static EnhancementManager getInstance() {
		return instance;
	}
}
