package net.mattbenson.gui.framework;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.mattbenson.modules.Module;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class MinecraftMenuImpl extends GuiScreen {
	protected Module feature;
	protected Menu menu;
	protected boolean ready = false;
	protected float guiScale = 1; 
	
	public MinecraftMenuImpl(Module feature, Menu menu) {
		this.feature = feature;
		this.menu = menu;
	}
	
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GlStateManager.pushMatrix();
		float value = guiScale / new ScaledResolution(mc).getScaleFactor();
		GlStateManager.scale(value, value, value);
		menu.onRender(Math.round((float)mouseX / value), Math.round((float)mouseY / value));
		GlStateManager.popMatrix();
		
		onMouseScroll(Mouse.getDWheel());
		
		if(feature != null) {
			if(feature.isEnabled() && feature.isBound()) {
				if(feature.getBindType() == BindType.HOLD) {
					if(!Keyboard.isKeyDown(feature.getKeyBind())) {
						mc.displayGuiScreen(null);
					}
				}
			}
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		menu.onMouseClick(mouseButton);
	}
	
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		menu.onMouseClickMove(clickedMouseButton);
    }
	
	public void onMouseScroll(int scroll) {
		menu.onScroll(scroll);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			if(menu.onMenuExit(keyCode)) {
				return;
			}
			
			mc.displayGuiScreen(null);
		} else {
			menu.onKeyDown(typedChar, keyCode);
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	public void onGuiClosed() {
		if(feature != null) {
			feature.setEnabled(false);
		}
		
		ready = false;
		super.onGuiClosed();
	}
	
	public Module getFeature() {
		return feature;
	}
}