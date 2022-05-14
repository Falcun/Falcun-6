package mchorse.emoticons.commands;

import mchorse.emoticons.common.EmoteAPI;
import mchorse.emoticons.common.emotes.Emotes;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class CommandEmote extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "emote";
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "emoticons.commands.emote.help";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        String emote = args.length >= 1 ? args[0] : "";

        if (!Emotes.has(emote))
        {
            emote = "";
        }

        EmoteAPI.setEmoteClient(emote, Minecraft.getMinecraft().thePlayer);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, Emotes.EMOTES.keySet());
        }

        return super.addTabCompletionOptions(sender, args, pos);
    }
}