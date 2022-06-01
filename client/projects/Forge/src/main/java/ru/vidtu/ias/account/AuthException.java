package ru.vidtu.ias.account;

import net.minecraft.util.IChatComponent;

public class AuthException extends Exception {
	private static final long serialVersionUID = 1L;
	private IChatComponent text;
	
	public AuthException(IChatComponent text) {
		super(text.getFormattedText());
		this.text = text;
	}
	
	public AuthException(IChatComponent text, String detailed) {
		super(text.getFormattedText() + ":" + detailed);
		this.text = text;
	}
	
	public IChatComponent getText() {
		return text;
	}
}
