package net.mattbenson.network.common;

public class WaypointMarker {
	private long id;
    private String name;
    private String marker;
    private long group;
    
    public WaypointMarker(long id, String name, String marker, long group) {
    	this.id = id;
    	this.name = name;
    	this.marker = marker;
    	this.group = group;
    }
    
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMarker() {
		return marker;
	}
	
	public void setMarker(String marker) {
		this.marker = marker;
	}
	
	public long getGroup() {
		return group;
	}
	
	public void setGroup(long group) {
		this.group = group;
	}
}
