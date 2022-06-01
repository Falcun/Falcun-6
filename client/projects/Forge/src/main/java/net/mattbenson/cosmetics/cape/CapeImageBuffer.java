package net.mattbenson.cosmetics.cape;

import java.awt.image.BufferedImage;

import net.mattbenson.Falcun;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.util.ResourceLocation;

public class CapeImageBuffer implements IImageBuffer
{
    private PlayerHandler player;
    private ResourceLocation resourceLocation;

    public CapeImageBuffer(PlayerHandler player, ResourceLocation resourceLocation)
    {
        this.player = player;
        this.resourceLocation = resourceLocation;
    }

    @Override
    public BufferedImage parseUserSkin(BufferedImage image)
    {
        return DownloadCape.parseCape(image);
    }

    @Override
    public void skinAvailable()
    {
        if (this.player != null)
        {
        	player.setCapeLocation(resourceLocation);
        }

        this.cleanup();
    }

    public void cleanup()
    {
        this.player = null;
    }
}
