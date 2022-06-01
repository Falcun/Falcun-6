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

public class FlagEmote3 extends Emote
{
    public FlagEmote3(String name, int duration, boolean looping, ResourceLocation sound, ResourceLocation location)
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
        animator.userConfig.meshes.get("flag3").visible = true;
        animator.userConfig.meshes.get("flag_pole").visible = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void stopAnimation(AnimatorEmoticonsController animator)
    {
        animator.userConfig.meshes.get("flag3").visible = false;
        animator.userConfig.meshes.get("flag_pole").visible = false;
    }
}