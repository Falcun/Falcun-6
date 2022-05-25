package mchorse.emoticons.common.emotes;

import mchorse.emoticons.api.animation.model.AnimatorEmoticonsController;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import net.mattbenson.network.network.packets.emoticons.EmoticonStartData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class RKOEmote extends Emote
{
    public RKOEmote(String name, int duration, boolean looping, ResourceLocation sound, ResourceLocation location)
    {
        super(name, duration, looping, sound, location);
    }

    @Override
    public void progressAnimation(EntityLivingBase entity, BOBJArmature armature, AnimatorEmoticonsController animator, int tick, float partial)
    {
    	if(animator.userConfig.meshes.get("body_2").texture == null) {
        	animator.userConfig.meshes.get("body_2").texture = getSkin(EmoticonStartData.getSkin(entity.getUniqueID()));
    	}
    }

    @Override
    public void startAnimation(AnimatorEmoticonsController animator)
    {
    	animator.userConfig.meshes.get("body_2").visible = true;
    	animator.userConfig.meshes.get("body_2").texture = null;
    }

    public ResourceLocation getSkin(String mcname) {
        ResourceLocation loction = Minecraft.getMinecraft().thePlayer.getLocationSkin();
        
        try {
            EntityPlayer player = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(mcname);
            
            if(player instanceof AbstractClientPlayer) {
               AbstractClientPlayer client = (AbstractClientPlayer) player;
               return client.getLocationSkin();
            }
        } catch(IllegalArgumentException e) {
        }
        
        return loction;
    }
    
    
    @Override
    public void stopAnimation(AnimatorEmoticonsController animator)
    {
        animator.userConfig.meshes.get("body_2").visible = false;
    }
}