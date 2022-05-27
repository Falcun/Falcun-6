package falcun.xyz.dev.boredhuman.dancore.falcunfork.api.textures;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class SingleSizedTexture extends SizedTexture {

	String resourceLocation;
	public int width, height;
	BufferedImage image;
	private boolean processed;

	public SingleSizedTexture(String resourceLocation) {
		this.resourceLocation = resourceLocation;
	}

	public SingleSizedTexture(String resourceLocation, BufferedImage image) {
		this.resourceLocation = resourceLocation;
		this.image = image;
	}

	@Override
	public void process() {
		try {
			if (this.image == null && resourceLocation != null) {
				InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(this.resourceLocation)).getInputStream();
				this.image = ImageIO.read(inputStream);
				this.processed = true;
			}
		} catch (Throwable err) {
			err.printStackTrace();
		}
	}

	@Override
	public void loadTexture() {
		if (this.image != null) {
			TextureUtil.uploadTextureImage(this.getGlTextureId(), image);
			this.width = image.getWidth();
			this.height = image.getHeight();
			image = null;
		}
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public String getResourceLocation() {
		return this.resourceLocation.toString();
	}
}