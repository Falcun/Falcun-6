package net.mattbenson.network.network.packets.chunks.data;

import mapwriter.overlay.groups.ChunkOverlayGroup;
import net.mattbenson.network.common.ChunkData;

public class ChunkName extends ChunkData {
	private int x;
	private int y;
	private int z;
	private String server;
	private int dimension;
	private String chunkName;
	private ChunkOverlayGroup chunkOverlay;
	
	public ChunkName(long id, String data, long groupId, int x, int y, int z, String server, int dimension, String chunkName) {
		super(id, data, groupId);
		this.x = x;
		this.y = y;
		this.z = z;
		this.server = server;
		this.dimension = dimension;
		this.chunkName = chunkName;
		this.chunkOverlay = new ChunkOverlayGroup(x >> 4, z >> 4, chunkName);
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

	public String getChunkName() {
		return chunkName;
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
		
		if(!(o instanceof ChunkName)) {
			return defaultVal;
		}
		
		ChunkName obj = (ChunkName) o;
		
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
