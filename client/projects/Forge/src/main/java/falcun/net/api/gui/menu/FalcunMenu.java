package falcun.net.api.gui.menu;

import falcun.net.api.gui.components.Component;
import falcun.net.api.gui.components.scroll.VerticalScroll;
import falcun.net.api.gui.inheritance.ScalingGui;
import falcun.net.api.gui.scaling.FalcunScaling;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public abstract class FalcunMenu extends GuiScreen {
	private List<Component> components = new LinkedList<>();

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.components = getComponents();
		GlStateManager.pushMatrix();
		if (!(this instanceof ScalingGui)) {
			float value = 1f / new FalcunScaling(mc).getScaleFactor();
			int newMouseX = Math.round((float) mouseX / value);
			int newMouseY = Math.round((float) mouseY / value);
			GlStateManager.scale(value, value, value);
			components.forEach(comp -> comp.draw(newMouseX, newMouseY, partialTicks));
			components.forEach(it -> drawComponent(newMouseX, newMouseY, it));
		} else {
			components.forEach(comp -> comp.draw(mouseX, mouseY, partialTicks));
			components.forEach(it -> drawComponent(mouseX, mouseY, it));
		}
		GlStateManager.popMatrix();
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		components.forEach(it -> handleMouseClick(mouseX, mouseY, state, it));
	}

	public abstract List<Component> getComponents();

	private static int savedW = -1, savedH = -1;
	protected boolean isScaled = false;

	public void initGui() {
		if (savedH != height || savedW != width || true) {
			savedH = height;
			savedW = width;
			isScaled = true;
		}
		this.init();
	}

	protected abstract void init();


	public void drawComponent(int mX, int mY, Component component) {
		component.preDraw(mX, mY);
		component.subComponents.forEach(it -> drawComponent(mX, mY, it));
		component.afterDraw(mX, mY);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (!(this instanceof ScalingGui)) {
			float value = 1f / new FalcunScaling(mc).getScaleFactor();
			int newMouseX = Math.round((float) mouseX / value);
			int newMouseY = Math.round((float) mouseY / value);
			components.forEach(it -> handleMouseClick(newMouseX, newMouseY, mouseButton, it));
		} else {
			components.forEach(it -> handleMouseClick(mouseX, mouseY, mouseButton, it));
		}
	}


	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) super.keyTyped(typedChar, keyCode);
		assert components != null;
		components.forEach(it -> handleKey(typedChar, keyCode, it));
	}

	public void handleKey(char typedChar, int keyCode, Component component) {
		component.onKey(keyCode, typedChar);
		component.subComponents.forEach(it -> handleKey(typedChar, keyCode, it));
	}


	public void handleMouseClick(int mX, int mY, int mouseButton, Component component) {
		component.onMouseClick(mX, mY, mouseButton);
		if (!(component instanceof VerticalScroll))
			component.subComponents.forEach(it -> handleMouseClick(mX, mY, mouseButton, it));
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		int wheelDelta = Mouse.getEventDWheel();
		if (wheelDelta != 0) {
			Component.Direction direction = wheelDelta > 0 ? Component.Direction.UP : Component.Direction.DOWN;
			components.forEach(it -> handleScroll(direction, it));
		}
	}

	public void handleScroll(Component.Direction direction, Component component) {
		int mX = Mouse.getEventX() * this.width / this.mc.displayWidth;
		int mY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
		if (!(this instanceof ScalingGui)) {
			float value = 1f / new FalcunScaling(mc).getScaleFactor();
			int newMouseX = Math.round((float) mX / value);
			int newMouseY = Math.round((float) mY / value);
			component.onScroll(newMouseX, newMouseY, direction);
			component.subComponents.forEach(it -> handleScroll(direction, it));
		} else {
			component.onScroll(mX, mY, direction);
			component.subComponents.forEach(it -> handleScroll(direction, it));
		}
	}


}
