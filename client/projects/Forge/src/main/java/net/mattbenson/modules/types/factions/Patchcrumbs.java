package net.mattbenson.modules.types.factions;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.lwjgl.opengl.GL11;

import net.java.games.input.Keyboard;
import net.mattbenson.Falcun;
import net.mattbenson.chat.ChatUtils;
import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.input.KeyDownEvent;
import net.mattbenson.events.types.render.RenderEvent;
import net.mattbenson.events.types.render.RenderType;
import net.mattbenson.events.types.world.OnTickEvent;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.input.KeybindManager;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.modules.types.mods.GroupPatchcrumb;
import net.mattbenson.network.NetworkingClient;
import net.mattbenson.network.common.GroupData;
import net.mattbenson.network.network.packets.groups.GroupList;
import net.mattbenson.utils.legacy.ColorMap;
import net.mattbenson.utils.legacy.GlShim;
import net.mattbenson.utils.legacy.HUDUtil;
import net.mattbenson.utils.legacy.LessArrayLists;
import net.minecraft.block.BlockGravel;
import net.minecraft.block.BlockSand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Patchcrumbs extends Module {
	@ConfigValue.Integer(name = "Timeout (sec)", min = 1, max = 60)
	private int time = 2;

	@ConfigValue.Float(name = "Width", min = 0.1F, max = 5.0F)
	public float width = 1;
	
	@ConfigValue.Integer(name = "Distance", min = 50, max = 1000)
	public int distance = 200;

	@ConfigValue.List(name = "Side", values = { "NORTH/SOUTH", "EAST/WEST", "BOTH" })
	public String mode = "BOTH";

	@ConfigValue.Boolean(name = "Render Tag")
	private boolean renderTag = true;

	@ConfigValue.Boolean(name = "Tracers")
	private boolean tracers = true;

	@ConfigValue.Boolean(name = "Still Merge TNT")
	public boolean mergeTNT = false;

	@ConfigValue.Boolean(name = "ABC")
	private boolean abc = true;
	
	@ConfigValue.Boolean(name = "Minimap Display")
    public boolean minimap = true;

	@ConfigValue.Color(name = "Tracer Color")
	private Color tracerColor = Color.RED;

	@ConfigValue.Color(name = "Patch Line Color")
	private Color patchColor = Color.ORANGE;

	@ConfigValue.Color(name = "Shot Color")
	private Color shotColor = Color.RED;

	@ConfigValue.Color(name = "Text Color")
	private Color textColor = Color.WHITE;

	@ConfigValue.Keybind(name = "Shout TNT Coordinates Key")
	private int keyBind2 = 0;

	@ConfigValue.Color(name = "Font Color")
	private Color color = Color.WHITE;

	@ConfigValue.Boolean(name = "Background")
	private boolean backGround = true;

	@ConfigValue.Color(name = "Background Color")
	private Color bColor = new Color(0, 0, 0, 150);

	@ConfigValue.Text(name = "Chat Format")
	private String chatFormat = "X: {x}, Y: {y}, Z: {z}";

	private ArrayList<WallCoord> wallCoords = new ArrayList<WallCoord>();
	public PatchCrumb currentCrumb = null;
	private PatchCrumb lastCrumb;

	private int currentCoordX;
	private int currentCoordZ;
	private double highestY;
	private TNTCrumb currentCrumbEntity;
	private TNTCrumb lastCrumb1;

	private long nextAllowed = 0L;
	private int timeout = 2;

	private HUDElement hud;

	public Patchcrumbs() {
		super("Patchcrumbs", ModuleCategory.FACTIONS);

		hud = new HUDElement("patchcrumbs", 1, 175) {
			@Override
			public void onRender() {
				render();
			}
		};
		
		addHUD(hud);
		
		Falcun.getInstance().EVENT_BUS.register(new LessArrayLists());
	}

	public void outlinedShotBox(final PatchCrumb crumb) {
		GlShim.glPushMatrix();
		GlShim.glBlendFunc(770, 771);
		GlShim.glEnable(3042);
		GlShim.glLineWidth((float) width);
		GlShim.glDisable(3553);
		GlShim.glDisable(2929);
		GlShim.glDepthMask(false);

		float red = (float) (shotColor.getRed() / 255.0F);
		float green = (float) (shotColor.getGreen() / 255.0F);
		float blue = (float) (shotColor.getBlue() / 255.0F);
		float alpha = (float) (shotColor.getAlpha() / 255.0F);

		GL11.glColor4f(red, green, blue, alpha);

		Minecraft.getMinecraft().getRenderManager();
		drawSelectionBoundingBox(new AxisAlignedBB(
				crumb.boundingBOX.minX - 0.05 - crumb.posX
						+ (crumb.posX - Minecraft.getMinecraft().getRenderManager().viewerPosX),
				crumb.boundingBOX.minY - crumb.posY
						+ (crumb.posY - Minecraft.getMinecraft().getRenderManager().viewerPosY),
				crumb.boundingBOX.minZ - 0.05 - crumb.posZ
						+ (crumb.posZ - Minecraft.getMinecraft().getRenderManager().viewerPosZ),
				crumb.boundingBOX.maxX + 0.05 - crumb.posX
						+ (crumb.posX - Minecraft.getMinecraft().getRenderManager().viewerPosX),
				crumb.boundingBOX.maxY + 0.1 - crumb.posY
						+ (crumb.posY - Minecraft.getMinecraft().getRenderManager().viewerPosY),
				crumb.boundingBOX.maxZ + 0.05 - crumb.posZ
						+ (crumb.posZ - Minecraft.getMinecraft().getRenderManager().viewerPosZ)));
		GlShim.glEnable(3553);
		GlShim.glEnable(2929);
		GlShim.glDepthMask(true);
		GlShim.glDisable(3042);
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlShim.glPopMatrix();
	}

	public void drawSelectionBoundingBox(AxisAlignedBB p_181561_0_) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(3, DefaultVertexFormats.POSITION);
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
		tessellator.draw();
		worldrenderer.begin(3, DefaultVertexFormats.POSITION);
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
		tessellator.draw();
		worldrenderer.begin(1, DefaultVertexFormats.POSITION);
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
		tessellator.draw();
	}

	public void patchFinder(final PatchCrumb crumb) {
		GlStateManager.pushMatrix();
		GlShim.glBlendFunc(770, 771);
		GlShim.glEnable(3042);
		GlShim.glLineWidth((float) width);
		GlShim.glDisable(3553);
		GlShim.glDisable(2929);
		GlShim.glDepthMask(false);

		float red = (float) (patchColor.getRed() / 255.0F);
		float green = (float) (patchColor.getGreen() / 255.0F);
		float blue = (float) (patchColor.getBlue() / 255.0F);
		float alpha = (float) (patchColor.getAlpha() / 255.0F);

		GL11.glColor4f(red, green, blue, alpha);

		final RenderManager rm = Minecraft.getMinecraft().getRenderManager();
		final double vX = rm.viewerPosX;
		final double vY = rm.viewerPosY;
		final double vZ = rm.viewerPosZ;
		final Entity player = (Entity) Minecraft.getMinecraft().thePlayer;
		final double pX = player.posX;
		final double pY = player.posY;
		final double pZ = player.posZ;
		final int extender = distance;
		if (mode.equalsIgnoreCase("NORTH/SOUTH")) {
			final AxisAlignedBB box = new AxisAlignedBB(crumb.boundingBOX.minX - 0.05 - crumb.posX + (crumb.posX - vX),
					crumb.boundingBOX.minY - crumb.posY + (crumb.posY - vY),
					vZ - extender - 0.05 - crumb.posZ + (crumb.posZ - vZ),
					crumb.boundingBOX.maxX + 0.05 - crumb.posX + (crumb.posX - vX),
					crumb.boundingBOX.maxY + 0.1 - crumb.posY + (crumb.posY - vY),
					vZ + extender + 0.05 - crumb.posZ + (crumb.posZ - vZ));
			drawSelectionBoundingBox(box);
		} else if (mode.equalsIgnoreCase("EAST/WEST")) {
			final AxisAlignedBB box = new AxisAlignedBB(vX - extender - 0.05 - crumb.posX + (crumb.posX - vX),
					crumb.boundingBOX.minY - crumb.posY + (crumb.posY - vY),
					crumb.boundingBOX.minZ - 0.05 - crumb.posZ + (crumb.posZ - vZ),
					vX + extender + 0.05 - crumb.posX + (crumb.posX - vX),
					crumb.boundingBOX.maxY + 0.1 - crumb.posY + (crumb.posY - vY),
					crumb.boundingBOX.maxZ + 0.05 - crumb.posZ + (crumb.posZ - vZ));
			drawSelectionBoundingBox(box);
		} else {
			final AxisAlignedBB bX = new AxisAlignedBB(vX - extender - 0.05 - crumb.posX + (crumb.posX - vX),
					crumb.boundingBOX.minY - crumb.posY + (crumb.posY - vY),
					crumb.boundingBOX.minZ - 0.05 - crumb.posZ + (crumb.posZ - vZ),
					vX + extender + 0.05 - crumb.posX + (crumb.posX - vX),
					crumb.boundingBOX.maxY + 0.1 - crumb.posY + (crumb.posY - vY),
					crumb.boundingBOX.maxZ + 0.05 - crumb.posZ + (crumb.posZ - vZ));
			drawSelectionBoundingBox(bX);
			final AxisAlignedBB bZ = new AxisAlignedBB(crumb.boundingBOX.minX - 0.05 - crumb.posX + (crumb.posX - vX),
					crumb.boundingBOX.minY - crumb.posY + (crumb.posY - vY),
					vZ - extender - 0.05 - crumb.posZ + (crumb.posZ - vZ),
					crumb.boundingBOX.maxX + 0.05 - crumb.posX + (crumb.posX - vX),
					crumb.boundingBOX.maxY + 0.1 - crumb.posY + (crumb.posY - vY),
					vZ + extender + 0.05 - crumb.posZ + (crumb.posZ - vZ));
			drawSelectionBoundingBox(bZ);
		}
		GlShim.glEnable(3553);
		GlShim.glEnable(2929);
		GlShim.glDepthMask(true);
		GlShim.glDisable(3042);
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}

	@SubscribeEvent
	public void clienttick(OnTickEvent event) {

		try {
			if (mc.theWorld == null) {
				return;
			}
			if (mc.thePlayer == null) {
				return;
			}
			if (mc.theWorld == null || mc.thePlayer == null
					|| mc.theWorld.loadedEntityList == null) {
				return;
			}

			if (abc) {
				for (final Entity entity : LessArrayLists.currentTnT) {
					if (entity instanceof EntityTNTPrimed) {
						boolean flag = false;
						for (int i = 0; i < wallCoords.size(); ++i) {
							final WallCoord wc = wallCoords.get(i);
							if (wc.testTNT((EntityTNTPrimed) entity)) {
								flag = true;
							}
						}
						if (flag) {
							continue;
						}
						wallCoords.add(new WallCoord((EntityTNTPrimed) entity));
					}
				}
				for (int j = 0; j < wallCoords.size(); ++j) {
					final WallCoord wc2 = wallCoords.get(j);
					if (mc.theWorld.getBlockState(new BlockPos(wc2.x, wc2.y - 1, wc2.z))
							.getBlock() instanceof BlockSand
							| mc.theWorld.getBlockState(new BlockPos(wc2.x, wc2.y - 1, wc2.z))
									.getBlock() instanceof BlockGravel) {
						currentCrumb = new PatchCrumb(wc2.x, (int) wc2.firstYLevel, wc2.z,
								(Math.abs(wc2.firstVelocityX) < Math.abs(wc2.firstVelocityZ)) ? Direction.EASTWEST
										: Direction.NORTHSOUTH,
								wc2.boundingBOX);
					}
					if (System.currentTimeMillis() > wc2.expiresAt) {
						wallCoords.remove(j);
						--j;
					}
				}
			}
		} catch (Exception E) {
		}
	}

	@SubscribeEvent
	public void renderworldlast(RenderEvent event) {
		if(event.getRenderType() != RenderType.WORLD) {
			return;
		}
		
		try {
			if (mc.theWorld == null) {
				return;
			}
			if (mc.thePlayer == null) {
				return;
			}
			if (mc.theWorld == null || mc.thePlayer == null
					|| mc.theWorld.loadedEntityList == null) {
				return;
			}

			if (abc) {

				if (currentCrumb != null) {
					if (tracers) {
						this.drawTracer(currentCrumb);
					}
					patchFinder(currentCrumb);
					outlinedShotBox(currentCrumb);

					if (renderTag) {
						this.renderTag(currentCrumb);
					}

					if (System.currentTimeMillis() > currentCrumb.expiresAt) {
						currentCrumb = null;
					}
				}

				if (System.currentTimeMillis() > nextAllowed) {

					if (currentCrumb != null) {
						nextAllowed = System.currentTimeMillis() + timeout * 2000;
						for (GroupData group : GroupList.getGroups()) {
							if (GroupList.getSettings(group.getName()).isPatchcrumbs()) {
								if (lastCrumb != currentCrumb) {

									String minX = currentCrumb.boundingBOX.minX + "";
									String minY = currentCrumb.boundingBOX.minY + "";
									String minZ = currentCrumb.boundingBOX.minZ + "";
									String maxX = currentCrumb.boundingBOX.maxX + "";
									String maxY = currentCrumb.boundingBOX.maxY + "";
									String maxZ = currentCrumb.boundingBOX.maxZ + "";

									NetworkingClient.sendLine("Patchcrumb", group.getId() + "",
											Falcun.getInstance().moduleManager.getModule(GroupPatchcrumb.class).isNotifications + "",
											currentCrumb.posX + "", currentCrumb.posY + "", currentCrumb.posZ + "",
											minX, minY, minZ, maxX, maxY, maxZ);
									lastCrumb = currentCrumb;
								}
							}
						}
					}

				}

			} else {

				if (currentCrumbEntity != null) {

					if (tracers) {
						this.drawTracer(currentCrumbEntity);
					}

					patchFinder(currentCrumbEntity);

					if (renderTag) {
						this.renderTag(currentCrumbEntity);
					}

					if (System.currentTimeMillis() > currentCrumbEntity.expireTime) {
						currentCrumbEntity = null;

					}
				}
				for (final Entity entity : LessArrayLists.currentTnT) {
					if (entity instanceof EntityTNTPrimed) {
						if ((int) Math.ceil(entity.posZ) != currentCoordZ
								|| (int) Math.ceil(entity.posX) != currentCoordX) {
							currentCoordX = (int) Math.ceil(entity.posX);
							currentCoordZ = (int) Math.ceil(entity.posZ);
							highestY = entity.posY;
							currentCrumbEntity = new TNTCrumb((EntityTNTPrimed) entity,
									time);
						}
					}
				}

				if (System.currentTimeMillis() > nextAllowed) {

					if (currentCrumbEntity != null) {
						nextAllowed = System.currentTimeMillis() + timeout * 2000;
						for (GroupData group : GroupList.getGroups()) {
							if (GroupList.getSettings(group.getName()).isPatchcrumbs()) {
								if (lastCrumb1 != currentCrumbEntity) {

									String minX = currentCrumbEntity.boundingBOX.minX + "";
									String minY = currentCrumbEntity.boundingBOX.minY + "";
									String minZ = currentCrumbEntity.boundingBOX.minZ + "";
									String maxX = currentCrumbEntity.boundingBOX.maxX + "";
									String maxY = currentCrumbEntity.boundingBOX.maxY + "";
									String maxZ = currentCrumbEntity.boundingBOX.maxZ + "";

									NetworkingClient.sendLine("Patchcrumb", group.getId() + "",
											Falcun.getInstance().moduleManager.getModule(GroupPatchcrumb.class).isNotifications + "",
											currentCrumbEntity.posX + "", currentCrumbEntity.posY + "",
											currentCrumbEntity.posZ + "", minX, minY, minZ, maxX, maxY, maxZ);
									lastCrumb1 = currentCrumbEntity;
								}
							}
						}
					}

				}

			}

		} catch (Exception e) {
		}

	}

	public void fullShotBox(final PatchCrumb crumb, final float partialTicks) {
		final double x_fix = mc.thePlayer.lastTickPosX
				+ (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * partialTicks;
		final double y_fix = mc.thePlayer.lastTickPosY
				+ (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * partialTicks;
		final double z_fix = mc.thePlayer.lastTickPosZ
				+ (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * partialTicks;
		GlStateManager.pushMatrix();
		GlStateManager.translate(-x_fix, -y_fix, -z_fix);
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		GlStateManager.disableTexture2D();

		float red = (float) (shotColor.getRed() / 255.0F);
		float green = (float) (shotColor.getGreen() / 255.0F);
		float blue = (float) (shotColor.getBlue() / 255.0F);
		float alpha = (float) (shotColor.getAlpha() / 255.0F);

		GL11.glColor4f(red, green, blue, alpha);

		final Tessellator tessellator = Tessellator.getInstance();
		final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		final AxisAlignedBB aabb = crumb.boundingBOX;
		worldrenderer.begin(7, DefaultVertexFormats.POSITION);
		worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
		worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
		worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
		worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
		tessellator.draw();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION);
		worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
		worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
		worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
		worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
		tessellator.draw();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION);
		worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
		worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
		worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
		worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
		tessellator.draw();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION);
		worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
		worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
		worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
		worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
		tessellator.draw();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION);
		worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
		worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
		worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
		worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
		tessellator.draw();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION);
		worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
		worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
		worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
		worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		GlStateManager.popMatrix();
	}

	public void drawTracer(final PatchCrumb tnt) {
		try {
			GlShim.glPushMatrix();
			GlShim.glBlendFunc(770, 771);
			GlShim.glEnable(3042);
			GlShim.glEnable(2848);
			GlShim.glLineWidth(2.0f);
			GlShim.glDisable(3553);
			GlShim.glDisable(2929);
			GlShim.glDepthMask(false);
			GlShim.glBegin(1);

			float red = (float) (tracerColor.getRed() / 255.0F);
			float green = (float) (tracerColor.getGreen() / 255.0F);
			float blue = (float) (tracerColor.getBlue() / 255.0F);
			float alpha = (float) (tracerColor.getAlpha() / 255.0F);

			GL11.glColor4f(red, green, blue, alpha);

			final EntityPlayer thePlayer = (EntityPlayer) mc.thePlayer;
			final double x = tnt.posX - mc.getRenderManager().viewerPosX;
			final double y = tnt.posY - mc.getRenderManager().viewerPosY;
			final double z = tnt.posZ - mc.getRenderManager().viewerPosZ;
			final Vec3 eyeVector = new Vec3(0.0, 0.0, 1.0)
					.rotatePitch((float) (-Math.toRadians(thePlayer.rotationPitch)))
					.rotateYaw((float) (-Math.toRadians(thePlayer.rotationYaw)));
			GlShim.glVertex3d(eyeVector.xCoord, thePlayer.getEyeHeight() + eyeVector.yCoord, eyeVector.zCoord);
			GlShim.glVertex3d(x, y, z);
			GlShim.glEnd();
			GlShim.glEnable(3553);
			GlShim.glDisable(2848);
			GlShim.glEnable(2929);
			GlShim.glDepthMask(true);
			GlShim.glDisable(3042);
			GlStateManager.color(1.0f, 1.0f, 1.0f);
			GlShim.glPopMatrix();
		} catch (Exception ex) {
		}
	}

	public void drawTracer(final TNTCrumb tnt) {
		try {
			GlShim.glPushMatrix();
			GlShim.glBlendFunc(770, 771);
			GlShim.glEnable(3042);
			GlShim.glEnable(2848);
			GlShim.glLineWidth(2.0f);
			GlShim.glDisable(3553);
			GlShim.glDisable(2929);
			GlShim.glDepthMask(false);
			GlShim.glBegin(1);

			float red = (float) (tracerColor.getRed() / 255.0F);
			float green = (float) (tracerColor.getGreen() / 255.0F);
			float blue = (float) (tracerColor.getBlue() / 255.0F);
			float alpha = (float) (tracerColor.getAlpha() / 255.0F);

			GL11.glColor4f(red, green, blue, alpha);

			final EntityPlayer thePlayer = (EntityPlayer) mc.thePlayer;
			final double x = tnt.posX - mc.getRenderManager().viewerPosX;
			final double y = tnt.posY - mc.getRenderManager().viewerPosY;
			final double z = tnt.posZ - mc.getRenderManager().viewerPosZ;
			final Vec3 eyeVector = new Vec3(0.0, 0.0, 1.0)
					.rotatePitch((float) (-Math.toRadians(thePlayer.rotationPitch)))
					.rotateYaw((float) (-Math.toRadians(thePlayer.rotationYaw)));
			GlShim.glVertex3d(eyeVector.xCoord, thePlayer.getEyeHeight() + eyeVector.yCoord, eyeVector.zCoord);
			GlShim.glVertex3d(x, y, z);
			GlShim.glEnd();
			GlShim.glEnable(3553);
			GlShim.glDisable(2848);
			GlShim.glEnable(2929);
			GlShim.glDepthMask(true);
			GlShim.glDisable(3042);
			GlStateManager.color(1.0f, 1.0f, 1.0f);
			GlShim.glPopMatrix();
		} catch (Exception ex) {
		}
	}

	public void renderTag(final PatchCrumb tnt) {
		final EntityPlayer player = (EntityPlayer) Minecraft.getMinecraft().thePlayer;
		double x = tnt.posX - player.posX;
		final double y = tnt.posY - player.posY + 0.5;
		double z = tnt.posZ - player.posZ;
		String str;
		if (mode.equalsIgnoreCase("NORTH/SOUTH")) {
			str = "x:" + tnt.posX + " y:" + tnt.posY;
			z = 3.0;
		} else {
			str = "x:" + tnt.posX + " z:" + tnt.posZ + " y:" + tnt.posY;
			x = 3.0;
		}
		final FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;
		final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		final float f = 2.3f;
		final float f2 = 0.016666668f * f;
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x + 0.0f, (float) y + 1.0f, (float) z);
		GL11.glNormal3f(0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
		GlStateManager.scale(-f2, -f2, f2);
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);
		GlStateManager.disableDepth();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.disableTexture2D();
		GlStateManager.enableTexture2D();
		fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, 0, ColorMap.colors.get("White").getRGB());
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		GlStateManager.popMatrix();
	}

	public void renderTag(final TNTCrumb tnt) {
		final EntityPlayer player = (EntityPlayer) Minecraft.getMinecraft().thePlayer;
		double x = tnt.posX - player.posX;
		final double y = tnt.posY - player.posY + 0.5;
		double z = tnt.posZ - player.posZ;
		String str;
		if (mode.equalsIgnoreCase("NORTH/SOUTH")) {
			str = "x:" + tnt.posX + " y:" + tnt.posY;
			z = 3.0;
		} else {
			str = "z:" + tnt.posZ + " y:" + tnt.posY;
			x = 3.0;
		}
		final FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;
		final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		final float f = 2.3f;
		final float f2 = 0.016666668f * f;
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x + 0.0f, (float) y + 1.0f, (float) z);
		GL11.glNormal3f(0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
		GlStateManager.scale(-f2, -f2, f2);
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);
		GlStateManager.disableDepth();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.disableTexture2D();
		GlStateManager.enableTexture2D();
		fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, 0, ColorMap.colors.get("White").getRGB());
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		GlStateManager.popMatrix();
	}

	public void patchFinder(final TNTCrumb crumb) {
		GlStateManager.pushMatrix();
		GlShim.glBlendFunc(770, 771);
		GlShim.glEnable(3042);
		GlShim.glLineWidth((float) width);
		GlShim.glDisable(3553);
		GlShim.glDisable(2929);
		GlShim.glDepthMask(false);

		float red = (float) (patchColor.getRed() / 255.0F);
		float green = (float) (patchColor.getGreen() / 255.0F);
		float blue = (float) (patchColor.getBlue() / 255.0F);
		float alpha = (float) (patchColor.getAlpha() / 255.0F);

		GL11.glColor4f(red, green, blue, alpha);

		Minecraft.getMinecraft().getRenderManager();
		final String domode = mode;
		if (domode.equalsIgnoreCase("NORTH/SOUTH")) {
			drawSelectionBoundingBox(new AxisAlignedBB(
					crumb.boundingBOX.minX - 0.05 - crumb.posX
							+ (crumb.posX - Minecraft.getMinecraft().getRenderManager().viewerPosX),
					crumb.boundingBOX.minY - crumb.posY
							+ (crumb.posY - Minecraft.getMinecraft().getRenderManager().viewerPosY),
					Minecraft.getMinecraft().thePlayer.posZ - 150.0 - 0.05 - crumb.posZ
							+ (crumb.posZ - Minecraft.getMinecraft().getRenderManager().viewerPosZ),
					crumb.boundingBOX.maxX + 0.05 - crumb.posX
							+ (crumb.posX - Minecraft.getMinecraft().getRenderManager().viewerPosX),
					crumb.boundingBOX.maxY + 0.1 - crumb.posY
							+ (crumb.posY - Minecraft.getMinecraft().getRenderManager().viewerPosY),
					Minecraft.getMinecraft().thePlayer.posZ + 150.0 + 0.05 - crumb.posZ
							+ (crumb.posZ - Minecraft.getMinecraft().getRenderManager().viewerPosZ)));
		} else if (domode.equalsIgnoreCase("EAST/WEST")) {
			drawSelectionBoundingBox(new AxisAlignedBB(
					Minecraft.getMinecraft().thePlayer.posX - 150.0 - 0.05 - crumb.posX
							+ (crumb.posX - Minecraft.getMinecraft().getRenderManager().viewerPosX),
					crumb.boundingBOX.minY - crumb.posY
							+ (crumb.posY - Minecraft.getMinecraft().getRenderManager().viewerPosY),
					crumb.boundingBOX.minZ - 0.05 - crumb.posZ
							+ (crumb.posZ - Minecraft.getMinecraft().getRenderManager().viewerPosZ),
					Minecraft.getMinecraft().thePlayer.posX + 150.0 + 0.05 - crumb.posX
							+ (crumb.posX - Minecraft.getMinecraft().getRenderManager().viewerPosX),
					crumb.boundingBOX.maxY + 0.1 - crumb.posY
							+ (crumb.posY - Minecraft.getMinecraft().getRenderManager().viewerPosY),
					crumb.boundingBOX.maxZ + 0.05 - crumb.posZ
							+ (crumb.posZ - Minecraft.getMinecraft().getRenderManager().viewerPosZ)));
		} else {
			drawSelectionBoundingBox(new AxisAlignedBB(
					crumb.boundingBOX.minX - 0.05 - crumb.posX
							+ (crumb.posX - Minecraft.getMinecraft().getRenderManager().viewerPosX),
					crumb.boundingBOX.minY - crumb.posY
							+ (crumb.posY - Minecraft.getMinecraft().getRenderManager().viewerPosY),
					Minecraft.getMinecraft().thePlayer.posZ - 150.0 - 0.05 - crumb.posZ
							+ (crumb.posZ - Minecraft.getMinecraft().getRenderManager().viewerPosZ),
					crumb.boundingBOX.maxX + 0.05 - crumb.posX
							+ (crumb.posX - Minecraft.getMinecraft().getRenderManager().viewerPosX),
					crumb.boundingBOX.maxY + 0.1 - crumb.posY
							+ (crumb.posY - Minecraft.getMinecraft().getRenderManager().viewerPosY),
					Minecraft.getMinecraft().thePlayer.posZ + 150.0 + 0.05 - crumb.posZ
							+ (crumb.posZ - Minecraft.getMinecraft().getRenderManager().viewerPosZ)));
			drawSelectionBoundingBox(new AxisAlignedBB(
					Minecraft.getMinecraft().thePlayer.posX - 150.0 - 0.05 - crumb.posX
							+ (crumb.posX - Minecraft.getMinecraft().getRenderManager().viewerPosX),
					crumb.boundingBOX.minY - crumb.posY
							+ (crumb.posY - Minecraft.getMinecraft().getRenderManager().viewerPosY),
					crumb.boundingBOX.minZ - 0.05 - crumb.posZ
							+ (crumb.posZ - Minecraft.getMinecraft().getRenderManager().viewerPosZ),
					Minecraft.getMinecraft().thePlayer.posX + 150.0 + 0.05 - crumb.posX
							+ (crumb.posX - Minecraft.getMinecraft().getRenderManager().viewerPosX),
					crumb.boundingBOX.maxY + 0.1 - crumb.posY
							+ (crumb.posY - Minecraft.getMinecraft().getRenderManager().viewerPosY),
					crumb.boundingBOX.maxZ + 0.05 - crumb.posZ
							+ (crumb.posZ - Minecraft.getMinecraft().getRenderManager().viewerPosZ)));
		}
		GlShim.glEnable(3553);
		GlShim.glEnable(2929);
		GlShim.glDepthMask(true);
		GlShim.glDisable(3042);
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}

	@SubscribeEvent
	public void onKeyPress(KeyDownEvent event) {
		if (this.mc == null || this.mc.thePlayer == null || this.mc.theWorld == null)
			return;
		
		
		if(KeybindManager.isInvalidScreen(mc.currentScreen)) {
			return;
		}
		
		if (event.getKey() == keyBind2 && keyBind2 != 0) {
			if (abc) {
				if (currentCrumb != null) {
					double x = currentCrumb.posX;
					double y = currentCrumb.posY;
					double z = currentCrumb.posZ;
					String toSend = chatFormat.replace("{x}", (int) x + "").replace("{y}", (int) y + "").replace("{z}",
							(int) z + "");
					mc.thePlayer.sendChatMessage(toSend);
				} else {
					ChatUtils.sendLocalMessage("No shot found yet!", true);
				}
			} else {

				if (currentCrumbEntity != null) {
					double x = currentCrumbEntity.posX;
					double y = currentCrumbEntity.posY;
					double z = currentCrumbEntity.posZ;
					String toSend = chatFormat.replace("{x}", (int) x + "").replace("{y}", (int) y + "").replace("{z}",
							(int) z + "");
					mc.thePlayer.sendChatMessage(toSend);
				} else {
					ChatUtils.sendLocalMessage("No shot found yet!", true);
				}
			}
		}
	}

	private void render() {
		int posX = hud.getX();
		int posY = hud.getY();

		if (abc) {
			if (currentCrumb == null)
				return;
			GL11.glPushMatrix();

			int width = 0;
			int height = 15;
			int added = 0;

			int x = (int) currentCrumb.posX;
			int y = (int) currentCrumb.posY;
			int z = (int) currentCrumb.posZ;

			String sx = "X: " + x;
			if (HUDUtil.getStringWidth(sx) > width) {
				width = HUDUtil.getStringWidth(sx);
			}
			String sy = "Y: " + y;
			if (HUDUtil.getStringWidth(sy) > width) {
				width = HUDUtil.getStringWidth(sy);
			}
			String sz = "Z: " + z;
			if (HUDUtil.getStringWidth(sz) > width) {
				width = HUDUtil.getStringWidth(sz);
			}

			width += 10;

			HUDUtil.drawHUD(sx, posX, posY + added, width, height, bColor, backGround, color, true);
			added += 15;

			HUDUtil.drawHUD(sy, posX, posY + added, width, height, bColor, backGround, color, true);
			added += 15;

			HUDUtil.drawHUD(sz, posX, posY + added, width, height, bColor, backGround, color, true);

			GL11.glScaled(1.0, 1.0, 1.0);
			GL11.glPopMatrix();

			hud.setHeight(height + added);
			hud.setWidth(width);
		} else {
			if (currentCrumbEntity == null)
				return;

			GL11.glPushMatrix();

			int width = 0;
			int height = 15;
			int added = 0;

			int x = (int) currentCrumbEntity.posX;
			int y = (int) currentCrumbEntity.posY;
			int z = (int) currentCrumbEntity.posZ;

			String sx = "X: " + x;
			if (HUDUtil.getStringWidth(sx) > width) {
				width = HUDUtil.getStringWidth(sx);
			}
			String sy = "Y: " + y;
			if (HUDUtil.getStringWidth(sy) > width) {
				width = HUDUtil.getStringWidth(sy);
			}
			String sz = "Z: " + z;
			if (HUDUtil.getStringWidth(sz) > width) {
				width = HUDUtil.getStringWidth(sz);
			}

			width += 10;

			HUDUtil.drawHUD(sx, posX, posY + added, width, height, bColor, backGround, color, true);
			added += 15;

			HUDUtil.drawHUD(sy, posX, posY + added, width, height, bColor, backGround, color, true);
			added += 15;

			HUDUtil.drawHUD(sz, posX, posY + added, width, height, bColor, backGround, color, true);

			GL11.glScaled(1.0, 1.0, 1.0);
			GL11.glPopMatrix();

			hud.setHeight(height + added);
			hud.setWidth(width);
		}
	}

	public class WallCoord {
		public int x;
		public int z;
		public int y;
		public double firstVelocityX;
		public double firstVelocityZ;
		public double firstYLevel;
		public long expiresAt;
		public AxisAlignedBB boundingBOX;
		public HashMap<UUID, TNTSpot> tntSpots;

		public WallCoord(final EntityTNTPrimed entityTNTPrimed) {
			this.x = MathHelper.floor_double(entityTNTPrimed.posX);
			this.z = MathHelper.floor_double(entityTNTPrimed.posZ);
			this.y = MathHelper.floor_double(entityTNTPrimed.posY);
			this.firstYLevel = entityTNTPrimed.posY;
			(this.tntSpots = new HashMap<UUID, TNTSpot>()).put(entityTNTPrimed.getUniqueID(),
					new TNTSpot(entityTNTPrimed));
			this.expiresAt = System.currentTimeMillis() + 4500L;
			this.boundingBOX = entityTNTPrimed.getEntityBoundingBox();
			this.firstVelocityX = entityTNTPrimed.prevPosX - entityTNTPrimed.posX;
			this.firstVelocityZ = entityTNTPrimed.prevPosZ - entityTNTPrimed.posZ;
		}

		public void addTNT(final EntityTNTPrimed tnt) {
			if (this.tntSpots.containsKey(tnt.getUniqueID())) {
				return;
			}
			this.tntSpots.put(tnt.getUniqueID(), new TNTSpot(tnt));
		}

		public boolean testTNT(final EntityTNTPrimed tnt) {
			if (MathHelper.floor_double(tnt.posX) == this.x && MathHelper.floor_double(tnt.posZ) == this.z) {
				this.addTNT(tnt);
				return true;
			}
			return false;
		}
	}

	public class PatchCrumb {
		public int posX;
		public int posY;
		public int posZ;
		public Direction direction;
		public long expiresAt;
		public AxisAlignedBB boundingBOX;

		public PatchCrumb(final int x, final int y, final int z, final Direction direction, final AxisAlignedBB bb) {
			this.posX = x;
			this.posY = y;
			this.posZ = z;
			this.direction = direction;
			this.expiresAt = System.currentTimeMillis() + time * 1000;
			this.boundingBOX = bb;
		}

		public PatchCrumb(final int x, final int y, final int z) {
			this.posX = x;
			this.posY = y;
			this.posZ = z;
			this.direction = Direction.NORTHSOUTH;
			this.expiresAt = System.currentTimeMillis() + time * 1000;
			this.boundingBOX = new AxisAlignedBB(x + 0.5, y + 0.5, z + 0.5, x - 0.5, y - 0.5, z - 0.5);
		}
	}

	public class TNTCrumb {
		public int x;
		public int y;
		public int z;
		public long expireTime;
		public long expireChecks;
		public double posX;
		public double posZ;
		public double posY;
		public double lastX;
		public double lastZ;
		public int amount;
		public AxisAlignedBB boundingBOX;
		public Entity entity;

		public TNTCrumb(final EntityTNTPrimed tnt, final int timeout) {
			this.amount = 0;
			this.entity = (Entity) tnt;
			this.x = (int) Math.ceil(tnt.posX);
			this.y = (int) Math.ceil(tnt.posY) + 1;
			this.z = (int) Math.ceil(tnt.posZ);
			this.posX = tnt.posX;
			this.posZ = tnt.posZ;
			this.posY = tnt.posY;
			this.lastX = tnt.prevPosX;
			this.lastZ = tnt.prevPosZ;
			this.boundingBOX = tnt.getEntityBoundingBox();
			this.expireTime = System.currentTimeMillis() + timeout * 1000;
			this.expireChecks = System.currentTimeMillis() + tnt.fuse * 50;
		}

		public TNTCrumb(final int xx, final double yy, final int zz, final int timeout) {
			this.amount = 0;
			this.x = (int) Math.ceil(xx);
			this.y = (int) Math.ceil(yy) + 1;
			this.z = (int) Math.ceil(zz);
			this.posX = xx;
			this.posZ = zz;
			this.posY = yy;
			this.lastX = -1.0;
			this.lastZ = -1.0;
			this.boundingBOX = new AxisAlignedBB(xx + 0.5, yy + 0.5, zz + 0.5, xx - 0.5, yy - 0.5, zz - 0.5);
			this.expireTime = System.currentTimeMillis() + timeout * 1000;
		}

		public TNTCrumb(final int xx, final double yy, final int zz, final int timeout, final AxisAlignedBB box) {
			this.amount = 0;
			this.x = (int) Math.ceil(xx);
			this.y = (int) Math.ceil(yy) + 1;
			this.z = (int) Math.ceil(zz);
			this.posX = xx;
			this.posZ = zz;
			this.posY = yy;
			this.lastX = -1.0;
			this.lastZ = -1.0;
			this.boundingBOX = box;
			this.expireTime = System.currentTimeMillis() + timeout * 1000;
		}

		public void increaseAmount() {
			++this.amount;
		}
	}

	public class TNTSpot {
		public double x;
		public double y;
		public double z;
		public long fuseExpires;

		public TNTSpot(final EntityTNTPrimed tntPrimed) {
			this.x = tntPrimed.posX;
			this.y = tntPrimed.posY;
			this.z = tntPrimed.posZ;
			this.fuseExpires = System.currentTimeMillis() + tntPrimed.fuse * 50;
		}
	}

	public enum Direction {
		NORTHSOUTH, EASTWEST;
	}
}
