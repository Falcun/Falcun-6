package net.minecraft.client.audio;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.RegistrySimple;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoundRegistry extends RegistrySimple<ResourceLocation, SoundEventAccessorComposite>
{
    private Map<ResourceLocation, SoundEventAccessorComposite> soundRegistry;

    protected Map<ResourceLocation, SoundEventAccessorComposite> createUnderlyingMap()
    {
        this.soundRegistry = Maps.<ResourceLocation, SoundEventAccessorComposite>newHashMap();
        return this.soundRegistry;
    }

    public void registerSound(SoundEventAccessorComposite p_148762_1_)
    {
        this.putObject(p_148762_1_.getSoundEventLocation(), p_148762_1_);
    }

    public void clearMap()
    {
        this.soundRegistry.clear();
    }
}