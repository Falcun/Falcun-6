package falcun.net.api.guidan.pages;

public interface FalcunFullScreenMenuPage extends FalcunGuiPage {
	default int getWidth() {
		return getFullWidth();
	}

	default int getHeight() {
		return getFullHeight();
	}

}
