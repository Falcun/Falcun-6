package falcun.net.util;

import net.minecraft.launchwrapper.Launch;
import org.apache.logging.log4j.LogManager;

public class FalcunDevEnvironment {
	public static boolean isDevEnvironment;
	static {
		try {
			isDevEnvironment = Launch.classLoader.getClassBytes("net.minecraft.world.World") != null;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		try {
			LogManager.getLogger().info("Dev Environment: " + isDevEnvironment);
		} catch (Throwable err) {
			err.printStackTrace();
		}
	}
}
