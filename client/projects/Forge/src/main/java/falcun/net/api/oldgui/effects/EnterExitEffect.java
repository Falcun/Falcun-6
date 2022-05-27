package falcun.net.api.oldgui.effects;
import falcun.net.api.oldgui.components.*;

import java.util.function.BiConsumer;

public class EnterExitEffect extends Effect {

	boolean over = false;
	BiConsumer<Component, Boolean> onChange;

	public EnterExitEffect(BiConsumer<Component, Boolean> onChange) {
		this.onChange = onChange;
	}

	@Override
	public void draw(int mX, int mY, Component component, Phase phase) {
		if (phase == Phase.AFTER) {
			return;
		}
		if (!over && component.isOver(mX, mY)) {
			over = true;
			onChange.accept(component, true);
		} else if (over && !component.isOver(mX, mY)) {
			over = false;
			onChange.accept(component, false);
		}
	}
}
