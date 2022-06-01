package net.mattbenson.gui.menu.pages;

import java.awt.Color;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONObject;

import com.github.lunatrius.schematica.proxy.ClientProxy;

import net.mattbenson.Falcun;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.Menu;
import net.mattbenson.gui.framework.MenuComponent;
import net.mattbenson.gui.framework.TextPattern;
import net.mattbenson.gui.menu.IngameMenu;
import net.mattbenson.gui.menu.Page;
import net.mattbenson.gui.menu.components.cosmetics.CosmeticGenericButton;
import net.mattbenson.gui.menu.components.groups.GroupBase;
import net.mattbenson.gui.menu.components.groups.GroupListEntry;
import net.mattbenson.gui.menu.components.groups.GroupListMiniEntry;
import net.mattbenson.gui.menu.components.groups.GroupTextButton;
import net.mattbenson.gui.menu.components.groups.GroupUserListHeader;
import net.mattbenson.gui.menu.components.groups.GroupUserListUser;
import net.mattbenson.gui.menu.components.groups.GroupValueList;
import net.mattbenson.gui.menu.components.macros.MacroButton;
import net.mattbenson.gui.menu.components.macros.MacroTextfield;
import net.mattbenson.gui.menu.components.macros.SimpleTextButton;
import net.mattbenson.gui.menu.components.mods.FeatureText;
import net.mattbenson.gui.menu.components.mods.MenuModCheckbox;
import net.mattbenson.gui.menu.components.mods.MenuModColorPicker;
import net.mattbenson.gui.menu.components.mods.ModCategoryButton;
import net.mattbenson.gui.menu.components.mods.ModScrollPane;
import net.mattbenson.gui.menu.components.profiles.ProfilesBase;
import net.mattbenson.gui.menu.components.profiles.ProfilesBlueButton;
import net.mattbenson.gui.menu.pages.groups.GroupSubTab;
import net.mattbenson.network.NetworkingClient;
import net.mattbenson.network.common.GroupData;
import net.mattbenson.network.common.GroupSetting;
import net.mattbenson.network.common.Request;
import net.mattbenson.network.common.SchemShareRequest;
import net.mattbenson.network.common.UserInfo;
import net.mattbenson.network.common.WaypointMarker;
import net.mattbenson.network.network.packets.groups.GroupList;
import net.mattbenson.network.network.packets.play.SchemShare;
import net.mattbenson.network.network.packets.schematica.SchematicaList;
import net.mattbenson.network.network.packets.waypoints.WaypointList;
import net.mattbenson.utils.AssetUtils;
import net.mattbenson.utils.NetworkUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GroupsPage extends Page {
	private final static int SUB_TAB_COLOR = new Color(18, 17, 22, 255).getRGB();
	
	private final static ResourceLocation USER_ICON = AssetUtils.getResource("gui", "user.png");
	
	private MacroTextfield nameNew;
	private MacroTextfield nameJoin;
	private MenuModColorPicker color;
	private MacroButton add;
	private MacroButton join;
	private ModScrollPane scrollPane;
	private ModScrollPane scrollPaneUsers;
	
	private GroupTextButton settings;
	private GroupTextButton schematics;
	private GroupTextButton waypoints;
	private GroupTextButton admin;
	
	private GroupData currentGroup;
	
	public GroupSubTab subTab;
	
	public GroupsPage(Minecraft mc, Menu menu, IngameMenu parent) {
		super(mc, menu, parent);
	}

	@Override
	public void onInit() {
		NetworkingClient.sendLine("RequestGroupList");
		NetworkingClient.sendLine("RequestWaypointList");
		int width = 300;
		int x = menu.getWidth() - width + 20;
		int y = 59;
		
		int compWidth = width - 6 - 20 * 2;

		nameNew = new MacroTextfield(TextPattern.TEXT_AND_NUMBERS, x, y + 85, compWidth, 22, "...") ;
		nameJoin = new MacroTextfield(TextPattern.TEXT_AND_NUMBERS, x, y + 300, compWidth, 22, "...");

		int acceptWidth = compWidth - 40;
		
		add = new MacroButton("CREATE", x - 21 + width / 2 - acceptWidth / 2, y + 180, acceptWidth, 22, true) {
			@Override
			public void onAction() {
				setActive(false);
				
				if(nameNew.getText().isEmpty()) {
					return;
				}
				
				NetworkingClient.sendLine("CreateGroup", nameNew.getText(), color.getColor().getRGB() + "");
					
				populateScrollPane();
			}
		};
		
		color = new MenuModColorPicker(x - 1, y + 145, compWidth + 6, 22, Color.WHITE.getRGB());
		
		join = new MacroButton("JOIN", x - 21 + width / 2 - acceptWidth / 2, y + 340, acceptWidth, 20, true) {
			@Override
			public void onAction() {
				setActive(false);
				
				if(nameJoin.getText().isEmpty()) {
					return;
				}
			
				NetworkingClient.sendLine("JoinGroup", nameJoin.getText());
	
				nameJoin.setText("");
				
				populateScrollPane();
			}
		};
		
		int xPos = 165;
		int spacing = 20;
		
		settings = new GroupTextButton(GroupSubTab.SETTINGS, xPos, y + 30 / 2 - getStringHeight(GroupSubTab.SETTINGS.getText()) / 2) {
			@Override
			public void onAction() {
				setActive(false);
				
				subTab = GroupSubTab.SETTINGS;
				setupGroupView();
			}
		};
		
		xPos += Fonts.Roboto.getStringWidth(GroupSubTab.SETTINGS.getText()) + spacing;
		
		schematics = new GroupTextButton(GroupSubTab.SCHEMATICS, xPos, y + 30 / 2 - getStringHeight(GroupSubTab.SCHEMATICS.getText()) / 2) {
			@Override
			public void onAction() {
				setActive(false);
				
				subTab = GroupSubTab.SCHEMATICS;
				setupGroupView();
			}
		};
		
		xPos += Fonts.Roboto.getStringWidth(GroupSubTab.SCHEMATICS.getText()) + spacing;
		
		waypoints = new GroupTextButton(GroupSubTab.WAYPOINTS, xPos, y + 30 / 2 - getStringHeight(GroupSubTab.WAYPOINTS.getText()) / 2) {
			@Override
			public void onAction() {
				setActive(false);
				
				subTab = GroupSubTab.WAYPOINTS;
				setupGroupView();
			}
		};
		
		xPos += Fonts.Roboto.getStringWidth(GroupSubTab.WAYPOINTS.getText()) + spacing;
		
		admin = new GroupTextButton(GroupSubTab.ADMIN, xPos, y + 30 / 2 - getStringHeight(GroupSubTab.ADMIN.getText()) / 2) {
			@Override
			public void onAction() {
				setActive(false);
				
				subTab = GroupSubTab.ADMIN;
				setupGroupView();
			}
		};
		
		scrollPane = new ModScrollPane(31, 150, menu.getWidth() - width - 31 * 2, menu.getHeight() - 151, false);
		scrollPaneUsers = new ModScrollPane(menu.getWidth() - width, 58 + 31, width - 1, menu.getHeight() - 58 - 31, false);
		
		populateScrollPane();
	}
	
	public void setupGroupView() {
		scrollPane.setY(150);
		scrollPane.setHeight(menu.getHeight() - 151);
		
		scrollPaneUsers.getComponents().clear();
		scrollPane.getComponents().clear();
		
		if(menu.getComponents().contains(nameNew)) {
			menu.getComponents().remove(nameNew);
			menu.getComponents().remove(color);
			menu.getComponents().remove(nameJoin);
			menu.getComponents().remove(add);
			menu.getComponents().remove(join);
		}
		
		if(!menu.getComponents().contains(settings)) {
			menu.addComponent(settings);
			menu.addComponent(schematics);
			menu.addComponent(waypoints);
			
			if(currentGroup != null && currentGroup.getOwner().equals(mc.thePlayer.getUniqueID())) {
				menu.addComponent(admin);
			}
		}
		
		for(MenuComponent component : menu.getComponents()) {
			if(component instanceof GroupTextButton) {
				GroupTextButton button = (GroupTextButton) component;
				button.setActive(button.getText().equalsIgnoreCase(subTab.getText()));
			}
		}
		
		int y = 5;		
		int online = 0;
		
		for(UserInfo user : currentGroup.getUsers()) {
			if(user.getUUID().equals(currentGroup.getOwner())) {
				online++;
			}
		}
		
		y += addHeader("ADMINS", online, y);
		
		for(UserInfo user : currentGroup.getUsers()) {
			if(user.getUUID().equals(currentGroup.getOwner())) {
				y += addUser(user.getName(), "OWNER", user.isOnline(), y);
			}
		}
		
		online = 0;
		
		for(UserInfo user : currentGroup.getUsers()) {
			if(user.getUUID().equals(currentGroup.getOwner())) {
				continue;
			}
			
			online++;
		}
		
		y += addHeader("MEMBERS", online, y);
		
		for(UserInfo user : currentGroup.getUsers()) {
			if(user.getUUID().equals(currentGroup.getOwner())) {
				continue;
			}
			
			y += addUser(user.getName(), "MEMBER", user.isOnline(), y);
		}
		
		switch(subTab) {
			case ADMIN:
				initAdminPage();
				break;
				
			case SCHEMATICS:
				initSchematicsPage();
				break;
				
			case SETTINGS:
				initSettingsPage();
				break;
				
			case WAYPOINTS:
				initWaypointsPage();
				break;
		}
	}
	
	private void initAdminPage() {
		int x = 5;
		int y = 5;
		int width = scrollPane.getWidth() - 10;
		int height = 25;
		int spacing = 10;
		
		scrollPane.addComponent(new CosmeticGenericButton("CLICK TO COPY INVITE CODE", x, y, width, height, true) {
			@Override
			public void onAction() {
				setActive(false);
				for(GroupData group : GroupList.getGroups()) {
					currentGroup = group;
					}
				IngameMenu.setClipboardString(currentGroup.getInviteCode());
			}
		});
		
		y += height + spacing;
		
		scrollPane.addComponent(new CosmeticGenericButton("REFRESH INVITE CODE", x, y, width, height, true) {
			@Override
			public void onAction() {
				setActive(false);
				for(GroupData group : GroupList.getGroups()) {
				currentGroup = group;
				}
				NetworkingClient.sendLine("RefreshInviteCode", currentGroup.getId() + "");
				onInit();
			}
		});
		
		y += height + spacing * 2;
		
		scrollPane.addComponent(new FeatureText("DISCORD BOT", x, y));
		
		y += spacing * 2;
		
		scrollPane.addComponent(new CosmeticGenericButton("CLICK HERE TO INVITE THE BOT", x, y, width, height, true) {
			@Override
			public void onAction() {
				setActive(false);
				
				if(Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().browse(new URI("https://discord.com/oauth2/authorize?client_id=824093412084940881&scope=bot&permissions=3072"));
					} catch (IOException e) {
						e.printStackTrace();
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		y += spacing + height;
		
		scrollPane.addComponent(new FeatureText("DISCORD CODE | CLICK BOX TO COPY", x, y));
		
		y += spacing * 2;
		
		scrollPane.addComponent(new CosmeticGenericButton(currentGroup.GetDiscordUnique(), x, y, width, height, true) {
			@Override
			public void onAction() {
				setActive(false);
				
				IngameMenu.setClipboardString(currentGroup.GetDiscordUnique());
			}
		});
		
		y += height + spacing * 2;
		
		scrollPane.addComponent(new FeatureText("WALL CHECKER", x, y));
		
		y += spacing * 2;
		
		GroupValueList checkTime = new GroupValueList("CHECK TIME", new String[] {currentGroup.checktime + "", "1", "0"}, x, y, height) {
			@Override
			public void onAction() {
				if(getValue().equalsIgnoreCase("1")) {
					currentGroup.checktime++;
					NetworkingClient.sendLine("CheckTime", currentGroup.getId() + "", "0");
				} else if(getValue().equalsIgnoreCase("0")) {
					currentGroup.checktime--;
					NetworkingClient.sendLine("CheckTime", currentGroup.getId() + "", "1");
				}
			}
		};
		
		scrollPane.addComponent(checkTime);
		
		GroupValueList checkDelay = new GroupValueList("CHECK DELAY", new String[] {currentGroup.checkdelay + "", "1", "0"}, x + 250, y, height) {
			@Override
			public void onAction() {
				if(getValue().equalsIgnoreCase("1")) {
					currentGroup.checkdelay++;
					NetworkingClient.sendLine("CheckDelay", currentGroup.getId() + "", "0");
				} else if(getValue().equalsIgnoreCase("0")) {
					currentGroup.checkdelay--;
					NetworkingClient.sendLine("CheckDelay", currentGroup.getId() + "", "1");
				}
			}
		};
		
		scrollPane.addComponent(checkDelay);
		
		scrollPane.addComponent(new CosmeticGenericButton("CHECK", width - 150, y, 150, height, true) {
			@Override
			public void onAction() {
				setActive(false);
				
				NetworkingClient.sendLine("WallChecker", currentGroup.getId() + "", currentGroup.wallChecker ? "0" : "1");
			}
		});
	}

	private void initSchematicsPage() {
		int x = 5;
		int y = 5;
		int width = scrollPane.getWidth() - 10;
		int height = 25;
		
		for(SchemShareRequest schem : SchematicaList.getSchematics()) {
			scrollPane.addComponent(new GroupListEntry(schem.getName(), x, y, width, height));
			scrollPane.addComponent(new ProfilesBlueButton("LOAD", x + width - 180, y + 5, 80, 15) {
				@Override
				public void onAction() {
					setActive(false);
					
					schem.accept();
				}
			});
			
			scrollPane.addComponent(new MacroButton("DELETE", x + width - 90, y + 5, 80, 15, false) {
				@Override
				public void onAction() {
					setActive(false);
					
					NetworkingClient.sendLine("RemoveSchematica", schem.getId() + "");
					populateScrollPane();
				}
			});
			
			y += height;
		}
	}
	
	private void initWaypointsPage() {
		int x = 5;
		int y = 5;
		int width = scrollPane.getWidth() - 10;
		int height = 35;
		
		for(WaypointMarker waypoint : WaypointList.getWaypoints()) {
			if(waypoint.getGroup() != currentGroup.getId()) {
				continue;
			}
			
			JSONObject obj = new JSONObject(waypoint.getMarker());
			String name = waypoint.getName();
			double xPos = obj.getDouble("x");
			double yPos = obj.getDouble("y");
			double zPos = obj.getDouble("z");
			
			scrollPane.addComponent(new GroupListEntry(waypoint.getName(), x, y, width, height));
			
			scrollPane.addComponent(new GroupListMiniEntry("X", (int)xPos + "", x + 150, y + 6, 90, 23));
			scrollPane.addComponent(new GroupListMiniEntry("Y", (int)yPos + "", x + 150 + 90 + 10, y + 6, 90, 23));
			scrollPane.addComponent(new GroupListMiniEntry("Z", (int)zPos + "", x + 150 + 90 * 2 + 10 * 2, y + 6, 90, 23));
			
			scrollPane.addComponent(new MacroButton("DELETE", x + width - 90, y + 5, 80, 24, false) {
				@Override
				public void onAction() {
					NetworkingClient.sendLine("RemoveWaypoint", waypoint.getId() + "");
				}
			});
			
			y += height;
		}
	}

	private void initSettingsPage() {
		GroupSetting settings = GroupList.getSettings(currentGroup.getName());
		
		int xText = 12;
		int xCheckbox = 350;
		int y = 5;
		int ySpacing = 20;
		
		//Ping
		MenuModCheckbox checkbox = new MenuModCheckbox(xCheckbox, y, 15, 15) {
			@Override
			public void onAction() {
				settings.setPing(isChecked());
			}
		};
		
		checkbox.setChecked(settings.isPing());
		scrollPane.addComponent(checkbox);
		scrollPane.addComponent(new FeatureText("PING", xText, y));
		
		y += ySpacing;
		
		//Highlight
		checkbox = new MenuModCheckbox(xCheckbox, y, 15, 15) {
			@Override
			public void onAction() {
				settings.setHighlight(isChecked());
			}
		};
		
		checkbox.setChecked(settings.isHighlight());
		scrollPane.addComponent(checkbox);
		scrollPane.addComponent(new FeatureText("HIGHLIGHT PLAYERS", xText, y));
		
		y += ySpacing;
		
		//Patchcrumbs
		checkbox = new MenuModCheckbox(xCheckbox, y, 15, 15) {
			@Override
			public void onAction() {
				settings.setPatchcrumbs(isChecked());
			}
		};
		
		checkbox.setChecked(settings.isPatchcrumbs());
		scrollPane.addComponent(checkbox);
		scrollPane.addComponent(new FeatureText("PATCHCRUMBS", xText, y));
		
		y += ySpacing;
	
		//Wall checker
		checkbox = new MenuModCheckbox(xCheckbox, y, 15, 15) {
			@Override
			public void onAction() {
				settings.setwallChecker(isChecked());
			}
		};
		
		checkbox.setChecked(settings.iswallChecker());
		scrollPane.addComponent(checkbox);
		scrollPane.addComponent(new FeatureText("WALL CHECKER", xText, y));
		
		scrollPane.addComponent(new FeatureText("CREATE WAYPOINT", xCheckbox + 15 + 30, 5));
		
		MacroTextfield field = new MacroTextfield(TextPattern.TEXT_AND_NUMBERS, xCheckbox + 15 + 30, 25, 260, 20, "ENTER WAYPOINT NAME");
		
		scrollPane.addComponent(field);
		
		scrollPane.addComponent(new CosmeticGenericButton("CREATE", (xCheckbox + 15 + 30) + 55, 55, 150, 20, true) {
			@Override
			public void onAction() {
				setActive(false);
				String text = field.getText();
				
				JSONObject markerJSON = new JSONObject();
				markerJSON.put("x", mc.thePlayer.posX);
				markerJSON.put("y", mc.thePlayer.posY);
				markerJSON.put("z", mc.thePlayer.posZ);
				markerJSON.put("dimension", mc.thePlayer.dimension);
				markerJSON.put("server", NetworkUtils.getServer());
				
				markerJSON.put("color", currentGroup.getColor());
				
				NetworkingClient.sendLine("NewWaypoint", text, markerJSON.toString(), currentGroup.getId() + "");
				
				populateScrollPane();
			}
		});
		
		scrollPane.addComponent(new FeatureText("SCHEMATICS", xCheckbox + 15 + 30, 100));
		
		scrollPane.addComponent(new MacroButton("SAVE CURRENT SCHEMATIC", xCheckbox + 15 + 30, 120, 260, 20, true) {
			@Override
			public void onAction() {
				setActive(false);
				
				SchemShare.shareSchem(currentGroup.getId());
				
				populateScrollPane();
			}
		});
		
		scrollPane.addComponent(new MacroButton("SHARE CURRENT SCHEMATIC", xCheckbox + 15 + 30, 120 + 20 + 10, 260, 20, true) {
			@Override
			public void onAction() {
				setActive(false);

				if(ClientProxy.schematic == null || ClientProxy.loadedFile == null) {
					Falcun.getInstance().notificationManager.showNotification("You do not have a schematic loaded.", Color.RED);
				} else {
					SchematicaList.shareSchem(currentGroup.getId());
				}
				
				populateScrollPane();
			}
		});
	}
	
	private int addUser(String user, String rank, boolean online, int y) {
		GroupUserListUser component = new GroupUserListUser(USER_ICON, user, rank, online, 5, y, scrollPaneUsers.getWidth() - 10, 20);
		scrollPaneUsers.addComponent(component);
		return component.getHeight();
	}
	
	private int addHeader(String header, int amount, int y) {
		GroupUserListHeader component = new GroupUserListHeader(header, amount, 0, y, scrollPaneUsers.getWidth(), 40);
		scrollPaneUsers.addComponent(component);
		return component.getHeight();
	}
	
	public void populateScrollPane() {
		if(scrollPane == null) {
			return;
		}
		
		scrollPaneUsers.getComponents().clear();
		scrollPane.getComponents().clear();
		
		scrollPane.setY(110);
		scrollPane.setHeight(menu.getHeight() - 111);
		
		if(!menu.getComponents().contains(nameNew)) {
			menu.addComponent(nameNew);
			menu.addComponent(color);
			menu.addComponent(nameJoin);
			menu.addComponent(add);
			menu.addComponent(join);
		}
		
		if(menu.getComponents().contains(settings)) {
			menu.getComponents().remove(settings);
			menu.getComponents().remove(schematics);
			menu.getComponents().remove(waypoints);
			
			if(menu.getComponents().contains(admin)) {
				menu.getComponents().remove(admin);
			}
		}
		
		if(currentGroup != null) {
			setupGroupView();
			return;
		}
		
		int spacing = 15;
		int height = 110;
		
		int defaultX = spacing;
		
		int y = spacing;
		int x = spacing;
		
		int width = 190;
		
		int maxWidth = scrollPane.getWidth() - spacing * 2;
		
		int innerSpacing = 5;
		int innerWidth = width - innerSpacing * 2;
		int buttonHeight = 20;
		
		int exitButtonSize = 18;
		
		for(GroupData group : GroupList.getGroups()) {
			String groupLeader = "";
			int membersOnline = 0;
			int membersTotal = 0;
			
			for(UserInfo user : group.getUsers()) {
				if(user.getUUID().equals(group.getOwner())) {
					groupLeader = user.getName();
				}
				
				if(user.isOnline()) {
					membersOnline++;
				}
				
				membersTotal++;
			}
			
			scrollPane.addComponent(new GroupBase(group.getName(), groupLeader, membersOnline, membersTotal, x, y, width, height));
			
			scrollPane.addComponent(new SimpleTextButton("X", x + innerWidth - exitButtonSize + innerSpacing, y + innerSpacing, exitButtonSize, exitButtonSize, false) {
				@Override
				public void onAction() {
					setActive(false);
					
					if(group.getOwner().equals(mc.thePlayer.getUniqueID())) {
						NetworkingClient.sendLine("DeleteGroup", group.getId() + "");
					} else {
						NetworkingClient.sendLine("GroupLeave", group.getId() + "");
					}
					
					populateScrollPane();
				}
			});
			
			scrollPane.addComponent(new MacroButton("OPEN PANEL", x + innerSpacing, y + height - innerSpacing * 2 - spacing, innerWidth, buttonHeight, true) {
				@Override
				public void onAction() {
					setActive(false);
					currentGroup = group;
					subTab = GroupSubTab.SETTINGS;
					
					setupGroupView();
				}
			});
			
			x += spacing + width;
			
			if(x + spacing + width > maxWidth) {
				x = defaultX;
				y += height + spacing;
			}
		}
		
		for(Request group : GroupList.getIngoingRequests()) {
			scrollPane.addComponent(new ProfilesBase(group.getLabel() + " - Invite", x, y, width, height));
			
			scrollPane.addComponent(new MacroButton("ACCEPT", x + innerSpacing, y + height - buttonHeight - innerSpacing * 3 - spacing, innerWidth, buttonHeight, true) {
				@Override
				public void onAction() {
					setActive(false);
					
					NetworkingClient.sendLine("AcceptGroupRequest", group.getId() + "");
				}
			});
			
			scrollPane.addComponent(new MacroButton("DECLINE", x + innerSpacing, y + height - innerSpacing * 2 - spacing, innerWidth, buttonHeight, false) {
				@Override
				public void onAction() {
					setActive(false);
					
					NetworkingClient.sendLine("DeclineGroupRequest", group.getId() + "");
				}
			});
			
			x += spacing + width;
			
			if(x + spacing + width > maxWidth) {
				x = defaultX;
				y += height + spacing;
			}
		}
	}

	
	@Override
	public void onRender() {
		int width = 300;
		int x = menu.getX() + menu.getWidth() - width + 20;
		int y = menu.getY() + 59;
		int height = 32;
		
		drawRectFalcun(menu.getX(), menu.getY() + 58, menu.getWidth() - width, height + 1, SUB_TAB_COLOR);
		
		drawRectFalcun(menu.getX() + menu.getWidth() - width, menu.getY() + 58, width, menu.getHeight() - 58, MacrosPage.MENU_SIDE_BG_COLOR);
		
		drawRectFalcun(menu.getX() + menu.getWidth() - width, menu.getY() + 58, width, height + 1, ModCategoryButton.MAIN_COLOR);
		drawShadowDown(menu.getX(), y - 1, menu.getWidth());
		
		if(currentGroup == null) {
			Fonts.Roboto.drawString("YOUR GROUPS", menu.getX() + (menu.getWidth() - width) / 2 - Fonts.Roboto.getStringWidth("YOUR GROUPS") / 2, y + height / 2 - Fonts.Roboto.getStringHeight("YOUR GROUPS") / 2, IngameMenu.MENU_HEADER_TEXT_COLOR);

			drawShadowDown(menu.getX() + menu.getWidth() - width, y + height, width);
			
			Fonts.RobotoMiniHeader.drawString("CREATE A GROUP", menu.getX() + menu.getWidth() - width / 2 - Fonts.RobotoMiniHeader.getStringWidth("CREATE A GROUP") / 2, y + height / 2 - Fonts.RobotoMiniHeader.getStringHeight("CREATE A GROUP") / 2, IngameMenu.MENU_HEADER_TEXT_COLOR);
			
			y += 60;
			
			Fonts.Roboto.drawString("GROUP NAME", x, y, IngameMenu.MENU_HEADER_TEXT_COLOR);
			
			y += 65;
			
			Fonts.Roboto.drawString("COLOR", x, y, IngameMenu.MENU_HEADER_TEXT_COLOR);
			
			y += 100;
			
			drawRectFalcun(menu.getX() + menu.getWidth() - width, y, width, height + 1, ModCategoryButton.MAIN_COLOR);
			drawShadowDown(menu.getX() + menu.getWidth() - width, y + height, width);
			drawShadowUp(menu.getX() + menu.getWidth() - width, y, width);
			Fonts.RobotoMiniHeader.drawString("JOIN A GROUP", menu.getX() + menu.getWidth() - width / 2 - Fonts.RobotoMiniHeader.getStringWidth("JOIN A GROUP") / 2, y + height / 2 - Fonts.RobotoMiniHeader.getStringHeight("JOIN A GROUP") / 2, IngameMenu.MENU_HEADER_TEXT_COLOR);
			
			y += 50;
			
			Fonts.Roboto.drawString("GROUP JOIN ID", x, y, IngameMenu.MENU_HEADER_TEXT_COLOR);
		} else {
			Fonts.RobotoTitle.drawString(subTab.getText(), menu.getX() + 31, menu.getY() + 110, IngameMenu.MENU_HEADER_TEXT_COLOR);
			drawHorizontalLine(menu.getX() + 31, menu.getY() + 140, menu.getWidth() - width - 31 * 2, 3, IngameMenu.MENU_LINE_COLOR);
			
			Fonts.RobotoMiniHeader.drawString("USERS", menu.getX() + menu.getWidth() - width + (width / 2) - Fonts.RobotoMiniHeader.getStringWidth("USERS") / 2, y + height / 2 - Fonts.RobotoMiniHeader.getStringHeight("USERS") / 2, IngameMenu.MENU_HEADER_TEXT_COLOR);
		}
	}
	
	@Override
	public void onLoad() {
		currentGroup = null;
		
		menu.addComponent(nameNew);
		menu.addComponent(color);
		menu.addComponent(nameJoin);
		menu.addComponent(add);
		menu.addComponent(join);
		menu.addComponent(scrollPane);
		menu.addComponent(scrollPaneUsers);
		
		menu.addComponent(settings);
		menu.addComponent(schematics);
		menu.addComponent(waypoints);
		
		if(currentGroup != null && currentGroup.getOwner().equals(mc.thePlayer.getUniqueID())) {
			menu.addComponent(admin);
		}

		populateScrollPane();
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
