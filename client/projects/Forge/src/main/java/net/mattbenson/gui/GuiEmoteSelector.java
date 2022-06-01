package net.mattbenson.gui;

import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.json.JSONException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import mchorse.emoticons.common.EmoteAPI;
import mchorse.emoticons.common.emotes.Emote;
import mchorse.emoticons.common.emotes.Emotes;
import net.mattbenson.Falcun;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.fonts.NahrFont.FontType;
import net.mattbenson.modules.types.mods.Gui;
import net.mattbenson.requests.ContentType;
import net.mattbenson.requests.WebRequest;
import net.mattbenson.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class GuiEmoteSelector extends GuiScreen {
	private Color color = new Color(0, 0, 0, 64);
	
	private static final float PRECISION = 5.0f;
	
	List<Emote> emotes;

	int centerX = DrawUtils.getCenterX();
	int centerY = DrawUtils.getCenterY(); 

	public Emote hoveredEmote = null;
	public GuiEmoteSelector() {
		emotes = new ArrayList<>();
		
		if(Falcun.getInstance().emoteSettings.getEmote1() != null) {
			emotes.add(Emotes.get(Falcun.getInstance().emoteSettings.getEmote1().toLowerCase().replace(' ', '_')));
		}
		
		if(Falcun.getInstance().emoteSettings.getEmote2() != null) {
			emotes.add(Emotes.get(Falcun.getInstance().emoteSettings.getEmote2().toLowerCase().replace(' ', '_')));
		}
		
		if(Falcun.getInstance().emoteSettings.getEmote3() != null) {
			emotes.add(Emotes.get(Falcun.getInstance().emoteSettings.getEmote3().toLowerCase().replace(' ', '_')));
		}
		
		if(Falcun.getInstance().emoteSettings.getEmote4() != null) {
			emotes.add(Emotes.get(Falcun.getInstance().emoteSettings.getEmote4().toLowerCase().replace(' ', '_')));
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		GlStateManager.pushMatrix();
		
		int numberOfSlices = 4;
		float radiusIn = 60;
		float radiusOut = radiusIn * 2;
		float itemRadius = (radiusIn + radiusOut) * 0.5f;
		
		String title = "EMOTE SELECTOR";
		Fonts.largeBoldFontRenderer.drawString(title, centerX - (Fonts.largeBoldFontRenderer.getStringWidth(title)/ 2), centerY - 150, FontType.SHADOW_THICK,Color.WHITE.getRGB(), Color.BLACK.getRGB());

		hoveredEmote = null;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer buffer = tessellator.getWorldRenderer();

		double a = Math.toDegrees(Math.atan2(mouseY - centerY, mouseX - centerX));
		double d = Math.sqrt(Math.pow(mouseX - centerX, 2) + Math.pow(mouseY - centerY, 2));
		float s0 = (((0 - 0.5f) / (float) 4) + 0.25f) * 360;
		if (a < s0) {
			a += 360;
		}


		int selectedItem = -1;
		for (int i = 0; i < numberOfSlices; i++) {
			float s = (((i - 0.5f) / (float) numberOfSlices) + 0.25f) * 360;
			float e = (((i + 0.5f) / (float) numberOfSlices) + 0.25f) * 360;
			if (a >= s && a < e && d >= radiusIn && d < radiusOut) {
				selectedItem = i;
				break;
			}
		}

		GlStateManager.popMatrix();
		
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		for (int i = 0; i < 4; i++) {
			try {
				float s = (((i - 0.5f) / (float) numberOfSlices) + 0.25f) * 360;
				float e = (((i + 0.5f) / (float) numberOfSlices) + 0.25f) * 360;
				buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);


				int colorR = 0;
				int colorG = 0;
				int colorB = 0;

				if(selectedItem == i) {
					colorR = 255;
					colorG = 0;
					colorB = 0;
				}


				drawSlice(buffer, centerX, centerY, zLevel, radiusIn, radiusOut, s, e, colorR,colorG,colorB,64);
				tessellator.draw();
			}
			catch(Exception ex) 
			{
				ex.printStackTrace();
			}
		}
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();


		GlStateManager.pushMatrix();
		DrawUtils.drawStrip(centerX, centerY, 5F, 360, 10000, 121, new Color(0,0,0,34).getRGB());
		DrawUtils.drawStrip(centerX, centerY, 5F, 360, 10000, 61, new Color(0,0,0,34).getRGB());
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F,1.0F);


		if(emotes.size() >= 1) {
			
			String getemote1 = Falcun.getInstance().emoteSettings.getEmote1();
			getemote1 = getemote1.replace("emoticons.emotes.", "");
			getemote1 = getemote1.replace(".title", "");
		
			Emote emote1 = Emotes.get(getemote1);
			
		if(emote1 != null) {
			if(selectedItem == 0) {
				hoveredEmote = emote1;
			}
			float angle1 = ((0 / (float) 4) + 0.25f) * 2 * (float) Math.PI;
			float posX = centerX - 33 + itemRadius * (float) Math.cos(angle1);
			float posY = centerY - 32 + itemRadius * (float) Math.sin(angle1);
			if(emote1.getLocation() != null) {
				DrawUtils.drawSquareTexture(emote1.getLocation(), 30, posX, posY);
			} else {
				String name = emote1.name.toUpperCase().replace("_", " ");
				Fonts.RalewayExtraBoldSmall
				.drawString(name, posX + 30 - Fonts.RalewayExtraBoldExtraSmall.getStringWidth(name) / 2, posY+ 30, FontType.SHADOW_THICK, Color.WHITE.getRGB(),Color.BLACK.getRGB());
			}
		}
		}
		if(emotes.size() >= 2) {
			
			String getemote1 = Falcun.getInstance().emoteSettings.getEmote2();
			getemote1 = getemote1.replace("emoticons.emotes.", "");
			getemote1 = getemote1.replace(".title", "");
		
			Emote emote1 = Emotes.get(getemote1);
			
			if(emote1 != null) {
				if(selectedItem == 1) {
					hoveredEmote = emote1;
				}
				float angle1 = ((1 / (float) 4) + 0.25f) * 2 * (float) Math.PI;
				float posX = centerX - 33 + itemRadius * (float) Math.cos(angle1);
				float posY = centerY - 32 + itemRadius * (float) Math.sin(angle1);
				if(emote1.getLocation() != null) {
					DrawUtils.drawSquareTexture(emote1.getLocation(), 30, posX, posY);
				} else {
					String name = emote1.name.toUpperCase().replace("_", " ");
					Fonts.RalewayExtraBoldSmall
					.drawString(name, posX + 30 - Fonts.RalewayExtraBoldExtraSmall.getStringWidth(name) / 2, posY+ 30, FontType.SHADOW_THICK, Color.WHITE.getRGB(),Color.BLACK.getRGB());
				}
			}
		}
		if(emotes.size() >= 3) {

			String getemote1 = Falcun.getInstance().emoteSettings.getEmote3();
			getemote1 = getemote1.replace("emoticons.emotes.", "");
			getemote1 = getemote1.replace(".title", "");
		
			Emote emote1 = Emotes.get(getemote1);
			
			if(emote1 != null) {
				if(selectedItem == 2) {
					hoveredEmote = emote1;
				}
				float angle1 = ((2 / (float) 4) + 0.25f) * 2 * (float) Math.PI;
				float posX = centerX - 33 + itemRadius * (float) Math.cos(angle1);
				float posY = centerY - 32 + itemRadius * (float) Math.sin(angle1);
				if(emote1.getLocation() != null) {
					DrawUtils.drawSquareTexture(emote1.getLocation(), 30, posX, posY);
				}
				 else {
						String name = emote1.name.toUpperCase().replace("_", " ");
						Fonts.RalewayExtraBoldSmall
						.drawString(name, posX + 30 - Fonts.RalewayExtraBoldExtraSmall.getStringWidth(name) / 2, posY+ 30, FontType.SHADOW_THICK, Color.WHITE.getRGB(),Color.BLACK.getRGB());
					}
			}
		}
		if(emotes.size() >= 4) {

			String getemote1 = Falcun.getInstance().emoteSettings.getEmote4();
			getemote1 = getemote1.replace("emoticons.emotes.", "");
			getemote1 = getemote1.replace(".title", "");
		
			Emote emote1 = Emotes.get(getemote1);
			
			if(emote1 != null) {
				if(selectedItem == 3) {
					hoveredEmote = emote1;
				}
				float angle1 = ((3 / (float) 4) + 0.25f) * 2 * (float) Math.PI;
				float posX = centerX - 33 + itemRadius * (float) Math.cos(angle1);
				float posY = centerY - 32 + itemRadius * (float) Math.sin(angle1);
				if(emote1.getLocation() != null) {
					DrawUtils.drawSquareTexture(emote1.getLocation(), 30, posX, posY);
				}
				 else {
						String name = emote1.name.toUpperCase().replace("_", " ");
						Fonts.RalewayExtraBoldSmall
						.drawString(name, posX + 30 - Fonts.RalewayExtraBoldExtraSmall.getStringWidth(name) / 2, posY+ 30, FontType.SHADOW_THICK, Color.WHITE.getRGB(),Color.BLACK.getRGB());
					}
			}
		}
		

		GlStateManager.popMatrix();

		
		//
		
		String one = "Hover Over An Emote".toUpperCase();
		String two = "Click To Dance".toUpperCase();
		
		String emote = "NONE";
		if(hoveredEmote != null) {
		
			emote = hoveredEmote.getKey().toUpperCase().replace("_", "  ");
			if(emote.contains(":")) {
				emote = emote.split(":")[0];
			}
		
		} else {
			emote = "NONE";
		}
		
		int centerX = DrawUtils.getCenterX();
		int centerY = DrawUtils.getCenterY();

		Fonts.RalewayExtraBoldSmall.drawString(one, centerX - (Fonts.RalewayExtraBoldSmall.getStringWidth(one)/2) , centerY - 40, FontType.SHADOW_THICK,Color.WHITE.getRGB(), Color.BLACK.getRGB());
		Fonts.RalewayExtraBoldSmall.drawString(two, centerX - (Fonts.RalewayExtraBoldSmall.getStringWidth(two)/2), centerY - 40 + Fonts.RalewayExtraBoldSmall.getStringHeight(one), FontType.SHADOW_THICK,Color.WHITE.getRGB(), Color.BLACK.getRGB());

		Fonts.RobotoVeryLarge.drawString(emote, centerX - (Fonts.RobotoVeryLarge.getStringWidth(emote)/2), centerY - 30 + (Fonts.RalewayExtraBoldSmall.getStringHeight(two) * 3), FontType.SHADOW_THICK,Color.WHITE.getRGB(), Color.BLACK.getRGB());
	
		
		if(!Keyboard.isKeyDown(Falcun.getInstance().emoteSettings.getKeyBind()) && !Mouse.isButtonDown(Falcun.getInstance().emoteSettings.getKeyBind())) {
			mouseClicked(mouseX, mouseY, 0);
		}
		
		if (!Display.isActive()) {
			if (Minecraft.getMinecraft().theWorld != null) {
				Minecraft.getMinecraft().displayGuiScreen(null);
			}
		} 
		
		
	}


	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		float radiusIn = 60;
		float radiusOut = radiusIn * 2;
		double a = Math.toDegrees(Math.atan2(mouseY - centerY, mouseX - centerX));
		double d = Math.sqrt(Math.pow(mouseX - centerX, 2) + Math.pow(mouseY - centerY, 2));
		float s0 = (((0 - 0.5f) / (float) 4) + 0.25f) * 360;
		if (a < s0) {
			a += 360;
		}
		
		//Reconnect button
		/*
		if(mouseButton == 0) {
			int centerX = DisplayUtil.getCenterX();
			int centerY = DisplayUtil.getCenterY();

			String text = Wrapper.getInstance().getConnectionLabel();
			int width = (int)Fonts.RalewayExtraBoldSmall.getStringWidth(text);
			
			boolean hover = mouseX >= centerX + (220 - width - 5) && mouseX <= centerX + 215 && mouseY >= centerY - 88 && mouseY <= centerY - 81;
			
			if(hover) {
				Wrapper.getInstance().reconnect();
			}
		}
		*/
		
		int selectedItem = -1;
		for (int i = 0; i < 4; i++) {
			float s = (((i - 0.5f) / (float) 4) + 0.25f) * 360;
			float e = (((i + 0.5f) / (float) 4) + 0.25f) * 360;
			if (a >= s && a < e && d >= radiusIn && d < radiusOut) {
				selectedItem = i;
				break;
			}
		}
		
		if(selectedItem == 0) {
			if(emotes.size() >= 1) {

				String getemote1 = Falcun.getInstance().emoteSettings.getEmote1();
				getemote1 = getemote1.replace("emoticons.emotes.", "");
				getemote1 = getemote1.replace(".title", "");
			
				Emote emote1 = Emotes.get(getemote1);
				
				if(emote1 != null) {
					EmoteAPI.setEmoteClient(emote1.getKey(), (EntityPlayer) mc.thePlayer); 
					Minecraft.getMinecraft().displayGuiScreen(null);
					return;
				}
			}
		} else if(selectedItem == 1) {
			if(emotes.size() >= 2) {
				
				String getemote1 = Falcun.getInstance().emoteSettings.getEmote2();
				getemote1 = getemote1.replace("emoticons.emotes.", "");
				getemote1 = getemote1.replace(".title", "");
			
				Emote emote1 = Emotes.get(getemote1);
				
				if(emote1 != null) {
					EmoteAPI.setEmoteClient(emote1.getKey(), (EntityPlayer) mc.thePlayer); 
					Minecraft.getMinecraft().displayGuiScreen(null);
					return;
				}
			}
		} else if(selectedItem == 2) {
			if(emotes.size() >= 3) {

				String getemote1 = Falcun.getInstance().emoteSettings.getEmote3();
				getemote1 = getemote1.replace("emoticons.emotes.", "");
				getemote1 = getemote1.replace(".title", "");
			
				Emote emote1 = Emotes.get(getemote1);
				
				if(emote1 != null) {
					EmoteAPI.setEmoteClient(emote1.getKey(), (EntityPlayer) mc.thePlayer); 
					Minecraft.getMinecraft().displayGuiScreen(null);
					return;
				}
			}
		} else if(selectedItem == 3) {
			if(emotes.size() >= 4) {

				String getemote1 = Falcun.getInstance().emoteSettings.getEmote4();
				getemote1 = getemote1.replace("emoticons.emotes.", "");
				getemote1 = getemote1.replace(".title", "");
			
				Emote emote1 = Emotes.get(getemote1);
				
				if(emote1 != null) {
					EmoteAPI.setEmoteClient(emote1.getKey(), (EntityPlayer) mc.thePlayer); 
					Minecraft.getMinecraft().displayGuiScreen(null);
					return;
				}
			}
		}
		
		mc.displayGuiScreen(null);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {

	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {
		if(keyCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(null);
		}
	}

	private void drawSlice(
			WorldRenderer buffer,
			float x,
			float y,
			float z,
			float radiusIn,
			float radiusOut,
			float startAngle,
			float endAngle,
			int r,
			int g,
			int b,
			int a
			) {
		float angle = endAngle - startAngle;
		int sections = Math.max(1, MathHelper.ceiling_float_int(angle / PRECISION));

		startAngle = (float) Math.toRadians(startAngle);
		endAngle = (float) Math.toRadians(endAngle);
		angle = endAngle - startAngle;

		for (int i = 0; i < sections; i++)
		{
			float angle1 = startAngle + (i / (float) sections) * angle;
			float angle2 = startAngle + ((i + 1) / (float) sections) * angle;

			float pos1InX = x + radiusIn * (float) Math.cos(angle1);
			float pos1InY = y + radiusIn * (float) Math.sin(angle1);
			float pos1OutX = x + radiusOut * (float) Math.cos(angle1);
			float pos1OutY = y + radiusOut * (float) Math.sin(angle1);
			float pos2OutX = x + radiusOut * (float) Math.cos(angle2);
			float pos2OutY = y + radiusOut * (float) Math.sin(angle2);
			float pos2InX = x + radiusIn * (float) Math.cos(angle2);
			float pos2InY = y + radiusIn * (float) Math.sin(angle2);

			buffer.pos(pos1OutX, pos1OutY, z).color(r, g, b, a).endVertex();
			buffer.pos(pos1InX, pos1InY, z).color(r, g, b, a).endVertex();
			buffer.pos(pos2InX, pos2InY, z).color(r, g, b, a).endVertex();
			buffer.pos(pos2OutX, pos2OutY, z).color(r, g, b, a).endVertex();

		}
	}





}
