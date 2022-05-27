package falcun.xyz.dev.boredhuman.dancore.falcunfork.api.textures;

import falcun.net.Falcun;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.Pair;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TextureManager {

	private Map<String, Pair<SizedTexture, Long>> textures = new ConcurrentHashMap<>();
	private ConcurrentLinkedDeque<SizedTexture> loadQueue = new ConcurrentLinkedDeque();
	private Set<String> loadingSet = ConcurrentHashMap.newKeySet();
	private Set<String> failedURLS = new HashSet<>();

	@SubscribeEvent
	public void onRender(TickEvent.RenderTickEvent tick) {
		if (tick.phase == TickEvent.Phase.END) {
			return;
		}
		long start = System.currentTimeMillis();
		while (!loadQueue.isEmpty()) {
			SizedTexture texture = loadQueue.poll();
			try {
				texture.loadTexture();
				String location = texture.getResourceLocation();
				textures.put(location, Pair.of(texture, start));
				if (!this.loadingSet.remove(texture.getResourceLocation())) {
					System.out.println("Tried to remove non existent texture");
				}
			} catch (Throwable err) {
//				Logger.log("Failed to make texture");
			}
			// don't spend more than 10 ms in this method
			if (System.currentTimeMillis() - start > 10) {
				break;
			}
		}
		Iterator<Map.Entry<String, Pair<SizedTexture, Long>>> entryIterator = textures.entrySet().iterator();
		while (entryIterator.hasNext()) {
			Map.Entry<String, Pair<SizedTexture, Long>> entry = entryIterator.next();
			// textures timeout and are deleted after 3 minutes
			if (entry.getValue().second + 180000 < start) {
				GL11.glDeleteTextures(entry.getValue().first.getGlTextureId());
				entryIterator.remove();
			}
			// don't spend more than 10 ms in this method
			if (System.currentTimeMillis() - start > 10) {
				break;
			}
		}
	}

	/**
	 * @param resourceLocation
	 * @return returns true if the texture was bound
	 */
	public boolean bindTexture(String resourceLocation) {
		Pair<SizedTexture, Long> texture = this.textures.get(resourceLocation);
		if (texture != null) {
			GlStateManager.bindTexture(texture.first.getGlTextureId());
			// renew timeout
			texture.second = System.currentTimeMillis();
			return true;
		} else {
			if (resourceLocation.startsWith("http")) {
				this.loadTextureURL(resourceLocation);
			} else {
				this.loadTexture(resourceLocation);
			}
		}
		return false;
	}

	public SizedTexture getSizedTexture(String resourceLocation) {
		Pair<SizedTexture, Long> texture = this.textures.get(resourceLocation);
		if (texture == null) {
			if (resourceLocation.startsWith("http")) {
				this.loadTextureURL(resourceLocation);
			} else {
				this.loadTexture(resourceLocation);
			}
			return null;
		}
		// renew timeout
		texture.second = System.currentTimeMillis();
		return texture.first;
	}

	public void loadTextureURL(String url) {
		if (!this.shouldLoadTexture(url)) {
			return;
		}
		this.loadingSet.add(url);
		Falcun.submitJob(() -> {
			try {
				URL link = new URL(url);
				URLConnection connection = link.openConnection();
				InputStream stream = connection.getInputStream();
				BufferedImage image = ImageIO.read(stream);
				SizedTexture texture = new SingleSizedTexture(url, image);
				return texture;
			} catch (Throwable err) {
				this.failedURLS.add(url);
//				Logger.log("Failed to load texture from url");
				this.loadingSet.remove(url);
			}
			return null;
		}, img -> {
			if (img != null) {
				this.loadQueue.add(img);
			}
		});
	}

	public void loadTexture(String resourceLocation) {
		if (!this.shouldLoadTexture(resourceLocation)) {
			return;
		}
		this.loadingSet.add(resourceLocation);
		Falcun.submitJob(() -> {
			SizedTexture texture = null;
			if (resourceLocation.endsWith(".png")) {
				texture = new SingleSizedTexture(resourceLocation);
			} else if (resourceLocation.endsWith(".gif")) {
				texture = new GIFTexture(resourceLocation);
			} else {
				this.loadingSet.remove(resourceLocation);
				return null;
			}
			texture.process();
			return texture;
		}, result -> {
			if (result != null) {
				this.loadQueue.add(result);
			}
		});
	}

	private boolean shouldLoadTexture(String resourceLocation) {
		return !this.textures.containsKey(resourceLocation) && !this.loadingSet.contains(resourceLocation) && !this.failedURLS.contains(resourceLocation);
	}
}