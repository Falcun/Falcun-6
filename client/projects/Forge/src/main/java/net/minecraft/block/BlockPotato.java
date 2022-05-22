package net.minecraft.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockPotato extends BlockCrops
{
    protected Item getSeed()
    {
        return Items.potato;
    }

    protected Item getCrop()
    {
        return Items.potato;
    }

    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
    }
    @Override
    public java.util.List<net.minecraft.item.ItemStack> getDrops(net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        java.util.List<ItemStack> ret = super.getDrops(world, pos, state, fortune);
        java.util.Random rand = world instanceof World ? ((World)world).rand : new java.util.Random();
        if (((Integer)state.getValue(AGE)) >= 7 && rand.nextInt(50) == 0)
            ret.add(new ItemStack(Items.poisonous_potato));
        return ret;
    }
}