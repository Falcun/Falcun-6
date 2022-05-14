package net.mattbenson.events.types.entity;

import net.mattbenson.events.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class ItemPickupEvent extends Event {
	private EntityLivingBase entity;
	private Entity item;
	
	public ItemPickupEvent(EntityLivingBase entity, Entity item) {
		this.entity = entity;
		this.item = item;
	}

	public EntityLivingBase getEntity() {
		return entity;
	}
	
	public Entity getItem() {
		return item;
	}
}
