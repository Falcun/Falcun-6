package mchorse.emoticons.common.emotes;

import mchorse.emoticons.api.animation.model.AnimatorEmoticonsController;
import mchorse.emoticons.client.particles.PopcornParticle;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJBone;
import mchorse.emoticons.utils.Time;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Vector4f;

public class SmokeEmote extends Emote
{
    public SmokeEmote(String name, int duration, boolean looping, ResourceLocation sound, ResourceLocation location)
    {
        super(name, duration, looping, sound, location);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void progressAnimation(EntityLivingBase entity, BOBJArmature armature, AnimatorEmoticonsController animator, int tick, float partial)
    {
        if (tick == 155)
        {
    			for (int i = 0; i < 10; i ++)
    			{
    				Vector4f result = animator.calcPosition(entity, armature.bones.get("head"), 0, 0.125F, 0.25F, partial);

    				entity.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, result.x, result.y, result.z, this.rand(0.05F), 0.025F, this.rand(0.05F));
    			
    			}
    		}
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void startAnimation(AnimatorEmoticonsController animator)
    {
        animator.userConfig.meshes.get("joint").visible = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void stopAnimation(AnimatorEmoticonsController animator)
    {
        animator.userConfig.meshes.get("joint").visible = false;
    }
}