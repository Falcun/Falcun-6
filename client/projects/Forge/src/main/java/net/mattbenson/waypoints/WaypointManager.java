package net.mattbenson.waypoints;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WaypointManager {
	private List<Waypoint> waypoints;
	
	public WaypointManager() {
		waypoints = new CopyOnWriteArrayList<>();
	}
	
	public List<Waypoint> getWaypoints() {
		return waypoints;
	}
}
