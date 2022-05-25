package falcun.net.managers;

import falcun.net.Falcun;
import falcun.net.gui.container.playerinventory.FalcunGuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.LinkedHashMap;
import java.util.Map;

public final class FalcunKeyBindManager {
	public static FalcunKeyBindManager instance = new FalcunKeyBindManager();
	private static final Map<KeyBinding, Runnable> falcunKeyBindings = new LinkedHashMap<>();

	static {
		ClientRegistry.registerKeyBinding(setupKey("Open Gui", FalcunGuiManager::openModGui, Keyboard.KEY_RSHIFT));
	}

	private static KeyBinding setupKey(String name, Runnable r) {
		return setupKey(name, r, 0);
	}

	private static KeyBinding setupKey(String name, Runnable r, int keyCode) {
		KeyBinding key = new KeyBinding(name, keyCode, "Falcun");
		falcunKeyBindings.put(key, r);
		return key;
	}

	@SubscribeEvent
	public void key(InputEvent.KeyInputEvent e) {
//		if (Falcun.minecraft.gameSettings.keyBindInventory.isPressed()) {
//			FalcunGuiManager.openInventoryGui();
//		}
		for (Map.Entry<KeyBinding, Runnable> kv : falcunKeyBindings.entrySet()) {
			if (kv.getKey().isPressed()) {
				kv.getValue().run();
			}
		}
	}

}
