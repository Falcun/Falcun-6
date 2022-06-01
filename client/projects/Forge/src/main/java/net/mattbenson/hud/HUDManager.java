package net.mattbenson.hud;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HUDManager {
	private List<HUD> components;
	
	public HUDManager() {
		this.components = new CopyOnWriteArrayList<>();
	}
	
	public List<HUD> getComponents() {
		return components;
	}
}
