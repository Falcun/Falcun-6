package falcun.net.api.guidragon.region;


import java.util.Objects;

public class GuiRegion {
	public int x, y, width, height;


	public GuiRegion(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

	}

	public int getMidX() {
		return x + width / 2;
	}

	public int getMidY() {
		return y + height / 2;
	}

	public int getBottom() {
		return y + height;
	}

	public int getRight() {
		return x + width;
	}

	public GuiRegion offSet(int xOffset, int yOffset) {
		return new GuiRegion(x + xOffset, y + yOffset, width, height);
	}

	public GuiRegion offsetSize(int widthOffset, int heightOffset) {
		return new GuiRegion(x, y, width + widthOffset, height + heightOffset);
	}

	public GuiRegion duplicate() {
		return new GuiRegion(x, y, width, height);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GuiRegion guiRegion = (GuiRegion) o;
		return x == guiRegion.x && y == guiRegion.y && width == guiRegion.width && height == guiRegion.height;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, width, height);
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + "," + width + "," + height + ")";
	}
}
