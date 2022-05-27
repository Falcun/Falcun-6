package falcun.net.api.oldgui.effects;

import falcun.net.Falcun;
import falcun.net.api.oldgui.components.*;

import falcun.net.api.oldgui.region.GuiRegion;
import falcun.net.api.oldgui.scaling.FalcunScaling;
import org.lwjgl.input.Mouse;

public class DragEffect extends Effect {
	int prevMouseX = 0;
	int prevMouseY = 0;
	boolean dragging = false;
	GuiRegion constraints;

	public DragEffect() {

	}

	public DragEffect(GuiRegion constraints) {
		this.constraints = constraints;
	}

	@Override
	public void draw(int mouseX, int mouseY, Component component, Phase phase) {
		if (phase == Phase.AFTER) {
			return;
		}
		float scale = 1f / new FalcunScaling(Falcun.minecraft).getScaleFactor();
		mouseX *= scale;
		mouseY *= scale;
//		int newMouseX = Math.round((float) mouseX / value);
//		int newMouseY = Math.round((float) mouseY / value);
//		mouseX = newMouseX;
//		mouseY = newMouseY;
		if (dragging) {
			int xDelta = mouseX - prevMouseX;
			int yDelta = mouseY - prevMouseY;
			component.region.x += xDelta;
			component.region.y += yDelta;
			prevMouseX = mouseX;
			prevMouseY = mouseY;

		}
		if (constraints != null) {
			if (component.region.x < constraints.x) {
				component.region.x = constraints.x;
			}
			if (component.region.y < constraints.y) {
				component.region.y = constraints.y;
			}
			if (component.region.getRight() > constraints.getRight()) {
				component.region.x = constraints.getRight() - component.region.width;
			}
			if (component.region.getBottom() > constraints.getBottom()) {
				component.region.y = constraints.getBottom() - component.region.height;
			}
		}

	}

	@Override
	public void anyClick(int mX, int mY, int mouseButton, Component component, Phase phase) {
		if (phase == Phase.AFTER) {
			return;
		}
		if (component.isOver(mX, mY) && Mouse.getEventButtonState()) {
			dragging = true;
			return;
		}
		if (!Mouse.getEventButtonState() || !component.isOver(mX, mY) || dragging ) {
			dragging = false;
		}

//		if (component.isOver(mX, mY) && FalcunMouse.getEventButtonState()) {
//			dragging = !dragging;
//			prevMouseX = mX;
//			prevMouseY = mY;
//		} else if (dragging) {
//			dragging = false;
//		}
	}
}
