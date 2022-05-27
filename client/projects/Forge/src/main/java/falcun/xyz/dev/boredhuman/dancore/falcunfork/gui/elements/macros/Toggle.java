package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.macros;

import falcun.net.api.modules.config.FalcunValue;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.ContainerElement;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.ClickListener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.ElementListener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.Listener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util.AnimationTimer;

public class Toggle {
	public static ContainerElement makeToggle(FalcunValue<Boolean> field, long duration) {

		FalcunValue<AnimationTimer> leftPaddingAnimationTimer = new FalcunValue<>(null);
		FalcunValue<AnimationTimer> rightPaddingAnimationTimer = new FalcunValue<>(null);

		return new ContainerElement()
			.setRounding(7)
			.addChildren(new ContainerElement()
				.setVisible(false)
				.setColor(0xCC888888)
				.setRounding(5)
				.addListener((ElementListener) (element, phase) -> {
					if (phase == Listener.Phase.PRE) {
						if (field.getValue()) {
							element.setColor(0xFF0F9D58);
						} else {
							element.setColor(0xCC888888);
						}
					}
				})
			)
			.setPaddingTB(6, 6)
			.addListener((ClickListener) element -> {
				if (leftPaddingAnimationTimer.getValue() == null) {
					field.setValue(!field.getValue());
					if (field.getValue()) {
						leftPaddingAnimationTimer.setValue(new AnimationTimer(6, element.width / 2, duration));
						rightPaddingAnimationTimer.setValue(new AnimationTimer(element.width / 2, 2, duration));
					} else {
						leftPaddingAnimationTimer.setValue(new AnimationTimer(element.width / 2, 2, duration));
						rightPaddingAnimationTimer.setValue(new AnimationTimer(6, element.width / 2, duration));
					}
				}
				return false;
			})
			.addListener((ElementListener<?>) (element, phase) -> {
				if (phase == Listener.Phase.PRE) {
					if (leftPaddingAnimationTimer.getValue() != null) {
						element.paddingLeft = leftPaddingAnimationTimer.getValue().getPosition();
						element.paddingRight = rightPaddingAnimationTimer.getValue().getPosition();
						if (leftPaddingAnimationTimer.getValue().isDone()) {
							leftPaddingAnimationTimer.setValue(null);
							rightPaddingAnimationTimer.setValue(null);
						}
					} else {
						if (field.getValue()) {
							element.paddingLeft = element.width / 2;
							element.paddingRight = 6;
						} else {
							element.paddingLeft = 6;
							element.paddingRight = element.width / 2;
						}
					}
					element.children.get(0).setVisible(true);
				}
			});
	}

	public static ContainerElement makeToggleFillAnimation(FalcunValue<Boolean> field, long duration) {

		FalcunValue<AnimationTimer> fillAnimation = new FalcunValue<>(null);

		ContainerElement child = new ContainerElement()
			.setRounding(5);

		return new ContainerElement()
			.addListener((ElementListener) (element, phase) -> {
				if (phase == Listener.Phase.POST) {
					if (fillAnimation.getValue() != null) {
						float progress = fillAnimation.getValue().getPosition() / 100F;
						int halfWidth = element.width / 2;
						int halfHeight = element.height / 2;
						int pad = (int) (halfHeight * progress);
						element.paddingTop = pad;
						element.paddingBottom = pad;
						element.paddingLeft = Math.min(pad, halfWidth);
						element.paddingRight = Math.min(pad, halfWidth);

						if (fillAnimation.getValue().isDone()) {
							fillAnimation.setValue(null);
						}
					} else {
						if (field.getValue()) {
							element.setColor(0xFF0F9D58);
						} else {
							element.setColor(0xFF888888);
						}
						child.setVisible(false);
					}
				}
			})
			.addChildren(child)
			.addListener((ClickListener) element -> {
				if (fillAnimation.getValue() == null) {
					field.setValue(!field.getValue());
					fillAnimation.setValue(new AnimationTimer(100, 0, duration));
					child.setVisible(true);
					if (field.getValue()) {
						child.setColor(0xFF0F9D58);
					} else {
						child.setColor(0xFF888888);
					}
				}
				return false;
			});
	}
}