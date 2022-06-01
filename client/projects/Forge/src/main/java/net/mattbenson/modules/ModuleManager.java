package net.mattbenson.modules;

import java.util.ArrayList;
import java.util.List;

import net.mattbenson.Falcun;
import net.mattbenson.modules.types.factions.Breadcrumbsmod;
import net.mattbenson.modules.types.factions.ButtonSpammer;
import net.mattbenson.modules.types.factions.CaneHelper;
import net.mattbenson.modules.types.factions.DispenserChecker;
import net.mattbenson.modules.types.factions.Patchcrumbs;
import net.mattbenson.modules.types.factions.Patchcrumbs2;
import net.mattbenson.modules.types.factions.PatchcrumbsOld;
import net.mattbenson.modules.types.factions.TickCounter;
import net.mattbenson.modules.types.factions.WorldEditCUI;
import net.mattbenson.modules.types.fpssettings.FPSSettings;
import net.mattbenson.modules.types.general.Settings;
import net.mattbenson.modules.types.general.ToggleSneak;
import net.mattbenson.modules.types.hud.Image1;
import net.mattbenson.modules.types.hud.Image2;
import net.mattbenson.modules.types.hud.Image3;
import net.mattbenson.modules.types.hud.Image4;
import net.mattbenson.modules.types.hud.Image5;
import net.mattbenson.modules.types.hud.Text1;
import net.mattbenson.modules.types.hud.Text2;
import net.mattbenson.modules.types.hud.Text3;
import net.mattbenson.modules.types.hud.Text4;
import net.mattbenson.modules.types.hud.Text5;
import net.mattbenson.modules.types.mods.ArmorStatus;
import net.mattbenson.modules.types.mods.Coordinates;
import net.mattbenson.modules.types.mods.DirectionHUD;
import net.mattbenson.modules.types.mods.FancyCompass;
import net.mattbenson.modules.types.mods.GroupLocation;
import net.mattbenson.modules.types.mods.GroupPatchcrumb;
import net.mattbenson.modules.types.mods.GroupPingLocation;
import net.mattbenson.modules.types.mods.Gui;
import net.mattbenson.modules.types.mods.Hitbox;
import net.mattbenson.modules.types.mods.Keystrokes;
import net.mattbenson.modules.types.mods.MapWriter;
import net.mattbenson.modules.types.mods.MotionBlur;
import net.mattbenson.modules.types.mods.MouseHUD;
import net.mattbenson.modules.types.mods.OldAnimations;
import net.mattbenson.modules.types.mods.PotionStatus;
import net.mattbenson.modules.types.mods.Schematica;
import net.mattbenson.modules.types.mods.StopWatch;
import net.mattbenson.modules.types.other.BlockCoordinates;
import net.mattbenson.modules.types.other.BossBar;
import net.mattbenson.modules.types.other.Chat;
import net.mattbenson.modules.types.other.Clock;
import net.mattbenson.modules.types.other.ConsoleClient;
import net.mattbenson.modules.types.other.HealthVignette;
import net.mattbenson.modules.types.other.Hits18;
import net.mattbenson.modules.types.other.InventoryAllignment;
import net.mattbenson.modules.types.other.MouseBindFix;
import net.mattbenson.modules.types.other.MouseDelay;
import net.mattbenson.modules.types.other.PerspectiveMod;
import net.mattbenson.modules.types.other.PortalFix;
import net.mattbenson.modules.types.other.ScreenshotUploader;
import net.mattbenson.modules.types.other.SwordFOV;
import net.mattbenson.modules.types.other.VoidFlicker;
import net.mattbenson.modules.types.other.Zoom;
import net.mattbenson.modules.types.render.BlockOverlay;
import net.mattbenson.modules.types.render.CPS;
import net.mattbenson.modules.types.render.ChunkBorders;
import net.mattbenson.modules.types.render.ColoredRedstone;
import net.mattbenson.modules.types.render.Combo;
import net.mattbenson.modules.types.render.Cooldowns;
import net.mattbenson.modules.types.render.Crosshair;
import net.mattbenson.modules.types.render.EntityHUD;
import net.mattbenson.modules.types.render.FPS;
import net.mattbenson.modules.types.render.Gamma;
import net.mattbenson.modules.types.render.MemoryUsage;
import net.mattbenson.modules.types.render.Ping;
import net.mattbenson.modules.types.render.PotCounter;
import net.mattbenson.modules.types.render.ReachDisplay;
import net.mattbenson.modules.types.render.Saturation;
import net.mattbenson.modules.types.render.ScoreboardHUD;
import net.mattbenson.modules.types.render.ServerAddress;
import net.mattbenson.modules.types.render.Speed;
import net.mattbenson.modules.types.render.TPS;

public class ModuleManager {
	public List<Module> modules;
	public ModuleManager() {
		modules = new ArrayList<>();

		// Factions
		modules.add(new Breadcrumbsmod());
		modules.add(new ButtonSpammer());
		modules.add(new CaneHelper());
		modules.add(new DispenserChecker());
		modules.add(new Patchcrumbs());
		modules.add(new PatchcrumbsOld());
		modules.add(new Patchcrumbs2());
		modules.add(new TickCounter());
		modules.add(new WorldEditCUI());

		// FPS Settings
		modules.add(new FPSSettings());
		
		// General
		modules.add(new Settings());
		modules.add(new ToggleSneak());
		
		// Mods
		modules.add(new ArmorStatus());
		modules.add(new Coordinates());
		modules.add(new DirectionHUD());
		modules.add(new FancyCompass());
		//modules.add(new Friends());
		modules.add(new Gui());
		modules.add(new Hitbox());
		modules.add(new Keystrokes());
		modules.add(new MapWriter());
		modules.add(new MotionBlur());
		modules.add(new MouseHUD());
		modules.add(new OldAnimations());
		modules.add(new PotionStatus());
		modules.add(new Schematica());
		modules.add(new StopWatch());
		
		// Other
		modules.add(new BlockCoordinates());
		modules.add(new BossBar());
		modules.add(new Clock());
		modules.add(new ConsoleClient());
		modules.add(new HealthVignette());
		modules.add(new Hits18());
		modules.add(new InventoryAllignment());
		modules.add(new MouseBindFix());
		modules.add(new MouseDelay());
		modules.add(new PerspectiveMod());
		modules.add(new PortalFix());
		modules.add(new ScreenshotUploader());
		modules.add(new SwordFOV());
		modules.add(new Chat());
		//modules.add(new VoiceChat());
		modules.add(new VoidFlicker());
		modules.add(new Zoom());
		
		// Render
		modules.add(new BlockOverlay());
		modules.add(new ChunkBorders());
		modules.add(new ColoredRedstone());
		modules.add(new Combo());
		modules.add(new Cooldowns());
		modules.add(new CPS());
		modules.add(new Crosshair());
		modules.add(new EntityHUD());
		modules.add(new FPS());
		modules.add(new Gamma());
		modules.add(new MemoryUsage());
		modules.add(new Ping());
		modules.add(new PotCounter());
		modules.add(new ReachDisplay());
		modules.add(new Saturation());
		modules.add(new ScoreboardHUD());
		modules.add(new ServerAddress());
		modules.add(new Speed());
		modules.add(new TPS());
		modules.add(new Image1());
		modules.add(new Image2());
		modules.add(new Image3());
		modules.add(new Image4());
		modules.add(new Image5());
		modules.add(new Text1());
		modules.add(new Text2());
		modules.add(new Text3());
		modules.add(new Text4());
		modules.add(new Text5());
		
		//Groups
		modules.add(new GroupPatchcrumb());
		modules.add(new GroupLocation());
		modules.add(new GroupPingLocation());
		
//		orbitModules.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
		modules.sort((a,b)-> a.getName().compareToIgnoreCase(b.getName()));
	}
	
	public List<Module> getModules() {
		return modules;
	}
	
	public <T extends Module> T getModule(Class<T> clazz) {
		for(Module module : modules) {
			if(module.getClass() == clazz) {
				return (T) module;
			}
		}
		
		Falcun.getInstance().log.warn("Tried accessing non existing module(" + clazz + ") (class).");
		return null;
	}
	
	public <T extends Module> T getModule(String name) {
		for(Module module : modules) {
			if(module.getName().equalsIgnoreCase(name)) {
				return (T) module;
			}
		}
		
		Falcun.getInstance().log.warn("Tried accessing non existing module(" + name + ") (name).");
		return null;
	}
}
