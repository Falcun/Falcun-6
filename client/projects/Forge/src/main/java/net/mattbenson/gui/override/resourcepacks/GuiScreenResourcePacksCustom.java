package net.mattbenson.gui.override.resourcepacks;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.Util;

public class GuiScreenResourcePacksCustom extends GuiScreen
{
    private static final Logger logger = LogManager.getLogger();
    private final GuiScreen parentScreen;
    private List<ResourcePackListEntryCustom> availableResourcePacks;
    private List<ResourcePackListEntryCustom> selectedResourcePacks;

    /** List component that contains the available resource packs */
    private GuiResourcePackAvailableCustom availableResourcePacksList;

    /** List component that contains the selected resource packs */
    private GuiResourcePackSelectedCustom selectedResourcePacksList;
    private boolean changed = false;

    public GuiScreenResourcePacksCustom(GuiScreen parentScreenIn)
    {
        this.parentScreen = parentScreenIn;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        this.buttonList.add(new GuiOptionButton(2, this.width / 2 - 154, this.height - 48, I18n.format("resourcePack.openFolder", new Object[0])));
        this.buttonList.add(new GuiOptionButton(1, this.width / 2 + 4, this.height - 48, I18n.format("gui.done", new Object[0])));

        if (!this.changed)
        {
            this.availableResourcePacks = Lists.<ResourcePackListEntryCustom>newArrayList();
            this.selectedResourcePacks = Lists.<ResourcePackListEntryCustom>newArrayList();
            ResourcePackRepository resourcepackrepository = this.mc.getResourcePackRepository();
            resourcepackrepository.updateRepositoryEntriesAll();
            List<ResourcePackRepository.Entry> list = Lists.newArrayList(resourcepackrepository.getRepositoryEntriesAll());
            list.removeAll(resourcepackrepository.getRepositoryEntries());

            for (ResourcePackRepository.Entry resourcepackrepository$entry : list)
            {
                this.availableResourcePacks.add(new ResourcePackListEntryFoundCustom(this, resourcepackrepository$entry));
            }

            for (ResourcePackRepository.Entry resourcepackrepository$entry1 : Lists.reverse(resourcepackrepository.getRepositoryEntries()))
            {
                this.selectedResourcePacks.add(new ResourcePackListEntryFoundCustom(this, resourcepackrepository$entry1));
            }

            this.selectedResourcePacks.add(new ResourcePackListEntryDefaultCustom(this));
        }

        this.availableResourcePacksList = new GuiResourcePackAvailableCustom(this.mc, 200, this.height, this.availableResourcePacks);
        this.availableResourcePacksList.setSlotXBoundsFromLeft(this.width / 2 - 4 - 200);
        this.availableResourcePacksList.registerScrollButtons(7, 8);
        this.selectedResourcePacksList = new GuiResourcePackSelectedCustom(this.mc, 200, this.height, this.selectedResourcePacks);
        this.selectedResourcePacksList.setSlotXBoundsFromLeft(this.width / 2 + 4);
        this.selectedResourcePacksList.registerScrollButtons(7, 8);
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        this.selectedResourcePacksList.handleMouseInput();
        this.availableResourcePacksList.handleMouseInput();
    }

    public boolean hasResourcePackEntry(ResourcePackListEntryCustom p_146961_1_)
    {
        return this.selectedResourcePacks.contains(p_146961_1_);
    }

    public List<ResourcePackListEntryCustom> getListContaining(ResourcePackListEntryCustom p_146962_1_)
    {
        return this.hasResourcePackEntry(p_146962_1_) ? this.selectedResourcePacks : this.availableResourcePacks;
    }

    public List<ResourcePackListEntryCustom> getAvailableResourcePacks()
    {
        return this.availableResourcePacks;
    }

    public List<ResourcePackListEntryCustom> getSelectedResourcePacks()
    {
        return this.selectedResourcePacks;
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id == 2)
            {
                File file1 = this.mc.getResourcePackRepository().getDirResourcepacks();
                String s = file1.getAbsolutePath();

                if (Util.getOSType() == Util.EnumOS.OSX)
                {
                    try
                    {
                        logger.info(s);
                        Runtime.getRuntime().exec(new String[] {"/usr/bin/open", s});
                        return;
                    }
                    catch (IOException ioexception1)
                    {
                        logger.error((String)"Couldn\'t open file", (Throwable)ioexception1);
                    }
                }
                else if (Util.getOSType() == Util.EnumOS.WINDOWS)
                {
                    String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[] {s});

                    try
                    {
                        Runtime.getRuntime().exec(s1);
                        return;
                    }
                    catch (IOException ioexception)
                    {
                        logger.error((String)"Couldn\'t open file", (Throwable)ioexception);
                    }
                }

                boolean flag = false;

                try
                {
                    Class<?> oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
                    oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {file1.toURI()});
                }
                catch (Throwable throwable)
                {
                    logger.error("Couldn\'t open link", throwable);
                    flag = true;
                }

                if (flag)
                {
                    logger.info("Opening via system class!");
                    Sys.openURL("file://" + s);
                }
            }
            else if (button.id == 1)
            {
                if (this.changed)
                {
                    List<ResourcePackRepository.Entry> list = Lists.<ResourcePackRepository.Entry>newArrayList();

                    for (ResourcePackListEntryCustom resourcepacklistentry : this.selectedResourcePacks)
                    {
                        if (resourcepacklistentry instanceof ResourcePackListEntryFoundCustom)
                        {
                            list.add(((ResourcePackListEntryFoundCustom)resourcepacklistentry).func_148318_i());
                        }
                    }

                    Collections.reverse(list);
                    this.mc.getResourcePackRepository().setRepositories(list);
                    this.mc.gameSettings.resourcePacks.clear();
                    this.mc.gameSettings.incompatibleResourcePacks.clear();

                    for (ResourcePackRepository.Entry resourcepackrepository$entry : list)
                    {
                        this.mc.gameSettings.resourcePacks.add(resourcepackrepository$entry.getResourcePackName());

                        if (resourcepackrepository$entry.func_183027_f() != 1)
                        {
                            this.mc.gameSettings.incompatibleResourcePacks.add(resourcepackrepository$entry.getResourcePackName());
                        }
                    }

                    this.mc.gameSettings.saveOptions();
                    this.mc.refreshResources();
                }

                this.mc.displayGuiScreen(this.parentScreen);
            }
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.availableResourcePacksList.mouseClicked(mouseX, mouseY, mouseButton);
        this.selectedResourcePacksList.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Called when a mouse button is released.  Args : mouseX, mouseY, releaseButton
     */
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawBackground(0);
        this.availableResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        this.selectedResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, I18n.format("resourcePack.title", new Object[0]), this.width / 2, 16, 16777215);
        this.drawCenteredString(this.fontRendererObj, I18n.format("resourcePack.folderInfo", new Object[0]), this.width / 2 - 77, this.height - 26, 8421504);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Marks the selected resource packs list as changed to trigger a resource reload when the screen is closed
     */
    public void markChanged()
    {
        this.changed = true;
    }
}
