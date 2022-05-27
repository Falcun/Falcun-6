package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.macros;

import falcun.net.api.modules.config.FalcunValue;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.api.module.ModColor;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.DynamicElement;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.ElementListener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.Listener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.MouseListener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.ColorUtil;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.GuiUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class ColorPicker {
	public static BasicElement<?> makeHueSlider(ModColor modColor) {

		FalcunValue<Boolean> dragging = new FalcunValue<>(false);

		return new DynamicElement<>(element -> {
			GlStateManager.disableTexture2D();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(770, 771);
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
			GlStateManager.disableCull();
			Tessellator tess = Tessellator.getInstance();
			WorldRenderer wr = tess.getWorldRenderer();
			wr.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_COLOR);

			float increment = element.width / 6F;

			float bottom = element.y;
			float top = element.y + element.height;

			wr.pos(element.x, top, 0).color(1F, 0F, 0F, 1F).endVertex();
			wr.pos(element.x, bottom, 0).color(1F, 0F, 0F, 1F).endVertex();
			wr.pos(element.x + increment, top, 0).color(1F, 1F, 0F, 1F).endVertex();
			wr.pos(element.x + increment, bottom, 0).color(1F, 1F, 0F, 1F).endVertex();

			increment += element.width / 6F;

			wr.pos(element.x + increment, top, 0).color(0F, 1F, 0F, 1F).endVertex();
			wr.pos(element.x + increment, bottom, 0).color(0F, 1F, 0F, 1F).endVertex();

			increment += element.width / 6F;

			wr.pos(element.x + increment, top, 0).color(0F, 1F, 1F, 1F).endVertex();
			wr.pos(element.x + increment, bottom, 0).color(0F, 1F, 1F, 1F).endVertex();

			increment += element.width / 6F;

			wr.pos(element.x + increment, top, 0).color(0F, 0F, 1F, 1F).endVertex();
			wr.pos(element.x + increment, bottom, 0).color(0F, 0F, 1F, 1F).endVertex();

			increment += element.width / 6F;

			wr.pos(element.x + increment, top, 0).color(1F, 0F, 1F, 1F).endVertex();
			wr.pos(element.x + increment, bottom, 0).color(1F, 0F, 1F, 1F).endVertex();

			increment += element.width / 6F;

			wr.pos(element.x + increment, top, 0).color(1F, 0F, 0F, 1F).endVertex();
			wr.pos(element.x + increment, bottom, 0).color(1F, 0F, 0F, 1F).endVertex();

			tess.draw();
			GlStateManager.enableCull();
			GlStateManager.shadeModel(GL11.GL_FLAT);
			GlStateManager.disableBlend();
			GlStateManager.enableTexture2D();
			GuiUtil.drawColorCircle((int) (element.x + (element.width * modColor.getHue())), (int) (element.y + element.height * 0.5F), element.height, 0xFFFFFFFF);
		})
			.setColor(modColor.getIntColor())
			.addListener((MouseListener) element -> {
				int mouseX = Mouse.getX();
				if (Mouse.getEventButtonState()) {
					if (element.isMouseOver() && !dragging.getValue()) {
						dragging.setValue(true);
					}
				} else if (Mouse.getEventButton() != -1) {
					dragging.setValue(false);
				}
				if (dragging.getValue()) {
					if (mouseX < element.x) {
						modColor.setHue(0);
					} else if (mouseX > element.x + element.width) {
						modColor.setHue(1);
					} else {
						float percent = (mouseX - element.x) / (float) element.width;
						modColor.setHue(percent);
					}
				}
				return false;
			});
	}

	public static BasicElement<?> makeSaturationValueSquare(ModColor modColor) {

		FalcunValue<Boolean> dragging = new FalcunValue<>(false);

		return new DynamicElement<>(element -> {
			float[] colors = ColorUtil.getColorComponents(element.color);
			GlStateManager.disableTexture2D();
			GlStateManager.enableBlend();
			GlStateManager.disableAlpha();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.shadeModel(7425);
			Tessellator tess = Tessellator.getInstance();
			WorldRenderer wr = tess.getWorldRenderer();
			wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			wr.pos(element.x, element.y + element.height, 0).color(0F, 0F, 0F, 1F).endVertex();
			wr.pos(element.x + element.width, element.y + element.height, 0).color(0F, 0F, 0F, 1F).endVertex();
			wr.pos(element.x + element.width, element.y, 0).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
			wr.pos(element.x, element.y, 0).color(1F, 1F, 1F, 1F).endVertex();
			tess.draw();
			GlStateManager.shadeModel(7424);
			GlStateManager.disableBlend();
			GlStateManager.enableAlpha();
			GlStateManager.enableTexture2D();
			GuiUtil.drawColorCircleOutline((int) (element.x + (element.width * modColor.getSaturation())), (int) (element.y + (element.height * (1 - modColor.getValue()))), 8, 0xFFFFFFFF, 2);
		})
			.addListener((ElementListener) (element, phase) -> {
				if (phase == Listener.Phase.PRE) {
					element.setColor(modColor.getPlainColor());
				}
			})
			.addListener((MouseListener) element -> {
				int mouseX = Mouse.getX();
				int mouseY = Display.getHeight() - Mouse.getY();
				if (Mouse.getEventButtonState()) {
					if (element.isMouseOver() && !dragging.getValue()) {
						dragging.setValue(true);
					}
				} else if (Mouse.getEventButton() != -1) {
					dragging.setValue(false);
				}
				if (dragging.getValue()) {
					if (mouseX < element.x) {
						modColor.setSaturation(0);
					} else if (mouseX > element.x + element.width) {
						modColor.setSaturation(1);
					} else {
						float percent = (mouseX - element.x) / (float) element.width;
						modColor.setSaturation(percent);
					}
					if (mouseY < element.y) {
						modColor.setValue(1);
					} else if (mouseY > element.y + element.height) {
						modColor.setValue(0);
					} else {
						float percent = (mouseY - element.y) / (float) element.height;
						modColor.setValue(1 - percent);
					}
				}
				return false;
			});
	}

	public static BasicElement<?> getAlphaSlider(ModColor modColor) {

		FalcunValue<Boolean> dragging = new FalcunValue<>(false);

		return new DynamicElement<>(element -> {
			GlStateManager.disableTexture2D();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(770, 771);
			GlStateManager.disableCull();
			Tessellator tess = Tessellator.getInstance();
			WorldRenderer wr = tess.getWorldRenderer();
			// top row
			wr.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_COLOR);
			int increment = element.height / 2;
			int top = element.y;
			int bottom = element.y + increment;
			int x = element.x;
			wr.pos(element.x, top, 0).color(0.3F, 0.3F, 0.3F, 1F).endVertex();
			wr.pos(element.x, bottom, 0).color(0.3F, 0.3F, 0.3F, 1F).endVertex();
			while ((x += increment) < element.x + element.width) {
				int i = (x - element.x) / increment;

				float red, green, blue;

				red = green = blue = i % 2 == 0 ? 1F : 0.8F;

				wr.pos(x, top, 0).color(red, green, blue, 1F).endVertex();
				wr.pos(x, bottom, 0).color(red, green, blue, 1F).endVertex();
			}
			tess.draw();
			// bottom row
			wr.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_COLOR);
			top += increment;
			bottom += increment;
			x = element.x;
			wr.pos(element.x, top, 0).color(1F, 1F, 1F, 1F).endVertex();
			wr.pos(element.x, bottom, 0).color(1F, 1F, 1F, 1F).endVertex();
			while ((x += increment) < element.x + element.width) {
				int i = (x - element.x) / increment;

				float red, green, blue;

				red = green = blue = i % 2 == 0 ? 0.8F : 1F;

				wr.pos(x, top, 0).color(red, green, blue, 1F).endVertex();
				wr.pos(x, bottom, 0).color(red, green, blue, 1F).endVertex();
			}
			tess.draw();
			// actual color
			float[] colors = ColorUtil.getColorComponents(modColor.getIntColor());
			GlStateManager.enableCull();
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
			wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			wr.pos(element.x, element.y, 0).color(colors[0], colors[1], colors[2], 0F).endVertex();
			wr.pos(element.x, element.y + element.height, 0).color(colors[0], colors[1], colors[2], 0F).endVertex();
			wr.pos(element.x + element.width, element.y + element.height, 0).color(colors[0], colors[1], colors[2], 1F).endVertex();
			wr.pos(element.x + element.width, element.y, 0).color(colors[0], colors[1], colors[2], 1F).endVertex();
			tess.draw();
			GlStateManager.shadeModel(GL11.GL_FLAT);
			GlStateManager.disableBlend();
			GlStateManager.enableTexture2D();
			GuiUtil.drawColorCircle((int) (element.x + (element.width * modColor.getAlpha())), (int) (element.y + element.height * 0.5F), element.height, 0xFFFFFFFF);
		})
			.addListener((MouseListener) element -> {
				int mouseX = Mouse.getX();
				if (Mouse.getEventButtonState()) {
					if (element.isMouseOver() && !dragging.getValue()) {
						dragging.setValue(true);
					}
				} else if (Mouse.getEventButton() != -1) {
					dragging.setValue(false);
				}
				if (dragging.getValue()) {
					if (mouseX < element.x) {
						modColor.setAlpha(0);
					} else if (mouseX > element.x + element.width) {
						modColor.setAlpha(1);
					} else {
						float percent = (mouseX - element.x) / (float) element.width;
						modColor.setAlpha(percent);
					}
				}
				return false;
			});
	}
}