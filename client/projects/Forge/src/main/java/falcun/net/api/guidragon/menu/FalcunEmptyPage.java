package falcun.net.api.guidragon.menu;

import falcun.net.api.guidragon.components.Component;

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
