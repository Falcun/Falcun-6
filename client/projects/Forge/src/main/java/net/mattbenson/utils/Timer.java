package net.mattbenson.utils;

public class Timer {
	public long previousMS = 0L;

	public boolean hasReached(double ms) {
		return ((getTime() - this.previousMS) >= ms);
	}

	public long getTime() {
		return System.nanoTime() / 1000000L;
	}

	public void reset() {
		this.previousMS = getTime();
	}
}