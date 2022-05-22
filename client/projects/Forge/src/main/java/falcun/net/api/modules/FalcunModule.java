package falcun.net.api.modules;

import falcun.net.Falcun;
import falcun.net.api.modules.config.FalcunField;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.api.modules.inheritance.FalcunEventBusModule;
import falcun.net.modules.ModuleCategory;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import java.lang.reflect.Field;
import java.util.*;

public abstract class FalcunModule {

	protected static transient final Minecraft minecraft = Minecraft.getMinecraft();
	protected static transient final Falcun falcun = Falcun.instance;
	private final transient FalcunModuleInfo info;
	public transient ModuleCategory category;

	private volatile boolean enabled = false;
	public transient boolean serverDisabled = false;

	public final transient List<Pair<FalcunSetting, FalcunField<?>>> configData;

	public FalcunModule() {
		configData = new LinkedList<>();
		this.info = this.getClass().getAnnotation(FalcunModuleInfo.class);
		if (this.info == null) return;
		this.category = this.info.category();
		for (final Field field : this.getClass().getDeclaredFields()) {
			if (!field.isAnnotationPresent(FalcunSetting.class)) {
				continue;
			}
			FalcunSetting falcunSetting = field.getAnnotation(FalcunSetting.class);
			if (falcunSetting == null) continue;
			FalcunField<?> falcunField = new FalcunField<>(field, this, falcunSetting);
			this.configData.add(Pair.of(falcunSetting, falcunField));
		}
	}


	public void toggle() {
		this.setEnabled(!this.isEnabled());
	}

	public boolean isEnabled() {
		return enabled && !serverDisabled;
	}

	public boolean setEnabled(boolean enabled) {
		this.enabled = enabled;
		return this.enabled;
	}

	private final transient boolean eventBusMod = this instanceof FalcunEventBusModule;

	public void onEnable() {
		if (eventBusMod) {
			MinecraftForge.EVENT_BUS.register(this);
		}
	}

	public void onDisable() {
		if (eventBusMod) {
			MinecraftForge.EVENT_BUS.unregister(this);
		}
	}

	public void update() {
	}

	public final String getName() {
		return this.info.name();
	}

	public final String getDescription() {
		return this.info.description();
	}

	public final String getVersion() {
		return this.info.version();
	}

	public final String getFileName() {
		return this.info.fileName();
	}

	public void save() {

	}



}
