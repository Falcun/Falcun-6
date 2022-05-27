package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui;

import falcun.net.api.fonts.FalcunFont;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.LabelElement;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.KeyListener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.MouseListener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util.Alignment;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util.TypeFetcher;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.Pair;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BasicGuiHandler extends GuiScreen {

	public static LabelElement createLabel(String text, FalcunFont font) {
		return createLabel(text, font, Alignment.Vertical.CENTER, Alignment.Horizontal.CENTER);
	}


	public static LabelElement createLabel(String text, FalcunFont font, Alignment.Horizontal horizAlign) {
		return createLabel(text, font, Alignment.Vertical.CENTER, horizAlign);
	}

	public static LabelElement createLabel(String text, FalcunFont font, Alignment.Vertical vertAlign) {
		return createLabel(text, font, vertAlign, Alignment.Horizontal.CENTER);
	}

	public static LabelElement createLabel(String text, FalcunFont font, Alignment.Vertical vertAlign, Alignment.Horizontal horizAlign) {
		return new LabelElement(text, font).setVerticalAlignment(vertAlign).setHorizontalAlignment(horizAlign);
	}

	public static LabelElement createLabel(String text, FalcunFont font, Alignment.Vertical vertAlign, Alignment.Horizontal horizAlign, int color) {
		return new LabelElement(text, font).setVerticalAlignment(vertAlign).setHorizontalAlignment(horizAlign).setColor(color);
	}

	public static LabelElement createLabel(String text, FalcunFont font, int vert, int horiz, int color) {
		Alignment.Horizontal h = horiz == 0 ? Alignment.Horizontal.LEFT : horiz == 1 ? Alignment.Horizontal.CENTER : Alignment.Horizontal.RIGHT;
		Alignment.Vertical v = vert == 0 ? Alignment.Vertical.TOP : vert == 1 ? Alignment.Vertical.CENTER : Alignment.Vertical.BOTTOM;
		return new LabelElement(text, font).setVerticalAlignment(v).setHorizontalAlignment(h).setColor(color);
	}

	public static LabelElement createLabel(String text, FalcunFont font, int vert, int horiz) {
		Alignment.Horizontal h = horiz == 0 ? Alignment.Horizontal.LEFT : horiz == 1 ? Alignment.Horizontal.CENTER : Alignment.Horizontal.RIGHT;
		Alignment.Vertical v = vert == 0 ? Alignment.Vertical.TOP : vert == 1 ? Alignment.Vertical.CENTER : Alignment.Vertical.BOTTOM;
		return new LabelElement(text, font).setVerticalAlignment(v).setHorizontalAlignment(h);
	}

	public List<BasicElement<?>> elements = new ArrayList<>();

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int old = this.mc.gameSettings.guiScale;
		this.mc.gameSettings.guiScale = 1;
		this.mc.entityRenderer.setupOverlayRendering();
		this.mc.gameSettings.guiScale = old;
		ElementRenderer.renderElements(this.elements);
	}

	@Override
	public void handleMouseInput() {
		List<Pair<BasicElement<?>, MouseListener>> clickListenersList = new ArrayList<>();
		for (BasicElement<?> element : this.elements) {
			clickListenersList.addAll(TypeFetcher.getListenerType(MouseListener.class, element));
		}
		for (int i = clickListenersList.size() - 1; i > -1; i--) {
			Pair<BasicElement<?>, MouseListener> elementListenerPair = clickListenersList.get(i);
			if (elementListenerPair.second.onMouseEvent(elementListenerPair.first)) {
				// System.out.println("mouse click consumed by: " + elementListenerPair.first.getClass().getName());
				break;
			}
		}
	}

	@Override
	public void handleKeyboardInput() throws IOException {
		List<Pair<BasicElement<?>, KeyListener>> keyListenerList = new ArrayList<>();
		for (BasicElement<?> element : this.elements) {
			keyListenerList.addAll(TypeFetcher.getListenerType(KeyListener.class, element));
		}
		boolean consumed = false;
		for (Pair<BasicElement<?>, KeyListener> keyListenerPair : keyListenerList) {
			BasicElement<?> element = keyListenerPair.first;
			if (keyListenerPair.second.onKeyEvent(element)) {
				consumed = true;
				break;
			}
		}
		if (!consumed && Keyboard.getEventKeyState() && Keyboard.getEventKey() == 1) {
			super.handleKeyboardInput();
		}
	}
}