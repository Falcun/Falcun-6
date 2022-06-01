package net.mattbenson.events.types.entity;

import net.mattbenson.events.Event;
import net.minecraft.entity.Entity;

//Hook at net.minecraft.entity.player.EntityPlayer.java
public class AttackEntityEvent extends Event {
	private Entity entity;
	private Entity target;
	
	public AttackEntityEvent(Entity entity, Entity target) {
		this.entity = entity;
		this.target = target;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public Entity getTarget() {
		return target;
	}
}
