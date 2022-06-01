package net.mattbenson.modules.types.mods;

import java.awt.Color;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.input.MouseDownEvent;
import net.mattbenson.events.types.input.MouseMoveEvent;
import net.mattbenson.events.types.world.OnTickEvent;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.utils.ClickCounter;
import net.mattbenson.utils.DrawUtils;
import net.minecraft.util.MathHelper;

public class MouseHUD extends Module {
	private final ClickCounter leftClickCounter = new ClickCounter();

	private static long lastEvent;
	public static float dx;
	public static float dy;
	public static float mX = 0;
	public static float mY = 0;

	float mouseMultiH = 4.0f;
	float mouseMultiV = 2.5f;
	float smoothX = 0.7F;
	float smoothY = 0.4F;
	
	@ConfigValue.Color(name = "Background Color")
	private Color background = new Color(0, 0, 0, 150);
	
	@ConfigValue.Color(name = "Mouse Color")
	private Color mouseColor = Color.BLACK;
	
	@ConfigValue.Color(name = "Center Color")
	private Color centerColor = Color.WHITE;
	
	@ConfigValue.Color(name = "Sidebar Color")
	private Color sideBarColor = Color.BLACK;
	
	@ConfigValue.Color(name = "Font Color")
	private Color fontColor = Color.WHITE;
	
	@ConfigValue.List(name = "Mode", values = {"Left", "Right"})
	private String Mode = "Right";
	
	private HUDElement hud;
	private int width = 114;
	private int height = 41;
	
	public MouseHUD() {
		super("Mouse HUD", ModuleCategory.MODS);
		
		hud = new HUDElement("hud", width, height) {
			@Override
			public void onRender() {
				render();
			}
		};
		
		hud.setX(1);
		hud.setY(190);
		
		addHUD(hud);
	}
	
	public void render() {
		if (mc.gameSettings.showDebugInfo) {
			return;
		}

		GL11.glPushMatrix();
		float posY = hud.getY();
		float posX = hud.getX();
		
		if(this.Mode.equalsIgnoreCase("Right")) {
			/*
			 * Background
			 */
			DrawUtils.drawGradientRect(posX, posY, posX + 70, posY + 21, background.getRGB(), background.getRGB());
			DrawUtils.drawGradientRect(posX+70, posY, posX + 114, posY + 40.8,background.getRGB(), background.getRGB());
			drawSlant((int)posX - 1, (int)posY-9,(int) 45, background.getRGB());
			/*
			 * Font 
			 */
			if(fontColor.getBlue() == 5 && fontColor.getRed() == 5 && fontColor.getGreen() == 5) {
				DrawUtils.drawChromaString(leftClickCounter.getCps() + " CPS", posX + 15, posY + 7, true, true);
			} else if(fontColor.getBlue() == 6 && fontColor.getRed() == 6 && fontColor.getGreen() == 6) {
				DrawUtils.drawChromaString(leftClickCounter.getCps() + " CPS", posX + 15, posY + 7, false, true);
			} else {
				mc.fontRendererObj.drawStringWithShadow(leftClickCounter.getCps() + " CPS", (float) posX + 15, (float) posY + 7, fontColor.getRGB());
			}
			/*
			 * Sidebar
			 */
			DrawUtils.drawGradientRect(posX, posY, posX + 5, posY + 21, sideBarColor.getRGB(), sideBarColor.getRGB());


			DrawUtils.drawCircle((float)posX+90.0F, (float) posY+20.0F, 1, centerColor.getRGB(), 1, true);			
			DrawUtils.drawCircle((float)posX+90+mX, (float)posY+20+mY, 5, mouseColor.getRGB(), 1, false);
		} else {
			/*
			 * Background
			 */
			DrawUtils.drawGradientRect(posX, posY, posX + 44, posY + 40.8, background.getRGB(), background.getRGB());
			DrawUtils.drawGradientRect(posX+44, posY, posX + 114, posY + 21,background.getRGB(), background.getRGB());
			drawSlant((int)posX-7,(int) posY-9, 315, background.getRGB());
			/*
			 * Font 
			 */
			if(fontColor.getBlue() == 5 && fontColor.getRed() == 5 && fontColor.getGreen() == 5) {
				DrawUtils.drawChromaString(leftClickCounter.getCps() + " CPS", posX + 75, posY + 7, true, true);
			} else if(fontColor.getBlue() == 6 && fontColor.getRed() == 6 && fontColor.getGreen() == 6) {
				DrawUtils.drawChromaString(leftClickCounter.getCps() + " CPS", posX + 75, posY + 7, false, true);
			} else {
				mc.fontRendererObj.drawStringWithShadow(leftClickCounter.getCps() + " CPS", (float) posX + 75, (float) posY + 7, fontColor.getRGB());
			}
			/*
			 * Sidebar
			 */
			DrawUtils.drawGradientRect(posX + 109, posY, posX + 114,posY + 21, sideBarColor.getRGB(), sideBarColor.getRGB());

			DrawUtils.drawCircle((float) posX+24, (float) posY+20.0F, 1, centerColor.getRGB(), 1, true);
			DrawUtils.drawCircle((float) posX+24+mX, (float) posY+20+mY, 5, mouseColor.getRGB(), 1, false);
		}
		GL11.glColor3f(1, 1, 1);
		GL11.glScaled(1, 1, 1);
		GL11.glPopMatrix();
	}
	
	public void drawSlant(int x, int y, double rotation, int paramColor) {
		float alpha = (float)(paramColor >> 24 & 0xFF) / 255F;
		float red = (float)(paramColor >> 16 & 0xFF) / 255F;
		float green = (float)(paramColor >> 8 & 0xFF) / 255F;
		float blue = (float)(paramColor & 0xFF) / 255F;
		GL11.glPushMatrix();
		GL11.glClear(256);
		//GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);

		GL11.glColor4f(red, green, blue, alpha);
		GL11.glScaled(1, 1, 1);
		GL11.glTranslated(x+61, y+40, 0);
		GL11.glRotated(rotation, 0F, 0F, 1.0F);

		GL11.glPushMatrix();

		GL11.glBegin(GL11.GL_TRIANGLES);
		GL11.glVertex2d(0, -13.9);
		GL11.glVertex2d(-13.9, 0);
		GL11.glVertex2d(13.9, 0);
		GL11.glEnd();
		GL11.glPopMatrix();

		GL11.glRotated(-rotation, 0F, 0F, 1.0F);
		GL11.glTranslated(-x - 61, -y - 40, 0);

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		//GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glPopMatrix();
	}
	
	@SubscribeEvent
	public void onClick(MouseDownEvent e) {
		if(e.getButton() == 0) {
			leftClickCounter.onClick();
		}
	}
	
	@SubscribeEvent
	public void onClick(MouseMoveEvent e) {
		dx = Mouse.getEventDX();
		dy = Mouse.getEventDY();
		lastEvent = System.currentTimeMillis();
	}
	
	@SubscribeEvent
	public void update(OnTickEvent event) {
		float circleX = MathHelper.clamp_float(dx * mouseMultiH, -12.0f, 20.0f);
		float circleY = MathHelper.clamp_float(-dy * mouseMultiV, -10.0f, 10.0f);
		
		if (mX < circleX) {
			if (mX + smoothX > circleX) {
				mX = circleX;
			}
			else {
				mX += smoothX;
			}
		}
		else if (mX > circleX) {
			if (mX - smoothX < circleX) {
				mX = circleX;
			}
			else {
				mX -= smoothX;
			}
		}
		if (mY < circleY) {
			if (mY + smoothY > circleY) {
				mY = circleY;
			}
			else {
				mY += smoothY;
			}
		}
		else if (mY > circleY) {
			if (mY - smoothY < circleY) {
				mY = circleY;
			}
			else {
				mY -= smoothY;
			}
		}
		final boolean timeUp = timeSinceLastEvent() >= 45L;
		if (timeUp) {
			dx = 0;
		}
		if (timeUp) {
			dy = 0;
		}
	}

	public static long timeSinceLastEvent() {
		return System.currentTimeMillis() - lastEvent;
	}
}
