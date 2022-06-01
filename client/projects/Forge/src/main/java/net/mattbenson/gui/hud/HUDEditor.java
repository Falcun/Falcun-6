package net.mattbenson.gui.hud;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.input.Mouse;

import net.mattbenson.Falcun;
import net.mattbenson.config.Config;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.Menu;
import net.mattbenson.gui.framework.MinecraftMenuImpl;
import net.mattbenson.gui.framework.draw.DrawImpl;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.types.hud.Image1;
import net.mattbenson.modules.types.hud.Image2;
import net.mattbenson.modules.types.hud.Image3;
import net.mattbenson.modules.types.hud.Image4;
import net.mattbenson.modules.types.hud.Image5;
import net.mattbenson.modules.types.hud.Text1;
import net.mattbenson.modules.types.hud.Text2;
import net.mattbenson.modules.types.hud.Text3;
import net.mattbenson.modules.types.hud.Text4;
import net.mattbenson.modules.types.hud.Text5;
import net.mattbenson.modules.types.mods.Gui;
import net.mattbenson.utils.AssetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class HUDEditor extends MinecraftMenuImpl implements DrawImpl {
	private final static Menu menu = new Menu("", Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
	private final static ResourceLocation SETTINGS = AssetUtils.getResource("/gui/settings.png");

	private final static int HIGLIGHT = new Color(255, 255, 255, 200).getRGB();
	private final static int HELPER = new Color(200, 200, 200, 150).getRGB();

	private final static int BACKGROUND = new Color(200, 200, 200, 100).getRGB();
	private final static int BORDER = new Color(225, 225, 225, 200).getRGB();
	private final static int RESIZE = new Color(50, 50, 50, 200).getRGB();
	private final static int TEXT_COLOR = new Color(255, 255, 255, 255).getRGB();

	private final static int BACKGROUND_HIDDEN = new Color(200, 0, 0, 50).getRGB();
	private final static int BORDER_HIDDEN = new Color(225, 0, 0, 100).getRGB();

	private final static int HOVER_TEXT = new Color(200, 200, 200, 255).getRGB();
	private final static int HOVER_BACKGROUND = new Color(75, 75, 75, 255).getRGB();
	private final static int HOVER_BORDER = new Color(0, 0, 0, 255).getRGB();

	private final static int SCALE = 2;
	private final static int HELPER_THICKNESS = 2;
	private final static int RESIZE_SIZE = 10;
	private final static int SETTINGS_SIZE = 16;
	private final static int SNAP_SENS = 3;

	private HUDElement selected;
	private boolean resizing;
	private boolean moving;
	private boolean mouseDown;
	private boolean mouseDownCache;
	private boolean rightMouseDown;
	private boolean mouseDownRightCache;
	private int cachedX;
	private int cachedY;
	private double originalScale;

	public HUDEditor(Module feature) {
		super(feature, menu);
	}

	@Override
	public void initGui() {
		menu.setLocation(0, 0);
		menu.setWidth(mc.displayWidth);
		menu.setHeight(mc.displayHeight);

		super.initGui();
	}

	@Override
	protected void mouseClicked(int mX, int mY, int mouseButton) throws IOException {
		if (mouseButton == 0) {
			mouseDown = true;
		} else if (mouseButton == 1) {
			rightMouseDown = true;
		}

		super.mouseClicked(mX, mY, mouseButton);
	}

	@Override
	public void drawScreen(int mX, int mY, float partialTicks) {
		GlStateManager.pushMatrix();
		float value = guiScale / new ScaledResolution(mc).getScaleFactor();
		GlStateManager.scale(value, value, value);

		drawRectFalcun(menu.getX() + menu.getWidth() / 2 - HELPER_THICKNESS / 2, 0, HELPER_THICKNESS, menu.getHeight(), HELPER);
		drawRectFalcun(0, menu.getHeight() / 2 - HELPER_THICKNESS / 2, menu.getWidth(), HELPER_THICKNESS, HELPER);

		String line1 = "Left click to control components.".toUpperCase();
		String line2 = "Right click a component to toggle visibility.".toUpperCase();
		int line1Width = getStringWidth(line1);
		int line2Width = getStringWidth(line2);
		int line1Height = getStringHeight(line1);
		int line2Height = getStringHeight(line2);

		int boxWidth = Math.max(line1Width, line2Width) + 20;
		int boxHeight = line1Height + line2Height + 4;

		int heightOffset = 10;

		int boxX = menu.getX() + menu.getWidth() / 2 - boxWidth / 2;
		int boxY = heightOffset;

		drawHorizontalLine(boxX, boxY, boxWidth + 1, 1, HOVER_BORDER);
		drawVerticalLine(boxX, boxY + 1, boxHeight - 1, 1, HOVER_BORDER);
		drawHorizontalLine(boxX, boxY + boxHeight, boxWidth + 1, 1, HOVER_BORDER);
		drawVerticalLine(boxX + boxWidth, boxY + 1, boxHeight - 1, 1, HOVER_BORDER);

		drawRectFalcun(boxX + 1, boxY + 1, boxWidth - 1, boxHeight - 1, HOVER_BACKGROUND);

		int mouseX = Math.round((float) mX / value);
		int mouseY = Math.round((float) mY / value);

		drawText(line1, boxX + boxWidth / 2 - line1Width / 2, boxY + 2, HOVER_TEXT);
		drawText(line2, boxX + boxWidth / 2 - line2Width / 2, boxY + line1Height + 2, HOVER_TEXT);


		if (selected != null || !mouseDownCache) {
			for (HUDElement element : Falcun.getInstance().hudManager.getElements()) {
				if (!element.getParent().isEnabled()) {
					continue;
				}

				int x = Math.round(element.getX() * SCALE);
				int y = Math.round(element.getY() * SCALE);
				int width = Math.round(element.getWidth() * SCALE * (float) (element.getScale()));
				int height = Math.round(element.getHeight() * SCALE * (float) (element.getScale()));

				int border = element.isVisible() ? BORDER : BORDER_HIDDEN;
				int background = element.isVisible() ? BACKGROUND : BACKGROUND_HIDDEN;

				drawHorizontalLine(x, y, width + 1, 1, border);
				drawVerticalLine(x, y + 1, height - 1, 1, border);
				drawHorizontalLine(x, y + height, width + 1, 1, border);
				drawVerticalLine(x + width, y + 1, height - 1, 1, border);

				drawRectFalcun(x, y, width, height, background);

				boolean hover = !(mouseX < x || mouseX > x + width || mouseY < y || mouseY > y + height);

				if (mouseX < x || mouseX > x + width || mouseY < y || mouseY > y + height) {
					if (selected != element) {
						continue;
					}
				}

				if (selected != null && (selected != element)) {
					continue;
				}

				if (height > SETTINGS_SIZE) {
					if (element.getParent().getName().equals("Text1") ||
						element.getParent().getName().equals("Text2") ||
						element.getParent().getName().equals("Text3") ||
						element.getParent().getName().equals("Text4") ||
						element.getParent().getName().equals("Text5") ||
						element.getParent().getName().equals("Image1") ||
						element.getParent().getName().equals("Image2") ||
						element.getParent().getName().equals("Image3") ||
						element.getParent().getName().equals("Image4") ||
						element.getParent().getName().equals("Image5")) {

					} else {
						drawImage(SETTINGS, x + 1, y + height - SETTINGS_SIZE, SETTINGS_SIZE, SETTINGS_SIZE);
					}
				}

				int resizeSize = RESIZE_SIZE;
				if (height < 10) {
					resizeSize = height - 1;
				}

				drawBottomRect(x + width - resizeSize, y + height - resizeSize, resizeSize, resizeSize, RESIZE);

				double dScale = element.getScale() * 100;

				String scale = ((Math.round(dScale) / 100F) + "x").replace(".0x", "x");
				String text = element.getParent().getName().toUpperCase() + "(" + scale + ")";

				int tHeight = Math.round(getStringHeight(text) * SCALE);
				int tWidth = Math.round(getStringWidth(text) * SCALE);
				int tX = x + width / 2 - getStringWidth(text) / 2;
				int tY = y;

				if (tY - tHeight >= 0) {
					tY -= tHeight - 5;
				} else if (tY + height + tHeight < menu.getHeight()) {
					tY += height + 5;
				}

				drawText(text, tX, tY, TEXT_COLOR);

				for (HUDElement e : Falcun.getInstance().hudManager.getElements()) {
					if (!e.getParent().isEnabled()) {
						continue;
					}

					if (e == element) {
						continue;
					}

					int sX = Math.round(e.getX() * SCALE);
					int sY = Math.round(e.getY() * SCALE);
					int sWidth = Math.round(e.getWidth() * SCALE * (float) (e.getScale()));
					int sHeight = Math.round(e.getHeight() * SCALE * (float) (e.getScale()));

					if (moving) {
						if (Math.abs(sX - x) <= SNAP_SENS || Math.abs(sX - (x + width)) <= SNAP_SENS) {
							drawRectFalcun(sX - HELPER_THICKNESS / 2, 0, HELPER_THICKNESS, menu.getHeight(), HELPER);
						} else if (Math.abs(x - (sX + sWidth)) <= SNAP_SENS || Math.abs((sX + sWidth) - (x + width)) <= SNAP_SENS) {
							drawRectFalcun(sX + sWidth - HELPER_THICKNESS / 2, 0, HELPER_THICKNESS, menu.getHeight(), HELPER);
						}

						if (sY == y || Math.abs(sY - (y + height)) <= SNAP_SENS) {
							drawRectFalcun(0, sY - HELPER_THICKNESS / 2, menu.getWidth(), HELPER_THICKNESS, HELPER);
						} else if (Math.abs(y - (sY + sHeight)) <= SNAP_SENS || Math.abs((sY + sHeight) - (y + height)) <= SNAP_SENS) {
							drawRectFalcun(0, sY + sHeight - HELPER_THICKNESS / 2, menu.getWidth(), HELPER_THICKNESS, HELPER);
						}
					}
				}

				if (rightMouseDown && !mouseDownRightCache) {
					element.setVisible(!element.isVisible());
				} else if (mouseDown && !mouseDownCache) {
					selected = element;

					if (mouseX < x + SETTINGS_SIZE && mouseY > y + height - SETTINGS_SIZE) {
						if (height > SETTINGS_SIZE) {
							if (element.getParent().getName().equals("Text1") ||
								element.getParent().getName().equals("Text2") ||
								element.getParent().getName().equals("Text3") ||
								element.getParent().getName().equals("Text4") ||
								element.getParent().getName().equals("Text5") ||
								element.getParent().getName().equals("Image1") ||
								element.getParent().getName().equals("Image2") ||
								element.getParent().getName().equals("Image3") ||
								element.getParent().getName().equals("Image4") ||
								element.getParent().getName().equals("Image5")) {
							} else {
								Gui gui = Falcun.getInstance().moduleManager.getModule(Gui.class);
								gui.menuImpl.openSettings(element.getParent());
								gui.setEnabled(true);
							}
						}
					} else if (mouseX > x + width - RESIZE_SIZE && mouseY > y + height - RESIZE_SIZE) {
						cachedX = mouseX / SCALE;
						cachedY = mouseY / SCALE;
						originalScale = selected.getScale();

						resizing = true;
					} else {
						cachedX = (selected.getX() - mouseX / SCALE);
						cachedY = (selected.getY() - mouseY / SCALE);

						moving = true;
					}
				}
			}
		}

		GlStateManager.popMatrix();

		super.drawScreen(mouseX, mouseY, partialTicks);

		mouseDownCache = mouseDown;
		mouseDownRightCache = rightMouseDown;

		if (mouseDown) {
			mouseDown = Mouse.isButtonDown(0);
		}

		if (rightMouseDown) {
			rightMouseDown = Mouse.isButtonDown(1);
		}


		if (mouseDown) {
			if (selected != null) {
				if (resizing) {
					int xDelta = mouseX / SCALE - cachedX;
					int yDelta = mouseY / SCALE - cachedY;

					double maxDelta = 1 + Math.max(xDelta, yDelta) / 30D;
					double delta = maxDelta * originalScale;

					if (delta > 3) {
						delta = 3;
					} else if (delta < 0.5) {
						delta = 0.5;
					}

					selected.setScale(delta);
				} else if (moving) {
					int xDelta = mouseX / SCALE + cachedX;
					int yDelta = mouseY / SCALE + cachedY;

					int x = xDelta;
					int y = yDelta;
					int width = Math.round(selected.getWidth() * (float) (selected.getScale()));
					int height = Math.round(selected.getHeight() * (float) (selected.getScale()));

					for (HUDElement e : Falcun.getInstance().hudManager.getElements()) {
						if (!e.getParent().isEnabled()) {
							continue;
						}

						if (e == selected) {
							continue;
						}

						int sX = Math.round(e.getX());
						int sY = Math.round(e.getY());
						int sWidth = Math.round(e.getWidth() * (float) (e.getScale()));
						int sHeight = Math.round(e.getHeight() * (float) (e.getScale()));

						if (Math.abs(sX - x) <= SNAP_SENS) {
							xDelta = sX;
						} else if (Math.abs(sX - (x + width)) <= SNAP_SENS) {
							xDelta = sX - width;
						} else if (Math.abs(x - (sX + sWidth)) <= SNAP_SENS) {
							xDelta = sX + sWidth;
						} else if (Math.abs((sX + sWidth) - (x + width)) <= SNAP_SENS) {
							xDelta = sX + sWidth - width;
						}

						if (Math.abs(sY - y) <= SNAP_SENS) {
							yDelta = sY;
						} else if (Math.abs(sY - (y + height)) <= SNAP_SENS) {
							yDelta = sY - height;
						} else if (Math.abs(y - (sY + sHeight)) <= SNAP_SENS) {
							yDelta = sY + sHeight;
						} else if (Math.abs((sY + sHeight) - (y + height)) <= SNAP_SENS) {
							yDelta = sY + sHeight - height;
						}
					}

					if (xDelta < 0) {
						xDelta = 0;
					} else if (xDelta + selected.getWidth() * selected.getScale() > menu.getWidth() / SCALE) {
						xDelta = Math.round(menu.getWidth() / SCALE - selected.getWidth() * (float) selected.getScale());
					}

					if (yDelta < 0) {
						yDelta = 0;
					} else if (yDelta + selected.getHeight() * selected.getScale() > menu.getHeight() / SCALE) {
						yDelta = Math.round(menu.getHeight() / SCALE - selected.getHeight() * (float) selected.getScale());
					}

					selected.setX(xDelta);
					selected.setY(yDelta);
				}
			}
		} else {
			selected = null;
			resizing = false;
			moving = false;
		}
	}

	@Override
	public void drawText(String text, int x, int y, int color) {
		Fonts.Roboto.drawString(text, x, y, color);
	}

	@Override
	public int getStringWidth(String string) {
		return Fonts.Roboto.getStringWidth(string);
	}

	@Override
	public int getStringHeight(String string) {
		return Fonts.Roboto.getStringHeight(string);
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();

		Image1.imageRun();
		Image2.imageRun();
		Image3.imageRun();
		Image4.imageRun();
		Image5.imageRun();
		Text1.imageRun();
		Text2.imageRun();
		Text3.imageRun();
		Text4.imageRun();
		Text5.imageRun();

		new Thread(() -> {
			Config config = Falcun.getInstance().configManager.getLoadedConfig();

			if (config != null) {
				config.save();
			}
		}).start();
	}
}
