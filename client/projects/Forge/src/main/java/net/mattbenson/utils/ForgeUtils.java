package net.mattbenson.utils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.mattbenson.Falcun;
import net.mattbenson.events.types.entity.AttachCapabilitiesEvent;
import net.mattbenson.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ForgeUtils {
	public static WeakReference<NetHandlerPlayClient> currentPlayClient;
	
	public static File getPreferredPath(Module mod) {
		return Paths.get(Falcun.MAIN_DIR.getAbsolutePath(), "mod-configs", mod.getName() + ".cfg").toFile();
	}

	public static File getPreferredPath(String mod) {
		return Paths.get(Falcun.MAIN_DIR.getAbsolutePath(), "mod-configs", mod + ".cfg").toFile();
	}
	
	public static File getPreferredFolder(String name) {
		return Paths.get(Falcun.MAIN_DIR.getAbsolutePath(), "mod-configs", name).toFile();
	}
	
	public static File getPreferredFolderFile(String folder, String name) {
		return Paths.get(Falcun.MAIN_DIR.getAbsolutePath(), "mod-configs", folder, name).toFile();
	}
	
	public static CapabilityDispatcher gatherCapabilities(Entity entity) {
		return gatherCapabilities(new AttachCapabilitiesEvent(entity), null);
	}
	
	private static CapabilityDispatcher gatherCapabilities(AttachCapabilitiesEvent event, ICapabilityProvider parent)  {
		Falcun.getInstance().EVENT_BUS.post(event);
		return event.getCapabilities().size() > 0 || parent != null ? new CapabilityDispatcher(event.getCapabilities(), parent) : null;
	}

	public static boolean isModLoaded(String string) {
		return true;
	}
	
	public static NetHandlerPlayClient getPlayClient() {
		return currentPlayClient == null ? null : currentPlayClient.get();
	}
}
