package net.mattbenson.modules.types.mods;

import java.awt.Color;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.render.gui.GuiInitEvent;
import net.mattbenson.events.types.world.OnTickEvent;
import net.mattbenson.gui.framework.Menu;
import net.mattbenson.gui.menu.IngameMenu;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.resources.FallbackResourceManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;

public class Gui extends Module {
	@ConfigValue.Integer(name = "Blur Radius", min = 1, max = 100)
	private int radius = 12;
	
	@ConfigValue.Integer(name = "Fade Time", min = 1, max = 1000)
	private int fadeTime = 200;
	
	@ConfigValue.Boolean(name = "Gui Blur")
	private boolean guiBlur = true;
	
	@ConfigValue.Color(name = "Background Color")
	private Color colorD = new Color(18, 17, 22, 255);
	
	private final Map<String, FallbackResourceManager> domainResourceManagers = ((SimpleReloadableResourceManager) mc.getResourceManager()).domainResourceManagers;
	
	private List<Shader> listshaders;

	private ResourceLocation location = new ResourceLocation("blur", "blur");
	private ResourceLocation location2 = new ResourceLocation("fadein", "fadein");
	
	private long start = System.currentTimeMillis();
	
	public IngameMenu menuImpl;
	private Menu menu;
	
	private int width = 1035;
	private int height = 485;
	
	public Gui() {
		super("Gui", ModuleCategory.MODS);
		setKeyBind(Keyboard.KEY_BACKSLASH);
		
		menu = new Menu("", width, height);
		menuImpl = new IngameMenu(this, menu);
	}
	
	@Override
	public void onEnable() {
		setKeyBind(Keyboard.KEY_BACKSLASH);
		EntityRenderer er = Minecraft.getMinecraft().entityRenderer;
		if (listshaders == null) {
			listshaders = Lists.<Shader>newArrayList();
		}
		
		boolean excluded = mc.currentScreen == null;
		if (!er.isShaderActive() && !excluded && listshaders != null && this.guiBlur) {
			er.loadShader(location2);
			start = System.currentTimeMillis();
		} else if (er.isShaderActive() && excluded) {
			er.stopUseShader();
		}
		
		mc.displayGuiScreen(menuImpl);
	}
	
	private float getProgress() {
		return Math.min((System.currentTimeMillis() - start) / (float) fadeTime, 1);
	}

	@SubscribeEvent
	public void onRenderTick(OnTickEvent event) {
		if (domainResourceManagers != null) {
			if (!domainResourceManagers.containsKey("blur")) {
				domainResourceManagers.put("blur", new BlurResourceManager(mc.metadataSerializer_));
			}
			if (!domainResourceManagers.containsKey("fadein")) {
				domainResourceManagers.put("fadein", new FadeInResourceManager(mc.metadataSerializer_));
			}
		}
		
		if (Minecraft.getMinecraft().currentScreen != null
				&& Minecraft.getMinecraft().entityRenderer.isShaderActive()) {

			ShaderGroup sg = Minecraft.getMinecraft().entityRenderer.getShaderGroup();
			try {
				if (listshaders != null && listshaders.size() > 0) {
					for (Shader s : listshaders) {
						ShaderUniform su = s.getShaderManager().getShaderUniform("Progress");
						ShaderUniform su2 = s.getShaderManager().getShaderUniform("Radius");
						if (su != null) {
							su.set(getProgress());
							su2.set(radius);
						}
					}
				}
			} catch (Exception e) {
			}
		}
	}
	
	public class FadeInResourceManager extends FallbackResourceManager implements IResourceManager {
		public FadeInResourceManager(IMetadataSerializer frmMetadataSerializerIn) {
			super(frmMetadataSerializerIn);
		}

		@Override
		public Set<String> getResourceDomains() {
			return null;
		}

		@Override
		public IResource getResource(ResourceLocation location) {
			return new FadeInResource();
		}

		@Override
		public List<IResource> getAllResources(ResourceLocation location) {
			return null;
		}
	}
	
	public class FadeInResource implements IResource {

		private static final String JSON = "{\"blend\":{\"func\":\"add\",\"srcrgb\":\"one\",\"dstrgb\":\"zero\"},\"vertex\":\"sobel\",\"fragment\":\"fade_in_blur\",\"attributes\":[\"Position\"],\"samplers\":[{\"name\":\"DiffuseSampler\"}],\"uniforms\":[{\"name\":\"ProjMat\",\"type\":\"matrix4x4\",\"count\":16,\"values\":[1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1]},{\"name\":\"InSize\",\"type\":\"float\",\"count\":2,\"values\":[1,1]},{\"name\":\"OutSize\",\"type\":\"float\",\"count\":2,\"values\":[1,1]},{\"name\":\"BlurDir\",\"type\":\"float\",\"count\":2,\"values\":[1,1]},{\"name\":\"Radius\",\"type\":\"float\",\"count\":1,\"values\":[5]},{\"name\":\"Progress\",\"type\":\"float\",\"count\":1,\"values\":[0]}]}";

		@Override
		public ResourceLocation getResourceLocation() {
			return null;
		}

		@Override
		public InputStream getInputStream() {
			return IOUtils.toInputStream(JSON, Charset.defaultCharset());
		}

		@Override
		public boolean hasMetadata() {
			return false;
		}

		@Override
		public <T extends IMetadataSection> T getMetadata(String p_110526_1_) {
			return null;
		}

		@Override
		public String getResourcePackName() {
			return null;
		}
	}

	
	public class BlurResourceManager extends FallbackResourceManager implements IResourceManager {
		public BlurResourceManager(IMetadataSerializer frmMetadataSerializerIn) {
			super(frmMetadataSerializerIn);
		}

		@Override
		public Set<String> getResourceDomains() {
			return null;
		}

		@Override
		public IResource getResource(ResourceLocation location) {
			return new BlurResource();
		}

		@Override
		public List<IResource> getAllResources(ResourceLocation location) {
			return null;
		}
	}
	
	public class BlurResource implements IResource {

		private static final String JSON = "{\"blend\":{\"func\":\"add\",\"srcrgb\":\"one\",\"dstrgb\":\"zero\"},\"vertex\":\"sobel\",\"fragment\":\"blur\",\"attributes\":[\"Position\"],\"samplers\":[{\"name\":\"DiffuseSampler\"}],\"uniforms\":[{\"name\":\"ProjMat\",\"type\":\"matrix4x4\",\"count\":16,\"values\":[1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1]},{\"name\":\"InSize\",\"type\":\"float\",\"count\":2,\"values\":[1,1]},{\"name\":\"OutSize\",\"type\":\"float\",\"count\":2,\"values\":[1,1]},{\"name\":\"BlurDir\",\"type\":\"float\",\"count\":2,\"values\":[1,1]},{\"name\":\"Radius\",\"type\":\"float\",\"count\":1,\"values\":[5]}]}";

		@Override
		public ResourceLocation getResourceLocation() {
			return null;
		}

		@Override
		public InputStream getInputStream() {
			return IOUtils.toInputStream(JSON, Charset.defaultCharset());
		}

		@Override
		public boolean hasMetadata() {
			return false;
		}

		@Override
		public <T extends IMetadataSection> T getMetadata(String p_110526_1_) {
			return null;
		}

		@Override
		public String getResourcePackName() {
			return null;
		}
	}
}
