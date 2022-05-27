package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.ElementListener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.Listener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.MouseListener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util.AnimationTimer;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util.Scissor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class VerticalScroll extends ContainerElement {

	// actual scroll
	private ContainerElement scroll;
	// moving part
	private ContainerElement innerScroll;

	public int yOffset;
	private boolean dragging = false;
	private int startY = 0;
	private int clickY;
	private int yOffsetStart;
	private AnimationTimer timer;
	private int scrollBoxPaddingTop;

	public VerticalScroll() {
		this.addListener(new Scissor())
			.addListener((ElementListener) (element, phase) -> {
				if (phase == Listener.Phase.PRE) {
					int totalHeight = this.computeTotalHeight();
					int excess = totalHeight - this.height;
					if (excess < 0) {
						excess = 0;
					}
					int yO;
					if (this.timer != null) {
						yO = this.yOffsetStart + this.timer.getPosition();
						if (this.timer.isDone()) {
							this.yOffset += this.timer.end;
							this.timer = null;
						}
					} else {
						yO = this.yOffset;
					}
					float scrolledAmount = yO / (float) (this.height - this.computeInnerScrollHeight());
					if (scrolledAmount < 0) {
						scrolledAmount = 0;
					} else if (scrolledAmount > 1) {
						scrolledAmount = 1;
					}
					element.paddingTop = -(int) (scrolledAmount * excess) + this.scrollBoxPaddingTop;
				}
			})
			.addListener((MouseListener) element -> {
				int scrolling = Mouse.getEventDWheel();
				if (scrolling != 0 && this.isMouseOver()) {
					if (this.timer != null) {
						this.yOffset = this.yOffsetStart + this.timer.end;
					}
					this.yOffsetStart = this.yOffset;
					if (scrolling > 0) {
						this.timer = new AnimationTimer(0, -32, 50);
					} else {
						this.timer = new AnimationTimer(0, 32, 50);
					}
					this.clampYOffset();
				}
				return false;
			});
		this.scroll = new ContainerElement()
			.addListener((ElementListener) (element, phase) -> {
				if (phase == Listener.Phase.POST) {
					this.innerScroll.setHeightPx(this.computeInnerScrollHeight());
				}
			})
			.addListener((MouseListener) element -> {
				int mouseY = Display.getHeight() - Mouse.getEventY();
				if (Mouse.getEventButtonState()) {
					if (this.scroll.isMouseOver() && !this.dragging) {
						this.dragging = true;
						this.startY = mouseY;
						this.clickY = this.yOffset;
					}
				} else if (Mouse.getEventButton() != -1) {
					this.dragging = false;
				}
				if (this.dragging) {
					this.yOffset = this.clickY + mouseY - this.startY;
					this.clampYOffset();
				}
				return false;
			});

		this.innerScroll = new ContainerElement()
			.setColor(0xFF0F9D58)
			.addListener((ElementListener) (element, phase) -> {
				if (phase == Listener.Phase.PRE) {
					element.parentElement.paddingTop = this.yOffset;
				}
			});
		this.scroll.addChildren(this.innerScroll);
	}

	public int computeInnerScrollHeight() {
		int totalHeight = this.computeTotalHeight();
		float scrollPercent = this.height / (float) totalHeight;
		if (scrollPercent > 1) {
			scrollPercent = 1;
		}
		return (int) (scrollPercent * this.height);
	}

	public int computeTotalHeight() {
		int top = Integer.MAX_VALUE;
		int bottom = Integer.MIN_VALUE;
		for (BasicElement<?> child : this.children) {
			if (child.y < top) {
				top = child.y;
			}
			if (child.y + child.height > bottom) {
				bottom = child.y + child.height;
			}
		}
		return bottom - top + this.paddingBottom;
	}

	public void addScrollElement(BasicElement<?>... children) {
		this.addChildren(children);
	}

	public void clampYOffset() {
		if (this.yOffset < 0) {
			this.yOffset = 0;
		} else if (this.yOffset + this.computeInnerScrollHeight() > this.height) {
			this.yOffset = this.height - this.computeInnerScrollHeight();
		}
	}

	public ContainerElement setPaddingTB(int paddingTop, int paddingBottom) {
		VerticalScroll.this.scrollBoxPaddingTop = paddingTop;
		return super.setPaddingTB(paddingTop, paddingBottom);
	}

	// if you want the box to have a scroll bar use this, which will work with the mouse wheel
	public ContainerElement getScroll() {
		return this.scroll;
	}

	// this is the bar inside the scroll bar
	public ContainerElement getInnerScroll() {
		return this.innerScroll;
	}
}