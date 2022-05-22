package net.minecraft.server.management;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;

public class ItemInWorldManager
{
    /** Forge reach distance */
    private double blockReachDistance = 5.0d;
    public World theWorld;
    public EntityPlayerMP thisPlayerMP;
    private WorldSettings.GameType gameType = WorldSettings.GameType.NOT_SET;
    private boolean isDestroyingBlock;
    private int initialDamage;
    private BlockPos field_180240_f = BlockPos.ORIGIN;
    private int curblockDamage;
    private boolean receivedFinishDiggingPacket;
    private BlockPos field_180241_i = BlockPos.ORIGIN;
    private int initialBlockDamage;
    private int durabilityRemainingOnBlock = -1;

    public ItemInWorldManager(World worldIn)
    {
        this.theWorld = worldIn;
    }

    public void setGameType(WorldSettings.GameType type)
    {
        this.gameType = type;
        type.configurePlayerCapabilities(this.thisPlayerMP.capabilities);
        this.thisPlayerMP.sendPlayerAbilities();
        this.thisPlayerMP.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.UPDATE_GAME_MODE, new EntityPlayerMP[] {this.thisPlayerMP}));
    }

    public WorldSettings.GameType getGameType()
    {
        return this.gameType;
    }

    public boolean survivalOrAdventure()
    {
        return this.gameType.isSurvivalOrAdventure();
    }

    public boolean isCreative()
    {
        return this.gameType.isCreative();
    }

    public void initializeGameType(WorldSettings.GameType type)
    {
        if (this.gameType == WorldSettings.GameType.NOT_SET)
        {
            this.gameType = type;
        }

        this.setGameType(this.gameType);
    }

    public void updateBlockRemoving()
    {
        ++this.curblockDamage;

        if (this.receivedFinishDiggingPacket)
        {
            int i = this.curblockDamage - this.initialBlockDamage;
            Block block = this.theWorld.getBlockState(this.field_180241_i).getBlock();

            if (block.getMaterial() == Material.air)
            {
                this.receivedFinishDiggingPacket = false;
            }
            else
            {
                float f = block.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, this.field_180241_i) * (float)(i + 1);
                int j = (int)(f * 10.0F);

                if (j != this.durabilityRemainingOnBlock)
                {
                    this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180241_i, j);
                    this.durabilityRemainingOnBlock = j;
                }

                if (f >= 1.0F)
                {
                    this.receivedFinishDiggingPacket = false;
                    this.tryHarvestBlock(this.field_180241_i);
                }
            }
        }
        else if (this.isDestroyingBlock)
        {
            Block block1 = this.theWorld.getBlockState(this.field_180240_f).getBlock();

            if (block1.getMaterial() == Material.air)
            {
                this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, -1);
                this.durabilityRemainingOnBlock = -1;
                this.isDestroyingBlock = false;
            }
            else
            {
                int k = this.curblockDamage - this.initialDamage;
                float f1 = block1.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, this.field_180240_f) * (float)(k + 1); //Forge: Fix network break progress using wrong position
                int l = (int)(f1 * 10.0F);

                if (l != this.durabilityRemainingOnBlock)
                {
                    this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, l);
                    this.durabilityRemainingOnBlock = l;
                }
            }
        }
    }

    public void onBlockClicked(BlockPos pos, EnumFacing side)
    {
        net.minecraftforge.event.entity.player.PlayerInteractEvent event = net.minecraftforge.event.ForgeEventFactory.onPlayerInteract(thisPlayerMP,
                net.minecraftforge.event.entity.player.PlayerInteractEvent.Action.LEFT_CLICK_BLOCK, theWorld, pos, side, net.minecraftforge.common.ForgeHooks.rayTraceEyeHitVec(thisPlayerMP, getBlockReachDistance() + 1));
        if (event.isCanceled())
        {
            thisPlayerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(theWorld, pos));
            return;
        }

        if (this.isCreative())
        {
            if (!this.theWorld.extinguishFire((EntityPlayer)null, pos, side))
            {
                this.tryHarvestBlock(pos);
            }
        }
        else
        {
            Block block = this.theWorld.getBlockState(pos).getBlock();

            if (this.gameType.isAdventure())
            {
                if (this.gameType == WorldSettings.GameType.SPECTATOR)
                {
                    return;
                }

                if (!this.thisPlayerMP.isAllowEdit())
                {
                    ItemStack itemstack = this.thisPlayerMP.getCurrentEquippedItem();

                    if (itemstack == null)
                    {
                        return;
                    }

                    if (!itemstack.canDestroy(block))
                    {
                        return;
                    }
                }
            }

            //
            this.initialDamage = this.curblockDamage;
            float f = 1.0F;

            if (!block.isAir(theWorld, pos))
            {
                if (event.useBlock != net.minecraftforge.fml.common.eventhandler.Event.Result.DENY)
                {
                    block.onBlockClicked(this.theWorld, pos, this.thisPlayerMP);
                    this.theWorld.extinguishFire((EntityPlayer)null, pos, side);
                }
                else
                {
                    thisPlayerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(theWorld, pos));
                }
                f = block.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, pos);
            }

            if (event.useItem == net.minecraftforge.fml.common.eventhandler.Event.Result.DENY)
            {
                if (f >= 1.0F)
                {
                    thisPlayerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(theWorld, pos));
                }
                return;
            }

            if (!block.isAir(theWorld, pos) && f >= 1.0F)
            {
                this.tryHarvestBlock(pos);
            }
            else
            {
                this.isDestroyingBlock = true;
                this.field_180240_f = pos;
                int i = (int)(f * 10.0F);
                this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), pos, i);
                this.durabilityRemainingOnBlock = i;
            }
        }
    }

    public void blockRemoving(BlockPos pos)
    {
        if (pos.equals(this.field_180240_f))
        {
            int i = this.curblockDamage - this.initialDamage;
            Block block = this.theWorld.getBlockState(pos).getBlock();

            if (!block.isAir(theWorld, pos))
            {
                float f = block.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, pos) * (float)(i + 1);

                if (f >= 0.7F)
                {
                    this.isDestroyingBlock = false;
                    this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), pos, -1);
                    this.tryHarvestBlock(pos);
                }
                else if (!this.receivedFinishDiggingPacket)
                {
                    this.isDestroyingBlock = false;
                    this.receivedFinishDiggingPacket = true;
                    this.field_180241_i = pos;
                    this.initialBlockDamage = this.initialDamage;
                }
            }
        }
    }

    public void cancelDestroyingBlock()
    {
        this.isDestroyingBlock = false;
        this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, -1);
    }

    private boolean removeBlock(BlockPos pos)
    {
        return removeBlock(pos, false);
    }
    private boolean removeBlock(BlockPos pos, boolean canHarvest)
    {
        IBlockState iblockstate = this.theWorld.getBlockState(pos);
        iblockstate.getBlock().onBlockHarvested(this.theWorld, pos, iblockstate, this.thisPlayerMP);
        boolean flag = iblockstate.getBlock().removedByPlayer(theWorld, pos, thisPlayerMP, canHarvest);

        if (flag)
        {
            iblockstate.getBlock().onBlockDestroyedByPlayer(this.theWorld, pos, iblockstate);
        }

        return flag;
    }

    public boolean tryHarvestBlock(BlockPos pos)
    {
        int exp = net.minecraftforge.common.ForgeHooks.onBlockBreakEvent(theWorld, gameType, thisPlayerMP, pos);
        if (exp == -1)
        {
            return false;
        }
        else
        {
            IBlockState iblockstate = this.theWorld.getBlockState(pos);
            TileEntity tileentity = this.theWorld.getTileEntity(pos);

            ItemStack stack = thisPlayerMP.getCurrentEquippedItem();
            if (stack != null && stack.getItem().onBlockStartBreak(stack, pos, thisPlayerMP)) return false;

            this.theWorld.playAuxSFXAtEntity(this.thisPlayerMP, 2001, pos, Block.getStateId(iblockstate));
            boolean flag1 = false;

            if (this.isCreative())
            {
                flag1 = this.removeBlock(pos);
                this.thisPlayerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(this.theWorld, pos));
            }
            else
            {
                ItemStack itemstack1 = this.thisPlayerMP.getCurrentEquippedItem();
                boolean flag = iblockstate.getBlock().canHarvestBlock(theWorld, pos, thisPlayerMP);

                if (itemstack1 != null)
                {
                    itemstack1.onBlockDestroyed(this.theWorld, iblockstate.getBlock(), pos, this.thisPlayerMP);

                    if (itemstack1.stackSize == 0)
                    {
                        this.thisPlayerMP.destroyCurrentEquippedItem();
                    }
                }

                flag1 = this.removeBlock(pos, flag);
                if (flag1 && flag)
                {
                    iblockstate.getBlock().harvestBlock(this.theWorld, this.thisPlayerMP, pos, iblockstate, tileentity);
                }
            }

            // Drop experiance
            if (!this.isCreative() && flag1 && exp > 0)
            {
                iblockstate.getBlock().dropXpOnBlockBreak(theWorld, pos, exp);
            }
            return flag1;
        }
    }

    public boolean tryUseItem(EntityPlayer player, World worldIn, ItemStack stack)
    {
        if (this.gameType == WorldSettings.GameType.SPECTATOR)
        {
            return false;
        }
        else
        {
            int i = stack.stackSize;
            int j = stack.getMetadata();
            ItemStack itemstack = stack.useItemRightClick(worldIn, player);

            if (itemstack != stack || itemstack != null && (itemstack.stackSize != i || itemstack.getMaxItemUseDuration() > 0 || itemstack.getMetadata() != j))
            {
                player.inventory.mainInventory[player.inventory.currentItem] = itemstack;

                if (this.isCreative())
                {
                    itemstack.stackSize = i;

                    if (itemstack.isItemStackDamageable())
                    {
                        itemstack.setItemDamage(j);
                    }
                }

                if (itemstack.stackSize == 0)
                {
                    player.inventory.mainInventory[player.inventory.currentItem] = null;
                    net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, itemstack);
                }

                if (!player.isUsingItem())
                {
                    ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
                }

                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public boolean activateBlockOrUseItem(EntityPlayer player, World worldIn, ItemStack stack, BlockPos pos, EnumFacing side, float offsetX, float offsetY, float offsetZ)
    {
        if (this.gameType == WorldSettings.GameType.SPECTATOR)
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof ILockableContainer)
            {
                Block block = worldIn.getBlockState(pos).getBlock();
                ILockableContainer ilockablecontainer = (ILockableContainer)tileentity;

                if (ilockablecontainer instanceof TileEntityChest && block instanceof BlockChest)
                {
                    ilockablecontainer = ((BlockChest)block).getLockableContainer(worldIn, pos);
                }

                if (ilockablecontainer != null)
                {
                    player.displayGUIChest(ilockablecontainer);
                    return true;
                }
            }
            else if (tileentity instanceof IInventory)
            {
                player.displayGUIChest((IInventory)tileentity);
                return true;
            }

            return false;
        }
        else
        {
            net.minecraftforge.event.entity.player.PlayerInteractEvent event = net.minecraftforge.event.ForgeEventFactory.onPlayerInteract(player,
                    net.minecraftforge.event.entity.player.PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, worldIn, pos, side, new net.minecraft.util.Vec3(offsetX, offsetY, offsetZ));
            if (event.isCanceled())
            {
                thisPlayerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(theWorld, pos));
                return false;
            }

            if (stack != null && stack.getItem().onItemUseFirst(stack, player, worldIn, pos, side, offsetX, offsetY, offsetZ))
            {
                if (stack.stackSize <= 0) net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(thisPlayerMP, stack);
                return true;
            }

            IBlockState iblockstate = worldIn.getBlockState(pos);
            boolean isAir = worldIn.isAirBlock(pos);
            boolean useBlock = !player.isSneaking() || player.getHeldItem() == null;
            if (!useBlock) useBlock = player.getHeldItem().getItem().doesSneakBypassUse(worldIn, pos, player);
            boolean result = false;

            if (useBlock)
            {
                if (event.useBlock != net.minecraftforge.fml.common.eventhandler.Event.Result.DENY)
                {
                    result = iblockstate.getBlock().onBlockActivated(worldIn, pos, iblockstate, player, side, offsetX, offsetY, offsetZ);
                }
                else
                {
                    thisPlayerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(theWorld, pos));
                    result = event.useItem != net.minecraftforge.fml.common.eventhandler.Event.Result.ALLOW;
                }
            }
            if (stack != null && !result && event.useItem != net.minecraftforge.fml.common.eventhandler.Event.Result.DENY)
            {
                int meta = stack.getMetadata();
                int size = stack.stackSize;
                result = stack.onItemUse(player, worldIn, pos, side, offsetX, offsetY, offsetZ);
                if (isCreative())
                {
                    stack.setItemDamage(meta);
                    stack.stackSize = size;
                }
                if (stack.stackSize <= 0) net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(thisPlayerMP, stack);
            }
            return result;
        }
    }

    public void setWorld(WorldServer serverWorld)
    {
        this.theWorld = serverWorld;
    }

    public double getBlockReachDistance()
    {
        return blockReachDistance;
    }
    public void setBlockReachDistance(double distance)
    {
        blockReachDistance = distance;
    }
}