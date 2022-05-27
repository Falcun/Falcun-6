package falcun.net.api.oldgui.animations;

public final class AnimationTimer {
	public int start;
	public int end;
	public long duration;
	public long creation;

	public AnimationTimer(int start, int end, long duration) {
		this.start = start;
		this.end = end;
		this.duration = duration;
		creation = System.currentTimeMillis();
	}

	public int getProgress() {
		double dif = (double) (System.currentTimeMillis() - creation);
		double percent = dif / duration;
		if (percent > 1) {
			percent = 1;
		}
		int gap = end - start;
		int progress = (int) (percent * gap);
		return start + progress;

	}

	public boolean isDone() {
		return System.currentTimeMillis() > creation + duration;
	}

	public void swapDirection() {
		long now = System.currentTimeMillis();
		long remaingTime = duration + creation - now;
		if(remaingTime < 0) {
			remaingTime = 0;
		}
		creation = now - remaingTime;
		int s = start;
		start = end;
		end = s;
	}

}