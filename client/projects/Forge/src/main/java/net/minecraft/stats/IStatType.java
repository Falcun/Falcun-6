package net.minecraft.stats;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IStatType
{
    @SideOnly(Side.CLIENT)
    String format(int p_75843_1_);
}