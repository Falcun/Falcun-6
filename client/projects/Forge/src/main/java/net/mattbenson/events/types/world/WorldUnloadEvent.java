package net.mattbenson.events.types.world;

import net.mattbenson.events.Event;
import net.minecraft.world.World;

//Code at net.minecraft.server.MinecraftServer.java
//Code at net.minecraft.server.integrated.IntegratedServer.java

public class WorldUnloadEvent extends Event {
	private World world;
	
	public WorldUnloadEvent(World world) {
		this.world = world;
	}
	
	public World getWorld() {
		return world;
	}
}
