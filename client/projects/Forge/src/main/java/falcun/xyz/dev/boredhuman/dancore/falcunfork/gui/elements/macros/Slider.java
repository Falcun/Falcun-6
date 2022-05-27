package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.macros;

import falcun.net.api.modules.config.FalcunValue;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.ContainerElement;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.ElementListener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.Listener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.MouseListener;
import org.lwjgl.input.Mouse;

public class Slider {

	public static ContainerElement makeSlider(FalcunValue<Number> slider, Number min, Number max) {
		return Slider.makeSlider(slider, new FalcunValue<>(min), new FalcunValue<>(max));
	}
	public static <T extends Number> ContainerElement makeSlider(FalcunValue<T> slider, FalcunValue<Number> min, FalcunValue<Number> max) {

		FalcunValue<Boolean> dragging = new FalcunValue<>(false);

		return new ContainerElement()
			.setRounding(8)
			.setPaddingTB(2, 2)
			.setPaddingLR(2, 2)
			.addChildren(
				new ContainerElement()
					.setColor(0xFF0F9D58)
					.setRounding(5)
					.setVisible(false)
			)
			.addListener((MouseListener) element -> {
				int mouseX = Mouse.getX();
				if (Mouse.getEventButtonState()) {
					if (element.isMouseOver() && !dragging.getValue()) {
						dragging.setValue(true);
					}
				} else if (Mouse.getEventButton() != -1) {
					dragging.setValue(false);
				}
				if (dragging.getValue()) {
					if (mouseX < element.x) {
						Slider.safeSet(min.getValue(), (FalcunValue<Number>) slider);
					} else if (mouseX > element.x + element.width) {
						Slider.safeSet(max.getValue(), (FalcunValue<Number>) slider);
					} else {
						float percent = (mouseX - element.x) / (float) element.width;
						double dif = max.getValue().doubleValue() - min.getValue().doubleValue();
						Slider.safeSet(min.getValue().doubleValue() + (dif * percent), (FalcunValue<Number>) slider);
					}
				}
				return false;
			})
			.addListener((ElementListener<ContainerElement>) (element, phase) -> {
				if (phase == Listener.Phase.PRE) {
					double percent = 1 - ((slider.getValue().doubleValue() - min.getValue().doubleValue()) / (max.getValue().doubleValue() - min.getValue().doubleValue()));
					element.paddingRight = Math.max((int) (element.width * percent), 2);
					element.children.get(0).setVisible(true);
				}
			});
	}

	public static void safeSet(Number number, FalcunValue<Number> field) {
		if (field.getValue() instanceof Float) {
			field.setValue(number.floatValue());
		} else if (field.getValue() instanceof Integer) {
			field.setValue(number.intValue());
		} else if (field.getValue() instanceof Double) {
			field.setValue(number.doubleValue());
		}
	}
}