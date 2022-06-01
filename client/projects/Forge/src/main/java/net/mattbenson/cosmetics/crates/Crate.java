package net.mattbenson.cosmetics.crates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.mattbenson.network.common.Reward;
import net.minecraft.entity.Entity;

public abstract class Crate implements ICrateCallback {
	private Entity entity;
	private long start;
	private int renderDelta;
	private int savedX;
	private String lastSelect;
	private long finishTime;
	
	private List<Reward> entries;
	private String reward;
	
	public Crate(Entity entity, List<Reward> entries, String reward) {
		this.entity = entity;
		this.entries = entries;
		this.reward = reward;
		
		Reward win = null;
		for(Reward r : entries) {
			if(r.getName().equalsIgnoreCase(reward)) {
				win = r;
				break;
			}
		}

		List<Reward> rewards =  new ArrayList<>();
		rewards.addAll(entries);
		
		while(rewards.size() < 60) {
			rewards.addAll(rewards);
		}
		
		Collections.shuffle(rewards);
		
		rewards.add(win);
		
		rewards.addAll(rewards);
		
		this.entries = rewards;
		start = System.currentTimeMillis();
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public long getStart() {
		return start;
	}
	
	public List<Reward> getEntries() {
		return entries;
	}
	
	public String getReward() {
		return reward;
	}
	
	public int getRenderDelta() {
		return renderDelta;
	}
	
	public void setRenderDelta(int renderDelta) {
		this.renderDelta = renderDelta;
	}
	
	public String getLastSelect() {
		return lastSelect;
	}
	
	public void setLastSelect(String lastSelect) {
		this.lastSelect = lastSelect;
	}

	public int getSavedX() {
		return savedX;
	}
	
	public void setSavedX(int savedX) {
		this.savedX = savedX;
	}

	public long getFinishTime() {
		return finishTime;
	}
	
	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}
	
}
