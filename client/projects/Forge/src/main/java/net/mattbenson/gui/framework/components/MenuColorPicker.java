package net.mattbenson.gui.framework.components;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

import org.lwjgl.input.Mouse;

import net.mattbenson.gui.framework.MenuComponent;
import net.mattbenson.gui.framework.MenuPriority;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class MenuColorPicker extends MenuComponent {
	protected Color color;
	protected Color temp;
	protected Point startPos;
	protected ButtonState lastState = ButtonState.NORMAL;
	protected ResourceLocation colorPickerMain;
	protected boolean mouseDown = false;
	
	protected int lastYPress;
	
	protected int startType = 0;
	protected boolean wantsToDrag = false;
	
	protected boolean mouseDragging = false;
	protected boolean pickingColor = false;
	protected boolean canPick = true;
	protected int size = 80;
	protected int colorOffset = 10;
	protected int alphaOffset = 10;
	protected int pickerWindowWidth = size + colorOffset;
	protected int pickerWindowHeight = size + alphaOffset;
	protected MenuSlider alphaSlider;
	
	public MenuColorPicker(int x, int y, int width, int height, int defaultColor) {
		super(x, y, width, height);
		lastYPress = -1;
		
		Color theColor = new Color(defaultColor, true);
		
		color = theColor;
		temp = theColor;
		
		alphaSlider = new MenuSlider(1f, 0f, 1f, 1, 0, 0, pickerWindowWidth, 10){
			@Override
			public void onAction() {
				color = new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.round(alphaSlider.getValue() * 255));
				MenuColorPicker.this.onAction();
			}
		};
		
		alphaSlider.setValue((float)theColor.getAlpha() / 255);
	}
	
	@Override
	public void onInitColors() {
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(35, 35, 35, 255));
		setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(81, 108, 255, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(50, 50, 50, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(100, 120, 255, 255));
		setColor(DrawType.BACKGROUND, ButtonState.POPUP, new Color(10, 10, 10, 255));	
		setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(100, 100, 100, 255));	

		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(10, 10, 10, 255));
		setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(10, 10, 10, 255));
		setColor(DrawType.LINE, ButtonState.HOVER, new Color(10, 10, 10, 255));
		setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(10, 10, 10, 255));
		setColor(DrawType.LINE, ButtonState.POPUP, new Color(100, 120, 255, 255));
		setColor(DrawType.LINE, ButtonState.DISABLED, new Color(100, 100, 100, 255));

		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(200, 200, 200, 255));
		setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(235, 235, 235, 255));
		setColor(DrawType.TEXT, ButtonState.HOVER, new Color(225, 225, 225, 255));
		setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(235, 235, 235, 255));
		setColor(DrawType.TEXT, ButtonState.POPUP, new Color(100, 100, 100, 255));	
		setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
	}
	
	@Override
	public void onMouseClick(int button) {
		if(button == 0 && alphaSlider.passesThrough()) {
			mouseDown = true;
		}
		
		alphaSlider.onMouseClick(button);
	}
	
	@Override
	public void onMouseClickMove(int button) {
		if(button == 0 && alphaSlider.passesThrough()) {
			mouseDragging = true;
		}
		
		alphaSlider.onMouseClickMove(button);
	}
	
	@Override
	public boolean onExitGui(int key) {
		if(pickingColor) {
			pickingColor = false;
		}
		
		alphaSlider.onExitGui(key);
		return false;
	}
	
	@Override
	public boolean passesThrough() {
		if(pickingColor) {
			return false;
		}
		
		if(disabled) {
			return true;
		}
		
		int x = this.getRenderX();
		int y = this.getRenderY();
		int mouseX = parent.getMouseX();
		int mouseY = parent.getMouseY();
		
		if(startPos != null) {			
			if(mouseX >= startPos.x && mouseX <= startPos.x + pickerWindowWidth) {
				if(mouseY >= startPos.y && mouseY <= startPos.y + pickerWindowHeight) {
					return false;
				}
			}
		} else if(mouseDown) {
			if(mouseX >= x && mouseX <= x + width) {
				if(mouseY >= y && mouseY <= y + height + 1) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	@Override
	public void onPreSort() {
		if(alphaSlider.getParent() == null && getParent() != null) {
			alphaSlider.setParent(getParent());
		}
		
		int x = this.getRenderX();
		int y = this.getRenderY();
		int mouseX = parent.getMouseX();
		int mouseY = parent.getMouseY();
		ButtonState state = ButtonState.NORMAL;
		
		if(!disabled) {
			if(mouseX >= x && mouseX <= x + width) {
				if(mouseY >= y && mouseY <= y + height + 1) {
					state = ButtonState.HOVER;
				}
			}
			
			if(startPos != null) {
				boolean hover = false;
				
				if(mouseX >= startPos.x && mouseX <= startPos.x + pickerWindowWidth) {
					if(mouseY >= startPos.y && mouseY <= startPos.y + pickerWindowHeight + 1) {
						hover = true;
					}
				}
				
				if(hover && mouseDown) {
					wantsToDrag = true;
				}
				
				pickingColor = (mouseDown && hover) || (!mouseDown && pickingColor);
				
				if(pickingColor) {
					state = ButtonState.HOVER;
				}
			} else if(state == ButtonState.HOVER && mouseDown) {
				pickingColor = true;
			}
		} else {
			state = ButtonState.DISABLED;
		}
		
		if(pickingColor) {
			setPriority(MenuPriority.HIGHEST);
		} else {
			if(state == ButtonState.HOVER || state == ButtonState.HOVERACTIVE) {
				setPriority(MenuPriority.HIGH);
			} else {
				setPriority(MenuPriority.MEDIUM);
			}
		}
		
		lastState = state;
	}
	
	@Override
	public void onRender() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int lineColor = getColor(DrawType.LINE, lastState);
				
		int index = 0;
		
		for(int h = y; h < y + height; h++) {
			drawRectFalcun(x + 1, h, width - 1, 1, disabled ? lightenColor(index, 7, color).getRGB() : darkenColor(index, 7, color).getRGB());
			index++;
		}
		
		drawHorizontalLine(x, y, width + 1, 1, lineColor);
		drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
		drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
		drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);
		
		if(startType <= 0) {
			if(alphaSlider.getParent() == null) {
				alphaSlider.setParent(getParent());
			}
			
			alphaSlider.onPreSort();
		}
		
		drawPicker();
		
		if(wantsToDrag) {
			mouseDragging = Mouse.isButtonDown(0);
			wantsToDrag = mouseDragging;
		}
		
		mouseDown = false;
		mouseDragging = false;
	}
	
	public void drawPicker() {
		if(pickingColor) {
			int mouseX = parent.getMouseX();
			int mouseY = parent.getMouseY();
			int backgroundColor = getColor(DrawType.BACKGROUND, ButtonState.POPUP);
			int lineColor = getColor(DrawType.LINE, lastState);
			
			if(!mouseDown) {
				canPick = true;
			}
			
			if(!wantsToDrag) {
				startType = 0;
			}
			
			if(startPos == null) {
				ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft(), 1);
				int windowX = mouseX;
				int windowY = mouseY;
				
				if(windowX + pickerWindowWidth >= res.getScaledWidth()) {
					windowX -= pickerWindowWidth;
				}
				
				if(windowY + pickerWindowHeight >= res.getScaledHeight()) {
					windowY -= pickerWindowHeight;
				}
				
				startPos = new Point(windowX, windowY);
				
				alphaSlider.setX(startPos.x);
				alphaSlider.setY(startPos.y + pickerWindowHeight - alphaSlider.getHeight());

				canPick = false;
			}
			
			if(canPick && startType == 0) {
				if(mouseY > startPos.y && mouseY < startPos.y + pickerWindowHeight - alphaSlider.getHeight()) {
					if(mouseX > startPos.x + pickerWindowWidth - colorOffset && mouseX < startPos.x + pickerWindowWidth) {
						startType = 1;
					} else if(mouseX > startPos.x && mouseX < startPos.x + size)  {
						startType = 2;
					}
				} else {
					startType = -1;
					canPick = false;
				}
			}
			
			if(startType != 0) {
				if(startType == 2) {
					if(mouseX >= startPos.x + pickerWindowWidth) {
						mouseX = startPos.x + pickerWindowWidth - size - 1;
					} else if(mouseX <= startPos.x) {
						mouseX = startPos.x + pickerWindowWidth - size + 1;
					}
				} else {
					if(mouseX >= startPos.x + pickerWindowWidth) {
						mouseX = startPos.x + pickerWindowWidth - 1;
					} else if(mouseX <= startPos.x + size) {
						mouseX = startPos.x + size + 1;
					}
				}
				
				if(mouseY >= startPos.y + pickerWindowHeight - alphaSlider.getHeight()) {
					mouseY = startPos.y + pickerWindowHeight - alphaSlider.getHeight() - 1;
				} else if(mouseY <= startPos.y) {
					mouseY = startPos.y + 1;
				}
			}
			
			drawRectFalcun(startPos.x + 1, startPos.y + 1, pickerWindowWidth - 1, pickerWindowHeight - 1, backgroundColor );
			drawHorizontalLine(startPos.x, startPos.y, pickerWindowWidth + 1, 1, lineColor);
			drawVerticalLine(startPos.x, startPos.y + 1, pickerWindowHeight  - 1, 1, lineColor);
			drawHorizontalLine(startPos.x, startPos.y + pickerWindowHeight, pickerWindowWidth + 1, 1, lineColor);
			drawVerticalLine(startPos.x + pickerWindowWidth, startPos.y + 1, pickerWindowHeight - 1, 1, lineColor);
			
			if(colorPickerMain == null) {
				BufferedImage bufferedPicker = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
				
				for(int y = 0; y < size; y++) { 
					float blackMod = 255 * (float)y / size;
							
					for(int x = 0; x < size; x++) {					
						Color color = new Color(clampColor(temp.getRed() - blackMod), clampColor(temp.getGreen() - blackMod), clampColor(temp.getBlue() - blackMod));
						bufferedPicker.setRGB(x, y, color.getRGB());
					}
				}
				
				DynamicTexture texture = new DynamicTexture(bufferedPicker);
				bufferedPicker.getRGB(0, 0, bufferedPicker.getWidth(), bufferedPicker.getHeight(), texture.getTextureData(), 0, bufferedPicker.getWidth());
				final ResourceLocation resource = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("color-picker-active", texture);
				colorPickerMain = resource;
			} 
			
			if((mouseDown || mouseDragging || wantsToDrag) && canPick && startType == 2) {
				if(mouseX > startPos.x && mouseX < startPos.x + size && mouseY > startPos.y && mouseY < startPos.y + size) {
					int y = mouseY - startPos.y;
					float blackMod = 255 * (float)y / size;
					
					this.color = new Color(clampColor(temp.getRed() - blackMod), clampColor(temp.getGreen() - blackMod), clampColor(temp.getBlue() - blackMod));
					onAction();
				}
			}
			
			drawImage(colorPickerMain, startPos.x + 1, startPos.y + 1, size - 1, size - 1);
			
			float colorSpeed = size / 3.8f;
			
			float red = 275;
			float green = 275;
			float blue = 275;
			
			for(int y = startPos.y + 1; y < startPos.y + size; y++) {
				if(red > 255) {
					red--;
				} 
				
				if(green > 255) {
					green--;
				}
				
				if(blue > 255) {
					blue--;
				}
				
				if(red >= 255 && green >= 255 && blue > 0) {
					blue -= colorSpeed;
				} else if(red >= 255 && green > 0 && blue <= 0) {
					green -= colorSpeed;
				} else if(red >= 255 && green <= 0 && blue < 255) {
					blue += colorSpeed;
				} else if(red > 0 && green <= 0 && blue >= 255) {
					red -= colorSpeed;
				} else if(red <= 0 && green < 255 && blue >= 255) {
					green += colorSpeed;
				} else if(red <= 0 && green >= 255 && blue > 0) {
					blue -= colorSpeed;
				} else if(red < 255 && green >= 255 && blue <= 0) {
					red += colorSpeed;
				}
				
				for(int x = startPos.x + size + 1; x < startPos.x + pickerWindowWidth; x++) { 
					Color color = new Color(clampColor(red), clampColor(green), clampColor(blue));
					
					if((mouseDown || mouseDragging || wantsToDrag) && canPick && startType == 1 && isInPixel(mouseX, mouseY, x, y)) {
						temp = color;
						lastYPress = y;
						onMiniAction();
						Minecraft.getMinecraft().getTextureManager().deleteTexture(colorPickerMain);
						colorPickerMain = null;
					}
					
					if(lastYPress == -1) {
						if(Math.abs(color.getRGB() - temp.getRGB()) < 3) {
							lastYPress = y;
						}
					}
					
					drawPixel(x, y, color.getRGB());
				}
			}
			
			
			if(lastYPress != -1) {
				drawHorizontalLine(startPos.x + pickerWindowWidth - colorOffset, lastYPress, colorOffset, 1, lineColor);
			}
			
			alphaSlider.onRender();
			drawVerticalLine(startPos.x + size, startPos.y + 1, pickerWindowHeight - alphaOffset - 1, 1, lineColor);
		} else if(startPos != null) {
			startPos = null;
			
			if(colorPickerMain != null) {
				Minecraft.getMinecraft().getTextureManager().deleteTexture(colorPickerMain);
				colorPickerMain = null;
			}
			
			alphaSlider.setX(Integer.MAX_VALUE);
			alphaSlider.setY(Integer.MAX_VALUE);
		}
	}
	
	private boolean isInPixel(int mouseX, int mouseY, int x, int y) {
		return mouseX == x && mouseY == y;
	}
	
	private int clampColor(float color) {
		int theColor = Math.round(color);
		
		if(theColor > 255) {
			return 255;
		}
		
		else if(theColor < 0) {
			return 0;
		}
		
		return theColor;
	}
	
	protected Color darkenColor(int index, int modifier, Color color) {
		int newRed = color.getRed() - index * modifier;
		int newGreen = color.getGreen() - index * modifier;
		int newBlue = color.getBlue() - index * modifier;
		
		if(newRed < 0) {
			newRed = 0;
		}
		
		if(newGreen < 0) {
			newGreen = 0;
		}
		
		if(newBlue < 0) {
			newBlue = 0;
		}
		
		return new Color(newRed, newGreen, newBlue, Math.round(alphaSlider.getValue() * 255));
	}
	
	protected Color lightenColor(int index, int modifier, Color color) {
		int newRed = color.getRed() + index * modifier;
		int newGreen = color.getGreen() + index * modifier;
		int newBlue = color.getBlue() + index * modifier;
		
		if(newRed > 255) {
			newRed = 255;
		}
		
		if(newGreen > 255) {
			newGreen = 255;
		}
		
		if(newBlue > 255) {
			newBlue = 255;
		}
		
		return new Color(newRed, newGreen, newBlue, color.getAlpha());
	}
	
	
	public Color getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = new Color(color);
		Minecraft.getMinecraft().getTextureManager().deleteTexture(colorPickerMain);
		colorPickerMain = null;
	}
	
	public Color getColorCategory() {
		return temp;
	}
	
	public void setColorCategory(int color) {
		this.temp = new Color(color);
		lastYPress = -1;
	}

	public MenuSlider getAlphaSlider() {
		return alphaSlider;
	}

	public void onAction() {}
	public void onMiniAction() {}
}
