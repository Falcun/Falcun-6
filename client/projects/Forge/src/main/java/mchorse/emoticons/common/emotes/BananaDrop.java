package mchorse.emoticons.common.emotes;

import mchorse.emoticons.api.animation.model.AnimatorEmoticonsController;
import mchorse.emoticons.client.particles.PopcornParticle;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJBone;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Vector4f;

public class BananaDrop extends Emote
{
    public BananaDrop(String name, int duration, boolean looping, ResourceLocation sound, ResourceLocation location)
    {
        super(name, duration, looping, sound, location);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void progressAnimation(EntityLivingBase entity, BOBJArmature armature, AnimatorEmoticonsController animator, int tick, float partial)
    {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void startAnimation(AnimatorEmoticonsController animator)
    {
        animator.userConfig.meshes.get("glasses").visible = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void stopAnimation(AnimatorEmoticonsController animator)
    {
        animator.userConfig.meshes.get("glasses").visible = false;
    }
}