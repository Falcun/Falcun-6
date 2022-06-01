package net.mattbenson.modules.types.other;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.input.KeyDownEvent;
import net.mattbenson.events.types.input.KeyUpEvent;
import net.mattbenson.input.KeybindManager;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class PerspectiveMod extends Module {
	@ConfigValue.Boolean(name = "Require Hold")
	private boolean returnOnRelease = false;
	
	@ConfigValue.Boolean(name = "Show own name")
	public boolean ownName = false;
	
	@ConfigValue.Keybind(name = "Perspective Key")
	private int keyBind = 0;
	
	public static boolean perspectiveToggled = false;
	public static float cameraYaw = 0F;
	public static float cameraPitch = 0F;
	public int previousPerspective = 0;
	public boolean isEmoting = false;
	public boolean isCrating = false;
	
	public PerspectiveMod() {
		super("360 Perspective", ModuleCategory.OTHER);
	}
	
	@Override
	public void onDisable() {
		perspectiveToggled = false;
		mc.gameSettings.thirdPersonView = previousPerspective;
	}
	
	@SubscribeEvent
	public void onKeyPress(KeyDownEvent event) {
		//Matt
		//Line to stop chat 
		if(KeybindManager.isInvalidScreen(mc.currentScreen)) {
			return;
		}
		
		if (keyBind == 0)
			return;

		if (event.getKey() == keyBind) {
			perspectiveToggled = !perspectiveToggled;

			if (perspectiveToggled) {
				previousPerspective = mc.gameSettings.thirdPersonView;
				mc.gameSettings.thirdPersonView = 1;
				cameraPitch = mc.thePlayer.rotationPitch;
				cameraYaw = mc.thePlayer.rotationYaw;
				mc.entityRenderer.loadEntityShader((Entity) null);
				Minecraft.getMinecraft().renderGlobal.setDisplayListEntitiesDirty();
			} else {
				mc.gameSettings.thirdPersonView = previousPerspective;
			}
		}

		if (event.getKey() == mc.gameSettings.keyBindTogglePerspective.getKeyCode()) {
			perspectiveToggled = false;
		}
	}

	@SubscribeEvent
	public void onKeyUp(KeyUpEvent event) {
		if (event.getKey() == keyBind) {
			if (returnOnRelease) {
				perspectiveToggled = false;
				mc.gameSettings.thirdPersonView = previousPerspective;
			}
		}
	}

	public float getCameraYaw() {
		return perspectiveToggled ? cameraYaw : Minecraft.getMinecraft().thePlayer.rotationYaw;
	}

	public float getCameraPitch() {
		return perspectiveToggled ? cameraPitch : Minecraft.getMinecraft().thePlayer.rotationPitch;
	}

	public boolean overrideMouse() {
		if (Minecraft.getMinecraft().inGameHasFocus && Display.isActive() && (isEnabled() || isEmoting || isCrating)) {
			if (!perspectiveToggled) {
				return true;
			}

			// CODE
			Minecraft.getMinecraft().mouseHelper.mouseXYChange();
			float f1 = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
			float f2 = f1 * f1 * f1 * 8.0F;
			float f3 = (float) Minecraft.getMinecraft().mouseHelper.deltaX * f2;
			float f4 = (float) Minecraft.getMinecraft().mouseHelper.deltaY * f2;

			cameraYaw += f3 * 0.15F;
			cameraPitch -= f4 * 0.15F;
			
			if (cameraPitch > 90)
				cameraPitch = 90;
			if (cameraPitch < -90)
				cameraPitch = -90;
			Minecraft.getMinecraft().renderGlobal.setDisplayListEntitiesDirty();
		}

		return false;
	}
}
