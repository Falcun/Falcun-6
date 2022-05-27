package falcun.net.oldgui.ingame.mods;

import falcun.net.api.colors.FalcunGuiColorPalette;
import falcun.net.api.fonts.FalcunFont;
import falcun.net.api.fonts.Fonts;
import falcun.net.api.oldgui.GuiUtils;
import falcun.net.api.oldgui.components.Component;
import falcun.net.api.oldgui.components.number.NumberSlider;
import falcun.net.api.oldgui.components.rect.ColorSquare;
import falcun.net.api.oldgui.components.scroll.VerticalScroll;
import falcun.net.api.oldgui.components.text.Label;
import falcun.net.api.oldgui.menu.FalcunPage;
import falcun.net.api.oldgui.region.GuiRegion;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.*;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

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
			final FalcunFont font = Fonts.Roboto;
			final int wid = (int) font.getStringWidth(name);
			if (Number.class.isAssignableFrom(type)) {
				final FalcunBounds bounds = field.getBounds();
				final double min = bounds.min();
				final double max = bounds.max();
				final GuiRegion gr = new GuiRegion(x, y, boxWidth, 10);
				final Label label = new Label(gr, () -> name + " | " + ((FalcunValue<Number>) field.getValue()).getValue(), 0, () -> 0xffffffff, font);
				scroll.addComponent(label);

				final NumberSlider slider = new NumberSlider(gr.offSet(0, 15), 0x00000000, FalcunGuiColorPalette.getToggleColorDarker(true), (FalcunValue<Number>) field.getValue(), min, max);
				slider.region.height += 2;
				scroll.addComponent(slider);
				y += 12;
				y += 17;
				y += 10;
			} else if (type == Boolean.class || type == boolean.class) {
				final GuiRegion gr = new GuiRegion(x, y, boxWidth, 10);
				final Label label = new Label(gr, name, 0, 0xffffffff, font);

				scroll.addComponent(label);
				final Supplier<Integer> color = () -> FalcunGuiColorPalette.getToggleColorDarker(((FalcunValue<Boolean>) field.getValue()).getValue());
				final GuiRegion toggGr = gr.offSet(0, 0);
				toggGr.x = toggGr.getRight() - toggGr.height;
				toggGr.x -= 5;
				toggGr.width = toggGr.height;
				final Component component = new Component(toggGr) {
					@Override
					public void draw(int mX, int mY) {
						int drawcolor = color.get();
						if (isOver(mX,mY) && ((FalcunValue<Boolean>) field.getValue()).getValue()){
							drawcolor = FalcunGuiColorPalette.getToggleColor(true);
						}
						GuiUtils.drawShape(toggGr.offSet(0, 0), drawcolor, 0, true, 0);
					}

					@Override
					public void onClicked(int mX, int mY, int mouseButton) {
						if (isOver(mX, mY)) {
							try {
								FalcunValue<Boolean> val = ((FalcunValue<Boolean>) field.getValue());
								val.setValue(!val.getValue());
							} catch (Throwable err) {
								err.printStackTrace();
							}
						}
					}
				};
				scroll.addComponent(component);

				y += 24;


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
