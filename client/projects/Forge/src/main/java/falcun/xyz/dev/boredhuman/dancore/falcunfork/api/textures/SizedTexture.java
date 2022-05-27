package falcun.xyz.dev.boredhuman.dancore.falcunfork.api.textures;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public abstract class SizedTexture extends AbstractTexture {


	public abstract void process();

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract void loadTexture();

	public abstract String getResourceLocation();

	@Override
	public void loadTexture(IResourceManager resourceManager) throws IOException {

	}
}