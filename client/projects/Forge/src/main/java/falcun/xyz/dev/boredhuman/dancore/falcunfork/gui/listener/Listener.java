package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;

public interface Listener<T extends BasicElement<?>> {

	default void render(T element, Phase phase) {
	}

	enum Phase {
		PRE, PRECHILD ,POST;
	}

}