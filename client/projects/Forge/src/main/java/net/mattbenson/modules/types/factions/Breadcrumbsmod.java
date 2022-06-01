package net.mattbenson.modules.types.factions;


import java.awt.Color;


import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import org.lwjgl.opengl.GL11;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.types.network.IngoingPacketEvent;
import net.mattbenson.events.types.render.RenderEvent;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.world.OnTickEvent;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.utils.BreadcrumbExplosion;
import net.mattbenson.utils.Timer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.sound.PlaySoundEvent;

public class Breadcrumbsmod extends Module {
	public Breadcrumbsmod() {
		super("Breadcrumbs", ModuleCategory.FACTIONS);
	}
	
	private Minecraft mc = Minecraft.getMinecraft();

	Set<BreadCrumbTNT> breadCrumbTNTs = Collections.newSetFromMap(new IdentityHashMap<BreadCrumbTNT, Boolean>());
	Set<BreadCrumbFall> breadCrumbFALLs = Collections.newSetFromMap(new IdentityHashMap<BreadCrumbFall, Boolean>());
	public static Set<BreadcrumbExplosion> breadCrumbExplosions = Collections.newSetFromMap(new IdentityHashMap<BreadcrumbExplosion, Boolean>());
	
	Timer timer = new Timer();

	@ConfigValue.Boolean(name = "Visualize Explosions")
    public boolean visualized = true;
	
	@ConfigValue.Integer(name = "Display Timer (sec)", min = 1, max = 60)
	public int age = 15;
	
	@ConfigValue.Boolean(name = "Track TNT")
	public boolean tnt = true;
	
	@ConfigValue.Color(name = "TNT Color")
	public Color vColor = Color.RED;
	
	@ConfigValue.Color(name = "Sand Color")
	public Color sandColor = Color.YELLOW;
	
	@ConfigValue.Color(name = "Explosion Color")
	public Color explosionColor = Color.RED;
	
	@ConfigValue.Boolean(name = "Track Sand")
	public boolean sand = true;
	
	@ConfigValue.Float(name = "Line Width", min = 0.1F, max = 5.0F)
	public float lineWidth = 1;
	
	@ConfigValue.Boolean(name = "Disable If Near Dispensers")
	public static boolean dispenserChec = true;
	
	@ConfigValue.List(name = "Render Mode", values = {"Points", "Line", "Line Strip", "Line Loop"}) 
	public String mode = "Line";

	boolean isUsingTNTChroma = false;
	boolean isUsingSandChroma = false;

	@Override
	public void onEnable() {
		breadCrumbTNTs.clear();
		breadCrumbFALLs.clear();
		breadCrumbExplosions.clear();
		timer.reset();
	}

	@Override
	public void onDisable() {
		breadCrumbTNTs.clear();
		breadCrumbFALLs.clear();
		breadCrumbExplosions.clear();
		timer.reset();
	}

	@SubscribeEvent
	public void onTick(OnTickEvent event) {
		if(!this.enabled) return;
		if(!tnt && !sand) return;
		if(Minecraft.getMinecraft().thePlayer == null) return;
		if(Minecraft.getMinecraft().theWorld == null) return;

		if(timer.hasReached(1000)) {
			Iterator<BreadCrumbFall> iteratorFall = breadCrumbFALLs.iterator();
			Iterator<BreadCrumbTNT> iteratorTNT = breadCrumbTNTs.iterator();
			while(iteratorFall.hasNext()) {
				BreadCrumbFall bcf = iteratorFall.next();
				if(bcf != null && bcf.hasReached((long) (age * 1000)) ){
					iteratorFall.remove();
				}
			}
			while(iteratorTNT.hasNext()) {
				BreadCrumbTNT bct = iteratorTNT.next();
				if(bct != null && bct.hasReached((long) (age * 1000))){
					iteratorTNT.remove();
				}
			}
			timer.reset();
		}
		
		Iterator<Entity> iterator = mc.theWorld.getLoadedEntityList().iterator();

		while(iterator.hasNext()) {
			Entity entity = iterator.next();
			if(entity instanceof EntityTNTPrimed && tnt) {
				EntityTNTPrimed etp = (EntityTNTPrimed) entity;
				if(isDispenserNear(etp.getPosition())) return;
                boolean has = false;
                BreadCrumbTNT found = null;
            	Iterator<BreadCrumbTNT> iteratorTNT = breadCrumbTNTs.iterator();
            	while(iteratorTNT.hasNext()) {
            		BreadCrumbTNT bct = iteratorTNT.next();
            		if (bct.etp == etp){
                        found = bct;
                        has = true;
                    }
            	}
                if (has){
                    found.positions.add(new double[]{found.etp.posX, found.etp.posY, found.etp.posZ});
                } else {
                    breadCrumbTNTs.add(new BreadCrumbTNT(etp, new ArrayList<double[]>(), new double[] {etp.posX, etp.posY, etp.posZ}));
                }	
			} else if(entity instanceof EntityFallingBlock && sand) {
				EntityFallingBlock etp = (EntityFallingBlock) entity;
				if(isDispenserNear(etp.getPosition())) return;
                boolean has = false;
                BreadCrumbFall found = null;
                Iterator<BreadCrumbFall> iteratorFall = breadCrumbFALLs.iterator();
            	while(iteratorFall.hasNext()) {
            		BreadCrumbFall bct = iteratorFall.next();
            		if (bct.etp == etp){
                        found = bct;
                        has = true;
                    }
            	}
                if (has){
                    found.positions.add(new double[]{found.etp.posX, found.etp.posY, found.etp.posZ});
                } else {
                    breadCrumbFALLs.add(new BreadCrumbFall(etp, new ArrayList<double[]>(), new double[] {etp.posX, etp.posY, etp.posZ}));
                }
			}
		}


	}

	public void render2DEsp(double pX, double pY, double pZ) {
    
        int red = (int) (explosionColor.getRed() / 255.0F);
        int green = (int) (explosionColor.getGreen() / 255.0F);
        int blue = (int) (explosionColor.getBlue() / 255.0F);
        int alpha = (int) (explosionColor.getAlpha() / 255.0F);
        
        double renderPosX = mc.getRenderManager().viewerPosX;
		double renderPosY = mc.getRenderManager().viewerPosY;
		double renderPosZ = mc.getRenderManager().viewerPosZ;

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.disableTexture2D();
		GlStateManager.disableDepth();
		GlStateManager.disableAlpha();
		GL11.glLineWidth(lineWidth);
		GL11.glColor4f(red, green, blue, alpha);
		BlockPos pos = new BlockPos(pX,pY,pZ);
		Block bloc = mc.theWorld.getBlockState(pos).getBlock();
		AxisAlignedBB bb = bloc.getSelectedBoundingBox(mc.theWorld, pos).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-renderPosX, -renderPosY, -renderPosZ);
		RenderGlobal.drawSelectionBoundingBox(bb);
		GL11.glLineWidth(1F);
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GlStateManager.enableAlpha();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
    }
	
	@SubscribeEvent
	public void onPacket(IngoingPacketEvent event) 
	{
		if(!this.enabled) return;
		if(Minecraft.getMinecraft().thePlayer == null) return;
		if(Minecraft.getMinecraft().theWorld == null) return;
		if(!this.visualized) return;
		
		if (event.getPacket() instanceof S29PacketSoundEffect) {
			if(((S29PacketSoundEffect) event.getPacket()).getSoundName().toLowerCase().contains("explode")) {
				if(this.isDispenserNear(new BlockPos(((S29PacketSoundEffect) event.getPacket()).getX(), ((S29PacketSoundEffect) event.getPacket()).getY(),((S29PacketSoundEffect) event.getPacket()).getZ()))) return;
				breadCrumbExplosions.add(new BreadcrumbExplosion(((S29PacketSoundEffect) event.getPacket()).getX(),((S29PacketSoundEffect) event.getPacket()).getY(),((S29PacketSoundEffect) event.getPacket()).getZ()));	
			}
			
		}
		
	}
	
	@SubscribeEvent
	public void renderworldlast(RenderEvent event) {

		if(!this.enabled) return;

		if ( mc.theWorld != null)
		{
			float f = event.partialTicks;
			float px = (float)mc.thePlayer.posX;
			float py = (float)mc.thePlayer.posY;
			float pz = (float)mc.thePlayer.posZ;
			float mx = (float)mc.thePlayer.prevPosX;
			float my = (float)mc.thePlayer.prevPosY;
			float mz = (float)mc.thePlayer.prevPosZ;
			float dx = mx + ( px - mx ) * f;
			float dy = my + ( py - my ) * f;
			float dz = mz + ( pz - mz ) * f;

			Iterator<BreadCrumbTNT> iteratorTNT = breadCrumbTNTs.iterator();
			Iterator<BreadCrumbFall> iteratorFall = breadCrumbFALLs.iterator();
			Iterator<BreadcrumbExplosion> iteratorExplosion = breadCrumbExplosions.iterator();
		
			if (visualized) {
				while(iteratorExplosion.hasNext()) {
					BreadcrumbExplosion be = iteratorExplosion.next();
					if (be.isDone()) {
						iteratorExplosion.remove();
	                } else {
	                    GL11.glPushMatrix();;
	                    render2DEsp(be.posX, be.posY, be.posZ);
	                    GL11.glPopMatrix();
	                }
				}
	        }
			
			if (tnt){
				while(iteratorTNT.hasNext()) {
					BreadCrumbTNT bct = iteratorTNT.next();
					GL11.glPushMatrix();
					GL11.glDisable(3553);
					GL11.glDisable(2896);
					GL11.glEnable(3042);
					GL11.glBlendFunc(770, 771);
					GL11.glDisable(2929);
					GL11.glEnable(2848);
					GL11.glDepthMask(false);
					GL11.glColor4d(vColor.getRed()/255.0F, vColor.getGreen()/255.0F, vColor.getBlue()/255.0F, vColor.getAlpha()/255.0F);
					GL11.glLineWidth(lineWidth);
					if(mode.equalsIgnoreCase("line")) {
						GL11.glBegin(GL11.GL_LINES);
					}
					if(mode.equalsIgnoreCase("points")) {
						GL11.glBegin(GL11.GL_POINTS);
					}
					if(mode.equalsIgnoreCase("line strip")) {
						GL11.glBegin(GL11.GL_LINE_STRIP);
					}
					if(mode.equalsIgnoreCase("line loop")) {
						GL11.glBegin(GL11.GL_LINE_LOOP);
					}
					GL11.glVertex3d(bct.startPos[0] -
							dx, bct.startPos[1] -
							dy, bct.startPos[2] -
							dz);

					Iterator<double[]> pIterator = bct.positions.iterator();
					while(pIterator.hasNext()) {
						double[] position = pIterator.next();
						double x = position[0] - dx;
						double y = position[1] - dy;
						double z = position[2] - dz;
						GL11.glVertex3d(x, y, z);
					}
					GL11.glEnd();
					GL11.glDepthMask(true);
					GL11.glDisable(2848);
					GL11.glEnable(2929);
					//GL11.glEnable(2896);
					GL11.glEnable(GL11.GL_TEXTURE_2D);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
					GL11.glPopMatrix();
				}
			}
			if (sand){
				while(iteratorFall.hasNext()) {
					BreadCrumbFall bct = iteratorFall.next();
					GL11.glPushMatrix();
					GL11.glDisable(3553);
					GL11.glDisable(2896);
					GL11.glEnable(3042);
					GL11.glBlendFunc(770, 771);
					GL11.glDisable(2929);
					GL11.glEnable(2848);
					GL11.glDepthMask(false);
					GL11.glLineWidth(lineWidth);
					GL11.glColor4d(sandColor.getRed()/255.0F, sandColor.getGreen()/255.0F, sandColor.getBlue()/255.0F, sandColor.getAlpha()/255.0F);
					if(mode.equalsIgnoreCase("line")) {
						GL11.glBegin(GL11.GL_LINES);
					}
					if(mode.equalsIgnoreCase("points")) {
						GL11.glBegin(GL11.GL_POINTS);
					}
					if(mode.equalsIgnoreCase("line strip")) {
						GL11.glBegin(GL11.GL_LINE_STRIP);
					}
					if(mode.equalsIgnoreCase("line loop")) {
						GL11.glBegin(GL11.GL_LINE_LOOP);
					}
					GL11.glVertex3d(bct.startPos[0] -
							dx, bct.startPos[1] -
							dy, bct.startPos[2] -
							dz);
					Iterator<double[]> pIterator = bct.positions.iterator();
					while(pIterator.hasNext()) {
						double[] position = pIterator.next();
						double x = position[0] - dx;
						double y = position[1] - dy;
						double z = position[2] - dz;
						GL11.glVertex3d(x, y, z);
					}
					GL11.glEnd();
					GL11.glDepthMask(true);
					GL11.glDisable(2848);
					GL11.glEnable(2929);
					//GL11.glEnable(2896);
					GL11.glEnable(GL11.GL_TEXTURE_2D);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
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
	
	public boolean isDispenserNear(BlockPos pos) {
		if(!dispenserChec) return false;
		
		Map<BlockPos, TileEntity> currentChunkTE = mc.theWorld.getChunkFromBlockCoords(pos).getTileEntityMap();
		
		Optional<TileEntity> entity = currentChunkTE.values().stream().filter(new Predicate<TileEntity>() {
			@Override
			public boolean test(TileEntity x) {
				return x instanceof TileEntityDispenser;
			}
		}).findFirst();
		
		return entity.isPresent();
	}
}