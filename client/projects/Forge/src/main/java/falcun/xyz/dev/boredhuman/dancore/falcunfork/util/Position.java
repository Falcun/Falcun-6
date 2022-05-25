package falcun.xyz.dev.boredhuman.dancore.falcunfork.util;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.Credits;

import java.util.Objects;

@Credits("https://github.com/boredhuman/Dancore/blob/master/src/main/java/dev/boredhuman/util/Position.java")
public final class Position {
	public double x, y, z;

	public Position(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}
		Position position = (Position) o;
		return Double.compare(position.x, this.x) == 0 && Double.compare(position.y, this.y) == 0 && Double.compare(position.z, this.z) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.x, this.y, this.z);
	}
}


