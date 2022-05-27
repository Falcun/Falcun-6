package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;
import org.lwjgl.input.Mouse;

public interface ClickListener extends MouseListener {

	@Override
	default boolean onMouseEvent(BasicElement<?> element) {
		if (Mouse.getEventButtonState() && Mouse.getEventButton() != -1) {
			if (element.isMouseOver()) {
				return this.onClick(element);
			}
		}
		return false;
	}

	boolean onClick(BasicElement<?> clickedElement);
}