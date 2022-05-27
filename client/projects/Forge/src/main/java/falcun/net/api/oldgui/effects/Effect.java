package falcun.net.api.oldgui.effects;


import falcun.net.api.oldgui.components.Component;

public abstract class Effect {

	public void draw(int mX, int mY, Component component, Phase phase) {
		if (component.isOver(mX, mY)) {
			onHover(mX, mY, component, phase);
		}
	}

	public void onClick(int mX, int mY, int mouseButton, Component component, Phase phase) {

	}

	public void onHover(int mX, int mY, Component component, Phase phase) {

	}

	public void anyClick(int mX, int mY, int mouseButton, Component component, Phase phase) {
		if (component.isOver(mX, mY)) {
			onClick(mX, mY, mouseButton, component, phase);
		}
	}

	public enum Phase {
		BEFORE, AFTER
	}

}
