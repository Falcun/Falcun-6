package net.mattbenson.waypoints;

public class Waypoint {
	private String name;
	private String descripton;
	private int x;
	private int y;
	private int z;
	private int color;
	private boolean enabled;
	
	public Waypoint(String name, String descripton, int x, int y, int z) {
		this(name, descripton, x, y, z, true);
	}
	
	public Waypoint(String name, String descripton, int x, int y, int z, boolean enabled) {
		this.name = name;
		this.descripton = descripton;
		this.x = x;
		this.y = y;
		this.z = z;
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return descripton;
	}

	public void setDescription(String descripton) {
		this.descripton = descripton;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
