package falcun.net.gui.ingame.mods;

import falcun.net.api.colors.FalcunGuiColorPalette;
import falcun.net.api.fonts.FalcunFont;
import falcun.net.api.fonts.Fonts;
import falcun.net.api.gui.components.Component;
import falcun.net.api.gui.components.number.NumberSlider;
import falcun.net.api.gui.components.rect.ColorSquare;
import falcun.net.api.gui.components.scroll.VerticalScroll;
import falcun.net.api.gui.menu.FalcunPage;
import falcun.net.api.gui.region.GuiRegion;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.*;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.Pair;

import java.util.LinkedList;
import java.util.List;

public class FalcunModSettingsPage implements FalcunPage {
	private final List<Component> components;

	static int left, top;

	public FalcunModSettingsPage(int left, int top) {
		FalcunModSettingsPage.left = left + 220;
		FalcunModSettingsPage.top = top + 87;
		components = new LinkedList<>();
		update();
	}

	public void addTopBar() { // TODO: ADD TOP BAR
		final int left = FalcunModSettingsPage.left - 220;
		final int top = FalcunModSettingsPage.top - 87;
	}

	public void update() {
		components.clear();
		final FalcunModule module = FalcunModPage.selectedModule;
		if (module == null) return;
		addTopBar();
		int x = left;
		int y = top - 30;
		int boxWidth = getBackgroundWidth() - 220;
		int boxHeight = getBackgroundHeight() - 135;
		boxWidth -= 20;
		boxHeight -= 20;
		final GuiRegion boxRegion = new GuiRegion(x, y, boxWidth, boxHeight);
		{ // TODO: MAKE THIS LOOK NICE
			final ColorSquare square = new ColorSquare(boxRegion, () -> 0x66ffffff);
			components.add(square);
		}
		GuiRegion scrollRegion = new GuiRegion(boxRegion.getRight() - 8, boxRegion.y + 1, 8, boxRegion.height - 2);
		VerticalScroll scroll = new VerticalScroll(scrollRegion, () -> 0x80646464, () -> 0xff333333, boxRegion);
		components.add(scroll);
		x += 10;
		y += 10;
		boxWidth -= 24;
		boxHeight -= 10;
		for (final Pair<FalcunSetting, FalcunField<?>> configDatum : module.configData) {
			final FalcunSetting setting = configDatum.first;
			final FalcunField<?> field = configDatum.second;
			final Class<?> type = field.getParameterizedFieldType();
			final String name = setting.value().toUpperCase();
			final FalcunFont font = Fonts.RobotoSmall;
			final int wid = (int) font.getStringWidth(name);
			if (Number.class.isAssignableFrom(type)) {
				final FalcunBounds bounds = field.getBounds();
				final double min = bounds.min();
				final double max = bounds.max();
				GuiRegion gr = new GuiRegion(x, y, boxWidth, 10);
				final NumberSlider slider = new NumberSlider(gr, 0x00000000, FalcunGuiColorPalette.getToggleColor(true), (FalcunValue<Number>) field.getValue(), min, max);
				scroll.addComponent(slider);
			} else if (type == Boolean.class || type == boolean.class) {

			} else if (type == String.class) {

			} else if (Enum.class.isAssignableFrom(type)) {

			} else if (type == FalcunKeyBind.class) {

			} else if (type == FalcunPosition.class) {

			}
		}
	}

	@Override
	public List<Component> getComponents() {
		return components;
	}

}
