package falcun.net.api.gui.components.shape;

import falcun.net.api.gui.GuiUtils;
import falcun.net.api.gui.region.GuiRegion;

import java.util.function.Supplier;

public class CurvedShapeOutline extends CurvedComponent {

	float lineWidth;

	public CurvedShapeOutline(GuiRegion region, Supplier<Integer> color, int curve, float lineWidth) {
		super(region, color, curve);
		this.lineWidth = lineWidth;
	}

	@Override
	public void draw(int mX, int mY) {
		GuiUtils.drawShape(region, color.get(), curve, false, lineWidth);
	}

}