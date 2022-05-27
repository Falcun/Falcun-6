package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.Listener;

public class Scissor<T extends BasicElement<?>> implements Listener<T> {

	@Override
	public void render(T element, Phase phase) {
		if (phase == Phase.PRE) {
			ScissorUtil.scissorBegin(element.x, element.y, element.width, element.height);
		} else if (phase == Phase.POST) {
			ScissorUtil.scissorEnd();
		}
	}
}