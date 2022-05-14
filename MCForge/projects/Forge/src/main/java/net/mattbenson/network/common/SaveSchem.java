package net.mattbenson.network.common;

public class SaveSchem {
	private long id;
    private String schem;
    private long group;
    
    public SaveSchem(long id, String schem, long group) {
    	this.id = id;
    	this.schem = schem;
    	this.group = group;
    }
    
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getSchem() {
		return schem;
	}
	
	public void setSchem(String schem) {
		this.schem = schem;
	}
	
	public long getGroup() {
		return group;
	}
	
	public void setGroup(long group) {
		this.group = group;
	}
}
