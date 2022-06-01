package net.mattbenson.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class RenderLightmap extends RendererLivingEntity<EntityLivingBase>
{
    /**
     * Private instance 
     */
    private static RenderLightmap instance;

    public static void create()
    {
        instance = new RenderLightmap(Minecraft.getMinecraft().getRenderManager(), null, 0);
    }

    public static boolean canRenderNamePlate(EntityLivingBase entity)
    {
        return instance.canRenderName(entity);
    }

    public static boolean set(EntityLivingBase entity, float partialTicks)
    {
        return instance.setBrightness(entity, partialTicks, true);
    }

    public static void unset()
    {
        instance.unsetBrightness();
    }

    public RenderLightmap(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

    @Override
    protected int getColorMultiplier(EntityLivingBase entitylivingbaseIn, float lightBrightness, float partialTickTime)
    {
        return 0;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityLivingBase entity)
    {
        return null;
    }
}