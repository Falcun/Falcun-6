package net.mattbenson.gui.override.resourcepacks;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class GuiResourcePackSelectedCustom extends GuiResourcePackListCustom
{
    public GuiResourcePackSelectedCustom(Minecraft mcIn, int p_i45056_2_, int p_i45056_3_, List<ResourcePackListEntryCustom> p_i45056_4_)
    {
        super(mcIn, p_i45056_2_, p_i45056_3_, p_i45056_4_);
    }

    protected String getListHeader()
    {
        return I18n.format("resourcePack.selected.title", new Object[0]);
    }
}
