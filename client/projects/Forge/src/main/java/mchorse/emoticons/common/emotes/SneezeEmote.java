package mchorse.emoticons.common.emotes;

import mchorse.emoticons.api.animation.model.AnimatorEmoticonsController;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import mchorse.emoticons.utils.Time;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;

import javax.vecmath.Vector4f;

public class SneezeEmote extends Emote
{
	public SneezeEmote(String name, int duration, boolean looping)
	{
		super(name, duration, looping);
	}

	@Override
	public void progressAnimation(EntityLivingBase entity, BOBJArmature armature, AnimatorEmoticonsController animator, int tick, float partial)
	{
		super.progressAnimation(entity, armature, animator, tick, partial);

		if (tick == Time.toTicks(121) - 1)
		{
			for (int i = 0; i < 10; i ++)
			{
				Vector4f result = animator.calcPosition(entity, armature.bones.get("head"), 0, 0.125F, 0.25F, partial);

				entity.worldObj.spawnParticle(EnumParticleTypes.CLOUD, result.x, result.y, result.z, this.rand(0.05F), -0.025F, this.rand(0.05F));
			}
		}
	}
}