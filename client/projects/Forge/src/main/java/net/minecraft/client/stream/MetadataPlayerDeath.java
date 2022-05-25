package net.minecraft.client.stream;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MetadataPlayerDeath extends Metadata
{
    public MetadataPlayerDeath(EntityLivingBase p_i46066_1_, EntityLivingBase p_i46066_2_)
    {
        super("player_death");

        if (p_i46066_1_ != null)
        {
            this.func_152808_a("player", p_i46066_1_.getName());
        }

        if (p_i46066_2_ != null)
        {
            this.func_152808_a("killer", p_i46066_2_.getName());
        }
    }
}