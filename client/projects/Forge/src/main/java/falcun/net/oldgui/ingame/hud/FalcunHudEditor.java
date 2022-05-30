package falcun.net.oldgui.ingame.hud;

import falcun.net.Falcun;
import falcun.net.api.fonts.Fonts;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.hud.FalcunHudModule;
import falcun.net.api.oldgui.GuiUtils;
import falcun.net.api.oldgui.components.Component;
import falcun.net.api.oldgui.components.hud.HudComponent;
import falcun.net.api.oldgui.components.rect.ColorSquareOutline;
import falcun.net.api.oldgui.effects.Effect;
import falcun.net.api.oldgui.effects.HudDrag;
import falcun.net.api.oldgui.effects.OnClickEffect;
import falcun.net.api.oldgui.menu.FalcunMenu;
import falcun.net.api.oldgui.region.GuiRegion;
import falcun.net.api.oldgui.scaling.FalcunScaling;
import falcun.net.managers.FalcunConfigManager;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.GuiUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public final class FalcunHudEditor extends FalcunMenu {

	private final static int HELPER = new Color(200, 200, 200, 150).getRGB();

	private final static int BACKGROUND = new Color(200, 200, 200, 100).getRGB();
	private final static int BORDER = new Color(225, 225, 225, 200).getRGB();
	private final static int RESIZE = new Color(50, 50, 50, 200).getRGB();
	private final static int TEXT_COLOR = new Color(255, 255, 255, 255).getRGB();

	private final static int BACKGROUND_HIDDEN = new Color(200, 0, 0, 50).getRGB();
	private final static int BORDER_HIDDEN = new Color(225, 0, 0, 100).getRGB();

	private final static int HOVER_TEXT = new Color(200, 200, 200, 255).getRGB();
	private final static int HOVER_BACKGROUND = new Color(75, 75, 75, 255).getRGB();
	private final static int HOVER_BORDER = new Color(0, 0, 0, 255).getRGB();


	private final static int SCALE = 2;
	private final static int HELPER_THICKNESS = 2;
	private final static int RESIZE_SIZE = 10;
	private final static int SETTINGS_SIZE = 16;
	private final static int SNAP_SENS = 3;

	private HudComponent selected;
	private boolean resizing;
	private boolean moving;
	private boolean mouseDown;
	private boolean mouseDownCache;
	private boolean rightMouseDown;
	private boolean mouseDownRightCache;
	private int cachedX;
	private int cachedY;
	private double originalScale;

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(false);
		super.initGui();
	}

	private final List<HudComponent> components = new LinkedList<>();

	@Override
	protected void init() {
		components.clear();
		for (FalcunModule mod : FalcunConfigManager.modules.values()) {
			if (!(mod instanceof FalcunHudModule)) continue;
			FalcunHudModule falcunModule = (FalcunHudModule) mod;
			HudComponent hudComponent = new HudComponent(falcunModule);
			hudComponent.effects.add(new Effect() {
				@Override
				public void draw(int mX, int mY, Component component, Phase phase) {
					if (phase != Phase.BEFORE) return;
					falcunModule.screenPosition.getValue().setFirst((double) component.region.x).setSecond((double) component.region.y);
					hudComponent.region.width = falcunModule.width.getValue();
					hudComponent.region.height = falcunModule.height.getValue();
//					Gui.drawRect(hudComponent.region.x, hudComponent.region.y, hudComponent.region.getRight(), hudComponent.region.getBottom(), 0x22ffffff);
				}
			});
			HudComponent.ScalingEffect scalingEffect = hudComponent.createScaling(0xffffffff, hudComponent);
			// TODO: ADD DRAGGING HERE
			HudDrag drag = new HudDrag(falcunModule);
			drag.doDrag = () -> !scalingEffect.selected;

			hudComponent.effects.add(drag);
			hudComponent.effects.add(scalingEffect);
			components.add(hudComponent);
		}
	}

	private static void drawRectFalcun(int x, int y, int width, int height, int color) {
		Gui.drawRect(x, y, x + width, y + height, color);
	}


	@Override
	public void drawScreen(int mX, int mY, float partialTicks) {
		GlStateManager.pushMatrix();
		final float scale = 1f / new FalcunScaling(mc).getScaleFactor();
		final int newWidth = Math.round((float) width / scale);
		final int newHeight = Math.round((float) height / scale);
		GlStateManager.scale(scale, scale, scale);

		GuiUtils.drawShape(new GuiRegion(0, 0, newWidth, newHeight), 0x44101010, 0, true, 0);
		GuiUtil.doBlur(1);
		drawRectFalcun((newWidth >> 1) - (HELPER_THICKNESS >> 1), 0, HELPER_THICKNESS, newHeight, HELPER);
		drawRectFalcun(0, (newHeight >> 1) - (HELPER_THICKNESS >> 1), newWidth, HELPER_THICKNESS, HELPER);

		String line1 = "Left click to control components.".toUpperCase();
		String line2 = "Right click a component to toggle visibility.".toUpperCase();
		int line1Width = (int) Fonts.Roboto.getStringWidth(line1);
		int line2Width = (int) Fonts.Roboto.getStringWidth(line2);
		int line1Height = (int) Fonts.Roboto.stringHeight(line1);
		int line2Height = (int) Fonts.Roboto.stringHeight(line2);

		int boxWidth = Math.max(line1Width, line2Width) + 20;
		int boxHeight = line1Height + line2Height + 4;

		int heightOffset = 10;

		int boxX = (newWidth >> 1) - (boxWidth >> 1);
		int boxY = heightOffset;

		drawRectFalcun(boxX, boxY, boxWidth + 1, 1, HOVER_BORDER);
		drawRectFalcun(boxX, boxY + 1, boxHeight - 1, 1, HOVER_BORDER);
		drawRectFalcun(boxX, boxY + boxHeight, boxWidth + 1, 1, HOVER_BORDER);
		drawRectFalcun(boxX + boxWidth, boxY + 1, boxHeight - 1, 1, HOVER_BORDER);

		drawRectFalcun(boxX + 1, boxY + 1, boxWidth - 1, boxHeight - 1, HOVER_BACKGROUND);
		int mouseX = Math.round((float) mX / scale);
		int mouseY = Math.round((float) mY / scale);

		Fonts.Roboto.drawString(line1, boxX + (boxWidth >> 1) - (line1Width >> 1), boxY + 2, HOVER_TEXT, false);
		Fonts.Roboto.drawString(line2, boxX + (boxWidth >> 1) - (line2Width >> 1), boxY + line1Height + 2, HOVER_TEXT, false);

		final List<FalcunHudModule> hudModules = new LinkedList<>();
		for (final FalcunModule value : FalcunConfigManager.modules.values()) {
			if (value instanceof FalcunHudModule) {
				hudModules.add((FalcunHudModule) value);
			}
		}

//		for (HudComponent component : this.components) {
//			super.drawComponent(mouseX, mouseY, component);
//		}


		GlStateManager.popMatrix();
		super.drawScreen(mX, mY, partialTicks);

	}


	@Override
	public void onGuiClosed() {
		Falcun.saveConfig();
	}

	private static final List<Component> emptyList = new LinkedList<>();

	@Override
	public List<Component> getComponents() {
		return new LinkedList<>(components);
	}

}
