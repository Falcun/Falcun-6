package net.mattbenson.gui.menu.pages;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.mattbenson.Falcun;
import net.mattbenson.config.ConfigEntry;
import net.mattbenson.config.types.BooleanEntry;
import net.mattbenson.config.types.ColorEntry;
import net.mattbenson.config.types.DoubleEntry;
import net.mattbenson.config.types.FloatEntry;
import net.mattbenson.config.types.IntEntry;
import net.mattbenson.config.types.ListEntry;
import net.mattbenson.config.types.StringEntry;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.BindType;
import net.mattbenson.gui.framework.Menu;
import net.mattbenson.gui.framework.MenuComponent;
import net.mattbenson.gui.framework.TextPattern;
import net.mattbenson.gui.framework.components.MenuButton;
import net.mattbenson.gui.framework.components.MenuTextField;
import net.mattbenson.gui.hud.HUDEditor;
import net.mattbenson.gui.menu.IngameMenu;
import net.mattbenson.gui.menu.Page;
import net.mattbenson.gui.menu.components.mods.FeatureText;
import net.mattbenson.gui.menu.components.mods.FeatureValueText;
import net.mattbenson.gui.menu.components.mods.GoBackButton;
import net.mattbenson.gui.menu.components.mods.MenuModCheckbox;
import net.mattbenson.gui.menu.components.mods.MenuModColorPicker;
import net.mattbenson.gui.menu.components.mods.MenuModKeybind;
import net.mattbenson.gui.menu.components.mods.MenuModList;
import net.mattbenson.gui.menu.components.mods.MenuModSlider;
import net.mattbenson.gui.menu.components.mods.ModCategoryButton;
import net.mattbenson.gui.menu.components.mods.ModScrollPane;
import net.mattbenson.gui.menu.components.mods.ModTextbox;
import net.mattbenson.gui.menu.components.mods.ModsButton;
import net.mattbenson.gui.menu.components.mods.ModuleBox;
import net.mattbenson.gui.menu.components.mods.SearchTextfield;
import net.mattbenson.gui.profile.ProfileViewer;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.modules.NonViewableModule;
import net.mattbenson.modules.types.render.Crosshair;
import net.mattbenson.utils.AssetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ModsPage extends Page {
	public final int MENU_HEADER_TEXT_COLOR_MOD = new Color(129, 129, 129, IngameMenu.MENU_ALPHA).getRGB();
	
	public final static int MENU_BG_COLOR_MOD = new Color(18, 17, 22, IngameMenu.MENU_ALPHA).getRGB();
	public final int MENU_BG_COLOR_MOD_BORDER = new Color(41, 40, 42, IngameMenu.MENU_ALPHA).getRGB();
	public final static int MENU_SIDE_BG_COLOR = new Color(18, 17, 22, IngameMenu.MENU_ALPHA).getRGB();
	
	private final ResourceLocation[] MOD_TABS = new ResourceLocation[ModuleCategory.values().length];
	
	private ModuleCategory modCategory = ModuleCategory.ALL_MODS;
	public Module activeModule;
	private String search;
	
	private HUDEditor hudEditor;
	
	private ProfileViewer profileViewer;
	
	public ModsPage(Minecraft mc, Menu menu, IngameMenu parent) {
		super(mc, menu, parent);
		
		hudEditor = new HUDEditor(parent.getFeature());
		profileViewer = new ProfileViewer(parent.getFeature());
	}
	
	@Override
	public void onInit() {
		for(ModuleCategory category : ModuleCategory.values()) {
			String icon = category.getIcon();
			
			if(icon.trim().length() > 0 && !category.isHidden()) {
				MOD_TABS[category.getIndex()] = AssetUtils.getResource(icon);
			}
		}
	}

	@Override
	public void onRender() {
		drawRectFalcun(menu.getX(), menu.getY() + 58, 225, menu.getHeight() - 58, MENU_SIDE_BG_COLOR);
		
		int y = menu.getY() + 59;
		int height = 32;
		
		drawRectFalcun(menu.getX(), menu.getY() + 58, 225, height + 1, ModCategoryButton.MAIN_COLOR);
		
		drawShadowDown(menu.getX(), y - 1, 225);
		drawShadowDown(menu.getX(), y + height, 225);
		
		Fonts.RobotoMiniHeader.drawString("MODS", menu.getX() + 225 / 2 - Fonts.RobotoMiniHeader.getStringWidth("MODS") / 2, y + height / 2 - Fonts.RobotoMiniHeader.getStringHeight("MODS") / 2, IngameMenu.MENU_HEADER_TEXT_COLOR);
		
		y += 50;
		
		for(ModuleCategory category : ModuleCategory.values()) {
			String icon = category.getIcon();
			
			if(icon.trim().length() > 0 && !category.isHidden()) {
				drawShadowUp(menu.getX(), y, 225);
				drawShadowDown(menu.getX(), y + height, 225);
				y += height + 2 + 10;
			}
		}
		
		y = menu.getY() + menu.getHeight() - height;
		drawShadowUp(menu.getX(), y - 2, 225);
		
		if(modCategory != null) {
			Fonts.RobotoTitle.drawString(activeModule != null ? "SETTINGS | " : modCategory.getText(), menu.getX() + 255, menu.getY() + 80, IngameMenu.MENU_HEADER_TEXT_COLOR);
			
			if(activeModule != null) {
				int offset = Fonts.RobotoTitle.getStringWidth("SETTINGS | ");
				String text = activeModule.getName().toUpperCase().trim();
			
				Fonts.RobotoTitle.drawString(text, menu.getX() + 255 + offset, menu.getY() + 80, MENU_HEADER_TEXT_COLOR_MOD);
				
				drawShadowUp(menu.getX() + 255, menu.getY() + 110 + 25 + 2, menu.getWidth() - 286);
				drawShadowLeft(menu.getX() + 255 + 2, menu.getY() + 110 + 25, menu.getHeight() - 110 - 50 - 5);
				drawShadowDown(menu.getX() + 255, menu.getY() + menu.getHeight() - 27 - 5, menu.getWidth() - 286);
				drawShadowRight(menu.getX() + menu.getWidth() - 33, menu.getY() + 110 + 25, menu.getHeight() - 110 - 50 - 5);
				
				drawRectFalcun(menu.getX() + 255, menu.getY() + 110 + 25, menu.getWidth() - 255 - 31, menu.getHeight() - 110 - 50 - 5, MENU_BG_COLOR_MOD_BORDER);
				drawRectFalcun(menu.getX() + 255 + 1, menu.getY() + 110 + 25 + 1, menu.getWidth() - 255 - 33, menu.getHeight() - 110 - 52 - 5, MENU_BG_COLOR_MOD);
				
				if(activeModule instanceof Crosshair) {
					Crosshair crosshair = (Crosshair) activeModule;
					int w = 86;
					int h = 76;
					
					crosshair.drawPicker(menu.getX() + 255 + 25, menu.getY() + 290, w, h, menu.getMouseX(), menu.getMouseY());
				}
			}
			
			drawHorizontalLine(menu.getX() + 255, menu.getY() + 110, menu.getWidth() - 255 - 31, 3, IngameMenu.MENU_LINE_COLOR);
		}
	}

	@Override
	public void onLoad() {
		int y = 59 + 50;
		int height = 32;
		
		for(ModuleCategory category : ModuleCategory.values()) {
			String icon = category.getIcon();
			
			if(icon.trim().length() > 0 && !category.isHidden()) {
				MenuButton comp = new ModCategoryButton(category, MOD_TABS[category.getIndex()], 0, y, 225, height) {
					@Override
					public void onAction() {
						for(MenuComponent component : menu.getComponents()) {
							if(component instanceof ModCategoryButton) {
								ModCategoryButton button = (ModCategoryButton) component;
								button.setActive(component == this);
							}
						}
						
						modCategory = category;
						activeModule = null;
						ModsPage.this.parent.initPage();
					}
				};
				
				if(category == modCategory) {
					comp.setActive(true);
				}
				
				menu.addComponent(comp);
				
				y += height + 2 + 10;
			}
		}
		
		if(activeModule == null) {
			MenuTextField searchbar = new SearchTextfield(TextPattern.NONE, menu.getWidth() - 31 - 250 - 5, 110 - 38, 250, 30) {
				@Override
				public void onAction() {
					search = getText();
					initModPage();
				}
				
				@Override
				public void onClick() {
					setText("");
					search = "";
					initModPage();
				}
			};
			
			searchbar.setText(search != null ? search : "");
			menu.addComponent(searchbar);
		} else {
			int w = 150;
			int h = 20;
			
			ModsButton enable = new ModsButton(activeModule.isEnabled() ? "DISABLE" : "ENABLE", 255, menu.getHeight() - h - 6) {
				@Override
				public void onAction() {
					activeModule.setEnabled(isActive());
					
					setText(activeModule.isEnabled() ? "DISABLE" : "ENABLE");
				}
			};
			
			enable.setActive(activeModule.isEnabled());
			
			menu.addComponent(enable);
			
			MenuModList list = new MenuModList(BindType.class, menu.getWidth() - 182 - 160, menu.getHeight() - h - 6, 20) {
				@Override
				public void onAction() {
					activeModule.setBindType(BindType.valueOf(getValue().toUpperCase()));
				}
			};
			
			list.setValue(activeModule.getBindType().toString());

			menu.addComponent(list);
			
			MenuModKeybind btn = new MenuModKeybind(menu.getWidth() - 182, menu.getHeight() - h - 6, w, h) {
				@Override
				public void onAction() {
					activeModule.setKeyBind(getBind());
					setX(menu.getWidth() - 182);
				}
			};
			
			btn.setBind(activeModule.getKeyBind());
			
			menu.addComponent(btn);
			
			menu.addComponent(new GoBackButton(menu.getWidth() - 154, 110 - 38) {
				@Override
				public void onAction() {
					activeModule = null;
					ModsPage.this.parent.initPage();
				}
			});
		}
		
		ModScrollPane pane = new ModScrollPane(255, 140, menu.getWidth() - 255 - 32, menu.getHeight() - 141, false);
		menu.addComponent(pane);
		
		menu.addComponent(new ModCategoryButton("EDIT HUD", 0, menu.getHeight() - height - 1, 225, height) {
			@Override
			public void onAction() {
				setActive(false);
				mc.displayGuiScreen(hudEditor);
			}
		});

		if(activeModule == null) {
			initModPage(pane);
		} else {
			pane.setX(255 + 1);
			pane.setY(110 + 25 + 1);
			pane.setWidth(menu.getWidth() - 255 - 33);
			pane.setHeight(menu.getHeight() - 110 - 52 - 5);

			pane.setFullHeightScroller(true);
			
			pane.getComponents().clear();
			
			List<MenuComponent> toAdd = new ArrayList<>();
			
			int xSpacing = 25;
			int ySpacing = 15;
			
			int sliderWidth = pane.getWidth() - xSpacing * 2;
			
			for(ConfigEntry configEntry : activeModule.getEntries()) {
				final FeatureText label;
				
				String key = configEntry.getKey().toUpperCase();
				
				if(configEntry.hasDescription()) {
					toAdd.add(label = new FeatureText(key, 0, 0));
				} else {
					toAdd.add(label = new FeatureText(key, configEntry.getDescription(), 0, 0));
				}
				
				if(configEntry instanceof BooleanEntry) {
					BooleanEntry entry = (BooleanEntry) configEntry;
					
					MenuModCheckbox checkbox = new MenuModCheckbox(0, 0, 15, 15) {
						@Override
						public void onAction() {
							entry.setValue(activeModule, isChecked());
						}
					};
					
					checkbox.setChecked((boolean) entry.getValue(activeModule));
					
					toAdd.add(checkbox);
				} else if(configEntry instanceof ColorEntry) {
					ColorEntry entry = (ColorEntry) configEntry;
					
					toAdd.add(new MenuModColorPicker(0, 0, 15, 15, ((Color) entry.getValue(activeModule)).getRGB()) {
						@Override
						public void onAction() {
							entry.setValue(activeModule, getColor());
						}
					});
				} else if(configEntry instanceof DoubleEntry) {
					DoubleEntry entry = (DoubleEntry) configEntry;
					
					FeatureValueText valueText = new FeatureValueText("", 0, 0);
					
					toAdd.add(valueText);
					
					MenuModSlider slider = new MenuModSlider((double)entry.getValue(activeModule), entry.getMin(), entry.getMax(), 2, 0, 0, sliderWidth, 15) {
						@Override
						public void onAction() {
							label.setText((entry.getKey() + " | ").toUpperCase());
							entry.setValue(activeModule, (double)getValue());
							
							valueText.setText(getValue() + "");
						}
					};
						
					slider.onAction();
					
					toAdd.add(slider);
				} else if(configEntry instanceof FloatEntry) {
					FloatEntry entry = (FloatEntry) configEntry;
					
					FeatureValueText valueText = new FeatureValueText("", 0, 0);
					
					toAdd.add(valueText);
					
					MenuModSlider slider = new MenuModSlider((float)entry.getValue(activeModule), entry.getMin(), entry.getMax(), 2, 0, 0, sliderWidth, 15) {
						@Override
						public void onAction() {
							label.setText((entry.getKey() + " | ").toUpperCase());
							entry.setValue(activeModule, (float)getValue());
							
							valueText.setText(getValue() + "");
						}
					};
					
					slider.onAction();
											
					toAdd.add(slider);
				} else if(configEntry instanceof IntEntry) {
					IntEntry entry = (IntEntry) configEntry;
					
					FeatureValueText valueText = new FeatureValueText("", 0, 0);
					
					toAdd.add(valueText);
					
					if(entry.isKeyBind()) {
						MenuModKeybind bind = new MenuModKeybind(0, 0, 175, 15) {
							@Override
							public void onAction() {
								entry.setValue(activeModule, (int) getBind());
							}
						};
						
						bind.setBind((int) entry.getValue(activeModule));
						
						toAdd.add(bind);
					} else {
						MenuModSlider slider = new MenuModSlider((int)entry.getValue(activeModule), entry.getMin(), entry.getMax(), 0, 0, sliderWidth, 15) {
							@Override
							public void onAction() {
								label.setText((entry.getKey() + " | ").toUpperCase());
								entry.setValue(activeModule, getIntValue());
								
								valueText.setText(getIntValue() + "");
							}
						};
						
						slider.onAction();
						
						toAdd.add(slider);
					}
				} else if(configEntry instanceof ListEntry) {
					ListEntry entry = (ListEntry) configEntry;
					
					MenuModList list = new MenuModList(entry.getValues(), 0, 0, 15) {
						@Override
						public void onAction() {
							entry.setValue(activeModule, getValue());
						}
					};
					
					list.setValue((String) configEntry.getValue(activeModule));
					
					toAdd.add(list);
				} else if(configEntry instanceof StringEntry) {
					StringEntry entry = (StringEntry) configEntry;
					
					ModTextbox box = new ModTextbox(TextPattern.NONE, 0, 0, 175, 15) {
						@Override
						public void onAction() {
							entry.setValue(activeModule, getText());
						}
					};
					
					box.setText((String) configEntry.getValue(activeModule));
					
					toAdd.add(box);
				}
			}
			
			int defaultX = 25;
			
			int xPos = defaultX;
			int yPos = 25;
			
			boolean isText = false;
			MenuComponent last = null;
			
			for(MenuComponent component : toAdd) {
				 if(component instanceof FeatureValueText) {
					if(last != null) {
						component.setX(xPos);
						component.setY(yPos);
					}
				} else if(component instanceof FeatureText) {
					component.setX(xPos);
					component.setY(yPos);
					
					xPos += component.getWidth();
					
					isText = true;
				} else {
					xPos = defaultX;
					
					if(isText) {
						if(component instanceof MenuModSlider) {
							yPos += ySpacing;
							
							component.setX(xPos);
							component.setY(yPos);
						} else {
							if(component instanceof MenuModList) {
								component.setX(pane.getWidth() - component.getWidth() - xSpacing * 3 + 12);
							} else {
								component.setX(pane.getWidth() - component.getWidth() - xSpacing);
							}
							
							component.setY(yPos);
						}
						
						isText = false;
					} else {
						component.setX(xPos);
						component.setY(yPos);
					}
					
					yPos += ySpacing + component.getHeight();
				}
				
				pane.addComponent(component);
				
				last = component;
			}
		}
	}

	@Override
	public void onUnload() {
		activeModule = null;
		ModsPage.this.parent.initPage();
	}
	
	@Override
	public void onOpen() {
		updateStates();
	}

	@Override
	public void onClose() {
		
	}
	
	private void updateStates() {
		for(MenuComponent component : menu.getComponents()) {
			if(component instanceof ModsButton) {
				ModsButton button = (ModsButton) component;
				
				button.setActive(activeModule.isEnabled());
				button.onAction();
				
				break;
			}
		}
	}
	
	private void initModPage() {
		for(MenuComponent component : menu.getComponents()) {
			if(component instanceof ModScrollPane) {
				initModPage((ModScrollPane) component);
				return;
			}
		}
	}
	
	private void initModPage(ModScrollPane pane) {
		pane.getComponents().clear();
		
		for(MenuComponent c : menu.getComponents()) {
			if(c instanceof ModsButton || c instanceof MenuModList) {
				menu.getComponents().remove(c);
			}
		}
		
		int x = 0;
		int y = 5;
		int width = 170;
		int height = 150;
		int spacing = 16;
		
		for(Module module : Falcun.getInstance().moduleManager.getModules().stream().filter(entry ->
			((search == null || search.isEmpty()) || entry.getName().toLowerCase().indexOf(search.toLowerCase()) >= 0) && 
			(entry.getCategory() == modCategory || modCategory == ModuleCategory.ALL_MODS) && !entry.getCategory().isHidden()).collect(Collectors.toList())) {
			
			if (module.getName().equals("Image1")) continue;
			if (module.getName().equals("Image2")) continue;
			if (module.getName().equals("Image3")) continue;
			if (module.getName().equals("Image4")) continue;
			if (module.getName().equals("Image5")) continue;
			if (module.getName().equals("Text1")) continue;
			if (module.getName().equals("Text2")) continue;
			if (module.getName().equals("Text3")) continue;
			if (module.getName().equals("Text4")) continue;
			if (module.getName().equals("Text5")) continue;
			
			if (module instanceof NonViewableModule) {
				continue;
			}

			
			pane.addComponent(new ModuleBox(module, x, y, width, height) {
				@Override
				public void onOpenSettings() {
					activeModule = module;
					ModsPage.this.parent.initPage();
				}
			});
			
			x += width + spacing;
			
			if(x + width >= pane.getWidth()) {
				x = 0;
				y += height + spacing;
			}
		}
	}
}
