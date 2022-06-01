package net.mattbenson.events.types.world;

import net.mattbenson.events.Event;
import net.minecraft.world.World;

public class WorldLoadEvent extends Event {
	private World world;
	
	public WorldLoadEvent(World world) {
		this.world = world;
	System.out.println("ran3");
	}
	
	public World getWorld() {
		return world;
	}
}
