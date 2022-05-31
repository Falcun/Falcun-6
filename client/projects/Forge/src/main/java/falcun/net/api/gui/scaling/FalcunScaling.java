package falcun.net.api.gui.scaling;

import net.minecraft.client.Minecraft;

public class FalcunScaling {
	private int falcunScale;

	public FalcunScaling(Minecraft falcun){
		this(falcun, falcun.gameSettings.guiScale);
	}

	public FalcunScaling(Minecraft falcun, int scale) {
		int falcunWidth = falcun.displayWidth;
		int falcunHeight = falcun.displayHeight;
		this.falcunScale = 1;
		boolean flag = falcun.isUnicode();
		int i = scale;

		if (i == 0) {
			i = 1000;
		}

		while (this.falcunScale < i && falcunWidth / (this.falcunScale + 1) >= 320 && falcunHeight / (this.falcunScale + 1) >= 240) {
			++this.falcunScale;
		}

		if (flag && this.falcunScale % 2 != 0 && this.falcunScale != 1) {
			--this.falcunScale;
		}

	}


	public int getScaleFactor() {
		return this.falcunScale;
	}
}