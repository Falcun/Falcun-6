package mchorse.emoticons.api.animation.model;

import com.google.common.collect.Maps;
import mchorse.emoticons.skin_n_bones.api.animation.AnimationMesh;
import mchorse.emoticons.skin_n_bones.api.animation.AnimationMeshConfig;
import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorController;
import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorHeldItemConfig;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import net.mattbenson.Falcun;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;


@SideOnly(Side.CLIENT)
public class AnimatorEmoticonsController extends AnimatorController
{
    private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.<String, ResourceLocation>newHashMap();

    public ItemStack itemSlot = null;
    public float itemSlotScale = 0;

    public AnimatorEmoticonsController(String animationName, NBTTagCompound userData)
    {
        super(animationName, userData);
    }

    /**
     * Render current animation
     */
    @Override
    public void renderAnimation(EntityLivingBase entity, AnimationMesh mesh, float yaw, float partialTicks)
    {
        this.updateArmor(entity);

        super.renderAnimation(entity, mesh, yaw, partialTicks);
    }

    /**
     * Render hand held items and skin layer
     */
    @Override
    protected void renderItems(EntityLivingBase entity, BOBJArmature armature)
    {
        if (!this.userConfig.renderHeldItems)
        {
            return;
        }

        float scale = this.userConfig.scaleItems;

        ItemStack mainItem = entity.getHeldItem();

        if (this.itemSlot != null && this.userConfig.rightHands != null)
        {
            if (this.itemSlotScale > 0)
            {
                for (AnimatorHeldItemConfig itemConfig : this.userConfig.rightHands.values())
                {
                    this.renderItem(entity, this.itemSlot, armature, itemConfig, TransformType.THIRD_PERSON, scale * this.itemSlotScale);
                }
            }
        }
        else if (mainItem != null && this.userConfig.rightHands != null)
        {
            for (AnimatorHeldItemConfig itemConfig : this.userConfig.rightHands.values())
            {
                this.renderItem(entity, mainItem, armature, itemConfig, TransformType.THIRD_PERSON, scale);
            }
        }
    }

    /**
     * Update armor slots 
     */
    private void updateArmor(EntityLivingBase entity)
    {
        AnimationMeshConfig helmet = this.userConfig.meshes.get("armor_helmet");
        AnimationMeshConfig chest = this.userConfig.meshes.get("armor_chest");
        AnimationMeshConfig leggings = this.userConfig.meshes.get("armor_leggings");
        AnimationMeshConfig feet = this.userConfig.meshes.get("armor_feet");
      
        if (helmet != null) this.updateArmorSlot(helmet, entity, 3);
        if (chest != null) this.updateArmorSlot(chest, entity, 2);
        if (leggings != null) this.updateArmorSlot(leggings, entity, 1);
        if (feet != null) this.updateArmorSlot(feet, entity, 0);
    }

    /**
     * Update separate armor slots 
     */
    private void updateArmorSlot(AnimationMeshConfig config, EntityLivingBase entity, int slot)
    {
        ItemStack stack = entity.getCurrentArmor(slot);

        if (stack != null && stack.getItem() instanceof ItemArmor)
        {
            ItemArmor item = (ItemArmor) stack.getItem();

            config.visible = true;
            config.texture = this.getArmorResource(entity, stack, slot, null);
            config.color = 0xffffffff;

            if (item.hasColor(stack))
            {
                config.color = item.getColor(stack);
            }
        }
        else
        {
            config.visible = false;
            config.color = 0xffffffff;
        }
    }

    /**
     * More generic ForgeHook version of the above function, it allows for Items to have more control over what texture they provide.
     *
     * @param entity Entity wearing the armor
     * @param stack ItemStack for the armor
     * @param slot Slot ID that the item is in
     * @param type Subtype, can be null or "overlay"
     * @return ResourceLocation pointing at the armor's texture
     */
    private ResourceLocation getArmorResource(net.minecraft.entity.Entity entity, ItemStack stack, int slot, String type)
    {
        ItemArmor item = (ItemArmor) stack.getItem();
        String texture = item.getArmorMaterial().getName();
        String domain = "minecraft";
        int idx = texture.indexOf(':');
        if (idx != -1)
        {
            domain = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }
        String s1 = String.format("%s:textures/models/armor/%s_layer_%d%s.png", domain, texture, (isLegSlot(slot) ? 2 : 1), type == null ? "" : String.format("_%s", type));

        s1 = net.minecraftforge.client.ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
        ResourceLocation resourcelocation = ARMOR_TEXTURE_RES_MAP.get(s1);

        if (resourcelocation == null)
        {
            resourcelocation = new ResourceLocation(s1);
            ARMOR_TEXTURE_RES_MAP.put(s1, resourcelocation);
        }

        return resourcelocation;
    }

    private boolean isLegSlot(int slotIn)
    {
        return slotIn == 1;
    }
}