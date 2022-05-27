package falcun.net.api.oldgui.components.arrow;

import falcun.net.api.oldgui.GuiUtils;
import falcun.net.api.oldgui.components.ColoredComponent;
import falcun.net.api.oldgui.region.GuiRegion;

import java.util.function.Supplier;

public final class ArrowComponent extends ColoredComponent {

	float lineWidth;

	public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;

	public ArrowComponent(GuiRegion region, Supplier<Integer> color, float lineWidth) {
		super(region, color);
		this.lineWidth = lineWidth;
	}

	@Override
	public void draw(int mX, int mY) {
		GuiUtils.drawRightArrow(region, color.get(), lineWidth);
	}

}
