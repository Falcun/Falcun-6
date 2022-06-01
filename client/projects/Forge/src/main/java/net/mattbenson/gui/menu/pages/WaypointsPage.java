package net.mattbenson.gui.menu.pages;

import java.awt.Color;

import mapwriter.Mw;
import mapwriter.map.Marker;
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
import net.mattbenson.gui.menu.components.mods.MenuModColorPicker;
import net.mattbenson.gui.menu.components.mods.ModCategoryButton;
import net.mattbenson.gui.menu.components.mods.ModScrollPane;
import net.mattbenson.gui.menu.components.waypoints.WaypointTextBarrier;
import net.mattbenson.network.utils.StringUtils;
import net.mattbenson.utils.NetworkUtils;
import net.mattbenson.waypoints.Waypoint;
import net.minecraft.client.Minecraft;

public class WaypointsPage extends Page {
	private MacroTextfield name;
	private MacroTextfield description;
	private WaypointTextBarrier xBarrier;
	private MacroTextfield x;
	private WaypointTextBarrier yBarrier;
	private MacroTextfield y;
	private WaypointTextBarrier zBarrier;
	private MacroTextfield z;
	private MenuModColorPicker color;
	private MacroButton button;
	private MacroButton delete;
	private ModScrollPane scrollPane;

	private boolean editing;
	
	public WaypointsPage(Minecraft mc, Menu menu, IngameMenu parent) {
		super(mc, menu, parent);
	}

	@Override
	public void onInit() {
		int width = 300;
		int x = menu.getWidth() - width + 20;
		int y = 59;
		
		int compWidth = width - 6 - 20 * 2;
		
		name = new MacroTextfield(TextPattern.NONE, x, y + 85, compWidth, 22, "...") ;
		description = new MacroTextfield(TextPattern.NONE, x, y + 155, compWidth, 22, "...");
		
		xBarrier = new WaypointTextBarrier("X", x, y + 195 - 1, 30, 24);
		this.x = new MacroTextfield(TextPattern.NUMBERS_ONLY, x + 30, y + 195, compWidth - 30, 22, "...") {
			@Override
			public void onAction() {
				editing = true;
			}
		};
		
		yBarrier = new WaypointTextBarrier("Y", x, y + 225 - 1, 30, 24);
		this.y = new MacroTextfield(TextPattern.NUMBERS_ONLY, x + 30, y + 225, compWidth - 30, 22, "...") {
			@Override
			public void onAction() {
				editing = true;
			}
		};
		
		zBarrier = new WaypointTextBarrier("Z", x, y + 255 - 1, 30, 24);
		this.z = new MacroTextfield(TextPattern.NUMBERS_ONLY, x + 30, y + 255, compWidth - 30, 22, "...") {
			@Override
			public void onAction() {
				editing = true;
			}
		};
		
		color = new MenuModColorPicker(x, menu.getHeight() - 22 - 20 - 40 - 35, compWidth, 22, Color.RED.getRGB());
		
		int acceptWidth = compWidth - 40;
		
		button = new MacroButton("ADD", x - 21 + width / 2 - acceptWidth / 2, menu.getHeight() - 22 - 20 - 40, acceptWidth, 22, true) {
			@Override
			public void onAction() {
				setActive(false);
				
				if(name.getText().isEmpty()) {
					return;
				}
				
				if(description.getText().isEmpty()) {
					return;
				}
				
				String xText = WaypointsPage.this.x.getText();
				String yText = WaypointsPage.this.y.getText();
				String zText = WaypointsPage.this.z.getText();
				
				if(xText.isEmpty()) {
					return;
				}

				if(yText.isEmpty()) {
					return;
				}

				if(zText.isEmpty()) {
					return;
				}
				
				if(!StringUtils.isInteger(xText) || !StringUtils.isInteger(yText) || !StringUtils.isInteger(zText)) {
					return;
				}
				
				int xPos = Integer.parseInt(xText);
				int yPos = Integer.parseInt(yText);
				int zPos = Integer.parseInt(zText);
				
				Mw.getInstance().markerManager.addMarker(name.getText(), description.getText(), xPos, yPos, zPos, mc.thePlayer.dimension, color.getColor().getRGB(), NetworkUtils.getServer());
				
				name.setText("");
				description.setText("");
				WaypointsPage.this.x.setText("");
				WaypointsPage.this.y.setText("");
				WaypointsPage.this.z.setText("");
				Mw.getInstance().markerManager.update();
				
				editing = false;
				
				populateScrollPane();
			}
		};
		
		delete = new MacroButton("CLEAR ALL WAYPOINTS", x - 21 + width / 2 - compWidth / 2, menu.getHeight() - 22 - 20, compWidth, 22, false) {
			@Override
			public void onAction() {
				setActive(false);
				
				Mw.getInstance().markerManager.clear();
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
		
		for(Marker waypoint : Mw.getInstance().markerManager.markerList) {
			scrollPane.addComponent(new MacroBase(waypoint.name, x, y, width, height));
			
			MacroSlimTextField field = new MacroSlimTextField(TextPattern.NONE, x + 160 + spacing, y, width - 160 - spacing * 4 - 90, height - 5) {
				@Override
				public void onAction() {
					waypoint.groupName = getText();
				}
			};
			
			field.setText(waypoint.groupName);
			
			scrollPane.addComponent(field);
			
			FlipButton flipButton = new FlipButton(width - spacing - 90, y, 90 - 10, height) {
				@Override
				public void onAction() {
					waypoint.visible = isActive();
				}
			};
			
			flipButton.setActive(waypoint.visible);
			
			scrollPane.addComponent(flipButton);
			
			scrollPane.addComponent(new SimpleTextButton("X", width - spacing, y, 30, height, true) {
				@Override
				public void onAction() {
					Mw.getInstance().markerManager.delMarker(waypoint);
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
		
		Fonts.RobotoTitle.drawString("WAYPOINTS", menu.getX() + 31, menu.getY() + 80, IngameMenu.MENU_HEADER_TEXT_COLOR);

		drawHorizontalLine(menu.getX() + 31, menu.getY() + 110, menu.getWidth() - width - 31 * 2, 3, IngameMenu.MENU_LINE_COLOR);
		
		drawRectFalcun(menu.getX() + menu.getWidth() - width, menu.getY() + 58, width, menu.getHeight() - 58, MacrosPage.MENU_SIDE_BG_COLOR);
		
		drawRectFalcun(menu.getX() + menu.getWidth() - width, menu.getY() + 58, width, height + 1, ModCategoryButton.MAIN_COLOR);
		drawShadowDown(menu.getX() + menu.getWidth() - width, y + height, width);
		Fonts.RobotoMiniHeader.drawString("ADD NEW WAYPOINT", menu.getX() + menu.getWidth() - width / 2 - Fonts.RobotoMiniHeader.getStringWidth("ADD NEW WAYPOINT") / 2, y + height / 2 - Fonts.RobotoMiniHeader.getStringHeight("ADD NEW WAYPOINT") / 2, IngameMenu.MENU_HEADER_TEXT_COLOR);
		
		drawShadowDown(menu.getX() + menu.getWidth() - width, y - 1, width);
		
		y += 60;
		
		Fonts.Roboto.drawString("ENTER NAME", x, y, IngameMenu.MENU_HEADER_TEXT_COLOR);
		
		y += 70;
		
		Fonts.Roboto.drawString("DESCRIPTION", x, y, IngameMenu.MENU_HEADER_TEXT_COLOR);
		
		y += 70;
		y += 30;
		y += 30;
		y += 30;
		
		Fonts.Roboto.drawString("COLOR", x, y, IngameMenu.MENU_HEADER_TEXT_COLOR);
	}
	
	@Override
	public void onLoad() {
		editing = false;
		onOpen();
		
		menu.addComponent(name);
		menu.addComponent(description);
		menu.addComponent(x);
		menu.addComponent(xBarrier);
		menu.addComponent(y);
		menu.addComponent(yBarrier);
		menu.addComponent(z);
		menu.addComponent(zBarrier);
		menu.addComponent(color);
		menu.addComponent(button);
		menu.addComponent(delete);
		menu.addComponent(scrollPane);
	}

	@Override
	public void onUnload() {
	}
	
	@Override
	public void onOpen() {
		if(!editing) {
			x.setText(mc.thePlayer.getPosition().getX() + "");
			y.setText(mc.thePlayer.getPosition().getY() + "");
			z.setText(mc.thePlayer.getPosition().getZ() + "");
		}
	}
	
	@Override
	public void onClose() {
		Mw.getInstance().markerManager.update();
	}
}