package net.mattbenson.gui.menu.pages;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.mattbenson.Falcun;
import net.mattbenson.config.ConfigEntry;
import net.mattbenson.config.types.BooleanEntry;
import net.mattbenson.config.types.ColorEntry;
import net.mattbenson.config.types.DoubleEntry;
import net.mattbenson.config.types.FloatEntry;
import net.mattbenson.config.types.IntEntry;
import net.mattbenson.config.types.ListEntry;
import net.mattbenson.config.types.StringEntry;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.Menu;
import net.mattbenson.gui.framework.MenuComponent;
import net.mattbenson.gui.framework.TextPattern;
import net.mattbenson.gui.menu.IngameMenu;
import net.mattbenson.gui.menu.Page;
import net.mattbenson.gui.menu.components.cosmetics.CosmeticGenericButton;
import net.mattbenson.gui.menu.components.fps.FPSGenericButton;
import net.mattbenson.gui.menu.components.fps.FlipButtonFPS;
import net.mattbenson.gui.menu.components.fps.FlipButtonParent;
import net.mattbenson.gui.menu.components.fps.MainWindowBackgroundPS;
import net.mattbenson.gui.menu.components.fps.OptifineParentBackground;
import net.mattbenson.gui.menu.components.macros.FlipButton;
import net.mattbenson.gui.menu.components.mods.FeatureText;
import net.mattbenson.gui.menu.components.mods.FeatureValueText;
import net.mattbenson.gui.menu.components.mods.MenuModCheckbox;
import net.mattbenson.gui.menu.components.mods.MenuModColorPicker;
import net.mattbenson.gui.menu.components.mods.MenuModKeybind;
import net.mattbenson.gui.menu.components.mods.MenuModList;
import net.mattbenson.gui.menu.components.mods.MenuModSlider;
import net.mattbenson.gui.menu.components.mods.ModCategoryButton;
import net.mattbenson.gui.menu.components.mods.ModScrollPane;
import net.mattbenson.gui.menu.components.mods.ModTextbox;
import net.mattbenson.gui.menu.pages.fps.BlacklistModule;
import net.mattbenson.gui.menu.pages.fps.BlacklistType;
import net.mattbenson.gui.menu.pages.fps.FPSSubTab;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.types.fpssettings.FPSSettings;
import net.mattbenson.modules.types.general.Settings;
import net.mattbenson.utils.AssetUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockBarrier;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockBrewingStand;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockCarrot;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDropper;
import net.minecraft.block.BlockEnchantmentTable;
import net.minecraft.block.BlockEndPortal;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.BlockRail;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.BlockRedstoneLight;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.BlockWeb;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;

public class FPSPage extends Page {
	private static ResourceLocation OF_LOW;
	private static ResourceLocation OF_MEDMED;
	private static ResourceLocation OF_HIGH;
	
	public static List<Class<? extends Entity>> ENTITIES;
	public static List<Class<? extends TileEntity>> TILE_ENTITIES;
	public static List<Class<? extends Block>> BLOCKS;
	public static List<EnumParticleTypes> PARTICLES;
	
	private static List<BlacklistModule> blacklistModules;
	
	private CosmeticGenericButton entities;
	private CosmeticGenericButton tileEntities;
	private CosmeticGenericButton blocks;
	private CosmeticGenericButton particles;
	
	private ModScrollPane blacklistPane;
	private BlacklistType currentType;
	
	private MainWindowBackgroundPS menuBackground;
	private MainWindowBackgroundPS fpsBackground;
	private ModScrollPane genericPane;
	
	private CosmeticGenericButton fpsButton;
	private CosmeticGenericButton optifineButton;
	private CosmeticGenericButton settingsButton;
	
	private FPSSubTab subTab;
	private boolean cleared;
	
	static {
		ENTITIES = new ArrayList<>();
		TILE_ENTITIES = new ArrayList<>();
		BLOCKS = new ArrayList<>();
		PARTICLES = new ArrayList<>();
		blacklistModules = new ArrayList<>();
		
		for(EnumParticleTypes type : EnumParticleTypes.values()) {
			registerParticle(type.toString(), type);
		}
		
		registerEntity("Armor Stand", EntityArmorStand.class);
		registerEntity("Arrow", EntityArrow.class);
		registerEntity("FallingSand", EntityFallingBlock.class);
		registerEntity("Paintings", EntityPainting.class);
		registerEntity("Bat", EntityBat.class);
		registerEntity("Blaze", EntityBlaze.class);
		registerEntity("Boat", EntityBoat.class);
		registerEntity("CaveSpider", EntityCaveSpider.class);
		registerEntity("Chicken", EntityChicken.class);
		registerEntity("Cow", EntityCow.class);
		registerEntity("Creeper", EntityCreeper.class);
		registerEntity("Enderman", EntityEnderman.class);
		registerEntity("Horse", EntityHorse.class);
		registerEntity("Ghast", EntityGhast.class);
		registerEntity("Guardian", EntityGuardian.class);
		registerEntity("Ocelot", EntityOcelot.class);
		registerEntity("Pig", EntityPig.class);
		registerEntity("Zombie Pigmen", EntityPigZombie.class);
		registerEntity("Rabbit", EntityRabbit.class);
		registerEntity("Sheep", EntitySheep.class);
		registerEntity("Silverfish", EntitySilverfish.class);
		registerEntity("Skeleton", EntitySkeleton.class);
		registerEntity("Slime", EntitySlime.class);
		registerEntity("Spider", EntitySpider.class);
		registerEntity("Squid", EntitySquid.class);
		registerEntity("Villager", EntityVillager.class);
		registerEntity("Witch", EntityWitch.class);
		registerEntity("Wolf", EntityWolf.class);
		registerEntity("Zombie", EntityZombie.class);
	
		registerTileEntity("Banner", TileEntityBanner.class);
		registerTileEntity("Beacon", TileEntityBeacon.class);
		registerTileEntity("Chest", TileEntityChest.class);
		registerTileEntity("Comparitor", TileEntityComparator.class);
		registerTileEntity("Dropper", TileEntityDropper.class);
		registerTileEntity("Enchantment Table", TileEntityEnchantmentTable.class);
		registerTileEntity("Piston", TileEntityPiston.class);
		registerTileEntity("Sign", TileEntitySign.class);
		
		registerBlock("Anvil", BlockAnvil.class);
		registerBlock("Barrier", BlockBarrier.class);
		registerBlock("Beacon", BlockBeacon.class);
		registerBlock("Bed", BlockBed.class);
		registerBlock("Door", BlockDoor.class);
		registerBlock("Fence", BlockFence.class);
		registerBlock("Stairs", BlockStairs.class);
		registerBlock("Redstone Comparator", BlockRedstoneComparator.class);
		registerBlock("Redstone Diode", BlockRedstoneDiode.class);
		registerBlock("Redstone Light", BlockRedstoneLight.class);
		registerBlock("Redstone Repeater", BlockRedstoneRepeater.class);
		registerBlock("Redstone Torch", BlockRedstoneTorch.class);
		registerBlock("Redstone Wire", BlockRedstoneWire.class);
		registerBlock("Brewing Stand", BlockBrewingStand.class);
		registerBlock("Button", BlockButton.class);
		registerBlock("Cactus", BlockCactus.class);
		registerBlock("Carpet", BlockCarpet.class);
		registerBlock("Carrots", BlockCarrot.class);
		registerBlock("Cauldron", BlockCauldron.class);
		registerBlock("Chest", BlockChest.class);
		registerBlock("Cobweb", BlockWeb.class);
		registerBlock("Cocoa", BlockCocoa.class);
		registerBlock("Command Block", BlockCommandBlock.class);
		registerBlock("Crops", BlockCrops.class);
		registerBlock("Bush", BlockBush.class);
		registerBlock("Dispenser", BlockDispenser.class);
		registerBlock("Dropper", BlockDropper.class);
		registerBlock("Enchantment Table", BlockEnchantmentTable.class);
		registerBlock("End Portal", BlockEndPortal.class);
		registerBlock("Enderchest", BlockEnderChest.class);
		registerBlock("Fire", BlockFire.class);
		registerBlock("Flower", BlockFlower.class);
		registerBlock("Hopper", BlockHopper.class);
		registerBlock("Ladder", BlockLadder.class);
		registerBlock("Lava", BlockLiquid.class);
		registerBlock("Leaves", BlockLeaves.class);
		registerBlock("LilyPad", BlockLilyPad.class);
		registerBlock("Monster Spawner", BlockMobSpawner.class);
		registerBlock("Mushroom", BlockMushroom.class);
		registerBlock("Netherwart", BlockNetherWart.class);
		registerBlock("Piston Base", BlockPistonBase.class);
		registerBlock("Piston Extension", BlockPistonExtension.class);
		registerBlock("Piston Movement", BlockPistonMoving.class);
		registerBlock("Portal", BlockPortal.class);
		registerBlock("Rail", BlockRail.class);
		registerBlock("Sign", BlockSign.class);
		registerBlock("Snow", BlockSnow.class);
		registerBlock("Slab", BlockSlab.class);
		registerBlock("Trap Door", BlockTrapDoor.class);
		registerBlock("Tripwire", BlockTripWire.class);
		registerBlock("Tripwire Hook", BlockTripWireHook.class);
		
		registerEntity("Player (Local)", EntityPlayerSP.class);
		registerEntity("Player (Other)", EntityOtherPlayerMP.class);
	}
	
	public FPSPage(Minecraft mc, Menu menu, IngameMenu parent) {
		super(mc, menu, parent);
	}

	@Override
	public void onInit() {
		OF_LOW = AssetUtils.getResource("/gui/oflow.png");
		OF_MEDMED = AssetUtils.getResource("/gui/ofmed.png");
		OF_HIGH = AssetUtils.getResource("/gui/ofhigh.png");
		
		int x = 15;
		int y = 59 + 50;
		int typeWidth = 125;
		int typeHeight = 20;
		
		int width = 300;
		int compWidth = width - 6 - 20 * 2;
		
		entities = new CosmeticGenericButton("ENTITIES", x, y, typeWidth, typeHeight, false) {
			@Override
			public void onAction() {
				setActive(false);
				currentType = BlacklistType.ENTITY;
				populateScrollPane();
			}
		};
		
		tileEntities = new CosmeticGenericButton("TILEENTITIES", x + typeWidth + 20, y, typeWidth, typeHeight, false) {
			@Override
			public void onAction() {
				setActive(false);
				currentType = BlacklistType.TILE_ENTITY;
				populateScrollPane();
			}
		};
		
		blocks = new CosmeticGenericButton("BLOCKS", x, y + typeHeight + 10, typeWidth, typeHeight, false) {
			@Override
			public void onAction() {
				setActive(false);
				currentType = BlacklistType.BLOCK;
				populateScrollPane();
			}
		};
		
		particles = new CosmeticGenericButton("PARTICLES", x + typeWidth + 20, y + typeHeight + 10, typeWidth, typeHeight, false) {
			@Override
			public void onAction() {
				setActive(false);
				currentType = BlacklistType.PARTICLE;
				populateScrollPane();
			}
		};
		
		blacklistPane = new ModScrollPane(x, y + typeHeight * 2 + 20, width - x - 2, menu.getHeight() - y - typeHeight * 2 - 20 - 1, true);
		
		menuBackground = new MainWindowBackgroundPS(285 + 40 + 3, y - 25, menu.getWidth() - 285 - 40 * 2, 45);
		fpsBackground = new MainWindowBackgroundPS(285 + 40 + 3, y + 30, menu.getWidth() - 285 - 40 * 2, menu.getHeight() - y - 30 - 30);
		
		int bWidth = 207;
		int bSpace = 10;
		
		fpsButton = new FPSGenericButton("FPS", 285 + 43 + 15, y - 18, bWidth, 30, true) {
			@Override
			public void onAction() {
				setActive(false);
				subTab = FPSSubTab.FPS;
				populateMainBoard();
			}
		};
		
		optifineButton = new FPSGenericButton("OPTIFINE", 285 + 43 + 15 + bWidth + bSpace, y - 18, bWidth, 30, true) {
			@Override
			public void onAction() {
				setActive(false);
				subTab = FPSSubTab.OPTIFINE;
				populateMainBoard();
			}
		};
		
		settingsButton = new FPSGenericButton("SETTINGS", 285 + 43 + 15 + bWidth * 2 + bSpace * 2, y - 18, bWidth, 30, true) {
			@Override
			public void onAction() {
				setActive(false);
				subTab = FPSSubTab.SETTINGS;
				populateMainBoard();
			}
		};
		
		genericPane = new ModScrollPane(285 + 40 + 3, y + 30, menu.getWidth() - 285 - 40 * 2, menu.getHeight() - y - 30 - 30, true);
		
		entities.onAction();
		fpsButton.onAction();
	}

	@Override
	public void onRender() {
		int width = 300;
		int x = menu.getX() + menu.getWidth() - width + 20;
		int y = menu.getY() + 59;
		int height = 32;
		
		drawRectFalcun(menu.getX(), menu.getY() + 58, width, menu.getHeight() - 58, MacrosPage.MENU_SIDE_BG_COLOR);
				
		drawRectFalcun(menu.getX(), menu.getY() + 58, width, height + 1, ModCategoryButton.MAIN_COLOR);
		drawShadowDown(menu.getX(), y + height, width);
		
		drawShadowDown(menu.getX(), y - 1, width);
		
		Fonts.RobotoMiniHeader.drawString("FPS", menu.getX() + width / 2 - Fonts.RobotoMiniHeader.getStringWidth("FPS") / 2, y + height / 2 - Fonts.RobotoMiniHeader.getStringHeight("FPS") / 2, IngameMenu.MENU_HEADER_TEXT_COLOR);
	}

	@Override
	public void onLoad() {
		cleared = false;
		
		for(BlacklistModule module : blacklistModules) {
			boolean found = false;
			
			if(module.getType() == BlacklistType.BLOCK) {
				for(Class<? extends Block> block : BLOCKS) {
					if(module.getClazz() == block) {
						found = true;
						break;
					}
				}
			} else if(module.getType() == BlacklistType.ENTITY) {
				for(Class<? extends Entity> entity : ENTITIES) {
					if(module.getClazz() == entity) {
						found = true;
						break;
					}
				}
			} else if(module.getType() == BlacklistType.TILE_ENTITY) {
				for(Class<? extends TileEntity> tileEntity : TILE_ENTITIES) {
					if(module.getClazz() == tileEntity) {
						found = true;
						break;
					}
				}
			} else if(module.getType() == BlacklistType.PARTICLE) {
				for(EnumParticleTypes particle : PARTICLES) {
					if(particle.toString().equals(module.getParticle().toString())) {
						found = true;
						break;
					}
				}
			}
			
			module.setEnabled(found);
			
			for(MenuComponent comp : blacklistPane.getComponents()) {
				if(comp instanceof FlipButtonFPS) {
					FlipButtonFPS flip = (FlipButtonFPS) comp;
					
					if(flip.getModule() == module) {
						flip.setActive(found);
						break;
					}
				}
			}
		}
		
		menu.addComponent(entities);
		menu.addComponent(tileEntities);
		menu.addComponent(blocks);
		menu.addComponent(particles);
		
		menu.addComponent(blacklistPane);
		
		menu.addComponent(menuBackground);
		menu.addComponent(fpsBackground);
		menu.addComponent(genericPane);
		
		menu.addComponent(fpsButton);
		menu.addComponent(optifineButton);
		menu.addComponent(settingsButton);
	}
	
	@Override
	public void onUnload() {
		if(!cleared) {
			Minecraft.getMinecraft().renderGlobal.loadRenderers();
			cleared = true;
		}
	}
	
	@Override
	public void onOpen() {
		cleared = false;
	}
	
	@Override
	public void onClose() {
		if(!cleared) {
			Minecraft.getMinecraft().renderGlobal.loadRenderers();
			cleared = true;
		}
	}
	
	private void populateMainBoard() {
		genericPane.getComponents().clear();
		
		fpsButton.setActive(false);
		optifineButton.setActive(false);
		settingsButton.setActive(false);
		
		switch(subTab) {
			case FPS:
				fpsButton.setActive(true);
				break;
				
			case OPTIFINE:
				optifineButton.setActive(true);
				break;
				
			case SETTINGS:
				settingsButton.setActive(true);
				break;
		}	
		
		if(subTab == FPSSubTab.FPS) {
			initSettings(Falcun.getInstance().moduleManager.getModule(FPSSettings.class));
		} else if(subTab == FPSSubTab.OPTIFINE) {
			genericPane.addComponent(new OptifineParentBackground(OF_HIGH, "BEST FPS", "LOW QUALITY", 14, 16, 200, 280));
			genericPane.addComponent(new OptifineParentBackground(OF_MEDMED, "MEDIUM FPS", "MEDIUM QUALITY", 234, 16, 200, 280));
			genericPane.addComponent(new OptifineParentBackground(OF_LOW, "LOW FPS", "HIGH QUALITY", 454, 16, 200, 280));
			
			genericPane.addComponent(new CosmeticGenericButton("APPLY", 24, 257, 180, 30, true) {
				@Override
				public void onAction() {
					setActive(false);
					loadLowSettings();
				}
			});
			
			genericPane.addComponent(new CosmeticGenericButton("APPLY", 244, 257, 180, 30, true) {
				@Override
				public void onAction() {
					setActive(false);
					loadMedmedSettings();
				}
			});
			
			genericPane.addComponent(new CosmeticGenericButton("APPLY", 464, 257, 180, 30, true) {
				@Override
				public void onAction() {
					setActive(false);
					loadHighSettings();
				}
			});	
		} else if(subTab == FPSSubTab.SETTINGS) {
			initSettings(Falcun.getInstance().moduleManager.getModule(Settings.class));
		}
	}
	
	private void populateScrollPane() {
		blacklistPane.getComponents().clear();
		
		entities.setActive(false);
		tileEntities.setActive(false);
		blocks.setActive(false);
		particles.setActive(false);
		
		switch(currentType) {
			case BLOCK:
				blocks.setActive(true);
				break;
				
			case ENTITY:
				entities.setActive(true);
				break;
				
			case PARTICLE:
				particles.setActive(true);
				break;
				
			case TILE_ENTITY:
				tileEntities.setActive(true);
				break;
		}
		
		int spacing = 10;
		int originalX = 0;
		int x = originalX;
		int y = 10;
		int width = blacklistPane.getWidth() - 15;
		int height = 30;
		
		for(BlacklistModule module : blacklistModules) {
			if(module.getType() != currentType) {
				continue;
			}
			
			blacklistPane.addComponent(new FlipButtonParent(module.getName(), x, y + height - 25, width - 10, height));
			
			FlipButton flip = new FlipButtonFPS(module, x + width - 60, y + height - 18, 40, 15) {
				@Override
				public void onAction() {
					module.setEnabled(isActive());
				}
			};
			
			flip.setActive(module.isEnabled());
			
			blacklistPane.addComponent(flip);
			
			y += height + spacing;
		}
	}
	
	private static void registerParticle(String name, EnumParticleTypes particle) {
		blacklistModules.add(new BlacklistModule(name, particle));
	}
	
	private static void registerEntity(String name, Class<? extends Entity> entity) {
		if(name.contains(".") || name.contains("_")) {
			return;
		}
		
		blacklistModules.add(new BlacklistModule(name, BlacklistType.ENTITY, entity));
	}
	
	private static void registerTileEntity(String name, Class<? extends TileEntity> tileEntity) {
		if(name.contains(".") || name.contains("_")) {
			return;
		}
		
		blacklistModules.add(new BlacklistModule(name, BlacklistType.TILE_ENTITY, tileEntity));
	}
	
	private static void registerBlock(String name, Class<? extends Block> block) {
		if(name.contains(".") || name.contains("_")) {
			return;
		}
		
		blacklistModules.add(new BlacklistModule(name, BlacklistType.BLOCK, block));
	}
	
	private void loadLowSettings() {
		Minecraft.getMinecraft().gameSettings.fancyGraphics = false;
		Minecraft.getMinecraft().gameSettings.renderDistanceChunks = 2;
		Minecraft.getMinecraft().gameSettings.ofFogType = 3;
		Minecraft.getMinecraft().gameSettings.ofFogStart = 0.2F;
		Minecraft.getMinecraft().gameSettings.ofOcclusionFancy = false;
		Minecraft.getMinecraft().gameSettings.ofSmoothFps = true;
		Minecraft.getMinecraft().gameSettings.ofSmoothWorld = true;
		Minecraft.getMinecraft().gameSettings.ofAoLevel = 0F;
		Minecraft.getMinecraft().gameSettings.ofClouds = 3;
		Minecraft.getMinecraft().gameSettings.ofCloudsHeight = 0.0F;
		Minecraft.getMinecraft().gameSettings.ofTrees = 1;
		Minecraft.getMinecraft().gameSettings.ofDroppedItems = 1;
		Minecraft.getMinecraft().gameSettings.ofRain = 3;
		Minecraft.getMinecraft().gameSettings.ofAnimatedWater = 2;
		Minecraft.getMinecraft().gameSettings.ofAnimatedLava = 2;
		Minecraft.getMinecraft().gameSettings.ofAnimatedFire = false;
		Minecraft.getMinecraft().gameSettings.ofAnimatedPortal = false;
		Minecraft.getMinecraft().gameSettings.ofAnimatedExplosion = false;
		Minecraft.getMinecraft().gameSettings.ofAnimatedFlame = false;
		Minecraft.getMinecraft().gameSettings.ofAnimatedRedstone = false;
		Minecraft.getMinecraft().gameSettings.ofAnimatedSmoke = false;
		Minecraft.getMinecraft().gameSettings.ofVoidParticles = false;
		Minecraft.getMinecraft().gameSettings.ofWaterParticles = false;
		Minecraft.getMinecraft().gameSettings.ofPortalParticles = false;
		Minecraft.getMinecraft().gameSettings.ofPortalParticles = false;
		Minecraft.getMinecraft().gameSettings.ofFireworkParticles = false;
		Minecraft.getMinecraft().gameSettings.ofDrippingWaterLava = false;
		Minecraft.getMinecraft().gameSettings.ofAnimatedTerrain = false;
		Minecraft.getMinecraft().gameSettings.ofAnimatedTextures = false;
		Minecraft.getMinecraft().gameSettings.ofRainSplash = false;
		Minecraft.getMinecraft().gameSettings.ofLagometer = false;
		Minecraft.getMinecraft().gameSettings.ofShowFps = false;
		Minecraft.getMinecraft().gameSettings.ofAutoSaveTicks = 30000;
		Minecraft.getMinecraft().gameSettings.ofBetterGrass = 3;
		Minecraft.getMinecraft().gameSettings.ofConnectedTextures = 3;
		Minecraft.getMinecraft().gameSettings.ofWeather = false;
		Minecraft.getMinecraft().gameSettings.ofSky = false;
		Minecraft.getMinecraft().gameSettings.ofStars = false;
		Minecraft.getMinecraft().gameSettings.ofSunMoon = false;
		Minecraft.getMinecraft().gameSettings.ofVignette = 1;
		Minecraft.getMinecraft().gameSettings.ofChunkUpdates = 1;
		Minecraft.getMinecraft().gameSettings.ofChunkUpdatesDynamic = false;
		Minecraft.getMinecraft().gameSettings.ofTime = 0;
		Minecraft.getMinecraft().gameSettings.ofClearWater = false;
		Minecraft.getMinecraft().gameSettings.ofAaLevel = 0;
		Minecraft.getMinecraft().gameSettings.ofAfLevel = 1;
		Minecraft.getMinecraft().gameSettings.ofProfiler = false;
		Minecraft.getMinecraft().gameSettings.ofBetterSnow = false;
		Minecraft.getMinecraft().gameSettings.ofSwampColors = false;
		Minecraft.getMinecraft().gameSettings.ofSmoothBiomes = false;
		Minecraft.getMinecraft().gameSettings.ofCustomFonts = false;
		Minecraft.getMinecraft().gameSettings.ofCustomColors = false;
		Minecraft.getMinecraft().gameSettings.ofCustomSky = false;
		Minecraft.getMinecraft().gameSettings.ofShowCapes = false;
		Minecraft.getMinecraft().gameSettings.ofNaturalTextures = false;
		Minecraft.getMinecraft().gameSettings.ofLazyChunkLoading = false;
		Minecraft.getMinecraft().gameSettings.ofDynamicFov = true;
		Minecraft.getMinecraft().gameSettings.ofDynamicLights = 3;
		Minecraft.getMinecraft().gameSettings.saveOptions();
		Minecraft.getMinecraft().gameSettings.loadOfOptions();
	}
	
	private void loadMedmedSettings() {
		Minecraft.getMinecraft().gameSettings.fancyGraphics = true;
		Minecraft.getMinecraft().gameSettings.renderDistanceChunks = 8;
		Minecraft.getMinecraft().gameSettings.ofFogType = 3;
		Minecraft.getMinecraft().gameSettings.ofFogStart = 0.2F;
		Minecraft.getMinecraft().gameSettings.ofOcclusionFancy = false;
		Minecraft.getMinecraft().gameSettings.ofSmoothFps = true;
		Minecraft.getMinecraft().gameSettings.ofSmoothWorld = true;
		Minecraft.getMinecraft().gameSettings.ofAoLevel = 0F;
		Minecraft.getMinecraft().gameSettings.ofClouds = 3;
		Minecraft.getMinecraft().gameSettings.ofCloudsHeight = 0.0F;
		Minecraft.getMinecraft().gameSettings.ofTrees = 1;
		Minecraft.getMinecraft().gameSettings.ofDroppedItems = 1;
		Minecraft.getMinecraft().gameSettings.ofRain = 3;
		Minecraft.getMinecraft().gameSettings.ofAnimatedWater = 2;
		Minecraft.getMinecraft().gameSettings.ofAnimatedLava = 2;
		Minecraft.getMinecraft().gameSettings.ofAnimatedFire = false;
		Minecraft.getMinecraft().gameSettings.ofAnimatedPortal = false;
		Minecraft.getMinecraft().gameSettings.ofAnimatedExplosion = true;
		Minecraft.getMinecraft().gameSettings.ofAnimatedFlame = false;
		Minecraft.getMinecraft().gameSettings.ofAnimatedRedstone = true;
		Minecraft.getMinecraft().gameSettings.ofAnimatedSmoke = true;
		Minecraft.getMinecraft().gameSettings.ofVoidParticles = false;
		Minecraft.getMinecraft().gameSettings.ofWaterParticles = false;
		Minecraft.getMinecraft().gameSettings.ofPortalParticles = false;
		Minecraft.getMinecraft().gameSettings.ofPortalParticles = false;
		Minecraft.getMinecraft().gameSettings.ofFireworkParticles = false;
		Minecraft.getMinecraft().gameSettings.ofDrippingWaterLava = false;
		Minecraft.getMinecraft().gameSettings.ofAnimatedTerrain = true;
		Minecraft.getMinecraft().gameSettings.ofAnimatedTextures = true;
		Minecraft.getMinecraft().gameSettings.ofRainSplash = false;
		Minecraft.getMinecraft().gameSettings.ofLagometer = false;
		Minecraft.getMinecraft().gameSettings.ofShowFps = false;
		Minecraft.getMinecraft().gameSettings.ofAutoSaveTicks = 30000;
		Minecraft.getMinecraft().gameSettings.ofBetterGrass = 3;
		Minecraft.getMinecraft().gameSettings.ofConnectedTextures = 3;
		Minecraft.getMinecraft().gameSettings.ofWeather = false;
		Minecraft.getMinecraft().gameSettings.ofSky = true;
		Minecraft.getMinecraft().gameSettings.ofStars = true;
		Minecraft.getMinecraft().gameSettings.ofSunMoon = true;
		Minecraft.getMinecraft().gameSettings.ofVignette = 1;
		Minecraft.getMinecraft().gameSettings.ofChunkUpdates = 1;
		Minecraft.getMinecraft().gameSettings.ofChunkUpdatesDynamic = false;
		Minecraft.getMinecraft().gameSettings.ofTime = 0;
		Minecraft.getMinecraft().gameSettings.ofClearWater = true;
		Minecraft.getMinecraft().gameSettings.ofAaLevel = 0;
		Minecraft.getMinecraft().gameSettings.ofAfLevel = 1;
		Minecraft.getMinecraft().gameSettings.ofProfiler = false;
		Minecraft.getMinecraft().gameSettings.ofBetterSnow = false;
		Minecraft.getMinecraft().gameSettings.ofSwampColors = false;
		Minecraft.getMinecraft().gameSettings.ofSmoothBiomes = true;
		Minecraft.getMinecraft().gameSettings.ofCustomFonts = false;
		Minecraft.getMinecraft().gameSettings.ofCustomColors = true;
		Minecraft.getMinecraft().gameSettings.ofCustomSky = false;
		Minecraft.getMinecraft().gameSettings.ofShowCapes = false;
		Minecraft.getMinecraft().gameSettings.ofNaturalTextures = false;
		Minecraft.getMinecraft().gameSettings.ofLazyChunkLoading = false;
		Minecraft.getMinecraft().gameSettings.ofDynamicFov = true;
		Minecraft.getMinecraft().gameSettings.ofDynamicLights = 3;
		Minecraft.getMinecraft().gameSettings.saveOptions();
		Minecraft.getMinecraft().gameSettings.loadOfOptions();
	}
	
	private void loadHighSettings() {
		Minecraft.getMinecraft().gameSettings.fancyGraphics = true;
		Minecraft.getMinecraft().gameSettings.renderDistanceChunks = 32;
		Minecraft.getMinecraft().gameSettings.ofFogType = 1;
		Minecraft.getMinecraft().gameSettings.ofFogStart = 0.6F;
		Minecraft.getMinecraft().gameSettings.ofOcclusionFancy = false;
		Minecraft.getMinecraft().gameSettings.ofSmoothFps = true;
		Minecraft.getMinecraft().gameSettings.ofSmoothWorld = true;
		Minecraft.getMinecraft().gameSettings.ofAoLevel = 1.0F;
		Minecraft.getMinecraft().gameSettings.ofClouds = 2;
		Minecraft.getMinecraft().gameSettings.ofCloudsHeight = 0.0F;
		Minecraft.getMinecraft().gameSettings.ofTrees = 2;
		Minecraft.getMinecraft().gameSettings.ofDroppedItems = 1;
		Minecraft.getMinecraft().gameSettings.ofRain = 2;
		Minecraft.getMinecraft().gameSettings.ofAnimatedWater = 0;
		Minecraft.getMinecraft().gameSettings.ofAnimatedLava = 0;
		Minecraft.getMinecraft().gameSettings.ofAnimatedFire = true;
		Minecraft.getMinecraft().gameSettings.ofAnimatedPortal = true;
		Minecraft.getMinecraft().gameSettings.ofAnimatedExplosion = true;
		Minecraft.getMinecraft().gameSettings.ofAnimatedFlame = true;
		Minecraft.getMinecraft().gameSettings.ofAnimatedRedstone = true;
		Minecraft.getMinecraft().gameSettings.ofAnimatedSmoke = true;
		Minecraft.getMinecraft().gameSettings.ofVoidParticles = true;
		Minecraft.getMinecraft().gameSettings.ofWaterParticles = true;
		Minecraft.getMinecraft().gameSettings.ofPortalParticles = true;
		Minecraft.getMinecraft().gameSettings.ofPortalParticles = true;
		Minecraft.getMinecraft().gameSettings.ofFireworkParticles = true;
		Minecraft.getMinecraft().gameSettings.ofDrippingWaterLava = true;
		Minecraft.getMinecraft().gameSettings.ofAnimatedTerrain = true;
		Minecraft.getMinecraft().gameSettings.ofAnimatedTextures = true;
		Minecraft.getMinecraft().gameSettings.ofRainSplash = true;
		Minecraft.getMinecraft().gameSettings.ofLagometer = false;
		Minecraft.getMinecraft().gameSettings.ofShowFps = false;
		Minecraft.getMinecraft().gameSettings.ofAutoSaveTicks = 30000;
		Minecraft.getMinecraft().gameSettings.ofBetterGrass = 2;
		Minecraft.getMinecraft().gameSettings.ofConnectedTextures = 2;
		Minecraft.getMinecraft().gameSettings.ofWeather = false;
		Minecraft.getMinecraft().gameSettings.ofSky = true;
		Minecraft.getMinecraft().gameSettings.ofStars = true;
		Minecraft.getMinecraft().gameSettings.ofSunMoon = true;
		Minecraft.getMinecraft().gameSettings.ofVignette = 2;
		Minecraft.getMinecraft().gameSettings.ofChunkUpdates = 5;
		Minecraft.getMinecraft().gameSettings.ofChunkUpdatesDynamic = true;
		Minecraft.getMinecraft().gameSettings.ofTime = 0;
		Minecraft.getMinecraft().gameSettings.ofClearWater = true;
		Minecraft.getMinecraft().gameSettings.ofAaLevel = 0;
		Minecraft.getMinecraft().gameSettings.ofAfLevel = 1;
		Minecraft.getMinecraft().gameSettings.ofProfiler = false;
		Minecraft.getMinecraft().gameSettings.ofBetterSnow = true;
		Minecraft.getMinecraft().gameSettings.ofSwampColors = true;
		Minecraft.getMinecraft().gameSettings.ofSmoothBiomes = true;
		Minecraft.getMinecraft().gameSettings.ofCustomFonts = true;
		Minecraft.getMinecraft().gameSettings.ofCustomColors = true;
		Minecraft.getMinecraft().gameSettings.ofCustomSky = true;
		Minecraft.getMinecraft().gameSettings.ofShowCapes = true;
		Minecraft.getMinecraft().gameSettings.ofNaturalTextures = true;
		Minecraft.getMinecraft().gameSettings.ofLazyChunkLoading = true;
		Minecraft.getMinecraft().gameSettings.ofDynamicFov = true;
		Minecraft.getMinecraft().gameSettings.ofDynamicLights = 2;
		Minecraft.getMinecraft().gameSettings.saveOptions();
		Minecraft.getMinecraft().gameSettings.loadOfOptions();
	}
	
	private void initSettings(Module module) {
		List<MenuComponent> toAdd = new ArrayList<>();
		
		int xSpacing = 25;
		int ySpacing = 15;
		
		int sliderWidth = genericPane.getWidth() - xSpacing * 2;
		
		for(ConfigEntry configEntry : module.getEntries()) {
			final FeatureText label;
			
			String key = configEntry.getKey().toUpperCase();
			
			if(configEntry.hasDescription()) {
				toAdd.add(label = new FeatureText(key, 0, 0));
			} else {
				toAdd.add(label = new FeatureText(key, configEntry.getDescription(), 0, 0));
			}
			
			if(configEntry instanceof BooleanEntry) {
				BooleanEntry entry = (BooleanEntry) configEntry;
				
				MenuModCheckbox checkbox = new MenuModCheckbox(0, 0, 15, 15) {
					@Override
					public void onAction() {
						entry.setValue(module, isChecked());
					}
				};
				
				checkbox.setChecked((boolean) entry.getValue(module));
				
				toAdd.add(checkbox);
			} else if(configEntry instanceof ColorEntry) {
				ColorEntry entry = (ColorEntry) configEntry;
				
				toAdd.add(new MenuModColorPicker(0, 0, 15, 15, ((Color) entry.getValue(module)).getRGB()) {
					@Override
					public void onAction() {
						entry.setValue(module, getColor());
					}
				});
			} else if(configEntry instanceof DoubleEntry) {
				DoubleEntry entry = (DoubleEntry) configEntry;
				
				FeatureValueText valueText = new FeatureValueText("", 0, 0);
				
				toAdd.add(valueText);
				
				MenuModSlider slider = new MenuModSlider((double)entry.getValue(module), entry.getMin(), entry.getMax(), 2, 0, 0, sliderWidth, 15) {
					@Override
					public void onAction() {
						label.setText((entry.getKey() + " | ").toUpperCase());
						entry.setValue(module, (double)getValue());
						
						valueText.setText(getValue() + "");
					}
				};
					
				slider.onAction();
				
				toAdd.add(slider);
			} else if(configEntry instanceof FloatEntry) {
				FloatEntry entry = (FloatEntry) configEntry;
				
				FeatureValueText valueText = new FeatureValueText("", 0, 0);
				
				toAdd.add(valueText);
				
				MenuModSlider slider = new MenuModSlider((float)entry.getValue(module), entry.getMin(), entry.getMax(), 2, 0, 0, sliderWidth, 15) {
					@Override
					public void onAction() {
						label.setText((entry.getKey() + " | ").toUpperCase());
						entry.setValue(module, (float)getValue());
						
						valueText.setText(getValue() + "");
					}
				};
				
				slider.onAction();
										
				toAdd.add(slider);
			} else if(configEntry instanceof IntEntry) {
				IntEntry entry = (IntEntry) configEntry;
				
				FeatureValueText valueText = new FeatureValueText("", 0, 0);
				
				toAdd.add(valueText);
				
				if(entry.isKeyBind()) {
					MenuModKeybind bind = new MenuModKeybind(0, 0, 175, 15) {
						@Override
						public void onAction() {
							entry.setValue(module, (int) getBind());
						}
					};
					
					bind.setBind((int) entry.getValue(module));
					
					toAdd.add(bind);
				} else {
					MenuModSlider slider = new MenuModSlider((int)entry.getValue(module), entry.getMin(), entry.getMax(), 0, 0, sliderWidth, 15) {
						@Override
						public void onAction() {
							label.setText((entry.getKey() + " | ").toUpperCase());
							entry.setValue(module, getIntValue());
							
							valueText.setText(getIntValue() + "");
						}
					};
					
					slider.onAction();
					
					toAdd.add(slider);
				}
			} else if(configEntry instanceof ListEntry) {
				ListEntry entry = (ListEntry) configEntry;
				
				MenuModList list = new MenuModList(entry.getValues(), 0, 0, 15) {
					@Override
					public void onAction() {
						entry.setValue(module, getValue());
					}
				};
				
				list.setValue((String) configEntry.getValue(module));
				
				toAdd.add(list);
			} else if(configEntry instanceof StringEntry) {
				StringEntry entry = (StringEntry) configEntry;
				
				ModTextbox box = new ModTextbox(TextPattern.NONE, 0, 0, 175, 15) {
					@Override
					public void onAction() {
						entry.setValue(module, getText());
					}
				};
				
				box.setText((String) configEntry.getValue(module));
				
				toAdd.add(box);
			}
		}
		
		int defaultX = 25;
		
		int xPos = defaultX;
		int yPos = 25;
		
		boolean isText = false;
		MenuComponent last = null;
		
		for(MenuComponent component : toAdd) {
			 if(component instanceof FeatureValueText) {
				if(last != null) {
					component.setX(xPos);
					component.setY(yPos);
				}
			} else if(component instanceof FeatureText) {
				component.setX(xPos);
				component.setY(yPos);
				
				xPos += component.getWidth();
				
				isText = true;
			} else {
				xPos = defaultX;
				
				if(isText) {
					if(component instanceof MenuModSlider) {
						yPos += ySpacing;
						
						component.setX(xPos);
						component.setY(yPos);
					} else {
						if(component instanceof MenuModList) {
							component.setX(genericPane.getWidth() - component.getWidth() - xSpacing * 3 + 12);
						} else {
							component.setX(genericPane.getWidth() - component.getWidth() - xSpacing);
						}
						
						component.setY(yPos);
					}
					
					isText = false;
				} else {
					component.setX(xPos);
					component.setY(yPos);
				}
				
				yPos += ySpacing + component.getHeight();
			}
			
			 genericPane.addComponent(component);
			
			last = component;
		}
	}
}
