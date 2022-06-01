package falcun.net.managers;

import com.google.gson.*;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunConfigValue;
import falcun.net.api.modules.config.FalcunFPSModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.throwables.FinalFalcunSettingException;
import falcun.net.modules.ModuleCategory;
import falcun.net.util.FalcunDevEnvironment;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.apache.commons.codec.binary.Base32;
import org.reflections.Reflections;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT).setExclusionStrategies(exclusionStrategy).create();

	static {
		Set<Class<?>> moduleStuff = reflections.getTypesAnnotatedWith(FalcunModuleInfo.class);
		moduleClasses = new Class<?>[moduleStuff.size()];
		int i = -1;
		for (Class<?> aClass : moduleStuff) {
			moduleClasses[++i] = aClass;
		}
	}


	private static boolean loadModule(Class<?> moduleClass) {
		try {
			FalcunModuleInfo falcunModuleInfo = moduleClass.getAnnotation(FalcunModuleInfo.class);
			Map<Class<?>, FalcunModule> map = moduleClass.isAnnotationPresent(FalcunFPSModule.class) ? fps : modules;
			File file = new File(dataFolder, FalcunDevEnvironment.isDevEnvironment ? falcunModuleInfo.fileName() + ".falcun" : encodeBase32(falcunModuleInfo.fileName()) + ".falcun");
			FalcunModule module;
			if (!file.exists()) {
				module = (FalcunModule) moduleClass.newInstance();
				file.createNewFile();
			} else {
				byte[] bytes = Files.readAllBytes(file.toPath());
				String jsonString = new String(bytes);
				if (isValidJson(jsonString)) {
					try {
						module = (FalcunModule) gson.fromJson(jsonString, moduleClass);
					} catch (Throwable err) {
						System.out.println("--------------------------------------------------------");
						System.out.println("valid json but could not initiate " + moduleClass);
						System.out.println("--------------------------------------------------------");
						module = (FalcunModule) moduleClass.newInstance();
						file.delete();
						err.printStackTrace();
					}
				} else {
					file.delete();
					module = (FalcunModule) moduleClass.newInstance();
				}
			}
			if (module == null) return false;
			if (FalcunDevEnvironment.isDevEnvironment) {
				System.out.println(module + " " + moduleClass);
			}
			saveModule(module);
			map.put(moduleClass, module);
			module.update();
			if (module.isEnabled()) {
				module.onEnable();
			}
			return true;
		} catch (Throwable err) {
			err.printStackTrace();
			return false;
		}
	}

	private static boolean isValidJson(String str) {
		if (str.length() < 10) return false;
		try {
			JsonObject jsonObject = gson.fromJson(str, JsonObject.class);
			return true;
		} catch (JsonSyntaxException ignored) {
			return false;
		} catch (Throwable err) {
			err.printStackTrace();
			return false;
		}
	}

	private static void deleteConfigFileOf(Class<?> modClass) {
		try {
			FalcunModuleInfo falcunModuleInfo = modClass.getAnnotation(FalcunModuleInfo.class);
			if (falcunModuleInfo == null) {
				for (int i = 0; i < 40; ++i) {
					System.out.println("INVALID FALCUN MODULE DUMBASS " + modClass);
				}
				return;
			}
			File file = new File(dataFolder, FalcunDevEnvironment.isDevEnvironment ? falcunModuleInfo.fileName() + ".falcun" : encodeBase32(falcunModuleInfo.fileName()) + ".falcun");
			if (file.exists()) {
				file.delete();
			}
		} catch (Throwable ignored) {

		}
	}

	public static void init() {
		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
		}
		for (Class<?> moduleClass : moduleClasses) {
			if (!loadModule(moduleClass)) {
				deleteConfigFileOf(moduleClass);
				System.out.println("--------");
				for (int i = 0; i < 5; ++i) {
					System.out.println("FAILED TO LOAD: " + moduleClass);
				}
				System.out.println("--------");
			}
		}
		modules.values().forEach(FalcunConfigManager::checkForFinal);
		fps.values().forEach(FalcunConfigManager::checkForFinal);
		System.out.println("All modules successfully have loaded.");
	}

	private static void checkForFinal(FalcunModule value) {
		try {
			for (Field field : value.getClass().getDeclaredFields()) {
				if (!field.isAnnotationPresent(FalcunSetting.class)) continue;
				if ((field.getModifiers() & Modifier.FINAL) == Modifier.FINAL) {
					FalcunSetting setting = field.getAnnotation(FalcunSetting.class);
					String s = field.getClass() + " " + field.getName() + " " + setting.value() + " IS FINAL";
					throw new FinalFalcunSettingException(s);
				}
			}
		} catch (Throwable err) {
			err.printStackTrace();
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
//			jsonString = encodeBase32(jsonString);
			writer.write(jsonString);
			writer.flush();
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
