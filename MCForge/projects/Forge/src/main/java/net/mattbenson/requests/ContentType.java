package net.mattbenson.requests;

public enum ContentType {
	NONE(""), JSON("application/json"), FORM("application/x-www-form-urlencoded"), MULTIPART_FORM("multipart/form-data");
	
	private String header;
	
	ContentType(String header) {
		this.header = header;
	}
	
	@Override
	public String toString() {
		return header;
	}
}
