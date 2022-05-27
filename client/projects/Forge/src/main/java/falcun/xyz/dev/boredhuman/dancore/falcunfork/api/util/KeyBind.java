package falcun.xyz.dev.boredhuman.dancore.falcunfork.api.util;

import org.lwjgl.input.Keyboard;

import java.util.Objects;
import java.util.function.Consumer;

public class KeyBind {

	int key;
	private transient Consumer<Boolean> onPress;

	public KeyBind(int key) {
		this.key = key;
	}

	public void setCallBack(Consumer<Boolean> keyBindConsumer) {
		this.onPress = keyBindConsumer;
	}

	public void onKeyPress() {
		if (this.onPress != null) {
			this.onPress.accept(Keyboard.getEventKeyState());
		}
	}

	public int getKey() {
		return this.key;
	}

	public void setKey(int key) { // TODO: SET KEY MGR
		if (key != this.key) {
			int oldKey = this.key;
			this.key = key;
//			DancoreMod.getInstance().getKeyBindManager().onKeyBindChange(this, oldKey);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}
		KeyBind keyBind = (KeyBind) o;
		return this.key == keyBind.key && Objects.equals(this.onPress, keyBind.onPress);
	}

	@Override
	public int hashCode() {
		return this.key;
	}
}