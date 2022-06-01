package net.mattbenson.modules.types.other;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.input.KeyDownEvent;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.input.KeybindManager;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.utils.DrawUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class BlockCoordinates extends Module {
	@ConfigValue.Boolean(name = "Show Name")
	private boolean showName = true;
	
	@ConfigValue.Boolean(name = "Show Icon")
	private boolean showIcon = true;
	
	@ConfigValue.Boolean(name = "Custom Font")
	private boolean customFont = false;
	
	@ConfigValue.Boolean(name = "Background")
	private boolean shadedCoords = true;
	
	@ConfigValue.Keybind(name = "Shout Coordinates Key")
	private int keyBind = 0;
	
	@ConfigValue.Text(name = "Chat Format")
	private String chatFormat = "X: {x}, Y: {y}, Z: {z} Block: {block}";
	
	@ConfigValue.Color(name = "Background Color")
	private Color background = new Color(0, 0, 0, 150);
	
	@ConfigValue.Color(name = "Label Color")
	private Color color = Color.WHITE;
	
	@ConfigValue.Color(name = "Value Color")
	private Color vColor = Color.WHITE;
	
	@ConfigValue.Boolean(name = "Static Chroma")
	private boolean isUsingStaticChroma = false;
	
	@ConfigValue.Boolean(name = "Wave Chroma")
	private boolean isUsingWaveChroma = false;
	
	private HUDElement hud;
	private int width = 70;
	private int height = 30;
	
	public BlockCoordinates() {
		super("Block Coords", ModuleCategory.OTHER);
		
		hud = new HUDElement("coords", width, height) {
			@Override
			public void onRender() {
				render();
			}
		};
		
		hud.setX(1);
		hud.setY(175);
		
		addHUD(hud);
	}
	
	@SubscribeEvent
	public void onKeyPress(KeyDownEvent event) {
		if (keyBind == 0)
			return;
		
		if(KeybindManager.isInvalidScreen(mc.currentScreen)) {
			return;
		}
		
		if (event.getKey() == keyBind) {
			MovingObjectPosition mop = mc.objectMouseOver;
			if (mop != null) {
				if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK) {
					String toSend = chatFormat.replace("{x}", myPosX + "").replace("{y}", myPosY + "")
							.replace("{z}", myPosZ + "").replace("{block}",
									mc.theWorld.getBlockState(mop.getBlockPos()).getBlock().getLocalizedName());
					mc.thePlayer.sendChatMessage(toSend);
				}
			}
		}
	}
			
	int myPosX;
	int myPosY;
	int myPosZ;

	private void renderBlockCoords(Block block, BlockPos pos) {
		int stringWidth = 0;
		myPosX = pos.getX();
		myPosY = pos.getY();
		myPosZ = pos.getZ();
		if(Math.abs(myPosX) > Math.abs(myPosZ) && Math.abs(myPosX) > Math.abs(myPosY)) {
			stringWidth = getStringWidth(myPosX +"");
		}
		else if(Math.abs(myPosZ) > Math.abs(myPosX) && Math.abs(myPosZ) > Math.abs(myPosY)) {
			stringWidth = getStringWidth(myPosZ +"");
		} else {
			stringWidth = getStringWidth(myPosY +"");
		}


		int width = 20;
		int height = 12*3 + 6;
		if(Math.abs(myPosX) > Math.abs(myPosZ) && Math.abs(myPosX) > Math.abs(myPosY)) {
			stringWidth = getStringWidth(myPosX +"");
		}
		else if(Math.abs(myPosZ) > Math.abs(myPosX) && Math.abs(myPosZ) > Math.abs(myPosY)) {
			stringWidth = getStringWidth(myPosZ +"");
		} else {
			stringWidth = getStringWidth(myPosY +"");
		}
		
		if(this.showName) {
			if(stringWidth < getStringWidth("Block: " + block.getLocalizedName())) {
				stringWidth = getStringWidth("Block: " + block.getLocalizedName());
			} 
			height += 12;
		}

		if(this.showIcon) {
			stringWidth += 16;
		}
		width += stringWidth;

		
		int shift = 0;

		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		int posY = hud.getY();
		int posX = hud.getX();
		
		if (this.shadedCoords) {	
			DrawUtils.drawGradientRect(posX, posY, posX + width, posY + height, background.getRGB(), background.getRGB());
		} 

		if(block != null) {

			drawString("BX: ", posX + 3, posY+3+shift,color);
			drawString(myPosX + "", posX + 22, posY+3+shift,vColor);
			shift += 12;

			drawString("BY: ", posX + 3, posY+3+shift,color);
			drawString(myPosY + "", posX + 22, posY+3+shift,vColor);
			shift += 12;
		
			drawString("BZ: ", posX + 3, posY+3+shift,color);
			drawString(myPosZ + "", posX + 22, posY+3+shift,vColor);
			shift += 12;
			if(showName) {
				if(this.showIcon) {
					renderItemStack(new ItemStack(Item.getItemFromBlock(block)),(int)posX+width - 16, (int)posY+5+shift);
				}
			drawString("Block: ", posX + 3, posY+3+shift,color);
			drawString(block.getLocalizedName(), posX + 3 + getStringWidth("Block: "), posY+3+shift,new Color(block.getBlockColor()));
			}
			hud.setWidth(width);
			hud.setHeight(height);

		}
		GL11.glScaled(1,1,1);
		GL11.glColor3f(1, 1, 1);
		GL11.glPopMatrix();
	}

	public void render() {
		if (!this.mc.gameSettings.showDebugInfo) {
			MovingObjectPosition mop = mc.objectMouseOver;
			if (mop != null) {
				if(mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK) {
					this.renderBlockCoords(mc.theWorld.getBlockState(mop.getBlockPos()).getBlock(),mop.getBlockPos());
				}
			}
		}
	}
	
	public void drawString(String text, int posX, int posY, Color color) {
		if(this.customFont) {
			if(isUsingStaticChroma) {
				DrawUtils.drawCustomFontChromaString(Fonts.RobotoHUD,text, posX , posY-2, true, true);
			} else if(isUsingWaveChroma) {
				DrawUtils.drawCustomFontChromaString(Fonts.RobotoHUD,text, posX, posY-2, false, true);
			} else {
				Fonts.RobotoHUD.drawString(text, posX, posY-2, color.getRGB());
			}
		} else {
			if(isUsingStaticChroma) {
				DrawUtils.drawChromaString(text, posX+2, posY+2, true, true);
			} else if(isUsingWaveChroma) {
				DrawUtils.drawChromaString(text, posX+2, posY+2, false, true);
			} else {
				mc.fontRendererObj.drawStringWithShadow(text,  (float)posX + 2, (float) posY+2, color.getRGB());
			}
		}
	}

	public int getStringWidth(String text) {
		if(this.customFont) {
			return (int) Fonts.RobotoHUD.getStringWidth(text);
		} else {
			return mc.fontRendererObj.getStringWidth(text);
		}
	}

	public int getStringHeight(String text) {
		if(this.customFont) {
			return (int) Fonts.RobotoHUD.getStringWidth(text);
		} else {
			return mc.fontRendererObj.getStringWidth(text);
		}
	}

	public void renderItemStack(ItemStack stack, int x, int y) {

		RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
		itemRender.zLevel = (float) (-50.0F);
		renderIcon(stack,x,y,8,8);

	}

	private void renderIcon(ItemStack stack, int vertexX, int vertexY, int intU, int intV) {
		try {
			IBakedModel iBakedModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
			TextureAtlasSprite textureAtlasSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(iBakedModel.getTexture().getIconName());
			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldRenderer = tessellator.getWorldRenderer();
			worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
			worldRenderer.pos((double)(vertexX), 		(double)(vertexY + intV), 	0.0D).tex((double) textureAtlasSprite.getMinU(), (double) textureAtlasSprite.getMaxV()).endVertex();
			worldRenderer.pos((double)(vertexX + intU), (double)(vertexY + intV),	0.0D).tex((double) textureAtlasSprite.getMaxU(), (double) textureAtlasSprite.getMaxV()).endVertex();
			worldRenderer.pos((double)(vertexX + intU), (double)(vertexY), 			0.0D).tex((double) textureAtlasSprite.getMaxU(), (double) textureAtlasSprite.getMinV()).endVertex();
			worldRenderer.pos((double)(vertexX), 		(double)(vertexY), 			0.0D).tex((double) textureAtlasSprite.getMinU(), (double) textureAtlasSprite.getMinV()).endVertex();
			tessellator.draw();
		} catch (Exception e) {}
	}

}
