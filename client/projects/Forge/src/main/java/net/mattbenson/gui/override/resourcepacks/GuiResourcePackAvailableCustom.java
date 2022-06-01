package net.mattbenson.gui.override.resourcepacks;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiResourcePackList;
import net.minecraft.client.resources.I18n;

public class GuiResourcePackAvailableCustom extends GuiResourcePackListCustom
{
    public GuiResourcePackAvailableCustom(Minecraft mcIn, int p_i45054_2_, int p_i45054_3_, List<ResourcePackListEntryCustom> p_i45054_4_)
    {
        super(mcIn, p_i45054_2_, p_i45054_3_, p_i45054_4_);
    }

    protected String getListHeader()
    {
        return I18n.format("resourcePack.available.title", new Object[0]);
    }
}
