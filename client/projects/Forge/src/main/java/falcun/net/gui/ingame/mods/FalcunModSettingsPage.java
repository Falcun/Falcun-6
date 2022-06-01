package falcun.net.gui.ingame.mods;

import falcun.net.api.colors.FalcunGuiColorPalette;
import falcun.net.api.fonts.FalcunFont;
import falcun.net.api.fonts.Fonts;
import falcun.net.api.gui.GuiUtils;
import falcun.net.api.gui.components.Component;
import falcun.net.api.gui.components.number.NumberSlider;
import falcun.net.api.gui.components.rect.ColorSquare;
import falcun.net.api.gui.components.rect.ColorSquareOutline;
import falcun.net.api.gui.components.scroll.VerticalScroll;
import falcun.net.api.gui.components.text.Label;
import falcun.net.api.gui.effects.OnClickEffect;
import falcun.net.api.gui.menu.FalcunPage;
import falcun.net.api.gui.region.GuiRegion;
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
			final ColorSquare square = new ColorSquare(boxRegion, () -> 0x80111111);
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
			final FalcunValue<?> fieldValue = (FalcunValue<?>) field.getValue();
			final Class<?> type = field.getParameterizedFieldType();
			final String name = setting.value().toUpperCase();
			final FalcunFont font = Fonts.Roboto12;
			final int wid = (int) font.getStringWidth(name);
			FalcunKey falcunKey = field.getKey();

			if (Number.class.isAssignableFrom(type) && falcunKey == null) {
				final FalcunBounds bounds = field.getBounds();
				final double min = bounds.min();
				final double max = bounds.max();
				final GuiRegion gr = new GuiRegion(x, y, boxWidth, 10);
				final Supplier<String> txt = () -> name + " | " + Math.round(100 * ((FalcunValue<Number>) field.getValue()).getValue().doubleValue()) / 100d;
				final Label label = new Label(gr, txt, 0, () -> 0xffffffff, font);
				scroll.addComponent(label);

				final NumberSlider slider = new NumberSlider(gr.offSet(0, 15), 0x00000000, FalcunGuiColorPalette.getToggleColorDarker(true), (FalcunValue<Number>) field.getValue(), min, max);
				slider.region.height += 6;
				scroll.addComponent(slider);
				y += 18;
				y += 17;
				y += 17;

			} else if (type == Boolean.class || type == boolean.class) {
				final GuiRegion gr = new GuiRegion(x, y, boxWidth, 10);
				final Label label = new Label(gr, name, 0, 0xffffffff, font);

				scroll.addComponent(label);
				final Supplier<Integer> color = () -> FalcunGuiColorPalette.getToggleColorDarker(((FalcunValue<Boolean>) field.getValue()).getValue());
				final GuiRegion toggGr = gr.offSet(0, 0);
				toggGr.height += 3;
				toggGr.x = toggGr.getRight() - toggGr.height;
				toggGr.x -= 5;
				toggGr.width = toggGr.height;
				final Component component = new Component(toggGr) {
					@Override
					public void draw(int mX, int mY) {
						int drawcolor = color.get();
						if (isOver(mX, mY) && ((FalcunValue<Boolean>) field.getValue()).getValue()) {
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

				y += 31;


			} else if (type == String.class) {

			} else if (Enum.class.isAssignableFrom(type)) {
				FalcunValue<Enum<?>> fieldCasted = ((FalcunValue<Enum<?>>) fieldValue);
				Enum<?> currentValue = fieldCasted.getValue();
				final Enum[] allValues = currentValue.getDeclaringClass().getEnumConstants();
				final int len = allValues.length;
				Runnable leftArrowClick = () -> {
					int i = (fieldCasted.getValue().ordinal() - 1) % len;
					if (i < 0) {
						i = len - 1;
					}
					Enum<?> newValue = allValues[i];
					fieldCasted.setValue(newValue);
				};
				Runnable rightArrowClick = () -> {
					int i = (fieldCasted.getValue().ordinal() + 1) % len;
					if (i > len - 1) {
						i = 0;
					}
					Enum<?> newValue = allValues[i];
					fieldCasted.setValue(newValue);
				};
				final GuiRegion gr = new GuiRegion(x, y, boxWidth, 14);
				final Label label = new Label(gr, name, 0, 0xffffffff, font);
				final GuiRegion right = gr.offSet(0, 0);
				right.x = right.getRight() - right.height;
				right.x -= 10;
				right.width = right.height;
				right.width += 5;
				ColorSquare rightSq = GuiUtils.makeSquare(right, 0xff000000);
				ColorSquareOutline rightOutline = new ColorSquareOutline(right, 0x66ffffff, 0.04f, 0);
				int stringlen = 0;
				for (Enum allValue : allValues) {
					stringlen = Math.max((int) font.getStringWidth(allValue.name().toUpperCase()), stringlen);
				}
				stringlen += 10;
				GuiRegion center = new GuiRegion(right.x - stringlen, right.y, stringlen, right.height);
				ColorSquare fontbg = GuiUtils.makeSquare(center, 0xff000000);

				GuiRegion left = new GuiRegion(center.x - right.width, right.y, right.width, right.height);
				ColorSquare leftSq = GuiUtils.makeSquare(left, 0xff000000);
				leftSq.effects.add(new OnClickEffect(comp -> leftArrowClick.run()));
				rightSq.effects.add(new OnClickEffect(comp -> rightArrowClick.run()));

				ColorSquareOutline leftOutline = new ColorSquareOutline(left, 0x66ffffff, 0.04f, 0);
				Label val = new Label(center.offSet(0, -2), () -> fieldCasted.getValue().name().toUpperCase().replaceAll("_", " "), 1, () -> 0xffffffff, font);
				scroll.addComponent(label);

				ColorSquareOutline centerOutline = new ColorSquareOutline(center, 0x44ffffff, 0.04f, 0);

				Label leftArrow = new Label(left.offSet(0, -3), "<", 1, 0xffffffff, Fonts.Roboto);
				Label rightArrow = new Label(right.offSet(0, -3), ">", 1, 0xffffffff, Fonts.Roboto);

				scroll.addComponent(rightSq);
				scroll.addComponent(leftSq);
				scroll.addComponent(fontbg);

				scroll.addComponent(centerOutline);
				scroll.addComponent(leftOutline);
				scroll.addComponent(rightOutline);
				scroll.addComponent(val);
				scroll.addComponent(leftArrow);
				scroll.addComponent(rightArrow);
				y += 31;


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
