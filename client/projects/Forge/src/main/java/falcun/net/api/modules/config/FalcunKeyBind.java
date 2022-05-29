package falcun.net.api.modules.config;

import org.lwjgl.input.Keyboard;

import java.io.Serializable;

public final class FalcunKeyBind  {
	public int keyBind;
	public FalcunKeyBind() {
		this.keyBind = 0;
	}
	public FalcunKeyBind(final int keycode) {
		this.keyBind = keycode;
	}

	public String getString() {
		try {
			return Keyboard.getKeyName(keyBind);
		} catch (Throwable err){
			return "BUTTON " + (keyBind+101);
		}
	}
}
