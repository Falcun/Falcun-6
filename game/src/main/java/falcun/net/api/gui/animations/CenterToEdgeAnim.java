package falcun.net.api.gui.animations;


import falcun.net.api.gui.components.Component;
import falcun.net.api.gui.region.GuiRegion;
import falcun.net.api.gui.util.ScissorManager;
import falcun.net.api.gui.effects.Effect;

public class CenterToEdgeAnim extends Effect {

	AnimationTimer anim;
	public long duration;

	public CenterToEdgeAnim(long duration) {
		this.duration = duration;
	}

	@Override
	public void draw(int mX, int mY, Component component, Phase phase) {
		if (anim != null) {
			if (phase == Phase.BEFORE) {
				GuiRegion toUse = component.region.duplicate();
				toUse.x = component.region.getMidX() - anim.getProgress();
				toUse.width = anim.getProgress() * 2;
				ScissorManager.startScissor(toUse);

			} else {
				ScissorManager.finishScissor();
			}
		} else {
			if (phase == Phase.AFTER) {
				anim = new AnimationTimer(0, component.region.width / 2, duration);
			}
		}
	}
}
