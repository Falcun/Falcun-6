package falcun.net.oldgui.ingame.fps;

import falcun.net.api.oldgui.components.Component;
import falcun.net.api.oldgui.menu.FalcunPage;
import falcun.net.oldgui.ingame.FalcunInGameMenu;

import java.util.LinkedList;
import java.util.List;

public class FalcunFPSPage implements FalcunPage {

	final List<Component> components;

	public FalcunFPSPage(int width, int height) {
		components = new LinkedList<>(FalcunInGameMenu.bgComponents);
		if (components.isEmpty())return;
	}


	@Override
	public List<Component> getComponents() {
		return components;
	}
}
