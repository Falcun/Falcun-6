package falcun.net.gui.mainmenu.pages;

import falcun.net.api.colors.FalcunGuiColorPalette;
import falcun.net.api.fonts.Fonts;
import falcun.net.api.gui.elements.FalcunLabel;
import falcun.net.api.gui.pages.FalcunFullScreenMenuPage;
import falcun.net.gui.mainmenu.FalcunGuiMainMenu;
import falcun.net.gui.mainmenu.MainMenuPage;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.ContainerElement;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.OutlineElement;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.Row;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.ClickListener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util.ColorHover;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util.Colors;

import java.util.LinkedList;
import java.util.List;

public class PageSelector implements FalcunFullScreenMenuPage {

	private final List<BasicElement<?>> elements;

	public PageSelector() {
		elements = new LinkedList<>();
		elements.add(new ContainerElement().setColor(0xff000000).setDimensions(0, 0, getWidth(), getHeight()));
		int halfw = getWidth() >> 1;
		int halfh = getHeight() >> 1;
		ContainerElement left = new ContainerElement(), right = new ContainerElement();
		left.setDimensions(halfw - (halfw >> 1), halfh, halfw, halfh).setWidthPercent(0.5f).setPadding(2);
		right.setDimensions(halfw, halfh, halfw, halfh).setWidthPercent(0.5f).setPadding(2);
		left.width >>= 1;
		right.width >>= 1;
		for (int i = 1; i < 4; ++i) {
			final int j = i;
			left.addChildren(makeButton(left, MainMenuPage.values()[i].name, () -> FalcunGuiMainMenu.selectedPage = MainMenuPage.values()[j], 3));
		}
		for (int i = 4; i < 7; ++i) {
			final int j = i;
			right.addChildren(makeButton(right, MainMenuPage.values()[i].name, () -> FalcunGuiMainMenu.selectedPage = MainMenuPage.values()[j], 3));
		}
		elements.add(left);
		elements.add(right);
	}

	private static BasicElement<?> makeButton(final BasicElement<?> parent, final String text, final Runnable r, final int buttonAmount) {
		Row row = (Row) new Row().setHeightPercent((1f / buttonAmount) * 0.3f).setPaddingTB(2, 2).setColor(0x00000000);
		ContainerElement buttonBg = new ContainerElement()
			.setColor(0)
			.addListener(new ColorHover(0, 0xFF454545));
		buttonBg.setRounding((int) Math.min(((double) parent.width / buttonAmount) * row.heightPercent, ((double) parent.height / buttonAmount) * row.heightPercent));
		OutlineElement outline = (OutlineElement) new OutlineElement().setColor(0xff2b2b30);
		FalcunLabel label = new FalcunLabel(text, Fonts.RobotoTitle).setColor(Colors.WHITE).setVert(1).setHoriz(1).addListener((ClickListener) element -> {
			r.run();
			return true;
		});
		return row.addChildren(buttonBg.addChildren(label).addChildren(outline));
	}

	@Override
	public List<BasicElement<?>> getElements() {
		return elements;
	}
}
