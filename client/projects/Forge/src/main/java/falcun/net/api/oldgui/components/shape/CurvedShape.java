package falcun.net.api.oldgui.components.shape;

import falcun.net.api.oldgui.GuiUtils;
import falcun.net.api.oldgui.region.GuiRegion;

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