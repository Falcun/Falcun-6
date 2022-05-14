package net.mattbenson.network.common;

public class ChunkData {
	private long id;
	private String data;
	private long groupId;
	
	public ChunkData(long id, String data, long groupId) {
		this.id = id;
		this.data = data;
		this.groupId = groupId;
	}

	public long getId() {
		return id;
	}
	
	public String getData() {
		return data;
	}

	public long getGroup() {
		return groupId;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) {
			return false;
		}
		
		if(!(o instanceof ChunkData)) {
			return false;
		}
		
		ChunkData obj = (ChunkData) o;
		
		if(obj.getId() == id) {
			return true;
		}
		
		if(obj.getData().equalsIgnoreCase(data) && obj.getGroup() == groupId) {
			return true;
		}
		
		return false;
	}
}
