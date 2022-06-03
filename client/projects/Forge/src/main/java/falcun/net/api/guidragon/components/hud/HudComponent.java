package falcun.net.api.guidragon.components.hud;

import falcun.net.api.modules.hud.FalcunHudModule;
import falcun.net.api.guidragon.GuiUtils;
import falcun.net.api.guidragon.components.Component;
import falcun.net.api.guidragon.effects.Effect;
import falcun.net.api.guidragon.effects.HudDrag;
import falcun.net.api.guidragon.region.GuiRegion;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.util.function.Supplier;

public class HudComponent extends Component {

	public FalcunHudModule hudElement;

	public HudComponent(FalcunHudModule hudElement) {
		super(new GuiRegion((int) (double) hudElement.screenPosition.getValue().first, (int) (double) hudElement.screenPosition.getValue().second, hudElement.width.getValue(), hudElement.height.getValue()));
		this.hudElement = hudElement;
	}

	@Override
	public void draw(int mX, int mY) {

		hudElement.renderPreview();
	}

	@Override
	public void onClicked(int mX, int mY, int mouseButton) {
		float scale = hudElement.scale.getValue() / 200.0F;
		int scaledWidth = (int) (hudElement.width.getValue() * scale);
		int scaledHeight = (int) (hudElement.height.getValue() * scale);
		GuiRegion gr = new GuiRegion(region.x, region.y, scaledWidth, scaledHeight);
		if (isOver(gr, mX, mY)) {
			if (mouseButton == 0) {
				HudDrag.setLastClicked(this);
			} else if (mouseButton == 1) {
				hudElement.toggle();
			}
		}
	}

	public HudComponent.ScalingEffect createScaling(Supplier<Integer> color, HudComponent component) {
		return new HudComponent.ScalingEffect(color, component);
	}

	public ScalingEffect createScaling(int color, HudComponent component) {
		return createScaling(() -> color, component);
	}

	public class ScalingEffect extends Effect { // DO NOT MAKE THIS STATIC
		int mXStart = -1;
		Supplier<Integer> color;
		public boolean selected = false;
		HudComponent component;
		FalcunHudModule hudElement;

		private ScalingEffect(Supplier<Integer> color, HudComponent component) {
			this.color = color;
			this.component = component;
			this.hudElement = component.hudElement;
			int i = HudComponent.this.hudElement.width.getValue();
		}

		@Override
		public void draw(int mX, int mY, Component component, Phase phase) {
			float scale = (float) (hudElement.scale.getValue()) / 200.0F;
			GuiRegion bottomRight = new GuiRegion(component.region.getRight() - 5, component.region.getBottom() - 5, 5, 5);
			if (phase == Phase.BEFORE) {
				GlStateManager.pushMatrix();
				GlStateManager.translate((scale - 1.0F) * -hudElement.screenPosition.getValue().first, (scale - 1.0F) * -hudElement.screenPosition.getValue().second, 0.0F);
				GlStateManager.scale(scale, scale, 1.0F);
				GuiUtils.drawShape(bottomRight, color.get(), 0, true, 0);

			} else {
				GuiUtils.drawShape(bottomRight, color.get(), 0, true, 0);

				GlStateManager.scale(1.0F / scale, 1.0F / scale, 1.0F);
				GlStateManager.translate((scale - 1.0F) * hudElement.screenPosition.getValue().first, (scale - 1.0F) * hudElement.screenPosition.getValue().second, 0.0F);
				GlStateManager.popMatrix();
			}
			if (selected) {
				int xDif = (mX + mXStart) - (int) (double) hudElement.screenPosition.getValue().first;
				float percent = xDif / (float) hudElement.width.getValue();

				//THIS CHECK MAKES IT SO U CAN'T BREAK THIS COMPONENT.
				if (percent < 0.8 || (xDif > 0 && percent > 2.5)) return;


				if (hudElement.scale.getValue() > 120) {
					hudElement.scale.setValue((int) (percent * 200));
				} else {
					hudElement.scale.setValue(120);
				}
			}
		}

		@Override
		public void anyClick(int mX, int mY, int mouseButton, Component component, Phase phase) {
			if (phase == Phase.BEFORE) {
				float scale = hudElement.scale.getValue() / 200.0F;
				int scaledWidth = (int) (hudElement.width.getValue() * scale);
				int scaledHeight = (int) (hudElement.height.getValue() * scale);
				int scaledSegW = (int) (8 * scale);
				GuiRegion bottomRight = new GuiRegion((int) (double) hudElement.screenPosition.getValue().first + scaledWidth - scaledSegW, (int) (double) hudElement.screenPosition.getValue().second + scaledHeight - scaledSegW, scaledSegW, scaledSegW);
				if (Component.isOver(bottomRight, mX, mY)) {
					mXStart = bottomRight.getRight() - mX + 1;
					selected = Mouse.getEventButtonState();
				}
				if (!Mouse.getEventButtonState() && selected) {
					selected = false;
				}
			}
		}
	}

}
