package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;

public interface KeyListener extends Listener {
	// return true if the key has been consumed
	boolean onKeyEvent(BasicElement<?> element);
}