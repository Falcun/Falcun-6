package net.minecraft.entity.item;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityMinecart extends Entity implements IWorldNameable
{
    private boolean isInReverse;
    private String entityName;
    private static final int[][][] matrix = new int[][][] {{{0, 0, -1}, {0, 0, 1}}, {{ -1, 0, 0}, {1, 0, 0}}, {{ -1, -1, 0}, {1, 0, 0}}, {{ -1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, { -1, 0, 0}}, {{0, 0, -1}, { -1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};
    private int turnProgress;
    private double minecartX;
    private double minecartY;
    private double minecartZ;
    private double minecartYaw;
    private double minecartPitch;
    @SideOnly(Side.CLIENT)
    private double velocityX;
    @SideOnly(Side.CLIENT)
    private double velocityY;
    @SideOnly(Side.CLIENT)
    private double velocityZ;

    /* Forge: Minecart Compatibility Layer Integration. */
    public static float defaultMaxSpeedAirLateral = 0.4f;
    public static float defaultMaxSpeedAirVertical = -1f;
    public static double defaultDragAir = 0.94999998807907104D;
    protected boolean canUseRail = true;
    protected boolean canBePushed = true;
    private static net.minecraftforge.common.IMinecartCollisionHandler collisionHandler = null;

    /* Instance versions of the above physics properties */
    private float currentSpeedRail = getMaxCartSpeedOnRail();
    protected float maxSpeedAirLateral = defaultMaxSpeedAirLateral;
    protected float maxSpeedAirVertical = defaultMaxSpeedAirVertical;
    protected double dragAir = defaultDragAir;

    public EntityMinecart(World worldIn)
    {
        super(worldIn);
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.7F);
    }

    public static EntityMinecart func_180458_a(World worldIn, double p_180458_1_, double p_180458_3_, double p_180458_5_, EntityMinecart.EnumMinecartType p_180458_7_)
    {
        switch (p_180458_7_)
        {
            case CHEST:
                return new EntityMinecartChest(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            case FURNACE:
                return new EntityMinecartFurnace(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            case TNT:
                return new EntityMinecartTNT(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            case SPAWNER:
                return new EntityMinecartMobSpawner(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            case HOPPER:
                return new EntityMinecartHopper(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            case COMMAND_BLOCK:
                return new EntityMinecartCommandBlock(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            default:
                return new EntityMinecartEmpty(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
        }
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(1));
        this.dataWatcher.addObject(19, new Float(0.0F));
        this.dataWatcher.addObject(20, new Integer(0));
        this.dataWatcher.addObject(21, new Integer(6));
        this.dataWatcher.addObject(22, Byte.valueOf((byte)0));
    }

    public AxisAlignedBB getCollisionBox(Entity entityIn)
    {
        if (getCollisionHandler() != null) return getCollisionHandler().getCollisionBox(this, entityIn);
        return entityIn.canBePushed() ? entityIn.getEntityBoundingBox() : null;
    }

    public AxisAlignedBB getCollisionBoundingBox()
    {
        if (getCollisionHandler() != null) return getCollisionHandler().getBoundingBox(this);
        return null;
    }

    public boolean canBePushed()
    {
        return canBePushed;
    }

    public EntityMinecart(World worldIn, double x, double y, double z)
    {
        this(worldIn);
        this.setPosition(x, y, z);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }

    public double getMountedYOffset()
    {
        return 0.0D;
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (!this.worldObj.isRemote && !this.isDead)
        {
            if (this.isEntityInvulnerable(source))
            {
                return false;
            }
            else
            {
                this.setRollingDirection(-this.getRollingDirection());
                this.setRollingAmplitude(10);
                this.setBeenAttacked();
                this.setDamage(this.getDamage() + amount * 10.0F);
                boolean flag = source.getEntity() instanceof EntityPlayer && ((EntityPlayer)source.getEntity()).capabilities.isCreativeMode;

                if (flag || this.getDamage() > 40.0F)
                {
                    if (this.riddenByEntity != null)
                    {
                        this.riddenByEntity.mountEntity((Entity)null);
                    }

                    if (flag && !this.hasCustomName())
                    {
                        this.setDead();
                    }
                    else
                    {
                        this.killMinecart(source);
                    }
                }

                return true;
            }
        }
        else
        {
            return true;
        }
    }

    public void killMinecart(DamageSource p_94095_1_)
    {
        this.setDead();

        if (this.worldObj.getGameRules().getBoolean("doEntityDrops"))
        {
            ItemStack itemstack = new ItemStack(Items.minecart, 1);

            if (this.entityName != null)
            {
                itemstack.setStackDisplayName(this.entityName);
            }

            this.entityDropItem(itemstack, 0.0F);
        }
    }

    @SideOnly(Side.CLIENT)
    public void performHurtAnimation()
    {
        this.setRollingDirection(-this.getRollingDirection());
        this.setRollingAmplitude(10);
        this.setDamage(this.getDamage() + this.getDamage() * 10.0F);
    }

    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    public void setDead()
    {
        super.setDead();
    }

    public void onUpdate()
    {
        if (this.getRollingAmplitude() > 0)
        {
            this.setRollingAmplitude(this.getRollingAmplitude() - 1);
        }

        if (this.getDamage() > 0.0F)
        {
            this.setDamage(this.getDamage() - 1.0F);
        }

        if (this.posY < -64.0D)
        {
            this.kill();
        }

        if (!this.worldObj.isRemote && this.worldObj instanceof WorldServer)
        {
            this.worldObj.theProfiler.startSection("portal");
            MinecraftServer minecraftserver = ((WorldServer)this.worldObj).getMinecraftServer();
            int i = this.getMaxInPortalTime();

            if (this.inPortal)
            {
                if (minecraftserver.getAllowNether())
                {
                    if (this.ridingEntity == null && this.portalCounter++ >= i)
                    {
                        this.portalCounter = i;
                        this.timeUntilPortal = this.getPortalCooldown();
                        int j;

                        if (this.worldObj.provider.getDimensionId() == -1)
                        {
                            j = 0;
                        }
                        else
                        {
                            j = -1;
                        }

                        this.travelToDimension(j);
                    }

                    this.inPortal = false;
                }
            }
            else
            {
                if (this.portalCounter > 0)
                {
                    this.portalCounter -= 4;
                }

                if (this.portalCounter < 0)
                {
                    this.portalCounter = 0;
                }
            }

            if (this.timeUntilPortal > 0)
            {
                --this.timeUntilPortal;
            }

            this.worldObj.theProfiler.endSection();
        }

        if (this.worldObj.isRemote)
        {
            if (this.turnProgress > 0)
            {
                double d4 = this.posX + (this.minecartX - this.posX) / (double)this.turnProgress;
                double d5 = this.posY + (this.minecartY - this.posY) / (double)this.turnProgress;
                double d6 = this.posZ + (this.minecartZ - this.posZ) / (double)this.turnProgress;
                double d1 = MathHelper.wrapAngleTo180_double(this.minecartYaw - (double)this.rotationYaw);
                this.rotationYaw = (float)((double)this.rotationYaw + d1 / (double)this.turnProgress);
                this.rotationPitch = (float)((double)this.rotationPitch + (this.minecartPitch - (double)this.rotationPitch) / (double)this.turnProgress);
                --this.turnProgress;
                this.setPosition(d4, d5, d6);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
            else
            {
                this.setPosition(this.posX, this.posY, this.posZ);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
        }
        else
        {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.motionY -= 0.03999999910593033D;
            int k = MathHelper.floor_double(this.posX);
            int l = MathHelper.floor_double(this.posY);
            int i1 = MathHelper.floor_double(this.posZ);

            if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(k, l - 1, i1)))
            {
                --l;
            }

            BlockPos blockpos = new BlockPos(k, l, i1);
            IBlockState iblockstate = this.worldObj.getBlockState(blockpos);

            if (canUseRail() && BlockRailBase.isRailBlock(iblockstate))
            {
                this.func_180460_a(blockpos, iblockstate);

                if (iblockstate.getBlock() == Blocks.activator_rail)
                {
                    this.onActivatorRailPass(k, l, i1, ((Boolean)iblockstate.getValue(BlockRailPowered.POWERED)).booleanValue());
                }
            }
            else
            {
                this.moveDerailedMinecart();
            }

            this.doBlockCollisions();
            this.rotationPitch = 0.0F;
            double d0 = this.prevPosX - this.posX;
            double d2 = this.prevPosZ - this.posZ;

            if (d0 * d0 + d2 * d2 > 0.001D)
            {
                this.rotationYaw = (float)(MathHelper.atan2(d2, d0) * 180.0D / Math.PI);

                if (this.isInReverse)
                {
                    this.rotationYaw += 180.0F;
                }
            }

            double d3 = (double)MathHelper.wrapAngleTo180_float(this.rotationYaw - this.prevRotationYaw);

            if (d3 < -170.0D || d3 >= 170.0D)
            {
                this.rotationYaw += 180.0F;
                this.isInReverse = !this.isInReverse;
            }

            this.setRotation(this.rotationYaw, this.rotationPitch);

            AxisAlignedBB box;
            if (getCollisionHandler() != null) box = getCollisionHandler().getMinecartCollisionBox(this);
            else                               box = this.getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D);
            for (Entity entity : this.worldObj.getEntitiesWithinAABBExcludingEntity(this, box))
            {
                if (entity != this.riddenByEntity && entity.canBePushed() && entity instanceof EntityMinecart)
                {
                    entity.applyEntityCollision(this);
                }
            }

            if (this.riddenByEntity != null && this.riddenByEntity.isDead)
            {
                if (this.riddenByEntity.ridingEntity == this)
                {
                    this.riddenByEntity.ridingEntity = null;
                }

                this.riddenByEntity = null;
            }

            this.handleWaterMovement();
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.minecart.MinecartUpdateEvent(this, this.getCurrentRailPosition()));
        }
    }

    protected double getMaximumSpeed()
    {
        return 0.4D;
    }

    public void onActivatorRailPass(int x, int y, int z, boolean receivingPower)
    {
    }

    protected void moveDerailedMinecart()
    {
        double d0 = onGround ? this.getMaximumSpeed() : getMaxSpeedAirLateral();
        this.motionX = MathHelper.clamp_double(this.motionX, -d0, d0);
        this.motionZ = MathHelper.clamp_double(this.motionZ, -d0, d0);

        double moveY = motionY;
        if(getMaxSpeedAirVertical() > 0 && motionY > getMaxSpeedAirVertical())
        {
            moveY = getMaxSpeedAirVertical();
            if(Math.abs(motionX) < 0.3f && Math.abs(motionZ) < 0.3f)
            {
                moveY = 0.15f;
                motionY = moveY;
            }
        }

        if (this.onGround)
        {
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        }

        this.moveEntity(this.motionX, moveY, this.motionZ);

        if (!this.onGround)
        {
            this.motionX *= getDragAir();
            this.motionY *= getDragAir();
            this.motionZ *= getDragAir();
        }
    }

    @SuppressWarnings("incomplete-switch")
    protected void func_180460_a(BlockPos p_180460_1_, IBlockState p_180460_2_)
    {
        this.fallDistance = 0.0F;
        Vec3 vec3 = this.func_70489_a(this.posX, this.posY, this.posZ);
        this.posY = (double)p_180460_1_.getY();
        boolean flag = false;
        boolean flag1 = false;
        BlockRailBase blockrailbase = (BlockRailBase)p_180460_2_.getBlock();

        if (blockrailbase == Blocks.golden_rail)
        {
            flag = ((Boolean)p_180460_2_.getValue(BlockRailPowered.POWERED)).booleanValue();
            flag1 = !flag;
        }

        double slopeAdjustment = getSlopeAdjustment();
        BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = blockrailbase.getRailDirection(worldObj, p_180460_1_, p_180460_2_, this);

        switch (blockrailbase$enumraildirection)
        {
            case ASCENDING_EAST:
                this.motionX -= slopeAdjustment;
                ++this.posY;
                break;
            case ASCENDING_WEST:
                this.motionX += slopeAdjustment;
                ++this.posY;
                break;
            case ASCENDING_NORTH:
                this.motionZ += slopeAdjustment;
                ++this.posY;
                break;
            case ASCENDING_SOUTH:
                this.motionZ -= slopeAdjustment;
                ++this.posY;
        }

        int[][] aint = matrix[blockrailbase$enumraildirection.getMetadata()];
        double d1 = (double)(aint[1][0] - aint[0][0]);
        double d2 = (double)(aint[1][2] - aint[0][2]);
        double d3 = Math.sqrt(d1 * d1 + d2 * d2);
        double d4 = this.motionX * d1 + this.motionZ * d2;

        if (d4 < 0.0D)
        {
            d1 = -d1;
            d2 = -d2;
        }

        double d5 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

        if (d5 > 2.0D)
        {
            d5 = 2.0D;
        }

        this.motionX = d5 * d1 / d3;
        this.motionZ = d5 * d2 / d3;

        if (this.riddenByEntity instanceof EntityLivingBase)
        {
            double d6 = (double)((EntityLivingBase)this.riddenByEntity).moveForward;

            if (d6 > 0.0D)
            {
                double d7 = -Math.sin((double)(this.riddenByEntity.rotationYaw * (float)Math.PI / 180.0F));
                double d8 = Math.cos((double)(this.riddenByEntity.rotationYaw * (float)Math.PI / 180.0F));
                double d9 = this.motionX * this.motionX + this.motionZ * this.motionZ;

                if (d9 < 0.01D)
                {
                    this.motionX += d7 * 0.1D;
                    this.motionZ += d8 * 0.1D;
                    flag1 = false;
                }
            }
        }

        if (flag1 && shouldDoRailFunctions())
        {
            double d17 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

            if (d17 < 0.03D)
            {
                this.motionX *= 0.0D;
                this.motionY *= 0.0D;
                this.motionZ *= 0.0D;
            }
            else
            {
                this.motionX *= 0.5D;
                this.motionY *= 0.0D;
                this.motionZ *= 0.5D;
            }
        }

        double d18 = 0.0D;
        double d19 = (double)p_180460_1_.getX() + 0.5D + (double)aint[0][0] * 0.5D;
        double d20 = (double)p_180460_1_.getZ() + 0.5D + (double)aint[0][2] * 0.5D;
        double d21 = (double)p_180460_1_.getX() + 0.5D + (double)aint[1][0] * 0.5D;
        double d10 = (double)p_180460_1_.getZ() + 0.5D + (double)aint[1][2] * 0.5D;
        d1 = d21 - d19;
        d2 = d10 - d20;

        if (d1 == 0.0D)
        {
            this.posX = (double)p_180460_1_.getX() + 0.5D;
            d18 = this.posZ - (double)p_180460_1_.getZ();
        }
        else if (d2 == 0.0D)
        {
            this.posZ = (double)p_180460_1_.getZ() + 0.5D;
            d18 = this.posX - (double)p_180460_1_.getX();
        }
        else
        {
            double d11 = this.posX - d19;
            double d12 = this.posZ - d20;
            d18 = (d11 * d1 + d12 * d2) * 2.0D;
        }

        this.posX = d19 + d1 * d18;
        this.posZ = d20 + d2 * d18;
        this.setPosition(this.posX, this.posY, this.posZ);
        this.moveMinecartOnRail(p_180460_1_);

        if (aint[0][1] != 0 && MathHelper.floor_double(this.posX) - p_180460_1_.getX() == aint[0][0] && MathHelper.floor_double(this.posZ) - p_180460_1_.getZ() == aint[0][2])
        {
            this.setPosition(this.posX, this.posY + (double)aint[0][1], this.posZ);
        }
        else if (aint[1][1] != 0 && MathHelper.floor_double(this.posX) - p_180460_1_.getX() == aint[1][0] && MathHelper.floor_double(this.posZ) - p_180460_1_.getZ() == aint[1][2])
        {
            this.setPosition(this.posX, this.posY + (double)aint[1][1], this.posZ);
        }

        this.applyDrag();
        Vec3 vec31 = this.func_70489_a(this.posX, this.posY, this.posZ);

        if (vec31 != null && vec3 != null)
        {
            double d14 = (vec3.yCoord - vec31.yCoord) * 0.05D;
            d5 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

            if (d5 > 0.0D)
            {
                this.motionX = this.motionX / d5 * (d5 + d14);
                this.motionZ = this.motionZ / d5 * (d5 + d14);
            }

            this.setPosition(this.posX, vec31.yCoord, this.posZ);
        }

        int j = MathHelper.floor_double(this.posX);
        int i = MathHelper.floor_double(this.posZ);

        if (j != p_180460_1_.getX() || i != p_180460_1_.getZ())
        {
            d5 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.motionX = d5 * (double)(j - p_180460_1_.getX());
            this.motionZ = d5 * (double)(i - p_180460_1_.getZ());
        }


        if(shouldDoRailFunctions())
        {
            ((BlockRailBase)p_180460_2_.getBlock()).onMinecartPass(worldObj, this, p_180460_1_);
        }

        if (flag && shouldDoRailFunctions())
        {
            double d15 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

            if (d15 > 0.01D)
            {
                double d16 = 0.06D;
                this.motionX += this.motionX / d15 * d16;
                this.motionZ += this.motionZ / d15 * d16;
            }
            else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST)
            {
                if (this.worldObj.getBlockState(p_180460_1_.west()).getBlock().isNormalCube())
                {
                    this.motionX = 0.02D;
                }
                else if (this.worldObj.getBlockState(p_180460_1_.east()).getBlock().isNormalCube())
                {
                    this.motionX = -0.02D;
                }
            }
            else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH)
            {
                if (this.worldObj.getBlockState(p_180460_1_.north()).getBlock().isNormalCube())
                {
                    this.motionZ = 0.02D;
                }
                else if (this.worldObj.getBlockState(p_180460_1_.south()).getBlock().isNormalCube())
                {
                    this.motionZ = -0.02D;
                }
            }
        }
    }

    protected void applyDrag()
    {
        if (this.riddenByEntity != null)
        {
            this.motionX *= 0.996999979019165D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.996999979019165D;
        }
        else
        {
            this.motionX *= 0.9599999785423279D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.9599999785423279D;
        }
    }

    public void setPosition(double x, double y, double z)
    {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        float f = this.width / 2.0F;
        float f1 = this.height;
        this.setEntityBoundingBox(new AxisAlignedBB(x - (double)f, y, z - (double)f, x + (double)f, y + (double)f1, z + (double)f));
    }

    @SideOnly(Side.CLIENT)
    public Vec3 func_70495_a(double p_70495_1_, double p_70495_3_, double p_70495_5_, double p_70495_7_)
    {
        int i = MathHelper.floor_double(p_70495_1_);
        int j = MathHelper.floor_double(p_70495_3_);
        int k = MathHelper.floor_double(p_70495_5_);

        if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(i, j - 1, k)))
        {
            --j;
        }

        IBlockState iblockstate = this.worldObj.getBlockState(new BlockPos(i, j, k));

        if (BlockRailBase.isRailBlock(iblockstate))
        {
            BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = ((BlockRailBase)iblockstate.getBlock()).getRailDirection(worldObj, new BlockPos(i, j, k), iblockstate, this);
            p_70495_3_ = (double)j;

            if (blockrailbase$enumraildirection.isAscending())
            {
                p_70495_3_ = (double)(j + 1);
            }

            int[][] aint = matrix[blockrailbase$enumraildirection.getMetadata()];
            double d0 = (double)(aint[1][0] - aint[0][0]);
            double d1 = (double)(aint[1][2] - aint[0][2]);
            double d2 = Math.sqrt(d0 * d0 + d1 * d1);
            d0 = d0 / d2;
            d1 = d1 / d2;
            p_70495_1_ = p_70495_1_ + d0 * p_70495_7_;
            p_70495_5_ = p_70495_5_ + d1 * p_70495_7_;

            if (aint[0][1] != 0 && MathHelper.floor_double(p_70495_1_) - i == aint[0][0] && MathHelper.floor_double(p_70495_5_) - k == aint[0][2])
            {
                p_70495_3_ += (double)aint[0][1];
            }
            else if (aint[1][1] != 0 && MathHelper.floor_double(p_70495_1_) - i == aint[1][0] && MathHelper.floor_double(p_70495_5_) - k == aint[1][2])
            {
                p_70495_3_ += (double)aint[1][1];
            }

            return this.func_70489_a(p_70495_1_, p_70495_3_, p_70495_5_);
        }
        else
        {
            return null;
        }
    }

    public Vec3 func_70489_a(double p_70489_1_, double p_70489_3_, double p_70489_5_)
    {
        int i = MathHelper.floor_double(p_70489_1_);
        int j = MathHelper.floor_double(p_70489_3_);
        int k = MathHelper.floor_double(p_70489_5_);

        if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(i, j - 1, k)))
        {
            --j;
        }

        IBlockState iblockstate = this.worldObj.getBlockState(new BlockPos(i, j, k));

        if (BlockRailBase.isRailBlock(iblockstate))
        {
            BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = ((BlockRailBase)iblockstate.getBlock()).getRailDirection(worldObj, new BlockPos(i, j, k), iblockstate, this);
            int[][] aint = matrix[blockrailbase$enumraildirection.getMetadata()];
            double d0 = 0.0D;
            double d1 = (double)i + 0.5D + (double)aint[0][0] * 0.5D;
            double d2 = (double)j + 0.0625D + (double)aint[0][1] * 0.5D;
            double d3 = (double)k + 0.5D + (double)aint[0][2] * 0.5D;
            double d4 = (double)i + 0.5D + (double)aint[1][0] * 0.5D;
            double d5 = (double)j + 0.0625D + (double)aint[1][1] * 0.5D;
            double d6 = (double)k + 0.5D + (double)aint[1][2] * 0.5D;
            double d7 = d4 - d1;
            double d8 = (d5 - d2) * 2.0D;
            double d9 = d6 - d3;

            if (d7 == 0.0D)
            {
                p_70489_1_ = (double)i + 0.5D;
                d0 = p_70489_5_ - (double)k;
            }
            else if (d9 == 0.0D)
            {
                p_70489_5_ = (double)k + 0.5D;
                d0 = p_70489_1_ - (double)i;
            }
            else
            {
                double d10 = p_70489_1_ - d1;
                double d11 = p_70489_5_ - d3;
                d0 = (d10 * d7 + d11 * d9) * 2.0D;
            }

            p_70489_1_ = d1 + d7 * d0;
            p_70489_3_ = d2 + d8 * d0;
            p_70489_5_ = d3 + d9 * d0;

            if (d8 < 0.0D)
            {
                ++p_70489_3_;
            }

            if (d8 > 0.0D)
            {
                p_70489_3_ += 0.5D;
            }

            return new Vec3(p_70489_1_, p_70489_3_, p_70489_5_);
        }
        else
        {
            return null;
        }
    }

    protected void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        if (tagCompund.getBoolean("CustomDisplayTile"))
        {
            int i = tagCompund.getInteger("DisplayData");

            if (tagCompund.hasKey("DisplayTile", 8))
            {
                Block block = Block.getBlockFromName(tagCompund.getString("DisplayTile"));

                if (block == null)
                {
                    this.func_174899_a(Blocks.air.getDefaultState());
                }
                else
                {
                    this.func_174899_a(block.getStateFromMeta(i));
                }
            }
            else
            {
                Block block1 = Block.getBlockById(tagCompund.getInteger("DisplayTile"));

                if (block1 == null)
                {
                    this.func_174899_a(Blocks.air.getDefaultState());
                }
                else
                {
                    this.func_174899_a(block1.getStateFromMeta(i));
                }
            }

            this.setDisplayTileOffset(tagCompund.getInteger("DisplayOffset"));
        }

        if (tagCompund.hasKey("CustomName", 8) && tagCompund.getString("CustomName").length() > 0)
        {
            this.entityName = tagCompund.getString("CustomName");
        }
    }

    protected void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        if (this.hasDisplayTile())
        {
            tagCompound.setBoolean("CustomDisplayTile", true);
            IBlockState iblockstate = this.getDisplayTile();
            ResourceLocation resourcelocation = (ResourceLocation)Block.blockRegistry.getNameForObject(iblockstate.getBlock());
            tagCompound.setString("DisplayTile", resourcelocation == null ? "" : resourcelocation.toString());
            tagCompound.setInteger("DisplayData", iblockstate.getBlock().getMetaFromState(iblockstate));
            tagCompound.setInteger("DisplayOffset", this.getDisplayTileOffset());
        }

        if (this.entityName != null && this.entityName.length() > 0)
        {
            tagCompound.setString("CustomName", this.entityName);
        }
    }

    public void applyEntityCollision(Entity entityIn)
    {

        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.minecart.MinecartCollisionEvent(this, entityIn));
        if (getCollisionHandler() != null)
        {
            getCollisionHandler().onEntityCollision(this, entityIn);
            return;
        }
        if (!this.worldObj.isRemote)
        {
            if (!entityIn.noClip && !this.noClip)
            {
                if (entityIn != this.riddenByEntity)
                {
                    if (entityIn instanceof EntityLivingBase && !(entityIn instanceof EntityPlayer) && !(entityIn instanceof EntityIronGolem) && canBeRidden() && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.01D && this.riddenByEntity == null && entityIn.ridingEntity == null)
                    {
                        entityIn.mountEntity(this);
                    }

                    double d0 = entityIn.posX - this.posX;
                    double d1 = entityIn.posZ - this.posZ;
                    double d2 = d0 * d0 + d1 * d1;

                    if (d2 >= 9.999999747378752E-5D)
                    {
                        d2 = (double)MathHelper.sqrt_double(d2);
                        d0 = d0 / d2;
                        d1 = d1 / d2;
                        double d3 = 1.0D / d2;

                        if (d3 > 1.0D)
                        {
                            d3 = 1.0D;
                        }

                        d0 = d0 * d3;
                        d1 = d1 * d3;
                        d0 = d0 * 0.10000000149011612D;
                        d1 = d1 * 0.10000000149011612D;
                        d0 = d0 * (double)(1.0F - this.entityCollisionReduction);
                        d1 = d1 * (double)(1.0F - this.entityCollisionReduction);
                        d0 = d0 * 0.5D;
                        d1 = d1 * 0.5D;

                        if (entityIn instanceof EntityMinecart)
                        {
                            double d4 = entityIn.posX - this.posX;
                            double d5 = entityIn.posZ - this.posZ;
                            Vec3 vec3 = (new Vec3(d4, 0.0D, d5)).normalize();
                            Vec3 vec31 = (new Vec3((double)MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F), 0.0D, (double)MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F))).normalize();
                            double d6 = Math.abs(vec3.dotProduct(vec31));

                            if (d6 < 0.800000011920929D)
                            {
                                return;
                            }

                            double d7 = entityIn.motionX + this.motionX;
                            double d8 = entityIn.motionZ + this.motionZ;

                            if (((EntityMinecart)entityIn).isPoweredCart() && !isPoweredCart())
                            {
                                this.motionX *= 0.20000000298023224D;
                                this.motionZ *= 0.20000000298023224D;
                                this.addVelocity(entityIn.motionX - d0, 0.0D, entityIn.motionZ - d1);
                                entityIn.motionX *= 0.949999988079071D;
                                entityIn.motionZ *= 0.949999988079071D;
                            }
                            else if (((EntityMinecart)entityIn).isPoweredCart() && isPoweredCart())
                            {
                                entityIn.motionX *= 0.20000000298023224D;
                                entityIn.motionZ *= 0.20000000298023224D;
                                entityIn.addVelocity(this.motionX + d0, 0.0D, this.motionZ + d1);
                                this.motionX *= 0.949999988079071D;
                                this.motionZ *= 0.949999988079071D;
                            }
                            else
                            {
                                d7 = d7 / 2.0D;
                                d8 = d8 / 2.0D;
                                this.motionX *= 0.20000000298023224D;
                                this.motionZ *= 0.20000000298023224D;
                                this.addVelocity(d7 - d0, 0.0D, d8 - d1);
                                entityIn.motionX *= 0.20000000298023224D;
                                entityIn.motionZ *= 0.20000000298023224D;
                                entityIn.addVelocity(d7 + d0, 0.0D, d8 + d1);
                            }
                        }
                        else
                        {
                            this.addVelocity(-d0, 0.0D, -d1);
                            entityIn.addVelocity(d0 / 4.0D, 0.0D, d1 / 4.0D);
                        }
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_)
    {
        this.minecartX = x;
        this.minecartY = y;
        this.minecartZ = z;
        this.minecartYaw = (double)yaw;
        this.minecartPitch = (double)pitch;
        this.turnProgress = posRotationIncrements + 2;
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }

    public void setDamage(float p_70492_1_)
    {
        this.dataWatcher.updateObject(19, Float.valueOf(p_70492_1_));
    }

    @SideOnly(Side.CLIENT)
    public void setVelocity(double x, double y, double z)
    {
        this.velocityX = this.motionX = x;
        this.velocityY = this.motionY = y;
        this.velocityZ = this.motionZ = z;
    }

    public float getDamage()
    {
        return this.dataWatcher.getWatchableObjectFloat(19);
    }

    public void setRollingAmplitude(int p_70497_1_)
    {
        this.dataWatcher.updateObject(17, Integer.valueOf(p_70497_1_));
    }

    public int getRollingAmplitude()
    {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    public void setRollingDirection(int p_70494_1_)
    {
        this.dataWatcher.updateObject(18, Integer.valueOf(p_70494_1_));
    }

    public int getRollingDirection()
    {
        return this.dataWatcher.getWatchableObjectInt(18);
    }

    public abstract EntityMinecart.EnumMinecartType getMinecartType();

    public IBlockState getDisplayTile()
    {
        return !this.hasDisplayTile() ? this.getDefaultDisplayTile() : Block.getStateById(this.getDataWatcher().getWatchableObjectInt(20));
    }

    public IBlockState getDefaultDisplayTile()
    {
        return Blocks.air.getDefaultState();
    }

    public int getDisplayTileOffset()
    {
        return !this.hasDisplayTile() ? this.getDefaultDisplayTileOffset() : this.getDataWatcher().getWatchableObjectInt(21);
    }

    public int getDefaultDisplayTileOffset()
    {
        return 6;
    }

    public void func_174899_a(IBlockState p_174899_1_)
    {
        this.getDataWatcher().updateObject(20, Integer.valueOf(Block.getStateId(p_174899_1_)));
        this.setHasDisplayTile(true);
    }

    public void setDisplayTileOffset(int p_94086_1_)
    {
        this.getDataWatcher().updateObject(21, Integer.valueOf(p_94086_1_));
        this.setHasDisplayTile(true);
    }

    public boolean hasDisplayTile()
    {
        return this.getDataWatcher().getWatchableObjectByte(22) == 1;
    }

    public void setHasDisplayTile(boolean p_94096_1_)
    {
        this.getDataWatcher().updateObject(22, Byte.valueOf((byte)(p_94096_1_ ? 1 : 0)));
    }

    public void setCustomNameTag(String name)
    {
        this.entityName = name;
    }

    public String getName()
    {
        return this.entityName != null ? this.entityName : super.getName();
    }

    public boolean hasCustomName()
    {
        return this.entityName != null;
    }

    public String getCustomNameTag()
    {
        return this.entityName;
    }

    public IChatComponent getDisplayName()
    {
        if (this.hasCustomName())
        {
            ChatComponentText chatcomponenttext = new ChatComponentText(this.entityName);
            chatcomponenttext.getChatStyle().setChatHoverEvent(this.getHoverEvent());
            chatcomponenttext.getChatStyle().setInsertion(this.getUniqueID().toString());
            return chatcomponenttext;
        }
        else
        {
            ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(this.getName(), new Object[0]);
            chatcomponenttranslation.getChatStyle().setChatHoverEvent(this.getHoverEvent());
            chatcomponenttranslation.getChatStyle().setInsertion(this.getUniqueID().toString());
            return chatcomponenttranslation;
        }
    }

    /* =================================== FORGE START ===========================================*/
    private BlockPos getCurrentRailPosition()
    {
        int x = MathHelper.floor_double(this.posX);
        int y = MathHelper.floor_double(this.posY);
        int z = MathHelper.floor_double(this.posZ);

        if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(x, y - 1, z))) y--;
        return new BlockPos(x, y, z);
    }

    protected double getMaxSpeed()
    {
        if (!canUseRail()) return getMaximumSpeed();
        BlockPos pos = this.getCurrentRailPosition();
        IBlockState state = this.worldObj.getBlockState(pos);
        if (!BlockRailBase.isRailBlock(state)) return getMaximumSpeed();

        float railMaxSpeed = ((BlockRailBase)state.getBlock()).getRailMaxSpeed(worldObj, this, pos);
        return Math.min(railMaxSpeed, getCurrentCartSpeedCapOnRail());
    }

    /**
     * Moved to allow overrides.
     * This code handles minecart movement and speed capping when on a rail.
     */
    public void moveMinecartOnRail(BlockPos pos)
    {
        double mX = this.motionX;
        double mZ = this.motionZ;

        if (this.riddenByEntity != null)
        {
            mX *= 0.75D;
            mZ *= 0.75D;
        }

        double max = this.getMaxSpeed();
        mX = MathHelper.clamp_double(mX, -max, max);
        mZ = MathHelper.clamp_double(mZ, -max, max);
        this.moveEntity(mX, 0.0D, mZ);
    }

    /**
     * Gets the current global Minecart Collision handler if none
     * is registered, returns null
     * @return The collision handler or null
     */
    public static net.minecraftforge.common.IMinecartCollisionHandler getCollisionHandler()
    {
        return collisionHandler;
    }

    /**
     * Sets the global Minecart Collision handler, overwrites any
     * that is currently set.
     * @param handler The new handler
     */
    public static void setCollisionHandler(net.minecraftforge.common.IMinecartCollisionHandler handler)
    {
        collisionHandler = handler;
    }

    /**
     * This function returns an ItemStack that represents this cart.
     * This should be an ItemStack that can be used by the player to place the cart,
     * but is not necessary the item the cart drops when destroyed.
     * @return An ItemStack that can be used to place the cart.
     */
    public ItemStack getCartItem()
    {
        if (this instanceof EntityMinecartFurnace)
        {
            return new ItemStack(Items.furnace_minecart);
        }
        else if (this instanceof EntityMinecartChest)
        {
            return new ItemStack(Items.chest_minecart);
        }
        else if (this instanceof EntityMinecartTNT)
        {
            return new ItemStack(Items.tnt_minecart);
        }
        else if (this instanceof EntityMinecartHopper)
        {
            return new ItemStack(Items.hopper_minecart);
        }
        else if (this instanceof EntityMinecartCommandBlock)
        {
            return new ItemStack(Items.command_block_minecart);
        }
        return new ItemStack(Items.minecart);
    }

    /**
     * Returns true if this cart can currently use rails.
     * This function is mainly used to gracefully detach a minecart from a rail.
     * @return True if the minecart can use rails.
     */
    public boolean canUseRail()
    {
        return canUseRail;
    }

    /**
     * Set whether the minecart can use rails.
     * This function is mainly used to gracefully detach a minecart from a rail.
     * @param use Whether the minecart can currently use rails.
     */
    public void setCanUseRail(boolean use)
    {
        canUseRail = use;
    }

    /**
     * Return false if this cart should not call onMinecartPass() and should ignore Powered Rails.
     * @return True if this cart should call onMinecartPass().
     */
    public boolean shouldDoRailFunctions()
    {
        return true;
    }

    /**
     * Returns true if this cart is self propelled.
     * @return True if powered.
     */
    public boolean isPoweredCart()
    {
        return getMinecartType() == EntityMinecart.EnumMinecartType.FURNACE;
    }

    /**
     * Returns true if this cart can be ridden by an Entity.
     * @return True if this cart can be ridden.
     */
    public boolean canBeRidden()
    {
        return this.getMinecartType() == EntityMinecart.EnumMinecartType.RIDEABLE;
    }

    /**
     * Getters/setters for physics variables
     */

    /**
     * Returns the carts max speed when traveling on rails. Carts going faster
     * than 1.1 cause issues with chunk loading. Carts cant traverse slopes or
     * corners at greater than 0.5 - 0.6. This value is compared with the rails
     * max speed and the carts current speed cap to determine the carts current
     * max speed. A normal rail's max speed is 0.4.
     *
     * @return Carts max speed.
     */
    public float getMaxCartSpeedOnRail()
    {
        return 1.2f;
    }

    /**
     * Returns the current speed cap for the cart when traveling on rails. This
     * functions differs from getMaxCartSpeedOnRail() in that it controls
     * current movement and cannot be overridden. The value however can never be
     * higher than getMaxCartSpeedOnRail().
     *
     * @return
     */
    public final float getCurrentCartSpeedCapOnRail()
    {
        return currentSpeedRail;
    }

    public final void setCurrentCartSpeedCapOnRail(float value)
    {
        value = Math.min(value, getMaxCartSpeedOnRail());
        currentSpeedRail = value;
    }

    public float getMaxSpeedAirLateral()
    {
        return maxSpeedAirLateral;
    }

    public void setMaxSpeedAirLateral(float value)
    {
        maxSpeedAirLateral = value;
    }

    public float getMaxSpeedAirVertical()
    {
        return maxSpeedAirVertical;
    }

    public void setMaxSpeedAirVertical(float value)
    {
        maxSpeedAirVertical = value;
    }

    public double getDragAir()
    {
        return dragAir;
    }

    public void setDragAir(double value)
    {
        dragAir = value;
    }

    public double getSlopeAdjustment()
    {
        return 0.0078125D;
    }

    /* =================================== FORGE END ===========================================*/

    public static enum EnumMinecartType
    {
        RIDEABLE(0, "MinecartRideable"),
        CHEST(1, "MinecartChest"),
        FURNACE(2, "MinecartFurnace"),
        TNT(3, "MinecartTNT"),
        SPAWNER(4, "MinecartSpawner"),
        HOPPER(5, "MinecartHopper"),
        COMMAND_BLOCK(6, "MinecartCommandBlock");

        private static final Map<Integer, EntityMinecart.EnumMinecartType> ID_LOOKUP = Maps.<Integer, EntityMinecart.EnumMinecartType>newHashMap();
        private final int networkID;
        private final String name;

        private EnumMinecartType(int networkID, String name)
        {
            this.networkID = networkID;
            this.name = name;
        }

        public int getNetworkID()
        {
            return this.networkID;
        }

        public String getName()
        {
            return this.name;
        }

        public static EntityMinecart.EnumMinecartType byNetworkID(int id)
        {
            EntityMinecart.EnumMinecartType entityminecart$enumminecarttype = (EntityMinecart.EnumMinecartType)ID_LOOKUP.get(Integer.valueOf(id));
            return entityminecart$enumminecarttype == null ? RIDEABLE : entityminecart$enumminecarttype;
        }

        static
        {
            for (EntityMinecart.EnumMinecartType entityminecart$enumminecarttype : values())
            {
                ID_LOOKUP.put(Integer.valueOf(entityminecart$enumminecarttype.getNetworkID()), entityminecart$enumminecarttype);
            }
        }
    }
}