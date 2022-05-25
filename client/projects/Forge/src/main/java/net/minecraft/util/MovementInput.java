package net.minecraft.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MovementInput
{
    public float moveStrafe;
    public float moveForward;
    public boolean jump;
    public boolean sneak;

    public void updatePlayerMoveState()
    {
    }
}