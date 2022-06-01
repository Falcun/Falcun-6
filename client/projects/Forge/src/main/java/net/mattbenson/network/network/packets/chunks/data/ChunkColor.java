package net.mattbenson.network.network.packets.chunks.data;

import mapwriter.overlay.groups.ChunkOverlayGroup;
import net.mattbenson.network.common.ChunkData;

public class ChunkColor extends ChunkData {
	private int x;
	private int y;
	private int z;
	private String server;
	private int dimension;
	private int color;
	private ChunkOverlayGroup chunkOverlay;
	
	public ChunkColor(long id, String data, long groupId, int x, int y, int z, String server, int dimension, int color) {
		super(id, data, groupId);
		this.x = x;
		this.y = y;
		this.z = z;
		this.server = server;
		this.dimension = dimension;
		this.color = color;
		this.chunkOverlay = new ChunkOverlayGroup(x >> 4, z >> 4, color);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public String getServer() {
		return server;
	}

	public int getDimension() {
		return dimension;
	}

	public int getColor() {
		return color;
	}
	
	public ChunkOverlayGroup getOverlay() {
		return chunkOverlay;
	}
	
	@Override
	public boolean equals(Object o) {
		boolean defaultVal = super.equals(o);
		
		if(o == this) {
			return true;
		}
		
		if(!(o instanceof ChunkColor)) {
			return defaultVal;
		}
		
		ChunkColor obj = (ChunkColor) o;
		
		if(obj.getServer().equalsIgnoreCase(server) &&
				obj.getDimension() == dimension &&
				obj.getX() >> 4 == x >> 4 &&
				obj.getY() >> 4 == y >> 4 &&
				obj.getZ() >> 4 == z >> 4) {
			return true;
		}
		
		return defaultVal;
	}
}
