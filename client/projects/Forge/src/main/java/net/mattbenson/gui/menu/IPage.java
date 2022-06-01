package net.mattbenson.gui.menu;

public interface IPage {
	void onInit();
	
	void onRender();
	void onLoad();
	void onUnload();
	
	void onOpen();
	void onClose();
}
