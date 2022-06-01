package net.mattbenson.cosmetics.cape;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class DownloadCustomCape
{
	private static final Pattern PATTERN_USERNAME = Pattern.compile("[a-zA-Z0-9_]+");

	public static void downloadCape(final PlayerHandler player)
	{
		String s = player.getPlayerUUID();

		if (s != null && !s.isEmpty() && player.getCapeLocation() == null && !player.getHasInfo())
		{
			String s1 = "https://www.falcun.net/old/capes/custom/" + s + ".png";
            final ResourceLocation resourcelocation = new ResourceLocation("capeof/" + s);
            TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
            ITextureObject itextureobject = texturemanager.getTexture(resourcelocation);

            if (itextureobject != null && itextureobject instanceof ThreadDownloadImageData)
            {
                ThreadDownloadImageData threaddownloadimagedata = (ThreadDownloadImageData)itextureobject;

                if (threaddownloadimagedata.imageFound != null)
                {
                    if (threaddownloadimagedata.imageFound.booleanValue())
                    {
                    	player.setCapeLocation(resourcelocation);
                    }
                }
            }

            CapeImageBuffer capeImageBuffer = new CapeImageBuffer(player, resourcelocation);
            ThreadDownloadImageData threaddownloadimagedata1 = new ThreadDownloadImageData((File)null, s1, (ResourceLocation)null, capeImageBuffer);
            threaddownloadimagedata1.pipeline = true;
            texturemanager.loadTexture(resourcelocation, threaddownloadimagedata1);
        	player.setHasInfo(true);
		}
	}

	public static BufferedImage parseCape(BufferedImage p_parseCape_0_)
	{
		int i = 64;
		int j = 32;
		int k = p_parseCape_0_.getWidth();

		for (int l = p_parseCape_0_.getHeight(); i < k || j < l; j *= 2)
		{
			i *= 2;
		}

		BufferedImage bufferedimage = new BufferedImage(i, j, 2);
		Graphics graphics = bufferedimage.getGraphics();
		graphics.drawImage(p_parseCape_0_, 0, 0, (ImageObserver)null);
		graphics.dispose();
		return bufferedimage;
	}
}
