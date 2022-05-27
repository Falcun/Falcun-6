package falcun.net.oldgui.test;

import falcun.net.api.colors.FalcunGuiColorPalette;
import falcun.net.api.fonts.Fonts;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.BasicGuiHandler;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.ContainerElement;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.Row;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util.Alignment;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util.Colors;

public class DanFrame extends BasicGuiHandler {
	int lastWidth;
	int lastHeight;

	@Override
	public void initGui() {
		if (!(this.lastWidth == this.width && this.lastHeight == this.height)) {

		}
		this.elements.clear();

		int w = 1035, h = 485;
		int x = (this.mc.displayWidth / 2) - w / 2;
		int y = (this.mc.displayHeight / 2) - h / 2;
		ContainerElement background = new ContainerElement().setColor(FalcunGuiColorPalette.getBackgroundColor()).setRounding(0).setDimensions(x, y, w, h).setPadding(20);
		ContainerElement header = new Row();
		header.addChildren(
//			new LabelElement("Test", Fonts.Roboto).setColor(Colors.WHITE).setHorizontalAlignment(Alignment.Horizontal.CENTER).setVerticalAlignment(Alignment.Vertical.TOP).setWidthPercent(0.5f)
			createLabel("Test", Fonts.Roboto, Alignment.Vertical.TOP, Alignment.Horizontal.CENTER, Colors.WHITE.getIntColor()).setWidthPercent(0.5f)
		);
		background.addChildren(header);

		this.elements.add(background);
	}
}
