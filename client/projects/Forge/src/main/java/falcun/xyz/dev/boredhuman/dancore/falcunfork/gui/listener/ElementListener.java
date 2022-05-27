package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;

public interface ElementListener<T extends BasicElement<?>> extends Listener<T> {

	@Override
	void render(T element, Listener.Phase phase);

}