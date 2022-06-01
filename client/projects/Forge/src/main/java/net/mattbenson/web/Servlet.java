package net.mattbenson.web;

public abstract class Servlet implements IServlet {
	private String id;
	
	public Servlet(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
}
