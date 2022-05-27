package falcun.net.api.oldgui.effects;
import falcun.net.api.oldgui.components.*;

import falcun.net.api.oldgui.region.GuiRegion;
import falcun.net.api.oldgui.util.ScissorManager;

public class ScissorEffect extends Effect {

	public GuiRegion custom;

	public ScissorEffect(GuiRegion custom) {
		this.custom = custom;
	}

	@Override
	public void draw(int mX, int mY, Component component, Phase phase) {
		if (phase == Phase.BEFORE) {
			ScissorManager.startScissor(custom == null ? component.region : custom);
		} else {
			ScissorManager.finishScissor();
		}
	}

}