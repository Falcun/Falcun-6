package falcun.xyz.accswitcher.ru.vidtu.ias.gui;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import falcun.xyz.accswitcher.ru.vidtu.ias.Config;

/**
 * Screen for editing IAS config.
 * @author VidTu
 */
public class IASConfigScreen extends GuiScreen {
	public final GuiScreen prev;
	public GuiCheckBox caseS, mpscreen, titlescreen;
	public GuiTextField textX, textY, btnX, btnY;
	public IASConfigScreen(GuiScreen prev) {
		this.prev = prev;
	}
	
	@Override
	public void initGui() {
		buttonList.add(caseS = new GuiCheckBox(1, width / 2 - fontRendererObj.getStringWidth(I18n.format("ias.cfg.casesensitive")) / 2 - 8, 40, I18n.format("ias.cfg.casesensitive"), Config.caseSensitiveSearch));
		buttonList.add(mpscreen = new GuiCheckBox(2, width / 2 - fontRendererObj.getStringWidth(I18n.format("ias.cfg.mpscreen")) / 2 - 8, 60, I18n.format("ias.cfg.mpscreen"), Config.showOnMPScreen));
		buttonList.add(titlescreen = new GuiCheckBox(3, width / 2 - fontRendererObj.getStringWidth(I18n.format("ias.cfg.titlescreen")) / 2 - 8, 80, I18n.format("ias.cfg.titlescreen"), Config.showOnTitleScreen));
		textX = new SuggestorTextField(4, fontRendererObj, width / 2 - 100, 110, 98, 20, "X");
		textY = new SuggestorTextField(5, fontRendererObj, width / 2 + 2, 110, 98, 20, "Y");
		btnX = new SuggestorTextField(6, fontRendererObj, width / 2 - 100, 152, 98, 20, "X");
		btnY = new SuggestorTextField(7, fontRendererObj, width / 2 + 2, 152, 98, 20, "Y");
		buttonList.add(new GuiButton(0, width / 2 - 75, height - 24, 150, 20, I18n.format("gui.done")));
		textX.setText(StringUtils.trimToEmpty(Config.textX));
		textY.setText(StringUtils.trimToEmpty(Config.textY));
		btnX.setText(StringUtils.trimToEmpty(Config.btnX));
		btnY.setText(StringUtils.trimToEmpty(Config.btnY));
	}
	
	@Override
	public void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			mc.displayGuiScreen(prev);
		}
	}
	
	@Override
	public void onGuiClosed() {
		Config.caseSensitiveSearch = caseS.isChecked();
		Config.showOnMPScreen = mpscreen.isChecked();
		Config.showOnTitleScreen = titlescreen.isChecked();
		Config.textX = textX.getText();
		Config.textY = textY.getText();
		Config.btnX = btnX.getText();
		Config.btnY = btnY.getText();
		Config.save(mc);
	}
	
	@Override
	public void updateScreen() {
		btnX.setVisible(titlescreen.isChecked());
		btnY.setVisible(titlescreen.isChecked());
		textX.updateCursorCounter();
		textY.updateCursorCounter();
		btnX.updateCursorCounter();
		btnY.updateCursorCounter();
		super.updateScreen();
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		textX.textboxKeyTyped(typedChar, keyCode);
		textY.textboxKeyTyped(typedChar, keyCode);
		btnX.textboxKeyTyped(typedChar, keyCode);
		btnY.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		textX.mouseClicked(mouseX, mouseY, mouseButton);
		textY.mouseClicked(mouseX, mouseY, mouseButton);
		btnX.mouseClicked(mouseX, mouseY, mouseButton);
		btnY.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void drawScreen(int mx, int my, float delta) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, "config/ias.json", width / 2, 10, -1);
		drawCenteredString(fontRendererObj, I18n.format("ias.cfg.textpos"), width / 2, 100, -1);
		if (titlescreen.isChecked()) drawCenteredString(fontRendererObj, I18n.format("ias.cfg.btnpos"), width / 2, 142, -1);
		textX.drawTextBox();
		textY.drawTextBox();
		btnX.drawTextBox();
		btnY.drawTextBox();
		super.drawScreen(mx, my, delta);
	}
}
