package com.github.lunatrius.schematica.client.printer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.github.lunatrius.core.util.BlockPosHelper;
import com.github.lunatrius.core.util.MBlockPos;
import com.github.lunatrius.schematica.block.state.BlockStateHelper;
import com.github.lunatrius.schematica.client.printer.nbtsync.NBTSync;
import com.github.lunatrius.schematica.client.printer.nbtsync.SyncRegistry;
import com.github.lunatrius.schematica.client.printer.registry.PlacementData;
import com.github.lunatrius.schematica.client.printer.registry.PlacementRegistry;
import com.github.lunatrius.schematica.client.util.BlockList;
import com.github.lunatrius.schematica.client.util.BlockStateToItemStack;
import com.github.lunatrius.schematica.client.world.SchematicWorld;
import com.github.lunatrius.schematica.handler.ConfigurationHandler;
import com.github.lunatrius.schematica.proxy.ClientProxy;
import com.github.lunatrius.schematica.reference.Constants;
import com.github.lunatrius.schematica.reference.Reference;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.modules.types.mods.Schematica;
import net.mattbenson.utils.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneComparator.Mode;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fluids.BlockFluidBase;

public class SchematicPrinter {
  public static final SchematicPrinter INSTANCE = new SchematicPrinter();

  private final Minecraft minecraft = Minecraft.getMinecraft();

  public static List < BlockPos > placedBlocks = new ArrayList < > ();

  protected List < BlockList.WrappedItemStack > blockList;
  Timer timer = new Timer();
  Timer timer1 = new Timer();
  Timer timer2 = new Timer();
  Timer timer3 = new Timer();
  Timer timer4 = new Timer();
  Timer timer5 = new Timer();
  Timer timer6 = new Timer();
  
  private boolean isEnabled = true;
  private boolean isPrinting = false;

  Packet < ? > packet;
  List < Integer > gfg;

  private SchematicWorld schematic = null;
  private byte[][][] timeout = null;
  private byte[][][] tickTimeout = null;
  private HashMap < BlockPos,
    Integer > syncBlacklist = new HashMap < BlockPos,
    Integer > ();

  public SchematicPrinter() {
    this.gfg = new ArrayList < Integer > (Arrays.asList(23));
  }

  public boolean isEnabled() {
    return this.isEnabled;
  }

  public void setEnabled(final boolean isEnabled) {
    this.isEnabled = isEnabled;
  }

  public boolean togglePrinting() {
    placedBlocks.clear();
    this.isPrinting = !this.isPrinting && this.schematic != null;
    return this.isPrinting;
  }

  public boolean isPrinting() {
    return this.isPrinting;
  }

  public void setPrinting(final boolean isPrinting) {
    this.isPrinting = isPrinting;
  }

  public SchematicWorld getSchematic() {
    return this.schematic;
  }

  public void setSchematic(final SchematicWorld schematic) {
    placedBlocks.clear();
    this.isPrinting = false;
    this.schematic = schematic;
    refresh();
  }

  public void refresh() {
    if (this.schematic != null) {
      this.timeout = new byte[this.schematic.getWidth()][this.schematic.getHeight()][this.schematic.getLength()];
      this.tickTimeout = new byte[this.schematic.getWidth()][this.schematic.getHeight()][this.schematic.getLength()];

    } else {
      this.timeout = null;
    }
    this.syncBlacklist.clear();
  }

  public boolean print(final WorldClient world, final EntityPlayerSP player) {

    final double dX = ClientProxy.playerPosition.x - this.schematic.position.x;
    final double dY = ClientProxy.playerPosition.y - this.schematic.position.y;
    final double dZ = ClientProxy.playerPosition.z - this.schematic.position.z;
    final int x = (int) Math.floor(dX);
    final int y = (int) Math.floor(dY);
    final int z = (int) Math.floor(dZ);
    final int range = ConfigurationHandler.placeDistance;

    final int minX = Math.max(0, x - range);
    final int maxX = Math.min(this.schematic.getWidth() - 1, x + range);
    int minY = Math.max(0, y - range);
    int maxY = Math.min(this.schematic.getHeight() - 1, y + range);
    final int minZ = Math.max(0, z - range);
    final int maxZ = Math.min(this.schematic.getLength() - 1, z + range);

    if (minX > maxX || minY > maxY || minZ > maxZ) {
      return false;
    }

    final int slot = player.inventory.currentItem;
    final boolean isSneaking = player.isSneaking();

    switch (schematic.layerMode) {
    case ALL:
      break;
    case SINGLE_LAYER:
      if (schematic.renderingLayer > maxY) {
        return false;
      }
      maxY = schematic.renderingLayer;
      //$FALL-THROUGH$
    case ALL_BELOW:
      if (schematic.renderingLayer < minY) {
        return false;
      }
      maxY = schematic.renderingLayer;
      break;
    }

    syncSneaking(player, true);

    final double blockReachDistance = this.minecraft.playerController.getBlockReachDistance() - 0.1;
    final double blockReachDistanceSq = blockReachDistance * blockReachDistance;
    Iterable < MBlockPos > it = BlockPosHelper.getAllInBoxXZY(minX, minY, minZ, maxX, maxY, maxZ);

    for (final MBlockPos pos: it) {
      if (pos.distanceSqToCenter(dX, dY, dZ) > blockReachDistanceSq) {
        continue;
      }

      try {
        if (placeBlock(world, player, pos)) {
          return syncSlotAndSneaking(player, slot, isSneaking, true);
        }
      } catch (final Exception e) {
        Reference.logger.error("Could not place block!", e);
        return syncSlotAndSneaking(player, slot, isSneaking, false);
      }
    }

    return syncSlotAndSneaking(player, slot, isSneaking, true);
  }

  private boolean syncSlotAndSneaking(final EntityPlayerSP player, final int slot, final boolean isSneaking, final boolean success) {
    player.inventory.currentItem = slot;
    syncSneaking(player, isSneaking);
    return success;
  }

  private boolean placeBlock(final WorldClient world, final EntityPlayerSP player, final BlockPos pos) {

    final int x = pos.getX();
    final int y = pos.getY();
    final int z = pos.getZ();
    if (this.timeout[x][y][z] > 0) {
      final byte[] array = this.timeout[x][y];
      final int n = z;
      --array[n];
      return false;
    }
    final int wx = this.schematic.position.x + x;
    final int wy = this.schematic.position.y + y;
    final int wz = this.schematic.position.z + z;
    final BlockPos realPos = new BlockPos(wx, wy, wz);
    final IBlockState blockState = this.schematic.getBlockState(pos);
    final IBlockState realBlockState = world.getBlockState(realPos);
    final Block realBlock = realBlockState.getBlock();
    if (BlockStateHelper.areBlockStatesEqual(blockState, realBlockState)) {
      final NBTSync handler = SyncRegistry.INSTANCE.getHandler(realBlock);
      if (handler != null) {
        this.timeout[x][y][z] = (byte) ConfigurationHandler.timeout;
        Integer tries = this.syncBlacklist.get(realPos);
        if (tries == null) {
          tries = 0;
        } else if (tries >= 10) {
          return false;
        }
        Reference.logger.trace("Trying to sync block at {} {}", new Object[] {
          realPos,
          tries
        });
        final boolean success = handler.execute(player, this.schematic, pos, world, realPos);
        if (success) {
          this.syncBlacklist.put(realPos, tries + 1);
        }
        return success;
      }
      return false;
    } else {

      if (Schematica.betaMode) {

        if (Schematica.BetaOnlyCreative) {
          if (!this.minecraft.playerController.isInCreativeMode()) {
            return false;
          }
        }

        if (Schematica.destoryPlaced) {

          if (!world.isAirBlock(realPos) && this.minecraft.playerController.isInCreativeMode()) {
            if (realBlock instanceof BlockDispenser && blockState.getBlock() instanceof BlockDispenser) {
              if (Minecraft.getMinecraft().theWorld.getBlockState(realPos).getValue(BlockDispenser.FACING) != blockState.getValue(BlockDispenser.FACING)) {
                this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);
                this.timeout[x][y][z] = (byte) ConfigurationHandler.timeout;
                return false;
              }
            }
            if (realBlock instanceof BlockPistonBase && blockState.getBlock() instanceof BlockPistonBase) {
              if (Minecraft.getMinecraft().theWorld.getBlockState(realPos).getValue(BlockPistonBase.FACING) != blockState.getValue(BlockPistonBase.FACING)) {
                this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);
                this.timeout[x][y][z] = (byte) ConfigurationHandler.timeout;
                return false;
              }
            }
            if (realBlock instanceof BlockRedstoneRepeater && blockState.getBlock() instanceof BlockRedstoneRepeater) {
              if (Minecraft.getMinecraft().theWorld.getBlockState(realPos).getValue(BlockRedstoneRepeater.FACING) != blockState.getValue(BlockRedstoneRepeater.FACING)) {
                this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);
                this.timeout[x][y][z] = (byte) ConfigurationHandler.timeout;
                return false;
              }
            }
            if (realBlock instanceof BlockRedstoneComparator && blockState.getBlock() instanceof BlockRedstoneComparator) {
              if (Minecraft.getMinecraft().theWorld.getBlockState(realPos).getValue(BlockRedstoneComparator.FACING) != blockState.getValue(BlockRedstoneComparator.FACING)) {
                this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);
                this.timeout[x][y][z] = (byte) ConfigurationHandler.timeout;
                return false;
              }
            }
            if (realBlock instanceof BlockDispenser && blockState.getBlock() instanceof BlockDispenser) {
              if (Minecraft.getMinecraft().theWorld.getBlockState(realPos).getValue(BlockDispenser.FACING) != blockState.getValue(BlockDispenser.FACING)) {
                this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);
                this.timeout[x][y][z] = (byte) ConfigurationHandler.timeout;
                return false;
              }
            }
          }

          if (placedBlocks.contains(realPos) || getBlock(realPos) instanceof BlockTripWire) {

            if (Schematica.destryBlocks && !world.isAirBlock(realPos) && this.minecraft.playerController.isInCreativeMode()) {
              if (realBlock instanceof BlockRedstoneRepeater && blockState.getBlock() instanceof BlockRedstoneRepeater && !world.isAirBlock(realPos)) {
                if (Minecraft.getMinecraft().theWorld.getBlockState(realPos).getValue(BlockRedstoneRepeater.FACING) != blockState.getValue(BlockRedstoneRepeater.FACING)) {
                  this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);
                  placedBlocks.remove(realPos);
                  this.timeout[x][y][z] = (byte) ConfigurationHandler.timeout;
                  return !ConfigurationHandler.destroyInstantly;
                }
              } else if (realBlock instanceof BlockRedstoneWire && blockState.getBlock() instanceof BlockRedstoneWire && !world.isAirBlock(realPos)) {

              } else if (realBlock instanceof BlockRedstoneComparator && blockState.getBlock() instanceof BlockRedstoneComparator && !world.isAirBlock(realPos)) {

              } else if (realBlock instanceof BlockPistonBase && blockState.getBlock() instanceof BlockPistonBase && !world.isAirBlock(realPos)) {

              } else if (realBlock instanceof BlockPistonExtension && blockState.getBlock() instanceof BlockPistonExtension && !world.isAirBlock(realPos)) {

              } else if (realBlock instanceof BlockPistonMoving && blockState.getBlock() instanceof BlockPistonMoving && !world.isAirBlock(realPos)) {

              } else if (realBlock instanceof BlockRedstoneTorch && blockState.getBlock() instanceof BlockRedstoneTorch && !world.isAirBlock(realPos)) {

              } else if (realBlock instanceof BlockTrapDoor && blockState.getBlock() instanceof BlockTrapDoor && !world.isAirBlock(realPos)) {

              } else {
                if (getBlock(realPos) != Blocks.tripwire) {
                  if (getBlock(realPos) != Blocks.piston || getBlock(realPos) != Blocks.piston_extension || getBlock(realPos) != Blocks.piston_head || getBlock(realPos) != Blocks.sticky_piston) {
                    if (timer.hasReached(50)) {
                      placedBlocks.remove(realPos);
                      this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);
                      this.timeout[x][y][z] = (byte) ConfigurationHandler.timeout;
                      timer.reset();
                    }
                    return !ConfigurationHandler.destroyInstantly;
                  } else {
                    placedBlocks.remove(realPos);
                    this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);
                    this.timeout[x][y][z] = (byte) ConfigurationHandler.timeout;
                    return !ConfigurationHandler.destroyInstantly;
                  }
                } else {
                  if (getBlock(realPos) == Blocks.tripwire) {
                    if (getBlock(realPos.add(0, 1, 0)) != Blocks.air) {
                      placedBlocks.remove(realPos);
                      if (timer3.hasReached(50)) {
                        this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);
                        timer3.reset();
                      }
                    } else {
                      if (timer3.hasReached(Schematica.betaBreakDelay)) {
                        placedBlocks.remove(realPos);
                        this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);
                        timer3.reset();
                      }
                    }
                  } else if (getBlock(realPos) == Blocks.piston || getBlock(realPos) == Blocks.piston_extension || getBlock(realPos) == Blocks.piston_head || getBlock(realPos) == Blocks.sticky_piston) {
                    if (timer4.hasReached(Schematica.betaBreakDelay)) {
                      placedBlocks.remove(realPos);
                      this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);
                      timer4.reset();
                    }
                  } else {
                    if (timer4.hasReached(Schematica.betaBreakDelay)) {
                      this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);
                      timer4.reset();
                    }
                  }
                }
              }
            }

          }

        } else {
          if (Schematica.destryBlocks && !world.isAirBlock(realPos) && this.minecraft.playerController.isInCreativeMode()) {
            if (realBlock instanceof BlockRedstoneRepeater && blockState.getBlock() instanceof BlockRedstoneRepeater && !world.isAirBlock(realPos)) {
              if (Minecraft.getMinecraft().theWorld.getBlockState(realPos).getValue(BlockRedstoneRepeater.FACING) != blockState.getValue(BlockRedstoneRepeater.FACING)) {
                this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);
                placedBlocks.remove(realPos);
                this.timeout[x][y][z] = (byte) ConfigurationHandler.timeout;
                return !ConfigurationHandler.destroyInstantly;
              }
            } else if (realBlock instanceof BlockRedstoneWire && blockState.getBlock() instanceof BlockRedstoneWire && !world.isAirBlock(realPos)) {

            } else if (realBlock instanceof BlockRedstoneComparator && blockState.getBlock() instanceof BlockRedstoneComparator && !world.isAirBlock(realPos)) {

            } else if (realBlock instanceof BlockPistonBase && blockState.getBlock() instanceof BlockPistonBase && !world.isAirBlock(realPos)) {

            } else if (realBlock instanceof BlockPistonExtension && blockState.getBlock() instanceof BlockPistonExtension && !world.isAirBlock(realPos)) {

            } else if (realBlock instanceof BlockPistonMoving && blockState.getBlock() instanceof BlockPistonMoving && !world.isAirBlock(realPos)) {

            } else if (realBlock instanceof BlockRedstoneTorch && blockState.getBlock() instanceof BlockRedstoneTorch && !world.isAirBlock(realPos)) {

            } else if (realBlock instanceof BlockTrapDoor && blockState.getBlock() instanceof BlockTrapDoor && !world.isAirBlock(realPos)) {

            } else {
              if (getBlock(realPos) != Blocks.tripwire) {
                if (getBlock(realPos) != Blocks.piston || getBlock(realPos) != Blocks.piston_extension || getBlock(realPos) != Blocks.piston_head || getBlock(realPos) != Blocks.sticky_piston) {
                  if (timer.hasReached(50)) {
                    this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);
                    this.timeout[x][y][z] = (byte) ConfigurationHandler.timeout;
                    timer.reset();
                  }
                  return !ConfigurationHandler.destroyInstantly;
                } else {
                  this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);
                  this.timeout[x][y][z] = (byte) ConfigurationHandler.timeout;
                  return !ConfigurationHandler.destroyInstantly;
                }
              } else {
                if (getBlock(realPos) == Blocks.tripwire && Schematica.betaString) {
                  if (timer3.hasReached(300)) {
                    placedBlocks.remove(realPos);
                    this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);
                    timer3.reset();
                  }
                } else if (getBlock(realPos) == Blocks.piston || getBlock(realPos) == Blocks.piston_extension || getBlock(realPos) == Blocks.piston_head || getBlock(realPos) == Blocks.sticky_piston) {
                  if (timer1.hasReached(200)) {
                    this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);
                    timer1.reset();
                  }
                } else {

                  this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);

                }
              }
            }
          }

          if (Schematica.breakBadDispensers && !world.isAirBlock(realPos) && this.minecraft.playerController.isInCreativeMode()) {
            if (realBlock instanceof BlockDispenser && blockState.getBlock() instanceof BlockDispenser && Schematica.breakBadDispensers) {
              this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);
              this.timeout[x][y][z] = (byte) ConfigurationHandler.timeout;
              return false;
            }
            if (realBlock instanceof BlockSlab && blockState.getBlock() instanceof BlockSlab && Schematica.breakBadSlabs) {
              this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);
              this.timeout[x][y][z] = (byte) ConfigurationHandler.timeout;
              return false;
            }
          }
        }

        if (realBlock instanceof BlockRedstoneRepeater && blockState.getBlock() instanceof BlockRedstoneRepeater) {

          if (timer2.hasReached(Schematica.autotickDelay)) {
            int one = Minecraft.getMinecraft().theWorld.getBlockState(realPos).getValue(BlockRedstoneRepeater.DELAY);
            int two = blockState.getValue(BlockRedstoneRepeater.DELAY);

            if (one == two) return true;

            if (two != 1 && one != 0) {

              if (Schematica.autoTickRepeaters) {
                this.syncSneaking(player, false);
                if (timer2.hasReached(Schematica.autotickDelay)) {
                  Minecraft.getMinecraft().playerController.onPlayerRightClick(player, world, Minecraft.getMinecraft().thePlayer.getHeldItem(), realPos, EnumFacing.DOWN, new Vec3(wx + 0.5, wy + 0.125, wz + 0.5));

                  one = Minecraft.getMinecraft().theWorld.getBlockState(realPos).getValue(BlockRedstoneRepeater.DELAY);
                  two = blockState.getValue(BlockRedstoneRepeater.DELAY);

                  timer2.reset();
                }
                this.syncSneaking(player, true);
              }

            }
          }
        }

        if (realBlock instanceof BlockRedstoneComparator && blockState.getBlock() instanceof BlockRedstoneComparator) {

          if (timer2.hasReached(Schematica.autotickDelay)) {
            Mode one = Minecraft.getMinecraft().theWorld.getBlockState(realPos).getValue(BlockRedstoneComparator.MODE);
            Mode two = blockState.getValue(BlockRedstoneComparator.MODE);

            if (!(one.equals(two))) {

              if (Schematica.autoTickRepeaters) {
                this.syncSneaking(player, false);
                if (timer2.hasReached(Schematica.autotickDelay)) {
                  Minecraft.getMinecraft().playerController.onPlayerRightClick(player, world, Minecraft.getMinecraft().thePlayer.getHeldItem(), realPos, EnumFacing.DOWN, new Vec3(wx + 0.5, wy + 0.125, wz + 0.5));

                  timer2.reset();
                }
                this.syncSneaking(player, true);
              }
            }
          }
        }

        if (realBlock instanceof BlockTrapDoor && blockState.getBlock() instanceof BlockTrapDoor) {
          if (Schematica.autoTickRepeaters) {
            this.syncSneaking(player, false);
            if (timer2.hasReached(Schematica.autotickDelay)) {
              Minecraft.getMinecraft().playerController.onPlayerRightClick(player, world, Minecraft.getMinecraft().thePlayer.getHeldItem(), realPos, EnumFacing.DOWN, new Vec3(wx + 0.5, wy + 0.125, wz + 0.5));
              timer2.reset();
            }
            this.syncSneaking(player, true);
          }

        }

        if (realBlock instanceof BlockLever && blockState.getBlock() instanceof BlockLever) {
          if (Schematica.autoTickRepeaters) {
            this.syncSneaking(player, false);
            if (timer6.hasReached(Schematica.autotickDelay)) {
              Minecraft.getMinecraft().playerController.onPlayerRightClick(player, world, Minecraft.getMinecraft().thePlayer.getHeldItem(), realPos, EnumFacing.DOWN, new Vec3(wx + 0.5, wy + 0.125, wz + 0.5));
              timer6.reset();
            }
            this.syncSneaking(player, true);
          }

        }

      } else {
        if (realBlock instanceof BlockRedstoneDiode && blockState.getBlock() instanceof BlockRedstoneDiode || realBlock instanceof BlockRedstoneComparator && blockState.getBlock() instanceof BlockRedstoneComparator || realBlock instanceof BlockTrapDoor && blockState.getBlock() instanceof BlockTrapDoor) {
          if (Schematica.autoTickRepeaters) {

            this.syncSneaking(player, false);

            final boolean success = Minecraft.getMinecraft().playerController.onPlayerRightClick(player, world, Minecraft.getMinecraft().thePlayer.getHeldItem(), realPos, EnumFacing.DOWN, new Vec3(wx + 0.5, wy + 0.125, wz + 0.5));
            if (success) {
              this.timeout[x][y][z] = (byte) Schematica.tickDelay;
              return success;

            }
          }

        }

        if (Schematica.destryBlocks && !world.isAirBlock(realPos) && this.minecraft.playerController.isInCreativeMode()) {
          this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);
          this.timeout[x][y][z] = (byte) ConfigurationHandler.timeout;
          return !ConfigurationHandler.destroyInstantly;
        }
        if (Schematica.breakBadDispensers && !world.isAirBlock(realPos) && this.minecraft.playerController.isInCreativeMode()) {
          if (realBlock instanceof BlockDispenser && blockState.getBlock() instanceof BlockDispenser && Schematica.breakBadDispensers) {
            this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);
            this.timeout[x][y][z] = (byte) ConfigurationHandler.timeout;
            return false;
          }
          if (realBlock instanceof BlockPistonBase && blockState.getBlock() instanceof BlockPistonBase && Schematica.breakBadPistons) {
            this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);
            this.timeout[x][y][z] = (byte) ConfigurationHandler.timeout;
            return false;
          }
          if (realBlock instanceof BlockSlab && blockState.getBlock() instanceof BlockSlab && Schematica.breakBadSlabs) {
            this.minecraft.playerController.clickBlock(realPos, EnumFacing.DOWN);
            this.timeout[x][y][z] = (byte) ConfigurationHandler.timeout;
            return false;
          }
        }
      }
      if (this.schematic.isAirBlock(pos)) {
        return false;
      }
      if (!realBlock.isReplaceable((World) world, realPos)) {
        return false;
      }
      final ItemStack itemStack = BlockStateToItemStack.getItemStack(blockState, new MovingObjectPosition(player), this.schematic, pos);
      if (itemStack == null || itemStack.getItem() == null) {
        Reference.logger.debug("{} is missing a mapping!", new Object[] {
          blockState
        });
        return false;
      }
      if (this.placeBlock(world, player, realPos, blockState, itemStack)) {
        this.timeout[x][y][z] = (byte) ConfigurationHandler.timeout;
        return !ConfigurationHandler.placeInstantly;
      }
      return false;
    }
  }

  private boolean isSolid(final World world, final BlockPos pos, final EnumFacing side) {
    final BlockPos offset = pos.offset(side);

    final IBlockState blockState = world.getBlockState(offset);
    final Block block = blockState.getBlock();

    if (block == null) {
      return false;
    }

    if (block.isAir(world, offset)) {
      return false;
    }

    if (block instanceof BlockFluidBase) {
      return false;
    }

    if (block.isReplaceable(world, offset)) {
      return false;
    }

    return true;
  }

  private List < EnumFacing > getSolidSides(final World world, final BlockPos pos) {
    if (!ConfigurationHandler.placeAdjacent) {
      return Arrays.asList(EnumFacing.VALUES);
    }

    final List < EnumFacing > list = new ArrayList < EnumFacing > ();

    for (final EnumFacing side: EnumFacing.VALUES) {
      if (isSolid(world, pos, side)) {
        list.add(side);
      }
    }

    return list;
  }

  public static Block getBlock(BlockPos pos) {
    return Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
  }

  private boolean placeBlock(final WorldClient world, final EntityPlayerSP player, final BlockPos pos, final IBlockState blockState, final ItemStack itemStack) {
    if (itemStack.getItem() instanceof ItemBucket) {}
    final PlacementData data = PlacementRegistry.INSTANCE.getPlacementData(blockState, itemStack);
    final float oldYaw = player.rotationYaw;
    if (data != null && !data.isValidPlayerFacing(blockState, (EntityPlayer) player, pos, (World) world)) {
      if (!Schematica.infintieMode) {
        return false;
      }

      if (this.isDirectional(itemStack)) {
        final double xdif = pos.getX() + 0.5 - player.posX;
        final double zdif = pos.getZ() + 0.5 - player.posZ;
        final double ratio = zdif / xdif;
        final double angle = Math.toDegrees(Math.atan(ratio));
        final boolean greater = player.posX > pos.getX() + 0.5;
        if (greater) {
          this.setRotation(MathHelper.wrapAngleTo180_double(angle) + 90.0);
        } else {
          this.setRotation(MathHelper.wrapAngleTo180_double(angle) - 90.0);
        }
        this.packet = (Packet) new C03PacketPlayer.C05PacketPlayerLook(player.rotationYaw, player.rotationPitch, player.onGround);
      }
      if (!data.isValidPlayerFacing(blockState, (EntityPlayer) player, pos, (World) world)) {
        if (this.isDirectional(itemStack) && this.packet != null) {
          this.packet = null;
          player.rotationYaw = oldYaw;
        }
        return false;
      }
    } else {
      if (Schematica.betaMode) {

        if (Schematica.BetaOnlyCreative) {
          if (!this.minecraft.playerController.isInCreativeMode()) {
            return false;
          }
        }
        
        if (Schematica.betaString) {

        final ItemStack itemStack1 = new ItemStack(Item.getItemById(287));

        if (timer1.hasReached(50)) {
          if (getBlock(pos.add(0, -2, 0)) != Blocks.air) {
            if (getBlock(pos.add(0, -1, 0)) == Blocks.air) {
              if (getBlock(pos.add(1, -2, 0)) != Blocks.air || getBlock(pos.add(-1, -2, 0)) != Blocks.air || getBlock(pos.add(0, -2, 1)) != Blocks.air || getBlock(pos.add(0, -2, -1)) != Blocks.air) {
                if (getBlock(pos.add(2, -2, 0)) != Blocks.air || getBlock(pos.add(-2, -2, 0)) != Blocks.air || getBlock(pos.add(0, -2, 2)) != Blocks.air || getBlock(pos.add(0, -2, -2)) != Blocks.air) {
                  player.inventory.setInventorySlotContents(player.inventory.currentItem, itemStack1);
                  this.minecraft.playerController.sendSlotPacket(player.inventory.getStackInSlot(player.inventory.currentItem), Constants.Inventory.SlotOffset.HOTBAR + player.inventory.currentItem);
                  if ((getBlock(pos.add(1, 0, 0)) == Blocks.air & getBlock(pos.add(-1, 0, 0)) == Blocks.air & getBlock(pos.add(0, 0, 1)) == Blocks.air & getBlock(pos.add(0, 0, -1)) == Blocks.air)) {
                    Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(pos.getX(), pos.getY() - 2, pos.getZ()), 1, itemStack1, (float) 1, (float) 1.0, (float) 1));
                    placedBlocks.add(new BlockPos(pos.getX(), pos.getY() - 2, pos.getZ()));
                    timer1.reset();
                  }
                }
              }
            }

          } else if (getBlock(pos.add(0, -3, 0)) != Blocks.air) {
            if (getBlock(pos.add(1, -3, 0)) != Blocks.air || getBlock(pos.add(-1, -3, 0)) != Blocks.air || getBlock(pos.add(0, -3, 1)) != Blocks.air || getBlock(pos.add(0, -3, -1)) != Blocks.air) {
              if (getBlock(pos.add(2, -3, 0)) != Blocks.air || getBlock(pos.add(-2, -3, 0)) != Blocks.air || getBlock(pos.add(0, -3, 2)) != Blocks.air || getBlock(pos.add(0, -3, -2)) != Blocks.air) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, itemStack1);
                this.minecraft.playerController.sendSlotPacket(player.inventory.getStackInSlot(player.inventory.currentItem), Constants.Inventory.SlotOffset.HOTBAR + player.inventory.currentItem);
                Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(pos.getX(), pos.getY() - 3, pos.getZ()), 1, itemStack1, (float) 1, (float) 1.0, (float) 1));
                Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(pos.getX(), pos.getY() - 2, pos.getZ()), 1, itemStack1, (float) 1, (float) 1.0, (float) 1));
                placedBlocks.add(new BlockPos(pos.getX(), pos.getY() - 3, pos.getZ()));
                placedBlocks.add(new BlockPos(pos.getX(), pos.getY() - 2, pos.getZ()));
              }
            }
            timer1.reset();

          } 
        }

        }
      }
    }
    if (Schematica.infintieMode && this.isDirectional(itemStack)) {
      player.rotationYaw = oldYaw;
    }
    final List < EnumFacing > solidSides = this.getSolidSides((World) world, pos);
    if (solidSides.size() == 0) {
      return false;
    }
    EnumFacing direction;
    float offsetX;
    float offsetY;
    float offsetZ;
    int extraClicks;
    if (data != null) {
      final List < EnumFacing > validDirections = data.getValidBlockFacings(solidSides, blockState);
      if (validDirections.size() == 0) {
        return false;
      }
      direction = validDirections.get(0);
      offsetX = data.getOffsetX(blockState);
      offsetY = data.getOffsetY(blockState);
      offsetZ = data.getOffsetZ(blockState);
      extraClicks = data.getExtraClicks(blockState);
    } else {
      direction = solidSides.get(0);
      offsetX = 0.5f;
      offsetY = 0.5f;
      offsetZ = 0.5f;
      extraClicks = 0;
    }

    if (itemStack != null) {
      if (itemStack.getItem() == Items.redstone) {
        if (getBlock(pos.add(0, -1, 0)) instanceof BlockAir) {
          return false;
        }
      }
    }

    return this.swapToItem(player.inventory, itemStack) && this.placeBlock(world, player, pos, direction, offsetX, offsetY, offsetZ, extraClicks);
  }

  private boolean placeBlock(final WorldClient world, final EntityPlayerSP player, final BlockPos pos, final EnumFacing direction, final float offsetX, final float offsetY, final float offsetZ, final int extraClicks) {
    final ItemStack itemStack = player.getCurrentEquippedItem();
    boolean success = false;
    if (!this.minecraft.playerController.isInCreativeMode() && itemStack != null && itemStack.stackSize <= extraClicks) {
      return false;
    }
    final BlockPos offset = pos.offset(direction);
    final EnumFacing side = direction.getOpposite();
    final Vec3 hitVec = new Vec3((double)(offset.getX() + offsetX), (double)(offset.getY() + offsetY), (double)(offset.getZ() + offsetZ));
    success = this.placeBlock(world, player, itemStack, offset, side, hitVec);
    for (int i = 0; success && i < extraClicks; success = this.placeBlock(world, player, itemStack, offset, side, hitVec), ++i) {}
    if (itemStack != null && itemStack.stackSize == 0 && success) {
      player.inventory.mainInventory[player.inventory.currentItem] = null;
    }
    return success;
  }

  private boolean placeBlock(final WorldClient world, final EntityPlayerSP player, final ItemStack itemStack, final BlockPos pos, final EnumFacing side, final Vec3 hitVec) {
    boolean success = !ForgeEventFactory.onPlayerInteract((EntityPlayer) player, Action.RIGHT_CLICK_BLOCK, (World) world, pos, side).isCanceled();
    if (success) {
      if (this.packet != null) {
        player.sendQueue.addToSendQueue(this.packet);
        this.packet = null;
      }
      success = this.minecraft.playerController.onPlayerRightClick(player, world, itemStack, pos, side, hitVec);
      if (success) {
        if (!placedBlocks.contains(pos)) {
          placedBlocks.add(pos);
        }
        player.swingItem();
        this.schematic.markBlockForUpdate(pos);
      }
    }
    return success;
  }

  public void setRotation(final double target) {
    final double angle = MathHelper.wrapAngleTo180_double((double) Minecraft.getMinecraft().thePlayer.rotationYaw);
    double a = target - angle;
    a += ((a > 180.0) ? -360.0 : ((a < -180.0) ? 360.0 : 0.0));
    final EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
    thePlayer.rotationYaw += (float) a;
  }

  private boolean isDirectional(final ItemStack itemStack) {
    return this.gfg.contains(Item.getIdFromItem(itemStack.getItem()));
  }

  private void syncSneaking(final EntityPlayerSP player, final boolean isSneaking) {
    player.setSneaking(isSneaking);
    player.sendQueue.addToSendQueue(new C0BPacketEntityAction(player, isSneaking ? C0BPacketEntityAction.Action.START_SNEAKING : C0BPacketEntityAction.Action.STOP_SNEAKING));
  }

  private boolean swapToItem(final InventoryPlayer inventory, final ItemStack itemStack) {
    return swapToItem(inventory, itemStack, true);
  }

  private boolean swapToItem(final InventoryPlayer inventory, final ItemStack itemStack, final boolean swapSlots) {
    final int slot = getInventorySlotWithItem(inventory, itemStack);

    if (this.minecraft.playerController.isInCreativeMode() && (slot < Constants.Inventory.InventoryOffset.HOTBAR || slot >= Constants.Inventory.InventoryOffset.HOTBAR + Constants.Inventory.Size.HOTBAR) && ConfigurationHandler.swapSlotsQueue.size() > 0) {
      inventory.currentItem = getNextSlot();
      inventory.setInventorySlotContents(inventory.currentItem, itemStack.copy());
      this.minecraft.playerController.sendSlotPacket(inventory.getStackInSlot(inventory.currentItem), Constants.Inventory.SlotOffset.HOTBAR + inventory.currentItem);
      return true;
    }

    if (slot >= Constants.Inventory.InventoryOffset.HOTBAR && slot < Constants.Inventory.InventoryOffset.HOTBAR + Constants.Inventory.Size.HOTBAR) {
      inventory.currentItem = slot;
      return true;
    } else if (swapSlots && slot >= Constants.Inventory.InventoryOffset.INVENTORY && slot < Constants.Inventory.InventoryOffset.INVENTORY + Constants.Inventory.Size.INVENTORY) {
      if (swapSlots(inventory, slot)) {
        return swapToItem(inventory, itemStack, false);
      }
    }

    return false;
  }

  private int getInventorySlotWithItem(final InventoryPlayer inventory, final ItemStack itemStack) {
    for (int i = 0; i < inventory.mainInventory.length; i++) {
      if (inventory.mainInventory[i] != null && inventory.mainInventory[i].isItemEqual(itemStack)) {
        return i;
      }
    }
    return -1;
  }

  private boolean swapSlots(final InventoryPlayer inventory, final int from) {
    if (ConfigurationHandler.swapSlotsQueue.size() > 0) {
      final int slot = getNextSlot();

      swapSlots(from, slot);
      return true;
    }

    return false;
  }

  private int getNextSlot() {
    final int slot = ConfigurationHandler.swapSlotsQueue.poll() % Constants.Inventory.Size.HOTBAR;
    ConfigurationHandler.swapSlotsQueue.offer(slot);
    return slot;
  }

  private boolean swapSlots(final int from, final int to) {
    return this.minecraft.playerController.windowClick(this.minecraft.thePlayer.inventoryContainer.windowId, from, to, 2, this.minecraft.thePlayer) == null;
  }
  
	private void highlight(Block block, BlockPos pos) {
		
		Color vColor = Color.BLUE;
		Color hColor = Color.BLUE;
		float lineWidth = 1;
		
		float red = hColor.getRed() / 255.0f;
		float green = hColor.getGreen() / 255.0f;
		float blue = hColor.getBlue() / 255.0f;
		float alpha = hColor.getAlpha() / 255.0f;
		float bx, by, bz;

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer vertexBuffer = tessellator.getWorldRenderer();

		double renderPosX = Minecraft.getMinecraft().getRenderManager().viewerPosX;
		double renderPosY = Minecraft.getMinecraft().getRenderManager().viewerPosY;
		double renderPosZ = Minecraft.getMinecraft().getRenderManager().viewerPosZ;

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);

		GL11.glLineWidth(lineWidth);
		GL11.glColor4f(red, green, blue, 0.5F);

		AxisAlignedBB bb = block.getSelectedBoundingBox(Minecraft.getMinecraft().theWorld, pos)
				.expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D)
				.offset(-renderPosX, -renderPosY, -renderPosZ);

		drawFilledBoundingBox(bb);
		GL11.glLineWidth(1F);
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();

	}
	
	private void drawFilledBoundingBox(AxisAlignedBB box) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();

		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		tessellator.draw();

		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		tessellator.draw();

		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		tessellator.draw();

		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		tessellator.draw();

		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		tessellator.draw();

		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		tessellator.draw();
	}

}