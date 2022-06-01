package net.mattbenson.gui.menu.components.cosmetics;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.mattbenson.gui.framework.MenuComponent;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.mattbenson.gui.menu.IngameMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;

public class CosmeticUserPreview extends MenuComponent {
	public CosmeticUserPreview(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public void onInitColors() {
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(18, 17, 22, IngameMenu.MENU_ALPHA));
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(46, 46, 48, IngameMenu.MENU_ALPHA));
	}
	
	@Override
	public void onRender() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int mouseX = parent.getMouseX();
		int width = this.width;
		
		int lineColor = getColor(DrawType.LINE, ButtonState.NORMAL);
		int backgroundColor = getColor(DrawType.BACKGROUND, ButtonState.NORMAL);

		drawRectFalcun(x, y, width, height, backgroundColor);
		
		drawHorizontalLine(x, y, width + 1, 1, lineColor);
		drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
		drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
		drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);

		drawShadowUp(x, y, width + 1);
		drawShadowLeft(x, y, height + 1);
		drawShadowDown(x, y + height + 1, width + 1);
		drawShadowRight(x + width + 1, y, height + 1);
		
		GL11.glPushMatrix();
		GlStateManager.translate(x + width / 2, y + height / 2 + 120, 125);
		GlStateManager.scale(1.25D, 1.25D, 1.25D);

		int speed = 15;
		float rotateDelta = System.currentTimeMillis() % (360 * speed) / speed;
		drawEntityOnScreen(100, rotateDelta, Minecraft.getMinecraft().thePlayer);
		
		GL11.glPopMatrix();
	}
	
	private void drawEntityOnScreen(int scale, float rotate, EntityPlayerSP ent) {
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(rotate, 0.0F, 1.0F, 0.0F);
		float f = ent.renderYawOffset;
		float f1 = ent.rotationYaw;
		float f2 = ent.rotationPitch;
		float f3 = ent.prevRotationYawHead;
		float f4 = ent.rotationYawHead;
		GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-((float)Math.atan((double)(0 / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
		ent.renderYawOffset = (float)Math.atan((double)(0 / 40.0F)) * 20.0F;
		ent.rotationYaw = (float)Math.atan((double)(0 / 40.0F)) * 40.0F;
		ent.rotationPitch = -((float)Math.atan((double)(0 / 40.0F))) * 20.0F;
		ent.rotationYawHead = ent.rotationYaw;
		ent.prevRotationYawHead = ent.rotationYaw;
		ent.dontRenderNameTag = true;
		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		GlStateManager.disableLighting();
		GlStateManager.color(0.5F, 0.5F, 0.5F, 0.5F);
		rendermanager.setPlayerViewY(180.0F);
		rendermanager.setRenderShadow(false);
		rendermanager.doRenderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		rendermanager.setRenderShadow(true);
		ent.renderYawOffset = f;
		ent.rotationYaw = f1;
		ent.rotationPitch = f2;
		ent.prevRotationYawHead = f3;
		ent.rotationYawHead = f4;
		ent.dontRenderNameTag = false;
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}
}
