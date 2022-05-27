package falcun.net.api.gui.pages;

public interface FalcunInGameGuiPage extends FalcunGuiPage {
	int width = 1035;

	default int getWidth() {
		return width;
	}

	int height = 485;

	default int getHeight() {
		return height;
	}
}
