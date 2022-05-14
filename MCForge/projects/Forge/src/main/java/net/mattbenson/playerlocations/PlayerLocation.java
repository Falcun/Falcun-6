package net.mattbenson.playerlocations;

import net.mattbenson.network.common.GroupData;

public class PlayerLocation {
	private String name;
	private long time;
	private GroupData group;
	private double x;
	private double y;
	private double z;
	private int dimension;
	private float health;
	private int potions;
	
	public PlayerLocation(String name, GroupData group, long time, double x, double y, double z, int dimension, float health, int potions) {
		this.name = name;
		this.time = time;
		this.group = group;
		this.x = x;
		this.y = y;
		this.z = z;
		this.dimension = dimension;
		this.health = health;
		this.potions = potions;
	}
	
	public String getName() {
		return name;
	}
	
	public GroupData getGroup() {
		return group;
	}
	
	public long getTime() {
		return time;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
	
	public int getDimension() {
		return dimension;
	}
	
	public float getHealth() {
		return health;
	}
	
	public int getPotions() {
		return potions;
	}
}
