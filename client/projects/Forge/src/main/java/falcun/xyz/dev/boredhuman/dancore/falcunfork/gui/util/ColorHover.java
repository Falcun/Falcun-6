package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.MouseListener;

public class ColorHover implements MouseListener {

	int regularColor, hoverColor;
	BasicElement<?> target;

	public ColorHover(int regularColor, int hoverColor) {
		this(regularColor, hoverColor, null);
	}

	// whenever the target is hovered the color of this element will change
	public ColorHover(int regularColor, int hoverColor, BasicElement<?> target) {
		this.regularColor = regularColor;
		this.hoverColor = hoverColor;
		this.target = target;
	}

	@Override
	public boolean onMouseEvent(BasicElement<?> element) {
		if ((this.target != null && this.target.isMouseOver()) || element.isMouseOver()) {
			element.color = this.hoverColor;
		} else {
			element.color = this.regularColor;
		}
		return false;
	}
}