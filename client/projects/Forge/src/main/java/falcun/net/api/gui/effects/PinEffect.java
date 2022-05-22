package falcun.net.api.gui.effects;

import falcun.net.api.gui.components.Component;

public class PinEffect extends Effect {

	Component pinner, pinee;
	int xOffset;
	int yOffset;
	//add this to pinee
	public PinEffect(Component pinner, Component pinee) {
		this.pinner = pinner;
		this.pinee = pinee;
		xOffset = pinner.region.x - pinee.region.x;
		yOffset = pinner.region.y - pinee.region.y;
	}

	@Override
	public void draw(int mX, int mY, Component component, Phase phase) {
		if(phase == Phase.BEFORE) {
			pinee.region.x = pinner.region.x - xOffset;
			pinee.region.y = pinner.region.y - yOffset;
		}
	}


}
