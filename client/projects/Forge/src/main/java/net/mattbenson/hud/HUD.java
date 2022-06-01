package net.mattbenson.hud;

public class HUD {
	private String name;
	private String file;
	private int width;
	private int height;
	private int r;
	private int g;
	private int b;
	private int opacity;
	private String image;
	private int posX;
	private int posY;
	
	public HUD(String name, String file, int width, int height, int r, int g, int b, int opacity, String image, int posX, int posY) {
		this.name = name;
		this.file = file;
		this.width = width;
		this.height = height;
		this.r = r;
		this.g = g;
		this.b = b;
		this.opacity = opacity;
		this.image = image;
		this.posX = posX;
		this.posY = posY;
	}
	
	public String getName() {
		return name;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public int getr() {
		return r;
	}
	
	public int getg() {
		return g;
	}
	
	public int getb() {
		return b;
	}
	
	public int getOpacity() {
		return opacity;
	}
	
	public String getImage() {
		return image;
	}
	
	public int getPosX() {
		return posX;
	}
	
	public int getPosY() {
		return posY;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setr(int r) {
		this.r = r;
	}
	
	public void setg(int g) {
		this.g = g;
	}
	
	public void setb(int b) {
		this.b = b;
	}

	public void setOpacity(int opacity) {
		this.opacity = opacity;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public void setposX(int posX) {
		this.posX = posX;
	}
	
	public void setposY(int posY) {
		this.posY = posY;
	}
	
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
}
