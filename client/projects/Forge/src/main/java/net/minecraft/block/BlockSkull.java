package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StatCollector;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSkull extends BlockContainer
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyBool NODROP = PropertyBool.create("nodrop");
    private static final Predicate<BlockWorldState> IS_WITHER_SKELETON = new Predicate<BlockWorldState>()
    {
        public boolean apply(BlockWorldState p_apply_1_)
        {
            return p_apply_1_.getBlockState() != null && p_apply_1_.getBlockState().getBlock() == Blocks.skull && p_apply_1_.getTileEntity() instanceof TileEntitySkull && ((TileEntitySkull)p_apply_1_.getTileEntity()).getSkullType() == 1;
        }
    };
    private BlockPattern witherBasePattern;
    private BlockPattern witherPattern;

    protected BlockSkull()
    {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(NODROP, Boolean.valueOf(false)));
        this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
    }

    public String getLocalizedName()
    {
        return StatCollector.translateToLocal("tile.skull.skeleton.name");
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        switch ((EnumFacing)worldIn.getBlockState(pos).getValue(FACING))
        {
            case UP:
            default:
                this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
                break;
            case NORTH:
                this.setBlockBounds(0.25F, 0.25F, 0.5F, 0.75F, 0.75F, 1.0F);
                break;
            case SOUTH:
                this.setBlockBounds(0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 0.5F);
                break;
            case WEST:
                this.setBlockBounds(0.5F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
                break;
            case EAST:
                this.setBlockBounds(0.0F, 0.25F, 0.25F, 0.5F, 0.75F, 0.75F);
        }
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(NODROP, Boolean.valueOf(false));
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntitySkull();
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, BlockPos pos)
    {
        return Items.skull;
    }

    public int getDamageValue(World worldIn, BlockPos pos)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity instanceof TileEntitySkull ? ((TileEntitySkull)tileentity).getSkullType() : super.getDamageValue(worldIn, pos);
    }

    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (player.capabilities.isCreativeMode)
        {
            state = state.withProperty(NODROP, Boolean.valueOf(true));
            worldIn.setBlockState(pos, state, 4);
        }
        this.dropBlockAsItem(worldIn, pos, state, 0);

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
    }
    public java.util.List<ItemStack> getDrops(IBlockAccess worldIn, BlockPos pos, IBlockState state, int fortune)
    {
        java.util.List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
        {
            if (!((Boolean)state.getValue(NODROP)).booleanValue())
            {
                TileEntity tileentity = worldIn.getTileEntity(pos);

                if (tileentity instanceof TileEntitySkull)
                {
                    TileEntitySkull tileentityskull = (TileEntitySkull)tileentity;
                    ItemStack itemstack = new ItemStack(Items.skull, 1, tileentityskull.getSkullType());

                    if (tileentityskull.getSkullType() == 3 && tileentityskull.getPlayerProfile() != null)
                    {
                        itemstack.setTagCompound(new NBTTagCompound());
                        NBTTagCompound nbttagcompound = new NBTTagCompound();
                        NBTUtil.writeGameProfile(nbttagcompound, tileentityskull.getPlayerProfile());
                        itemstack.getTagCompound().setTag("SkullOwner", nbttagcompound);
                    }

                    ret.add(itemstack);
                }
            }
        }
        return ret;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.skull;
    }

    public boolean canDispenserPlace(World worldIn, BlockPos pos, ItemStack stack)
    {
        return stack.getMetadata() == 1 && pos.getY() >= 2 && worldIn.getDifficulty() != EnumDifficulty.PEACEFUL && !worldIn.isRemote ? this.getWitherBasePattern().match(worldIn, pos) != null : false;
    }

    public void checkWitherSpawn(World worldIn, BlockPos pos, TileEntitySkull te)
    {
        if (te.getSkullType() == 1 && pos.getY() >= 2 && worldIn.getDifficulty() != EnumDifficulty.PEACEFUL && !worldIn.isRemote)
        {
            BlockPattern blockpattern = this.getWitherPattern();
            BlockPattern.PatternHelper blockpattern$patternhelper = blockpattern.match(worldIn, pos);

            if (blockpattern$patternhelper != null)
            {
                for (int i = 0; i < 3; ++i)
                {
                    BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(i, 0, 0);
                    worldIn.setBlockState(blockworldstate.getPos(), blockworldstate.getBlockState().withProperty(NODROP, Boolean.valueOf(true)), 2);
                }

                for (int j = 0; j < blockpattern.getPalmLength(); ++j)
                {
                    for (int k = 0; k < blockpattern.getThumbLength(); ++k)
                    {
                        BlockWorldState blockworldstate1 = blockpattern$patternhelper.translateOffset(j, k, 0);
                        worldIn.setBlockState(blockworldstate1.getPos(), Blocks.air.getDefaultState(), 2);
                    }
                }

                BlockPos blockpos = blockpattern$patternhelper.translateOffset(1, 0, 0).getPos();
                EntityWither entitywither = new EntityWither(worldIn);
                BlockPos blockpos1 = blockpattern$patternhelper.translateOffset(1, 2, 0).getPos();
                entitywither.setLocationAndAngles((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.55D, (double)blockpos1.getZ() + 0.5D, blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X ? 0.0F : 90.0F, 0.0F);
                entitywither.renderYawOffset = blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X ? 0.0F : 90.0F;
                entitywither.func_82206_m();

                for (EntityPlayer entityplayer : worldIn.getEntitiesWithinAABB(EntityPlayer.class, entitywither.getEntityBoundingBox().expand(50.0D, 50.0D, 50.0D)))
                {
                    entityplayer.triggerAchievement(AchievementList.spawnWither);
                }

                worldIn.spawnEntityInWorld(entitywither);

                for (int l = 0; l < 120; ++l)
                {
                    worldIn.spawnParticle(EnumParticleTypes.SNOWBALL, (double)blockpos.getX() + worldIn.rand.nextDouble(), (double)(blockpos.getY() - 2) + worldIn.rand.nextDouble() * 3.9D, (double)blockpos.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
                }

                for (int i1 = 0; i1 < blockpattern.getPalmLength(); ++i1)
                {
                    for (int j1 = 0; j1 < blockpattern.getThumbLength(); ++j1)
                    {
                        BlockWorldState blockworldstate2 = blockpattern$patternhelper.translateOffset(i1, j1, 0);
                        worldIn.notifyNeighborsRespectDebug(blockworldstate2.getPos(), Blocks.air);
                    }
                }
            }
        }
    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7)).withProperty(NODROP, Boolean.valueOf((meta & 8) > 0));
    }

    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | ((EnumFacing)state.getValue(FACING)).getIndex();

        if (((Boolean)state.getValue(NODROP)).booleanValue())
        {
            i |= 8;
        }

        return i;
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {FACING, NODROP});
    }

    protected BlockPattern getWitherBasePattern()
    {
        if (this.witherBasePattern == null)
        {
            this.witherBasePattern = FactoryBlockPattern.start().aisle(new String[] {"   ", "###", "~#~"}).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.soul_sand))).where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
        }

        return this.witherBasePattern;
    }

    protected BlockPattern getWitherPattern()
    {
        if (this.witherPattern == null)
        {
            this.witherPattern = FactoryBlockPattern.start().aisle(new String[] {"^^^", "###", "~#~"}).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.soul_sand))).where('^', IS_WITHER_SKELETON).where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
        }

        return this.witherPattern;
    }
}