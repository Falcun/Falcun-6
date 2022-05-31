package falcun.net.api.gui.menu;

import falcun.net.api.gui.components.Component;

import java.util.ArrayList;
import java.util.List;

public final class FalcunEmptyPage implements FalcunPage {
	public static FalcunEmptyPage emptyPage = new FalcunEmptyPage();
	private static final List<Component> c = new ArrayList<>(0);
	@Override
	public List<Component> getComponents() {
		return c;
	}
}
