package falcun.net.api.guidragon.effects;

import falcun.net.api.modules.hud.FalcunHudModule;
import falcun.net.api.guidragon.components.Component;
import falcun.net.api.guidragon.components.hud.HudComponent;
import falcun.net.api.guidragon.region.GuiRegion;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

import java.util.function.Supplier;

public class HudDrag extends Effect {

	private final Minecraft minecraft = Minecraft.getMinecraft();
	private static HudComponent lastClicked = null;
	int prevMouseX = 0;
	int prevMouseY = 0;
	boolean dragging = false;
	GuiRegion constraints;
	public Supplier<Boolean> doDrag = () -> true;
	FalcunHudModule HudComponent;

	public HudDrag(FalcunHudModule HudComponent) {
		this.HudComponent = HudComponent;
	}

	public static void setLastClicked(HudComponent lastClicked) {
		HudDrag.lastClicked = lastClicked;
	}

	@Override
	public void draw(int mouseX, int mouseY, Component component, Phase phase) {
		if (phase == Phase.AFTER || !doDrag.get() || component != lastClicked) return;

//		ScaledResolution scaledResolution = new ScaledResolution(minecraft);
//		FalcunScaling scaling = new FalcunScaling(Falcun.minecraft);

		GuiRegion region = component.region;
		float scale = HudComponent.scale.getValue() / 200.0F;

//		region.width = (int) (region.width * scale);
//		region.height = (int) (region.height * scale);

//		if (region.getRight() > scaledResolution.getScaledWidth())
//			region.x = scaledResolution.getScaledWidth() - region.width - 1;
//		if (region.getBottom() > scaledResolution.getScaledHeight())
//			region.y = scaledResolution.getScaledHeight() - region.height - 1;


		if (dragging) {
			int xDelta = mouseX - prevMouseX;
			int yDelta = mouseY - prevMouseY;
			region.x += xDelta;
			region.y += yDelta;
//			int right  = region.x + (int)(region.width * scale);
//			int bottom = region.x + (int)(region.height * scale);

//			if (region.getRight() > scaledResolution.getScaledWidth())
//				region.x -= xDelta;
//			if (region.x < 0)
//				region.x = 0;
//
//			if (region.getBottom() > scaledResolution.getScaledHeight())
//				region.y -= yDelta;
//			if (region.y < 0)
//				region.y = 0;

//			if (right > Falcun.minecraft.displayWidth) {
//				region.x -= xDelta;
//			}
//			if (region.x < 0) {
//				region.x = 0;
//			}
//			if (bottom > Falcun.minecraft.displayHeight){
//				region.y -= yDelta;
//			}
//			if (region.y < 0) {
//				region.y = 0;
//			}

			prevMouseX = mouseX;
			prevMouseY = mouseY;
		}

		if (constraints != null) {
			if (region.x < constraints.x) {
				region.x = constraints.x;
			}
			if (region.y < constraints.y) {
				region.y = constraints.y;
			}
			if (region.getRight() > constraints.getRight()) {
				region.x = constraints.getRight() - region.width;
			}
			if (region.getBottom() > constraints.getBottom()) {
				region.y = constraints.getBottom() - region.height;
			}
		}
	}

	@Override
	public void anyClick(int mX, int mY, int mouseButton, Component component, Phase phase) {
		if (phase == Phase.AFTER) return;

		GuiRegion dup = component.region.duplicate();
		float scale = HudComponent.scale.getValue() / 200.0F;
		dup.width = (int) (dup.width * scale);
		dup.height = (int) (dup.height * scale);

		if (Component.isOver(dup, mX, mY) && Mouse.getEventButtonState()) {
			if (lastClicked != null && Component.isOver(lastClicked.region.duplicate(), mX, mY) &&
				!component.equals(lastClicked)) return;
			dragging = !dragging;
			prevMouseX = mX;
			prevMouseY = mY;
			lastClicked = (HudComponent) component;

			return;
		}
		if (!dragging) return;

		dragging = false;
		lastClicked = (HudComponent) component;
		HudComponent.save();
	}
}
