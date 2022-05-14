package net.mattbenson.modules.types.factions;

import java.awt.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.github.lunatrius.core.util.vector.Vector3d;

import net.mattbenson.Falcun;
import net.mattbenson.Wrapper;
import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.types.render.RenderEvent;
import net.mattbenson.events.types.render.RenderType;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.utils.DrawUtils;
import net.mattbenson.utils.PatchcrumbSoundTNT;
import net.mattbenson.utils.PatchcrumbSource;
import net.mattbenson.utils.legacy.HUDUtil;
import net.mattbenson.utils.legacy.LessArrayLists;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class Patchcrumbs2 extends Module {
	
	@ConfigValue.Boolean(name = "Minimap Display")
    public boolean minimap = true;
	
	@ConfigValue.Boolean(name = "Disable If Near Dispensers")
	public boolean dispenserChec = true;
	
	@ConfigValue.Boolean(name = "Tracers")
	public boolean tracers = true;
	
	@ConfigValue.Boolean(name = "Overstackers")
	public boolean overstackers = true;
    
	@ConfigValue.List(name = "Side", values = {"N/S", "E/W", "Both"})
	private String direction = "Both";
	
	@ConfigValue.Integer(name = "Adjuster", min = -5, max = 5)
	public static int adjuster = 0;
	 
	@ConfigValue.Keybind(name = "Shout TNT Coordinates Key")
	public int keyBind2 = 0;
	    
	@ConfigValue.Text(name = "Chat Format")
	public static String chatFormat = "X: {x}, Y: {y}, Z: {z}";
    
	@ConfigValue.Integer(name = "Display Timer (sec)", min = 1, max = 60)
	public static int timeout = 5;
	
	@ConfigValue.Float(name = "Line Width", min = 0.1F, max = 5.0F)
	public float lineWidth = 1;
	
	@ConfigValue.Integer(name = "Transparency", min = 1, max = 255)
	public int alpha = 100;

	@ConfigValue.Color(name = "Shot Color")
	public Color primaryColour = Color.RED;
    
	@ConfigValue.Color(name = "Patch Line Color")
	public Color secondaryColour = Color.yellow;
	
	@ConfigValue.Color(name = "Tracer Color")
	public Color tracerColor = Color.RED;
	
	@ConfigValue.Color(name = "Font Color")
	public Color color = Color.WHITE;

	@ConfigValue.Boolean(name = "Background")
	public boolean backGround = true;
	
	@ConfigValue.Color(name = "Background Color")
	public Color bColor = new Color(0,0,0,90);
	
	@ConfigValue.Double(name = "Scale", min = 0.5, max = 1.5)
	public double scaled = 1.0;
	
    private static Patchcrumbs2 INSTANCE;
    public Vector3d current;
    public PatchcrumbSoundTNT currentCrumb;
    private long lightningTime;
    private boolean lockedXZ;
    private final List<Material> materials;

	private HUDElement hud;
	
	@Override
	public void onEnable() {
		Wrapper.getInstance().addChat("It is recommended to use the module 'Patchcrumbs'");
	}

	
	public Patchcrumbs2() {
		super("Patchcrumbs 2", ModuleCategory.FACTIONS);

		hud = new HUDElement("patchcrumbsold", 1, 175) {
			@Override
			public void onRender() {
				render();
			}
		};
		
		addHUD(hud);
		

        this.current = new Vector3d(0.0, 0.0, 0.0);
        this.currentCrumb = null;
        this.lightningTime = 0L;
        this.lockedXZ = false;
        this.materials = Arrays.asList(Material.sand, Material.anvil);
        Patchcrumbs2.INSTANCE = this;
        this.enabled = false;
		
		Falcun.getInstance().EVENT_BUS.register(new LessArrayLists());
	}
	
	
 
    
	@SubscribeEvent
	public void onWorldRenderLast(RenderEvent event) {
		if (event.getRenderType() != RenderType.WORLD) {
			return;
		}
        if (!this.enabled) {
            return;
        }
        if (this.currentCrumb != null) {
            this.drawPatchCrumb(this.currentCrumb);
            if (this.tracers && this.mc.thePlayer != null) {
            	DrawUtils.tracerLine(this.currentCrumb.x, this.currentCrumb.y + adjuster + 0.5, this.currentCrumb.z, tracerColor); 
            }
            if (this.currentCrumb.expired()) {
                this.currentCrumb = null;
                this.lockedXZ = false;
            }
        }
        else {
            this.current = new Vector3d(0.0, 0.0, 0.0);
        }
        
        List<EntityTNTPrimed> sortedTnt = new ArrayList<>();
        
        for(Entity entity : mc.theWorld.loadedEntityList) {
        	if(!(entity instanceof EntityTNTPrimed)) {
        		continue;
        	}
        	
			if (isDispenserNear(entity.getPosition())) return;
        	
        	EntityTNTPrimed tnt = (EntityTNTPrimed) entity;
        	sortedTnt.add(tnt);
        }
        
        sortedTnt.sort((first, second) -> {
        	return Integer.compare(first.fuse, second.fuse);
        });
        
        if (sortedTnt.size() == 0) {
            return;
        }
        final Entity entity = sortedTnt.get(0);
        if (entity == null) {
            return;
        }
        if (entity.posY >= 255.8) {
            return;
        }
        final Entity entity2 = entity;
        new Thread(() -> {
            try {
                Thread.sleep(10L);
                if (this.checkLocation(entity2.getPosition())) {
                    this.lockedXZ = true;
                }
                double errorMargin = 1.0;
                Vector3d midpoint = new Vector3d((Math.floor(entity2.posX) + Math.ceil(entity2.posX)) / 2.0, (Math.floor(entity2.posY) + Math.ceil(entity2.posY)) / 2.0, (Math.floor(entity2.posZ) + Math.ceil(entity2.posZ)) / 2.0);
                AxisAlignedBB boundingBox = entity2.getEntityBoundingBox();
                if (Math.ceil(entity2.posZ) == this.current.z && Math.ceil(entity2.posX) == this.current.x) {
                    if (entity2.posY > this.current.y) {
                        this.newCrumbY(entity2.posX, Math.ceil(entity2.posY), entity2.posZ, boundingBox, PatchcrumbSource.ENTITY);
                    }
                }
                else if (this.current.x > midpoint.x - errorMargin && this.current.x < midpoint.x + errorMargin) {
                    if (this.current.z > midpoint.z - errorMargin && this.current.z < midpoint.z + errorMargin) {
                        if (entity2.posY > this.current.y) {
                            this.newCrumbY(entity2.posX, Math.ceil(entity2.posY), entity2.posZ, boundingBox, PatchcrumbSource.ENTITY);
                        }
                    }
                    else {
                        this.newCrumbXZ(entity2.posX, Math.ceil(entity2.posY), entity2.posZ, boundingBox, PatchcrumbSource.ENTITY);
                    }
                }
                else {
                    this.newCrumbXZ(entity2.posX, Math.ceil(entity2.posY), entity2.posZ, boundingBox, PatchcrumbSource.ENTITY);
                }
            }
            catch (InterruptedException ex) {}
        }).start();
    }
    
    @SubscribeEvent
    public void onPlaySound(final PlaySoundEvent event) {
        if (!this.enabled) {
            return;
        }
        final String soundName = event.sound.getSoundLocation().toString();
        if (soundName.endsWith("thunder")) {
            this.lightningTime = System.currentTimeMillis();
        }
        else if (soundName.endsWith("explode") && this.lightningTime != System.currentTimeMillis()) {
            final double soundX = event.sound.getXPosF();
            final double soundY = event.sound.getYPosF();
            final double soundZ = event.sound.getZPosF();
            
            if (soundY >= 255.0) {
                return;
            }
            final BlockPos block1 = new BlockPos(soundX, soundY, soundZ);
            final BlockPos block2 = new BlockPos(soundX + 1.0, soundY + 1.0, soundZ + 1.0);
            
            if (isDispenserNear(block1)) return;
            if (isDispenserNear(block2)) return;
            
            final AxisAlignedBB boundingBox = new AxisAlignedBB(block1, block2);
            new Thread(() -> {
                try {
                    Thread.sleep(10L);
                    if (this.checkLocation(block1)) {
                        this.lockedXZ = true;
                    }
                    if (Math.ceil(soundZ) == this.current.z && Math.ceil(soundX) == this.current.x) {
                        if (soundY > this.current.y) {
                            this.newCrumbY(soundX, soundY, soundZ, boundingBox, PatchcrumbSource.SOUND);
                        }
                    }
                    else {
                        double errorMargin = 1.0;
                        Vector3d midpoint = new Vector3d(soundX + 0.5, soundY + 0.5, soundZ + 0.5);
                        if (this.current.x > midpoint.x - errorMargin && this.current.x < midpoint.x + errorMargin) {
                            if (this.current.z > midpoint.z - errorMargin && this.current.z < midpoint.z + errorMargin) {
                                if (soundY > this.current.y) {
                                    this.newCrumbY(soundX, soundY, soundZ, boundingBox, PatchcrumbSource.SOUND);
                                }
                            }
                            else {
                                this.newCrumbXZ(soundX, soundY, soundZ, boundingBox, PatchcrumbSource.SOUND);
                            }
                        }
                        else {
                            this.newCrumbXZ(soundX, soundY, soundZ, boundingBox, PatchcrumbSource.SOUND);
                        }
                    }
                }
                catch (InterruptedException ex) {}
            }).start();
        }
    }

	@Override
	public void onDisable() {
        this.current = new Vector3d(0.0, 0.0, 0.0);
        this.currentCrumb = null;
        this.lockedXZ = false;
	}

    private boolean checkLocation(final BlockPos pos) {
        for (int i = 0; i < 2; ++i) {
            if (this.materials.contains(this.mc.theWorld.getBlockState(pos.down(i + 1)).getBlock().getMaterial())) {
                return true;
            }
        }
        return false;
    }
    
    private void drawPatchCrumb(final PatchcrumbSoundTNT crumb) {
    	crumb.y = crumb.y + 1;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth((float)this.lineWidth);
        DrawUtils.setGlColour(this.secondaryColour, this.alpha);
        final AxisAlignedBB northSouthBB = DrawUtils.normalize(crumb.boundingBox).expand(0.05, 0.0, 240.05);
        final AxisAlignedBB eastWestBB = DrawUtils.normalize(crumb.boundingBox).expand(240.05, 0.0, 0.05);
        final String direction = this.direction;
        switch (direction) {
            case "N/S": {
                RenderGlobal.drawSelectionBoundingBox(northSouthBB);
                break;
            }
            case "E/W": {
                RenderGlobal.drawSelectionBoundingBox(eastWestBB);
                break;
            }
            case "Both": {
                RenderGlobal.drawSelectionBoundingBox(northSouthBB);
                RenderGlobal.drawSelectionBoundingBox(eastWestBB);
                break;
            }
        }
        DrawUtils.setGlColour(this.primaryColour, this.alpha);
        final AxisAlignedBB tntBox = DrawUtils.normalize(crumb.boundingBox).expand(0.05, 0.0, 0.05);
        RenderGlobal.drawSelectionBoundingBox(tntBox);
        DrawUtils.drawFilledBoundingBox(tntBox);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glDepthMask(true);
        GL11.glLineWidth(1.0f);
        GL11.glPopMatrix();
    }
    
    private void newCrumbY(final double x, final double y, final double z, final AxisAlignedBB boundingBox, final PatchcrumbSource source) {
        if (this.overstackers) {
            if (this.lockedXZ && Math.round(x) == this.current.x && Math.round(z) == this.current.z) {
                return;
            }
            if (!this.checkLocation(new BlockPos(x, y, z))) {
                return;
            }
        }
        this.current = new Vector3d((double)Math.round(x), y, (double)Math.round(z));
        this.currentCrumb = new PatchcrumbSoundTNT(x, y, z, boundingBox, source);
    }
    
    private void newCrumbXZ(final double x, final double y, final double z, final AxisAlignedBB boundingBox, final PatchcrumbSource source) {
        if (this.overstackers) {
            if (this.lockedXZ && Math.round(x) == this.current.x && Math.round(y) == this.current.y && Math.round(z) == this.current.z) {
                return;
            }
            if (!this.checkLocation(new BlockPos(x, y, z))) {
                return;
            }
        }
        this.current = new Vector3d((double)Math.round(x), y, (double)Math.round(z));
        this.currentCrumb = new PatchcrumbSoundTNT(x, y, z, boundingBox, source);
    }
    
    public static Patchcrumbs2 getInstance() {
        return Patchcrumbs2.INSTANCE;
    }
    
    
	public boolean isDispenserNear(BlockPos pos) {
		if (!dispenserChec) return false;

		Map < BlockPos,
		TileEntity > currentChunkTE = mc.theWorld.getChunkFromBlockCoords(pos).getTileEntityMap();

		Optional < TileEntity > entity = currentChunkTE.values().stream().filter(new Predicate < TileEntity > () {@Override
			public boolean test(TileEntity x) {
			return x instanceof TileEntityDispenser;
		}
		}).findFirst();

		return entity.isPresent();
	}
	
	private void render() {
		int posX = hud.getX();
		int posY = hud.getY();


			if (currentCrumb == null)
				return;

			GL11.glPushMatrix();

			int width = 0;
			int height = 15;
			int added = 0;

			int x = (int) currentCrumb.x;
			int y = (int) currentCrumb.y;
			int z = (int) currentCrumb.z;

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
	
	@SubscribeEvent 
	public void onKeyPress(KeyInputEvent event)
	{
		if(!this.enabled) return;
		if(this.mc == null || this.mc.thePlayer == null || this.mc.theWorld == null) return;
		if (Keyboard.getEventKey() == keyBind2 && keyBind2 != 0 && Keyboard.getEventKeyState())
		{
			if (currentCrumb != null)
			{
				double x = currentCrumb.x;
				double y = currentCrumb.y;
				double z = currentCrumb.z;
				String toSend = chatFormat.replace("{x}",(int) x+"")
						.replace("{y}",(int) y+"")
						.replace("{z}",(int) z+"");
				mc.thePlayer.sendChatMessage(toSend);
			} else {
				Wrapper.getInstance().addChat("No shot found yet!");
			}
		}
	}
}