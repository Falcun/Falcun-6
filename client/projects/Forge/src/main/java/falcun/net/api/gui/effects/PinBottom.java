package falcun.net.api.gui.effects;
import falcun.net.api.gui.components.*;

public class PinBottom extends Effect {

	Component pinner;

	public PinBottom(Component pinner) {
		this.pinner = pinner;
	}

	@Override
	public void draw(int mX, int mY, Component component, Phase phase) {
		if(phase == Phase.AFTER) {
			return;
		}
		component.region.y = pinner.region.getBottom();
	}
}