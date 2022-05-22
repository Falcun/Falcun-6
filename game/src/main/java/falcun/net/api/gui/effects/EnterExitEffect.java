package falcun.net.api.gui.effects;
import com.mojang.realmsclient.gui.ChatFormatting;
import falcun.net.api.gui.components.*;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.function.Consumer;

import falcun.net.api.gui.region.GuiRegion;
import org.lwjgl.input.Mouse;
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
