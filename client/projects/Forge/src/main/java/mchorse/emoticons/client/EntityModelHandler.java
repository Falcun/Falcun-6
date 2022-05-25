package mchorse.emoticons.client;

import mchorse.emoticons.ClientProxy;
import mchorse.emoticons.Emoticons;
import mchorse.emoticons.capabilities.cosmetic.Cosmetic;
import mchorse.emoticons.capabilities.cosmetic.CosmeticMode;
import mchorse.emoticons.capabilities.cosmetic.ICosmetic;
import mchorse.emoticons.utils.Interpolations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Entity model handler. This handler is responsible for rendering 
 * models on player.
 */
@SideOnly(Side.CLIENT)
public class EntityModelHandler
{
    /**
     * Draw an entity on the screen.
     *
     * Taken <s>stolen</s> from minecraft's class GuiInventory. I wonder what's
     * the license of minecraft's decompiled code?
     */
    public static void drawEntityOnScreen(int posX, int posY, float scale, EntityLivingBase ent, float partialTicks)
    {
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, 100.0F);
        GlStateManager.scale((-scale), scale, scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);

        boolean render = ent.getAlwaysRenderNameTag();

        RenderHelper.enableStandardItemLighting();

        GlStateManager.enableRescaleNormal();

        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        float f5 = ent.prevRotationYaw;
        float f6 = ent.prevRotationPitch;
        float f7 = ent.prevRenderYawOffset;

        ent.renderYawOffset = 0;
        ent.rotationYaw = 0;
        ent.rotationPitch = ent.prevRotationPitch = Interpolations.lerp(f6, f2, partialTicks);
        ent.rotationYawHead = ent.prevRotationYawHead = Interpolations.lerp(f3, f4, partialTicks) - Interpolations.lerp(f7, f, partialTicks);
        ent.prevRotationYaw = 0;
        ent.prevRenderYawOffset = 0;
        ent.setAlwaysRenderNameTag(false);

        GlStateManager.translate(0.0F, 0.0F, 0.0F);

        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.doRenderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);
        rendermanager.setRenderShadow(true);

        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        ent.prevRotationYaw = f5;
        ent.prevRotationPitch = f6;
        ent.prevRenderYawOffset = f7;

        ent.setAlwaysRenderNameTag(render);

        GlStateManager.popMatrix();

        RenderHelper.disableStandardItemLighting();

        GlStateManager.disableRescaleNormal();

        GlStateManager.disableBlend();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.disableDepth();
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onRenderPlayer(RenderPlayerEvent.Pre event)
    {
        EntityPlayer player = event.entityPlayer;

        if (player.isSpectator())
        {
            return;
        }

        ICosmetic cap = Cosmetic.get(player);

        if (cap != null && cap.render(player, event.x, event.y, event.z, event.partialRenderTick))
        {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onHUDRender(RenderGameOverlayEvent.Post event)
    {
    	/*
        ScaledResolution resolution = event.resolution;

        if (event.type == RenderGameOverlayEvent.ElementType.ALL)
        {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer player = mc.thePlayer;
            ICosmetic cap = Cosmetic.get(player);

            if (cap != null && this.canShow(cap) && mc.gameSettings.thirdPersonView == 0)
            {
                int w = resolution.getScaledWidth();
                int x = Emoticons.playerRenderingX;
                int y = Emoticons.playerRenderingY;
                int scale = Emoticons.playerRenderingScale;

                drawEntityOnScreen(w - x, y, scale, player, event.partialTicks);
            }
        }
        */
    }

    private boolean canShow(ICosmetic cap)
    {
        int mode = Emoticons.playerPreviewMode;

        if (mode == 1)
        {
            return true;
        }
        else if (mode == 2)
        {
            return false;
        }

        return cap.getEmote() != null;
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPlayerDisconnects(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
        ClientProxy.mode = CosmeticMode.CLIENT;
    }
}