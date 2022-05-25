package falcun.net.util;

import falcun.net.Falcun;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;

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
			e.printStackTrace();
		}

		return null;
	}

	public static BufferedImage getResourceAsImage(String... strings) {
		File file = buildStringFile(strings);

		try {
			return ImageIO.read(file);
		} catch (Throwable err) {
			err.printStackTrace();
			return null;
		}
	}

	private static ResourceLocation getResourceFromPath(File file) {
		DynamicTexture texture;

		try {
			texture = new DynamicTexture(ImageIO.read(file));
		} catch (Throwable err) {
			err.printStackTrace();
			return null;
		}

		return Falcun.minecraft.getTextureManager().getDynamicTextureLocation(file.getName(), texture);
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

		File file = Paths.get(Falcun.minecraft.mcDataDir.getPath(), builder.toString().split("/")).toFile();
		return file;
	}
}
