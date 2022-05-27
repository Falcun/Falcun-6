package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.api.util.KeyBind;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.fonts.DanFont;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.KeyListener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.MouseListener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util.Colors;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class KeyBindElement extends LabelElement {

	boolean selected = false;

	public KeyBindElement(KeyBind keyBind, DanFont font) {
		super(Keyboard.getKeyName(keyBind.getKey()), font);
		this.addListener((KeyListener) elem -> {
				if (Keyboard.getEventKeyState() && this.selected) {
					int key = Keyboard.getEventKey();
					if (key == Keyboard.KEY_ESCAPE) {
						key = 0;
						this.selected = false;
						elem.parentElement.setOutline(null);
					}
					keyBind.setKey(key);
					((LabelElement) elem).text = Keyboard.getKeyName(key);
					return true;
				}
				return false;
			})
			.addListener((MouseListener) elem -> {
				boolean ret = false;
				if (Mouse.getEventButton() != -1) {
					if (elem.isMouseOver()) {
						this.selected = true;
						ret = true;
					} else {
						this.selected = false;
					}
				}
				if (this.selected) {
					elem.parentElement.setOutline(new OutlineElement<>().setColor(Colors.RED));
				} else {
					elem.parentElement.setOutline(null);
				}
				return ret;
			});
	}
}