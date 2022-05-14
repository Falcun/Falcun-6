package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGlass extends BlockBreakable
{
    public BlockGlass(Material materialIn, boolean ignoreSimilarity)
    {
        super(materialIn, ignoreSimilarity);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    public boolean isFullCube()
    {
        return false;
    }

    protected boolean canSilkHarvest()
    {
        return true;
    }
}