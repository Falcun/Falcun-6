package falcun.net.api.gui.menu;

import falcun.net.api.gui.components.Component;

import java.util.List;

public interface FalcunPage {
	List<Component> getComponents();
	default int getBackgroundWidth(){
		return 1035;
	}
	default int getBackgroundHeight(){
		return 485;
	}
}
