package falcun.net.api.fonts;

public final class CD {
	public int x ,y, width, height;
	public CD(int x, int y, int width, int height){
		this.x = x;this.y =y;this.width =width;this.height = height;
	}
	public int getX(){
		return this.x;
	}
	public int getY(){
		return this.y;
	}

	public int getHeight() {
		return this.height;
	}

	public int getWidth() {
		return this.width;
	}
}
