package falcun.net.api.modules.config;

import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

import java.io.Serializable;

public final class FalcunPosition implements Serializable {
	public int x;
	public int y;
	public int z;

	public FalcunPosition(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public FalcunPosition(BlockPos pos) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
	}

	public FalcunPosition(double xIn, double yIn, double zIn) {
		this(MathHelper.floor_double(xIn), MathHelper.floor_double(yIn), MathHelper.floor_double(zIn));
	}

	public FalcunPosition(Number xIn, Number yIn, Number zIn) {
		this(xIn.intValue(), yIn.intValue(), zIn.intValue());
	}


}
