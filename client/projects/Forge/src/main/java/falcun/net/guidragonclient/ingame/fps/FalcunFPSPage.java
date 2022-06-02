package falcun.net.guidragonclient.ingame.fps;

import falcun.net.api.guidragon.components.Component;
import falcun.net.api.guidragon.menu.FalcunPage;
import falcun.net.guidragonclient.ingame.FalcunInGameMenu;

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
