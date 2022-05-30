package falcun.net.api.modules.hud;

import falcun.net.api.colors.FalcunColor;
import falcun.net.api.colors.FalcunColorCreator;
import falcun.net.api.fonts.FalcunFont;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunConfigValue;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.api.primitive.doubles.DoublePair;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public abstract class FalcunHudModule extends FalcunModule {

	protected final int IGNORE_THIS_METHOD() { // prevents a retard from extending falcunhudmodule and implementing falcuneventbusmodule in the same class
		return 0;
	}

	@FalcunConfigValue("screenpos")
	public FalcunValue<DoublePair> screenPosition = new FalcunValue<>(new DoublePair(0, 0));

	@FalcunSetting("Text Color")
	public FalcunValue<FalcunColor> textColor = new FalcunValue<>(FalcunColor.of(0xffffffff));
	@FalcunSetting("Shadow")
	public FalcunValue<Boolean> shadowEnabled = new FalcunValue<>(false);
	@FalcunSetting("Chroma")
	public FalcunValue<Boolean> chromaEnabled = new FalcunValue<>(false);


	@FalcunSetting("Background Color")
	public FalcunValue<FalcunColor> bgColor = new FalcunValue<>(new FalcunColorCreator().setAlpha(100).setRed(0).setGreen(0).setBlue(0).getColor());
	@FalcunSetting("Background")
	public FalcunValue<Boolean> backgroundEnabled = new FalcunValue<>(true);

	@FalcunConfigValue("Scale")
	public FalcunValue<Integer> scale = new FalcunValue<>(200);

	@FalcunConfigValue("width")
	public FalcunValue<Integer> width = new FalcunValue<>(0);
	@FalcunConfigValue("height")
	public FalcunValue<Integer> height = new FalcunValue<>(0);

	public abstract void render();

	public abstract void renderPreview();
	protected void renderTextModule(String text, FalcunFont font, int maxWid) {
		int x = getIntFromDouble(this.screenPosition.getValue().first), y = getIntFromDouble(this.screenPosition.getValue().second);
		int strheight = (int)font.stringHeight(text) + 9;
		if (this.backgroundEnabled.getValue()) {
			Gui.drawRect(x,
				y,
				x + maxWid + 10,
				y + ((int) font.stringHeight(text)) + 9, bgColor.getValue().color);
		}
		x += ((maxWid >> 1) - ((int) font.getStringWidth(text) >> 1)) + 5;
			if (!chromaEnabled.getValue()) {
				font.drawString(text, x, y, textColor.getValue().color, false);
			}
		this.width.setValue(maxWid + 10);
		this.height.setValue(strheight);
	}


	private int getIntFromDouble(Double d) {
		return (int) (double) d;
	}


}
