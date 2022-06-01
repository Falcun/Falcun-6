package net.mattbenson.events.types.entity;

import java.util.Collections;
import java.util.Map;

import com.google.common.collect.Maps;

import net.mattbenson.events.Event;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class AttachCapabilitiesEvent extends Event {
	private Entity entity;
	private Map<ResourceLocation, ICapabilityProvider> caps = Maps.newLinkedHashMap();
	private Map<ResourceLocation, ICapabilityProvider> view = Collections.unmodifiableMap(caps);

	public AttachCapabilitiesEvent(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return this.entity;
	}

	public void addCapability(ResourceLocation key, ICapabilityProvider cap) {
		if (caps.containsKey(key))
			throw new IllegalStateException("Duplicate Capability Key: " + key + " " + cap);
		this.caps.put(key, cap);
	}
	
	public Map<ResourceLocation, ICapabilityProvider> getCapabilities() {
		return view;
	}
}
