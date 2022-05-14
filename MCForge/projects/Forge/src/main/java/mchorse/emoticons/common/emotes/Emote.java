package mchorse.emoticons.common.emotes;

import java.util.Random;

import mchorse.emoticons.api.animation.model.AnimatorEmoticonsController;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Emote class
 * 
 * This class represents a data entry of an emote in Fortcraft's emote system
 */
public class Emote
{
    public final String name;
    public int duration;
    public boolean looping;
    protected ResourceLocation location;
    public ResourceLocation sound;
    public Random rand = new Random();

    public String customTitle = "";
    public String customDescription = "";
    
    public Emote(String name, int duration, boolean looping, ResourceLocation sound)
    {
    	this(name, duration, looping, sound, null);
    }
    
    
    public Emote(String name, int duration, boolean looping)
    {
    	this(name, duration, looping,null,null);
    }
    
    public Emote(String name, int duration, boolean looping, ResourceLocation sound, ResourceLocation location)
    {
        this.name = name;
        this.duration = duration;
        this.looping = looping;
        this.sound = sound;
        this.location = location;
    }

    @SideOnly(Side.CLIENT)
    public void progressAnimation(EntityLivingBase entity, BOBJArmature armature, AnimatorEmoticonsController animator, int tick, float partial)
    {}

    @SideOnly(Side.CLIENT)
    public void startAnimation(AnimatorEmoticonsController animator)
    {}

    @SideOnly(Side.CLIENT)
    public void stopAnimation(AnimatorEmoticonsController animator)
    {}

    /**
     * In case if the emote should have some random variations (like rock 
     * paper scissors), this method allows creating a new instance. 
     */
    public Emote getDynamicEmote()
    {
        return this;
    }

    /**
     * Get dynamic emote with  
     */
    public Emote getDynamicEmote(String suffix)
    {
        return this;
    }

    public String getKey()
    {
        return this.name;
    }

    public ResourceLocation getLocation() {
    	return this.location;
    }
    
    @SideOnly(Side.CLIENT)
    public String getTitle()
    {
        return this.customTitle.isEmpty() ? I18n.format("emoticons.emotes." + this.name + ".title") : this.customTitle;
    }

    @SideOnly(Side.CLIENT)
    public String getDescription()
    {
        return this.customDescription.isEmpty() ? I18n.format("emoticons.emotes." + this.name + ".desc") : this.customDescription;
    }

    public float rand(float factor)
    {
        return this.rand.nextFloat() * factor - factor / 2F;
    }
}