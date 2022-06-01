package net.mattbenson.events.types.entity;

import net.mattbenson.events.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.DamageSource;

//Hooked at net.minecraft.entity.EntityMinecartCommandBlock.java
//Hooked at net.minecraft.entity.item.EntityMinecartContainer.java
//Hooked at net.minecraft.entity.item.EntityMinecartEmpty.java
//Hooked at net.minecraft.entity.item.EntityMinecartFurnace.java
//Hooked at net.minecraft.entity.item.EntityMinecartHopper.java
public class MinecartInteractEvent extends Event {
	private Entity entity;
	private EntityMinecart minecart;
	
	public MinecartInteractEvent(Entity entity, EntityMinecart minecart) {
		this.entity = entity;
		this.minecart = minecart;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public EntityMinecart getMinecart() {
		return minecart;
	}
}
