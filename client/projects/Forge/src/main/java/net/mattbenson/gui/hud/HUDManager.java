package net.mattbenson.gui.hud;

import java.util.ArrayList;
import java.util.List;

import net.mattbenson.Falcun;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.render.RenderEvent;
import net.mattbenson.events.types.render.RenderType;
import net.mattbenson.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class HUDManager {
	private List<HUDElement> elements;
	
	public HUDManager() {
		elements = new ArrayList<>();
		
		for(Module mod : Falcun.getInstance().moduleManager.getModules()) {
			elements.addAll(mod.getHUDElements());
		}
	}
	
	@SubscribeEvent
	public void onRender(RenderEvent event) {
		if(event.getRenderType() != RenderType.INGAME_OVERLAY) {
			return;
		}
		
		GlStateManager.pushMatrix();
		float value = 2F / new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
		GlStateManager.scale(value, value, value);
		
		for(HUDElement element : elements) {
			if(!element.isVisible()) {
				continue;
			}
			
			if(element.getParent().isEnabled()) {
				GlStateManager.pushMatrix();
				GlStateManager.translate(element.getX(), element.getY(), 0);
				GlStateManager.scale(element.getScale(), element.getScale(), element.getScale());
				GlStateManager.translate(-element.getX(), -element.getY(), 0);
				
				element.onRender();
				
				GlStateManager.popMatrix();
			}
		}
		
		GlStateManager.popMatrix();
	}
	
	public List<HUDElement> getElements() {
		return elements;
	}
}
