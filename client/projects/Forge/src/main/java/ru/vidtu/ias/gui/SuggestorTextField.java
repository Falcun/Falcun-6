package ru.vidtu.ias.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class SuggestorTextField extends GuiTextField {
	public String suggestion;
	public SuggestorTextField(int id, FontRenderer font, int x, int y, int width, int height, String suggest) {
		super(id, font, x, y, width, height);
		this.suggestion = suggest;
	}
	
	@Override
	public void drawTextBox() {
		String prev = getText();
		if (prev.isEmpty()) {
			setText(suggestion);
			setTextColor(0xFFAAAAAA);
			super.drawTextBox();
			setText(prev);
			setTextColor(-1);
		} else {
			super.drawTextBox();
		}
	}

}
