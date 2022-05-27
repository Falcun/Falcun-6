package falcun.net.api.oldgui.effects;
import falcun.net.api.oldgui.components.*;

import java.util.function.Consumer;

import org.lwjgl.input.Mouse;

public class OnClickEffect extends Effect {
	Consumer<Component> onClick;
	private long cooldown = 0L;

	public OnClickEffect(Consumer<Component> onClick) {
		this.onClick = onClick;
	}

	@Override
	public void onClick(int mX, int mY, int mouseButton, Component component, Phase phase) {
		if (phase == Phase.AFTER && Mouse.getEventButtonState() && cooldown <= System.currentTimeMillis()) {
			onClick.accept(component);
			cooldown = System.currentTimeMillis() + 20;
		}
	}

}