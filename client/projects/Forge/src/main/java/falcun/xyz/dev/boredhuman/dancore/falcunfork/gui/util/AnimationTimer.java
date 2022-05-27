package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util;

public class AnimationTimer {

	public int start;
	public int end;
	public long duration;
	long startTime = System.currentTimeMillis();

	public AnimationTimer(int start, int end, long duration) {
		this.start = start;
		this.end = end;
		this.duration = duration;
	}

	public int getPosition() {
		int dif = this.end - this.start;
		float progress = Math.min((System.currentTimeMillis() - this.startTime) / (float) this.duration, 1.0F);
		return this.start + (int) (dif * progress);
	}

	public boolean isDone() {
		return System.currentTimeMillis() > this.startTime + this.duration;
	}

	public void inverse() {
		int start = this.start;
		this.start = this.end;
		this.end = start;
		this.startTime = System.currentTimeMillis();
	}

	public long getStartTime() {
		return this.startTime;
	}
}