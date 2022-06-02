package falcun.net.api.guidragon.components.arrow;

import falcun.net.api.guidragon.GuiUtils;
import falcun.net.api.guidragon.components.ColoredComponent;
import falcun.net.api.guidragon.region.GuiRegion;

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
