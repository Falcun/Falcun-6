package falcun.net.api.oldgui.components;

import falcun.net.api.oldgui.region.GuiRegion;

import java.util.function.Supplier;

public class OutlinedComponent extends ColoredComponent {

	protected float lineWidth;

	public OutlinedComponent(GuiRegion region, Supplier<Integer> color, float lineWidth) {
		super(region, color);
		this.lineWidth = lineWidth;
	}

}
