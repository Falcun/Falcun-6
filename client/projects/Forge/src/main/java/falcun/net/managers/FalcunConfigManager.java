package falcun.net.managers;

import falcun.net.api.modules.FalcunModule;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Map;

public final class FalcunConfigManager {


	public static final Map<Class<?>, FalcunModule> modules = new Object2ObjectOpenHashMap<>();
	public static final Map<Class<?>, FalcunModule> fps = new Object2ObjectOpenHashMap<>();


	static {

	}

	public static void init() {

	}


}
