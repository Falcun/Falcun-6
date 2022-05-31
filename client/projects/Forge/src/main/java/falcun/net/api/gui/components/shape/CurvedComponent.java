package falcun.net.api.gui.components.shape;

import falcun.net.api.gui.components.ColoredComponent;
import falcun.net.api.gui.region.GuiRegion;

import java.util.function.Supplier;

public class CurvedComponent extends ColoredComponent {

	int curve;

	public CurvedComponent(GuiRegion region, Supplier<Integer> color, int curve) {
		super(region, color);
		this.curve = curve;
	}

}