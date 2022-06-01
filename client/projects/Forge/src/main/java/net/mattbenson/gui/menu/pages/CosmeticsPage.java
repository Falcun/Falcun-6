package net.mattbenson.gui.menu.pages;

import java.awt.Color;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.NoSuchElementException;

import org.json.JSONException;
import org.lwjgl.input.Keyboard;

import mchorse.emoticons.common.emotes.Emote;
import net.mattbenson.Falcun;
import net.mattbenson.Wrapper;
import net.mattbenson.cosmetics.bandana.Bandana;
import net.mattbenson.cosmetics.bandana.BandanaPlayerHandler;
import net.mattbenson.cosmetics.cape.Cape;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.Menu;
import net.mattbenson.gui.framework.MenuComponent;
import net.mattbenson.gui.menu.IngameMenu;
import net.mattbenson.gui.menu.Page;
import net.mattbenson.gui.menu.components.cosmetics.CosmeticActionButton;
import net.mattbenson.gui.menu.components.cosmetics.CosmeticBindButton;
import net.mattbenson.gui.menu.components.cosmetics.CosmeticCapeView;
import net.mattbenson.gui.menu.components.cosmetics.CosmeticGenericButton;
import net.mattbenson.gui.menu.components.cosmetics.CosmeticList;
import net.mattbenson.gui.menu.components.cosmetics.CosmeticRainbowButton;
import net.mattbenson.gui.menu.components.cosmetics.CosmeticToggleButton;
import net.mattbenson.gui.menu.components.cosmetics.CosmeticUserPreview;
import net.mattbenson.gui.menu.components.macros.MacroButton;
import net.mattbenson.gui.menu.components.mods.MenuModKeybind;
import net.mattbenson.gui.menu.components.mods.ModCategoryButton;
import net.mattbenson.gui.menu.components.mods.ModScrollPane;
import net.mattbenson.gui.menu.pages.cosmetics.BandanaSize;
import net.mattbenson.gui.menu.pages.cosmetics.CapeType;
import net.mattbenson.gui.menu.pages.cosmetics.CosmeticType;
import net.mattbenson.network.NetworkingClient;
import net.mattbenson.requests.ContentType;
import net.mattbenson.requests.WebRequest;
import net.mattbenson.requests.WebRequestResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class CosmeticsPage extends Page {
	private CosmeticType cosmeticType = CosmeticType.CAPES;
	
	private CosmeticGenericButton capes;
	private CosmeticGenericButton bandanas;
	private CosmeticGenericButton emotes;
	private CosmeticGenericButton flags;
	
	private MenuModKeybind emoteWheelBind;
	
	private CosmeticBindButton bindButton1;
	private CosmeticBindButton bindButton2;
	private CosmeticBindButton bindButton3;
	private CosmeticBindButton bindButton4;
	
	private CosmeticBindButton lastButton;
	
	private CosmeticList capeLocation;
	private CosmeticList bandanaLocation;
	
	private MacroButton resetButton;
	
	private CosmeticRainbowButton openCrateButton;
	private CosmeticRainbowButton openChristmasButton;
	
	private CosmeticGenericButton resetCape;
	private CosmeticToggleButton capeEnabled;
	
	private CosmeticUserPreview userPreview;
	
	private ModScrollPane scrollpane;
	
	public CosmeticsPage(Minecraft mc, Menu menu, IngameMenu parent) {
		super(mc, menu, parent);
	}

	@Override
	public void onInit() {
		int x = 15;
		int y = 59 + 50;
		int typeWidth = 125;
		int typeHeight = 20;
		
		int width = 300;
		int compWidth = width - 6 - 20 * 2;
		
		capes = new CosmeticGenericButton(CosmeticType.CAPES.toString(), x, y, typeWidth, typeHeight, false) {
			@Override
			public void onAction() {
				setActive(false);
				cosmeticType = CosmeticType.CAPES;
				populateScrollPane();
			}
		};
		
		bandanas = new CosmeticGenericButton(CosmeticType.BANDANAS.toString(), x + typeWidth + 20, y, typeWidth, typeHeight, false) {
			@Override
			public void onAction() {
				setActive(false);
				cosmeticType = CosmeticType.BANDANAS;
				populateScrollPane();
			}
		};
		
		emotes = new CosmeticGenericButton(CosmeticType.EMOTES.toString(), x, y + typeHeight + 10, typeWidth, typeHeight, false) {
			@Override
			public void onAction() {
				setActive(false);
				cosmeticType = CosmeticType.EMOTES;
				populateScrollPane();
			}
		};
		
		flags = new CosmeticGenericButton(CosmeticType.FLAGS.toString(), x + typeWidth + 20, y + typeHeight + 10, typeWidth, typeHeight, false) {
			@Override
			public void onAction() {
				setActive(false);
				cosmeticType = CosmeticType.FLAGS;
				populateScrollPane();
			}
		};
		
		
		scrollpane = new ModScrollPane(x, y + typeHeight * 2 + 20, width - x - 2, menu.getHeight() - y - typeHeight * 2 - 20 - 1, true);
		
		x = menu.getWidth() - width + 20;
		y -= 30;
		
		emoteWheelBind = new MenuModKeybind(x - 21 + width / 2 - compWidth / 2, y + 50, compWidth, 22) {
			@Override
			public void onAction() {
				Falcun.getInstance().emoteSettings.setKeyBind(getBind());
				Falcun.getInstance().emoteSettings.saveEmoteSettings();
			}
		};
		
		bindButton1 = new CosmeticBindButton(x - 21 + width / 2 - compWidth / 2, y + 110, compWidth, 22) {
			@Override
			public void onAction() {
				lastButton = this;
				unpressAll(lastButton);
			}
		};
		
		bindButton2 = new CosmeticBindButton(x - 21 + width / 2 - compWidth / 2, y + 140, compWidth, 22) {
			@Override
			public void onAction() {
				lastButton = this;
				unpressAll(lastButton);
			}
		};
		
		bindButton3 = new CosmeticBindButton(x - 21 + width / 2 - compWidth / 2, y + 170, compWidth, 22) {
			@Override
			public void onAction() {
				lastButton = this;
				unpressAll(lastButton);
			}
		};
		
		bindButton4 = new CosmeticBindButton(x - 21 + width / 2 - compWidth / 2, y + 200, compWidth, 22) {
			@Override
			public void onAction() {
				lastButton = this;
				unpressAll(lastButton);
			}
		};
		
		capeLocation = new CosmeticList(CapeType.class, x - 21 + width / 2 - compWidth / 2, y + 260, compWidth, 22) {
			@Override
			public void onAction() {
				if(Falcun.getInstance().capeManager != null) {
					Falcun.getInstance().capeManager.getSettings().setType(CapeType.valueOf(getValue().replace(' ', '_').toUpperCase()));
					Falcun.getInstance().capeManager.saveSettings();
				}
			}
		};
		
		bandanaLocation = new CosmeticList(BandanaSize.class, x - 21 + width / 2 - compWidth / 2, y + 320, compWidth, 22) {
			@Override
			public void onAction() {
				BandanaPlayerHandler handler = Falcun.getInstance().bandanaManager.getFromUUID(mc.thePlayer.getUniqueID().toString());
				
				int bandanaHeightOffset = 0;
				BandanaSize val = BandanaSize.valueOf(getValue().toUpperCase());
				
				switch(val) {
					case LARGE:
						bandanaHeightOffset = 8;
						break;
						
					case MEDIUM:
						bandanaHeightOffset = 7;
						break;
						
					case SMALL:
						bandanaHeightOffset = 5;
						break;
						
					default:
						break;
				}
				
				if(handler != null) {
					handler.updateBandanaHeight(bandanaHeightOffset);
				}
			}
		};
		
		resetButton = new MacroButton("RESET TO DEFAULT", x - 21 + width / 2 - compWidth / 2, menu.getHeight() - 22 - 20, compWidth, 22, false) {
			@Override
			public void onAction() {
				setActive(false);
				
				emoteWheelBind.setBind(Keyboard.CHAR_NONE);
				
				bindButton1.setBind("");
				bindButton2.setBind("");
				bindButton3.setBind("");
				bindButton4.setBind("");
				
				Falcun.getInstance().emoteSettings.setKeyBind(Keyboard.CHAR_NONE);
				
				Falcun.getInstance().emoteSettings.setEmote1("");
				Falcun.getInstance().emoteSettings.setEmote2("");
				Falcun.getInstance().emoteSettings.setEmote3("");
				Falcun.getInstance().emoteSettings.setEmote4("");
				
				Falcun.getInstance().emoteSettings.saveEmoteSettings();

				capeLocation.setValue(CapeType.NORMAL.toString());
				bandanaLocation.setValue(BandanaSize.MEDIUM.toString());
			}
		};
		
		x = menu.getWidth() / 2;
		
		width = 350;
		
		openCrateButton = new CosmeticRainbowButton("OPEN A CRATE", x - width / 2, y, width - 175, 40) {
			@Override
			public void onAction() {
				setActive(false);
				
				Minecraft.getMinecraft().displayGuiScreen(null);
				
				if(Falcun.getInstance().crateManager.hasCrate(mc.thePlayer)) {
					Falcun.getInstance().notificationManager.showNotification("Please wait for your last crate to finish first.", Color.RED);
					return;
				}
				
				NetworkingClient.sendLine("Opencrate", "emote");
			}
		};
		
		openChristmasButton = new CosmeticRainbowButton("CHRISTMAS GIFT", x - width / 2 + 175, y, width - 175, 40) {
			@Override
			public void onAction() {
				setActive(false);
				
				Minecraft.getMinecraft().displayGuiScreen(null);
				
				try {
				 WebRequest request = new WebRequest("https://falcun.xyz/christmas.php?uuid=" + Minecraft.getMinecraft().thePlayer.getGameProfile().getId(), "GET", ContentType.FORM, false);
					WebRequestResult result = request.connect();
//					String result;
//					try {
//						String url = "https://falcun.xyz/christmas.php?uuid=" + Minecraft.getMinecraft().thePlayer.getGameProfile().getId();
//						HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
//						conn.setRequestMethod("GET");
//						conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36");
//						conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//						int resp = conn.getResponseCode();
//						System.out.println(resp);
//						BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//						String strCurrentLine;
//						while ((strCurrentLine = br.readLine()) != null) {
//							System.out.println(strCurrentLine);
//						}
//						result = strCurrentLine;
//					} catch (Throwable err) {
//						err.printStackTrace();
//						return;
//					}
					if (result.equals("0")) {
						Wrapper.getInstance().addChat("You have already redeemed your christmas gift.");
					} else if (result.getData().equals("1") | result.getData().equals("2") | result.getData().equals("3") | result.getData().equals("4") | result.getData().equals("5")) {
						Wrapper.getInstance().addChat("Congratulations you got " + result.getData() + " emote crates as your christmas gift.");
					}
					
				} catch (JSONException | NoSuchElementException | IOException e) {
					Falcun.getInstance().log.error("Failed to get christmas gift.", e);
				}
			}
		};
		
		resetCape = new CosmeticGenericButton("SET CAPE TO NONE", x - width / 2, menu.getHeight() - 40 - 40, width, 20, true) {
			@Override
			public void onAction() {
				setActive(false);
				
				if(Falcun.getInstance().capeManager != null) {
					new Thread(() -> {
						Falcun.getInstance().capeManager.getCape("none").updateSelectedCape();
						Falcun.getInstance().capeManager.capeLookupThread.performUpdate();
					}).start();
				}
			}
		};
		
		capeEnabled = new CosmeticToggleButton(Falcun.getInstance().capeManager != null && Falcun.getInstance().capeManager.getSettings().getEnabled() ? "ENABLED" : "DISABLED", x - width / 2, menu.getHeight() - 40 - 15, width, 20, Falcun.getInstance().capeManager != null && Falcun.getInstance().capeManager.getSettings().getEnabled()) {
			@Override
			public void onAction() {
				setActive(false);
				
				if(Falcun.getInstance().capeManager != null) {
					Falcun.getInstance().capeManager.getSettings().setEnabled(!Falcun.getInstance().capeManager.getSettings().getEnabled());
					Falcun.getInstance().capeManager.saveSettings();
					
					capeEnabled.setText(Falcun.getInstance().capeManager.getSettings().getEnabled() ? "ENABLED" : "DISABLED");
					capeEnabled.setApprove(Falcun.getInstance().capeManager.getSettings().getEnabled());
				}
			}
		};
		
		userPreview = new CosmeticUserPreview(x - width / 2, y + 45, width, 275);
		
		if(Falcun.getInstance().capeManager != null) {
			capeLocation.setValue(Falcun.getInstance().capeManager.getSettings().getType().toString());
		}
		
		bandanaLocation.setValue(BandanaSize.MEDIUM.toString());
		
		populateScrollPane();
		
		Falcun.getInstance().emoteSettings.loadEmoteSettings();
	}
	
	private void updateMenuStates(boolean load) {
		for(MenuComponent comp : menu.getComponents()) {
			if(comp instanceof CosmeticGenericButton) {
				CosmeticGenericButton button = (CosmeticGenericButton) comp;
				
				if(!button.getText().equalsIgnoreCase(cosmeticType.toString())) {
					button.setActive(false);
				} else {
					button.setActive(true);
					
					if(load) {
						button.onAction();
					}
				}
			}
		}
	}
	
	private void populateScrollPane() {
		scrollpane.getComponents().clear();
		
		updateMenuStates(false);
		
		if(cosmeticType == CosmeticType.CAPES) {
			int spacing = 10;
			int originalX = 10;
			int x = originalX;
			int y = 10;
			int width = 120;
			int height = 170;
			
			if(Falcun.getInstance().capeManager != null) {
				for(Cape cape : Falcun.getInstance().capeManager.ownedCapes) {
					CosmeticCapeView view = new CosmeticCapeView(cape, x, y, width, height);
					scrollpane.addComponent(view);
					
					scrollpane.addComponent(new CosmeticGenericButton("ENABLE", x + 5, y + view.getHeight() - 25, width - 10, 20, true) {
						@Override
						public void onAction() {
							setActive(false);
							
							mc.thePlayer.capeOverride = cape.getPreview();
							
							new Thread(() -> {
								cape.updateSelectedCape();
							}).start();
						}
					});
					
					x += view.getWidth() + spacing;
					
					if(x + view.getWidth() + spacing > scrollpane.getWidth()) {
						x = originalX;
						y += view.getHeight() + spacing;
					}
				}
			}
		} else if(cosmeticType == CosmeticType.EMOTES) {
			int spacing = 10;
			int originalX = 0;
			int x = originalX;
			int y = 10;
			int width = scrollpane.getWidth() - 15;
			int height = 30;
			
			if(Falcun.emoticons != null) {
				for(Emote emote : Falcun.ownedEmotes) {
					String emotename = emote.getTitle();
					emotename = emotename.replace("emoticons.emotes.", "");
					emotename = emotename.replace(".title", "");
					emotename = emotename.toUpperCase();

					scrollpane.addComponent(new CosmeticActionButton(I18n.format(emotename), emote.getKey(), x, y + height - 25, width - 10, height, true) {
						@Override
						public void onAction() {
							setActive(false);
							pressEmote(I18n.format(emote.getTitle()));
						}
					});
					
					y += height + spacing;
				}
			}
		} else if(cosmeticType == CosmeticType.BANDANAS) {
			int spacing = 10;
			int originalX = 0;
			int x = originalX;
			int y = 10;
			int width = scrollpane.getWidth() - 15;
			int height = 30;
			
			if(Falcun.getInstance().bandanaManager != null) {
				for(Bandana bandana : Falcun.getInstance().bandanaManager.ownedBandanas) {
					scrollpane.addComponent(new CosmeticActionButton(bandana.getName().toUpperCase(), bandana.getName(), x, y + height - 25, width - 10, height, true) {
						@Override
						public void onAction() {
							setActive(false);
							
							new Thread(() -> {
								bandana.updateSelectedBandana(bandanaLocation.getValue());
							}).start();
						}
					});
					
					y += height + spacing;
				}
			}
		} else if(cosmeticType == CosmeticType.FLAGS) {
			
		}
	}

	@Override
	public void onRender() {
		int width = 300;
		int x = menu.getX() + menu.getWidth() - width + 20;
		int y = menu.getY() + 59;
		int height = 32;
		
		drawRectFalcun(menu.getX() + menu.getWidth() - width, menu.getY() + 58, width, menu.getHeight() - 58, MacrosPage.MENU_SIDE_BG_COLOR);
		
		drawRectFalcun(menu.getX() + menu.getWidth() - width, menu.getY() + 58, width, height + 1, ModCategoryButton.MAIN_COLOR);
		drawShadowDown(menu.getX() + menu.getWidth() - width, y + height, width);
		Fonts.RobotoMiniHeader.drawString("COSMETIC SETTINGS", menu.getX() + menu.getWidth() - width / 2 - Fonts.RobotoMiniHeader.getStringWidth("COSMETIC SETTINGS") / 2, y + height / 2 - Fonts.RobotoMiniHeader.getStringHeight("COSMETIC SETTINGS") / 2, IngameMenu.MENU_HEADER_TEXT_COLOR);

		drawRectFalcun(menu.getX(), menu.getY() + 58, width, menu.getHeight() - 58, MacrosPage.MENU_SIDE_BG_COLOR);
				
		drawRectFalcun(menu.getX(), menu.getY() + 58, width, height + 1, ModCategoryButton.MAIN_COLOR);
		drawShadowDown(menu.getX(), y + height, width);
		Fonts.RobotoMiniHeader.drawString("COSMETICS", menu.getX() + width / 2 - Fonts.RobotoMiniHeader.getStringWidth("COSMETICS") / 2, y + height / 2 - Fonts.RobotoMiniHeader.getStringHeight("COSMETICS") / 2, IngameMenu.MENU_HEADER_TEXT_COLOR);
		
		drawShadowDown(menu.getX(), y - 1, width);
		drawShadowDown(menu.getX() + menu.getWidth() - width, y - 1, width);
		
		x += 3;
		y += 50;
		
		Fonts.Roboto.drawString("EMOTE WHEEL KEYBIND", x, y, IngameMenu.MENU_HEADER_TEXT_COLOR);
		
		y += 60;
		
		Fonts.Roboto.drawString("ENABLED EMOTES", x, y, IngameMenu.MENU_HEADER_TEXT_COLOR);
		
		y += 150;
		
		Fonts.Roboto.drawString("CAPE LOCATION", x, y, IngameMenu.MENU_HEADER_TEXT_COLOR);
		
		y += 60;
		
		Fonts.Roboto.drawString("BANDANA LOCATION", x, y, IngameMenu.MENU_HEADER_TEXT_COLOR);
	}
	
	@Override
	public void onLoad() {
		if(Falcun.getInstance().capeManager != null) {
			Falcun.getInstance().capeManager.updateOwnedCapes();
		}
		
		if(Falcun.getInstance().bandanaManager != null) {
			Falcun.getInstance().bandanaManager.updateOwnedBandanas();
		}
		
		if(Falcun.getInstance().emoticons != null) {
			Falcun.updateOwnedEmotes();
		}
		
		menu.addComponent(capes);
		menu.addComponent(bandanas);
		menu.addComponent(emotes);
		menu.addComponent(flags);
		
		emoteWheelBind.setBind(Falcun.getInstance().emoteSettings.getKeyBind());
		
		menu.addComponent(emoteWheelBind);
		
		bindButton1.setBind(Falcun.getInstance().emoteSettings.getEmote1());
		bindButton2.setBind(Falcun.getInstance().emoteSettings.getEmote2());
		bindButton3.setBind(Falcun.getInstance().emoteSettings.getEmote3());
		bindButton4.setBind(Falcun.getInstance().emoteSettings.getEmote4());
		
		menu.addComponent(bindButton1);
		menu.addComponent(bindButton2);
		menu.addComponent(bindButton3);
		menu.addComponent(bindButton4);
		
		menu.addComponent(capeLocation);
		menu.addComponent(bandanaLocation);
		
		menu.addComponent(resetButton);
		
		menu.addComponent(openCrateButton);
		menu.addComponent(openChristmasButton);
		
		menu.addComponent(resetCape);
		menu.addComponent(capeEnabled);
		
		menu.addComponent(userPreview);
		
		menu.addComponent(scrollpane);
		
		updateMenuStates(false);
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
	
	private void pressEmote(String emote) {
		if(lastButton != null) {
			lastButton.setBind(emote);
			
			if(lastButton == bindButton1) {
				Falcun.getInstance().emoteSettings.setEmote1(emote);
			} else if(lastButton == bindButton2) {
				Falcun.getInstance().emoteSettings.setEmote2(emote);
			} else if(lastButton == bindButton3) {
				Falcun.getInstance().emoteSettings.setEmote3(emote);
			} else if(lastButton == bindButton4) {
				Falcun.getInstance().emoteSettings.setEmote4(emote);
			}
			
			Falcun.getInstance().emoteSettings.saveEmoteSettings();
			
			lastButton = null;
			unpressAll(lastButton);
		}
	}
	
	private void unpressAll(CosmeticBindButton b) {
		for(MenuComponent comp : menu.getComponents()) {
			if(comp instanceof CosmeticBindButton) {
				CosmeticBindButton button = (CosmeticBindButton) comp;
				
				if(button != b) {
					button.setActive(false);
				}
			}
		}
	}
}
