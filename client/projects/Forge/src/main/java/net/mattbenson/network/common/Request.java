package net.mattbenson.network.common;

import java.util.UUID;

public class Request {
	private long id;
	private long target;
	private String label;
	private UUID uuid;
	private RequestType type;
	
	public Request(long id, long target, String label, UUID uuid, RequestType type) {
		this.id = id;
		this.target = target;
		this.label = label;
		this.uuid = uuid;
		this.type = type;
	}
	
	public long getId() {
		return id;
	}

	public long getTarget() {
		return target;
	}

	public String getLabel() {
		return label;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public RequestType getType() {
		return type;
	}
}
