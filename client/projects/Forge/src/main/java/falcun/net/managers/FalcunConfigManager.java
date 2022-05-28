package falcun.net.managers;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunConfigValue;
import falcun.net.api.modules.config.FalcunFPSModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.modules.ModuleCategory;
import falcun.net.util.FalcunDevEnvironment;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.apache.commons.codec.binary.Base32;
import org.reflections.Reflections;

import java.io.*;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public final class FalcunConfigManager {

	private static final ExclusionStrategy exclusionStrategy = new ExclusionStrategy() {
		@Override
		public boolean shouldSkipField(FieldAttributes f) {
			if (FalcunModule.class.isAssignableFrom(f.getDeclaringClass())) {
				return ((f.getAnnotation(FalcunSetting.class) == null && f.getAnnotation(FalcunConfigValue.class) == null));
			}
			return false;
		}

		@Override
		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}
	};
	private static final File dataFolder = FalcunDevEnvironment.isDevEnvironment ? new File("config" + File.separatorChar + "Falcun") : new File(System.getProperty("user.home") + File.separatorChar + "Falcun" + File.separatorChar + "configs");

	private static final Base32 base32 = new Base32();

	public static final Map<Class<?>, FalcunModule> modules = new Object2ObjectOpenHashMap<>();
	public static final Map<Class<?>, FalcunModule> fps = new Object2ObjectOpenHashMap<>();
	private static final Reflections reflections = new Reflections();

	private static final Class<?>[] moduleClasses;

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().setExclusionStrategies(exclusionStrategy).create();

	static {
		Set<Class<?>> moduleStuff = reflections.getTypesAnnotatedWith(FalcunModuleInfo.class);
		moduleClasses = new Class<?>[moduleStuff.size()];
		int i = -1;
		for (Class<?> aClass : moduleStuff) {
			moduleClasses[++i] = aClass;
		}
	}

	public static void init() {
		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
		}
		for (Class<?> moduleClass : moduleClasses) {
			try {
				FalcunModuleInfo falcunModuleInfo = moduleClass.getAnnotation(FalcunModuleInfo.class);
				Map<Class<?>, FalcunModule> map = moduleClass.isAnnotationPresent(FalcunFPSModule.class) ? fps : modules;
				File file = new File(dataFolder, FalcunDevEnvironment.isDevEnvironment ? falcunModuleInfo.fileName() + ".falcun" : encodeBase32(falcunModuleInfo.fileName()) + ".falcun");
				FalcunModule falcunModule;
				block1:
				{
					if (!file.exists()) {
						file.createNewFile();
						falcunModule = (FalcunModule) moduleClass.newInstance();
					} else {
						byte[] bytes = Files.readAllBytes(file.toPath());
						String jsonString = new String(bytes);
//					if (!FalcunDevEnvironment.isDevEnvironment) {
//						jsonString = changeBytes(jsonString);
//					}
						try {
							falcunModule = (FalcunModule) gson.fromJson(jsonString, moduleClass);
						} catch (Throwable err) {
							err.printStackTrace();
							falcunModule = (FalcunModule) moduleClass.newInstance();
							break block1;
						}
						if (falcunModule.isEnabled()) falcunModule.onEnable();
					}
				}
				if (FalcunDevEnvironment.isDevEnvironment) {
					System.out.println(falcunModule + " " + moduleClass);
				}
				saveModule(falcunModule);
				map.put(moduleClass, falcunModule);
			} catch (Throwable err) {
				err.printStackTrace();
			}
		}
	}


	private static String changeBytes(String current) {
		byte[] bytes = current.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			if (b > -120 && b < 120) {
				bytes[i] = (byte) (-b);
			}
		}
		return new String(bytes);
	}


	public static synchronized void saveModule(FalcunModule falcunModule) {
		FalcunModuleInfo falcunModuleInfo = falcunModule.getClass().getAnnotation(FalcunModuleInfo.class);
		File file = new File(dataFolder, FalcunDevEnvironment.isDevEnvironment ? falcunModuleInfo.fileName() + ".falcun" : encodeBase32(falcunModuleInfo.fileName()) + ".falcun");
		try (FileWriter writer = new FileWriter(file)) {
			if (!file.exists()) {
				file.createNewFile();
			}
			String jsonString = gson.toJson(falcunModule);
//			if (!FalcunDevEnvironment.isDevEnvironment) {
//			jsonString = changeBytes(jsonString);
//			}
			writer.write(jsonString);
		} catch (Throwable err) {
			err.printStackTrace();
		}
	}

	public static synchronized void saveAllModules() {
		modules.values().forEach(FalcunConfigManager::saveModule);
		fps.values().forEach(FalcunConfigManager::saveModule);
	}

	private static String encodeBase32(String s) {
		return new String(base32.encode(s.getBytes()));
	}

	private static String decodeBase32(String s) {
		return new String(base32.decode(s.getBytes()));
	}

	public static Collection<FalcunModule> getModulesFiltered(Collection<FalcunModule> modules, String filter) {
		return modules.stream().filter(a -> a.getName().toLowerCase().contains(filter.toLowerCase())).collect(java.util.stream.Collectors.toList());
	}

	public static Collection<FalcunModule> getModulesFiltered(ModuleCategory category, String filter) {
		return getModulesFiltered(getModulesSorted(category), filter);
	}

	public static Collection<FalcunModule> getModules(ModuleCategory category) {
		if (category == null || category == ModuleCategory.ALL) return getModules();
		return modules.values().stream().filter(a -> a.category == category).collect(java.util.stream.Collectors.toList());
	}

	public static Collection<FalcunModule> getModules() {
		return modules.values();
	}

	public static Collection<FalcunModule> getModulesSorted() {
		return getModules().stream().sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName())).collect(java.util.stream.Collectors.toList());
	}

	public static Collection<FalcunModule> getModulesSorted(ModuleCategory category) {
		return getModules(category).stream().sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName())).collect(java.util.stream.Collectors.toList());
	}

}
