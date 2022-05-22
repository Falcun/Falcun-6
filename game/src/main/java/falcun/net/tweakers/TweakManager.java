package falcun.net.tweakers;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.Credits;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.tweakers.BasicHook;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.tweakers.BasicHookModule;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.tweakers.CorePatchModule;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.tweakers.TweakerAPI;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Credits("https://github.com/boredhuman/Dancore/blob/master/src/main/java/dev/boredhuman/tweaker/TweakManager.java")
public class TweakManager extends TweakerAPI implements IFMLLoadingPlugin, IClassTransformer {

	static {
		String string = "CREDITS TO @boredhuman on github for the hook api";
	}

	public static boolean deObfEnv;
	Map<String, CorePatchModule> patches = new HashMap<>();
	private static boolean initialized = false;

	static {
		try {
			deObfEnv = Launch.classLoader.getClassBytes("net.minecraft.world.World") == null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			LogManager.getLogger().info("Deobfuscated env: " + TweakManager.deObfEnv);
		} catch (Throwable err) {
			err.printStackTrace();
		}
	}

	public TweakManager() {
		if (!TweakManager.initialized) {
			try {
				Field field = TweakerAPI.class.getDeclaredField("API");
				field.setAccessible(true);
				field.set(null, this);
			} catch (Throwable err) {
				err.printStackTrace();
			}
			LogManager.getLogger().info("Loading tweaker sub modules");
			TweakManager.initialized = true;
		}

		final Class<?>[] tweakers = new Class<?>[]{
		};
		for (final Class<?> module : tweakers) {
			try {
				this.registerTweaker((CorePatchModule) module.newInstance());
			} catch (Throwable err) {
				err.printStackTrace();
			}
		}
		if (optifinePresent()) {
		}
		if (!dancoreRedstonePresent()) {
		}
		if (!_5siWaterDancorePresent()) {
		}
	}

	public static boolean _5siWaterDancorePresent() {
		if (!dancorePresent()) return false;
		try {
			Class.forName("com.github._5si.dancoremods.water.WaterColorOptifinePatch");
			return true;
		} catch (Throwable err) {
			return false;
		}
	}

	public static boolean dancorePresent() {
		return TweakerLoader.dancore;
	}

	public static boolean dancoreRedstonePresent() {
		if (!dancorePresent()) return false;
		try {
			Class.forName("dev.boredhuman.redstonecolorizer.RedstonePatch");
			return true;
		} catch (Throwable err) {
			return false;
		}
	}

	public static boolean optifinePresent() {
		try {
			Class.forName("net.optifine.Log");
			return true;
		} catch (Throwable err) {
			return false;
		}
	}

	@Override
	public void registerTweaker(CorePatchModule tweaker) {
		System.out.println("Registering Tweaker:" + tweaker.getClass().getName());
		if (tweaker instanceof BasicHookModule) {
			BasicHook.register((BasicHookModule) tweaker);
		} else {
			this.patches.put(tweaker.getTargetClass(), tweaker);
		}

	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{TweakManager.class.getName(), BasicHook.class.getName(), };
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {

	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		CorePatchModule patch = this.patches.get(transformedName);
		if (patch != null) {
			try {
				ClassReader cr = new ClassReader(basicClass);
				ClassNode node = new ClassNode();
				cr.accept(node, 0);
				patch.transformClass(node);
				ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
				node.accept(cw);
				return cw.toByteArray();
			} catch (Throwable err) {
				err.printStackTrace();
			}
		}
		return basicClass;
	}
}
