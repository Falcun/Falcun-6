package falcun.net.api.oldgui.components.circle;

import falcun.net.api.oldgui.GuiUtils;
import falcun.net.api.oldgui.components.OutlinedComponent;
import falcun.net.api.oldgui.region.GuiRegion;

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
