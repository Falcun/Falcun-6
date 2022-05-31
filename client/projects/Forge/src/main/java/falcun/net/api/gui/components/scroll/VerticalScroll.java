package falcun.net.api.gui.components.scroll;

import falcun.net.api.gui.GuiUtils;
import falcun.net.api.gui.components.Component;
import falcun.net.api.gui.components.rect.ColorSquare;
import falcun.net.api.gui.effects.Effect;
import falcun.net.api.gui.effects.PinEffect;
import falcun.net.api.gui.effects.ScissorEffect;
import falcun.net.api.gui.region.GuiRegion;
import org.lwjgl.input.Mouse;

import java.util.function.Supplier;

public class VerticalScroll extends Component {

	Supplier<Integer> backColor, scrollColor;
	public boolean canClick = true;
	public double scrolledAmount;
	public Component controller;
	public GuiRegion boxRegion;
	// for scissor effect
	ColorSquare placeHolder;
	PinEffect pin;

	public VerticalScroll(GuiRegion region, Supplier<Integer> backColor, Supplier<Integer> scrollColor, GuiRegion scrollRegion) {
		super(region);
		boxRegion = scrollRegion;
		placeHolder = new ColorSquare(scrollRegion, () -> 0x00FFFFFF);
		pin = new PinEffect(this, placeHolder);
		placeHolder.effects.add(pin);
		subComponents.add(placeHolder);
		placeHolder.effects.add(new ScissorEffect(scrollRegion));
		this.backColor = backColor;
		this.scrollColor = scrollColor;
		controller = new ColorSquare(scrollRegion.offSet(0, 0), () -> 0x00FFFFFF);
		placeHolder.subComponents.add(controller);
	}

	int spaceNeeded;


	boolean isDragging = false;

	@Override
	public void onClicked(int mX, int mY, int mouseButton) {
		if (isOver(mX, mY) && Mouse.getEventButtonState() && canClick) {
			isDragging = !isDragging;
		} else {
			isDragging = false;
		}

		if (!isOver(this.boxRegion, mX, mY)) return;
		this.controller.subComponents.forEach(it -> {
			it.effects.forEach(effect -> effect.anyClick(mX, mY, mouseButton, it, Effect.Phase.BEFORE));
			it.onClicked(mX, mY, mouseButton);
			it.effects.forEach(effect -> effect.anyClick(mX, mY, mouseButton, it, Effect.Phase.AFTER));
		});
	}

	@Override
	public void onMouseClick(int mX, int mY, int mouseButton) {
		super.onMouseClick(mX, mY, mouseButton);
		if (!isOver(this.boxRegion, mX, mY)) return;
		this.controller.subComponents.forEach(it -> {
			it.onMouseClick(mX, mY, mouseButton);
		});
	}

	public void addComponent(Component component) {
		component.ispartofscroll = true;
		controller.subComponents.add(component);
		PinEffect pin = new PinEffect(controller, component);
		component.effects.add(pin);
		spaceNeeded = computeSpaceNeeded();
	}

	public void clear() {
		scrolledAmount = 0;
		controller.subComponents.clear();
		controller.region.y = boxRegion.y;
	}

	public void removeComp(Component component) {
		scrolledAmount = 0;
		controller.subComponents.remove(component);

		spaceNeeded = computeSpaceNeeded();
	}

	public int computeSpaceNeeded() {
		int lowest = -1;
		int highest = 0;
		for (Component child : controller.subComponents) {
			if (child.region.y < lowest || lowest == -1) {
				lowest = region.y;
			}
			if (child.region.getBottom() > highest) {
				highest = child.region.getBottom();
			}
		}
		return highest - lowest;
	}


	Direction lastDir = Direction.DOWN;
	private int scrollRemain = 0;

	private void addScroll(int amount) {
		if (scrollRemain <= 0) {
			++scrollRemain;
		}
		scrollRemain += amount;
	}

	private void getScrollRemaining() {
		scrollRemain = Math.max(--scrollRemain, 0);
	}

	private void smoothScroll(Direction dir) {
		if (dir != lastDir) {
			scrollRemain = 0;
			lastDir = dir;
			return;
		}
		addScroll(10);
	}

	@Override
	public void draw(int mX, int mY) {
		if (scrollRemain != 0) {
			getScrollRemaining();
			if (lastDir == Direction.DOWN) {
				scrolledAmount += (0.013);
			} else {
				scrolledAmount -= (0.013);
			}
			scrolledAmount = Math.min(scrolledAmount, 1);
			scrolledAmount = Math.max(scrolledAmount, 0);
		}

		double ratio = region.height / (double) spaceNeeded;
		if (ratio > 1) {
			ratio = 1;
		}
		int scrollHeight = (int) (ratio * region.height);
		int moveHeight = region.height - scrollHeight;
		int yOffset = (int) (moveHeight * scrolledAmount);
		int scrollTop = region.y + yOffset;
		GuiRegion scrollRegion = new GuiRegion(region.x, scrollTop, region.width, scrollHeight);
		//	if(ratio != 1){
		GuiUtils.drawShape(scrollRegion, scrollColor.get(), 0, true, 0);
		GuiUtils.drawShape(region, backColor.get(), 0, false, 2F);
		//}
		pin.draw(mX, mY, placeHolder, Effect.Phase.BEFORE);
		controller.region.x = boxRegion.x;
		int moveArea = spaceNeeded - region.height;
		int moveAmount = (int) (moveArea * scrolledAmount);
		if (spaceNeeded < region.height) {
			moveAmount = 0;
		}
		controller.region.y = boxRegion.y - moveAmount;
		if (isDragging) {
			if (mY < region.y + (scrollRegion.height / 2)) {
				scrolledAmount = 0;
			} else if (mY > (region.getBottom() - (scrollRegion.height / 2d))) {
				scrolledAmount = 1;
			} else {
				scrolledAmount = (mY - (region.y + scrollRegion.height / 2d)) / (double) (region.height - scrollRegion.height);
			}
		}
	}


	@Override
	public void onScroll(int mX, int mY, Direction direction) {
		if (!isOver(boxRegion, mX, mY)) return;
//		if (true) {
		smoothScroll(direction);
//			return;
//		}
//		if (direction == Direction.DOWN) {
//			scrolledAmount += 0.08;
//		} else {
//			scrolledAmount += -0.08;
//		}
//		if (scrolledAmount < 0) {
//			scrolledAmount = 0;
//		} else if (scrolledAmount > 1) {
//			scrolledAmount = 1;
//		}
	}

}
