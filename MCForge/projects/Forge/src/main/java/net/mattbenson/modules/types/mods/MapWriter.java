package net.mattbenson.modules.types.mods;

import java.awt.Color;
import java.nio.file.Paths;

import mapwriter.Mw;
import mapwriter.api.MwAPI;
import mapwriter.config.Config;
import mapwriter.config.ConfigurationHandler;
import mapwriter.forge.EventHandler;
import mapwriter.forge.MwKeyHandler;
import mapwriter.overlay.OverlayFaction;
import mapwriter.overlay.OverlayFactionGrid;
import mapwriter.overlay.OverlayGrid;
import mapwriter.overlay.OverlayGroup;
import mapwriter.overlay.OverlayHoverGrid;
import net.mattbenson.Falcun;
import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.world.OnTickEvent;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.utils.AssetUtils;
import net.mattbenson.utils.ForgeUtils;
import net.minecraft.client.Minecraft;

public class MapWriter extends Module {
	@ConfigValue.List(name = "Map Modes", values = {"Circle","Rectangle"})
	public String mode = "Circle";
	
	@ConfigValue.List(name = "Coords Modes", values = {"Small","Large", "Disabled"})
	public String coordsMode = "Small";
	
	@ConfigValue.Boolean(name = "Show border")
	public boolean showBorder = false;
	
	@ConfigValue.Boolean(name = "Show Grid")
	public boolean showGrid = false;
	
	@ConfigValue.Color(name = "Grid color")
	public static Color gridColor = Color.GRAY;
	
	@ConfigValue.Double(name = "Grid width", min = 0.1D, max = 1D)
	public static double gridWidth = 0.25D;
	
	@ConfigValue.Boolean(name = "Show Factions Overlay")
	public boolean showFactionsOverlay = false;
	
	@ConfigValue.Boolean(name = "Show Group Overlay")
	public boolean showGroupOverlay = true;
	
	@ConfigValue.Integer(name = "Render Radius", min = 4, max = 10)
	public int renderRadius = 6;
	
	@ConfigValue.Integer(name = "Ticks between Updates", min = 20, max = 500)
	public int ticksBetweenUpdates = 200;
	
	public HUDElement hud;
	
	public MapWriter() {
		super("MapWriter", ModuleCategory.MODS);
		
		hud = new HUDElement("map", 120, 140) {
			@Override
			public void onRender() {
				
			}
		};
		
		addHUD(hud);
		
		Falcun.getInstance().languageUtils.registerLanguageResource("mapwriter", "en_US");
		
		new ConfigurationHandler().init(ForgeUtils.getPreferredPath(this));
		
		MwAPI.registerDataProvider("Grid", new OverlayGrid());
		MwAPI.registerDataProvider("Faction", new OverlayFaction());
		MwAPI.registerDataProvider("Faction/Grid", new OverlayFactionGrid());
		MwAPI.registerDataProvider("Hover", new OverlayHoverGrid());
		MwAPI.registerDataProvider("Groups", new OverlayGroup());
	}
	
	@SubscribeEvent
	public void onTick(OnTickEvent event) {
		if ((Mw.getInstance().ready) && (Minecraft.getMinecraft().thePlayer == null)) {
			Mw.getInstance().close();
		}
		
		if(mc.thePlayer != null && mc.theWorld != null && Mw.getInstance().ready) {

			if(Mw.getInstance().miniMap != null && Mw.getInstance().miniMap.smallMap != null) {
				
				Config.smallMap.borderMode = showBorder;
				
				if(mode.equalsIgnoreCase("Circle")) {
					Config.smallMap.circular = true;
				} else {
					Config.smallMap.circular = false;
				}

				if(coordsMode.equalsIgnoreCase("Small")) {
					Config.smallMap.coordsMode = "mw.config.map.coordsMode.small";
				} else if(coordsMode.equalsIgnoreCase("Large")) {
					Config.smallMap.coordsMode = "mw.config.map.coordsMode.large";
				} else {
					Config.smallMap.coordsMode = "mw.config.map.coordsMode.disabled";
				}
			}
		}
	}
}
