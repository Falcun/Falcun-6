package net.mattbenson.events.types.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.mattbenson.events.Event;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;

//Hooked @ net.minecraft.command.ServerCommandManager.java
public class CommandRegisterEvent extends Event {
	private List<CommandBase> commands;
	
	public CommandRegisterEvent() {
		commands = new ArrayList<>();
	}
	
	public List<CommandBase> getCommands() {
		return commands;
	}
}
