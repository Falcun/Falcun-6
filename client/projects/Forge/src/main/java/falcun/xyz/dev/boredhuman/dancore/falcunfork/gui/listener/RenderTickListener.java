package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;

public interface RenderTickListener<T extends BasicElement<?>> extends ElementListener<T> {
	@Override
	default void render(T element, Phase phase) {
		if (phase == Phase.PRE) {
			this.render(element);
		}
	}

	void render(T element);
}