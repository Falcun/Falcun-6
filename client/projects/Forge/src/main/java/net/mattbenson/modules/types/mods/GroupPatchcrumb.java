package net.mattbenson.modules.types.mods;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.GL11;

import net.mattbenson.Falcun;
import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.render.RenderEvent;
import net.mattbenson.events.types.render.RenderType;
import net.mattbenson.events.types.world.OnTickEvent;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.modules.types.factions.Patchcrumbs;
import net.mattbenson.network.common.GroupData;
import net.mattbenson.utils.Timer;
import net.mattbenson.utils.legacy.GlShim;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;

public class GroupPatchcrumb extends Module {
	public Timer timer = new Timer();

	@ConfigValue.Integer(name = "Display Timer (sec)", min = 1, max = 60)
	public int age = 5;

	@ConfigValue.Boolean(name = "Send Chat Notification")
	public boolean isNotifications = true;

	@ConfigValue.Color(name = "Patch Line Color")
	public Color patchColor = Color.ORANGE;

	public AxisAlignedBB boundingBOX;
	public List<Crumb> crumbs = new CopyOnWriteArrayList<>();

	public GroupPatchcrumb() {
		super("Group Patchcrumbs", ModuleCategory.GROUPS);
		setEnabled(true);
	}

	class Crumb {
		private final double x;
		private final double y;
		private final double z;
		private final String server;
		private final GroupData group;
		private final long currentMS;
		private final String minX;
		private final String minY;
		private final String minZ;
		private final String maxX;
		private final String maxY;
		private final String maxZ;

		public Crumb(double x, double y, double z, String minX, String minY, String minZ, String maxX, String maxY,
				String maxZ, GroupData group, String server) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.minX = minX;
			this.minY = minY;
			this.minZ = minZ;
			this.maxX = maxX;
			this.maxY = maxY;
			this.maxZ = maxZ;
			this.group = group;
			this.server = server;
			currentMS = System.currentTimeMillis();
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

		public double getZ() {
			return z;
		}

		public String getMinX() {
			return minX;
		}

		public String getMinY() {
			return minY;
		}

		public String getMinZ() {
			return minZ;
		}

		public String getMaxX() {
			return maxX;
		}

		public String getMaxY() {
			return maxY;
		}

		public String getMaxZ() {
			return maxZ;
		}

		public String getServer() {
			return server;
		}

		public GroupData getGroup() {
			return group;
		}

		public boolean hasReached(long ms) {
			return System.currentTimeMillis() - currentMS >= ms;
		}
	}

	public void addCrumb(double x, double y, double z, String minX, String minY, String minZ, String maxX, String maxY,
			String maxZ, GroupData group, String server) {
		if (crumbs.stream().anyMatch(x1 -> x1.server.equals(server)))
			return;
		crumbs.removeIf(x1 -> x1.server.equals(server));
		crumbs.add(new Crumb(x, y, z, minX, minY, minZ, maxX, maxY, maxZ, group, server));
	}

	public void updateCrumbs() {
		crumbs.removeIf(x -> x.hasReached(age * 1000L));
	}

	@SubscribeEvent
	public void onTick(OnTickEvent event) {
		if (mc.theWorld != null) {
			updateCrumbs();
		}
	}

	@SubscribeEvent
	public void renderworldlast(RenderEvent event) {
		if(event.getRenderType() != RenderType.WORLD) {
			return;
		}
		
		crumbs.forEach(crumb -> {
			patchFinder(crumb.getX(), crumb.getY(), crumb.getZ(), crumb.getMinX(), crumb.getMinY(), crumb.getMinZ(),
					crumb.getMaxX(), crumb.getMaxY(), crumb.getMaxZ());
		});

	}

	public void patchFinder(double x, double y, double z, String minX, String minY, String minZ, String maxX,
			String maxY, String maxZ) {
		Patchcrumbs patchcrumbs = Falcun.getInstance().moduleManager.getModule(Patchcrumbs.class);
		
		double crumbMinX = Double.parseDouble(minX);
		double crumbMinY = Double.parseDouble(minY);
		double crumbMinZ = Double.parseDouble(minZ);
		double crumbMaxX = Double.parseDouble(maxX);
		double crumbMaxY = Double.parseDouble(maxY);
		double crumbMaxZ = Double.parseDouble(maxZ);

		GlStateManager.pushMatrix();
		GlShim.glBlendFunc(770, 771);
		GlShim.glEnable(3042);
		GlShim.glLineWidth((float) patchcrumbs.width);
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
		final int extender = 150;

		// DEBUG
		// System.out.println("MinX: " + crumbMinX + ":" + boundingBOX.minX);
		// System.out.println("MinY: " + crumbMinY + ":" + boundingBOX.minY);
		// System.out.println("MinZ: " + crumbMinZ + ":" + boundingBOX.minZ);
		// System.out.println("MaxX: " + crumbMaxX + ":" + boundingBOX.maxX);
		// System.out.println("MaxY: " + crumbMaxY + ":" + boundingBOX.maxY);
		// System.out.println("MaxZ: " + crumbMaxZ + ":" + boundingBOX.maxZ);

		double crumbposX = x;
		double crumbposY = y;
		double crumbposZ = z;

		if (patchcrumbs.mode.equalsIgnoreCase("North/South")) {
			final AxisAlignedBB box = new AxisAlignedBB(crumbMinX - 0.05 - crumbposX + (crumbposX - vX),
					crumbMinY - crumbposY + (crumbposY - vY), vZ - extender - 0.05 - crumbposZ + (crumbposZ - vZ),
					crumbMaxX + 0.05 - crumbposX + (crumbposX - vX), crumbMaxY + 0.1 - crumbposY + (crumbposY - vY),
					vZ + extender + 0.05 - crumbposZ + (crumbposZ - vZ));
			patchcrumbs.drawSelectionBoundingBox(box);
		} else if (patchcrumbs.mode.equalsIgnoreCase("East/West")) {
			final AxisAlignedBB box = new AxisAlignedBB(vX - extender - 0.05 - crumbposX + (crumbposX - vX),
					crumbMinY - crumbposY + (crumbposY - vY), crumbMinZ - 0.05 - crumbposZ + (crumbposZ - vZ),
					vX + extender + 0.05 - crumbposX + (crumbposX - vX), crumbMaxY + 0.1 - crumbposY + (crumbposY - vY),
					crumbMaxZ + 0.05 - crumbposZ + (crumbposZ - vZ));
			patchcrumbs.drawSelectionBoundingBox(box);
		} else {
			final AxisAlignedBB bX = new AxisAlignedBB(vX - extender - 0.05 - crumbposX + (crumbposX - vX),
					crumbMinY - crumbposY + (crumbposY - vY), crumbMinZ - 0.05 - crumbposZ + (crumbposZ - vZ),
					vX + extender + 0.05 - crumbposX + (crumbposX - vX), crumbMaxY + 0.1 - crumbposY + (crumbposY - vY),
					crumbMaxZ + 0.05 - crumbposZ + (crumbposZ - vZ));
			patchcrumbs.drawSelectionBoundingBox(bX);
			final AxisAlignedBB bZ = new AxisAlignedBB(crumbMinX - 0.05 - crumbposX + (crumbposX - vX),
					crumbMinY - crumbposY + (crumbposY - vY), vZ - extender - 0.05 - crumbposZ + (crumbposZ - vZ),
					crumbMaxX + 0.05 - crumbposX + (crumbposX - vX), crumbMaxY + 0.1 - crumbposY + (crumbposY - vY),
					vZ + extender + 0.05 - crumbposZ + (crumbposZ - vZ));
			patchcrumbs.drawSelectionBoundingBox(bZ);
		}

		GlShim.glEnable(3553);
		GlShim.glEnable(2929);
		GlShim.glDepthMask(true);
		GlShim.glDisable(3042);
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		GlStateManager.popMatrix();
	}
}
