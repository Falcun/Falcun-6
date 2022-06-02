package falcun.net.api.guidragon.components.shape;

import falcun.net.api.guidragon.components.ColoredComponent;
import falcun.net.api.guidragon.region.GuiRegion;

import java.util.function.Supplier;

public class CurvedComponent extends ColoredComponent {

	int curve;

	public CurvedComponent(GuiRegion region, Supplier<Integer> color, int curve) {
		super(region, color);
		this.curve = curve;
	}

}