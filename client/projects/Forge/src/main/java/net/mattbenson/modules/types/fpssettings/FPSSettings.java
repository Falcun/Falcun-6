package net.mattbenson.modules.types.fpssettings;

import java.util.ArrayList;
import java.util.List;

import net.mattbenson.Falcun;
import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.world.OnTickEvent;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.modules.types.factions.Patchcrumbs;
import net.mattbenson.modules.types.fpssettings.cruches.EntityCulling;
import net.mattbenson.modules.types.fpssettings.cruches.FontRenderHook;
import net.mattbenson.modules.types.fpssettings.cruches.RenderItemHook;
import net.mattbenson.utils.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.BlockPos;

public class FPSSettings extends Module {
	//Code at VisGraph.java
	//Code at EntityItem.java
	//Code at ThreadDownloadImageData.java
	//Code at EntityFX.java
	//Code at Block.java
	//Code at BlockLiquid.java
	//Code at RegionRenderCache.java
	//Code at Render.java
	//Code at Entity.java
	//Code at ChunkCache.java
	//Code at World.java
	//Code at DynamicLights.java
	//Code at ChunkRenderDispatcher.java
	//Code at TileEntityMobSpawnerRenderer.java
	//Code at RenderEntityItem.java
	//Code at Minecraft.java
	//Code at RenderItem.java
	//Code at LayerArmorBase.java
	//Code at RenderTNTPrimed.java
	//Code at ActiveRenderInfo.java
	//Code at BlockFluidRenderer.java
	//Code at NetHandlerPlayClient.java
	//Code at ArmorStandRenderer.java
	//Code at BlockRendererDispatcher.java
	//Code at RenderGlobal.java
	//Code at ModelChest.java
	
	@ConfigValue.Boolean(name = "Culling fix", description = "Fixes a bug which can cause chunks to not sometimes render, this can affect fps negatively.")
	public boolean CULLING_FIX 						= true;
	
	@ConfigValue.Boolean(name = "Entity Culling", description = "")
	public boolean ENTITY_CULLING 					= true;
	
	public int	  ENTITY_CULLING_INTERVAL 			= 0;	// Values accepted: [0, 1, 2]. Lower number = less updates, less fps demands.
	
	public boolean SMART_ENTITY_CULLING	 			= true;
	
	public boolean DONT_CULL_PLAYER_NAMETAGS 		= true;
	
	public boolean DONT_CULL_ENTITY_NAMETAGS 		= true;
	
	public boolean DONT_CULL_ARMOR_STANDS_NAMETAGS 	= true;
	
	public boolean PARTICLE_CULLING 				= true;
	
	public boolean ITEM_SEARCHING 					= true;
	
	public boolean OPTIMISED_ITEM_RENDERER			= true;
	
	public boolean OPTIMISED_FONT_RENDERER			= true;
	
	public boolean CACHED_FONT_DATA					= true;
	
	@ConfigValue.Boolean(name = "Low animation tick", description = "Renders animations slower for better performance.")
	public boolean LOW_ANIMATION_TICK 				= true;
	
	public boolean BATCH_MODEL_RENDERING 			= true; 
	
	public static boolean DISABLE_GL_ERROR_CHECKING 		= true; 
	
	@ConfigValue.Boolean(name = "Static particle color", description = "Makes particles render at full brightness.")
	public boolean STATIC_PARTICLE_COLOR			= true;
	
	@ConfigValue.Boolean(name = "Better skin loading", description = "Makes so skin loading should be less stuttery when you join servers.")
	public boolean BETTER_SKIN_LOADING				= true;
	
	@ConfigValue.Boolean(name = "Remove light calculation", description = "Removes light calculations, rendering the game in full brightness.")
	public boolean LIGHT_CALCULATION_REMOVAL		= true;
	
	@ConfigValue.Boolean(name = "Chunk update limiter", description = "Enable a chunk udpate limiter to improve performance")
	public boolean  CHUNK_UPDATE_LIMITE_ENABLED		= true;
	
	@ConfigValue.Integer(name = "Chunk updates per second", description = "Lower value, better fps.", min = 1, max = 250)
	public int	  CHUNK_UPDATE_LIMITER				= 50;	 // 0 = no cap, disabled.
		
	@ConfigValue.Boolean(name = "Remove text shadows", description = "Removes all text shadows.")
	public boolean REMOVE_TEXT_SHADOWS				= false;
	
	@ConfigValue.Boolean(name = "Remove chat background", description = "Removes the chat background.")
	public boolean REMOVE_CHAT_BACKGROUND			= false;
	
	@ConfigValue.Boolean(name = "Remove mob spawner entity", description = "Removes the spinning entity inside of mob spawners.")
	public boolean REMOVE_ENTITY_SPAWNER			= true;
	
	@ConfigValue.Boolean(name = "Static drops", description = "Items on ground no longer rotate.")
	public boolean STATIC_DROPS						= true;
	
	public boolean FAST_WORLD_LOADING				= true;
	
	@ConfigValue.Boolean(name = "Remove item glint", description = "Removes the enchantment glint from all items.")
	public boolean REMOVE_ITEM_GLINT				= true;
	
	@ConfigValue.Boolean(name = "Remove Piston Extentions", description = "Removes Piston Extentions Animation.")
    public static boolean REMOVE_PISTON_EXTENTION = false;
	
	@ConfigValue.Boolean(name = "Disable TNT Flashing", description = "Disables Prime TNT Flashing to help FPS")
	public boolean noLagTNTFlash 					= true;
    
	@ConfigValue.Boolean(name = "Remove TNT", description = "Removes all primed tnt blocks.")
	public boolean REMOVE_TNT 						= false;
    
	@ConfigValue.Boolean(name = "Disable TNT Expand", description = "Disables Prime TNT Expanding to help FPS")
	public boolean noLagTNTExpand					= true;
    
	@ConfigValue.Boolean(name = "Merge TNT", description = "If Prime TNT is in the same block, render it as 1 Prime TNT")
	public static boolean noLagStackTNT					= true;
    
	@ConfigValue.Boolean(name = "Holograms Render", description = "Disables the render of holograms")
	public boolean noLagHolograms 					= true;   
    
	@ConfigValue.Boolean(name = "Custom Cane Renderer", description = "Only render cane in a certain radius to help FPS")
	public boolean noLagCane 						= false;
    
	@ConfigValue.Boolean(name = "Liquid Vision", description = "Makes it clear in water an lava")
	public boolean noLagLiquidVision 				= false;

	@ConfigValue.Boolean(name = "Remove Water", description = "Removes the render of water")
    public static boolean noLagClearWater = false;
    
	@ConfigValue.Integer(name = "Tile Entity Render Distance", min = 1, max = 64)
	public int noLagBlockDistance 					= 32;
	
	@ConfigValue.Integer(name = "Entity Render Distance", min = 1, max = 64)
	public int noLagEntityDistance 					= 32;
    
	@ConfigValue.Boolean(name = "Disable fog", description = "Disables fog")
	public boolean noFog 							= true;
    
	@ConfigValue.Boolean(name = "Better Chests", description = "Disable render of knob/lid/reduce size of chests")
	public boolean noBetterChests = false;
	
	@ConfigValue.Boolean(name = "Remove Foliage", description = "")
	public boolean noFoliage = true;
	
	@ConfigValue.Boolean(name = "Stop Mob Spawning", description = "")
	public boolean noMobs = false;
	
	@ConfigValue.Boolean(name = "Stop Hit Red", description = "")
	public boolean noHitRed = false;
	
	public static RenderItemHook renderItemHook;
	public static Timer timer = new Timer();

	public static FontRenderHook patcherFontRenderer;
	public boolean initd;
	
	public FPSSettings() {
		super("FPS Settings", ModuleCategory.FPS_SETTINGS);
		
		renderItemHook = new RenderItemHook();
		patcherFontRenderer = new FontRenderHook(mc.fontRendererObj);
		
		Falcun.getInstance().EVENT_BUS.register(new EntityCulling());
	}
	
	@SubscribeEvent
	public void onTick(OnTickEvent event) {

	}

}
