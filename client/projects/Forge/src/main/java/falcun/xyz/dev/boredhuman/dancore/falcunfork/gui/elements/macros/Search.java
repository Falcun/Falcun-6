package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.macros;

import falcun.net.api.modules.config.FalcunValue;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.api.module.ModColor;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.fonts.DanFont;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.LabelElement;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.ElementListener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.KeyListener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.Listener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.MouseListener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util.Scissor;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.ColorUtil;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.GuiUtil;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.Pair;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.function.BiConsumer;

public class Search {
	// returns the label element for searching, and a dynamic boolean indicating whether the label is selected
	public static Pair<LabelElement, FalcunValue<Boolean>> makeSearch(FalcunValue<String> placeHolder, DanFont font, BiConsumer<LabelElement, String> callback) {

		FalcunValue<Boolean> selected = new FalcunValue<>(false);
		FalcunValue<Integer> indexStart = new FalcunValue<>(-1);
		FalcunValue<Integer> carretOffset = new FalcunValue<>(0);
		FalcunValue<Boolean> ctrlDown = new FalcunValue<>(false);

		return new Pair<>(new LabelElement(placeHolder.getValue(), font)
			.addListener((KeyListener) element -> {
				LabelElement label = (LabelElement) element;
				if (Keyboard.getEventKeyState() && selected.getValue()) {
					int keyCode = Keyboard.getEventKey();
					if (keyCode == Keyboard.KEY_BACK) {
						if (!label.text.isEmpty() && !label.text.equals(placeHolder.getValue())) {
							if (carretOffset.getValue() > label.text.length()) {
								carretOffset.setValue(label.text.length());
							}
							String left = label.text.substring(0, label.text.length() - carretOffset.getValue());
							String right = carretOffset.getValue() == 0 ? "" : label.text.substring(label.text.length() - carretOffset.getValue(), label.text.length());
							label.text = (left.length() == 0 ? "" : left.substring(0, left.length() - 1)) + right;
							callback.accept(label, label.text.equals(placeHolder.getValue()) ? "" : label.text);
						}
					} else if (keyCode == Keyboard.KEY_ESCAPE) {
						selected.setValue(false);
					} else if (keyCode == Keyboard.KEY_LEFT || keyCode == Keyboard.KEY_RIGHT){
						if (keyCode == Keyboard.KEY_LEFT) {
							carretOffset.setValue(Math.min(label.text.length(), carretOffset.getValue() + 1));
						} else {
							carretOffset.setValue(Math.max(0, carretOffset.getValue() - 1));
						}
					} else {
						char letter = Keyboard.getEventCharacter();
						if (Character.isLetter(letter) || Character.isDigit(letter) || Character.isSpaceChar(letter)) {
							if (label.text.equals(placeHolder.getValue())) {
								label.text = "";
							}
							if (label.text.isEmpty()) {
								label.text = label.text + Keyboard.getEventCharacter();
								callback.accept(label, label.text);
							} else {
								if (carretOffset.getValue() > label.text.length()) {
									carretOffset.setValue(label.text.length());
								}
								String left = label.text.substring(0, label.text.length() - carretOffset.getValue());
								String right = carretOffset.getValue() == 0 ? "" : label.text.substring(label.text.length() - carretOffset.getValue(), label.text.length());
								label.text = left + Keyboard.getEventCharacter() + right;
								callback.accept(label, label.text);
							}
						}
					}
				}
				return selected.getValue();
			})
			.addListener((ElementListener) (element, phase) -> {
				if (phase == Listener.Phase.POST) {
					if (selected.getValue() && System.currentTimeMillis() % 1500 < 750) {
						LabelElement label = (LabelElement) element;
						GlStateManager.color(ColorUtil.getRed(element.color), ColorUtil.getGreen(element.color), ColorUtil.getBlue(element.color), ColorUtil.getAlpha(element.color));
						GuiUtil.drawSquare(element.x + (int)label.font.getStringWidth(label.text.substring(0, label.text.length() - carretOffset.getValue())), Math.max(element.y, element.y + (element.height - label.font.size()) / 2), 1, Math.min(element.height, label.font.size()));
					}
				}
			})
			.addListener((MouseListener) element -> {
				if (Mouse.getEventButton() != -1 && Mouse.getEventButtonState()) {
					selected.setValue(element.isMouseOver());
					if (selected.getValue()) {
						LabelElement label = (LabelElement) element;
						if (!label.text.equals(placeHolder.getValue())) {
							int width = (int)label.font.getStringWidth(label.text);
							int x = Mouse.getX();
							if (x < element.x + width) {
								int index = 1;
								while (x < element.x + (int)label.font.getStringWidth(label.text.substring(0, label.text.length() - index))) {
									index += 1;
								}
								carretOffset.setValue(index);
							} else {
								carretOffset.setValue(0);
							}
						}
					}
				}
				return false;
			})
			.addListener((ElementListener) (element, phase) -> {
				if (phase == Listener.Phase.PRE) {
					if (((LabelElement) element).text.isEmpty()) {
						((LabelElement) element).text = placeHolder.getValue();
					}
				}
			})
			.addListener(new Scissor()),
			selected
		);
	}

	public static Pair<LabelElement, FalcunValue<Boolean>> makeHexBox(FalcunValue<String> placeHolder, DanFont font, BiConsumer<BasicElement<?>, String> callback, ModColor modColor) {

		FalcunValue<Boolean> selected = new FalcunValue<>(false);

		return new Pair<>(new LabelElement(placeHolder.getValue(), font)
			.addListener((KeyListener) element -> {
				LabelElement label = (LabelElement) element;
				if (Keyboard.getEventKeyState()) {
					int keyCode = Keyboard.getEventKey();
					if (keyCode == Keyboard.KEY_BACK) {
						if (!label.text.isEmpty()) {
							label.text = label.text.substring(0, label.text.length() - 1);
						}
					} else if (keyCode == Keyboard.KEY_ESCAPE) {
						selected.setValue(false);
					} else {
						char letter = Character.toUpperCase(Keyboard.getEventCharacter());
						if (((letter >= 'A' && letter <= 'F') || Character.isDigit(letter)) && label.text.length() < 6) {
							label.text = label.text + Character.toUpperCase(Keyboard.getEventCharacter());
							callback.accept(element, label.text);
						}
					}
				}
				return selected.getValue();
			})
			.addListener((MouseListener) element -> {
				if (Mouse.getEventButton() != -1 && Mouse.getEventButtonState()) {
					selected.setValue(element.isMouseOver());
				}
				return false;
			})
			.addListener((ElementListener) (element, phase) -> {
				if (phase == Listener.Phase.PRE) {
					if (!selected.getValue()) {
						((LabelElement) element).setText(String.format("%06X", modColor.getIntColor() & 0xFFFFFF));
					}
					if (((LabelElement) element).text.isEmpty()) {
						((LabelElement) element).text = placeHolder.getValue();
					}
				}
			})
			.addListener(new Scissor()),
			selected
		);
	}
}