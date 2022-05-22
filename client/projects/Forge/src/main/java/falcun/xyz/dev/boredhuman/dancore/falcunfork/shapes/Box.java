package falcun.xyz.dev.boredhuman.dancore.falcunfork.shapes;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.Credits;

import java.util.Objects;

@Credits("https://github.com/boredhuman/Dancore/blob/master/src/main/java/dev/boredhuman/api/rendering/shapes/Box.java")
public final class Box {
	public double minX, minY, minZ;
	public double maxX, maxY, maxZ;

	public Box(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		this.minX = Math.min(minX, maxX);
		this.minY = Math.min(minY, maxY);
		this.minZ = Math.min(minZ, maxZ);
		this.maxX = Math.max(minX, maxX);
		this.maxY = Math.max(minY, maxY);
		this.maxZ = Math.max(minZ, maxZ);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}
		Box box = (Box)o;
		return Double.compare(box.minX, this.minX) == 0 && Double.compare(box.minY, this.minY) == 0 && Double.compare(box.minZ, this.minZ) == 0 && Double.compare(box.maxX, this.maxX) == 0 && Double.compare(box.maxY, this.maxY) == 0 && Double.compare(box.maxZ, this.maxZ) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
	}
}