package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.DimensionComputer;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.DimensionsCalculator;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.Layout;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.Style;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.Listener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util.Colors;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class BasicElement<T extends BasicElement<?>> {

	public int x, y, width, height;
	public List<BasicElement<?>> children = new ArrayList<>();
	public List<Listener<T>> listeners = new ArrayList<>();
	public BasicElement<?> parentElement;
	public DimensionComputer dimensionsCalculator = new DimensionsCalculator();
	public Layout layout = Layout.BLOCK;
	public boolean visible = true;
	public float widthPercent = 1;
	public float heightPercent = 1;
	public int widthPixel = -1;
	public int heightPixel = -1;
	public int paddingLeft, paddingRight, paddingTop, paddingBottom;
	public float paddingLeftPercent, paddingRightPercent;
	public int color;
	public OutlineElement<?> outlineElement;

	public Function<BasicElement<?>, Boolean> shouldRender;

	public void render() {

	}

	public T setDimensions(int x, int y, int width, int height) {
		if (this.x == 0) {
			this.x = x;
		}
		if (this.y == 0) {
			this.y = y;
		}
		if (this.width == 0) {
			this.width = width;
		}
		if (this.height == 0) {
			this.height = height;
		}
		return (T) this;
	}

	public T addChildren(BasicElement<?>... children) {
		List<BasicElement<?>> childrenList = Arrays.asList(children);
		childrenList.forEach(child -> child.parentElement = this);
		this.children.addAll(childrenList);
		return (T) this;
	}

	public T setWidthPX(int width) {
		this.widthPixel = width;
		return (T) this;
	}

	public T setHeightPx(int height) {
		this.heightPixel = height;
		return (T) this;
	}

	public T setVisible(boolean visible) {
		this.visible = visible;
		return (T) this;
	}

	public T setWidthPercent(float widthPercent) {
		this.widthPercent = widthPercent;
		return (T) this;
	}

	public T setHeightPercent(float heightPercent) {
		this.heightPercent = heightPercent;
		return (T) this;
	}

	public T setLayout(Layout layout) {
		this.layout = layout;
		return (T) this;
	}

	public T setPaddingLR(int paddingLeft, int paddingRight) {
		this.paddingLeft = paddingLeft;
		this.paddingRight = paddingRight;
		return (T) this;
	}

	public T setPaddingTB(int paddingTop, int paddingBottom) {
		this.paddingTop = paddingTop;
		this.paddingBottom = paddingBottom;
		return (T) this;
	}

	public T addListener(Listener listener) {
		this.listeners.add(listener);
		return (T) this;
	}

	public T setColor(int color) {
		this.color = color;
		return (T) this;
	}

	public T setColor(Colors color) {
		this.setColor(color.getIntColor());
		return (T) this;
	}

	public T operate(Consumer<T> operation) {
		operation.accept((T) this);
		return (T) this;
	}

	public T clearChildren() {
		this.children.clear();
		return (T) this;
	}

	public T setPaddingPcLR(float left, float right) {
		this.paddingLeftPercent = left;
		this.paddingRightPercent = right;
		return (T) this;
	}

	public ContainerElement wrapElement() {
		ContainerElement containerElement = new ContainerElement();
		containerElement.addChildren(this);
		return containerElement;
	}

	public boolean isMouseOver() {
		int mouseX = Mouse.getX();
		int mouseY = Display.getHeight() - Mouse.getEventY();
		return this.x < mouseX && this.x + this.width > mouseX && this.y < mouseY && this.y + this.height > mouseY;
	}

	public boolean shouldRender() {
		return this.width > 0 && this.height > 0 && this.visible && (this.shouldRender == null || this.shouldRender.apply(this));
	}

	public T setShouldRenderHook(Function<BasicElement<?>, Boolean> shouldRender) {
		this.shouldRender = shouldRender;
		return (T) this;
	}

	public T applyStyle(Style style) {
		style.applyToElement(this);
		return (T) this;
	}

	public T setOutline(OutlineElement<?> outlineElement) {
		this.outlineElement = outlineElement;
		if (this.outlineElement != null) {
			this.outlineElement.parentElement = this;
		}
		return (T) this;
	}

	public T setPadding(int all) {
		this.setPaddingLR(all, all);
		this.setPaddingTB(all, all);
		return (T) this;
	}

}