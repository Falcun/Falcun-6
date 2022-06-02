package falcun.net.api.guidragon.components.number;

import falcun.net.api.colors.FalcunGuiColorPalette;
import falcun.net.api.guidragon.GuiUtils;
import falcun.net.api.guidragon.components.Component;
import falcun.net.api.guidragon.region.GuiRegion;
import falcun.net.api.modules.config.FalcunValue;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.function.Supplier;

public class NumberSlider extends Component {

	int backColor;
	Supplier<Integer> slideColor;
	FalcunValue<Number> numberSave;
	double min, max;

	public NumberSlider(GuiRegion region, int backColor, int slideColor, FalcunValue<Number> numberSave, double min, double max) {
		this(region, backColor, () -> slideColor, numberSave, min, max);
	}

	public NumberSlider(GuiRegion region, int backColor, Supplier<Integer> slideColor, FalcunValue<Number> numberSave, double min, double max) {
//		this(region, backColor, slideColor, numberSave, min, max);
		super(region);
		this.backColor = backColor;
		this.slideColor = slideColor;
		this.numberSave = numberSave;
		this.min = min;
		this.max = max;
	}

	boolean isDragging = false;

	@Override
	public void draw(int mX, int mY) {
		boolean over = isOver(new GuiRegion(this.region.x - 10, this.region.y - 10, this.region.width + 10, this.region.height + 10), mX, mY);
		if (!Mouse.getEventButtonState() && isDragging && !over) {
			isDragging = false;
		}
		boolean round = !(numberSave.getValue() instanceof Float || numberSave.getValue() instanceof Double);
		int x = region.x;
		int wid = region.width;

		double diff = this.max - this.min;
		double percent = (getNumberVal(numberSave.getValue()) - this.min) / diff;
		double sliderDiff = region.getRight() - x;
		int sliderOffset = (int) (region.width * percent);
		if (getNumberVal(numberSave.getValue()) >= this.max) {
			sliderOffset = wid;
		} else if (getNumberVal(numberSave.getValue()) <= this.min) {
			sliderOffset = 0;
		}
		GuiRegion slider = region.offSet(0, 0);
		slider.width = sliderOffset;
		GuiUtils.drawShape(slider, over ? 0xFF17fc03 : slideColor.get(), 0, true, 0);
		GuiUtils.drawShape(region, FalcunGuiColorPalette.getLineColor(), 0, false, 1.2f);
		if (isDragging) {
			if (mX < x) {
				set(this.min);
			} else if (mX > region.getRight()) {
				set(this.max);
			} else {
				round = round || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
				double p = (mX - region.x) / sliderDiff;
				double setTo = this.min + diff * p;
				set(round ? Math.round(setTo) : setTo);
			}
		}
	}

	private double getNumberVal(Number num) {
		if (num instanceof Double) {
			return num.doubleValue();
		} else if (num instanceof Float) {
			return num.floatValue();
		} else if (num instanceof Integer) {
			return num.intValue();
		} else if (num instanceof Long) {
			return num.longValue();
		} else if (num instanceof Short) {
			return num.shortValue();
		} else {
			return num.byteValue();
		}
	}

	private void set(Number num) {
		if (numberSave.getValue() instanceof Double) {
			numberSave.setValue(num.doubleValue());
		} else if (numberSave.getValue() instanceof Float) {
			numberSave.setValue(num.floatValue());
		} else if (numberSave.getValue() instanceof Integer) {
			numberSave.setValue(num.intValue());
		} else if (numberSave.getValue() instanceof Long) {
			numberSave.setValue(num.longValue());
		} else if (numberSave.getValue() instanceof Short) {
			numberSave.setValue(num.shortValue());
		} else {
			numberSave.setValue(num.byteValue());
		}
	}


	@Override
	public void onMouseClick(int mX, int mY, int mouseButton) {
		super.onMouseClick(mX, mY, mouseButton);
		if (isDragging) {
			isDragging = Mouse.getEventButtonState();
		} else {
			isDragging = isOver(mX, mY) && Mouse.getEventButtonState();
		}
	}
}
