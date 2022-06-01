package mapwriter.gui;

import mapwriter.util.ColorCodes;
import mapwriter.util.Utils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;

public class ScrollableSingularColorSelector extends ScrollableField {
	private ScrollableColorBox[] defaultColors = new ScrollableColorBox[16];
	private ScrollableColorSelector scrollable;
	
	private int focused = -1;
	
	private int colour = 0;
	
	private int colourFieldX = 0;
	private int colourFieldY = 0;
	private int colourFieldW = 0;
	private int colourFieldH = 0;
	
	private int y;
	
	public ScrollableSingularColorSelector(int x, int y, int width, String label, FontRenderer fontrendererObj, ScrollableColorSelector scrollable) {
		this(x, y, width, label, fontrendererObj);
		this.scrollable = scrollable;
	}
	
	public ScrollableSingularColorSelector(int x, int y, int width, String label, FontRenderer fontrendererObj) {
		super(x, y + 1, width, label, fontrendererObj);
		this.y = y;
		this.init();
	}
	
	private void init() {
		int textboxWidth = 16;
		int w = (ScrollableField.arrowsWidth * 2);

		String[] colors = ColorCodes.getColors().toArray(new String[ColorCodes.getColors().size() - 1]);
		for (int i = 0; i < ColorCodes.getColors().size(); i++) {
			this.defaultColors[i] = new ScrollableColorBox(this.x + (14 * i), this.y, colors[i], this.fontrendererObj);
		}

		this.colourFieldX = this.x + w + 2;
		this.colourFieldY = this.y + 6;
		this.colourFieldW = this.width - w - (ScrollableField.arrowsWidth * 2) - 8;
		this.colourFieldH = MwGuiMarkerDialogNew.elementVSpacing;
	}

	@Override
	public void nextElement() {
		//this.setColor(Utils.getNextColour());
	}

	@Override
	public void previousElement() {
		//this.setColor(Utils.getPrevColour());
	}

	@Override
	public void setFocused(Boolean focus) {
		this.defaultColors[this.focused = 0].setFocused(focus);
	}

	@Override
	public Boolean isFocused() {
		if (this.focused > -1) return true;
		return false;
	}

	public int getColor() {
		return this.colour;
	}

	public void setColor(int colour) {
		this.colour = colour;
	}

	@Override
	public void mouseClicked(int x, int y, int button) {
		super.mouseClicked(x, y, button);
		
		if(!this.posWithinColourField(x, y)) return;
		
		for (int i = 0; i < defaultColors.length; i++) {
			if (this.defaultColors[i].isMouseWithin(x, y)) {
				this.defaultColors[i].mouseClicked(x, y, button);
				this.focused = i;
				break;
			}
		}
		
		if(this.scrollable != null && this.focused != -1) {
			this.scrollable.setColor(this.defaultColors[this.focused].color);
		}
	}

	public void mouseDWheelScrolled(int x, int y, int direction) {
		for (int i = 0; i < defaultColors.length; i++) {
			this.defaultColors[i].mouseDWheelScrolled(x, y, direction);
		}

		if (this.posWithinColourField(x, y)) {
			this.colourFieldScroll(-direction);
		}
	}

	public boolean posWithinColourField(int x, int y) {
		return (x >= this.x) && (y >= this.y)
				&& (x <= (this.width + this.x))
				&& (y <= (this.colourFieldH + this.y));
	}

	public void colourFieldScroll(int direction) {
		if (direction > 0) {
			this.nextElement();
		} else if (direction < 0) {
			this.previousElement();
		}
	}

	@Override
	public void draw() {
		super.draw();
		
		for (int i = 0; i < defaultColors.length; i++) {
			this.defaultColors[i].draw();
		}
	}

	public ScrollableField thisField() {
		return this.defaultColors[this.focused];
	}

	// TODO: Fix problem between checking next and previous field.
	public ScrollableField nextField(ScrollableField field) {
		this.focused += 1;
		if (this.focused < 16) return this.defaultColors[this.focused];
		this.focused = -1;
		return field;
	}

	public ScrollableField prevField(ScrollableField field) {
		// TODO: Shift + Tab will end up causing an error
		// System.out.println("PREV: " + this.focused);
		// this.focused -= 1;
		// if(this.focused >= 0) return this.defaultColors[this.focused];
		// this.focused = -1;
		return field;
	}
}