package net.mattbenson.network.common;

public class GroupSetting {
	private boolean ping  = true;
	private boolean highlight = false;
	private boolean sharePatchcrumbs = true;
	private boolean wallChecker = false;

	public GroupSetting() {}
	
	public boolean isPing() {
		return ping;
	}

	
	public void setPing(boolean ping) {
		this.ping = ping;
	}
	
	public boolean isHighlight() {
		return highlight;
	}
	
	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}
	
	public boolean isPatchcrumbs() {
		return sharePatchcrumbs;
	}
	
	public void setPatchcrumbs(boolean patchcrumbs) {
		this.sharePatchcrumbs = patchcrumbs;
	}
	
	public boolean iswallChecker() {
		return wallChecker;
	}
	
	public void setwallChecker(boolean wallChecker) {
		this.wallChecker = wallChecker;
	}

}