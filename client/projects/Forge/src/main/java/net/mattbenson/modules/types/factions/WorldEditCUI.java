package net.mattbenson.modules.types.factions;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.entity.PlayerInteractEvent;
import net.mattbenson.events.types.entity.PlayerInteractEvent.Action;
import net.mattbenson.events.types.input.MouseDownEvent;
import net.mattbenson.events.types.render.RenderEvent;
import net.mattbenson.events.types.render.RenderType;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.utils.legacy.Point;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class WorldEditCUI extends Module {
	@ConfigValue.Boolean(name = "See Through")
	private boolean seeThrough = true;
	
	@ConfigValue.Color(name = "Highlight Color")
	private Color hColor = Color.BLACK;
	
	@ConfigValue.Color(name = "Border Color")
	private Color vColor = Color.BLACK;
	
	@ConfigValue.Color(name = "Info Color")
	private Color iColor = Color.WHITE;
	
	@ConfigValue.Float(name = "Line Width", min = 0.1F, max = 7.5F)
	private float lineWidth = 1;
	
	private boolean isUsingChroma = false;
	private boolean isUsingChroma2 = false;
	private boolean isUsingChroma3 = false;
	
	private Point start;
	private Point end;
	
	public WorldEditCUI() {
		super("WorldEdit CUI", ModuleCategory.FACTIONS);
	}
	
	@Override
	public void onEnable() {
		start = null;
		end = null;
	}
	
	@Override
	public void onDisable() {
		start = null;
		end = null;
	}
	
	@SubscribeEvent
	public void onWorldRenderLast(RenderEvent event)
	{
		if(event.getRenderType() != RenderType.WORLD) {
			return;
		}
		
		if (mc.theWorld != null)
		{

			float partial = event.getPartialTicks();
			float px = (float)mc.thePlayer.posX;
			float py = (float)mc.thePlayer.posY;
			float pz = (float)mc.thePlayer.posZ;
			float mx = (float)mc.thePlayer.prevPosX;
			float my = (float)mc.thePlayer.prevPosY;
			float mz = (float)mc.thePlayer.prevPosZ;
			float dx = mx + ( px - mx ) * partial;
			float dy = my + ( py - my ) * partial;
			float dz = mz + ( pz - mz ) * partial;

			double renderPosX = mc.getRenderManager().viewerPosX;
			double renderPosY = mc.getRenderManager().viewerPosY;
			double renderPosZ = mc.getRenderManager().viewerPosZ;
			BlockPos posPlayer = mc.thePlayer.getPosition();

			if(hColor.getBlue() == 5 && hColor.getRed() == 5 && hColor.getGreen() == 5) {
				isUsingChroma = true;
			} else {
				isUsingChroma = false;
			}
			float red = hColor.getRed()/ 255.0f;
			float green = hColor.getGreen()/ 255.0f;
			float blue = hColor.getBlue()/ 255.0f;
			float alpha = hColor.getAlpha()/ 255.0f;

			if(isUsingChroma) {
				float hue = System.currentTimeMillis() % 20000L / 20000.0f;
				int chroma = Color.HSBtoRGB(hue, 1.0f, 1.0f);
				alpha = (chroma >> 24 & 0xFF) / 255.0f;
				red = (chroma >> 16 & 0xFF) / 255.0f;
				green = (chroma >> 8 & 0xFF) / 255.0f;
				blue = (chroma & 0xFF) / 255.0f;
			} 

			if(vColor.getBlue() == 5 && vColor.getRed() == 5 && vColor.getGreen() == 5) {
				isUsingChroma2 = true;
			} else {
				isUsingChroma2 = false;
			}
			float red2 = vColor.getRed()/ 255.0f;
			float green2 = vColor.getGreen()/ 255.0f;
			float blue2 = vColor.getBlue()/ 255.0f;
			float alpha2 = vColor.getAlpha()/ 255.0f;

			if(isUsingChroma2) {
				float hue = System.currentTimeMillis() % 20000L / 20000.0f;
				int chroma = Color.HSBtoRGB(hue, 1.0f, 1.0f);
				alpha2 = (chroma >> 24 & 0xFF) / 255.0f;
				red2 = (chroma >> 16 & 0xFF) / 255.0f;
				green2 = (chroma >> 8 & 0xFF) / 255.0f;
				blue2 = (chroma & 0xFF) / 255.0f;
			} 

		

			if(start != null && end == null) {
				GL11.glPushMatrix();
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

				GL11.glLineWidth(lineWidth);
				if (seeThrough)
				{
					GL11.glDisable(GL11.GL_DEPTH_TEST);

				}
				else
				{
					GL11.glEnable(GL11.GL_DEPTH_TEST);
					GlStateManager.depthMask(false);
				}

				BlockPos pos = new BlockPos(start.getX(),start.getY(),start.getZ());
				Block block = (Block) mc.theWorld.getBlockState(pos).getBlock();
				AxisAlignedBB bb = block.getSelectedBoundingBox(mc.theWorld, pos).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-renderPosX, -renderPosY, -renderPosZ);


				GL11.glColor4f(0,0,1,0.3F);
				drawFilledBoundingBox(bb);

				GL11.glColor4f(0,0,1,1F);
				outLine((float)bb.minX,(float)bb.minY,(float)bb.minZ,(float)bb.maxX,(float)bb.maxY,(float)bb.maxZ);

				if(!seeThrough) {
					GL11.glDisable(GL11.GL_DEPTH_TEST);
					GlStateManager.depthMask(true);
				}
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glPopMatrix();
			}
			if(end != null && start == null) {
				GL11.glPushMatrix();
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

				GL11.glLineWidth(lineWidth);
				if (seeThrough)
				{
					GL11.glDisable(GL11.GL_DEPTH_TEST);

				}
				else
				{
					GL11.glEnable(GL11.GL_DEPTH_TEST);
					GlStateManager.depthMask(false);
				}

				BlockPos pos = new BlockPos(end.getX(),end.getY(),end.getZ());
				Block block = (Block) mc.theWorld.getBlockState(pos).getBlock();
				AxisAlignedBB bb = block.getSelectedBoundingBox(mc.theWorld, pos).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-renderPosX, -renderPosY, -renderPosZ);


				GL11.glColor4f(1,0,0,0.3F);
				drawFilledBoundingBox(bb);

				GL11.glColor4f(1,0,0,1F);
				outLine((float)bb.minX,(float)bb.minY,(float)bb.minZ,(float)bb.maxX,(float)bb.maxY,(float)bb.maxZ);

				if(!seeThrough) {
					GL11.glDisable(GL11.GL_DEPTH_TEST);
					GlStateManager.depthMask(true);
				}
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glPopMatrix();
			}
			if(start != null && end != null) {


				GL11.glPushMatrix();
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				//GL11.glTranslated(-renderPosX, -renderPosY, -renderPosZ);
				GL11.glLineWidth(lineWidth);

				if (seeThrough)
				{
					GL11.glDisable(GL11.GL_DEPTH_TEST);

				}
				else
				{
					GL11.glEnable(GL11.GL_DEPTH_TEST);
					GlStateManager.depthMask(false);
				}

				Point max = new Point(Math.max(start.getX(), end.getX()), Math.max(start.getY(), end.getY()), Math.max(start.getZ(), end.getZ()));
				Point min = new Point(Math.min(start.getX(), end.getX()), Math.min(start.getY(), end.getY()), Math.min(start.getZ(), end.getZ()));

				float x1 = min.getX() - 0.03F;
				float y1 = min.getY() - 0.03F;
				float z1 = min.getZ() - 0.03F;

				float x2 = max.getX() + 1 + 0.03F;
				float y2 = max.getY() + 1 + 0.03F;
				float z2 = max.getZ() + 1 + 0.03F;

				GL11.glColor4f(red2,green2,blue2,1F);
				outLine(x1,y1,z1,x2,y2,z2);
				
				GL11.glColor4f(red,green,blue,0.3F);
				drawFilledBoundingBox(new AxisAlignedBB(x1,y1,z1,x2,y2,z2).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-renderPosX, -renderPosY, -renderPosZ));


				if(!seeThrough) {
					GL11.glDisable(GL11.GL_DEPTH_TEST);
					GlStateManager.depthMask(true);
				}
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_TEXTURE_2D);

				GL11.glPopMatrix();

				GL11.glPushMatrix();
				renderNameTag(max.getY() - min.getY() > 1 ? (max.getY() - min.getY()+ 1) + " blocks long" : (max.getY() - min.getY()+1) + " block long", (start.getX() - dx)+ 0.5F, (start.getY() - dy) + 0.5F, (start.getZ() - dz)+0.5F);
				GL11.glPopMatrix();
				
				GL11.glPushMatrix();
				renderNameTag(max.getX() - min.getX() > 1 ? (max.getX() - min.getX()+ 1) + " blocks wide" : (max.getX() - min.getX()+1) + " block wide", (start.getX() - dx)+ 0.5F, (start.getY() - dy) + 0.8F, (start.getZ() - dz)+0.5F);
				GL11.glPopMatrix();
				
				
				GL11.glPushMatrix();
				renderNameTag(max.getZ() - min.getZ() > 1 ? (max.getZ() - min.getZ()+ 1) + " blocks deep" : (max.getZ() - min.getZ()+1) + " block deep", (start.getX() - dx)+ 0.5F, (start.getY() - dy) + 1.1F, (start.getZ() - dz)+0.5F);
				GL11.glPopMatrix();
			}
		}
	}

	@SubscribeEvent
	public void onClick(PlayerInteractEvent event) {
		if(event.getAction().equals(Action.LEFT_CLICK_BLOCK) && mc.thePlayer.getHeldItem() != null && isAxe(mc.thePlayer.getHeldItem().getItem())) {
			start = new Point(event.getPos().getX(),event.getPos().getY(),event.getPos().getZ());
		}
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && mc.thePlayer.getHeldItem() != null && isAxe(mc.thePlayer.getHeldItem().getItem())) {
			end = new Point(event.getPos().getX(),event.getPos().getY(),event.getPos().getZ());
		}
	}

	@SubscribeEvent
	public void onClickMouse(MouseDownEvent event) {
		if(event.getButton() == 0  && mc.thePlayer.getHeldItem() != null && isAxe(mc.thePlayer.getHeldItem().getItem())) {
			MovingObjectPosition mop = mc.objectMouseOver;

			if (mop == null)
				return;

			if(mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK) { 
				BlockPos blockPos = mop.getBlockPos();
				start = new Point(blockPos.getX(),blockPos.getY(),blockPos.getZ());
			}
		}

	}
	public static boolean isAxe(Item item) {
		return item == Item.getItemById(271) ||  item == Item.getItemById(275) ||  item == Item.getItemById(286) ||  item == Item.getItemById(279);   
	}

	private static void outLine(float x1, float y1, float z1, float x2, float y2, float z2)
	{
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer wr = tessellator.getWorldRenderer();

		wr.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);

		wr.pos(x1, y1, z1).endVertex();
		wr.pos(x2, y1, z1).endVertex();
		wr.pos(x2, y1, z2).endVertex();
		wr.pos(x1, y1, z2).endVertex();
		tessellator.draw();

		// Draw top face
		wr.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
		wr.pos(x1, y2, z1).endVertex();
		wr.pos(x2, y2, z1).endVertex();
		wr.pos(x2, y2, z2).endVertex();
		wr.pos(x1, y2, z2).endVertex();
		tessellator.draw();

		// Draw join top and bottom faces
		wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

		wr.pos(x1, y1, z1).endVertex();
		wr.pos(x1, y2, z1).endVertex();

		wr.pos(x2, y1, z1).endVertex();
		wr.pos(x2, y2, z1).endVertex();

		wr.pos(x2, y1, z2).endVertex();
		wr.pos(x2, y2, z2).endVertex();

		wr.pos(x1, y1, z2).endVertex();
		wr.pos(x1, y2, z2).endVertex();

		tessellator.draw();
	}

	private void drawFilledBoundingBox(AxisAlignedBB box) {

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();

		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		tessellator.draw();

		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		tessellator.draw();

		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		tessellator.draw();

		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		tessellator.draw();

		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		tessellator.draw();

		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		tessellator.draw();
	}

	public void renderNameTag(String str, double x, double y, double z) {
		if(iColor.getBlue() == 5 && iColor.getRed() == 5 && iColor.getGreen() == 5) {
			isUsingChroma3 = true;
		} else {
			isUsingChroma3 = false;
		}
		float red = iColor.getRed()/ 255.0f;
		float green = iColor.getGreen()/ 255.0f;
		float blue = iColor.getBlue()/ 255.0f;
		float alpha = iColor.getAlpha()/ 255.0f;

		if(isUsingChroma3) {
			float hue = System.currentTimeMillis() % 20000L / 20000.0f;
			int chroma = Color.HSBtoRGB(hue, 1.0f, 1.0f);
			alpha = (chroma >> 24 & 0xFF) / 255.0f;
			red = (chroma >> 16 & 0xFF) / 255.0f;
			green = (chroma >> 8 & 0xFF) / 255.0f;
			blue = (chroma & 0xFF) / 255.0f;
		} 
		
		FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;

		RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		float f = 1.6F;
		float f1 = 0.016666668F * f;
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x + 0.0F, (float)y + 1, (float)z);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(-f1, -f1, f1);
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);
		GlStateManager.disableDepth();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		int i = 0;

		int j = (int) (Fonts.ubuntuFont.getStringWidth(str) / 2);
		GlStateManager.disableTexture2D();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		worldrenderer.pos((double)(-j - 1), (double)(-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.35F).endVertex();
		worldrenderer.pos((double)(-j - 1), (double)(8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.35F).endVertex();
		worldrenderer.pos((double)(j + 1), (double)(8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.35F).endVertex();
		worldrenderer.pos((double)(j + 1), (double)(-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.35F).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		Fonts.ubuntuFont.drawString(str, -Fonts.ubuntuFont.getStringWidth(str) / 2, i-3, new Color(red,green,blue,alpha).getRGB());
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		Fonts.ubuntuFont.drawString(str, -Fonts.ubuntuFont.getStringWidth(str) / 2, i-3, new Color(red,green,blue,alpha).getRGB());
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GL11.glNormal3f(0.0F, 0.0F, 1.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}
}
