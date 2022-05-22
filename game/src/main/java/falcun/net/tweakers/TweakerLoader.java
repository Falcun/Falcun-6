package falcun.net.tweakers;

import falcun.net.discord.FalcunStatus;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;

import java.util.Map;

public class TweakerLoader implements IFMLLoadingPlugin {

	static {
		String string = "CREDITS TO @boredhuman on github for the hook api";
	}

	public static boolean deObfEnv;
	public static IFMLLoadingPlugin tweakerManager;
	public static boolean dancore;

	public static boolean getMCPEnvironment() {
		return !deObfEnv;
	}


	static {
		FalcunStatus.falcunStatus.loadRPC();
		try {
			TweakerLoader.deObfEnv = Launch.classLoader.getClassBytes("net.minecraft.world.World") == null;
			LogManager.getLogger().info("Deobfuscated env: " + TweakerLoader.deObfEnv);

			boolean nullbytes = Launch.classLoader.getClassBytes("dev.boredhuman.updater.UpdateTweaker") == null;
			boolean foundClass = false;
			try {
				Class.forName("dev.boredhuman.updater.UpdateTweaker");
				foundClass = true;
			} catch (Throwable ignored) {

			}
			dancore = !nullbytes && foundClass;
			Class<?> tweakerMangerClass = Launch.classLoader.findClass("falcun.net.tweakers.TweakManager");
			TweakerLoader.tweakerManager = (IFMLLoadingPlugin) tweakerMangerClass.newInstance();
		} catch (Throwable err) {
			err.printStackTrace();
		}
	}

	@Override
	public String[] getASMTransformerClass() {
		return TweakerLoader.tweakerManager == null ? null : TweakerLoader.tweakerManager.getASMTransformerClass();
	}

	@Override
	public String getModContainerClass() {
		return TweakerLoader.tweakerManager == null ? null : TweakerLoader.tweakerManager.getModContainerClass();
	}

	@Override
	public String getSetupClass() {
		return TweakerLoader.tweakerManager == null ? null : TweakerLoader.tweakerManager.getSetupClass();
	}

	@Override
	public void injectData(Map<String, Object> data) {
		if (TweakerLoader.tweakerManager != null) {
			TweakerLoader.tweakerManager.injectData(data);
		}
	}

	@Override
	public String getAccessTransformerClass() {
		return TweakerLoader.tweakerManager == null ? null : TweakerLoader.tweakerManager.getAccessTransformerClass();
	}

}