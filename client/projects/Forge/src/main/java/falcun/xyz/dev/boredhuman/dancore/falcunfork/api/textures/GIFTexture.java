package falcun.xyz.dev.boredhuman.dancore.falcunfork.api.textures;

import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.gif.GIFImageReaderSpi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class GIFTexture extends SizedTexture {

	String resourceLocation;
	BufferedImage image;
	// width of single frame
	int width;
	// height of single frame
	int height;
	int frames;
	int delayTimeMS;

	public GIFTexture(String resourceLocation) {
		this.resourceLocation = resourceLocation;
	}

	@Override
	public void process() {
		try (InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(this.resourceLocation)).getInputStream()) {
			ImageInputStream imageInputStream = ImageIO.createImageInputStream(inputStream);
			ImageReader imageReader = new GIFImageReader(new GIFImageReaderSpi());
			imageReader.setInput(imageInputStream);

			IIOMetadata imageMetaData = imageReader.getImageMetadata(0);
			String metaFormatName = imageMetaData.getNativeMetadataFormatName();

			IIOMetadataNode root = (IIOMetadataNode) imageMetaData.getAsTree(metaFormatName);

			IIOMetadataNode graphicsControlExtensionNode = this.getNode(root);

			int delayTime = Integer.parseInt(graphicsControlExtensionNode.getAttribute("delayTime"));
			boolean dispose = !graphicsControlExtensionNode.getAttribute("disposalMethod").equals("doNotDispose");
			this.delayTimeMS = delayTime * 10;

			this.width = imageReader.getWidth(0);
			this.height = imageReader.getHeight(0);
			this.frames = imageReader.getNumImages(true);

			BufferedImage spriteSheet = new BufferedImage(this.width * this.frames, this.height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphics2D = spriteSheet.createGraphics();
			BufferedImage sprite = null;
			for (int i = 0; i < this.frames; i++) {
				if (!dispose && sprite != null) {
					graphics2D.drawImage(imageReader.read(i - 1), this.width * i, 0, null);
				}
				graphics2D.drawImage(sprite = imageReader.read(i), this.width * i, 0, null);
			}
			graphics2D.dispose();
			this.image = spriteSheet;
		} catch (Throwable err) {
			err.printStackTrace();
		}
	}

	private IIOMetadataNode getNode(IIOMetadataNode rootNode) {
		int nNodes = rootNode.getLength();
		for (int i = 0; i < nNodes; i++) {
			if (rootNode.item(i).getNodeName().compareToIgnoreCase("GraphicControlExtension") == 0) {
				return ((IIOMetadataNode) rootNode.item(i));
			}
		}
		IIOMetadataNode node = new IIOMetadataNode("GraphicControlExtension");
		rootNode.appendChild(node);
		return (node);
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	public float[] getUVForFrame(int frame) {
		float frameWidth = 1F / this.frames;
		float minU = frame * frameWidth;
		float maxU = (frame + 1) * frameWidth;
		float minV = 0;
		float maxV = 1;
		return new float[]{minU, maxU, minV, maxV};
	}

	public boolean shouldDoNextFrame(long lastFrame) {
		return System.currentTimeMillis() - lastFrame > this.delayTimeMS;
	}

	public int getNextFrame(int currentFrame) {
		int nextFrame = currentFrame + 1;
		if (nextFrame < this.frames) {
			return nextFrame;
		} else {
			return 0;
		}
	}

	@Override
	public void loadTexture() {
		if (this.image != null) {
			TextureUtil.uploadTextureImage(this.getGlTextureId(), this.image);
			this.image = null;
		}
	}

	@Override
	public String getResourceLocation() {
		return this.resourceLocation;
	}
}