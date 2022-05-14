package net.mattbenson.modules.types.render;

import java.awt.Color;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

//Code at net.minecraft.block.BlockRedstoneWire.java
public class ColoredRedstone extends Module {
	@ConfigValue.Color(name = "Redstone Color")
	public Color redstoneColor = Color.RED;
	
	public ColoredRedstone() {
		super("Colored Redstone", ModuleCategory.RENDER);
	}
	
	@Override
	public void onEnable() {
		Minecraft.getMinecraft().renderGlobal.loadRenderers();
	}
	
	@Override
	public void onDisable() {
		Minecraft.getMinecraft().renderGlobal.loadRenderers();
	}
	
}
