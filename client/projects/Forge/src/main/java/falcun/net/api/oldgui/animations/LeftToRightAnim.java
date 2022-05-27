package falcun.net.api.oldgui.animations;


import falcun.net.api.oldgui.components.Component;
import falcun.net.api.oldgui.region.GuiRegion;
import falcun.net.api.oldgui.util.ScissorManager;
import falcun.net.api.oldgui.effects.Effect;

public class LeftToRightAnim extends Effect {
	AnimationTimer anim;
	public long duration;

	public LeftToRightAnim(long duration) {
		this.duration = duration;
	}

	@Override
	public void draw(int mX, int mY, Component component, Phase phase) {
		if (anim != null) {
			if (phase == Phase.BEFORE) {
				GuiRegion toUse = component.region.duplicate();
				toUse.x = toUse.getRight() - anim.getProgress();
				ScissorManager.startScissor(toUse);
			} else {
				ScissorManager.finishScissor();
			}
		} else {
			if (phase == Phase.AFTER) {
				anim = new AnimationTimer(component.region.width, 0, duration);
			}
		}
	}
}
