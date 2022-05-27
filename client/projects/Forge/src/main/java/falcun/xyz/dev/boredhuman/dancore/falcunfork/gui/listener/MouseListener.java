package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;

public interface MouseListener extends Listener {
	// return true if the click has been consumed
	boolean onMouseEvent(BasicElement<?> element);
}