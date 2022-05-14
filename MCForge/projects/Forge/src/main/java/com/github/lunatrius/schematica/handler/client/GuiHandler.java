package com.github.lunatrius.schematica.handler.client;

import com.github.lunatrius.schematica.client.printer.SchematicPrinter;

import net.mattbenson.Falcun;
import net.mattbenson.Wrapper;
import net.mattbenson.modules.types.mods.Schematica;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiHandler {
    public static final GuiHandler INSTANCE = new GuiHandler();

    @SubscribeEvent
    public void onGuiOpen(final GuiOpenEvent event) {
    	if(!Wrapper.getInstance().getModuleManager().getModule(Schematica.class).enabled) return;
        if (SchematicPrinter.INSTANCE.isPrinting()) {
            if (event.gui instanceof GuiEditSign) {
                event.gui = null;
            }
        }
    }
}
