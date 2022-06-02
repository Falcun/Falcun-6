package falcun.net.api.guidragon.components;

import falcun.net.api.guidragon.region.GuiRegion;

import java.util.function.Supplier;

public class OutlinedComponent extends ColoredComponent {

	protected float lineWidth;

	public OutlinedComponent(GuiRegion region, Supplier<Integer> color, float lineWidth) {
		super(region, color);
		this.lineWidth = lineWidth;
	}

}
