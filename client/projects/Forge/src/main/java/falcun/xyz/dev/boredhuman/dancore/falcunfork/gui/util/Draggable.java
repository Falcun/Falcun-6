package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util;

import falcun.net.api.modules.config.FalcunValue;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.ElementListener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.MouseListener;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Draggable {
	public static BasicElement<?> makeDraggable(BasicElement<?> basicElement, BasicElement<?> over) {
		FalcunValue<Integer> clickX = new FalcunValue<>(0), clickY = new FalcunValue<>(0);
		FalcunValue<Integer> initialX = new FalcunValue<>(0);
		FalcunValue<Integer> initialY = new FalcunValue<>(0);
		FalcunValue<Boolean> dragging = new FalcunValue<>(false);
		basicElement.addListener((MouseListener) element -> {
			if (Mouse.getEventButtonState()) {
				if (over.isMouseOver() && !dragging.getValue()) {
					dragging.setValue(true);
					initialX.setValue(basicElement.x);
					initialY.setValue(basicElement.y);
					clickX.setValue(Mouse.getX());
					clickY.setValue(Display.getHeight() - Mouse.getY());
				}
			} else if (Mouse.getEventButton() != -1) {
				dragging.setValue(false);
			}
			return false;
		});
		basicElement.addListener((ElementListener) (element, phase) -> {
			if (dragging.getValue()) {
				element.x = initialX.getValue() + (Mouse.getX() - clickX.getValue());
				element.y = initialY.getValue() + (Display.getHeight() - Mouse.getY() - clickY.getValue());
			}
		});
		return basicElement;
	}
}