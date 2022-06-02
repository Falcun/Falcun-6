package falcun.net.api.guidragon.components.text;

import falcun.net.api.fonts.FalcunFont;
import falcun.net.api.fonts.Fonts;
import falcun.net.api.guidragon.components.Component;
import falcun.net.api.guidragon.components.rect.ColorSquare;
import falcun.net.api.guidragon.components.shape.CurvedShape;
import falcun.net.api.guidragon.components.shape.CurvedShapeOutline;
import falcun.net.api.guidragon.effects.Effect;
import falcun.net.api.guidragon.effects.OnHoverEffect;
import falcun.net.api.guidragon.effects.ScissorEffect;
import falcun.net.api.guidragon.region.GuiRegion;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TextInputBox extends Component {

	private final Consumer<String> onChange;
	public boolean onEveryChange = false;

	public void setPlaceHolder(String placeHolder) {
		this.placeHolder = placeHolder;
	}

	private String placeHolder;
	public final Label label;
	private StringBuilder currentString = new StringBuilder();
	private final GuiRegion blinkerRegion;
	private boolean isActive;
	private int stringWidth = 0;
	private long inputCooldown = 0;
	public boolean disabled = false;
	public boolean center = false;
	public boolean isInScroll = true;
	private boolean wasCenter = false;

//	public TextInputBox(GuiRegion region, int bordercolor, int hovercolor, int backgroundcolor, int textcolor, Consumer<String> onChange, String placeHolder) {
////		this(region, ()-> bordercolor, ()-> hovercolor, ()-> hovercolor, ()-> backgroundcolor, ()-> textcolor, onChange, placeHolder);
//		this(region, ()-> bordercolor, ()-> hovercolor, ()-> hovercolor, ()-> backgroundcolor, ()-> textcolor, onChange, placeHolder);
//	}

	public TextInputBox(GuiRegion region, Supplier<Integer> borderColor, Supplier<Integer> hoverColor, Supplier<Integer> backgroundColor, Supplier<Integer> textColor, Consumer<String> onChange, String placeHolder, FalcunFont font) {
		super(region);

		this.onChange = onChange;
		this.placeHolder = placeHolder;

		CurvedShape backgroundShape = new CurvedShape(region, backgroundColor, 2);
		backgroundShape.pinTo(this);
		CurvedShapeOutline outline = new CurvedShapeOutline(region, borderColor, 2, 2);
		outline.pinTo(this);

		if (hoverColor != null) outline.effects.add(new OnHoverEffect((component, phase) -> {
			if (phase == Effect.Phase.BEFORE) outline.color = hoverColor;
			else outline.color = isActive ? hoverColor : borderColor;
		}));

		this.subComponents.add(backgroundShape);
		this.subComponents.add(outline);

		GuiRegion labelRegion = region.offSet(2, 0).offsetSize(-4, 0);
		this.label = new Label(labelRegion, () -> {
			if (!this.isActive && this.placeHolder != null && this.currentString.length() <= 0)
				return placeHolder;
			else {
				return currentString.toString();
			}
		}, this.center ? Label.CENTER : Label.LEFT, textColor, font);
		label.effects.add(new ScissorEffect(null));
		label.pinTo(this);

		this.blinkerRegion = new GuiRegion(region.x + 3, this.region.y + 1, 1, region.height - 2);
		ColorSquare blinker = new ColorSquare(this.blinkerRegion, () -> {
			if (!(System.currentTimeMillis() % 1000 < 500) && this.isActive) {
				this.blinkerRegion.x = label.region.x + (Math.min((int) label.font.getStringWidth(label.text.get()) + 1, label.region.width - 1));
				return textColor.get();
			} else return 0x00FFFFFF;
		});
		blinker.effects.add(new ScissorEffect(null));
		blinker.pinTo(this);
		this.subComponents.add(blinker);
	}

	public TextInputBox(GuiRegion region, Supplier<Integer> borderColor, Supplier<Integer> backgroundColor, Supplier<Integer> textColor, Consumer<String> onChange, String placeHolder) {
		this(region, borderColor, null, backgroundColor, textColor, onChange, placeHolder, Fonts.Roboto);
	}

	public TextInputBox(GuiRegion region, Supplier<Integer> borderColor, Supplier<Integer> hoverColor, Supplier<Integer> backgroundColor, Supplier<Integer> textColor, Consumer<String> onChange, String placeHolder, boolean onEveryChange) {
		this(region, borderColor, null, backgroundColor, textColor, onChange, placeHolder, Fonts.Roboto);
		this.onEveryChange = onEveryChange;
	}

	@Override
	public void draw(int mX, int mY) {
		label.alignment = this.center ? () -> 1 : () -> 0;
	}

	@Override
	public void afterDraw(int mX, int mY) {
		if (stringWidth > label.region.width) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(-(stringWidth - region.width) - 5, 0, 0);
		}

		if (isInScroll) {
			label.preDraw(mX, mY);
		}
		label.draw(mX, mY);
		if (isInScroll) {
			label.afterDraw(mX, mY);
		}
		if (stringWidth > label.region.width) {
			GlStateManager.popMatrix();
		}
	}

	@Override
	public void onClicked(int mX, int mY, int mouseButton) {
		if (!Mouse.getEventButtonState()) return;
		if (disabled) return;
		this.isActive = isOver(mX, mY);
		if (isActive) {
			wasCenter = center;
		} else {
			center = wasCenter;
		}
	}

	@Override
	public void onKey(int keyCode, char letter) {
		if (!this.isActive) return;
		this.center = false;
		if (keyCode == Keyboard.KEY_BACK) {
			if (currentString.length() < 1 || this.inputCooldown >= System.currentTimeMillis()) return;
//			this.stringWidth -= MainGui.exo2.getStringWidth(String.valueOf(currentString.charAt(currentString.length() - 1)));
			this.stringWidth -= (int) label.font.getStringWidth(String.valueOf(currentString.charAt(currentString.length() - 1)));
			currentString.deleteCharAt(currentString.length() - 1);
			inputCooldown = System.currentTimeMillis() + 10;
			if (onEveryChange && this.onChange != null) this.onChange.accept(currentString.toString());
			return;
		}

		if (GuiScreen.isKeyComboCtrlV(keyCode)) {
			if (this.inputCooldown >= System.currentTimeMillis()) return;
			String text = GuiScreen.getClipboardString();
			currentString.append(text);
			this.stringWidth += (int) label.font.getStringWidth(text);
			inputCooldown = System.currentTimeMillis() + 10;
			if (this.onChange != null) this.onChange.accept(currentString.toString());
			return;
		}

		if (!ChatAllowedCharacters.isAllowedCharacter(letter) || this.inputCooldown >= System.currentTimeMillis())
			return;

		currentString.append(letter);
		this.stringWidth += (int)label.font.getStringWidth(String.valueOf(letter));
		inputCooldown = System.currentTimeMillis() + 10;
		if (this.onChange != null) this.onChange.accept(currentString.toString());
	}

	public String getCurrentString() {
		return currentString.toString();
	}

	public void setCurrentString(String string) {
		this.currentString = new StringBuilder();
		currentString.append(string);
		this.stringWidth += (int) label.font.getStringWidth(string);
		inputCooldown = System.currentTimeMillis() + 10;
//        if (this.onChange != null) this.onChange.accept(currentString.toString());

	}

	public void setString(String string) {
		this.currentString = new StringBuilder();
		currentString.append(string);
		this.stringWidth = (int) label.font.getStringWidth(string);
		inputCooldown = System.currentTimeMillis() + 10;
	}

}