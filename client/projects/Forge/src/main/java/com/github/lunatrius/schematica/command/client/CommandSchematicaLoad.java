package com.github.lunatrius.schematica.command.client;

import java.util.List;

import com.github.lunatrius.schematica.client.world.SchematicWorld;
import com.github.lunatrius.schematica.command.CommandSchematicaBase;
import com.github.lunatrius.schematica.proxy.ClientProxy;
import com.github.lunatrius.schematica.reference.Names;
import com.github.lunatrius.schematica.reference.Reference;
import com.ibm.icu.text.DecimalFormat;

import net.mattbenson.Falcun;
import net.mattbenson.Wrapper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class CommandSchematicaLoad extends CommandSchematicaBase {
	

    @Override
    public String getCommandName() {
        return "convert";
    }

    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "/convert <hours>";
    }

    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return null;
    }

    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
    	 if (args.length != 1) {
             throw new CommandException("Incorrect Usuage! /convert <hours>");
         }
    	 try {
    		 double hours = Double.parseDouble(args[0]);
    		 double minutes = hours * 60;
    		 double seconds = minutes * 60;
    		 Wrapper.getInstance().addChat(hours + " hours is " + (Math.round(minutes * 100.0) / 100.0) +" minutes!");
    		 Wrapper.getInstance().addChat(hours + " hours is " + (Math.round(seconds * 100.0) / 100.0) +" seconds!");
    	 }
    	 catch(Exception ex) {;
             throw new CommandException(ex.getMessage());
    	 }
    }

}
