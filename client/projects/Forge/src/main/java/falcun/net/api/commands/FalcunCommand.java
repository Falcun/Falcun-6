//package falcun.net.api.commands;
//
//import net.minecraft.command.CommandBase;
//import net.minecraft.command.CommandException;
//import net.minecraft.command.ICommand;
//import net.minecraft.command.ICommandSender;
//import net.minecraft.util.BlockPos;
//
//import java.util.LinkedList;
//import java.util.List;
//
//public final class FalcunCommand implements ICommand {
//
//	public List<String> aliasList  = new LinkedList<>();
//
//	@Override
//	public String getCommandName() {
//		return null;
//	}
//
//	@Override
//	public String getCommandUsage(ICommandSender sender) {
//		return null;
//	}
//
//	@Override
//	public List<String> getCommandAliases() {
//		return this.aliasList;
//	}
//
//	@Override
//	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
//
//	}
//
//	@Override
//	public boolean canCommandSenderUseCommand(ICommandSender sender) {
//		return false;
//	}
//
//	@Override
//	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
//		return null;
//	}
//
//	@Override
//	public boolean isUsernameIndex(String[] args, int index) {
//		return false;
//	}
//
//	@Override
//	public int compareTo(ICommand o) {
//		return 0;
//	}
//}
