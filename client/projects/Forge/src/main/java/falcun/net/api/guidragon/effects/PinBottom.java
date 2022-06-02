package falcun.net.api.guidragon.effects;
import falcun.net.api.guidragon.components.*;

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