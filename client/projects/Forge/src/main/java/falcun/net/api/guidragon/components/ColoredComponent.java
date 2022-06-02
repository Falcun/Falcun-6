package falcun.net.api.guidragon.components;


import falcun.net.api.guidragon.region.GuiRegion;

import java.util.function.Supplier;

public class ColoredComponent extends Component{
	
	public Supplier<Integer> color;

	public ColoredComponent(GuiRegion region, Supplier<Integer> color) {
		super(region);
		this.color = color;
	}

	@Override
	public void draw(int mX, int mY) {
		
	}

}
