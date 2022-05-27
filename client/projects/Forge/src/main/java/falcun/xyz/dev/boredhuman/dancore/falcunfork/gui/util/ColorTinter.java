package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.HoverListener;

public class ColorTinter extends HoverListener {

	public int tint;

	public ColorTinter(int tint) {
		this.tint = tint;
	}

	@Override
	public void onMouseEnter(BasicElement<?> element) {
		element.color += this.tint;
	}

	@Override
	public void onMouseExit(BasicElement<?> element) {
		element.color -= this.tint;
	}
}