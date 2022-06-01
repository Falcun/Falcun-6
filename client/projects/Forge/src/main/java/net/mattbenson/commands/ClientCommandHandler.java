package net.mattbenson.commands;

import java.util.List;

import net.mattbenson.Falcun;
import net.mattbenson.chat.ChatUtils;
import net.mattbenson.events.types.commands.CommandRegisterEvent;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandBlockData;
import net.minecraft.command.CommandClearInventory;
import net.minecraft.command.CommandClone;
import net.minecraft.command.CommandCompare;
import net.minecraft.command.CommandDebug;
import net.minecraft.command.CommandDefaultGameMode;
import net.minecraft.command.CommandDifficulty;
import net.minecraft.command.CommandEffect;
import net.minecraft.command.CommandEnchant;
import net.minecraft.command.CommandEntityData;
import net.minecraft.command.CommandExecuteAt;
import net.minecraft.command.CommandFill;
import net.minecraft.command.CommandGameMode;
import net.minecraft.command.CommandGameRule;
import net.minecraft.command.CommandGive;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.CommandHelp;
import net.minecraft.command.CommandKill;
import net.minecraft.command.CommandParticle;
import net.minecraft.command.CommandPlaySound;
import net.minecraft.command.CommandReplaceItem;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.CommandServerKick;
import net.minecraft.command.CommandSetPlayerTimeout;
import net.minecraft.command.CommandSetSpawnpoint;
import net.minecraft.command.CommandShowSeed;
import net.minecraft.command.CommandSpreadPlayers;
import net.minecraft.command.CommandStats;
import net.minecraft.command.CommandTime;
import net.minecraft.command.CommandTitle;
import net.minecraft.command.CommandToggleDownfall;
import net.minecraft.command.CommandTrigger;
import net.minecraft.command.CommandWeather;
import net.minecraft.command.CommandWorldBorder;
import net.minecraft.command.CommandXP;
import net.minecraft.command.IAdminCommand;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.command.server.CommandAchievement;
import net.minecraft.command.server.CommandBanIp;
import net.minecraft.command.server.CommandBanPlayer;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.command.server.CommandBroadcast;
import net.minecraft.command.server.CommandDeOp;
import net.minecraft.command.server.CommandEmote;
import net.minecraft.command.server.CommandListBans;
import net.minecraft.command.server.CommandListPlayers;
import net.minecraft.command.server.CommandMessage;
import net.minecraft.command.server.CommandMessageRaw;
import net.minecraft.command.server.CommandOp;
import net.minecraft.command.server.CommandPardonIp;
import net.minecraft.command.server.CommandPardonPlayer;
import net.minecraft.command.server.CommandPublishLocalServer;
import net.minecraft.command.server.CommandSaveAll;
import net.minecraft.command.server.CommandSaveOff;
import net.minecraft.command.server.CommandSaveOn;
import net.minecraft.command.server.CommandScoreboard;
import net.minecraft.command.server.CommandSetBlock;
import net.minecraft.command.server.CommandSetDefaultSpawnpoint;
import net.minecraft.command.server.CommandStop;
import net.minecraft.command.server.CommandSummon;
import net.minecraft.command.server.CommandTeleport;
import net.minecraft.command.server.CommandTestFor;
import net.minecraft.command.server.CommandTestForBlock;
import net.minecraft.command.server.CommandWhitelist;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ClientCommandHandler extends CommandHandler implements IAdminCommand {
	public ClientCommandHandler() {
		CommandRegisterEvent event = new CommandRegisterEvent();
		Falcun.getInstance().EVENT_BUS.post(event);

		for (CommandBase cmd : event.getCommands()) {
			registerCommand(cmd);
		}
		
		CommandBase.setAdminCommander(this);
	}
		
	/**
	 * Send an informative message to the server operators
	 * 
	 * @param sender    The command sender
	 * @param command   The command that was executed
	 * @param msgFormat The message, optionally with formatting wildcards
	 * @param msgParams The formatting arguments for the {@code msgFormat}
	 */
	public void notifyOperators(ICommandSender sender, ICommand command, int flags, String msgFormat,
			Object... msgParams) {
		boolean flag = true;
		MinecraftServer minecraftserver = MinecraftServer.getServer();

		if (!sender.sendCommandFeedback()) {
			flag = false;
		}

		IChatComponent ichatcomponent = new ChatComponentTranslation("chat.type.admin",
				new Object[] { sender.getName(), new ChatComponentTranslation(msgFormat, msgParams) });
		ichatcomponent.getChatStyle().setColor(EnumChatFormatting.GRAY);
		ichatcomponent.getChatStyle().setItalic(Boolean.valueOf(true));

		if (flag) {
			for (EntityPlayer entityplayer : minecraftserver.getConfigurationManager().getPlayerList()) {
				if (entityplayer != sender
						&& minecraftserver.getConfigurationManager().canSendCommands(entityplayer.getGameProfile())
						&& command.canCommandSenderUseCommand(sender)) {
					boolean flag1 = sender instanceof MinecraftServer && MinecraftServer.getServer().shouldBroadcastConsoleToOps();
					boolean flag2 = sender instanceof RConConsoleSource && MinecraftServer.getServer().shouldBroadcastRconToOps();

					if (flag1 || flag2
							|| !(sender instanceof RConConsoleSource) && !(sender instanceof MinecraftServer)) {
						entityplayer.addChatMessage(ichatcomponent);
					}
				}
			}
		}

		if (sender != minecraftserver
				&& minecraftserver.worldServers[0].getGameRules().getGameRuleBooleanValue("logAdminCommands")) {
			minecraftserver.addChatMessage(ichatcomponent);
		}

		boolean flag3 = minecraftserver.worldServers[0].getGameRules().getGameRuleBooleanValue("sendCommandFeedback");

		if (sender instanceof CommandBlockLogic) {
			flag3 = ((CommandBlockLogic) sender).shouldTrackOutput();
		}

		if ((flags & 1) != 1 && flag3 || sender instanceof MinecraftServer) {
			sender.addChatMessage(new ChatComponentTranslation(msgFormat, msgParams));
		}
	}
}