package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBanner extends BlockContainer
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);

    protected BlockBanner()
    {
        super(Material.wood);
        float f = 0.25F;
        float f1 = 1.0F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
    }

    public String getLocalizedName()
    {
        return StatCollector.translateToLocal("item.banner.white.name");
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }

    public boolean isFullCube()
    {
        return false;
    }

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean func_181623_g()
    {
        return true;
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityBanner();
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.banner;
    }

    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        {
            super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        }
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, BlockPos pos)
    {
        return Items.banner;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return !this.func_181087_e(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos);
    }

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te)
    {
        if (te instanceof TileEntityBanner)
        {
            TileEntityBanner tileentitybanner = (TileEntityBanner)te;
            ItemStack itemstack = new ItemStack(Items.banner, 1, ((TileEntityBanner)te).getBaseColor());
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            TileEntityBanner.func_181020_a(nbttagcompound, tileentitybanner.getBaseColor(), tileentitybanner.func_181021_d());
            itemstack.setTagInfo("BlockEntityTag", nbttagcompound);
            spawnAsEntity(worldIn, pos, itemstack);
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, (TileEntity)null);
        }
    }

    @Override
    public java.util.List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        TileEntity te = world.getTileEntity(pos);

        java.util.List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
        if (te instanceof TileEntityBanner)
        {
            TileEntityBanner banner = (TileEntityBanner)te;
            ItemStack item = new ItemStack(Items.banner, 1, banner.getBaseColor());
            NBTTagCompound nbt = new NBTTagCompound();
            TileEntityBanner.func_181020_a(nbt, banner.getBaseColor(), banner.func_181021_d());
            item.setTagInfo("BlockEntityTag", nbt);
            ret.add(item);
        }
        else
        {
            ret.add(new ItemStack(Items.banner, 1, 0));
        }
        return ret;
    }

    public static class BlockBannerHanging extends BlockBanner
        {
            public BlockBannerHanging()
            {
                this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
            }

            public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
            {
                EnumFacing enumfacing = (EnumFacing)worldIn.getBlockState(pos).getValue(FACING);
                float f = 0.0F;
                float f1 = 0.78125F;
                float f2 = 0.0F;
                float f3 = 1.0F;
                float f4 = 0.125F;
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

                switch (enumfacing)
                {
                    case NORTH:
                    default:
                        this.setBlockBounds(f2, f, 1.0F - f4, f3, f1, 1.0F);
                        break;
                    case SOUTH:
                        this.setBlockBounds(f2, f, 0.0F, f3, f1, f4);
                        break;
                    case WEST:
                        this.setBlockBounds(1.0F - f4, f, f2, 1.0F, f1, f3);
                        break;
                    case EAST:
                        this.setBlockBounds(0.0F, f, f2, f4, f1, f3);
                }
            }

            public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
            {
                EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

                if (!worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock().getMaterial().isSolid())
                {
                    this.dropBlockAsItem(worldIn, pos, state, 0);
                    worldIn.setBlockToAir(pos);
                }

                super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
            }

            public IBlockState getStateFromMeta(int meta)
            {
                EnumFacing enumfacing = EnumFacing.getFront(meta);

                if (enumfacing.getAxis() == EnumFacing.Axis.Y)
                {
                    enumfacing = EnumFacing.NORTH;
                }

                return this.getDefaultState().withProperty(FACING, enumfacing);
            }

            public int getMetaFromState(IBlockState state)
            {
                return ((EnumFacing)state.getValue(FACING)).getIndex();
            }

            protected BlockState createBlockState()
            {
                return new BlockState(this, new IProperty[] {FACING});
            }
        }

    public static class BlockBannerStanding extends BlockBanner
        {
            public BlockBannerStanding()
            {
                this.setDefaultState(this.blockState.getBaseState().withProperty(ROTATION, Integer.valueOf(0)));
            }

            public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
            {
                if (!worldIn.getBlockState(pos.down()).getBlock().getMaterial().isSolid())
                {
                    this.dropBlockAsItem(worldIn, pos, state, 0);
                    worldIn.setBlockToAir(pos);
                }

                super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
            }

            public IBlockState getStateFromMeta(int meta)
            {
                return this.getDefaultState().withProperty(ROTATION, Integer.valueOf(meta));
            }

            public int getMetaFromState(IBlockState state)
            {
                return ((Integer)state.getValue(ROTATION)).intValue();
            }

            protected BlockState createBlockState()
            {
                return new BlockState(this, new IProperty[] {ROTATION});
            }
        }
}