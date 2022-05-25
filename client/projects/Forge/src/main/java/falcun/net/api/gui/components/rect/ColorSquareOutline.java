package falcun.net.api.gui.components.rect;

import falcun.net.api.gui.GuiUtils;
import falcun.net.api.gui.components.OutlinedComponent;
import falcun.net.api.gui.region.GuiRegion;

import java.util.function.Supplier;

public class ColorSquareOutline extends OutlinedComponent {
	final int total;
	private int curve = 0;

	public ColorSquareOutline(GuiRegion region, Supplier<Integer> color, float lineWidth) {
		super(region, color, lineWidth);
		this.total = (int) ((region.width + region.height) / 1.5);
	}

	public ColorSquareOutline(GuiRegion region, Supplier<Integer> color, float lineWidth, int curve) {
		this(region, color, lineWidth);
		this.curve = curve;
	}

	public ColorSquareOutline(GuiRegion region, int color, float lineWidth) {
		this(region, () -> color, lineWidth);
	}

	public ColorSquareOutline(GuiRegion region, int color, float lineWidth, int curve) {
		this(region, () -> color, lineWidth);
		this.curve = curve;
	}

	@Override
	public void draw(int mX, int mY) {
			GuiUtils.drawShape(region, color.get(), this.curve, false, lineWidth);
	}

}
