package mapwriter.gui;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;

public class ScrollableColorBox extends ScrollableField {
	public int textFieldX;
	public int textFieldY;
	public int textFieldWidth = 12;
	public int textFieldHeight = 12;

	public boolean isFocused;
	public int color;
	public String hex;

	ScrollableColorBox(int x, int y, String hex, FontRenderer fontrendererObj) {
		super(x, y, 12, "", fontrendererObj);
		this.hex = hex;
		this.color = (int) Long.parseLong("FF" + hex, 16);
		this.init();
	}

	private void init() {
		this.textFieldX = this.x + ScrollableField.arrowsWidth + 3;
		this.textFieldY = this.y;
	}

	@Override
	public void draw() {
		super.draw();

		if (isFocused()) {
			Gui.drawRect(this.textFieldX - 1, this.textFieldY - 1,
					this.textFieldX + this.textFieldWidth + 1, this.textFieldY
							+ this.textFieldHeight + 1, 0xFFFFFF00);
		}

		Gui.drawRect(this.textFieldX, this.textFieldY, this.textFieldX
				+ this.textFieldWidth, this.textFieldY + this.textFieldHeight,
				this.color);
	}
	
	public boolean isMouseWithin(int mx, int my) {
		return this.textFieldX < mx && this.textFieldY < my && this.textFieldX + this.textFieldWidth > mx && this.textFieldY + this.textFieldHeight > my;
	}
	
	@Override
	public void mouseClicked(int x, int y, int button) {
		super.mouseClicked(x, y, button);
	}

	public void mouseDWheelScrolled(int x, int y, int direction) {

	}

	public boolean posWithinTextField(int x, int y) {
		return (x >= this.textFieldX) && (y >= this.textFieldY)
				&& (x <= (this.textFieldWidth + this.textFieldX))
				&& (y <= (this.textFieldHeight + this.textFieldY));
	}

	@Override
	public void nextElement() {
	}

	@Override
	public void previousElement() {
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	@Override
	public void setFocused(Boolean focus) {
		this.isFocused = focus;
	}

	@Override
	public Boolean isFocused() {
		return this.isFocused;
	}
}