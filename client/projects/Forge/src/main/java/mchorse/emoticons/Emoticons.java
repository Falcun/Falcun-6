package mchorse.emoticons;

import net.mattbenson.events.types.MinecraftInitEvent;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Emoticons mod
 * 
 * This mod provides a functionality to load and playback animated 
 * models. This is going to make Minecraft machinimas 100% awesome!
 */
public final class Emoticons
{
  
	public Emoticons() {
		instance = this;
	}
	
    public static Emoticons instance;

    public static ClientProxy proxy = new ClientProxy();

    public static String config;

    public static boolean disableSoundEvents = false;

    public static boolean disableAnimations = false;
    public static boolean simpleModels = false;

    public static int playerPreviewMode = 0;
    public static int playerRenderingScale = 10;
    public static int playerRenderingX = 0;
    public static int playerRenderingY= 0;
    
    

    /**
     * Custom payload channel 
     */
 
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	proxy.preInit(event);
    }

    @EventHandler
    public void init(MinecraftInitEvent event)
    {
        proxy.init(event);
    }
}