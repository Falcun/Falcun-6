package net.mattbenson.events.types.entity;

import net.mattbenson.events.Event;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

//Hooked at net.minecraft.entity.EntityLivingBase.java
//Hooked at net.minecraft.entity.player.EntityPlayer.java
//Hooked at net.minecraft.entity.player.EntityPlayerMP.java
public class EntityDeathEvent extends Event {
	private Entity entity;
	private DamageSource cause;
	
	public EntityDeathEvent(Entity entity, DamageSource cause) {
		this.entity = entity;
		this.cause = cause;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public DamageSource getCause() {
		return cause;
	}
}
