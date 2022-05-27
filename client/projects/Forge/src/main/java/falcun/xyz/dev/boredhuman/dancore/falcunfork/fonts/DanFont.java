package falcun.xyz.dev.boredhuman.dancore.falcunfork.fonts;

import falcun.net.api.fonts.FalcunFont;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DanFont implements FalcunFont {
	private static final Map<Pair<String, Integer>, DanFont> cache = new Object2ObjectOpenHashMap<>();


	private final ResourceLocation location;
	public int resolution;
	private final FontTexture texture;
	private boolean loaded;

	/**
	 * This will create a font if it doesn't already exist, or return the cached font.
	 *
	 * @param resourcelocation the resourcelocation of the font
	 * @param resolution       the resolution of the font
	 * @return the font
	 */
	public static DanFont getOrCreateFont(String resourcelocation, int resolution) {
		Pair<String, Integer> pair = Pair.of(resourcelocation, resolution);
		return DanFont.cache.computeIfAbsent(pair, data -> new DanFont(data.first, data.second));
	}

	public DanFont(String location, int resolution) {
		this.location = new ResourceLocation(location);
		this.resolution = resolution;
		this.texture = new FontTexture(this.location, this.resolution);
		DanFont.cache.put(Pair.of(location, resolution), this);
	}

	public Number getStringWidth(String text) {
		int totalWidth = 0;
		for (int i = 0; i < text.length(); i++) {
			char letter = text.charAt(i);
			if (letter == 32) {
				totalWidth += this.resolution / 4;
				continue;
			}
			if (letter > 255) {
				continue;
			}
			totalWidth += this.getTexture().charWidths[letter];
		}
		return totalWidth;
	}

	public void drawCenteredString(String text, int posX, int posY, int color, boolean italic) {
		this.drawString(text, posX - (int)this.getStringWidth(text) / 2f, posY, color, italic);
	}

	public void drawString(String text, float posX, float posY, int color, boolean italic) {
		GlStateManager.enableTexture2D();
		GlStateManager.bindTexture(this.getTexture().getGlTextureId());
		Tessellator tessellator = Tessellator.getInstance();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		WorldRenderer wr = tessellator.getWorldRenderer();
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		int[] widths = this.texture.charWidths;
		float resolutionF = this.resolution - 0.01f;
		float size = this.resolution * 16;
		for (int k = 0; k < text.length(); k++) {
			char letter = text.charAt(k);
			if (letter == 32) {
				posX += this.resolution / 4;
				continue;
			}
			if (letter > 255) {
				continue;
			}
			int i = letter % 16 * this.resolution + 1;
			int j = letter / 16 * this.resolution;
			int italicOffset = italic ? (this.resolution / 8) : 0;
			int charWidth = widths[letter];
			float charOffset = (float) charWidth - 0.01F;
			int red = (color >> 16) & 0xff;
			int green = (color >> 8) & 0xff;
			int blue = color & 0xff;
			int alpha = (color >> 24) & 0xff;
			int z = 0;
			wr.pos(posX + italicOffset, posY, z).tex(i / size, j / size).color(red, green, blue, alpha).endVertex();
			wr.pos(posX - italicOffset, posY + resolutionF, z).tex(i / size, (j + resolutionF) / size).color(red, green, blue, alpha)
				.endVertex();
			wr.pos(posX + charOffset - 1.0f - italicOffset, posY + resolutionF, z)
				.tex((i + charOffset - 1.0f) / size, (j + resolutionF) / size)
				.color(red, green, blue, alpha).endVertex();
			wr.pos(posX + charOffset - 1.0f + (float) italicOffset, posY, z).tex((i + charOffset - 1.0f) / size, j / size)
				.color(red, green, blue, alpha)
				.endVertex();
			posX += charOffset;
		}
		tessellator.draw();
	}

	@Override
	public int size(){
		return resolution;
	}

	@Override
	public List<String> getLinesWrapped(String text, int maxWidth) {
		String[] words = text.split(" ");
		if (words.length == 0) {
			return Collections.emptyList();
		} else if (words.length == 1) {
			return Collections.singletonList(words[0]);
		}
		int width = 0;
		List<String> lines = new ArrayList<>();
		StringBuilder line = new StringBuilder();
		for (String word : words) {
			width += (int)this.getStringWidth(word);

			if (width > maxWidth) {
				lines.add(line.toString());
				line = new StringBuilder(word);
				width = 0;
			} else {
				if (line.length() != 0) {
					line.append(" ");
					width += this.resolution / 4;
				}
				line.append(word);
			}
		}
		lines.add(line.toString());
		return lines;
	}

	public FontTexture getTexture() {
		if (!this.loaded) {
			this.texture.loadTexture(null);
			this.loaded = true;
		}
		return this.texture;
	}

	public String getResourceLocation() {
		return this.location.toString();
	}

	public void drawString(String text, Number x, Number y, int color, boolean underline){
		drawString(text, (float)x.intValue(), (float)y.intValue(), color, false);
	}


	public Number stringHeight(String text){
		return resolution;
	}
}