package net.mattbenson.modules.types.mods;

import java.awt.Color;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;

public class Friends extends Module {
	@ConfigValue.Boolean(name = "Hide outgoing friend requests", description = "Hides outgoing friend requests from the friends list")
	private boolean HIDE_OUTGOING_FRIEND_REQUESTS = false;
	
	@ConfigValue.Boolean(name = "Hide incoming friend requests", description = "Hides incoming friend requests from the friends list.")
	private boolean HIDE_INCOMING_FRIEND_REQUESTS = false;
	
	@ConfigValue.Boolean(name = "Team mode", description = "Highlight friends")
	private boolean TEAM_MODE = false;
	
	@ConfigValue.Color(name = "Highlight color", description = "The color to highlight friends in.")
	private Color HIGHLIGHT_COLOR = Color.BLUE;
	
	@ConfigValue.Boolean(name = "Hide nametags", description = "Hide friend's nametags.")
	private boolean HIDE_NAMETAGS = false;
	
	@ConfigValue.Boolean(name = "Hide friends", description = "Hide friends completly.")
	private boolean HIDE_FRIENDS = false;
	
	@ConfigValue.Boolean(name = "Require shift", description = "Requires shift to press the friends key")
	private boolean REQUIRE_SHIFT = false;
	
	@ConfigValue.Boolean(name = "Show notifications", description = "Shows notifications when something new happens")
	public boolean SHOW_NOTIFICATIONS = true;
	
	public Friends() {
		super("Friends", ModuleCategory.MODS);
	}
}
