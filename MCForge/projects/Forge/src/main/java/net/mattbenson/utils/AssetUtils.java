package net.mattbenson.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import net.mattbenson.Falcun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class AssetUtils {
	public AssetUtils() {}
	
	public static ResourceLocation getResourceFromFile(File file) {
		return getResourceFromPath(file);
	}
	
	public static ResourceLocation getResource(String... strings) {
		File file = buildStringFile(strings);
		return getResourceFromPath(file);
	}
	
	public static File getResourceAsFile(String... strings) {
		return buildStringFile(strings);
	}
	
	public static InputStream getResourceAsStream(String... strings) {
		File file = buildStringFile(strings);
		
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			Falcun.getInstance().log.error("Failed to load stream outside namespace (" + file.getAbsolutePath() + ").", e);
		}
		
		return null;
	}
	
	public static BufferedImage getResourceAsImage(String... strings) {
		File file = buildStringFile(strings);
		
		try {
			return ImageIO.read(file);
		} catch (IOException e) {
			Falcun.getInstance().log.error("Failed to load raw image resource outside namespace (" + file.getAbsolutePath() + ").", e);
			return null;
		}
	}
	
	private static ResourceLocation getResourceFromPath(File file) {
		DynamicTexture texture;
		
		try {
			texture = new DynamicTexture(ImageIO.read(file));
		} catch (IOException e) {
			Falcun.getInstance().log.error("Failed to load resource outside namespace (" + file.getAbsolutePath() + ").", e);
			return null;
		}
		
        return Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(file.getName(), texture);
	}
	
	private static File buildStringFile(String... strings) {
		StringBuilder builder = new StringBuilder("falcunassets");
		
		for(int i = 0; i < strings.length; i++) {
			String string = strings[i];
			
			if(i == 0) {
				string = string.replace(':', '/');
			}
			
			builder.append("/" + string);
		}
		
		File file = Paths.get(Minecraft.getMinecraft().mcDataDir.getPath(), builder.toString().split("/")).toFile();
		return file;
	}
}
