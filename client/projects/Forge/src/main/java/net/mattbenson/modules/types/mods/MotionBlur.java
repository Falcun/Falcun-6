package net.mattbenson.modules.types.mods;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.input.KeyDownEvent;
import net.mattbenson.events.types.world.OnTickEvent;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.utils.MotionBlurResourceManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

public class MotionBlur extends Module {
	@ConfigValue.Double(name = "Blur Amount", min = 1.0, max = 10)
	public static double amount = 2.0D;
	
	private Map domainResourceManagers;
	private Field cachedFastRender;
	private int ticks;
	
	public MotionBlur() {
		super("Motionblur", ModuleCategory.RENDER);
		
		try {
			cachedFastRender = GameSettings.class.getDeclaredField("ofFastRender");
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	
	

	@Override
	public void onEnable() {
        this.reloadShader();
    }

    @Override
    public void onDisable() {
        this.reloadShader();
    }

	@SubscribeEvent
	public void tick(OnTickEvent event) {
        if(mc.theWorld == null)
            return;
        if (this.domainResourceManagers == null) {
            try {
                Field[] var2 = SimpleReloadableResourceManager.class.getDeclaredFields();
                int var3 = var2.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    Field field = var2[var4];
                    if (field.getType() == Map.class) {
                        field.setAccessible(true);
                        this.domainResourceManagers = (Map)field.get(Minecraft.getMinecraft().getResourceManager());
                        break;
                    }
                }
            } catch (Exception var6) {
                throw new RuntimeException(var6);
            }
        }

        if (!this.domainResourceManagers.containsKey("motionblur")) {
            this.domainResourceManagers.put("motionblur", new MotionBlurResourceManager());
        }
	}

	@SubscribeEvent
	public void onKey(KeyDownEvent event) {
		if (mc.thePlayer != null) {
			mc.entityRenderer.loadShader(new ResourceLocation("motionblur", "motionblur"));
		}
	}
	
	public boolean isFastRenderEnabled() {
		try {
			return cachedFastRender.getBoolean(mc.gameSettings);
		} catch (Exception ignored) {
			return false;
		}
	}


	  public void reloadShader(){
	        if(mc.theWorld == null)
	            return;
	        try {
	            Method method = EntityRenderer.class.getDeclaredMethod("loadShader", ResourceLocation.class);
	            method.setAccessible(true);
	            method.invoke(this.mc.entityRenderer, new ResourceLocation("motionblur", "motionblur"));
	            this.mc.entityRenderer.getShaderGroup().createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
	        } catch (Throwable e) {
	            System.out.println("failed to load shader");
	            e.printStackTrace();
	        }
	    }
	
	
}