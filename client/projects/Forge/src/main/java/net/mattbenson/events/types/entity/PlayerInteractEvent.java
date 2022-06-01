package net.mattbenson.events.types.entity;

import net.mattbenson.events.Event;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

//Hooked @ net.minecraft.entity.player.EntityPlayer.java
public class PlayerInteractEvent extends Event {
	private Action action;
	private BlockPos pos;
	private World world;
	
	public PlayerInteractEvent(Action action, BlockPos pos, World world) {
		this.action = action;
		this.pos = pos;
		this.world = world;
	}
	
	public Action getAction() {
		return action;
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
	public World getWorld() {
		return world;
	}
	
	public enum Action {
		USE_ITEM,
		LEFT_CLICK_BLOCK,
		RIGHT_CLICK_BLOCK;
	}
}
