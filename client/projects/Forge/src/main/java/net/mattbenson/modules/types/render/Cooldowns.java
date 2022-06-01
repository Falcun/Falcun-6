package net.mattbenson.modules.types.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.utils.Cooldown;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Cooldowns extends Module {
	
	private HUDElement hud;
	private int width = 56;
	private int height = 18;
    private Minecraft mc;
    public static LinkedHashSet<Cooldown> cooldowns;
    public static Cooldown cooldownsArray;
    public int scale = 1;
    public int yBase = 0;
    public int xBase = 0;
    
	@ConfigValue.Boolean(name = "Enterpearl Timer")
	public static boolean ep = true;
	@ConfigValue.Boolean(name = "Crapple Timer")
	public static boolean cp = true;
	@ConfigValue.Boolean(name = "Gapple Timer")
	public static boolean gp = true;
	
	@ConfigValue.Integer(name = "Enterpearl Time", min = 1, max = 25)
	public static int enderpearlTime = 15;
	
	@ConfigValue.Integer(name = "Crapple Time", min = 1, max = 500)
	public static int crappleTime = 120;
	
	@ConfigValue.Integer(name = "Gapple Time", min = 1, max = 500)
	public static int gapplelTime = 300;
    
	@ConfigValue.List(name = "Direction", values = { "Vertical", "Horizontal"})
	public String direction = "Vertical";
	
	@ConfigValue.Integer(name = "Radius", min = 0, max = 50)
	public int radius = 15;
	
	@ConfigValue.Integer(name = "Margin Between", min = 0, max = 50)
	public int margin = 5;
   
	@ConfigValue.Boolean(name = "Display Font")
	public static boolean font = false;
	
	@ConfigValue.Color(name = "Background Color")
	private Color backgroundColor = Color.black;
	
	@ConfigValue.Color(name = "Outline Color")
	private Color outlineColor = Color.white;
	
	@ConfigValue.Color(name = "Timing Color")
	private Color timingColor = Color.red;
	
	@ConfigValue.Color(name = "Text Color")
	private Color textColor = Color.yellow;
	
	public Cooldowns() {
		super("Cooldowns", ModuleCategory.RENDER);
		
		hud = new HUDElement("status", width, height) {
			@Override
			public void onRender() {
				render();
			}
		};
		
		hud.setX(1);
		hud.setY(175);
		
		addHUD(hud);
	}
	
	public void render() {

		
		if (direction.contains("Vertical")) {
			width = 100;
			height = 20;
		} else {
			width = 20;
			height = 100;
		}
		
		hud.setHeight(height);
		hud.setWidth(width);
		
		scale = 1;
		xBase = 0;
		yBase = 0;
        
		for (int i = 0; i < Cooldown.listOfStrings.size(); i++) {
			String[] data = Cooldown.listOfStrings.get(i).split(",");
			long time = Long.parseLong(data[1]);

			long remaining = (Long.parseLong(data[2]) + time);
			float differance = remaining - System.currentTimeMillis() ;
			
			if (differance > 0) {
				
			
                float percent = (float) (((double) differance / (double) (time / 1000)) * 100);
                
                percent = percent / 100000;

                
                if (percent > 0.0f) {
                    GL11.glEnable(3042);
                    GL11.glDisable(3553);
                    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                    final Tessellator tess = Tessellator.getInstance();
                    this.drawFullOval(tess, this.radius, outlineColor.getRGB(), hud.getX() + xBase, hud.getY() + yBase);
                    this.drawPartialOval(tess, this.radius, 1.0f - percent, timingColor.getRGB(), hud.getX() + xBase, hud.getY() + yBase);
                    this.drawFullOval(tess, this.radius - 2, backgroundColor.getRGB(), hud.getX() + xBase, hud.getY() + yBase);
                    GL11.glEnable(3553);
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    this.drawItem(Integer.parseInt(data[0]), hud.getX() + xBase, hud.getY() + yBase, data[3]);
                    if (font) {
                        Fonts.KardinalB.drawCenteredString("" + Math.round((differance / 1000) * 10.0) / 10.0, hud.getX() + xBase, hud.getY() + yBase + 13, textColor.getRGB());
                    }
                }
  

                if (direction.contains("Vertical")) {
                	xBase += 30 + margin;
                } else {
                	yBase += 30 + margin;
                }
                
			} else {
				Cooldown.listOfStrings.remove(i);
			}
		}
			  



	}

	
	
    private void drawPartialOval(final Tessellator tess, final int radius, final float percent, final int color, int x, int y) {
        final WorldRenderer wr = tess.getWorldRenderer();
        final float a = (color >> 24 & 0xFF) / 255.0f;
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        GL11.glColor4f(r, g, b, a);
        wr.begin(6, DefaultVertexFormats.POSITION);
        wr.pos((double)x, (double)y, 0.0).endVertex();
        for (int i = (int)(360.0f * percent); i >= 0; --i) {
            final double sin = Math.sin(Math.toRadians(i));
            final double cos = Math.cos(Math.toRadians(i));
            wr.pos(x + sin * radius, y - cos * radius, 0.0).endVertex();
        }
        tess.draw();
    }
    
    private void drawFullOval(final Tessellator tess, final int radius, final int color, int x, int y) {
        final WorldRenderer wr = tess.getWorldRenderer();
        final float a = (color >> 24 & 0xFF) / 255.0f;
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        GL11.glColor4f(r, g, b, a);
        wr.begin(6, DefaultVertexFormats.POSITION);
        for (int i = 0; i < 360; ++i) {
            final double sin = Math.sin(Math.toRadians(i));
            final double cos = Math.cos(Math.toRadians(i));
            wr.pos(x + sin * radius, y + cos * radius, 0.0).endVertex();
        }
        tess.draw();
    }
    private void drawItem(int itemID, int x, int y, String item) {
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableAlpha();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		RenderHelper.enableGUIStandardItemLighting();
        final ItemStack is;
    	if (item.equals("Gapple")) {
            is = new ItemStack(Item.getItemById(itemID), 0, 1);
    	} else {
            is = new ItemStack(Item.getItemById(itemID));
    	}
    	
        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
        itemRenderer.renderItemAndEffectIntoGUI(is, x - 8, y - 8);
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GL11.glPopMatrix();
    }
}