package falcun.net.api.oldgui.components.rect;

import falcun.net.api.oldgui.GuiUtils;
import falcun.net.api.oldgui.components.ColoredComponent;
import falcun.net.api.oldgui.region.GuiRegion;

import java.util.function.Supplier;

public class ColorSquare extends ColoredComponent {
	private int curve = 0;


	public ColorSquare(GuiRegion region, Supplier<Integer> color) {
		super(region, color);
	}

	public ColorSquare(GuiRegion region, Supplier<Integer> color, int curve) {
		super(region, color);

		this.curve = curve;
	}

	@Override
	public void draw(int mX, int mY) {
		GuiUtils.drawShape(region, color.get(), this.curve, true, 0);
	}

}
