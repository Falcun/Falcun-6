package net.mattbenson.modules.types.other;

import org.lwjgl.input.Mouse;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.world.OnTickEvent;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.minecraft.src.Config;

public class Zoom extends Module {
	@ConfigValue.Boolean(name = "Smooth Zoom")
	public boolean smoothZoom = false;
	
	@ConfigValue.Boolean(name = "Scroll Zoom")
	public boolean scrollZoom = false;
	
	@ConfigValue.Boolean(name = "Inverted")
	private boolean inverted = false;
	
	@ConfigValue.Float(name = "Zoom Factor", min = 0.1F, max = 10F)
	private float zoomFactor = 1;
	
	public float scrollAmount = 0;
	
	public Zoom() {
		super("Zoom", ModuleCategory.OTHER);
	}
	
	@SubscribeEvent
	public void onTick(OnTickEvent event) {
		if (Config.zoomMode) {
			if(scrollZoom) {
				while (Mouse.next()) {
					if (Mouse.getEventDWheel() != 0.0) {
						float scroll = Mouse.getEventDWheel();
						if (this.inverted) {
							if (scroll > 0.0) {
								scrollAmount += (1 * zoomFactor);
							} else {
								scrollAmount -= (1 * zoomFactor);
							}
						} else {
							if (scroll > 0.0) {
								scrollAmount -= (1 * zoomFactor);
							} else {
								scrollAmount += (1 * zoomFactor);
							}
						}
					}
				}
			} else {
				scrollAmount = 10 - (zoomFactor * 1);
			}
		}
	}

}
