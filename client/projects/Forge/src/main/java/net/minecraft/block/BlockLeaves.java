package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockLeaves extends BlockLeavesBase implements net.minecraftforge.common.IShearable
{
    public static final PropertyBool DECAYABLE = PropertyBool.create("decayable");
    public static final PropertyBool CHECK_DECAY = PropertyBool.create("check_decay");
    int[] surroundings;
    @SideOnly(Side.CLIENT)
    protected int iconIndex;
    @SideOnly(Side.CLIENT)
    protected boolean isTransparent;

    public BlockLeaves()
    {
        super(Material.leaves, false);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setHardness(0.2F);
        this.setLightOpacity(1);
        this.setStepSound(soundTypeGrass);
    }

    @SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        return ColorizerFoliage.getFoliageColor(0.5D, 1.0D);
    }

    @SideOnly(Side.CLIENT)
    public int getRenderColor(IBlockState state)
    {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
    {
        return BiomeColorHelper.getFoliageColorAtPos(worldIn, pos);
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        int i = 1;
        int j = i + 1;
        int k = pos.getX();
        int l = pos.getY();
        int i1 = pos.getZ();

        if (worldIn.isAreaLoaded(new BlockPos(k - j, l - j, i1 - j), new BlockPos(k + j, l + j, i1 + j)))
        {
            for (int j1 = -i; j1 <= i; ++j1)
            {
                for (int k1 = -i; k1 <= i; ++k1)
                {
                    for (int l1 = -i; l1 <= i; ++l1)
                    {
                        BlockPos blockpos = pos.add(j1, k1, l1);
                        IBlockState iblockstate = worldIn.getBlockState(blockpos);

                        if (iblockstate.getBlock().isLeaves(worldIn, blockpos))
                        {
                            iblockstate.getBlock().beginLeavesDecay(worldIn, blockpos);
                        }
                    }
                }
            }
        }
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            if (((Boolean)state.getValue(CHECK_DECAY)).booleanValue() && ((Boolean)state.getValue(DECAYABLE)).booleanValue())
            {
                int i = 4;
                int j = i + 1;
                int k = pos.getX();
                int l = pos.getY();
                int i1 = pos.getZ();
                int j1 = 32;
                int k1 = j1 * j1;
                int l1 = j1 / 2;

                if (this.surroundings == null)
                {
                    this.surroundings = new int[j1 * j1 * j1];
                }

                if (worldIn.isAreaLoaded(new BlockPos(k - j, l - j, i1 - j), new BlockPos(k + j, l + j, i1 + j)))
                {
                    BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                    for (int i2 = -i; i2 <= i; ++i2)
                    {
                        for (int j2 = -i; j2 <= i; ++j2)
                        {
                            for (int k2 = -i; k2 <= i; ++k2)
                            {
                                Block block = worldIn.getBlockState(blockpos$mutableblockpos.set(k + i2, l + j2, i1 + k2)).getBlock();

                                if (!block.canSustainLeaves(worldIn, blockpos$mutableblockpos.set(k + i2, l + j2, i1 + k2)))
                                {
                                    if (block.isLeaves(worldIn, blockpos$mutableblockpos.set(k + i2, l + j2, i1 + k2)))
                                    {
                                        this.surroundings[(i2 + l1) * k1 + (j2 + l1) * j1 + k2 + l1] = -2;
                                    }
                                    else
                                    {
                                        this.surroundings[(i2 + l1) * k1 + (j2 + l1) * j1 + k2 + l1] = -1;
                                    }
                                }
                                else
                                {
                                    this.surroundings[(i2 + l1) * k1 + (j2 + l1) * j1 + k2 + l1] = 0;
                                }
                            }
                        }
                    }

                    for (int i3 = 1; i3 <= 4; ++i3)
                    {
                        for (int j3 = -i; j3 <= i; ++j3)
                        {
                            for (int k3 = -i; k3 <= i; ++k3)
                            {
                                for (int l3 = -i; l3 <= i; ++l3)
                                {
                                    if (this.surroundings[(j3 + l1) * k1 + (k3 + l1) * j1 + l3 + l1] == i3 - 1)
                                    {
                                        if (this.surroundings[(j3 + l1 - 1) * k1 + (k3 + l1) * j1 + l3 + l1] == -2)
                                        {
                                            this.surroundings[(j3 + l1 - 1) * k1 + (k3 + l1) * j1 + l3 + l1] = i3;
                                        }

                                        if (this.surroundings[(j3 + l1 + 1) * k1 + (k3 + l1) * j1 + l3 + l1] == -2)
                                        {
                                            this.surroundings[(j3 + l1 + 1) * k1 + (k3 + l1) * j1 + l3 + l1] = i3;
                                        }

                                        if (this.surroundings[(j3 + l1) * k1 + (k3 + l1 - 1) * j1 + l3 + l1] == -2)
                                        {
                                            this.surroundings[(j3 + l1) * k1 + (k3 + l1 - 1) * j1 + l3 + l1] = i3;
                                        }

                                        if (this.surroundings[(j3 + l1) * k1 + (k3 + l1 + 1) * j1 + l3 + l1] == -2)
                                        {
                                            this.surroundings[(j3 + l1) * k1 + (k3 + l1 + 1) * j1 + l3 + l1] = i3;
                                        }

                                        if (this.surroundings[(j3 + l1) * k1 + (k3 + l1) * j1 + (l3 + l1 - 1)] == -2)
                                        {
                                            this.surroundings[(j3 + l1) * k1 + (k3 + l1) * j1 + (l3 + l1 - 1)] = i3;
                                        }

                                        if (this.surroundings[(j3 + l1) * k1 + (k3 + l1) * j1 + l3 + l1 + 1] == -2)
                                        {
                                            this.surroundings[(j3 + l1) * k1 + (k3 + l1) * j1 + l3 + l1 + 1] = i3;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                int l2 = this.surroundings[l1 * k1 + l1 * j1 + l1];

                if (l2 >= 0)
                {
                    worldIn.setBlockState(pos, state.withProperty(CHECK_DECAY, Boolean.valueOf(false)), 4);
                }
                else
                {
                    this.destroy(worldIn, pos);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (worldIn.canLightningStrike(pos.up()) && !World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) && rand.nextInt(15) == 1)
        {
            double d0 = (double)((float)pos.getX() + rand.nextFloat());
            double d1 = (double)pos.getY() - 0.05D;
            double d2 = (double)((float)pos.getZ() + rand.nextFloat());
            worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

    private void destroy(World worldIn, BlockPos pos)
    {
        this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
        worldIn.setBlockToAir(pos);
    }

    public int quantityDropped(Random random)
    {
        return random.nextInt(20) == 0 ? 1 : 0;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(Blocks.sapling);
    }

    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
    }

    protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance)
    {
    }

    protected int getSaplingDropChance(IBlockState state)
    {
        return 20;
    }

    public boolean isOpaqueCube()
    {
        return !this.fancyGraphics;
    }

    @SideOnly(Side.CLIENT)
    public void setGraphicsLevel(boolean fancy)
    {
        this.isTransparent = fancy;
        this.fancyGraphics = fancy;
        this.iconIndex = fancy ? 0 : 1;
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return this.isTransparent ? EnumWorldBlockLayer.CUTOUT_MIPPED : EnumWorldBlockLayer.SOLID;
    }

    public boolean isVisuallyOpaque()
    {
        return false;
    }

    public abstract BlockPlanks.EnumType getWoodType(int meta);

    @Override public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos){ return true; }
    @Override public boolean isLeaves(IBlockAccess world, BlockPos pos){ return true; }

    @Override
    public void beginLeavesDecay(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        if (!(Boolean)state.getValue(CHECK_DECAY))
        {
            world.setBlockState(pos, state.withProperty(CHECK_DECAY, true), 4);
        }
    }

    @Override
    public java.util.List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        java.util.List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
        Random rand = world instanceof World ? ((World)world).rand : new Random();
        int chance = this.getSaplingDropChance(state);

        if (fortune > 0)
        {
            chance -= 2 << fortune;
            if (chance < 10) chance = 10;
        }

        if (rand.nextInt(chance) == 0)
            ret.add(new ItemStack(getItemDropped(state, rand, fortune), 1, damageDropped(state)));

        chance = 200;
        if (fortune > 0)
        {
            chance -= 10 << fortune;
            if (chance < 40) chance = 40;
        }

        this.captureDrops(true);
        if (world instanceof World)
            this.dropApple((World)world, pos, state, chance); // Dammet mojang
        ret.addAll(this.captureDrops(false));
        return ret;
    }

}