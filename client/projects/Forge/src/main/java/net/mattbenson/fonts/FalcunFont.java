package net.mattbenson.fonts;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.optifine.render.GlBlendState;

public class FalcunFont {
	private final static int FIRST_CHARACTER = 32;
	private final static int LAST_CHARACTER = 126;
	private final static int OFFSET = FIRST_CHARACTER;
	
	private static Map<?, ?> desktopHints = (Map<?, ?>) Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints");
	
	private CharacterData[] characterData;
	private ResourceLocation location;
	private int width;
	private int height;
	private boolean initd;
	
	private boolean normal;
	
	private final static int SPACING = 4;
	private final static int HEIGHT_ADD = 1;
	
	private GlBlendState oldBlendState = new GlBlendState();
	
	public FalcunFont(InputStream inputStream, int size, boolean normal) throws FontFormatException, IOException {
		Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont((float) size);
		init(font, normal);
	}

	public FalcunFont(String fontName, int size, boolean normal) {
		Font font = new Font(fontName, Font.PLAIN, size);
		init(font, normal);
	}

	private void init(Font font, boolean normal) {
		this.normal = normal;
		this.characterData = new CharacterData[LAST_CHARACTER - FIRST_CHARACTER];
		
		BufferedImage temp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = temp.createGraphics();
		
		if(desktopHints != null) {
			graphics.setRenderingHints(desktopHints);
		}
		
		graphics.setFont(font);
		
		int x = 0;
		int y = 0;
		int maxHeight = 0;
		
		StringBuilder builder = new StringBuilder();

		for(int i = FIRST_CHARACTER; i < LAST_CHARACTER; i++) {
			char character = (char) (i);
			
			Rectangle2D rect = graphics.getFontMetrics().getStringBounds(character + "", graphics);
			
			int w = (int) rect.getWidth();
			int h = (int) rect.getHeight() + HEIGHT_ADD;
			
			if(h > maxHeight) {
				maxHeight = h;
			}
			
			int index = i - OFFSET;
			
			characterData[index] = new CharacterData(x, y, w, h);
			
			x += w * 2;
			
			builder.append(character);
		}
		
		graphics.dispose();
		
		width = x;
		height = maxHeight;
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		graphics = image.createGraphics();
		
		if(desktopHints != null) {
			graphics.setRenderingHints(desktopHints);
		}
		
		graphics.setFont(font);
		graphics.setColor(Color.WHITE);
		
		graphics.translate(0, graphics.getFontMetrics().getAscent());
		
		int tempX = 0;
		
		for(int i = FIRST_CHARACTER; i < LAST_CHARACTER; i++) {
			int index = (int) i - OFFSET;
			
			CharacterData charData = characterData[index];
			
			graphics.drawString((char) i + "", tempX, 0);
			
			tempX += charData.getWidth() * 2;
		}
		
		graphics.translate(0, 0);
		
		graphics.dispose();
		
		DynamicTexture texture = new DynamicTexture(image);
		location = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("falcun-font-" + font.getName() + "#" + font.getSize(), texture);
		
		initd = true;
	}
	
	public void drawString(String string, int x, int y, int color) {
		if(!initd) {
			return;
		}
		
		GlStateManager.pushMatrix();
		GlStateManager.enableAlpha();
		
        GlStateManager.getBlendState(oldBlendState);
		
        GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		
		float value = 1F / new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
		
		if(!normal) {
			GlStateManager.translate(x, y, 0);
			GlStateManager.scale(value, value, value);
			GlStateManager.translate(-x, -y, 0);
		}
		
		Color c = new Color(color, true);
		
		GL11.glColor4f(c.getRed() / 255F, c.getGreen() / 255F, c.getBlue() / 255F, c.getAlpha());
		Minecraft.getMinecraft().getTextureManager().bindTexture(location);
		
		int xPos = x;
		
		for(char character : string.toCharArray()) {
			if(character < FIRST_CHARACTER || character > LAST_CHARACTER) {
				continue;
			}

			int index = (int) character - OFFSET;
			
			CharacterData charData = characterData[index];
			Gui.drawModalRectWithCustomSizedTexture(xPos, y, charData.getX(), charData.getY(), charData.getWidth(), charData.getHeight(), width, height);
			xPos += charData.getWidth() + SPACING;
		}
		
		GlStateManager.setBlendState(oldBlendState);
		GlStateManager.popMatrix();
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GlStateManager.color(1F, 1F, 1F);
	}
	
	public void drawCenteredString(String string, int x, int y, int color) {
		if(!initd) {
			return;
		}
		
		drawString(string, x - (getStringWidth(string) / 2), y, color);
	}
	
	public int getStringWidth(String string) {
		if(!initd) {
			return 0;
		}
		
		int width = 0;
		
		for(char character : string.toCharArray()) {
			if(character < FIRST_CHARACTER || character > LAST_CHARACTER) {
				continue;
			}
			
			int index = (int) character - OFFSET;
			
			CharacterData charData = characterData[index];
			width += charData.getWidth() + SPACING;
		}
		
		if(!normal) {
			float value = 1F / new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
			width *= value;
		}
		
		return width;
	}

	public int getStringHeight(String string) {
		if(!initd) {
			return 0;
		}
		
		int height = 0;
		
		for(char character : string.toCharArray()) {
			if(character < FIRST_CHARACTER || character > LAST_CHARACTER) {
				continue;
			}
			
			int index = (int) character - OFFSET;
			
			CharacterData charData = characterData[index];
			
			if(charData.getHeight() > height) {
				height = charData.getHeight();
			}
		}
		
		if(!normal) {
			float value = 1F / new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
			height *= value;
		}
		
		return height;
	}
	
	public void unload() {
		initd = false;
		
		characterData = null;
		
		if(location != null) {
			Minecraft.getMinecraft().getTextureManager().deleteTexture(location);
			location = null;
		}
	}
}
