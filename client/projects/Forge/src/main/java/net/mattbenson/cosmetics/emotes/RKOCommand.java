package net.mattbenson.cosmetics.emotes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.mattbenson.Falcun;
import net.mattbenson.chat.ChatUtils;
import net.mattbenson.network.NetworkingClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class RKOCommand extends CommandBase {
	@Override
	public String getCommandName() {
		return "rko";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/rko <name>";
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if(args.length == 0) {
			ChatUtils.sendLocalMessage("/rko <name>", true);
			return;
		}
		
		boolean valid = true;

		for (char character : args[0].toCharArray()) {
			if (!Character.isAlphabetic(character) && !Character.isDigit(character)) {
				valid = false;
				break;
			}
		}

		if (valid) {
			NetworkingClient.sendLine("EmoticonStartData", "rko", args[0]);
		} else {
			ChatUtils.sendLocalMessage("You must enter a valid player name.", true);
		}
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] p_addTabCompletionOptions_2_, BlockPos p_addTabCompletionOptions_3_) {
		Collection<NetworkPlayerInfo> players = Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap();
		
		ArrayList<String> playerNames = new ArrayList<>();
		
		for (NetworkPlayerInfo player : players) {
			playerNames.add(player.getGameProfile().getName());
		}
		
		return getListOfStringsMatchingLastWord(p_addTabCompletionOptions_2_, playerNames.toArray(new String[playerNames.size()]));
	}

	@Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }
}