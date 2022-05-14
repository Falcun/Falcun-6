package net.mattbenson.modules.types.mods;

import org.lwjgl.opengl.GL11;

import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.utils.AssetUtils;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class DirectionHUD extends Module {
	private float zLevel  = -100.0F;
	private ResourceLocation HUD_DIRECTION = AssetUtils.getResource("modules/compass/compass.png");
	
	private HUDElement hud;
	
	public DirectionHUD() {
		super("Direction HUD", ModuleCategory.MODS);
		
		hud = new HUDElement("hud", 65, 12) {
			@Override
			public void onRender() {
				render();
			}
		};
		
		hud.setX(320);
		hud.setY(0);
		
		addHUD(hud);
	}

	public void render() {
		if (this.mc.gameSettings.showDebugInfo) {
			return;
		}
		
		int posX = hud.getX();
		int posY = hud.getY();
		
		int xBase = 0;
		int yBase = 0;
		int direction = MathHelper.floor_double(((mc.thePlayer.rotationYaw * 256F) / 360F) + 0.5D) & 255;

		mc.getTextureManager().bindTexture(HUD_DIRECTION);
		
		GL11.glPushMatrix();
		GL11.glColor3f((float) 1, (float) 1, (float) 1);
		int compassIndex = 0;
		if (direction < 128)
			drawTexturedModalRect((int) posX + xBase, (int) posY + yBase, direction, (compassIndex * 24), 65,
					12, zLevel);
		else
			drawTexturedModalRect((int) posX + xBase, (int) posY + yBase, direction - 128,
					(compassIndex * 24) + 12, 65, 12, zLevel);

		// mc.renderEngine.resetBoundTexture();
		mc.fontRendererObj.drawString("|", (int) posX + xBase + 32, (int) posY + yBase + 1, 0xffffff);
		mc.fontRendererObj.drawString("|", (int) posX + xBase + 32, (int) posY + yBase + 5, 0xffffff);
		GL11.glScaled(1, 1, 1);
		GL11.glPopMatrix();
	}

	public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, float zLevel) {
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;
		WorldRenderer tessellator = Tessellator.getInstance().getWorldRenderer();
		tessellator.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tessellator.pos((x + 0), (y + height), zLevel).tex(((u + 0) * var7), ((v + height) * var8)).endVertex();
		tessellator.pos((x + width), (y + height), zLevel).tex(((u + width) * var7), ((v + height) * var8)).endVertex();
		tessellator.pos((x + width), (y + 0), zLevel).tex(((u + width) * var7), ((v + 0) * var8)).endVertex();
		tessellator.pos((x + 0), (y + 0), zLevel).tex(((u + 0) * var7), ((v + 0) * var8)).endVertex();
		Tessellator.getInstance().draw();
	}
}
