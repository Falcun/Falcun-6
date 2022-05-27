package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;

public abstract class HoverListener implements MouseListener {

	Boolean over = null;

	@Override
	public boolean onMouseEvent(BasicElement<?> element) {
		if (element.isMouseOver() && !this.over) {
			this.over = true;
			this.onMouseEnter(element);
		} else if (!element.isMouseOver() && this.over) {
			this.over = false;
			this.onMouseExit(element);
		}
		return false;
	}

	public abstract void onMouseEnter(BasicElement<?> element);

	public abstract void onMouseExit(BasicElement<?> element);
}