package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.api.DanCoreAPI;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.api.textures.GIFTexture;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.api.textures.SizedTexture;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.ColorUtil;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.GuiUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ImageElement<T extends ImageElement<T>> extends BasicElement<T> {

	private int textureId = -1;
	private String resourceLocation;
	private boolean keepResolution;
	// gif data
	private long lastFrame;
	private int currentFrame;

	private boolean tighten = false;

	public ImageElement(int textureId) {
		this.textureId = textureId;
	}

	public ImageElement(ResourceLocation resourceLocation) {
		this.resourceLocation = resourceLocation.toString();
		DanCoreAPI.getAPI().getTextureManager().loadTexture(resourceLocation.toString());
	}

	public ImageElement(String url) {
		this.resourceLocation = url;
		DanCoreAPI.getAPI().getTextureManager().loadTextureURL(url);
	}

	@Override
	public void render() {
		SizedTexture texture = DanCoreAPI.getAPI().getTextureManager().getSizedTexture(this.resourceLocation);
		if (texture != null) {
			GlStateManager.bindTexture(texture.getGlTextureId());
		} else if (this.textureId != -1) {
			GlStateManager.bindTexture(this.textureId);
		} else {
			return;
		}

		if (texture instanceof GIFTexture) {
			this.keepResolution = true;
		}

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GlStateManager.color(ColorUtil.getRed(this.color), ColorUtil.getGreen(this.color), ColorUtil.getBlue(this.color), ColorUtil.getAlpha(this.color));
		if (texture != null && this.keepResolution) {
			int imgWH = texture.getWidth() + texture.getHeight();
			float imgX = texture.getWidth() / (float) imgWH;
			float imgY = texture.getHeight() / (float) imgWH;

			int expectedWidth = (int) (imgX / imgY * this.height);

			if (expectedWidth <= this.width) {
				int excessWidth = this.width - expectedWidth;
				if (texture instanceof GIFTexture) {
					GIFTexture gifTexture = (GIFTexture) texture;
					if (this.lastFrame == 0) {
						this.lastFrame = System.currentTimeMillis();
					}
					float[] texData = gifTexture.getUVForFrame(this.currentFrame);
					GuiUtil.drawTextureSquare(this.x + excessWidth / 2, this.y, expectedWidth, this.height, texData[0], texData[1], texData[2], texData[3]);
					if (gifTexture.shouldDoNextFrame(this.lastFrame)) {
						this.lastFrame = System.currentTimeMillis();
						this.currentFrame = gifTexture.getNextFrame(this.currentFrame);
					}
				} else {
					if (this.tighten) {
						this.setWidthPX(texture.getWidth());
					}
					GuiUtil.drawTextureSquare(this.x + excessWidth / 2, this.y, expectedWidth, this.height);
				}
			} else {
				int expectedHeight = (int) (imgY / imgX) * this.width;
				if (expectedHeight <= this.height) {
					int excessHeight = this.height - expectedHeight;
					if (this.tighten) {
						this.setWidthPX(texture.getHeight());
					}
					GuiUtil.drawTextureSquare(this.x, this.y + excessHeight / 2, this.width, expectedHeight);
				}
			}
		} else {
			GuiUtil.drawTextureSquare(this.x, this.y, this.width, this.height);
		}
		GlStateManager.color(1, 1, 1, 1);
	}

	public T setTexture(int id) {
		this.textureId = id;
		return (T) this;
	}

	public T setTexture(ResourceLocation resourceLocation) {
		this.resourceLocation = resourceLocation.toString();
		DanCoreAPI.getAPI().getTextureManager().loadTexture(this.resourceLocation);
		return (T) this;
	}

	public T setTexture(String url) {
		this.resourceLocation = url;
		DanCoreAPI.getAPI().getTextureManager().loadTextureURL(url);
		return (T) this;
	}

	public String getTexture() {
		return this.resourceLocation;
	}

	public T setKeepResolution(boolean keepResolution) {
		this.keepResolution = keepResolution;
		return (T) this;
	}

	// causes this element to be the size of the contained image
	public T setTighten(boolean tighten) {
		this.tighten = tighten;
		return (T) this;
	}

}