package net.mattbenson.events.types.entity;

import net.mattbenson.events.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

//Hooked @ net.minecraft.entity.player.Entity.java
public class EntityCreateEvent extends Event {
	private Entity player;
	private World world;
	
	public EntityCreateEvent(Entity player, World world) {
		this.player = player;
		this.world = world;
	}
	
	public Entity getEntity() {
		return player;
	}
	
	public World getWorld() {
		return world;
	}
}
