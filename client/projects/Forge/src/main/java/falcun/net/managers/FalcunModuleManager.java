package falcun.net.managers;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.modules.ModuleCategory;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.reflections.Reflections;

import java.io.*;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public final class FalcunModuleManager {

	public static final FalcunModuleManager instance = new FalcunModuleManager();

	private static final Map<Class<?>, FalcunModule> falcunModules = new Object2ObjectOpenHashMap<>();
	private final File dataFolder = new File("config" + File.separatorChar + "Falcun");
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private final Set<Class<?>> classes;

	public static Collection<FalcunModule> getModulesFiltered(Collection<FalcunModule> modules, String filter) {
		return modules.stream().filter(a -> a.getName().toLowerCase().contains(filter.toLowerCase())).collect(java.util.stream.Collectors.toList());
	}

	public static Collection<FalcunModule> getModulesFiltered(ModuleCategory category, String filter) {
		return getModulesFiltered(getModulesSorted(category), filter);
	}

	public static Collection<FalcunModule> getModules(ModuleCategory category) {
		if (category == null || category == ModuleCategory.ALL) return getModules();
		return falcunModules.values().stream().filter(a -> a.category == category).collect(java.util.stream.Collectors.toList());
	}

	public static Collection<FalcunModule> getModules() {
		return falcunModules.values();
	}

	public static Collection<FalcunModule> getModulesSorted() {
		return getModules().stream().sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName())).collect(java.util.stream.Collectors.toList());
	}

	public static Collection<FalcunModule> getModulesSorted(ModuleCategory category) {
		return getModules(category).stream().sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName())).collect(java.util.stream.Collectors.toList());
	}

	public FalcunModuleManager() {
		Reflections reflection = new Reflections();
		this.classes = reflection.getTypesAnnotatedWith(FalcunModuleInfo.class);
	}

	public static void init(){
		instance.loadAll();
		instance.saveAllModules();
	}

	private void loadAll() {
		if (!this.dataFolder.exists()) {
			loadDefaults();
			return;
		}
		this.classes.forEach(aClass -> {
			try {
				FalcunModuleInfo falcunModuleInfo = aClass.getAnnotation(FalcunModuleInfo.class);
				File file = new File(this.dataFolder, falcunModuleInfo.fileName() + ".json");
				if (!file.exists()) {
					FalcunModule falcunModule = (FalcunModule) aClass.newInstance();
					if (falcunModule.isEnabled()) falcunModule.onEnable();
					falcunModules.put(falcunModule.getClass(), falcunModule);
					this.saveModule(falcunModule);
					return;
				}

				try (Reader reader = new FileReader(file)) {
					FalcunModule falcunModule = (FalcunModule) gson.fromJson(reader, aClass);

					System.out.println(falcunModule);

					if (falcunModule.isEnabled()) falcunModule.onEnable();
					falcunModule.update();
					falcunModules.put(falcunModule.getClass(), falcunModule);
				} catch (Exception ex) {
					ex.printStackTrace();
				}


			} catch (Exception e) {
				e.printStackTrace();
			}
		});
//		falcunModules.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
	}

	private void loadDefaults() {
		this.dataFolder.mkdir();

		this.classes.forEach(aClass -> {
			try {
				FalcunModule falcunModule = (FalcunModule) aClass.newInstance();
				if (falcunModule.isEnabled()) falcunModule.onEnable();
				falcunModules.put(falcunModule.getClass(), falcunModule);
				this.saveModule(falcunModule);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

	}

	public void saveModule(FalcunModule falcunModule) {
		FalcunModuleInfo falcunModuleInfo = falcunModule.getClass().getAnnotation(FalcunModuleInfo.class);
		File file = new File(this.dataFolder, falcunModuleInfo.fileName() + ".json");
		try (Writer writer = new FileWriter(file)) {
			this.gson.toJson(falcunModule, writer);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	public synchronized void saveAllModules() {
		falcunModules.values().forEach(this::saveModule);
	}

	public JsonObject getCombinedJson() {
		JsonObject jsonObject = new JsonObject();

		for (FalcunModule falcunModule : falcunModules.values()) {
			JsonElement jsonElement = this.gson.toJsonTree(falcunModule);

			jsonObject.add(falcunModule.getClass().getSimpleName(), jsonElement);
		}

		return jsonObject;
	}


	public void useProfile(JsonObject jsonObject) { // TODO: MAKE DISCORD RP MODULE
		this.unloadAll();
		this.classes.forEach(aClass -> {
			try {
//				if (!jsonObject.has(aClass.getSimpleName()) || aClass.equals(DiscordRPModule.class)) {
				if (!jsonObject.has(aClass.getSimpleName())) { // REMOVE AND UNCOMMENT LINE ABOVE WHEN DISCORD MODULE EXISTS!
					FalcunModule falcunModule = (FalcunModule) aClass.newInstance();
					if (falcunModule.isEnabled()) falcunModule.onEnable();
					falcunModules.put(falcunModule.getClass(), falcunModule);
					this.saveModule(falcunModule);
					return;
				}

				FalcunModule falcunModule = (FalcunModule) gson.fromJson(jsonObject.get(aClass.getSimpleName()), aClass);
				if (falcunModule.isEnabled()) falcunModule.onEnable();

				falcunModules.put(falcunModule.getClass(), falcunModule);
			} catch (Exception ex) {
				System.out.println("Couldn't load " + aClass.getSimpleName() + " from profile.");
			}
		});

//		OrbitClient.getInstance().getGuiHandler().getOrbitMainGUI().loadClasses(falcunModules);
//		OrbitClient.getInstance().sendMessage("&7(&9OrbitClient&7) Successfully loaded the profile.");
	}

	private void unloadAll() {
		falcunModules.values().forEach(FalcunModule::onDisable);
		falcunModules.clear();
	}

}
