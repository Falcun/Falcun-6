package net.mattbenson.modules.types.factions;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.mattbenson.Falcun;
import net.mattbenson.Wrapper;
import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.types.render.RenderEvent;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.utils.DrawUtils;
import net.mattbenson.utils.legacy.LessArrayLists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class PatchcrumbsOld extends Module {

	public static final int MAX_LIGHT_X = 0xF000F0;
	public static final int MAX_LIGHT_Y = 0xF000F0;

	private Minecraft mc = Minecraft.getMinecraft();

	public static List<BreadCrumbTNT> breadCrumbTNTs = new ArrayList();
	public static List<BreadCrumbFall> breadCrumbFALLs = new ArrayList();

	@ConfigValue.Boolean(name = "Track TNT")
	public static boolean tnt = true;

	@ConfigValue.Boolean(name = "Track Sand")
	public static boolean sand = true;

	@ConfigValue.Color(name = "TNT Color")
	public Color tColor = Color.RED;

	@ConfigValue.Color(name = "Sand Color")
	public Color sColor = Color.YELLOW;

	@ConfigValue.Color(name = "Font Color")
	public Color color = Color.WHITE;
	
	@ConfigValue.Keybind(name = "Shout Sand Coordinates Key")
	public int keyBind = 0;
	
	@ConfigValue.Keybind(name = "Shout TNT Coordinates Key")
	public int keyBind2 = 0;
	
	@ConfigValue.Text(name = "Chat Format")
	public static String chatFormat = "X: {x}, Y: {y}, Z: {z}";

	@ConfigValue.Integer(name = "Display Timer (sec)", min = 1, max = 60)
	public static int age = 15;

	@ConfigValue.Float(name = "Line Width", min = 0.1F, max = 7)
	public static float lineWidth = 1;

	@ConfigValue.Double(name = "Font Scale", min = 0.5, max = 2.0)
	public static double fontScale = 1.0;

	@ConfigValue.List(name = "Side", values = {"North/South", "East/West", "Both"})
	private String mode = "North/South";

	public static float currentX = -1;
	public static float currentY = -1;
	public static float currentZ = -1;

	public static float currentSandX = -1;
	public static float currentSandY = -1;
	public static float currentSandZ = -1;

	private HUDElement hud;
	
	public PatchcrumbsOld() {
		super("Patchcrumbs OLD", ModuleCategory.FACTIONS);

		hud = new HUDElement("patchcrumbsold", 1, 175) {
			@Override
			public void onRender() {
				render();
			}
		};
		
		addHUD(hud);
		
		Falcun.getInstance().EVENT_BUS.register(new LessArrayLists());
	}
	
	@Override
	public void onEnable() {
		Wrapper.getInstance().addChat("It is recommended to use the module 'Patchcrumbs'");
	}

	@Override
	public void onDisable() {
		breadCrumbTNTs.clear();
		breadCrumbFALLs.clear();
		currentX = -1;
		currentY = -1;
		currentZ = -1;
		currentSandX = -1;
		currentSandY = -1;
		currentSandZ = -1;
	}

	private int width = 20;
	private int height = 30;


	@SubscribeEvent 
	public void onKeyPress(KeyInputEvent event)
	{
		if(!this.enabled) return;
		if(this.mc == null || this.mc.thePlayer == null || this.mc.theWorld == null) return;
		if (Keyboard.getEventKey() == keyBind2 && keyBind2 != 0 && Keyboard.getEventKeyState())
		{
			if (currentX != -1 && currentY != -1 && currentZ != -1)
			{
				String toSend = chatFormat.replace("{x}",(int) currentX+"")
						.replace("{y}",(int) currentY+"")
						.replace("{z}",(int) currentZ+"");
				mc.thePlayer.sendChatMessage(toSend);
			} else {
				Wrapper.getInstance().addChat("No shot found yet!");
			}
		}
		if (Keyboard.getEventKey() == keyBind && keyBind != 0 && Keyboard.getEventKeyState())
		{
			if (currentSandX != -1 && currentSandY != -1 && currentSandZ != -1)
			{
				String toSend = chatFormat.replace("{x}",(int) currentSandX+"")
						.replace("{y}",(int) currentSandY+"")
						.replace("{z}",(int) currentSandZ+"");
				mc.thePlayer.sendChatMessage(toSend);
			} else {
				Wrapper.getInstance().addChat("No shot found yet!");
			}
		}
	}
	
	private void render() {
		if (this.enabled) {
			
			int posX = hud.getX();
			int posY = hud.getY();
			
			if((currentX != -1 && currentY != -1 && currentZ != -1) || (currentSandX != -1 && currentSandY != -1 && currentSandZ != -1) ) {
				GL11.glPushMatrix();

				if(color.getBlue() == 5 && color.getRed() == 5 && color.getGreen() == 5) {
					if(tnt && !sand) {
						DrawUtils.drawChromaString("TNT X: " + currentX, posX, posY + 1, true ,true);
						DrawUtils.drawChromaString("TNT Y: " + currentY, posX, posY + 11, true ,true);
						DrawUtils.drawChromaString("TNT Z: " + currentZ, posX, posY + 21, true ,true);
					}
					else if(sand && !tnt) {
						DrawUtils.drawChromaString("Sand X: " + currentSandX, posX, posY + 1, true ,true);
						DrawUtils.drawChromaString("Sand Y: " + currentSandY, posX, posY + 11, true ,true);
						DrawUtils.drawChromaString("Sand Z: " + currentSandZ, posX, posY + 21, true ,true);
					}else if(sand && tnt) {
						DrawUtils.drawChromaString("TNT X: " + currentX, posX, posY + 1, true ,true);
						DrawUtils.drawChromaString("TNT Y: " + currentY, posX, posY + 11, true ,true);
						DrawUtils.drawChromaString("TNT Z: " + currentZ, posX, posY + 21, true ,true);
						DrawUtils.drawChromaString("Sand X: " + currentSandX, posX, posY + 31, true ,true);
						DrawUtils.drawChromaString("Sand Y: " + currentSandY, posX, posY + 41, true ,true);
						DrawUtils.drawChromaString("Sand Z: " + currentSandZ, posX, posY + 51, true ,true);
					}
				} else if(color.getBlue() == 6 && color.getRed() == 6 && color.getGreen() == 6) {
					if(tnt && !sand) {
						DrawUtils.drawChromaString("TNT X: " + currentX, posX, posY + 1, false ,true);
						DrawUtils.drawChromaString("TNT Y: " + currentY, posX, posY + 11, false ,true);
						DrawUtils.drawChromaString("TNT Z: " + currentZ, posX, posY + 21, false ,true);
					}
					else if(sand && !tnt) {
						DrawUtils.drawChromaString("Sand X: " + currentSandX, posX, posY + 1, false ,true);
						DrawUtils.drawChromaString("Sand Y: " + currentSandY, posX, posY + 11, false ,true);
						DrawUtils.drawChromaString("Sand Z: " + currentSandZ, posX, posY + 21, false ,true);
					}else if(sand && tnt) {
						DrawUtils.drawChromaString("TNT X: " + currentX, posX, posY + 1, false ,true);
						DrawUtils.drawChromaString("TNT Y: " + currentY, posX, posY + 11, false ,true);
						DrawUtils.drawChromaString("TNT Z: " + currentZ, posX, posY + 21, false ,true);
						DrawUtils.drawChromaString("Sand X: " + currentSandX, posX, posY + 31, false ,true);
						DrawUtils.drawChromaString("Sand Y: " + currentSandY, posX, posY + 41, false ,true);
						DrawUtils.drawChromaString("Sand Z: " + currentSandZ, posX, posY + 51, false ,true);
					}
				} else {
					if(tnt && !sand) {
						mc.fontRendererObj.drawStringWithShadow("TNT X: " + currentX, (float) posX, (float)posY + 1, color.getRGB());
						mc.fontRendererObj.drawStringWithShadow("TNT Y: " + currentY, (float) posX, (float)posY + 11, color.getRGB());
						mc.fontRendererObj.drawStringWithShadow("TNT Z: " + currentZ, (float) posX, (float)posY + 21, color.getRGB());
					} else if(sand && !tnt) {
						mc.fontRendererObj.drawStringWithShadow("Sand X: " + currentSandX, (float) posX, (float)posY + 1, color.getRGB());
						mc.fontRendererObj.drawStringWithShadow("Sand Y: " + currentSandY, (float) posX, (float)posY + 11, color.getRGB());
						mc.fontRendererObj.drawStringWithShadow("Sand Z: " + currentSandZ, (float) posX, (float)posY + 21, color.getRGB());
					} else if(sand && tnt) {
						mc.fontRendererObj.drawStringWithShadow("TNT X: " + currentX, (float) posX, (float)posY + 1, color.getRGB());
						mc.fontRendererObj.drawStringWithShadow("TNT Y: " + currentY, (float) posX, (float)posY + 11, color.getRGB());
						mc.fontRendererObj.drawStringWithShadow("TNT Z: " + currentZ, (float) posX, (float)posY + 21, color.getRGB());
						mc.fontRendererObj.drawStringWithShadow("Sand X: " + currentSandX, (float) posX, (float)posY + 31, color.getRGB());
						mc.fontRendererObj.drawStringWithShadow("Sand Y: " + currentSandY, (float) posX, (float)posY + 41, color.getRGB());
						mc.fontRendererObj.drawStringWithShadow("Sand Z: " + currentSandZ, (float) posX, (float)posY + 51, color.getRGB());
					}
				}

				if(tnt && sand) {
					hud.setHeight(60);
				} else {
					hud.setHeight(30);
				}
				hud.setWidth(mc.fontRendererObj.getStringWidth("TNT X: " + currentX));

				GL11.glScaled(1, 1, 1);
				GL11.glColor3f(1, 1, 1);
				GL11.glPopMatrix();
			}
		}
	}

	@SubscribeEvent
	public void onTick(TickEvent.PlayerTickEvent event){
		if(!this.enabled) return;

		if(Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {

			for (int i = 0; i < breadCrumbFALLs.size(); i++) {
				BreadCrumbFall bcf = breadCrumbFALLs.get(i);
				if(bcf != null) {
					if (bcf.hasReached((long) (age * 1000))){
						breadCrumbFALLs.remove(bcf);
						currentSandX = -1;
						currentSandY = -1;
						currentSandZ = -1;
					}
				}
			}
			for (int i = 0; i < breadCrumbTNTs.size(); i++) {

				BreadCrumbTNT bct = breadCrumbTNTs.get(i);
				if(bct != null) {
					if (bct.hasReached((long) (age * 1000))){
						breadCrumbTNTs.remove(bct);
						currentX = -1;
						currentY = -1;
						currentZ = -1;
					}
				}
			} 

			List<Entity> entityList = new ArrayList();
			entityList.addAll(mc.theWorld.getLoadedEntityList());

			for (Entity o : entityList){
				if (o instanceof EntityTNTPrimed){
					EntityTNTPrimed etp = (EntityTNTPrimed) o;
					boolean has = false;
					BreadCrumbTNT found = null;
					for (BreadCrumbTNT bct : breadCrumbTNTs){
						if (bct.etp == etp){
							found = bct;
							has = true;
						}
					}
					if(currentX == -1 && currentY == -1 && currentZ == -1) {
						currentX = (float) etp.posX;
						currentY = (float) etp.posY;
						currentZ = (float) etp.posZ;
					}
					if (has){
						found.positions.add(new double[]{found.etp.posX, found.etp.posY + 0.5, found.etp.posZ});
					} else {
						breadCrumbTNTs.add(new BreadCrumbTNT(etp, new ArrayList<double[]>(), new double[] {etp.posX, etp.posY + 0.5, etp.posZ}));
					}
				} else if (o instanceof EntityFallingBlock){

					EntityFallingBlock etp = (EntityFallingBlock) o;
					boolean has = false;
					BreadCrumbFall found = null;
					for (BreadCrumbFall bct : breadCrumbFALLs){
						if (bct.etp == etp){
							found = bct;
							has = true;
						}
					}
					if(currentSandX == -1 && currentSandY == -1 && currentSandZ == -1) {
						currentSandX = (float) etp.posX;
						currentSandY = (float) etp.posY;
						currentSandZ = (float) etp.posZ;
					}
					if (has){
						found.positions.add(new double[]{found.etp.posX, found.etp.posY + 0.5, found.etp.posZ});
					} else {
						breadCrumbFALLs.add(new BreadCrumbFall(etp, new ArrayList<double[]>(), new double[] {etp.posX, etp.posY + 0.5, etp.posZ}));
					}

				}
			}
		}
	}

	@SubscribeEvent
	public void renderworldlast(RenderEvent event) {
		if(!this.enabled) return;

		if ( mc.theWorld != null)
		{
			if(currentX != -1 && currentY != -1 && currentZ != -1) {

				float[] colors = new float[] { tColor.getRed() / 255.0f ,tColor.getGreen()/ 255.0f,tColor.getBlue()/ 255.0f,tColor.getAlpha() / 255.0f};
				float[] colorSand = new float[] { sColor.getRed() / 255.0f ,sColor.getGreen()/ 255.0f,sColor.getBlue()/ 255.0f,sColor.getAlpha() / 255.0f};

				GL11.glPushMatrix();

				if(tnt) {
					renderLaser(currentX, 1, currentZ,currentX, currentY+0.5, currentZ, 0, colors[3],lineWidth / 10, colors);
					if(mode.contains("East") || mode.contains("Both"))
						renderLaser(currentX - 500, currentY+0.5, currentZ, currentX + 500, currentY+0.5, currentZ, 0,  colors[3],lineWidth / 10, colors);
					if(mode.contains("North") || mode.contains("Both"))
						renderLaser(currentX, currentY+0.5, currentZ - 500, currentX, currentY+0.5, currentZ + 500, 0,  colors[3], lineWidth / 10, colors);
					GL11.glPopMatrix();
				}
				if(sand) {
					renderLaser(currentSandX, 1, currentSandZ,currentSandX, currentSandY+0.5, currentSandZ, 0,  colorSand[3], lineWidth / 10, colorSand);
					if(mode.contains("East") || mode.contains("Both"))
						renderLaser(currentSandX - 500, currentSandY+0.5, currentSandZ, currentSandX + 500, currentSandY+0.5, currentSandZ,  0, colorSand[3],lineWidth / 10, colorSand);
					if(mode.contains("North") || mode.contains("Both"))
						renderLaser(currentSandX, currentSandY+0.5, currentSandZ - 500, currentSandX, currentSandY+0.5, currentSandZ + 500, 0, colorSand[3], lineWidth / 10, colorSand);
					GL11.glPopMatrix();
				}
			}
		}
	}

	class BreadCrumbTNT {
		public EntityTNTPrimed etp;
		public List<double[]> positions;
		public double[] startPos;

		private long lastMS = getCurrentMS();

		public long getCurrentMS()
		{
			return System.nanoTime() / 1000000L;
		}

		public long getLastMS()
		{
			return this.lastMS;
		}

		public boolean hasReached(long milliseconds)
		{
			return getCurrentMS() - this.lastMS >= milliseconds;
		}

		public void reset()
		{
			this.lastMS = getCurrentMS();
		}

		public void setLastMS(long currentMS)
		{
			this.lastMS = currentMS;
		}

		public long getTimeSinceReset()
		{
			return getCurrentMS() - this.lastMS;
		}

		public BreadCrumbTNT(EntityTNTPrimed etpp, List<double[]> positions, double[] startPos){
			this.etp = etpp;
			this.positions = positions;
			this.startPos = startPos;
			this.reset();
		}
	}

	class BreadCrumbFall {
		public EntityFallingBlock etp;
		public List<double[]> positions;
		public double[] startPos;

		private long lastMS = getCurrentMS();

		public long getCurrentMS()
		{
			return System.nanoTime() / 1000000L;
		}

		public long getLastMS()
		{
			return this.lastMS;
		}

		public boolean hasReached(long milliseconds)
		{
			return getCurrentMS() - this.lastMS >= milliseconds;
		}

		public void reset()
		{
			this.lastMS = getCurrentMS();
		}

		public void setLastMS(long currentMS)
		{
			this.lastMS = currentMS;
		}

		public long getTimeSinceReset()
		{
			return getCurrentMS() - this.lastMS;
		}

		public BreadCrumbFall(EntityFallingBlock etpp, List<double[]> positions, double[] startPos){
			this.etp = etpp;
			this.positions = positions;
			this.startPos = startPos;
			this.reset();
		}

	}
	public static void renderLaser(double firstX, double firstY, double firstZ, double secondX, double secondY, double secondZ, double rotationTime, float alpha, double beamWidth, float[] color) {
		Tessellator tessy = Tessellator.getInstance();
		WorldRenderer render = tessy.getWorldRenderer();
		World world = Minecraft.getMinecraft().theWorld;
		float r = color[0];
		float g = color[1];
		float b = color[2];

		Vec3 vec1 = new Vec3(firstX, firstY, firstZ);
		Vec3 vec2 = new Vec3(secondX, secondY, secondZ);
		Vec3 combinedVec = vec2.subtract(vec1);

		double rot = rotationTime > 0 ? 360D * (world.getTotalWorldTime() % rotationTime / rotationTime) : 0;
		double pitch = Math.atan2(combinedVec.yCoord, Math.sqrt(combinedVec.xCoord * combinedVec.xCoord + combinedVec.zCoord * combinedVec.zCoord));
		double yaw = Math.atan2(-combinedVec.zCoord, combinedVec.xCoord);

		double length = combinedVec.lengthVector();

		GlStateManager.pushMatrix();
		GlStateManager.disableDepth();
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		int func = GL11.glGetInteger(GL11.GL_ALPHA_TEST_FUNC);
		float ref = GL11.glGetFloat(GL11.GL_ALPHA_TEST_REF);
		GlStateManager.alphaFunc(GL11.GL_ALWAYS, 0);
		GlStateManager.translate(firstX - TileEntityRendererDispatcher.staticPlayerX, firstY - TileEntityRendererDispatcher.staticPlayerY, firstZ - TileEntityRendererDispatcher.staticPlayerZ);
		GlStateManager.rotate((float) (180 * yaw / Math.PI), 0, 1, 0);
		GlStateManager.rotate((float) (180 * pitch / Math.PI), 0, 0, 1);
		GlStateManager.rotate((float) rot, 1, 0, 0);
		GlStateManager.disableTexture2D();


		render.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		for (double i = 0; i < 4; i++) {
			double width = beamWidth * (i / 4.0);
			render.pos(length, width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
			render.pos(0, width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
			render.pos(0, -width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
			render.pos(length, -width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();

			render.pos(length, -width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
			render.pos(0, -width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
			render.pos(0, width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
			render.pos(length, width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();

			render.pos(length, width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
			render.pos(0, width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
			render.pos(0, width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
			render.pos(length, width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();

			render.pos(length, -width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
			render.pos(0, -width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
			render.pos(0, -width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
			render.pos(length, -width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
		}
		tessy.draw();

		GlStateManager.enableTexture2D();
		GlStateManager.alphaFunc(func, ref);
		GlStateManager.blendFunc(GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE_MINUS_DST_ALPHA);
		GlStateManager.disableBlend();
		//GlStateManager.enableLighting();
		GL11.glColor4f(1, 1, 1,1);
		GlStateManager.enableDepth();
		GlStateManager.popMatrix();
	}
}
