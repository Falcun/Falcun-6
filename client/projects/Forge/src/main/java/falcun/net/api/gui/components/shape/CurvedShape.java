package falcun.net.api.gui.components.shape;

import falcun.net.api.gui.GuiUtils;
import falcun.net.api.gui.region.GuiRegion;

import java.util.function.Supplier;
public class CurvedShape extends CurvedComponent{

	public CurvedShape(GuiRegion region, Supplier<Integer> color, int curve) {
		super(region, color, curve);
	}

	@Override
	public void draw(int mX, int mY) {
		GuiUtils.drawShape(region, color.get(), curve, true, 0);
	}

}