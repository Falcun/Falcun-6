package mapwriter.gui;

import java.io.IOException;

import mapwriter.Mw;
import mapwriter.api.MwAPI;
import mapwriter.fac.Faction;
import mapwriter.map.Marker;
import mapwriter.map.MarkerManager;
import mapwriter.util.Utils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/* TODO:
 *  - Clean up this mess of a file and any others. */

@SideOnly(Side.CLIENT)
public class MwGuiFactionDialog extends GuiScreen {
	private final GuiScreen parentScreen;
	
	ScrollableTextBox scrollableTextBoxName = null;
	ScrollableTextBox scrollableTextBoxImage = null;
	ScrollableSingularColorSelector scrollableSingularColor = null;
	ScrollableColorSelector scrollableColorSelectorColor = null;
	
	GuiButton buttonSubmit = null;
	GuiButton buttonCancel = null;
	
	boolean backToGameOnSubmit = false;
	static final int dialogWidthPercent = 40;
	static final int elementVSpacing = 20;
	static final int numberOfElements = 7;
	private Faction editingFaction;
	private String factionName = "";
	private int colour = 0;
	
	public MwGuiFactionDialog(MwGui mw, Faction editingFaction) {
		this.parentScreen = mw;
		
		this.editingFaction = editingFaction;
		this.factionName = editingFaction.getName();
		this.colour = (int) Long.parseLong("FF" + editingFaction.getColor(), 16);
	}
	
	public boolean submit() {
		boolean inputCorrect = true;
		
		// Check if name already exists, update new names claims, delete old name.
		if (this.scrollableTextBoxName.validateTextFieldData()) {
			this.factionName = this.scrollableTextBoxName.getText();
		} else inputCorrect = false;
		
		if (this.scrollableColorSelectorColor.validateColorData()) {
			this.colour = this.scrollableColorSelectorColor.getColor();
		} else inputCorrect = false;
		
		if (inputCorrect) {
			String r = Integer.toHexString((this.colour >> 16) & 0xff);
			String g = Integer.toHexString((this.colour >> 8) & 0xff);
			String b = Integer.toHexString(this.colour & 0xff);
			
			if (r.length() == 1) r += r;
			if (g.length() == 1) g += g;
			if (b.length() == 1) b += b;
			
			this.editingFaction.updateColor(r + g + b);// , "33", "CC"
			this.editingFaction.setTexture(this.scrollableTextBoxImage.getText());
			
			Mw.getInstance().facInput.changeFactionName(this.editingFaction.getName(), factionName);
		}
		
		return inputCorrect;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		int labelsWidth = this.fontRendererObj.getStringWidth("Group");
		int width = ((this.width * dialogWidthPercent) / 100) - labelsWidth - 20;
		int x = ((this.width - width) + labelsWidth) / 2;
		int y = (this.height - (elementVSpacing * numberOfElements)) / 2;
		
		this.scrollableTextBoxName = new ScrollableTextBox(
			x,
			y,
			width,
			I18n.format("mw.gui.mwguifactiondialog.editfactionName", new Object[0]),
			this.fontRendererObj);
		this.scrollableTextBoxName.setText(this.factionName);
		
		this.scrollableTextBoxImage = new ScrollableTextBox(
			x,
			y + MwGuiFactionDialog.elementVSpacing,
			width,
			I18n.format("mw.gui.mwguifactiondialog.editfactionImage", new Object[0]),
			this.fontRendererObj);
		this.scrollableTextBoxImage.textField.setMaxStringLength(150);
		this.scrollableTextBoxImage.setText(this.editingFaction.getTexture() == null ? "" : this.editingFaction.getTexture().toString());
		
		this.scrollableColorSelectorColor = new ScrollableColorSelector(
			x,
			y + MwGuiFactionDialog.elementVSpacing * 3,
			width, 
			I18n.format("mw.gui.mwguifactiondialog.editfactionColor", new Object[0]),
			this.fontRendererObj);
		this.scrollableColorSelectorColor.setColor(this.colour);
		this.scrollableColorSelectorColor.setDrawArrows(true);
		
		this.scrollableSingularColor = new ScrollableSingularColorSelector(
			x,
			y + MwGuiFactionDialog.elementVSpacing * 2,
			width,
			"Default",
			this.fontRendererObj,
			this.scrollableColorSelectorColor);
		
		this.buttonSubmit = new GuiButton(0, x - labelsWidth/2, y + MwGuiFactionDialog.elementVSpacing * 6 - 3, width/2 - 1, 20, "Submit");
		this.buttonCancel = new GuiButton(1, x + (width - labelsWidth)/2 + 1, y + MwGuiFactionDialog.elementVSpacing * 6 - 3, width/2 - 1, 20, "Cancel");
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float f) {
		if (this.parentScreen != null) {
			this.parentScreen.drawScreen(mouseX, mouseY, f);
		} else {
			this.drawDefaultBackground();
		}

		int w = (this.width * MwGuiFactionDialog.dialogWidthPercent) / 100;
		
		drawRect(
				(this.width - w) / 2,
				((this.height - (MwGuiFactionDialog.elementVSpacing * (numberOfElements + 2))) / 2) - 4,
				((this.width - w) / 2) + w,
				((this.height - (MwGuiFactionDialog.elementVSpacing * (numberOfElements + 2))) / 2)
						+ (MwGuiFactionDialog.elementVSpacing * (numberOfElements + 1)), 0x80000000);
		this.drawCenteredString(this.fontRendererObj, I18n.format("mw.gui.mwguifactiondialog.title.edit", new Object[0]), (this.width) / 2, ((this.height - (MwGuiFactionDialog.elementVSpacing * (numberOfElements + 1))) / 2) - (MwGuiFactionDialog.elementVSpacing / 4), 0xffffff);
		
		this.scrollableTextBoxName.draw();
		this.scrollableTextBoxImage.draw();
		this.scrollableSingularColor.draw();
		this.scrollableColorSelectorColor.draw();
		this.buttonSubmit.drawButton(mc, mouseX, mouseY);
		this.buttonCancel.drawButton(mc, mouseX, mouseY);
		
		super.drawScreen(mouseX, mouseY, f);
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		int x = (Mouse.getEventX() * this.width) / this.mc.displayWidth;
		int y = this.height - ((Mouse.getEventY() * this.height) / this.mc.displayHeight) - 1;
		int direction = Mouse.getEventDWheel();
		if (direction != 0) this.mouseDWheelScrolled(x, y, direction);
		
		super.handleMouseInput();
	}
	
	public void mouseDWheelScrolled(int x, int y, int direction) {
		this.scrollableTextBoxName.mouseDWheelScrolled(x, y, direction);
		this.scrollableTextBoxImage.mouseDWheelScrolled(x, y, direction);
		this.scrollableColorSelectorColor.mouseDWheelScrolled(x, y, direction);
	}
	
	@Override
	protected void mouseClicked(int x, int y, int button) throws IOException {
		this.scrollableTextBoxName.mouseClicked(x, y, button);
		this.scrollableTextBoxImage.mouseClicked(x, y, button);
		this.scrollableSingularColor.mouseClicked(x, y, button);
		this.scrollableColorSelectorColor.mouseClicked(x, y, button);
		
		if(this.buttonSubmit.mousePressed(mc, x, y))
			if (this.submit()) {
				this.mc.displayGuiScreen(this.parentScreen);
			} else {
				this.mc.displayGuiScreen(null);
			}
		if(this.buttonCancel.mousePressed(mc, x, y))
			this.mc.displayGuiScreen(this.parentScreen);
	}
	
	@Override
	protected void keyTyped(char c, int key) {
		switch (key) {
			case Keyboard.KEY_ESCAPE:
				this.mc.displayGuiScreen(this.parentScreen);
				break;
			case Keyboard.KEY_RETURN:
				// when enter pressed, submit current input
				if (this.submit()) {
					this.mc.displayGuiScreen(this.parentScreen);
				} else {
					this.mc.displayGuiScreen(null);
				}
				break;
			default:
				this.scrollableTextBoxName.KeyTyped(c, key);
				this.scrollableTextBoxImage.KeyTyped(c, key);
				this.scrollableColorSelectorColor.KeyTyped(c, key);
				break;
		}
	}
}