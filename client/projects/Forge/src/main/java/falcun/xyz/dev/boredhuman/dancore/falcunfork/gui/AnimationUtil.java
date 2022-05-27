package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui;

import falcun.net.api.modules.config.FalcunValue;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.ElementListener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util.AnimationTimer;

public class AnimationUtil {
	public static <T extends BasicElement<T>> BasicElement<T> makeSmoothVerticalAnim(int defaultHeight, FalcunValue<Integer> extension, BasicElement<T> element, long duration,
	                                                                                 FalcunValue<Boolean> extended) {
		FalcunValue<AnimationTimer> timer = new FalcunValue<>(null);
		FalcunValue<Integer> extendedValueSize = new FalcunValue<>(0);
		extended.callBack = val -> {
			if (timer.getValue() == null && extended.getValue()) {
				timer.setValue(new AnimationTimer(0, extension.getValue(), duration));
				extendedValueSize.setValue(extension.getValue());
			}
		};
		element.addListener((ElementListener) (elem, phase) -> {
			if (timer.getValue() != null) {
				elem.setHeightPx(defaultHeight + timer.getValue().getPosition());
				if (timer.getValue().isDone()) {
					timer.setValue(null);
				}
			} else {
				if (extended.getValue()) {
					element.setHeightPx(defaultHeight + extension.getValue());
				} else {
					element.setHeightPx(defaultHeight);
				}
			}
		});
		return element;
	}
}