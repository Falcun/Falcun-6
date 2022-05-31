package falcun.net.gui.mainmenu;

import falcun.net.Falcun;
import falcun.net.api.gui.components.Component;
import falcun.net.api.gui.components.rect.ColorSquare;
import falcun.net.api.gui.menu.FalcunMenu;
import falcun.net.api.gui.region.GuiRegion;
import falcun.net.api.gui.scaling.FalcunScaling;

import java.util.LinkedList;
import java.util.List;

public class FalcunAccountSwitchMenu extends FalcunMenu {

	private final List<Component> comps = new LinkedList<>();

	@Override
	public List<Component> getComponents() {
		return comps;
	}

	@Override
	protected void init() {
		comps.clear();
		int x = 0, y = 0;
		float scale = 1f / new FalcunScaling(Falcun.minecraft).getScaleFactor();
		final int w = Math.round((float) width / scale);
		final int h = Math.round((float) height / scale);
		{
			ColorSquare cs = new ColorSquare(new GuiRegion(0, 0, w, h), () -> 0xff000000);
			comps.add(cs);
		}
	}
}
