package net.mattbenson.modules.types.fpssettings.cruches;

import java.io.IOException;

import javax.imageio.ImageIO;

import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.util.ResourceLocation;

public class ThreadDownloadImageDataHook extends SimpleTexture {
	public ThreadDownloadImageDataHook(ResourceLocation textureResourceLocation) {
		super(textureResourceLocation);
	}
	
	public static void getImprovedCacheLoading(ThreadDownloadImageData data) {
		new Thread(() -> {
			if (data.imageThread == null) {
				if (data.cacheFile != null && data.cacheFile.isFile()) {
					ThreadDownloadImageData.logger.debug("Loading http texture from local cache ({})",
							new Object[] { data.cacheFile });
					try {
						data.bufferedImage = ImageIO.read(data.cacheFile);
						if (data.imageBuffer != null) {
							data.setBufferedImage(data.imageBuffer.parseUserSkin(data.bufferedImage));
						}
					} catch (IOException ioexception) {
						ThreadDownloadImageData.logger.error("Couldn't load skin " + data.cacheFile, ioexception);
						data.loadTextureFromServer();
					}
				} else {
					data.loadTextureFromServer();
				}
			}			
		}).start();
	}
}
