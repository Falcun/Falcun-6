package mchorse.emoticons.capabilities.cosmetic;

import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Vector4f;

import org.lwjgl.opengl.GL11;

import mchorse.emoticons.api.animation.model.AnimatorEmoticonsController;
import mchorse.emoticons.common.EmoteAPI;
import mchorse.emoticons.common.emotes.Emote;
import mchorse.emoticons.skin_n_bones.api.animation.model.ActionConfig;
import mchorse.emoticons.skin_n_bones.api.animation.model.ActionPlayback;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import net.mattbenson.Falcun;
import net.mattbenson.modules.types.other.PerspectiveMod;
import net.mattbenson.network.NetworkingClient;
import net.mattbenson.network.network.packets.emoticons.EmoticonStartData;
import net.mattbenson.network.network.packets.misc.UserList;
import net.mattbenson.render.RenderLightmap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class Cosmetic implements ICosmetic
{
    public AnimatorEmoticonsController animator;

    public ActionPlayback emoteAction;

    public Emote emote;
    public Map<Emote, PositionedSoundRecord> sound;

    private float NAME_TAG_RANGE = 64.0f;
    private float NAME_TAG_RANGE_SNEAK = 32.0f;

    /* Trackers */
    private int emoteTimer;
    private double lastX;
    private double lastY;
    private double lastZ;
    
    public Cosmetic() {
    	this.sound = new HashMap<>();
    }

    public static ICosmetic get(Entity entity)
    {
        return entity.getCapability(CosmeticProvider.COSMETIC, null);
    }

    @Override
    public void setEmote(Emote emote, EntityLivingBase target)
    {
        if (target.worldObj.isRemote)
        {
            this.stopAction(target);
        }
        
        this.emote = emote;
        this.emoteTimer = 0;

        if (target.worldObj.isRemote)
        {
            this.setActionEmote(emote, target);
        }
    }

    @Override
    public Emote getEmote()
    {
        return this.emote;
    }

    @Override
    public void update(EntityLivingBase target)
    {
        if (target.worldObj.isRemote)
        {
            this.updateClient(target);
        }
        else
        {
            if (this.emote != null)
            {
                /* Turn off emote when player moves */
                double diff = Math.abs((target.posX - this.lastX) + (target.posY - this.lastY) + (target.posZ - this.lastZ));

                if (diff > 0.015 || (!this.emote.looping && this.emoteTimer >= this.emote.duration))
                {
    				if(sound.containsKey(emote)) {
    					Minecraft.getMinecraft().getSoundHandler().stopSound(sound.get(emote));
    					sound.remove(emote);
    				}

                    EmoteAPI.setEmote("", (EntityPlayerMP) target);
                }

                this.emoteTimer++;
            }

            this.lastX = target.posX;
            this.lastY = target.posY;
            this.lastZ = target.posZ;
        }
    }

    /* Client side code */

    private void updateClient(final EntityLivingBase target)
    {
        /* On servers without Emoticons mod, reset the emote when 
         * finished */
        if (this.emote != null)
        {
            /* Turn off emote when player moves */
            double diff = Math.abs((target.posX - this.lastX) + (target.posY - this.lastY) + (target.posZ - this.lastZ));

            if (diff > 0.015 || (!this.emote.looping && this.emoteTimer >= this.emote.duration) 
            		&& target.getDisplayName().getUnformattedText() == Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText())
            {
				if(sound.containsKey(emote)) {
					Minecraft.getMinecraft().getSoundHandler().stopSound(sound.get(emote));
					sound.remove(emote);
				}
				
                this.setEmote(null, target);
                
				NetworkingClient.sendLine("EmoticonStop");
				Falcun.getInstance().moduleManager.getModule(PerspectiveMod.class).perspectiveToggled = false;
				Falcun.getInstance().moduleManager.getModule(PerspectiveMod.class).isEmoting = false;
                Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
				Minecraft.getMinecraft().renderGlobal.setDisplayListEntitiesDirty();
				return;
            }
            if(this.emoteTimer > 2 && target.getDisplayName().getUnformattedText() == Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText()
            		&& 
            		(Minecraft.getMinecraft().gameSettings.keyBindAttack.isKeyDown()
            		|| Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown()) ) {
            	
				if(sound.containsKey(emote)) {
					Minecraft.getMinecraft().getSoundHandler().stopSound(sound.get(emote));
					sound.remove(emote);
				}

            	
                this.setEmote(null, target);
                
                NetworkingClient.sendLine("EmoticonStop");
                Falcun.getInstance().moduleManager.getModule(PerspectiveMod.class).perspectiveToggled = false;
                Falcun.getInstance().moduleManager.getModule(PerspectiveMod.class).isEmoting = false;
                Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
				Minecraft.getMinecraft().renderGlobal.setDisplayListEntitiesDirty();
				return;
            }
        }

        this.lastX = target.posX;
        this.lastY = target.posY;
        this.lastZ = target.posZ;
        
		if (this.emote != null && this.emoteAction != null) {
			if (this.emote.sound != null && this.emoteAction.getTick(0) == 0) {
				if(sound.containsKey(emote)) {
					Minecraft.getMinecraft().getSoundHandler().stopSound(sound.get(emote));
					sound.remove(emote);
				}
				
				PositionedSoundRecord newSound = new PositionedSoundRecord(this.emote.sound, 1, 1, (float) target.posX, (float) target.posY, (float) target.posZ);
				sound.put(emote, newSound);
				Minecraft.getMinecraft().getSoundHandler().playSound(newSound);
			}

			this.emoteTimer++;
		}

		if (this.animator != null) {
			this.animator.update(target);
		}
    }

    private void stopAction(EntityLivingBase target)
    {
        if (this.emote != null)
        {
        	EmoticonStartData.removeSkin(target.getUniqueID());
            this.emote.stopAnimation(this.animator);
        }
    }

    private void setActionEmote(Emote emote, EntityLivingBase target)
    {
        if (this.animator == null)
        {
            this.setupAnimator(target);
        }

        if (emote != null)
        {
            ActionConfig config = this.animator.config.config.actions.getConfig("emote_" + emote.name);

            this.emoteAction = this.animator.animation.createAction(null, config, emote.looping);
            this.animator.setEmote(this.emoteAction);

            emote.startAnimation(this.animator);
        }
        else
        {
            this.emoteAction = null;
            this.animator.setEmote(null);
        }
    }

    /**
     * Render the animator controller based on given entity
     */
    @Override
    public boolean render(EntityLivingBase entity, double x, double y, double z, float partialTicks)
    {
        /* Load animator with texture */
        if (this.animator == null)
        {
            this.setupAnimator(entity);
        }

        boolean disable = true;
        boolean render = this.animator != null && (!disable || this.emote != null);
        
        if (render)
        {
            /* Just in case */
            if (entity instanceof AbstractClientPlayer)
            {
                AbstractClientPlayer player = (AbstractClientPlayer) entity;
                String type = player.getSkinType() + (Falcun.emoticons.simpleModels ? "_simple" : "");
                if (!type.equals(this.animator.animationName))
                {
                    this.animator.animationName = type;
                    this.animator.animation = null;
                    this.animator.fetchAnimation();
                }

                this.animator.userConfig.meshes.get("body").texture = player.getLocationSkin();
            }

            this.animator.render(entity, x, y, z, 0, partialTicks);

            BOBJArmature armature = this.animator.animation.meshes.get(0).armature;
            Minecraft mc = Minecraft.getMinecraft();

            if (RenderLightmap.canRenderNamePlate(entity))
            {
                RenderManager manager = mc.getRenderManager();
                Vector4f vec = this.animator.calcPosition(entity, armature.bones.get("head"), 0F, 0F, 0F, partialTicks);
                float pYaw = manager.playerViewY;
                float pPitch = manager.playerViewX;
                boolean frontal = mc.gameSettings.thirdPersonView == 2;

                float nx = vec.x - (float) manager.viewerPosX;
                float ny = vec.y - (float) manager.viewerPosY + 0.7F;
                float nz = vec.z - (float) manager.viewerPosZ;
                //try LOL

                renderName(entity, manager, entity.getDisplayName().getFormattedText(), nx, ny, nz, pYaw, pPitch, partialTicks);
                // TODO: EntityRenderer.drawNameplate(mc.fontRendererObj, entity.getDisplayName().getFormattedText(), nx, ny, nz, -6, pYaw, pPitch, frontal, entity.isSneaking());
            }
            
            
            if (RenderLightmap.canRenderNamePlate(entity))
            {
                RenderManager manager = mc.getRenderManager();
                Vector4f vec = this.animator.calcPosition(entity, armature.bones.get("head_2"), 0F, 0F, 0F, partialTicks);
                float pYaw = manager.playerViewY;
                float pPitch = manager.playerViewX;
                boolean frontal = mc.gameSettings.thirdPersonView == 2;

                float nx = vec.x - (float) manager.viewerPosX;
                float ny = vec.y - (float) manager.viewerPosY + 0.7F;
                float nz = vec.z - (float) manager.viewerPosZ;
                //try LOL

                renderName(entity, manager, EmoticonStartData.getSkin(entity.getUniqueID()), nx, ny, nz, pYaw, pPitch, partialTicks);
                // TODO: EntityRenderer.drawNameplate(mc.fontRendererObj, entity.getDisplayName().getFormattedText(), nx, ny, nz, -6, pYaw, pPitch, frontal, entity.isSneaking());
            }
            
            if (this.emote != null && !Minecraft.getMinecraft().isGamePaused() && emoteAction != null)
            {
                int tick = (int) this.emoteAction.getTick(0);
                this.emote.progressAnimation(entity, armature, this.animator, tick, partialTicks);
            }
        }

        return render;
    }

    protected float interpolateRotation(float par1, float par2, float par3)
    {
        float f;

        for (f = par2 - par1; f < -180.0F; f += 360.0F)
        {
            ;
        }

        while (f >= 180.0F)
        {
            f -= 360.0F;
        }

        return par1 + par3 * f;
    }

    
    public void renderName(EntityLivingBase entityIn, RenderManager renderManager, String str, double x, double y, double z, float pYaw, float pPitch, float partialTicks)
    {
    	FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;
        float f = 1.6F;
        float f1 = 0.016666668F * f;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x + 0.0F, (float)y + 0.2F, (float)z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-pYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(pPitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-f1, -f1, f1);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        int i = 0;

        if (str.equals("deadmau5"))
        {
            i = -10;
        }
        
        boolean renderLogo = entityIn instanceof AbstractClientPlayer && ((AbstractClientPlayer)entityIn).getPlayerInfo() != null && UserList.usesFalcun(((AbstractClientPlayer)entityIn).getGameProfile().getId())  && str.contains(entityIn.getDisplayName().getUnformattedText());
        boolean renderTalkIcon = false;
        int position = 0; 
        if(renderLogo) {
        	position = -10;
        }
        
        int j = (fontrenderer.getStringWidth(str) / 2); 
        GlStateManager.disableTexture2D();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double)(-j - 1 + ((renderLogo) ?  -12 : 0) ), (double)(-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double)(-j - 1 + ((renderLogo) ?  -12 : 0)), (double)(8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double)(j + 1), (double)(8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double)(j + 1), (double)(-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
               
        GlStateManager.enableTexture2D();
        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127);
       
        renderLogos(renderLogo, renderTalkIcon, i, j, worldrenderer, tessellator);
        
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1);
        
        renderLogos(renderLogo, renderTalkIcon, i, j, worldrenderer, tessellator);
        
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
 
    }
    
    public void renderLogos(boolean renderFalcun, boolean renderSpeaker, int i, int j, WorldRenderer worldrenderer, Tessellator tessellator) {
    	if(renderFalcun) {
        	Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("falcun/logo/falcunwings2.png"));
        	worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            int size = (renderSpeaker ? 3:  5);
            //we can play around with the size here i guess as well
            double xpos = j + 5.5;
            double ypos = i - 3.6;
            worldrenderer.pos((double)size - xpos, (double)size -ypos, 0.0).tex(1.0, 1.0).endVertex();
            worldrenderer.pos((double)size -xpos, (double)(-size -ypos), 0.0).tex(1.0, 0.0).endVertex();
            worldrenderer.pos((double)(-size -xpos), (double)(-size -ypos), 0.0).tex(0.0, 0.0).endVertex();
            worldrenderer.pos((double)(-size -xpos), (double)size -ypos, 0.0).tex(0.0, 1.0).endVertex();
            tessellator.draw();
        } 
    }
    
    public void setupAnimator(EntityLivingBase entity)
    {
        AbstractClientPlayer player = (AbstractClientPlayer) entity;

        this.animator = new AnimatorEmoticonsController(player.getSkinType(), new NBTTagCompound());

        NBTTagCompound meshes = new NBTTagCompound();
        NBTTagCompound body = new NBTTagCompound();

        meshes.setTag("body", body);
        body.setString("Texture", player.getLocationSkin().toString());

        this.animator.userData.setTag("Meshes", meshes);
        this.animator.fetchAnimation();
    }
    
}