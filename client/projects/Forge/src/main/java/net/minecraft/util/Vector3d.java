package net.minecraft.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Vector3d
{
    public double x;
    public double y;
    public double z;

    public Vector3d()
    {
        this.x = this.y = this.z = 0.0D;
    }
}