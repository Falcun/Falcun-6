package com.github.lunatrius.schematica.client.gui.load;

import com.github.lunatrius.core.client.gui.GuiScreenBase;
import com.github.lunatrius.schematica.Schematica;
import com.github.lunatrius.schematica.client.renderer.RenderSchematic;
import com.github.lunatrius.schematica.client.world.SchematicWorld;
import com.github.lunatrius.schematica.handler.ConfigurationHandler;
import com.github.lunatrius.schematica.proxy.ClientProxy;
import com.github.lunatrius.schematica.reference.Names;
import com.github.lunatrius.schematica.reference.Reference;
import com.github.lunatrius.schematica.util.FileFilterSchematic;
import com.github.lunatrius.schematica.world.schematic.SchematicUtil;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import net.mattbenson.Falcun;
import net.mattbenson.Wrapper;
import net.mattbenson.fonts.Fonts;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ServerListEntryLanScan;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.io.FileUtils;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GuiSchematicLoad extends GuiScreenBase {
    private static final FileFilterSchematic FILE_FILTER_FOLDER = new FileFilterSchematic(true);
    private static final FileFilterSchematic FILE_FILTER_SCHEMATIC = new FileFilterSchematic(false);

    private GuiSchematicLoadSlot guiSchematicLoadSlot;

    private GuiButton btnOpenDir = null;
    private GuiButton btnDone = null;
    
	private boolean isSearching = false;
	private String search = "Search";

    private final String strTitle = I18n.format(Names.Gui.Load.TITLE);
    private final String strFolderInfo = I18n.format(Names.Gui.Load.FOLDER_INFO);
    private String strNoSchematic = I18n.format(Names.Gui.Load.NO_SCHEMATIC);

    protected File currentDirectory = ConfigurationHandler.schematicDirectory;
    protected final List<GuiSchematicEntry> schematicFiles = new ArrayList<GuiSchematicEntry>();

    public GuiSchematicLoad(final GuiScreen guiScreen) {
        super(guiScreen);
    }

    @Override
    public void initGui() {
        int id = 0;

        this.btnOpenDir = new GuiButton(id++, this.width / 2 - 154, this.height - 36, 150, 20, I18n.format(Names.Gui.Load.OPEN_FOLDER));
        this.buttonList.add(this.btnOpenDir);

        this.btnDone = new GuiButton(id++, this.width / 2 + 4, this.height - 36, 150, 20, I18n.format(Names.Gui.DONE));
        this.buttonList.add(this.btnDone);

        this.guiSchematicLoadSlot = new GuiSchematicLoadSlot(this);

        reloadSchematics();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.guiSchematicLoadSlot.handleMouseInput();
    }

    @Override
    protected void actionPerformed(final GuiButton guiButton) {
    	
        if (guiButton.enabled) {
            if (guiButton.id == this.btnOpenDir.id) {
                boolean retry = false;

                try {
                    final Class<?> c = Class.forName("java.awt.Desktop");
                    final Object m = c.getMethod("getDesktop").invoke(null);
                    c.getMethod("browse", URI.class).invoke(m, ConfigurationHandler.schematicDirectory.toURI());
                } catch (final Throwable e) {
                    retry = true;
                }

                if (retry) {
                    Reference.logger.info("Opening via Sys class!");
                    Sys.openURL("file://" + ConfigurationHandler.schematicDirectory.getAbsolutePath());
                }
            } else if (guiButton.id == this.btnDone.id) {
                if (Schematica.proxy.isLoadEnabled) {
                    loadSchematic();
                }
                this.mc.displayGuiScreen(this.parentScreen);
            } else {
                this.guiSchematicLoadSlot.actionPerformed(guiButton);
            }
        }
    }

    @Override
    public void drawScreen(final int x, final int y, final float partialTicks) {
        this.guiSchematicLoadSlot.drawScreen(x, y, partialTicks);
        
       

		boolean hovered = x >= this.width / 2 - 75 && x <= this.width / 2+75 && y >= 4 && y <= 18;
		
		Wrapper.getInstance().drawRoundedRect(this.width / 2 - 75, 4, this.width / 2+75, 18, 4, hovered ? new Color(200,200,200,100).getRGB() : new Color(100,100,100,100).getRGB());
 		Wrapper.getInstance().drawRoundedRect(this.width / 2 - 75 + 1, 4+ 1, (this.width / 2+75) - 1, (18) - 1, 3, new Color(22, 24, 27,100).getRGB());
 		
		if(!this.isSearching) {
			 Wrapper.getInstance().drawString("Search", this.width / 2 - Wrapper.getInstance().getStringWidth("Search") / 2, 7, Color.WHITE.getRGB()); 
		} else {
			 Wrapper.getInstance().drawString(search + "|", this.width / 2 - Wrapper.getInstance().getStringWidth(search + "|") / 2, 7, Color.WHITE.getRGB());
		}
		
        super.drawScreen(x, y, partialTicks);
    }

    @Override
    public void onGuiClosed() {
        // loadSchematic();
    }

    protected void changeDirectory(final String directory) {
        this.currentDirectory = new File(this.currentDirectory, directory);

        try {
            this.currentDirectory = this.currentDirectory.getCanonicalFile();
        } catch (final IOException ioe) {
            Reference.logger.error("Failed to canonize directory!", ioe);
        }

        reloadSchematics();
    }

    protected void reloadSchematics() {
    	if (this.search.equals("Search")) {
        String name = null;
        Item item = null;

        this.schematicFiles.clear();

        try {
            if (!this.currentDirectory.getCanonicalPath().equals(ConfigurationHandler.schematicDirectory.getCanonicalPath())) {
                this.schematicFiles.add(new GuiSchematicEntry("..", Items.lava_bucket, 0, true));
            }
        } catch (final IOException e) {
            Reference.logger.error("Failed to add GuiSchematicEntry!", e);
        }

        final File[] filesFolders = this.currentDirectory.listFiles(FILE_FILTER_FOLDER);
        if (filesFolders == null) {
            Reference.logger.error("listFiles returned null (directory: {})!", this.currentDirectory);
        } else {
            for (final File file : filesFolders) {
                if (file == null) {
                    continue;
                }

                name = file.getName();

                final File[] files = file.listFiles();
                item = (files == null || files.length == 0) ? Items.bucket : Items.water_bucket;

                this.schematicFiles.add(new GuiSchematicEntry(name, item, 0, file.isDirectory()));
            }
        }

        final File[] filesSchematics = this.currentDirectory.listFiles(FILE_FILTER_SCHEMATIC);
        if (filesSchematics == null || filesSchematics.length == 0) {
            this.schematicFiles.add(new GuiSchematicEntry(this.strNoSchematic, Blocks.dirt, 0, false));
        } else {
            for (final File file : filesSchematics) {
                name = file.getName();
                
                this.schematicFiles.add(new GuiSchematicEntry(name, SchematicUtil.getIconFromFile(file), file.isDirectory()));
            }
        }
    	} else {
    		

    		String name = null;
            Item item = null;

            this.schematicFiles.clear();

            try {
                if (!this.currentDirectory.getCanonicalPath().equals(ConfigurationHandler.schematicDirectory.getCanonicalPath())) {
                    //this.schematicFiles.add(new GuiSchematicEntry("..", Items.lava_bucket, 0, true));
                }
            } catch (final IOException e) {
                Reference.logger.error("Failed to add GuiSchematicEntry!", e);
            }

            final File[] filesFolders = this.currentDirectory.listFiles(FILE_FILTER_FOLDER);
            if (filesFolders == null) {
                Reference.logger.error("listFiles returned null (directory: {})!", this.currentDirectory);
            } else {
                for (final File file : filesFolders) {
                    if (file == null) {
                        continue;
                    }

                    name = file.getName();

                    final File[] files = file.listFiles();
                    item = (files == null || files.length == 0) ? Items.bucket : Items.water_bucket;
                 
                   
                }
            }

            String[] extenstions = {"schematic"};
            final Collection<File> filesSchematics = FileUtils.listFiles(this.currentDirectory, extenstions, true);
     
            
            if (filesSchematics == null || filesSchematics.size() == 0) {
            	
                
            } else {
                for (final File file : filesSchematics) {
                    name = file.getName();
                   if (name.toLowerCase().contains(search.toLowerCase()))
                    this.schematicFiles.add(new GuiSchematicEntry(name, SchematicUtil.getIconFromFile(file), file.isDirectory()));
                }
            }
    		
    		
    		
    		
    		
    		
    		
    		
    	}
    }

    private void loadSchematic() {
    	
        final int selectedIndex = this.guiSchematicLoadSlot.selectedIndex;

        try {
            if (selectedIndex >= 0 && selectedIndex < this.schematicFiles.size()) {
                final GuiSchematicEntry schematicEntry = this.schematicFiles.get(selectedIndex);
                if (Schematica.proxy.loadSchematic(null, this.currentDirectory, schematicEntry.getName())) {
                    final SchematicWorld schematic = ClientProxy.schematic;
                    if (schematic != null) {
                    	
                        if (net.mattbenson.modules.types.mods.Schematica.autoY1) {
                            ClientProxy.schematic.position.y = 1;
            				RenderSchematic.INSTANCE.refresh();
                        }
                        
                        if (net.mattbenson.modules.types.mods.Schematica.autoY1auto) {
                        	if (schematic.getHeight() >= 254) {
                            ClientProxy.schematic.position.y = 1;
            				RenderSchematic.INSTANCE.refresh();
                        	}
                        }
                    	
                    	net.mattbenson.modules.types.mods.Schematica.path = this.currentDirectory + "/" + schematicEntry.getName();
                    	
                        ClientProxy.moveSchematicToPlayer(schematic);
	                    net.mattbenson.modules.types.mods.Schematica.previousSchematic = schematicEntry.getName();
	                    net.mattbenson.modules.types.mods.Schematica.flipsAndRotations = "";
	                    net.mattbenson.modules.types.mods.Schematica.saveToFile();
                    }
                }
            }
        } catch (final Exception e) {
            Reference.logger.error("Failed to load schematic!", e);
        }
    }
    
    
    
    
    
    protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(this.isSearching) {
			switch (keyCode)
			{
			case Keyboard.KEY_ESCAPE:
				this.mc.displayGuiScreen(this.parentScreen);
				break;
			case Keyboard.KEY_BACK:
				if(this.search.length() > 0)
					this.search = this.search.substring(0, this.search.length() - 1);
				reloadSchematics();
				break;
			case Keyboard.KEY_NUMPADENTER:
				this.isSearching = false;
				break;
			case 28:
				this.isSearching = false;
				break;
			default:
				if (ChatAllowedCharacters.isAllowedCharacter(typedChar))
				{
					this.search += Character.toString(typedChar);
					reloadSchematics();
				}
			} 
			

		} else {
			switch (keyCode)
			{
			case Keyboard.KEY_ESCAPE:
				this.mc.displayGuiScreen(this.parentScreen);
				break;
			} 
		}
	}
			
			
				
		

			
	
	
    
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		boolean isOverSearch = mouseX >= this.width / 2 - 75 && mouseX <= this.width / 2+75 && mouseY >= 1 && mouseY <= 15;
		if(isOverSearch) {
			this.isSearching = true;
			this.search = "";
		} else {
			this.isSearching = false;
		}
	}
    
    
    
}
