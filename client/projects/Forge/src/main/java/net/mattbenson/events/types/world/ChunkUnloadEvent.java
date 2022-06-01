package net.mattbenson.events.types.world;

import net.mattbenson.events.Event;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

//Hooked at net.minecraft.world.chunk.Chunk.java
public class ChunkUnloadEvent extends Event {
	private World world;
	private Chunk chunk;
	
	public ChunkUnloadEvent(World world, Chunk chunk) {
		this.world = world;
		this.chunk = chunk;
	}
	
	public World getWorld() {
		return world;
	}
	
	public Chunk getChunk() {
		return chunk;
	}
}
