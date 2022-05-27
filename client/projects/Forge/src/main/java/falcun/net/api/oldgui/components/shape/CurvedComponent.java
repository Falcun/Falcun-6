package falcun.net.api.oldgui.components.shape;

import falcun.net.api.oldgui.components.ColoredComponent;
import falcun.net.api.oldgui.region.GuiRegion;

import java.util.function.Supplier;

public class CurvedComponent extends ColoredComponent {

	int curve;

	public CurvedComponent(GuiRegion region, Supplier<Integer> color, int curve) {
		super(region, color);
		this.curve = curve;
	}

}