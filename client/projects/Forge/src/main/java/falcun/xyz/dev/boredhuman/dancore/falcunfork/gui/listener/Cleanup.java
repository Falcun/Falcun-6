package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;

public interface Cleanup extends Listener {
	void cleanup(BasicElement<?> element, Cleanup self);
}