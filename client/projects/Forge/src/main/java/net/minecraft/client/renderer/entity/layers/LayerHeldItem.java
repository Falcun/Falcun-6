package net.minecraft.client.renderer.entity.layers;


import net.mattbenson.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class LayerHeldItem implements LayerRenderer<EntityLivingBase>
{
    private final RendererLivingEntity<?> livingEntityRenderer;

    public LayerHeldItem(RendererLivingEntity<?> livingEntityRendererIn)
    {
        this.livingEntityRenderer = livingEntityRendererIn;
    }

    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        ItemStack itemstack = entitylivingbaseIn.getHeldItem();

        if (itemstack != null)
        {
            GlStateManager.pushMatrix();

            if (this.livingEntityRenderer.getMainModel().isChild)
            {
                float f = 0.5F;
                GlStateManager.translate(0.0F, 0.625F, 0.0F);
                GlStateManager.rotate(-20.0F, -1.0F, 0.0F, 0.0F);
                GlStateManager.scale(f, f, f);
            }

            //((ModelBiped)this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F);
            //GlStateManager.translate(-0.0625F, 0.4375F, 0.0625F);

            Label_0327:
				if (entitylivingbaseIn instanceof EntityPlayer) {
					if (Wrapper.getInstance().isOldAnimations() && Wrapper.getInstance().isOldAnimationsOldBlocking()) {
						if (((EntityPlayer) entitylivingbaseIn).isBlocking()) {
							if (entitylivingbaseIn.isSneaking()) {
								((ModelBiped) livingEntityRenderer.getMainModel()).postRenderArm(0.0325f);
								GlStateManager.scale(1.05f, 1.05f, 1.05f);
								GlStateManager.translate(-0.58f, 0.32f, -0.07f);
								GlStateManager
								.rotate(-24405.0f, 137290.0f, -2009900.0f, -2654900.0f);
							} else {
								((ModelBiped) livingEntityRenderer.getMainModel()).postRenderArm(0.0325f);
								GlStateManager.scale(1.05f, 1.05f, 1.05f);
								GlStateManager.translate(-0.45f, 0.25f, -0.07f);
								GlStateManager
								.rotate(-24405.0f, 137290.0f, -2009900.0f, -2654900.0f);
							}
						} else {
							((ModelBiped) livingEntityRenderer.getMainModel())
							.postRenderArm(0.0625f);
						}
					} else {
						((ModelBiped) livingEntityRenderer.getMainModel()).postRenderArm(0.0625f);
					}
					if (Wrapper.getInstance().isOldAnimations() && Wrapper.getInstance().isOldAnimationsOldHoldItem()) {
						if (!((EntityPlayer) entitylivingbaseIn).isBlocking()) {
								GlStateManager.translate(-0.0855f, 0.4775f, 0.1585f);
								GlStateManager.rotate(-19.0f, 20.0f, 0.0f, -6.0f);
								break Label_0327;
						}

						if (((EntityPlayer) entitylivingbaseIn).isBlocking()) {
							GlStateManager.translate(-0.0625f, 0.4375f, 0.0625f);
						}
					} else {
						GlStateManager.translate(-0.0625f, 0.4375f, 0.0625f);
					}
				} else {
					((ModelBiped) livingEntityRenderer.getMainModel()).postRenderArm(0.0625f);
					GlStateManager.translate(-0.0625f, 0.4375f, 0.0625f);
				}

            
            
            if (entitylivingbaseIn instanceof EntityPlayer && ((EntityPlayer)entitylivingbaseIn).fishEntity != null)
            {
                itemstack = new ItemStack(Items.fishing_rod, 0);
            }

            Item item = itemstack.getItem();
            Minecraft minecraft = Minecraft.getMinecraft();

            if (item instanceof ItemBlock && Block.getBlockFromItem(item).getRenderType() == 2)
            {
                GlStateManager.translate(0.0F, 0.1875F, -0.3125F);
                GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                float f1 = 0.375F;
                GlStateManager.scale(-f1, -f1, f1);
            }

            if (entitylivingbaseIn.isSneaking())
            {
                GlStateManager.translate(0.0F, 0.203125F, 0.0F);
            }

            minecraft.getItemRenderer().renderItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON);
            GlStateManager.popMatrix();   
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}