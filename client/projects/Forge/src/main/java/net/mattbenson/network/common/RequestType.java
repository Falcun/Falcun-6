package net.mattbenson.network.common;

public enum RequestType {
	GROUP("GROUP"), FRIEND("FRIEND");
	
	String id;
	
	RequestType(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
}
