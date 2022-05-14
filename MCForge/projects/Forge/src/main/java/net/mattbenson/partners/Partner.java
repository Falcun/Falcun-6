package net.mattbenson.partners;

public class Partner {
	private String name;
	private String ip;
	
	public Partner(String name, String ip) {
		this.name = name;
		this.ip = ip;
	}
	
	public String getName() {
		return name;
	}
	
	public String getIp() {
		return ip;
	}
}
