package falcun.net.api.guidragon.components.circle;

import falcun.net.api.guidragon.GuiUtils;
import falcun.net.api.guidragon.components.OutlinedComponent;
import falcun.net.api.guidragon.region.GuiRegion;

import java.util.function.Supplier;

public class CircleOutline extends OutlinedComponent {

	public CircleOutline(GuiRegion region, Supplier<Integer> color, float lineWidth) {
		super(region, color, lineWidth);
	}

	@Override
	public void draw(int mX, int mY) {
		GuiUtils.drawShape(region, color.get(), region.width / 2, false, 0);
	}

}
