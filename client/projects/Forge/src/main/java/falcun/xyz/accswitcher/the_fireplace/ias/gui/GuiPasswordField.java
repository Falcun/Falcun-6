package falcun.xyz.accswitcher.the_fireplace.ias.gui;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiPasswordField extends GuiTextField {
	public GuiPasswordField(int id, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
		super(id, fontrendererObj, x, y, par5Width, par6Height);
	}
	
	@Override
	public void drawTextBox() {
		String prev = getText();
		text(StringUtils.repeat('*', prev.length()));
		super.drawTextBox();
		text(prev);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		String prev = getText();
		text(StringUtils.repeat('*', prev.length()));
		super.mouseClicked(mouseX, mouseY, mouseButton);
		text(prev);
	}
	
	@Override
	public boolean textboxKeyTyped(char typedChar, int keyCode) {
		return !GuiScreen.isKeyComboCtrlC(keyCode) && !GuiScreen.isKeyComboCtrlX(keyCode) && super.textboxKeyTyped(typedChar, keyCode);
	}
	
	public void text(String txt) {
		int cursorPosition = getCursorPosition();
		int selectionEnd = getSelectionEnd();
		setText(txt);
		setCursorPosition(cursorPosition);
		setSelectionPos(selectionEnd);
	}
}
