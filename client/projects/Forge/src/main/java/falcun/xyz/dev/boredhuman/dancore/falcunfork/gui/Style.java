package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Style {
	public Layout layout = null;
	public float widthPercent = -1;
	public float heightPercent = -1;
	public int widthPixel = -1;
	public int heightPixel = -1;
	public int paddingLeft = -1, paddingRight = -1, paddingTop = -1, paddingBottom = -1;
	public Integer color = null;
	public List<Consumer<BasicElement<?>>> processors = new ArrayList<>();

	public Style setLayout(Layout layout) {
		this.layout = layout;
		return this;
	}

	public Style setWidthPercent(float widthPercent) {
		this.widthPercent = widthPercent;
		return this;
	}

	public Style setHeightPercent(float heightPercent) {
		this.heightPercent = heightPercent;
		return this;
	}

	public Style setWidthPixel(int widthPixel) {
		this.widthPixel = widthPixel;
		return this;
	}

	public Style setHeightPixel(int heightPixel) {
		this.heightPixel = heightPixel;
		return this;
	}

	public Style setPaddingLeft(int paddingLeft) {
		this.paddingLeft = paddingLeft;
		return this;
	}

	public Style setPaddingRight(int paddingRight) {
		this.paddingRight = paddingRight;
		return this;
	}

	public Style setPaddingTop(int paddingTop) {
		this.paddingTop = paddingTop;
		return this;
	}

	public Style setPaddingBottom(int paddingBottom) {
		this.paddingBottom = paddingBottom;
		return this;
	}

	public Style setColor(Integer color) {
		this.color = color;
		return this;
	}

	public Style addProcessor(Consumer<BasicElement<?>> processor) {
		this.processors.add(processor);
		return this;
	}

	public void applyToElement(BasicElement<?> element) {
		if (this.layout != null) {
			element.layout = this.layout;
		}
		if (this.widthPercent != -1) {
			element.widthPercent = this.widthPercent;
		}
		if (this.heightPercent != -1) {
			element.heightPercent = this.heightPercent;
		}
		if (this.widthPixel != -1) {
			element.widthPixel = this.widthPixel;
		}
		if (this.heightPixel != -1) {
			element.heightPixel = this.heightPixel;
		}
		if (this.paddingLeft != -1) {
			element.paddingLeft = this.paddingLeft;
		}
		if (this.paddingRight != -1) {
			element.paddingRight = this.paddingRight;
		}
		if (this.paddingTop != -1) {
			element.paddingTop = this.paddingTop;
		}
		if (this.paddingBottom != -1) {
			element.paddingBottom = this.paddingBottom;
		}
		if (this.color != null) {
			element.color = this.color;
		}
		this.processors.forEach(processor -> processor.accept(element));
	}


}