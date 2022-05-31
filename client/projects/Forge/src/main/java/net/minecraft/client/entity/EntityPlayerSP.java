package net.minecraft.client.entity;

import net.mattbenson.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityPlayerSP extends AbstractClientPlayer
{
    public final NetHandlerPlayClient sendQueue;
    private final StatFileWriter statWriter;
    private double lastReportedPosX;
    private double lastReportedPosY;
    private double lastReportedPosZ;
    private float lastReportedYaw;
    private float lastReportedPitch;
    private boolean serverSneakState;
    private boolean serverSprintState;
    private int positionUpdateTicks;
    private boolean hasValidHealth;
    private String clientBrand;
    public MovementInput movementInput;
    protected Minecraft mc;
    protected int sprintToggleTimer;
    public int sprintingTicksLeft;
    public float renderArmYaw;
    public float renderArmPitch;
    public float prevRenderArmYaw;
    public float prevRenderArmPitch;
    private int horseJumpPowerCounter;
    private float horseJumpPower;
    public float timeInPortal;
    public float prevTimeInPortal;

    public EntityPlayerSP(Minecraft mcIn, World worldIn, NetHandlerPlayClient netHandler, StatFileWriter statFile)
    {
        super(worldIn, netHandler.getGameProfile());
        this.sendQueue = netHandler;
        this.statWriter = statFile;
        this.mc = mcIn;
        this.dimension = 0;
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return false;
    }

    public void heal(float healAmount)
    {
    }

    public void mountEntity(Entity entityIn)
    {
        super.mountEntity(entityIn);

        if (entityIn instanceof EntityMinecart)
        {
            this.mc.getSoundHandler().playSound(new MovingSoundMinecartRiding(this, (EntityMinecart)entityIn));
        }
    }

    public void onUpdate()
    {
        if (this.worldObj.isBlockLoaded(new BlockPos(this.posX, 0.0D, this.posZ)))
        {
            super.onUpdate();

            if (this.isRiding())
            {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
                this.sendQueue.addToSendQueue(new C0CPacketInput(this.moveStrafing, this.moveForward, this.movementInput.jump, this.movementInput.sneak));
            }
            else
            {
                this.onUpdateWalkingPlayer();
            }
        }
    }

    public void onUpdateWalkingPlayer()
    {
        boolean flag = this.isSprinting();

        if (flag != this.serverSprintState)
        {
            if (flag)
            {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SPRINTING));
            }
            else
            {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }

            this.serverSprintState = flag;
        }

        boolean flag1 = this.isSneaking();

        if (flag1 != this.serverSneakState)
        {
            if (flag1)
            {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SNEAKING));
            }
            else
            {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }

            this.serverSneakState = flag1;
        }

        if (this.isCurrentViewEntity())
        {
            double d0 = this.posX - this.lastReportedPosX;
            double d1 = this.getEntityBoundingBox().minY - this.lastReportedPosY;
            double d2 = this.posZ - this.lastReportedPosZ;
            double d3 = (double)(this.rotationYaw - this.lastReportedYaw);
            double d4 = (double)(this.rotationPitch - this.lastReportedPitch);
            boolean flag2 = d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4D || this.positionUpdateTicks >= 20;
            boolean flag3 = d3 != 0.0D || d4 != 0.0D;

            if (this.ridingEntity == null)
            {
                if (flag2 && flag3)
                {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.posX, this.getEntityBoundingBox().minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround));
                }
                else if (flag2)
                {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.posX, this.getEntityBoundingBox().minY, this.posZ, this.onGround));
                }
                else if (flag3)
                {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
                }
                else
                {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer(this.onGround));
                }
            }
            else
            {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0D, this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
                flag2 = false;
            }

            ++this.positionUpdateTicks;

            if (flag2)
            {
                this.lastReportedPosX = this.posX;
                this.lastReportedPosY = this.getEntityBoundingBox().minY;
                this.lastReportedPosZ = this.posZ;
                this.positionUpdateTicks = 0;
            }

            if (flag3)
            {
                this.lastReportedYaw = this.rotationYaw;
                this.lastReportedPitch = this.rotationPitch;
            }
        }
    }

    public EntityItem dropOneItem(boolean dropAll)
    {
        C07PacketPlayerDigging.Action c07packetplayerdigging$action = dropAll ? C07PacketPlayerDigging.Action.DROP_ALL_ITEMS : C07PacketPlayerDigging.Action.DROP_ITEM;
        this.sendQueue.addToSendQueue(new C07PacketPlayerDigging(c07packetplayerdigging$action, BlockPos.ORIGIN, EnumFacing.DOWN));
        return null;
    }

    public void joinEntityItemWithWorld(EntityItem itemIn)
    {
    }

    public void sendChatMessage(String message)
    {
        this.sendQueue.addToSendQueue(new C01PacketChatMessage(message));
    }

    public void swingItem()
    {
        super.swingItem();
        this.sendQueue.addToSendQueue(new C0APacketAnimation());
    }

    public void respawnPlayer()
    {
        this.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
    }

    protected void damageEntity(DamageSource damageSrc, float damageAmount)
    {
        if (!this.isEntityInvulnerable(damageSrc))
        {
            this.setHealth(this.getHealth() - damageAmount);
        }
    }

    public void closeScreen()
    {
        this.sendQueue.addToSendQueue(new C0DPacketCloseWindow(this.openContainer.windowId));
        this.closeScreenAndDropStack();
    }

    public void closeScreenAndDropStack()
    {
        this.inventory.setItemStack((ItemStack)null);
        super.closeScreen();
        this.mc.displayGuiScreen((GuiScreen)null);
    }

    public void setPlayerSPHealth(float health)
    {
        if (this.hasValidHealth)
        {
            float f = this.getHealth() - health;

            if (f <= 0.0F)
            {
                this.setHealth(health);

                if (f < 0.0F)
                {
                    this.hurtResistantTime = this.maxHurtResistantTime / 2;
                }
            }
            else
            {
                this.lastDamage = f;
                this.setHealth(this.getHealth());
                this.hurtResistantTime = this.maxHurtResistantTime;
                this.damageEntity(DamageSource.generic, f);
                this.hurtTime = this.maxHurtTime = 10;
            }
        }
        else
        {
            this.setHealth(health);
            this.hasValidHealth = true;
        }
    }

    public void addStat(StatBase stat, int amount)
    {
        if (stat != null)
        {
            if (stat.isIndependent)
            {
                super.addStat(stat, amount);
            }
        }
    }

    public void sendPlayerAbilities()
    {
        this.sendQueue.addToSendQueue(new C13PacketPlayerAbilities(this.capabilities));
    }

    public boolean isUser()
    {
        return true;
    }

    protected void sendHorseJump()
    {
        this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.RIDING_JUMP, (int)(this.getHorseJumpPower() * 100.0F)));
    }

    public void sendHorseInventory()
    {
        this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.OPEN_INVENTORY));
    }

    public void setClientBrand(String brand)
    {
        this.clientBrand = brand;
    }

    public String getClientBrand()
    {
        return this.clientBrand;
    }

    public StatFileWriter getStatFileWriter()
    {
        return this.statWriter;
    }

    public void addChatComponentMessage(IChatComponent chatComponent)
    {
        this.mc.ingameGUI.getChatGUI().printChatMessage(chatComponent);
    }

    private boolean isHeadspaceFree(BlockPos pos, int height)
    {
        for (int y = 0; y < height; y++)
        {
            if (!isOpenBlockSpace(pos.add(0, y, 0))) return false;
        }
        return true;
    }

    protected boolean pushOutOfBlocks(double x, double y, double z)
    {
        if (this.noClip)
        {
            return false;
        }
        else
        {
            BlockPos blockpos = new BlockPos(x, y, z);
            double d0 = x - (double)blockpos.getX();
            double d1 = z - (double)blockpos.getZ();

            int entHeight = Math.max((int)Math.ceil(this.height), 1);

            boolean inTranslucentBlock = !this.isHeadspaceFree(blockpos, entHeight);

            if (inTranslucentBlock)
            {
                int i = -1;
                double d2 = 9999.0D;

                if (this.isHeadspaceFree(blockpos.west(), entHeight) && d0 < d2)
                {
                    d2 = d0;
                    i = 0;
                }

                if (this.isHeadspaceFree(blockpos.east(), entHeight) && 1.0D - d0 < d2)
                {
                    d2 = 1.0D - d0;
                    i = 1;
                }

                if (this.isHeadspaceFree(blockpos.north(), entHeight) && d1 < d2)
                {
                    d2 = d1;
                    i = 4;
                }

                if (this.isHeadspaceFree(blockpos.south(), entHeight) && 1.0D - d1 < d2)
                {
                    d2 = 1.0D - d1;
                    i = 5;
                }

                float f = 0.1F;

                if (i == 0)
                {
                    this.motionX = (double)(-f);
                }

                if (i == 1)
                {
                    this.motionX = (double)f;
                }

                if (i == 4)
                {
                    this.motionZ = (double)(-f);
                }

                if (i == 5)
                {
                    this.motionZ = (double)f;
                }
            }

            return false;
        }
    }

    private boolean isOpenBlockSpace(BlockPos pos)
    {
        return !this.worldObj.getBlockState(pos).getBlock().isNormalCube();
    }

    public void setSprinting(boolean sprinting)
    {
        super.setSprinting(sprinting);
        this.sprintingTicksLeft = sprinting ? 600 : 0;
    }

    public void setXPStats(float currentXP, int maxXP, int level)
    {
        this.experience = currentXP;
        this.experienceTotal = maxXP;
        this.experienceLevel = level;
    }

    public void addChatMessage(IChatComponent component)
    {
        this.mc.ingameGUI.getChatGUI().printChatMessage(component);
    }

    public boolean canCommandSenderUseCommand(int permLevel, String commandName)
    {
        return permLevel <= 0;
    }

    public BlockPos getPosition()
    {
        return new BlockPos(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D);
    }

    public void playSound(String name, float volume, float pitch)
    {
        net.minecraftforge.event.entity.PlaySoundAtEntityEvent event = net.minecraftforge.event.ForgeEventFactory.onPlaySoundAtEntity(this, name, volume, pitch);
        if (event.isCanceled() || event.name == null) return;
        name = event.name;
        volume = event.newVolume;
        pitch = event.newPitch;

        this.worldObj.playSound(this.posX, this.posY, this.posZ, name, volume, pitch, false);
    }

    public boolean isServerWorld()
    {
        return true;
    }

    public boolean isRidingHorse()
    {
        return this.ridingEntity != null && this.ridingEntity instanceof EntityHorse && ((EntityHorse)this.ridingEntity).isHorseSaddled();
    }

    public float getHorseJumpPower()
    {
        return this.horseJumpPower;
    }

    public void openEditSign(TileEntitySign signTile)
    {
        this.mc.displayGuiScreen(new GuiEditSign(signTile));
    }

    public void openEditCommandBlock(CommandBlockLogic cmdBlockLogic)
    {
        this.mc.displayGuiScreen(new GuiCommandBlock(cmdBlockLogic));
    }

    public void displayGUIBook(ItemStack bookStack)
    {
        Item item = bookStack.getItem();

        if (item == Items.writable_book)
        {
            this.mc.displayGuiScreen(new GuiScreenBook(this, bookStack, true));
        }
    }

    public void displayGUIChest(IInventory chestInventory)
    {
        String s = chestInventory instanceof IInteractionObject ? ((IInteractionObject)chestInventory).getGuiID() : "minecraft:container";

        if ("minecraft:chest".equals(s))
        {
            this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
        }
        else if ("minecraft:hopper".equals(s))
        {
            this.mc.displayGuiScreen(new GuiHopper(this.inventory, chestInventory));
        }
        else if ("minecraft:furnace".equals(s))
        {
            this.mc.displayGuiScreen(new GuiFurnace(this.inventory, chestInventory));
        }
        else if ("minecraft:brewing_stand".equals(s))
        {
            this.mc.displayGuiScreen(new GuiBrewingStand(this.inventory, chestInventory));
        }
        else if ("minecraft:beacon".equals(s))
        {
            this.mc.displayGuiScreen(new GuiBeacon(this.inventory, chestInventory));
        }
        else if (!"minecraft:dispenser".equals(s) && !"minecraft:dropper".equals(s))
        {
            this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
        }
        else
        {
            this.mc.displayGuiScreen(new GuiDispenser(this.inventory, chestInventory));
        }
    }

    public void displayGUIHorse(EntityHorse horse, IInventory horseInventory)
    {
        this.mc.displayGuiScreen(new GuiScreenHorseInventory(this.inventory, horseInventory, horse));
    }

    public void displayGui(IInteractionObject guiOwner)
    {
        String s = guiOwner.getGuiID();

        if ("minecraft:crafting_table".equals(s))
        {
            this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.worldObj));
        }
        else if ("minecraft:enchanting_table".equals(s))
        {
            this.mc.displayGuiScreen(new GuiEnchantment(this.inventory, this.worldObj, guiOwner));
        }
        else if ("minecraft:anvil".equals(s))
        {
            this.mc.displayGuiScreen(new GuiRepair(this.inventory, this.worldObj));
        }
    }

    public void displayVillagerTradeGui(IMerchant villager)
    {
        this.mc.displayGuiScreen(new GuiMerchant(this.inventory, villager, this.worldObj));
    }

    public void onCriticalHit(Entity entityHit)
    {
        this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT);
    }

    public void onEnchantmentCritical(Entity entityHit)
    {
        this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT_MAGIC);
    }

    public boolean isSneaking()
    {
        boolean flag = this.movementInput != null ? this.movementInput.sneak : false;
        return flag && !this.sleeping;
    }

    public void updateEntityActionState()
    {
        super.updateEntityActionState();

        if (this.isCurrentViewEntity())
        {
            this.moveStrafing = this.movementInput.moveStrafe;
            this.moveForward = this.movementInput.moveForward;
            this.isJumping = this.movementInput.jump;
            this.prevRenderArmYaw = this.renderArmYaw;
            this.prevRenderArmPitch = this.renderArmPitch;
            this.renderArmPitch = (float)((double)this.renderArmPitch + (double)(this.rotationPitch - this.renderArmPitch) * 0.5D);
            this.renderArmYaw = (float)((double)this.renderArmYaw + (double)(this.rotationYaw - this.renderArmYaw) * 0.5D);
        }
    }

    protected boolean isCurrentViewEntity()
    {
        return this.mc.getRenderViewEntity() == this;
    }

    public void onLivingUpdate()
    {
        if (this.sprintingTicksLeft > 0)
        {
            --this.sprintingTicksLeft;

            if (this.sprintingTicksLeft == 0)
            {
                this.setSprinting(false);
            }
        }

        if (this.sprintToggleTimer > 0)
        {
            --this.sprintToggleTimer;
        }

        this.prevTimeInPortal = this.timeInPortal;

        if (this.inPortal)
        {
        	if (this.mc.currentScreen != null && !this.mc.currentScreen.doesGuiPauseGame())
			{
				if(!Wrapper.getInstance().isPortalFix()) {
					this.mc.displayGuiScreen((GuiScreen)null);
				}
			}
            if (this.timeInPortal == 0.0F)
            {
                this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("portal.trigger"), this.rand.nextFloat() * 0.4F + 0.8F));
            }

            this.timeInPortal += 0.0125F;

            if (this.timeInPortal >= 1.0F)
            {
                this.timeInPortal = 1.0F;
            }

            this.inPortal = false;
        }
        else if (this.isPotionActive(Potion.confusion) && this.getActivePotionEffect(Potion.confusion).getDuration() > 60)
        {
            this.timeInPortal += 0.006666667F;

            if (this.timeInPortal > 1.0F)
            {
                this.timeInPortal = 1.0F;
            }
        }
        else
        {
            if (this.timeInPortal > 0.0F)
            {
                this.timeInPortal -= 0.05F;
            }

            if (this.timeInPortal < 0.0F)
            {
                this.timeInPortal = 0.0F;
            }
        }

        if (this.timeUntilPortal > 0)
        {
            --this.timeUntilPortal;
        }

        boolean flag = this.movementInput.jump;
        boolean flag1 = this.movementInput.sneak;
        float f = 0.8F;
        boolean flag2 = this.movementInput.moveForward >= f;

        
		if(Wrapper.getInstance().isToggleSneak()) {
			Wrapper.getInstance().updateMovement((MovementInputFromOptions)this.movementInput, this);
		} else {
			this.movementInput.updatePlayerMoveState();
		}

        if (this.isUsingItem() && !this.isRiding())
        {
            this.movementInput.moveStrafe *= 0.2F;
            this.movementInput.moveForward *= 0.2F;
            this.sprintToggleTimer = 0;
        }

        this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ + (double)this.width * 0.35D);
        this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ - (double)this.width * 0.35D);
        this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ - (double)this.width * 0.35D);
        this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ + (double)this.width * 0.35D);
        boolean flag3 = (float)this.getFoodStats().getFoodLevel() > 6.0F || this.capabilities.allowFlying;

    	/*
		 * Toggle Sneak Changes
		 */
        if(Wrapper.getInstance().isToggleSneak()) {
			boolean isSprintDisabled	= !Wrapper.getInstance().getOptionToggleSprint();
			boolean canDoubleTap		= Wrapper.getInstance().getOptionDoubleTap();

			// Detect when ToggleSprint was disabled in the in-game options menu
			if(Wrapper.getInstance().wasSprintDisabled())
			{
				this.setSprinting(false);
				Wrapper.getInstance().updateSprint(false, false);
				Wrapper.getInstance().setSprintDisabled(false);
			}

			// Default Sprint routine converted to PlayerAPI, use if ToggleSprint is disabled
			if(isSprintDisabled)
			{
				if(Wrapper.getInstance().getOptionDoubleTap() && this.onGround && !flag2 && this.movementInput.moveForward >= f && !this.isSprinting() && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness))
				{
					if(this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown())
					{
						this.sprintToggleTimer = 7;
					}
					else
					{
						this.setSprinting(true);
						Wrapper.getInstance().updateSprint(true, false);
					}
				}

				if(!this.isSprinting() && this.movementInput.moveForward >= f && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness) && this.mc.gameSettings.keyBindSprint.isKeyDown())
				{
					this.setSprinting(true);
					Wrapper.getInstance().updateSprint(true, false);
				}
			}
			else
			{
				boolean state = Wrapper.getInstance().getSprintState();
				
				// Only handle changes in state under the following conditions:
				// On ground, not hungry, not eating/using item, not blind, and not Vanilla
				//
				// 5/6/14 - onGround check removed to match vanilla's 'start sprint while jumping' behavior.
				//if(this.player.onGround && enoughHunger && !this.player.isUsingItem() && !this.player.isPotionActive(Potion.blindness) && !this.customMovementInput.sprintHeldAndReleased)

				if(flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness) && !Wrapper.getInstance().getSprintHeldAndReleased())
				{
					if(canDoubleTap && !this.isSprinting() || !canDoubleTap)
					{
						this.setSprinting(state);
					}
				}

				if(canDoubleTap && !state && this.onGround && !flag2 && this.movementInput.moveForward >= f && !this.isSprinting() && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness))
				{
					if(this.sprintToggleTimer == 0)
					{
						this.sprintToggleTimer = 7;
					}
					else
					{
						this.setSprinting(true);
						Wrapper.getInstance().updateSprint(true, true);
						this.sprintToggleTimer = 0;
					}
				}
			}
			
			// If sprinting, break the sprint in appropriate circumstances:
			// Player stops moving forward, runs into something, or gets too hungry
			if (isSprinting() && (movementInput.moveForward < f || mc.thePlayer.isCollidedHorizontally || !flag3 || isUsingItem() || isPotionActive(Potion.blindness)))
			{
				this.setSprinting(false);
				// Undo toggle if we resumed vanilla operation due to Hold&Release, DoubleTap, Fly, Ride
				if (Wrapper.getInstance().getSprintHeldAndReleased() || isSprintDisabled || Wrapper.getInstance().getSprintDoubleTapped() || this.isRiding() || (!Wrapper.getInstance().getOptionToggleFlyboost() ? this.capabilities.isFlying : false) )
				{
					Wrapper.getInstance().updateSprint(false, false);
				}
			}
			
		} else {

			if (this.onGround && !flag1 && !flag2 && this.movementInput.moveForward >= f && !this.isSprinting() && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness))
			{
				if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown())
				{
					this.sprintToggleTimer = 7;
				}
				else
				{
					this.setSprinting(true);
				}
			}

			if (!this.isSprinting() && this.movementInput.moveForward >= f && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness) && this.mc.gameSettings.keyBindSprint.isKeyDown())
			{
				this.setSprinting(true);
			}

			if (this.isSprinting() && (this.movementInput.moveForward < f || !flag3))
			{
				this.setSprinting(false);
			}
		}
		/*
		 * Fly Boost Changes
		 */
        if(Wrapper.getInstance().isToggleSneak()) {

			if(Wrapper.getInstance().getOptionFlyboost() && this.capabilities.isFlying && Wrapper.getInstance().getSprintState())
			{
				if(Wrapper.getInstance().getSprintState() && Wrapper.getInstance().getOptionToggleFlyboost()) {
					this.capabilities.setFlySpeed(0.05F * (float)Wrapper.getInstance().getFlyBoostSpeedHorizontal());
					if(this.movementInput.sneak)	this.motionY -= 0.15D * (double)Wrapper.getInstance().getFlyBoostSpeedVertical();
					if(this.movementInput.jump)	this.motionY += 0.15D * (double)Wrapper.getInstance().getFlyBoostSpeedVertical();
				} else if(!Wrapper.getInstance().getOptionToggleFlyboost() && this.mc.gameSettings.keyBindSprint.isKeyDown()) {
					this.capabilities.setFlySpeed(0.05F * (float)Wrapper.getInstance().getFlyBoostSpeedHorizontal());
					if(this.movementInput.sneak)	this.motionY -= 0.15D * (double)Wrapper.getInstance().getFlyBoostSpeedVertical();
					if(this.movementInput.jump)	this.motionY += 0.15D * (double)Wrapper.getInstance().getFlyBoostSpeedVertical();	
				}
			}
			else if(this.capabilities.getFlySpeed() != 0.05F)
			{
				this.capabilities.setFlySpeed(0.05F);
			}
			if(this.capabilities.allowFlying && !isJumping && this.movementInput.jump)
			{
				if(this.flyToggleTimer == 0)
				{
					this.flyToggleTimer = 7;
				}
				else
				{
					this.capabilities.isFlying = !this.capabilities.isFlying;
					this.sendPlayerAbilities();
					this.flyToggleTimer = 0;
				}
			}

			if(this.capabilities.isFlying)
			{
				if(this.movementInput.sneak)
				{
					this.motionY -= 0.15D;
				}
				if(this.movementInput.jump)
				{
					this.motionY += 0.15D;
				}
			}
		} else {
			//DEFAULT MC
			if (this.capabilities.allowFlying)
			{
				//Matt
				if(this.capabilities.getFlySpeed() != 0.05F)
				{
					this.capabilities.setFlySpeed(0.05F);
				}
				
				if (this.mc.playerController.isSpectatorMode())
				{
					if (!this.capabilities.isFlying)
					{
						this.capabilities.isFlying = true;
						this.sendPlayerAbilities();
					}
				}
				else if (!flag && this.movementInput.jump)
				{
					if (this.flyToggleTimer == 0)
					{
						this.flyToggleTimer = 7;
					}
					else
					{
						this.capabilities.isFlying = !this.capabilities.isFlying;
						this.sendPlayerAbilities();
						this.flyToggleTimer = 0;
					}
				}
			}

			if (this.capabilities.isFlying && this.isCurrentViewEntity())
			{
				if (this.movementInput.sneak)
				{
					this.motionY -= (double)(this.capabilities.getFlySpeed() * 3.0F);
				}

				if (this.movementInput.jump)
				{
					this.motionY += (double)(this.capabilities.getFlySpeed() * 3.0F);
				}
			}
		}
		//END


        if (this.isRidingHorse())
        {
            if (this.horseJumpPowerCounter < 0)
            {
                ++this.horseJumpPowerCounter;

                if (this.horseJumpPowerCounter == 0)
                {
                    this.horseJumpPower = 0.0F;
                }
            }

            if (flag && !this.movementInput.jump)
            {
                this.horseJumpPowerCounter = -10;
                this.sendHorseJump();
            }
            else if (!flag && this.movementInput.jump)
            {
                this.horseJumpPowerCounter = 0;
                this.horseJumpPower = 0.0F;
            }
            else if (flag)
            {
                ++this.horseJumpPowerCounter;

                if (this.horseJumpPowerCounter < 10)
                {
                    this.horseJumpPower = (float)this.horseJumpPowerCounter * 0.1F;
                }
                else
                {
                    this.horseJumpPower = 0.8F + 2.0F / (float)(this.horseJumpPowerCounter - 9) * 0.1F;
                }
            }
        }
        else
        {
            this.horseJumpPower = 0.0F;
        }

        super.onLivingUpdate();

        if (this.onGround && this.capabilities.isFlying && !this.mc.playerController.isSpectatorMode())
        {
            this.capabilities.isFlying = false;
            this.sendPlayerAbilities();
        }
    }
}