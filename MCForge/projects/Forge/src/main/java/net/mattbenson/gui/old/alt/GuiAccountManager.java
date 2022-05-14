package net.mattbenson.gui.old.alt;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import net.mattbenson.Falcun;
import net.mattbenson.accountmanager.Account;
import net.mattbenson.accountmanager.mojang.AccountMojang;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.utils.AccountUtils;
import net.mattbenson.utils.DrawUtils;
import net.mattbenson.utils.InputUtils;
import net.mattbenson.utils.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class GuiAccountManager extends GuiScreen implements GuiYesNoCallback
{
	Timer timer = new Timer();
	boolean ignoreReset = false;
	
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {new ResourceLocation("textures/falcun/gui/mainmenu/panorama_0.png"), new ResourceLocation("textures/falcun/gui/mainmenu/panorama_1.png"), new ResourceLocation("textures/falcun/gui/mainmenu/panorama_2.png"), new ResourceLocation("textures/falcun/gui/mainmenu/panorama_3.png"), new ResourceLocation("textures/falcun/gui/mainmenu/panorama_4.png"), new ResourceLocation("textures/falcun/gui/mainmenu/panorama_5.png")};
	private int panoramaTimer;
	private ResourceLocation backgroundTexture;
	private DynamicTexture viewportTexture;

	public GuiTextField usernameField;
	public GuiTextField passwordField;

	String typedUsername = "";
	String typedPassword = "";

	boolean typingUsername = false;
	boolean typingPassWord = false;

	GuiAltSlot altGui;
	public List<Account> altList;

	String message = "";


	public GuiAccountManager() {
		try {
			altList = AccountUtils.getAccounts();
		} catch (IOException e) {
			Falcun.getInstance().log.error("Failed to load account list.", e);
		}
		
		int[] size = InputUtils.getWindowsSize();
		
		int centerX = size[0] / 2;
		int centerY = size[1] / 2;
		
		this.altGui = new GuiAltSlot(this, altList, (centerX + 5) - centerX+centerX - 70);
		this.altGui.top = (int) (centerY - 65);
		this.altGui.bottom = (int) (centerY + 65);
		this.altGui.left = (int) (centerX + 5);
		this.altGui.slotHeight = 20;
	}

	public void initGui()
	{
		this.viewportTexture = new DynamicTexture(256, 256);
		this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
	}

	public void updateScreen()
	{
		++this.panoramaTimer;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{	
		GlStateManager.disableAlpha();
		this.renderSkybox(mouseX, mouseY, partialTicks);
		GlStateManager.enableAlpha();

		this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
		this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);

		int[] size = InputUtils.getWindowsSize();
		
		int startX = size[0] / 2;
		int startY = size[1] / 2;
		
		DrawUtils.drawRoundedRect(50, startY - 100, startX+startX - 50, startY + 100, 4, new Color(100,100,100,100).getRGB());
		DrawUtils.drawRoundedRect(51, startY - 99, startX+startX - 51, startY + 99, 3, new Color(22, 24, 27,80).getRGB());

		String s = "Account Manager";
		Fonts.RalewayExtraBoldLarge.drawString(s, startX - Fonts.RalewayExtraBoldLarge.getStringWidth(s) / 2, startY - 90, Color.WHITE.getRGB());

		String s1 = "Username / Email";
		boolean isOverUsername = mouseX >= 70 && mouseX <= startX && mouseY >= startY - 61 && mouseY <= startY - 44;
		DrawUtils.drawRoundedRect(70, startY - 61, startX, startY - 44, 4, isOverUsername ? new Color(200,200,200,100).getRGB() : new Color(100,100,100,100).getRGB());
		DrawUtils.drawRoundedRect(71, startY - 60, startX-1, startY - 45, 3, new Color(22, 24, 27,100).getRGB());

		String s2 = "Password";
		boolean isOverPassword = mouseX >= 70 && mouseX <= startX && mouseY >= startY - 41 && mouseY <= startY - 24;
		DrawUtils.drawRoundedRect(70, startY - 41, startX, startY - 24, 4, isOverPassword ? new Color(200,200,200,100).getRGB() : new Color(100,100,100,100).getRGB());
		DrawUtils.drawRoundedRect(71, startY - 40, startX-1, startY - 25, 3, new Color(22, 24, 27,100).getRGB());

		String s3 = "Back";
		boolean isOverBack = mouseX >= startX - 50 && mouseX <= startX && mouseY >= startY+75 && mouseY <= startY + 90;
		DrawUtils.drawRoundedRect(startX - 50, startY + 75, startX, startY+90, 4, isOverBack ? new Color(200,200,200,100).getRGB() : new Color(100,100,100,100).getRGB());
		DrawUtils.drawRoundedRect(startX - 49, startY + 76, startX-1, startY+89, 3, new Color(22, 24, 27,100).getRGB());
		Fonts.RalewayExtraBold.drawString(s3, startX - 34, startY + 78, Color.WHITE.getRGB());

		String s4 = "Login";
		boolean isOverLogin = mouseX >= startX+1 && mouseX <= startX + 50 && mouseY >= startY+75 && mouseY <= startY + 90;
		DrawUtils.drawRoundedRect(startX+1, startY + 75, startX + 50, startY+90, 4, isOverLogin ? new Color(200,200,200,100).getRGB() : new Color(100,100,100,100).getRGB());
		DrawUtils.drawRoundedRect(startX+2, startY + 76, startX + 49, startY+89, 3, new Color(22, 24, 27,100).getRGB());
		Fonts.RalewayExtraBold.drawString(s4, startX + 15, startY + 78, Color.WHITE.getRGB());

		Fonts.RalewayExtraBold.drawString(" - OR - ", 130, startY - 16, Color.WHITE.getRGB());
		
		String s5 = "Login with microsoft";
		boolean isOverLoginWithMicrosoft = mouseX >= 70 && mouseX <= startX && mouseY >= startY && mouseY <= startY + 15;
		DrawUtils.drawRoundedRect(70, startY, startX, startY + 15, 4, isOverLoginWithMicrosoft ? new Color(200,200,200,100).getRGB() : new Color(100,100,100,100).getRGB());
		DrawUtils.drawRoundedRect(71, startY + 1, startX - 1, startY + 15 - 1, 3, new Color(22, 24, 27,100).getRGB());
		Fonts.RalewayExtraBold.drawString(s5, 106, startY + 3, Color.WHITE.getRGB());

		

		if(timer.hasReached(5000) && !ignoreReset) {
			message = "";
		}
		Fonts.RalewayExtraBold.drawString(message, startX - Fonts.RalewayExtraBold.getStringWidth(message)/2, startY - 75, Color.WHITE.getRGB());

		if(usernameField != null && typingUsername) {
			usernameField.drawTextBox();
		} 
		if(!typingUsername) {
			Fonts.RalewayExtraBold.drawString(typedUsername.length() <= 0 ? s1 : typedUsername, 80, startY - 57, Color.WHITE.getRGB());
		}

		if(usernameField != null) {
			usernameField.setEnableBackgroundDrawing(false);
			usernameField.setVisible(true);
			this.usernameField.xPosition = 73;
			this.usernameField.yPosition = startY-57;
			this.usernameField.width = startX-1 - 70;
			this.usernameField.height = 17;
		} else {
			this.usernameField = new GuiTextField(1,Minecraft.getMinecraft().fontRendererObj,(int)73,(int)startY-57, startX-1 - 70,17);   
			usernameField.setFocused(false);
			usernameField.setCanLoseFocus(true);
			usernameField.setEnableBackgroundDrawing(false);
			usernameField.setMaxStringLength(100);
		}

		if(passwordField != null && typingPassWord) {
			passwordField.drawTextBox();
		} 
		if(!typingPassWord) {
			String censor = "";
			for(int i = 0; i < typedPassword.length(); i++) {
				censor += "*";
			}
			Fonts.RalewayExtraBold.drawString(typedPassword.length() <= 0 ? s2 : censor, 80, startY - 37, Color.WHITE.getRGB());
		}

		if(passwordField != null) {
			passwordField.setEnableBackgroundDrawing(false);
			passwordField.setVisible(true);
			this.passwordField.xPosition = 73;
			this.passwordField.yPosition = startY-38;
			this.passwordField.width = startX-1 - 70;
			this.passwordField.height = 17;
		} else {
			this.passwordField = new GuiTextField(2,Minecraft.getMinecraft().fontRendererObj,(int)73,(int)startY-38, startX-1 - 70,17);   
			passwordField.setFocused(false);
			passwordField.setCanLoseFocus(true);
			passwordField.setEnableBackgroundDrawing(false);
			passwordField.setMaxStringLength(100);
		}

		this.altGui.listWidth = (startX + startX - 70) - (startX + 5);
		this.altGui.top = (int) (startY - 65);
		this.altGui.bottom = (int) (startY + 65);
		this.altGui.left = (int) (startX + 5);
		altGui.drawScreen(mouseX, mouseY, partialTicks);

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public boolean doesGuiPauseGame()
	{
		return false;
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(keyCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(new GuiMainMenu());
		}

		if(keyCode == Keyboard.KEY_TAB) {
			if(typingUsername) {
				usernameField.setFocused(false);
				typedUsername = usernameField.getText();
				typingUsername = false;
				passwordField.setFocused(true);
				typingPassWord = true;
				return;
			}
			if(typingPassWord) {
				passwordField.setFocused(false);
				typedPassword = passwordField.getText();
				typingPassWord = false;
				return;
			}
		}
		if(keyCode == Keyboard.KEY_NUMPADENTER || keyCode == 28) {
			if(typingUsername) {
				usernameField.setFocused(false);
				typedUsername = usernameField.getText();
				typingUsername = false;
				passwordField.setFocused(true);
				typingPassWord = true;
			}
			if(typingPassWord) {
				passwordField.setFocused(false);
				typedPassword = passwordField.getText();
				typingPassWord = false;
				
				Object account = AccountUtils.createNewUserMojang(typedUsername, typedPassword);
				
				if(account instanceof String) {
					displayErrorMessage(EnumChatFormatting.RED + (String) account);
					return;
				} else if(account instanceof Account) {
					usernameField.setText("");
					passwordField.setText("");
					typedPassword = "";
					typedUsername = "";
					
					Account acc = (Account) account;
					displayErrorMessage(acc.loadUser());
					altList.add(acc);
				}
				else
				{
					displayErrorMessage(EnumChatFormatting.RED + "Please enter username & password or click on an alt slot to login!");
				}
			}
		}
		if(usernameField != null) {
			usernameField.textboxKeyTyped(typedChar, keyCode);
			if(typingUsername) {
				typedUsername = usernameField.getText();
			}
		}
		if(passwordField != null) {
			passwordField.textboxKeyTyped(typedChar, keyCode);
			if(typingPassWord) {
				typedPassword = passwordField.getText();
			}
		}
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		int[] size = InputUtils.getWindowsSize();
		
		int startX = size[0] / 2;
		int startY = size[1] / 2;

		boolean isOverUsername = mouseX >= 70 && mouseX <= startX && mouseY >= startY - 61 && mouseY <= startY - 44;
		boolean isOverPassword = mouseX >= 70 && mouseX <= startX && mouseY >= startY - 41 && mouseY <= startY - 24;
		boolean isOverBack = mouseX >= startX - 50 && mouseX <= startX && mouseY >= startY+75 && mouseY <= startY + 90;
		boolean isOverLogin = mouseX >= startX+1 && mouseX <= startX + 50 && mouseY >= startY+75 && mouseY <= startY + 90;
		boolean isOverLoginWithMicrosoft = mouseX >= 70 && mouseX <= startX && mouseY >= startY && mouseY <= startY + 15;
		
		if(isOverBack) {
			mc.displayGuiScreen(new GuiMainMenu());
		}

		if(isOverLoginWithMicrosoft) {
			AccountUtils.startMicrosoftAuth();
		}
		
		if(isOverLogin) {
			if(!typedPassword.isEmpty() && !typedUsername.isEmpty()) {
				Object account = AccountUtils.createNewUserMojang(typedUsername, typedPassword);
				
				if(account instanceof String) {
					displayErrorMessage(EnumChatFormatting.RED + (String) account);
					return;
				} else if(account instanceof Account) {
					usernameField.setText("");
					passwordField.setText("");
					typedPassword = "";
					typedUsername = "";
					
					Account acc = (Account) account;
					displayErrorMessage(acc.loadUser());
					altList.add(acc);
				}
			}
			else
			{
				displayErrorMessage(EnumChatFormatting.RED + "Please enter username & password or click on an alt slot to login!");
			}
		}

		if(isOverUsername) {
			typingUsername = true;
		} else {
			typingUsername = false;
		}

		if(isOverPassword) {
			typingPassWord = true;
			passwordField.setFocused(true);
		} else {
			typingPassWord = false;
		}

		if(usernameField != null) {
			usernameField.mouseClicked(mouseX, mouseY, mouseButton);
		}
		if(passwordField != null) {
			passwordField.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_)
	{
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.matrixMode(5889);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
		GlStateManager.matrixMode(5888);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.disableCull();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		int i = 8;

		for (int j = 0; j < i * i; ++j)
		{
			GlStateManager.pushMatrix();
			float f = ((float)(j % i) / (float)i - 0.5F) / 64.0F;
			float f1 = ((float)(j / i) / (float)i - 0.5F) / 64.0F;
			float f2 = 0.0F;
			GlStateManager.translate(f, f1, f2);
			GlStateManager.rotate(MathHelper.sin(((float)this.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(-((float)this.panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

			for (int k = 0; k < 6; ++k)
			{
				GlStateManager.pushMatrix();

				if (k == 1)
				{
					GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 2)
				{
					GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 3)
				{
					GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 4)
				{
					GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				}

				if (k == 5)
				{
					GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
				}

				this.mc.getTextureManager().bindTexture(titlePanoramaPaths[k]);
				worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				int l = 255 / (j + 1);
				float f3 = 0.0F;
				worldrenderer.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, l).endVertex();
				worldrenderer.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, l).endVertex();
				worldrenderer.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, l).endVertex();
				worldrenderer.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, l).endVertex();
				tessellator.draw();
				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();
			GlStateManager.colorMask(true, true, true, false);
		}

		worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.matrixMode(5889);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.enableDepth();
	}

	private void rotateAndBlurSkybox(float p_73968_1_)
	{
		this.mc.getTextureManager().bindTexture(this.backgroundTexture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.colorMask(true, true, true, false);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		GlStateManager.disableAlpha();
		int i = 3;

		for (int j = 0; j < i; ++j)
		{
			float f = 1.0F / (float)(j + 1);
			int k = this.width;
			int l = this.height;
			float f1 = (float)(j - i / 2) / 256.0F;

			worldrenderer.pos((double)k, (double)l, (double)this.zLevel).tex((double)(0.0F + f1), 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos((double)k, 0.0D, (double)this.zLevel).tex((double)(1.0F + f1), 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos(0.0D, 0.0D, (double)this.zLevel).tex((double)(1.0F + f1), 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos(0.0D, (double)l, (double)this.zLevel).tex((double)(0.0F + f1), 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
		}

		tessellator.draw();
		GlStateManager.enableAlpha();
		GlStateManager.colorMask(true, true, true, true);
	}

	private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_)
	{
		this.mc.getFramebuffer().unbindFramebuffer();
		GlStateManager.viewport(0, 0, 256, 256);
		this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.mc.getFramebuffer().bindFramebuffer(true);
		GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
		float f = this.width > this.height ? 120.0F / (float)this.width : 120.0F / (float)this.height;
		float f1 = (float)this.height * f / 256.0F;
		float f2 = (float)this.width * f / 256.0F;
		int i = this.width;
		int j = this.height;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		worldrenderer.pos(0.0D, (double)j, (double)this.zLevel).tex((double)(0.5F - f1), (double)(0.5F + f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos((double)i, (double)j, (double)this.zLevel).tex((double)(0.5F - f1), (double)(0.5F - f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos((double)i, 0.0D, (double)this.zLevel).tex((double)(0.5F + f1), (double)(0.5F - f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos(0.0D, 0.0D, (double)this.zLevel).tex((double)(0.5F + f1), (double)(0.5F + f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		tessellator.draw();
	}

	public void displayErrorMessage(String s) {
		ignoreReset = false;
		timer.reset();
		message = s;
	}

	public void displayErrorMessagePermanent(String s) {
		ignoreReset = true;
		message = s;
	}
}