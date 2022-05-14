package net.mattbenson.gui.menu.pages;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.mattbenson.Falcun;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.Menu;
import net.mattbenson.gui.framework.TextPattern;
import net.mattbenson.gui.menu.IngameMenu;
import net.mattbenson.gui.menu.Page;
import net.mattbenson.gui.menu.components.macros.FlipButton;
import net.mattbenson.gui.menu.components.macros.MacroBase;
import net.mattbenson.gui.menu.components.macros.MacroButton;
import net.mattbenson.gui.menu.components.macros.MacroSlimTextField;
import net.mattbenson.gui.menu.components.macros.MacroTextfield;
import net.mattbenson.gui.menu.components.macros.SimpleTextButton;
import net.mattbenson.gui.menu.components.mods.MenuModKeybind;
import net.mattbenson.gui.menu.components.mods.ModCategoryButton;
import net.mattbenson.gui.menu.components.mods.ModScrollPane;
import net.mattbenson.macros.Macro;
import net.minecraft.client.Minecraft;

public class MacrosPage extends Page {
	public final int MENU_SIDE_HEADER_BG_COLOR = new Color(18, 17, 22, IngameMenu.MENU_ALPHA).getRGB();
	public final static int MENU_SIDE_BG_COLOR = new Color(18, 17, 22, IngameMenu.MENU_ALPHA).getRGB();
	
	private MacroTextfield name;
	private MacroTextfield commandLine;
	private MacroButton button;
	private MacroButton delete;
	private MenuModKeybind bind;
	private ModScrollPane scrollPane;
	
	public MacrosPage(Minecraft mc, Menu menu, IngameMenu parent) {
		super(mc, menu, parent);
	}

	@Override
	public void onInit() {
		int width = 300;
		int x = menu.getWidth() - width + 20;
		int y = 59;
		
		int compWidth = width - 6 - 20 * 2;
		
		name = new MacroTextfield(TextPattern.NONE, x, y + 85, compWidth, 22, "...") ;
		commandLine = new MacroTextfield(TextPattern.NONE, x, y + 155, compWidth, 22, "...");
		bind = new MenuModKeybind(x, y + 225, compWidth, 22);
		
		int acceptWidth = compWidth - 40;
		
		button = new MacroButton("ADD", x - 21 + width / 2 - acceptWidth / 2, y + 265, acceptWidth, 22, true) {
			@Override
			public void onAction() {
				setActive(false);
				
				if(name.getText().isEmpty()) {
					return;
				}
				
				if(commandLine.getText().isEmpty()) {
					return;
				}
				
				if(!bind.isBound()) {
					return;
				}
				
				Falcun.getInstance().macroManager.getMacros().add(new Macro(name.getText(), commandLine.getText(), bind.getBind()));
				
				name.setText("");
				commandLine.setText("");
				bind.setBind(Keyboard.CHAR_NONE);
				populateScrollPane();
			}
		};
		
		delete = new MacroButton("CLEAR ALL MACROS", x - 21 + width / 2 - compWidth / 2, y = menu.getHeight() - 22 - 20, compWidth, 22, false) {
			@Override
			public void onAction() {
				setActive(false);
				
				Falcun.getInstance().macroManager.getMacros().clear();
				populateScrollPane();
			}
		};
		
		scrollPane = new ModScrollPane(31, 140, menu.getWidth() - width - 31 * 2, menu.getHeight() - 141, false);
		
		populateScrollPane();
	}
	
	private void populateScrollPane() {
		scrollPane.getComponents().clear();
		
		int spacing = 15;
		int height = 30;

		int y = spacing;
		int x = spacing;
		
		int width = scrollPane.getWidth() - spacing * 2;
		
		for(Macro macro : Falcun.getInstance().macroManager.getMacros()) {
			scrollPane.addComponent(new MacroBase(macro.getName(), x, y, width, height));
			
			MacroSlimTextField field = new MacroSlimTextField(TextPattern.NONE, x + 160 + spacing, y, width - 160 - spacing * 4 - 90, height - 5) {
				@Override
				public void onAction() {
					macro.setCommand(getText());
				}
			};
			
			field.setText(macro.getCommand());
			
			scrollPane.addComponent(field);
			
			FlipButton flipButton = new FlipButton(width - spacing - 90, y, 90 - 10, height) {
				@Override
				public void onAction() {
					macro.setEnabled(isActive());
				}
			};
			
			flipButton.setActive(macro.isEnabled());
			
			scrollPane.addComponent(flipButton);
			
			scrollPane.addComponent(new SimpleTextButton("X", width - spacing, y, 30, height, true) {
				@Override
				public void onAction() {
					Falcun.getInstance().macroManager.getMacros().remove(macro);
					populateScrollPane();
				}
			});
			
			y += height + spacing;
		}
	}

	@Override
	public void onRender() {
		int width = 300;
		int x = menu.getX() + menu.getWidth() - width + 20;
		int y = menu.getY() + 59;
		int height = 32;
		
		Fonts.RobotoTitle.drawString("MACROS", menu.getX() + 31, menu.getY() + 80, IngameMenu.MENU_HEADER_TEXT_COLOR);

		drawHorizontalLine(menu.getX() + 31, menu.getY() + 110, menu.getWidth() - width - 31 * 2, 3, IngameMenu.MENU_LINE_COLOR);
		
		drawRectFalcun(menu.getX() + menu.getWidth() - width, menu.getY() + 58, width, menu.getHeight() - 58, MENU_SIDE_BG_COLOR);
		
		drawRectFalcun(menu.getX() + menu.getWidth() - width, menu.getY() + 58, width, height + 1, ModCategoryButton.MAIN_COLOR);
		drawShadowDown(menu.getX() + menu.getWidth() - width, y + height, width);
		Fonts.RobotoMiniHeader.drawString("ADD NEW MACRO", menu.getX() + menu.getWidth() - width / 2 - Fonts.RobotoMiniHeader.getStringWidth("ADD NEW MACRO") / 2, y + height / 2 - Fonts.RobotoMiniHeader.getStringHeight("ADD NEW MACRO") / 2, IngameMenu.MENU_HEADER_TEXT_COLOR);
		
		drawShadowDown(menu.getX() + menu.getWidth() - width, y - 1, width);
		
		y += 60;
		
		Fonts.Roboto.drawString("ENTER NAME", x, y, IngameMenu.MENU_HEADER_TEXT_COLOR);
		
		y += 70;
		
		Fonts.Roboto.drawString("ENTER COMMAND LINE", x, y, IngameMenu.MENU_HEADER_TEXT_COLOR);
		
		y += 70;
		
		Fonts.Roboto.drawString("ADD KEYBIND", x, y, IngameMenu.MENU_HEADER_TEXT_COLOR);
	}

	@Override
	public void onLoad() {
		menu.addComponent(name);
		menu.addComponent(commandLine);
		menu.addComponent(bind);
		menu.addComponent(button);
		menu.addComponent(delete);
		menu.addComponent(scrollPane);
	}

	@Override
	public void onUnload() {
		
	}
	
	@Override
	public void onOpen() {
		
	}
	
	@Override
	public void onClose() {
		
	}
}
