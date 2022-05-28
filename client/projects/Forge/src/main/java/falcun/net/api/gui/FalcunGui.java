package falcun.net.api.gui;

import falcun.net.api.gui.pages.FalcunGuiPage;
import falcun.net.util.FalcunDevEnvironment;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.ElementRenderer;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.KeyListener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.MouseListener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util.TypeFetcher;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.Pair;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class FalcunGui extends GuiScreen {

	private static int lastWidth = -1, lastHeight = -1;
	public static boolean forceRefresh = true;

	@Override
	public final void initGui() {
		if (FalcunDevEnvironment.isDevEnvironment || (lastWidth != width || lastHeight != height) || forceRefresh) {
			init();
		}
		lastWidth = width;
		lastHeight = height;

	}


	protected abstract void init();

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int old = this.mc.gameSettings.guiScale;
		this.mc.gameSettings.guiScale = 1;
		this.mc.entityRenderer.setupOverlayRendering();
		this.mc.gameSettings.guiScale = old;
		Arrays.stream(getElements()).forEach(ElementRenderer::renderElements);
	}

	@Override
	public void handleMouseInput() {
		List<Pair<BasicElement<?>, MouseListener>> clickListenersList = new ArrayList<>();
		for (List<BasicElement<?>> element : getElements()) {
			for (BasicElement<?> basicElement : element) {
				clickListenersList.addAll(TypeFetcher.getListenerType(MouseListener.class, basicElement));
			}
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
		for (List<BasicElement<?>> elementList : getElements()) {
			for (BasicElement<?> element : elementList) {
				keyListenerList.addAll(TypeFetcher.getListenerType(KeyListener.class, element));
			}
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

	protected abstract List<BasicElement<?>>[] getElements();

	protected static List<BasicElement<?>>[] grabElements(FalcunGuiPage... pages) {
		List<BasicElement<?>>[] elements = new LinkedList[pages.length];
		for (int i = 0; i < elements.length; i++) {
			elements[i] = pages[i].getElements();
		}
		return elements;
	}

	protected static List<BasicElement<?>>[] grabElements(List<BasicElement<?>>... elements) {
		return elements;
	}
}
