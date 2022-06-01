package net.mattbenson.modules.types.render;

import java.awt.Color;
import java.lang.annotation.ElementType;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.GL11;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.network.IngoingPacketEvent;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.utils.DrawUtils;
import net.mattbenson.utils.legacy.HUDUtil;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

public class TPS extends Module {
	@ConfigValue.Boolean(name = "Preset Color")
	private boolean presetColor = true;
	
	@ConfigValue.Color(name = "TPS Color")
	private Color tpsColor = Color.WHITE;
	
	@ConfigValue.List(name = "Mode", values = {"Simple", "Number", "Graph", "Both"})
	private String mode = "Number";
		
	@ConfigValue.Boolean(name = "Background")
	private boolean backGround = true;
	
	@ConfigValue.Color(name = "Background Color")
	private Color bColor = new Color(0, 0, 0, 150);
	
	@ConfigValue.Boolean(name = "Custom Font")
	private boolean customFont = false;
	
	@ConfigValue.Boolean(name = "Static Chroma")
	private boolean isUsingStaticChroma = false;
	
	@ConfigValue.Boolean(name = "Wave Chroma")
	private boolean isUsingWaveChroma = false;
	
	private List<Float> lastMeasurements = new CopyOnWriteArrayList<>();
	private long lastTime = 0;
	private boolean first = true;
	private float tps;
	private Color color;
	private int count;
	
	private HUDElement hud;
	private int width = 56;
	private int height = 18;
	
	public TPS() {
		super("TPS", ModuleCategory.RENDER);
		
		hud = new HUDElement("tps", width, height) {
			@Override
			public void onRender() {
				render();
			}
		};
		
		addHUD(hud);
	}
	
	@SubscribeEvent
	public void onPacket(IngoingPacketEvent event) {
		if (event.getPacket() instanceof S03PacketTimeUpdate) {
			if(lastTime != 0) {
                if(first) {
                	first = false;
                    return;
                }
                float tps = 20 * (1000F / (System.currentTimeMillis() - lastTime));
                if(tps > 20) {
                	tps = 20;
                }
                if(lastMeasurements.isEmpty()) {
                	for(int i =0; i < 49; i++) {
                		lastMeasurements.add((float) 20);
                	}
                }
                if(lastMeasurements.size() > 56)
                	lastMeasurements.remove(0);
                lastMeasurements.add(tps);
            }
    		lastTime = System.currentTimeMillis();
		}
	}
	
	public void render() {
		GL11.glPushMatrix();
		
		int posX = hud.getX();
		int posY = hud.getY();
		
		try {
		if (lastMeasurements.size() > 0) {
			Float value = lastMeasurements.get(lastMeasurements.size() - 1);
			if(value != null) {
				if (value < 17 && value >= 15)
					color = Color.YELLOW;
				else if (value < 15 && value >= 12)
					color = Color.ORANGE;
				else if (value < 12)
					color = Color.RED;
				else
					color = Color.GREEN;
					tps = value;
			} else {
				color = Color.GREEN;
				tps = 20;
			}
		} else {
			color = Color.GREEN;
			tps = 20;
		}
		} catch (Exception e) {
			
		}
		

	
		String string = (int) tps + " TPS";
		
		if(mode.equalsIgnoreCase("Number")) {
			
			if (backGround) 
			DrawUtils.drawGradientRect(hud.getX(), hud.getY(), hud.getX() + width, hud.getY() + height, bColor.getRGB(), bColor.getRGB());
			
			posY = hud.getY() + 2;
			posX = hud.getX() + 11;
			
			hud.setWidth(mc.fontRendererObj.getStringWidth(string) + 16);
			hud.setHeight(mc.fontRendererObj.FONT_HEIGHT + 9);
			
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
		
		if(mode.equalsIgnoreCase("Graph")) {
			if (backGround) 
				DrawUtils.drawGradientRect(hud.getX(), hud.getY(), hud.getX() + width, hud.getY() + height, bColor.getRGB(), bColor.getRGB());
		} 
		if(mode.equalsIgnoreCase("Both")) {
			
			if (backGround) 
			DrawUtils.drawGradientRect(hud.getX(), hud.getY(), hud.getX() + width, hud.getY() + height + 5, bColor.getRGB(), bColor.getRGB());
			
			posY = hud.getY() + 2;
			posX = hud.getX() + 11;
			
			
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
			
			posY = hud.getY();
			posX = hud.getX();
			
		}
		
		if(mode.equalsIgnoreCase("Both")) {
			hud.setHeight((int) ( 10 + height));
		} else {
			hud.setHeight(height);
		}
		hud.setWidth(width);
		
		if(mode.equalsIgnoreCase("Simple")) {
			HUDUtil.drawString("[" + string + "]", posX, posY, presetColor ? color : tpsColor, false);
			hud.setHeight((int)mc.fontRendererObj.FONT_HEIGHT);
			hud.setWidth(mc.fontRendererObj.getStringWidth("[" + string + "]"));
		}
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glPopMatrix();
		
		
		float x = 14;
		
		float lastValue = 0;
		int i = 0;


		if(mode.equalsIgnoreCase("Graph") ||mode.equalsIgnoreCase("Both")) {
			GL11.glPushMatrix();

			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_TEXTURE_2D);

			GL11.glLineWidth(1F);
			for(float value : lastMeasurements) {
				GL11.glBegin(GL11.GL_LINES);
				{
					Color color;
					if (value < 17 && value >= 15)
						color = Color.YELLOW;
					else if (value < 15 && value >= 12)
						color = Color.ORANGE;
					else if (value < 12)
						color = Color.RED;
					else
						color = Color.GREEN;

					GL11.glColor3f(color.getRed(), color.getGreen(),
							color.getBlue());

					if(mode.equalsIgnoreCase("Graph")) {
						if (i > 0) {
							GL11.glVertex2f((float) (70+posX) - (x - 1), (float)(posY + 20 + (height/2)) - lastValue);
							GL11.glVertex2f((float)(70+posX) - x, (float)(posY + 20 + (height/2)) - value);
						} else {
							GL11.glVertex2f((float)(70+posX) - x, (float)(posY + 20 + (height/2)) - value);
							GL11.glVertex2f((float)(70+posX) - x, (float)(posY + 20 + (height/2)) - value);
						}	 
					} else {
						if (i > 0) {
							GL11.glVertex2f((float)(70+posX) - (x - 1), (float)(posY + 20 + height) - lastValue);
							GL11.glVertex2f((float)(70+posX) - x, (float)(posY + 20 + height) - value);
						} else {
							GL11.glVertex2f((float)(70+posX) - x, (float)(posY + 20 + height) - value);
							GL11.glVertex2f((float)(70+posX) - x, (float)(posY + 20  + height) - value);
						}
					}

				}
				GL11.glEnd();
				lastValue = value;
				x += 1;
				i++;
			}

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glColor4f(1F, 1F, 1F, 1F);
			GL11.glPopMatrix();	
		}
		
	}
}
