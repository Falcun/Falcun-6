package net.mattbenson.modules.types.mods;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.input.KeyDownEvent;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class Coordinates extends Module {
	@ConfigValue.Boolean(name = "Show Compass")
	private boolean showCompass = true;
	
	@ConfigValue.Boolean(name = "Show Label", description = "Show the X, Y, Z labels")
	private boolean showLabel = true;
	
	@ConfigValue.Boolean(name = "Show Direction", description = "Show the +- direction your going")
	private boolean showDirection = true;
	
	@ConfigValue.Boolean(name = "Show Biome")
	private boolean showBiome = true;
	
	@ConfigValue.Boolean(name = "Biome Preset Color", description = "Use the minecraft color for that biome")
	private boolean biomePresetColor = true;
	
	@ConfigValue.Boolean(name = "Background")
	private boolean shadedCoords = true;
	
	@ConfigValue.Boolean(name = "Static Chroma")
	private boolean isUsingStaticChroma = false;
	
	@ConfigValue.Boolean(name = "Wave Chroma")
	private boolean isUsingWaveChroma = false;
	
	@ConfigValue.Boolean(name = "Custom Font")
	private boolean customFont = false;
	
	@ConfigValue.Boolean(name = "Show Avatar", description = "Shows your player head on the hud")
	private boolean showAvatar = false;
	
	@ConfigValue.Keybind(name = "Shout Coordinates Key", description = "Sends coordinates in chat")
	private int keyBind = 0;
	
	@ConfigValue.Text(name = "Chat Format", description = "How the coordinates should be sent {x}, {y}, {z} are place holders")
	private String chatFormat = "X: {x}, Y: {y}, Z: {z}";
	
	@ConfigValue.List(name = "Display Mode", values = {"Horizontal", "Vertical"}, description = "How the hud should be displayed")
	private String displayMode = "Vertical";
	
	@ConfigValue.Color(name = "Background Color")
	private Color backgroundColor = new Color(0, 0, 0, 150);
	
	@ConfigValue.Color(name = "Label Color", description = "Color of the X, Y, Z labels")
	private Color color = Color.WHITE;
	
	@ConfigValue.Color(name = "Value Color", description = "Color of the X, Y, Z coordinates")
	private Color vColor = Color.WHITE;
	
	@ConfigValue.Color(name = "Compass Color")
	private Color tColor = Color.WHITE;
	
	@ConfigValue.Color(name = "Biome Color")
	private Color bColor = Color.WHITE;
	
	int myPosX;
	int myPosY;
	int myPosZ;
	int myAngle;
	int myDir;
	int myMoveX;
	int myMoveZ;
	int myFind;

	int coordLocation;    
	int myXLine, myYLine, myZLine, myBiomeLine;
	int myBaseOffset;
	int myCoord1Offset, myCoord2Offset;
	int myRHSlocation;
	int coordBoxW, coordBoxH;
	int coordBoxL, coordBoxR, coordBoxTop, coordBoxBase;

	private static final String[] myCardinalPoint = new String[] {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
	
	private HUDElement hud;
	
	public Coordinates() {
		super("Coordinates", ModuleCategory.MODS);
		
		hud = new HUDElement("hud", 70, 30) {
			@Override
			public void onRender() {
				render();
			}
		};
		
		hud.setX(0);
		hud.setY(150);
		
		addHUD(hud);
	}
	
	private int getCardinalPoint(float par0) {
		double myPoint;
		myPoint = MathHelper.wrapAngleTo180_float(par0) + 180D;
		myPoint += 22.5D;
		myPoint %= 360D;
		myPoint /= 45D;
		return MathHelper.floor_double(myPoint);
	}

	@SubscribeEvent
	public void onKeyPress(KeyDownEvent event) {
		if (keyBind == 0)
			return;
		if (event.getKey() == keyBind) {
			String toSend = chatFormat.replace("{x}", myPosX + "").replace("{y}", myPosY + "").replace("{z}",
					myPosZ + "");
			mc.thePlayer.sendChatMessage(toSend);
		}
	}

	/**
	 * Writes the player's Coordinates and Compass Bearing onto the game screen
	 * 
	 */
	private void renderPlayerCoords() {
		Color color1 = color;
		Color color2 = vColor;
		Color color3 = tColor;

		float posX = hud.getX();
		float posY = hud.getY();

		if (this.displayMode.equalsIgnoreCase("Horizontal")) {
			GL11.glPushMatrix();
			if (this.mc.thePlayer.posX > 0) {
				myPosX = (int) MathHelper.floor_double(this.mc.thePlayer.posX);
			} else {
				myPosX = (int) MathHelper.ceiling_double_int(this.mc.thePlayer.posX);
			}
			myPosY = (int) MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().minY);
			if (this.mc.thePlayer.posZ > 0) {
				myPosZ = (int) MathHelper.floor_double(this.mc.thePlayer.posZ);
			} else {
				myPosZ = (int) MathHelper.ceiling_double_int(this.mc.thePlayer.posZ);
			}
			myAngle = getCardinalPoint(this.mc.thePlayer.rotationYaw);
			myDir = MathHelper.floor_double((double) (this.mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

			String med = "X: " + myPosX + ", Y: " + myPosY + ", Z: " + myPosZ;

			if (this.showDirection) {
				med = "";
				if (!getDirectionX(myAngle).isEmpty()) {
					med += "X: " + myPosX + " (" + getDirectionX(myAngle) + "), ";
				} else {
					med += "X: " + myPosX + ", ";
				}
				med += "Y: " + myPosY + ", ";
				if (!getDirectionZ(myAngle).isEmpty()) {
					med += "Z: " + myPosZ + " (" + getDirectionZ(myAngle) + ")";
				} else {
					med += "Z: " + myPosZ;
				}
			}

			if (this.showCompass) {
				med += " " + getDirectionY(myAngle);
			}

			if (!this.showLabel) {
				med = med.replace("X:", "").replace("Y:", "").replace("Z:", "");
			}
			med = med.trim();

			int width = getStringWidth(med) + 12;
			int height = 21;

			if (this.shadedCoords) {
				DrawUtils.drawGradientRect(posX, posY, posX + width, posY + height, backgroundColor.getRGB(),
						backgroundColor.getRGB());
			}

			drawString(med, (float) (posX - 2 + (width / 2 - (getStringWidth(med) / 2))), (float) posY + 5, vColor);

			hud.setWidth(width);
			hud.setHeight(height);

			GL11.glScaled(1, 1, 1);
			GL11.glColor3f(1, 1, 1);
			GL11.glPopMatrix();
		}

		if (this.displayMode.equalsIgnoreCase("Vertical")) {
			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			FontRenderer var8 = this.mc.fontRendererObj;
			if (this.mc.thePlayer.posX > 0) {
				myPosX = (int) MathHelper.floor_double(this.mc.thePlayer.posX);
			} else {
				myPosX = (int) MathHelper.ceiling_double_int(this.mc.thePlayer.posX);
			}
			myPosY = (int) MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().minY);
			if (this.mc.thePlayer.posZ > 0) {
				myPosZ = (int) MathHelper.floor_double(this.mc.thePlayer.posZ);
			} else {
				myPosZ = (int) MathHelper.ceiling_double_int(this.mc.thePlayer.posZ);
			}

			myAngle = getCardinalPoint(this.mc.thePlayer.rotationYaw);
			myDir = MathHelper.floor_double((double) (this.mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

			int stringWidth = 0;
			if (Math.abs(myPosX) > Math.abs(myPosZ) && Math.abs(myPosX) > Math.abs(myPosY)) {
				stringWidth = getStringWidth(myPosX + "");
			} else if (Math.abs(myPosZ) > Math.abs(myPosX) && Math.abs(myPosZ) > Math.abs(myPosY)) {
				stringWidth = getStringWidth(myPosZ + "");
			} else {
				stringWidth = getStringWidth(myPosY + "");
			}

			int width = 50;
			int height = 42;
			if (this.showAvatar) {
				width += 5;
			}
			int shift = 0;
			if (this.showBiome) {
				if (stringWidth < getStringWidth(
						"Biome: " + mc.thePlayer.worldObj.getBiomeGenForCoords(mc.thePlayer.getPosition()).biomeName)) {
					stringWidth = getStringWidth("Biome: "
							+ mc.thePlayer.worldObj.getBiomeGenForCoords(mc.thePlayer.getPosition()).biomeName);
				}
				height = 54;
			}

			width += stringWidth;
			if (this.shadedCoords) {
				DrawUtils.drawGradientRect(posX, posY, posX + width, posY + height, backgroundColor.getRGB(),
						backgroundColor.getRGB());
			}

			drawString("X: ", posX + 3, posY + 3 + shift, color);
			drawString(myPosX + "", posX + 16, posY + 3 + shift, vColor);
			drawString(getDirectionX(myAngle), posX + width - 8 - getStringWidth(getDirectionX(myAngle)),
					posY + 3 + shift, tColor);
			shift += 12;
			drawString("Y: ", posX + 3, posY + 3 + shift, color);
			drawString(myPosY + "", posX + 16, posY + 3 + shift, vColor);
			if (this.showAvatar) {
				this.mc.getTextureManager().bindTexture(mc.thePlayer.getLocationSkin());
				GuiScreen.drawScaledCustomSizeModalRect(
						(int) (posX + width - 20 - getStringWidth(getDirectionY(myAngle))), (int) (posY + 5 + shift),
						8.0F, 8, 8, 8, 8, 8, 64.0F, 64.0F);
			}
			drawString(getDirectionY(myAngle), posX + width - 8 - getStringWidth(getDirectionY(myAngle)),
					posY + 3 + shift, tColor);
			shift += 12;
			drawString("Z: ", posX + 3, posY + 3 + shift, color);
			drawString(myPosZ + "", posX + 16, posY + 3 + shift, vColor);
			drawString(getDirectionZ(myAngle), posX + width - 8 - getStringWidth(getDirectionZ(myAngle)),
					posY + 3 + shift, tColor);

			if (this.showBiome) {
				shift += 12;
				drawString("Biome: ", posX + 3, posY + 3 + shift, color);
				drawString(mc.thePlayer.worldObj.getBiomeGenForCoords(mc.thePlayer.getPosition()).biomeName,
						posX + 3 + getStringWidth("Biome: "), posY + 3 + shift,
						biomePresetColor
								? new Color(
										mc.thePlayer.worldObj.getBiomeGenForCoords(mc.thePlayer.getPosition()).color)
								: bColor);

			}

			hud.setWidth(width);
			hud.setHeight(height);

			GL11.glScaled(1, 1, 1);
			GL11.glColor3f(1, 1, 1);
			GL11.glPopMatrix();
		}

	}

	public int getStringWidth(String text) {
		if (this.customFont) {
			return (int) Fonts.RobotoHUD.getStringWidth(text);
		} else {
			return mc.fontRendererObj.getStringWidth(text);
		}
	}

	public int getStringHeight(String text) {
		if (this.customFont) {
			return (int) Fonts.RobotoHUD.getStringWidth(text);
		} else {
			return mc.fontRendererObj.getStringWidth(text);
		}
	}

	public String getDirectionX(int myAngle) {
		String toReturn = "";
		if (!this.showDirection) {
			return "";
		}
		switch (this.myAngle) {
		case 1:
			toReturn = "+";
			break;
		case 2:
			toReturn = "+";
			break;
		case 3:
			toReturn = "+";
			break;
		case 5:
			toReturn = "-";
			break;
		case 6:
			toReturn = "-";
			break;
		case 7:
			toReturn = "-";
			break;
		default:
			toReturn = "";
			break;
		}
		return toReturn;
	}

	public String getDirectionY(int myAngle) {
		if (!this.showCompass) {
			return "";
		}
		return myCardinalPoint[myAngle];
	}

	public String getDirectionZ(int myAngle) {
		String toReturn = "";
		if (!this.showDirection) {
			return "";
		}
		switch (this.myAngle) {
		case 0:
			toReturn = "-";
			break;
		case 1:
			toReturn = "-";
			break;
		case 3:
			toReturn = "+";
			break;
		case 4:
			toReturn = "+";
			break;
		case 5:
			toReturn = "+";
			break;
		case 7:
			toReturn = "-";
			break;
		default:
			toReturn = "";
			break;
		}
		return toReturn;
	}

	public void drawString(String string, double posX, double posY, Color color) {
		if(customFont) {
			hud.setWidth((int)Fonts.RobotoHUD.getStringWidth(string) + 16);
			hud.setHeight((int)Fonts.RobotoHUD.getStringHeight(string) + 7);
			
			if(isUsingStaticChroma) {
				DrawUtils.drawCustomFontChromaString(Fonts.RobotoHUD, string, (int) (posX), (int) posY, true, true);
			} else if(isUsingWaveChroma) {
				DrawUtils.drawCustomFontChromaString(Fonts.RobotoHUD, string, (int) (posX), (int) posY, false, true);
			} else {
				Fonts.RobotoHUD.drawString(string,(int) (posX), (int)posY, color.getRGB());
			}
		} else {
			hud.setWidth(mc.fontRendererObj.getStringWidth(string) + 16);
			hud.setHeight(mc.fontRendererObj.FONT_HEIGHT + 9);
			

			if(isUsingStaticChroma) {
				DrawUtils.drawChromaString(string, posX, posY + 3, true ,true);
			} else if(isUsingWaveChroma) {
				DrawUtils.drawChromaString(string, posX, posY+ 3, false ,true);
			} else {
				mc.fontRendererObj.drawStringWithShadow(string, (float) (posX), (float) posY+ 3, color.getRGB());
			}
		}
	}

	public void renderItemStack(ItemStack stack, int x, int y) {
		RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
		itemRender.zLevel = (float) (-50.0F);
		renderIcon(stack, x, y, 8, 8);

	}

	private void renderIcon(ItemStack stack, int vertexX, int vertexY, int intU, int intV) {
		try {
			IBakedModel iBakedModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
			TextureAtlasSprite textureAtlasSprite = Minecraft.getMinecraft().getTextureMapBlocks()
					.getAtlasSprite(iBakedModel.getTexture().getIconName());
			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldRenderer = tessellator.getWorldRenderer();
			worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
			worldRenderer.pos((double) (vertexX), (double) (vertexY + intV), 0.0D)
					.tex((double) textureAtlasSprite.getMinU(), (double) textureAtlasSprite.getMaxV()).endVertex();
			worldRenderer.pos((double) (vertexX + intU), (double) (vertexY + intV), 0.0D)
					.tex((double) textureAtlasSprite.getMaxU(), (double) textureAtlasSprite.getMaxV()).endVertex();
			worldRenderer.pos((double) (vertexX + intU), (double) (vertexY), 0.0D)
					.tex((double) textureAtlasSprite.getMaxU(), (double) textureAtlasSprite.getMinV()).endVertex();
			worldRenderer.pos((double) (vertexX), (double) (vertexY), 0.0D)
					.tex((double) textureAtlasSprite.getMinU(), (double) textureAtlasSprite.getMinV()).endVertex();
			tessellator.draw();
		} catch (Exception e) {
		}
	}

	public void render() {
		if (!this.mc.gameSettings.showDebugInfo) {
			this.renderPlayerCoords();
		}
	}
}
