package falcun.net.api.guidragon.components.shape;

import falcun.net.api.guidragon.GuiUtils;
import falcun.net.api.guidragon.region.GuiRegion;

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