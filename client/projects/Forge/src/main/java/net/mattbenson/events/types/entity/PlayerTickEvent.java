package net.mattbenson.events.types.entity;

import net.mattbenson.events.Event;
import net.minecraft.entity.player.EntityPlayer;

//Hooked @ net.minecraft.entity.player.EntityPlayer.java
public class PlayerTickEvent extends Event {
	private EntityPlayer player;
	
	public PlayerTickEvent(EntityPlayer player) {
		this.player = player;
	}
	
	public EntityPlayer getPlayer() {
		return player;
	}
}
