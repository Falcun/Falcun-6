package falcun.net.api.fonts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public final class NahrFont implements FalcunFont {
	private BufferedImage bufferedImage;
	private DynamicTexture dynamicTexture;
	private final int endChar;

	public enum FontType {
		EMBOSS_BOTTOM, EMBOSS_TOP, NORMAL, OUTLINE_THIN, SHADOW_THICK, SHADOW_THIN;
	}

	private float extraSpacing = 0.0F;
	private final float fontSize;
	private final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OG]");
	private final Pattern patternUnsupported = Pattern.compile("(?i)\\u00A7[K-O]");
	private ResourceLocation resourceLocation;
	private final int startChar;
	private Font theFont;
	private Graphics2D theGraphics;
	private FontMetrics theMetrics;
	private final float[] xPos;
	private final float[] yPos;

	public NahrFont(Object font, float size) {
		this(font, size, 0.0F);
	}

	public NahrFont(Object font, float size, float spacing) {
		this.fontSize = size;
		this.startChar = 32;
		this.endChar = 255;
		this.extraSpacing = spacing;
		this.xPos = new float[this.endChar - this.startChar];
		this.yPos = new float[this.endChar - this.startChar];
		setupGraphics2D();
		createFont(font, size);
	}

	private void createFont(Object font, float size) {
		try {
			if ((font instanceof Font)) {
				this.theFont = ((Font) font);
			} else if ((font instanceof File)) {
				this.theFont = Font.createFont(0, (File) font).deriveFont(size);
			} else if ((font instanceof InputStream)) {
				this.theFont = Font.createFont(Font.TRUETYPE_FONT, (InputStream) font).deriveFont(size);
			} else if ((font instanceof String)) {
				this.theFont = new Font((String) font, Font.PLAIN, Math.round(size));
			} else {
				this.theFont = new Font("Verdana", Font.PLAIN, Math.round(size));
			}
			this.theGraphics.setFont(this.theFont);
		} catch (Exception e) {
			this.theFont = new Font("Verdana", Font.PLAIN, Math.round(size));
			this.theGraphics.setFont(this.theFont);
		}
		this.theGraphics.setColor(new Color(255, 255, 255, 0));
		this.theGraphics.fillRect(0, 0, 256, 256);
		this.theGraphics.setColor(Color.white);
		this.theMetrics = this.theGraphics.getFontMetrics();

		float x = 5.0F;
		float y = 5.0F;
		for (int i = this.startChar; i < this.endChar; i++) {
			this.theGraphics.drawString(Character.toString((char) i), x, y + this.theMetrics.getAscent());
			this.xPos[(i - this.startChar)] = x;
			this.yPos[(i - this.startChar)] = (y - this.theMetrics.getMaxDescent());
			x += this.theMetrics.stringWidth(Character.toString((char) i)) + 2.0F;
			if (x >= 250 - this.theMetrics.getMaxAdvance()) {
				x = 5.0F;

				y = y + (this.theMetrics.getMaxAscent() + this.theMetrics.getMaxDescent() + this.fontSize / 2.0F);
			}
		}
		this.resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("font" + font.toString() + size, this.dynamicTexture = new DynamicTexture(this.bufferedImage));
	}

	private void drawChar(char character, float x, float y) throws ArrayIndexOutOfBoundsException {
		Rectangle2D bounds = this.theMetrics.getStringBounds(Character.toString(character), this.theGraphics);
		drawTexturedModalRect(x, y, this.xPos[(character - this.startChar)], this.yPos[(character - this.startChar)], (float) bounds.getWidth(), (float) bounds.getHeight() + this.theMetrics.getMaxDescent() + 1.0F);
	}

	private void drawChar(char character, double x, double y) throws ArrayIndexOutOfBoundsException {
		Rectangle2D bounds = this.theMetrics.getStringBounds(Character.toString(character), this.theGraphics);
		drawTexturedModalRect(x, y, this.xPos[(character - this.startChar)], this.yPos[(character - this.startChar)], (float) bounds.getWidth(), (float) bounds.getHeight() + this.theMetrics.getMaxDescent() + 1.0F);
	}

	private void drawer(String text, float x, float y, int color) {
		GL11.glEnable(3553);
		Minecraft.getMinecraft().getTextureManager().bindTexture(this.resourceLocation);
		float alpha = (color >> 24 & 0xFF) / 255.0F;
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;
		GL11.glColor4f(red, green, blue, alpha);
		float startX = x;
		for (int i = 0; i < text.length(); i++) {
			String geC = EnumChatFormatting.RED + "";
			char cD = geC.charAt(0);
			if ((text.charAt(i) == cD) && (i + 1 < text.length())) {
				char oneMore = Character.toLowerCase(text.charAt(i + 1));
				if (oneMore == 'n') {
					y += this.theMetrics.getAscent() + 2;
					x = startX;
				}
				int colorCode = "0123456789abcdefklmnorg".indexOf(oneMore);
				if (colorCode < 16) {
					try {
						int newColor = Minecraft.getMinecraft().fontRendererObj.getColorCode((char) colorCode);
						GL11.glColor4f((newColor >> 16) / 255.0F, (newColor >> 8 & 0xFF) / 255.0F, (newColor & 0xFF) / 255.0F, alpha);
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				} else if (oneMore == 'f') {
					GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
				} else if (oneMore == 'r') {
					GL11.glColor4f(red, green, blue, alpha);
				} else if (oneMore == 'g') {
					GL11.glColor4f(0.3F, 0.7F, 1.0F, alpha);
				}
				i++;
			} else {
				try {
					char c = text.charAt(i);
					drawChar(c, x, y);
					x += getStringWidthFloat(Character.toString(c)) * 2.0F;
				} catch (ArrayIndexOutOfBoundsException indexException) {
					text.charAt(i);
				}
			}
		}
	}

	private void drawer(String text, double x, double y, int color) {
		x *= 2.0F;
		y *= 2.0F;
		GL11.glEnable(3553);
		Minecraft.getMinecraft().getTextureManager().bindTexture(this.resourceLocation);
		float alpha = (color >> 24 & 0xFF) / 255.0F;
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;
		GL11.glColor4f(red, green, blue, alpha);
		double startX = x;
		for (int i = 0; i < text.length(); i++) {
			String geC = EnumChatFormatting.RED + "";
			char cD = geC.charAt(0);
			if ((text.charAt(i) == cD) && (i + 1 < text.length())) {
				char oneMore = Character.toLowerCase(text.charAt(i + 1));
				if (oneMore == 'n') {
					y += this.theMetrics.getAscent() + 2;
					x = startX;
				}
				int colorCode = "0123456789abcdefklmnorg".indexOf(oneMore);
				if (colorCode < 16) {
					try {
						int newColor = Minecraft.getMinecraft().fontRendererObj.getColorCode((char) colorCode);
						GL11.glColor4f((newColor >> 16) / 255.0F, (newColor >> 8 & 0xFF) / 255.0F, (newColor & 0xFF) / 255.0F, alpha);
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				} else if (oneMore == 'f') {
					GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
				} else if (oneMore == 'r') {
					GL11.glColor4f(red, green, blue, alpha);
				} else if (oneMore == 'g') {
					GL11.glColor4f(0.3F, 0.7F, 1.0F, alpha);
				}
				i++;
			} else {
				try {
					char c = text.charAt(i);
					drawChar(c, x, y);
					x += getStringWidthFloat(Character.toString(c)) * 2.0F;
				} catch (ArrayIndexOutOfBoundsException indexException) {
					text.charAt(i);
				}
			}
		}
	}


	public void drawCenteredString(String text, float x, float y, int color) {
		drawString(text, x - getStringWidthFloat(text) / 2, y, color, false );
	}

	public void drawString(String text, float x, float y, FontType fontType, int color, int color2) {
		x *= 2;
		y *= 2;
		y -= 2f;
		text = stripUnsupported(text);
		GlStateManager.pushAttrib();
		GL11.glEnable(3042);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		String text2 = stripControlCodes(text);
		switch (fontType.ordinal()) {
			case 4:
				drawer(text2, x + 1.0F, y + 1.0F, color2);
				break;
			case 5:
				//drawer(text2, x + 0.5F, y + 0.5F, color2);
				break;
			case 3:
				drawer(text2, x + 0.5F, y, color2);
				drawer(text2, x - 0.5F, y, color2);
				drawer(text2, x, y + 0.5F, color2);
				drawer(text2, x, y - 0.5F, color2);
				break;
			case 2:
				//drawer(text2, x, y + 0.5F, color2);
				break;
			case 1:
				drawer(text2, x, y - 0.5F, color2);
				break;
		}
		drawer(text, x, y, color);
		GlStateManager.popAttrib();
		GL11.glScalef(2.0F, 2.0F, 2.0F);
	}

	public void drawString(String text, double x, double y, FontType fontType, int color, int color2) {
		y -= 2f;
		text = stripUnsupported(text);
		GlStateManager.pushAttrib();
		GL11.glEnable(3042);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		String text2 = stripControlCodes(text);
		switch (fontType.ordinal()) {
			case 4:
				drawer(text2, x + 1.0F, y + 1.0F, color2);
				break;
			case 5:
				//drawer(text2, x + 0.5F, y + 0.5F, color2);
				break;
			case 3:
				drawer(text2, x + 0.5F, y, color2);
				drawer(text2, x - 0.5F, y, color2);
				drawer(text2, x, y + 0.5F, color2);
				drawer(text2, x, y - 0.5F, color2);
				break;
			case 2:
				//drawer(text2, x, y + 0.5F, color2);
				break;
			case 1:
				drawer(text2, x, y - 0.5F, color2);
				break;
		}
		drawer(text, x, y, color);
		GlStateManager.popAttrib();
		GL11.glScalef(2.0F, 2.0F, 2.0F);
	}

	@Override
	public Number stringHeight(String text) {
		return getStringHeight(text);
	}



	private void drawTexturedModalRect(double x, double y, double u, double v, double width, double height) {
		WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
		Tessellator tesselator = Tessellator.getInstance();
		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);


		worldRenderer.pos(x + 0.0F, y + height, 0.0D).tex((u + 0.0F) * 0.0039063F, (v + height) * 0.0039063F).endVertex();


		worldRenderer.pos(x + width, y + height, 0.0D).tex((u + width) * 0.0039063F, (v + height) * 0.0039063F).endVertex();


		worldRenderer.pos(x + width, y + 0.0F, 0.0D).tex((u + width) * 0.0039063F, (v + 0.0F) * 0.0039063F).endVertex();


		worldRenderer.pos(x + 0.0F, y + 0.0F, 0.0D).tex((u + 0.0F) * 0.0039063F, (v + 0.0F) * 0.0039063F).endVertex();
		tesselator.draw();
	}

	private void drawTexturedModalRect(float x, float y, float u, float v, float width, float height) {
		WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
		Tessellator tesselator = Tessellator.getInstance();
		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);


		worldRenderer.pos(x + 0.0F, y + height, 0.0D).tex((u + 0.0F) * 0.0039063F, (v + height) * 0.0039063F).endVertex();


		worldRenderer.pos(x + width, y + height, 0.0D).tex((u + width) * 0.0039063F, (v + height) * 0.0039063F).endVertex();


		worldRenderer.pos(x + width, y + 0.0F, 0.0D).tex((u + width) * 0.0039063F, (v + 0.0F) * 0.0039063F).endVertex();


		worldRenderer.pos(x + 0.0F, y + 0.0F, 0.0D).tex((u + 0.0F) * 0.0039063F, (v + 0.0F) * 0.0039063F).endVertex();
		tesselator.draw();
	}

	private Rectangle2D getBounds(String text) {
		return this.theMetrics.getStringBounds(text, this.theGraphics);
	}

	public Font getFont() {
		return this.theFont;
	}

	private String getFormatFromString(String par0Str) {
		String var1 = "";
		int var2 = -1;
		int var3 = par0Str.length();
		while ((var2 = par0Str.indexOf((EnumChatFormatting.RED).toString().substring(1), var2 + 1)) != -1) {
			if (var2 < var3 - 1) {
				char var4 = par0Str.charAt(var2 + 1);
				if (isFormatColor(var4)) {
					var1 = (EnumChatFormatting.RED).toString().substring(1) + var4;
				} else if (isFormatSpecial(var4)) {
					var1 = var1 + (EnumChatFormatting.RED).toString().substring(1) + var4;
				}
			}
		}
		return var1;
	}

	public Graphics2D getGraphics() {
		return this.theGraphics;
	}

	public float getStringHeight(String text) {
		return (float) getBounds(text).getHeight() / 2.0F;
	}

	public Number getStringWidth(String text) {
		return (Number)getStringWidth(text);
	}

	public float getStringWidthFloat(String text) {
		return (float) (getBounds(text).getWidth() + this.extraSpacing) / 2.0F;
	}

	private boolean isFormatColor(char par0) {
		return ((par0 >= '0') && (par0 <= '9')) || ((par0 >= 'a') && (par0 <= 'f')) || ((par0 >= 'A') && (par0 <= 'F'));
	}

	private boolean isFormatSpecial(char par0) {
		return ((par0 >= 'k') && (par0 <= 'o')) || ((par0 >= 'K') && (par0 <= 'O')) || (par0 == 'r') || (par0 == 'R');
	}

	public List listFormattedStringToWidth(String s, int width) {
		return Arrays.asList(wrapFormattedStringToWidth(s, width).split("\n"));
	}

	private void setupGraphics2D() {
		this.bufferedImage = new BufferedImage(256, 256, 2);
		this.theGraphics = ((Graphics2D) this.bufferedImage.getGraphics());
		this.theGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	}

	private int sizeStringToWidth(String par1Str, float par2) {
		int var3 = par1Str.length();
		float var4 = 0.0F;
		int var5 = 0;
		int var6 = -1;
		for (boolean var7 = false; var5 < var3; var5++) {
			char var8 = par1Str.charAt(var5);
			switch (var8) {
				case '\n':
					var5--;
					break;
				case '\u00C2':
					if (var5 < var3 - 1) {
						var5++;
						char var9 = par1Str.charAt(var5);
						if ((var9 != 'l') && (var9 != 'L')) {
							if ((var9 == 'r') || (var9 == 'R') || (isFormatColor(var9))) {
								var7 = false;
							}
						} else {
							var7 = true;
						}
					}
					break;
				case ' ':
					var6 = var5;
				case '-':
					var6 = var5;
				case '_':
					var6 = var5;
				case ':':
					var6 = var5;
				default:
					String text = String.valueOf(var8);
					var4 += getStringWidthFloat(text);
					if (var7) {
						var4 += 1.0F;
					}
					break;
			}
			if (var8 == '\n') {
				var5++;
				var6 = var5;
			} else {
				if (var4 > par2) {
					break;
				}
			}
		}
		return (var5 != var3) && (var6 != -1) && (var6 < var5) ? var6 : var5;
	}

	public String stripControlCodes(String s) {
		return this.patternControlCode.matcher(s).replaceAll("");
	}


	/**
	 * Trims a string to fit a specified Width.
	 */
	public String trimStringToWidth(String text, int width) {
		return this.trimStringToWidth(text, width, false);
	}

	/**
	 * Array of width of all the characters in default.png
	 */
	private float[] charWidth = new float[256];
	private boolean unicodeFlag = true;
	private byte[] glyphWidth = new byte[65536];

	private float getCharWidthFloat(char character) {
		if (character == 167) {
			return -1.0F;
		} else if (character == 32) {
			return this.charWidth[32];
		} else {
			int var2 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(character);

			if (character > 0 && var2 != -1 && !this.unicodeFlag) {
				return this.charWidth[var2];
			} else if (this.glyphWidth[character] != 0) {
				int var3 = this.glyphWidth[character] >>> 4;
				int var4 = this.glyphWidth[character] & 15;

				if (var4 > 7) {
					var4 = 15;
					var3 = 0;
				}

				++var4;
				return (var4 - var3) / 2 + 1;
			} else {
				return 0.0F;
			}
		}
	}

	/**
	 * Trims a string to a specified width, and will reverse it if par3 is set.
	 */
	public String trimStringToWidth(String text, int width, boolean reverse) {
		StringBuilder var4 = new StringBuilder();
		float var5 = 0.0F;
		int var6 = reverse ? text.length() - 1 : 0;
		int var7 = reverse ? -1 : 1;
		boolean var8 = false;
		boolean var9 = false;

		for (int var10 = var6; var10 >= 0 && var10 < text.length() && var5 < width; var10 += var7) {
			char var11 = text.charAt(var10);
			float var12 = this.getCharWidthFloat(var11);

			if (var8) {
				var8 = false;

				if (var11 != 108 && var11 != 76) {
					if (var11 == 114 || var11 == 82) {
						var9 = false;
					}
				} else {
					var9 = true;
				}
			} else if (var12 < 0.0F) {
				var8 = true;
			} else {
				var5 += var12;

				if (var9) {
					++var5;
				}
			}

			if (var5 > width) {
				break;
			}

			if (reverse) {
				var4.insert(0, var11);
			} else {
				var4.append(var11);
			}
		}

		return var4.toString();
	}

	public void drawString(String text, Number x, Number y, int color, boolean underline) {
		drawString(text, (float)x,(float) y, FontType.NORMAL, color, 0xFF000000);
	}

	public int drawString2(String text, float x, float y, int color) {
		drawString(text, x, y, FontType.NORMAL, color, 0xFF000000);
		return (int) (x + this.getStringWidthFloat(text));
	}

	public String stripUnsupported(String s) {
		try {
			return this.patternUnsupported.matcher(s).replaceAll("");
		} catch (Exception e) {
			return "error";
		}
	}

	public String wrapFormattedStringToWidth(String s, float width) {
		int wrapWidth = sizeStringToWidth(s, width);
		if (s.length() <= wrapWidth) {
			return s;
		}
		String split = s.substring(0, wrapWidth);
		String split2 = getFormatFromString(split) + s.substring(wrapWidth + ((s.charAt(wrapWidth) == ' ') || (s.charAt(wrapWidth) == '\n') ? 1 : 0));
		try {
			return split + "\n" + wrapFormattedStringToWidth(split2, width);
		} catch (Exception e) {
			System.out.println("Cannot wrap string to width.");
		}
		return "";
	}
}