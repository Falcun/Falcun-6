package falcun.net.api.guidragon.effects;
import falcun.net.api.guidragon.components.*;

import java.util.function.BiConsumer;

public class OnHoverEffect extends Effect {
	BiConsumer<Component, Phase> onHover;

	public OnHoverEffect(BiConsumer<Component, Phase> onHover) {
		this.onHover = onHover;
	}

	@Override
	public void onHover(int mX, int mY, Component component, Phase phase) {
		onHover.accept(component, phase);
	}

}
