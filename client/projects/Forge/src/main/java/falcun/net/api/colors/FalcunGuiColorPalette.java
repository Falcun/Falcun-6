package falcun.net.api.colors;

import falcun.net.api.modules.FalcunModule;

public final class FalcunGuiColorPalette {

	public static int getToggleColor(FalcunModule module) {
		return getToggleColor(module.isEnabled());
	}

	public static int getToggleColor(boolean bool) {
		return bool ? 0xFF17fc03 : 0xfffc0303;
	}

	public static int getBackgroundColor() {
		return 0xd9121116;
	}

	public static int getBackgroundColor2(){
		return 0xd90f1313;
	}

	public static int getLineColor(){
		return 0xFF19191c;
	}
}
