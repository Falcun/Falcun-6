package mchorse.emoticons.client.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SaltParticle extends EntityFX
{
    /**
     * Particles texture 
     */
    public static final ResourceLocation PARTICLES = new ResourceLocation("emoticons", "textures/particles.png");

    public static ModelRenderer salt;

    public SaltParticle(World worldIn, double posXIn, double posYIn, double posZIn, double mY)
    {
        super(worldIn, posXIn, posYIn, posZIn);

        this.particleGravity = 0.5F;
        this.particleMaxAge = 20 + this.rand.nextInt(10);
        this.motionX = this.rand.nextFloat() * 0.05F;
        this.motionZ = this.rand.nextFloat() * 0.05F;
        this.motionY = mY;

        if (salt == null)
        {
            ModelBase model = new ModelBase()
            {};
            model.textureWidth = 64;
            model.textureHeight = 64;

            salt = new ModelRenderer(model, 0, 0);
            salt.addBox(-0.5F, -0.5F, 0.5F, 1, 1, 1);
        }
    }

    /**
     * It's probably so inefficient oof 
     */
    @Override
    public void renderParticle(WorldRenderer buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        float f5 = (float) (this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
        float f6 = (float) (this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
        float f7 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);

        int age = this.particleMaxAge - this.particleAge;
        float scale = 0.5F * (age < 5 ? age / 5F : 1);

        Minecraft.getMinecraft().renderEngine.bindTexture(SaltParticle.PARTICLES);

        int i = entityIn.getBrightnessForRender(0);

        if (entityIn.isBurning())
        {
            i = 15728880;
        }

        int j = i % 65536;
        int k = i / 65536;

        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.pushMatrix();
        GlStateManager.translate(f5, f6, f7);
        GlStateManager.scale(scale, scale, scale);
        RenderHelper.enableStandardItemLighting();
        salt.render(1 / 16F);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    @Override
    public int getFXLayer()
    {
        return 3;
    }
}