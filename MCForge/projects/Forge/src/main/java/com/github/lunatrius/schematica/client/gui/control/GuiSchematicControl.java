package com.github.lunatrius.schematica.client.gui.control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import com.github.lunatrius.core.client.gui.GuiNumericField;
import com.github.lunatrius.core.client.gui.GuiScreenBase;
import com.github.lunatrius.schematica.Schematica;
import com.github.lunatrius.schematica.client.printer.SchematicPrinter;
import com.github.lunatrius.schematica.client.renderer.RenderSchematic;
import com.github.lunatrius.schematica.client.util.FlipHelper;
import com.github.lunatrius.schematica.client.util.RotationHelper;
import com.github.lunatrius.schematica.client.world.SchematicWorld;
import com.github.lunatrius.schematica.client.world.SchematicWorld.LayerMode;
import com.github.lunatrius.schematica.handler.ConfigurationHandler;
import com.github.lunatrius.schematica.proxy.ClientProxy;
import com.github.lunatrius.schematica.reference.Constants;
import com.github.lunatrius.schematica.reference.Names;

import net.mattbenson.Falcun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.config.GuiUnicodeGlyphButton;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiSchematicControl extends GuiScreenBase {
	private SchematicWorld schematic;
	private SchematicPrinter printer;

	private int centerX = 0;
	private int centerY = 0;

	private GuiNumericField numericX = null;
	private GuiNumericField numericY = null;
	private GuiNumericField numericZ = null;

	private GuiButton btnUnload = null;
	private GuiButton btnLayerMode = null;
	private GuiNumericField nfLayer = null;

	private GuiButton btnHide = null;
	private GuiButton btnMove = null;
	private GuiButton btnShare = null;
	private GuiButton btnFlipDirection = null;
	private GuiButton btnFlip = null;
	private GuiButton btnRotateDirection = null;
	private GuiButton btnRotate = null;
    private GuiButton bntReload;
    private GuiButton bntMoveX;
    private GuiButton bntMoveZ;

	//Matt
	private GuiButton btnRecent1 = null;
	private GuiButton btnRecent2 = null;
	private GuiButton btnRecent3 = null;
	private GuiButton btnRecent4 = null;
	private GuiButton btnRecent5 = null;
	private ArrayList recentList = new ArrayList<>();
	private File[] files;
	
	private GuiButton btnMaterials = null;
	private GuiButton btnPrint = null;

	private final String strMoveSchematic = I18n.format(Names.Gui.Control.MOVE_SCHEMATIC);
	
	private final String strOperations = I18n.format(Names.Gui.Control.OPERATIONS);
	private final String strUnload = I18n.format(Names.Gui.Control.UNLOAD);

	private final String strMaterials = I18n.format(Names.Gui.Control.MATERIALS);
	private final String strPrinter = I18n.format(Names.Gui.Control.PRINTER);
	private final String strHide = I18n.format(Names.Gui.Control.HIDE);
	private final String strShow = I18n.format(Names.Gui.Control.SHOW);
	private final String strX = I18n.format(Names.Gui.X);
	private final String strY = I18n.format(Names.Gui.Y);
	private final String strZ = I18n.format(Names.Gui.Z);
	private final String strOn = I18n.format(Names.Gui.ON);
	private final String strOff = I18n.format(Names.Gui.OFF);

	public GuiSchematicControl(final GuiScreen guiScreen) {
		super(guiScreen);
		this.schematic = ClientProxy.schematic;
		this.printer = SchematicPrinter.INSTANCE;
	}

	@Override
	public void initGui() {
		this.centerX = this.width / 2;
		this.centerY = this.height / 2;

		this.buttonList.clear();

		int id = 0;
		
		File directory = new File(Falcun.MAIN_DIR + File.separator + "schematics");
		if(!directory.exists()) {
			directory.mkdir();
		}
		
		files = directory.listFiles();
		Arrays.sort(files, Comparator.comparingLong(File::lastModified));
		
		this.numericX = new GuiNumericField(this.fontRendererObj, id++, this.centerX - 50, this.centerY - 30, 100, 20);
		this.buttonList.add(this.numericX);

		this.numericY = new GuiNumericField(this.fontRendererObj, id++, this.centerX - 50, this.centerY - 5, 100, 20);
		this.buttonList.add(this.numericY);

		this.numericZ = new GuiNumericField(this.fontRendererObj, id++, this.centerX - 50, this.centerY + 20, 100, 20);
		this.buttonList.add(this.numericZ);

		this.btnUnload = new GuiButton(id++, this.width - 90, this.height - 200, 80, 20, this.strUnload);
		this.buttonList.add(this.btnUnload);

		this.btnLayerMode = new GuiButton(id++, this.width - 90, this.height - 150 - 25, 80, 20, I18n.format((this.schematic != null ? this.schematic.layerMode : LayerMode.ALL).name));
		this.buttonList.add(this.btnLayerMode);

		this.nfLayer = new GuiNumericField(this.fontRendererObj, id++, this.width - 90, this.height - 150, 80, 20);
		this.buttonList.add(this.nfLayer);

		this.btnHide = new GuiButton(id++, this.width - 90, this.height - 105, 80, 20, this.schematic != null && this.schematic.isRendering ? this.strHide : this.strShow);
		this.buttonList.add(this.btnHide);

		this.btnMove = new GuiButton(id++, this.width - 90, this.height - 80, 80, 20, I18n.format(Names.Gui.Control.MOVE_HERE));
		this.buttonList.add(this.btnMove);
		
        this.bntReload = new GuiButton(id++, this.width - 180, this.height - 105, 80, 20, I18n.format("Reload", new Object[0]));
        this.buttonList.add(this.bntReload);
        
        this.bntMoveX = new GuiButton(id++, this.width - 140, this.height - 130, 40, 20, I18n.format("Move X", new Object[0]));
        this.buttonList.add(this.bntMoveX);
        
        this.bntMoveZ = new GuiButton(id++, this.width - 180, this.height - 130, 40, 20, I18n.format("Move Y", new Object[0]));
        this.buttonList.add(this.bntMoveZ);
		
		//Penguin
		this.btnShare = new GuiButton(id++, this.width - 180, this.height - 80, 80, 20, "Share Schem");
		this.buttonList.add(this.btnShare);
		
		//Matt
		if (net.mattbenson.modules.types.mods.Schematica.autoSaveRecent) {
		if (files.length > 0) {
			this.btnRecent1 = new GuiButton(id++, this.centerX - 150, 20, 80, 20, "Load");
			this.buttonList.add(this.btnRecent1);
	    }
		
		if (files.length > 1) {
			this.btnRecent2 = new GuiButton(id++, this.centerX - 150, 45, 80, 20, "Load");
			this.buttonList.add(this.btnRecent2);
	    }
		
		if (files.length > 2) {
			this.btnRecent3 = new GuiButton(id++, this.centerX - 150, 70, 80, 20, "Load");
			this.buttonList.add(this.btnRecent3);
	    }
		
		if (files.length > 3) {
			this.btnRecent4 = new GuiButton(id++, this.centerX - 150, 95, 80, 20, "Load");
			this.buttonList.add(this.btnRecent4);
	    }
		
		if (files.length > 4) {
			this.btnRecent5 = new GuiButton(id++, this.centerX - 150, 120, 80, 20, "Load");
			this.buttonList.add(this.btnRecent5);
	    }
		}

		this.btnFlipDirection = new GuiButton(id++, this.width - 180, this.height - 55, 80, 20, I18n.format(Names.Gui.Control.TRANSFORM_PREFIX + ClientProxy.axisFlip.getName()));
		this.buttonList.add(this.btnFlipDirection);

		this.btnFlip = new GuiUnicodeGlyphButton(id++, this.width - 90, this.height - 55, 80, 20, " " + I18n.format(Names.Gui.Control.FLIP), "\u2194", 2.0f);
		this.buttonList.add(this.btnFlip);

		this.btnRotateDirection = new GuiButton(id++, this.width - 180, this.height - 30, 80, 20, I18n.format(Names.Gui.Control.TRANSFORM_PREFIX + ClientProxy.axisRotation.getName()));
		this.buttonList.add(this.btnRotateDirection);

		this.btnRotate = new GuiUnicodeGlyphButton(id++, this.width - 90, this.height - 30, 80, 20, " " + I18n.format(Names.Gui.Control.ROTATE), "\u21bb", 2.0f);
		this.buttonList.add(this.btnRotate);

		this.btnMaterials = new GuiButton(id++, 10, this.height - 70, 80, 20, this.strMaterials);
		this.buttonList.add(this.btnMaterials);

		this.btnPrint = new GuiButton(id++, 10, this.height - 30, 80, 20, this.printer.isPrinting() ? this.strOn : this.strOff);
		this.buttonList.add(this.btnPrint);

		this.numericX.setEnabled(this.schematic != null);
		this.numericY.setEnabled(this.schematic != null);
		this.numericZ.setEnabled(this.schematic != null);

		this.btnUnload.enabled = this.schematic != null;
		this.btnLayerMode.enabled = this.schematic != null;
		this.nfLayer.setEnabled(this.schematic != null && this.schematic.layerMode != LayerMode.ALL);

		this.btnHide.enabled = this.schematic != null;
		this.btnMove.enabled = this.schematic != null;
		this.btnFlipDirection.enabled = this.schematic != null;
		this.btnFlip.enabled = this.schematic != null;
		this.btnRotateDirection.enabled = this.schematic != null;
		this.btnRotate.enabled = this.schematic != null;
		this.btnMaterials.enabled = this.schematic != null;
		this.btnShare.enabled = this.schematic != null;
		this.btnPrint.enabled = this.schematic != null && this.printer.isEnabled();

		setMinMax(this.numericX);
		setMinMax(this.numericY);
		setMinMax(this.numericZ);

		if (this.schematic != null) {
			setPoint(this.numericX, this.numericY, this.numericZ, this.schematic.position);
		}

		this.nfLayer.setMinimum(0);
		this.nfLayer.setMaximum(this.schematic != null ? this.schematic.getHeight() - 1 : 0);
		if (this.schematic != null) {
			this.nfLayer.setValue(this.schematic.renderingLayer);
		}
	}

	private void setMinMax(final GuiNumericField numericField) {
		numericField.setMinimum(Constants.World.MINIMUM_COORD);
		numericField.setMaximum(Constants.World.MAXIMUM_COORD);
	}

	private void setPoint(final GuiNumericField numX, final GuiNumericField numY, final GuiNumericField numZ, final BlockPos point) {
		numX.setValue(point.getX());
		numY.setValue(point.getY());
		numZ.setValue(point.getZ());
	}

	@Override
	protected void actionPerformed(final GuiButton guiButton) {
		if (guiButton.enabled) {
			
			
			
			if (net.mattbenson.modules.types.mods.Schematica.autoSaveRecent) {
				try {
			if (guiButton.id == this.btnRecent1.id) {
				Schematica.proxy.unloadSchematic();
				loadSchematic(0);
                Minecraft.getMinecraft().displayGuiScreen(null);
			} else if (guiButton.id == this.btnRecent2.id) {
				Schematica.proxy.unloadSchematic();
				loadSchematic(1);
                Minecraft.getMinecraft().displayGuiScreen(null);
			} else if (guiButton.id == this.btnRecent3.id) {
				Schematica.proxy.unloadSchematic();
				loadSchematic(2);
                Minecraft.getMinecraft().displayGuiScreen(null);
			} else if (guiButton.id == this.btnRecent4.id) {
				Schematica.proxy.unloadSchematic();
				loadSchematic(3);
                Minecraft.getMinecraft().displayGuiScreen(null);
			} else if (guiButton.id == this.btnRecent5.id) {
				Schematica.proxy.unloadSchematic();
				loadSchematic(4);
                Minecraft.getMinecraft().displayGuiScreen(null);
			} 
			} catch (Exception e){ 
			}
			} 
		
			
			if (this.schematic == null) {
				return;
			}	
			
			if (guiButton.id == this.btnShare.id) {
				Minecraft.getMinecraft().displayGuiScreen(new GuiChat("{name:" + net.mattbenson.modules.types.mods.Schematica.previousSchematic.replace(".schematic", "") +", x:" +
			net.mattbenson.modules.types.mods.Schematica.x +"," + " y:" + net.mattbenson.modules.types.mods.Schematica.y + ", z:" + net.mattbenson.modules.types.mods.Schematica.z + ", fr:" 
			+ net.mattbenson.modules.types.mods.Schematica.flipsAndRotations.trim().replace("\n", ",").replace("ROTATION", "R")
			.replace("FLIP", "F")
			
			+ "}"));
			}
			

			
			if (guiButton.id == this.numericX.id) {
				this.schematic.position.x = this.numericX.getValue();
				net.mattbenson.modules.types.mods.Schematica.x = this.numericX.getValue();
				net.mattbenson.modules.types.mods.Schematica.repeaters.clear();
				net.mattbenson.modules.types.mods.Schematica.missinglocation.clear();
				net.mattbenson.modules.types.mods.Schematica.saveToFile();
				RenderSchematic.INSTANCE.refresh();
			} else if (guiButton.id == this.numericY.id) {
				this.schematic.position.y = this.numericY.getValue();
				net.mattbenson.modules.types.mods.Schematica.y = this.numericY.getValue();
				net.mattbenson.modules.types.mods.Schematica.repeaters.clear();
				net.mattbenson.modules.types.mods.Schematica.missinglocation.clear();
				net.mattbenson.modules.types.mods.Schematica.saveToFile();
				RenderSchematic.INSTANCE.refresh();
			} else if (guiButton.id == this.numericZ.id) {
				this.schematic.position.z = this.numericZ.getValue();
				net.mattbenson.modules.types.mods.Schematica.z = this.numericZ.getValue();
				net.mattbenson.modules.types.mods.Schematica.repeaters.clear();
				net.mattbenson.modules.types.mods.Schematica.missinglocation.clear();
				net.mattbenson.modules.types.mods.Schematica.saveToFile();
				RenderSchematic.INSTANCE.refresh();
			} else if (guiButton.id == this.btnUnload.id) {
				Schematica.proxy.unloadSchematic();
				net.mattbenson.modules.types.mods.Schematica.previousSchematic = "None";
				net.mattbenson.modules.types.mods.Schematica.x = 0;
				net.mattbenson.modules.types.mods.Schematica.y = 0;
				net.mattbenson.modules.types.mods.Schematica.z = 0;
				net.mattbenson.modules.types.mods.Schematica.repeaters.clear();
				net.mattbenson.modules.types.mods.Schematica.missinglocation.clear();
				net.mattbenson.modules.types.mods.Schematica.flipsAndRotations = "";
				net.mattbenson.modules.types.mods.Schematica.saveToFile();
				this.mc.displayGuiScreen(this.parentScreen);
			} else if (guiButton.id == this.btnLayerMode.id) {
				this.schematic.layerMode = LayerMode.next(this.schematic.layerMode);
				this.btnLayerMode.displayString = I18n.format(this.schematic.layerMode.name);
				this.nfLayer.setEnabled(this.schematic.layerMode != LayerMode.ALL);
				net.mattbenson.modules.types.mods.Schematica.repeaters.clear();
				net.mattbenson.modules.types.mods.Schematica.missinglocation.clear();
				RenderSchematic.INSTANCE.refresh();
			} else if (guiButton.id == this.nfLayer.id) {
				this.schematic.renderingLayer = this.nfLayer.getValue();
				net.mattbenson.modules.types.mods.Schematica.repeaters.clear();
				net.mattbenson.modules.types.mods.Schematica.missinglocation.clear();
				RenderSchematic.INSTANCE.refresh();
			} else if (guiButton.id == this.btnHide.id) {
				net.mattbenson.modules.types.mods.Schematica.repeaters.clear();
				net.mattbenson.modules.types.mods.Schematica.missinglocation.clear();
				this.btnHide.displayString = this.schematic.toggleRendering() ? this.strHide : this.strShow;
			} else if (guiButton.id == this.btnMove.id) {
				ClientProxy.moveSchematicToPlayer(this.schematic);
                if (net.mattbenson.modules.types.mods.Schematica.autoY1) {
                    ClientProxy.schematic.position.y = 1;
                }
                if (net.mattbenson.modules.types.mods.Schematica.autoY1auto) {
                	if (schematic.getHeight() >= 254) {
                    ClientProxy.schematic.position.y = 1;
                	}
                }
				net.mattbenson.modules.types.mods.Schematica.repeaters.clear();
				net.mattbenson.modules.types.mods.Schematica.missinglocation.clear();
				RenderSchematic.INSTANCE.refresh();
				setPoint(this.numericX, this.numericY, this.numericZ, this.schematic.position);
			} else if (guiButton.id == this.btnFlipDirection.id) {
				final EnumFacing[] values = EnumFacing.VALUES;
				ClientProxy.axisFlip = values[((ClientProxy.axisFlip.ordinal() + 2) % values.length)];
				guiButton.displayString = I18n.format(Names.Gui.Control.TRANSFORM_PREFIX + ClientProxy.axisFlip.getName());
			} else if (guiButton.id == this.btnFlip.id) {
				if (FlipHelper.INSTANCE.flip(this.schematic, ClientProxy.axisFlip, isShiftKeyDown())) {
					net.mattbenson.modules.types.mods.Schematica.flipsAndRotations += "FLIP:" + ClientProxy.axisFlip.getName() + "\n";
					net.mattbenson.modules.types.mods.Schematica.saveToFile();
					net.mattbenson.modules.types.mods.Schematica.repeaters.clear();
					net.mattbenson.modules.types.mods.Schematica.missinglocation.clear();
					RenderSchematic.INSTANCE.refresh();
					SchematicPrinter.INSTANCE.refresh();
				}
			} else if (guiButton.id == this.btnRotateDirection.id) {
				final EnumFacing[] values = EnumFacing.VALUES;
				ClientProxy.axisRotation = values[((ClientProxy.axisRotation.ordinal() + 1) % values.length)];
				guiButton.displayString = I18n.format(Names.Gui.Control.TRANSFORM_PREFIX + ClientProxy.axisRotation.getName());
				net.mattbenson.modules.types.mods.Schematica.saveToFile();
			} else if (guiButton.id == this.btnRotate.id) {
				if (RotationHelper.INSTANCE.rotate(this.schematic, ClientProxy.axisRotation, isShiftKeyDown())) {
					net.mattbenson.modules.types.mods.Schematica.flipsAndRotations += "ROTATION:" + ClientProxy.axisRotation.getName() + "\n";
					net.mattbenson.modules.types.mods.Schematica.saveToFile();
					setPoint(this.numericX, this.numericY, this.numericZ, this.schematic.position);
					net.mattbenson.modules.types.mods.Schematica.repeaters.clear();
					net.mattbenson.modules.types.mods.Schematica.missinglocation.clear();
					RenderSchematic.INSTANCE.refresh();
					SchematicPrinter.INSTANCE.refresh();
				}
			} else if (guiButton.id == this.btnMaterials.id) {
				this.mc.displayGuiScreen(new GuiSchematicMaterials(this));
			} else if (guiButton.id == this.btnPrint.id && this.printer.isEnabled()) {
				final boolean isPrinting = this.printer.togglePrinting();
				this.btnPrint.displayString = isPrinting ? this.strOn : this.strOff;
			}
			
            else if (guiButton.id == this.bntReload.id) {
                if (Minecraft.getMinecraft().thePlayer != null && !Minecraft.getMinecraft().isSingleplayer() && Minecraft.getMinecraft().getCurrentServerData().serverIP.indexOf("archon") == -1) {
                    Minecraft.getMinecraft().getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX + 1000.0, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ, false));
                }
                RenderSchematic.INSTANCE.refresh();
                Minecraft.getMinecraft().renderGlobal.loadRenderers();
            } else if (guiButton.id == this.bntMoveX.id) {
				this.schematic.position.x = (int) Minecraft.getMinecraft().thePlayer.posX;
				net.mattbenson.modules.types.mods.Schematica.y = this.numericY.getValue();
				net.mattbenson.modules.types.mods.Schematica.repeaters.clear();
				net.mattbenson.modules.types.mods.Schematica.missinglocation.clear();
				net.mattbenson.modules.types.mods.Schematica.saveToFile();
				RenderSchematic.INSTANCE.refresh();
			} else if (guiButton.id == this.bntMoveZ.id) {
				this.schematic.position.z = (int) Minecraft.getMinecraft().thePlayer.posZ;
				net.mattbenson.modules.types.mods.Schematica.y = this.numericY.getValue();
				net.mattbenson.modules.types.mods.Schematica.repeaters.clear();
				net.mattbenson.modules.types.mods.Schematica.missinglocation.clear();
				net.mattbenson.modules.types.mods.Schematica.saveToFile();
				RenderSchematic.INSTANCE.refresh();
			}
			
			
		}
	}

	@Override
	public void handleKeyboardInput() throws IOException {
		super.handleKeyboardInput();

		if (this.btnFlip.enabled) {
			this.btnFlip.packedFGColour = isShiftKeyDown() ? 0xFF0000 : 0x000000;
		}

		if (this.btnRotate.enabled) {
			this.btnRotate.packedFGColour = isShiftKeyDown() ? 0xFF0000 : 0x000000;
		}
	}

	@Override
	public void drawScreen(final int par1, final int par2, final float par3) {
		// drawDefaultBackground();

		drawCenteredString(this.fontRendererObj, this.strMoveSchematic, this.centerX, this.centerY - 45, 0xFFFFFF);
		drawCenteredString(this.fontRendererObj, this.strMaterials, 50, this.height - 85, 0xFFFFFF);
		drawCenteredString(this.fontRendererObj, this.strPrinter, 50, this.height - 45, 0xFFFFFF);

		drawCenteredString(this.fontRendererObj, this.strOperations, this.width - 50, this.height - 120, 0xFFFFFF);

		drawString(this.fontRendererObj, this.strX, this.centerX - 65, this.centerY - 24, 0xFFFFFF);
		drawString(this.fontRendererObj, this.strY, this.centerX - 65, this.centerY + 1, 0xFFFFFF);
		drawString(this.fontRendererObj, this.strZ, this.centerX - 65, this.centerY + 26, 0xFFFFFF);
		
		if (net.mattbenson.modules.types.mods.Schematica.autoSaveRecent) {
			
			drawCenteredString(this.fontRendererObj, "Recently Used Schematics", this.centerX, 5, 0xFFFFFF);
			
		if (files.length > 0) {
	        String name = files[files.length - 1].getName();
	        drawString(this.fontRendererObj, name, this.centerX - 50, 25, 0xFFFFFF);
	    } 
		
		if (files.length > 1) {
	        String name = files[files.length - 2].getName();
	        drawString(this.fontRendererObj, name, this.centerX - 50, 50, 0xFFFFFF);
	    }
		
		if (files.length > 2) {
	        String name = files[files.length - 3].getName();
	        drawString(this.fontRendererObj, name, this.centerX - 50, 75, 0xFFFFFF);
	    }
		
		if (files.length > 3) {
	        String name = files[files.length - 4].getName();
	        drawString(this.fontRendererObj, name, this.centerX - 50, 100, 0xFFFFFF);
	    }
		
		if (files.length > 4) {
	        String name = files[files.length - 5].getName();
	        drawString(this.fontRendererObj, name, this.centerX - 50, 125, 0xFFFFFF);
	    }
		}
		super.drawScreen(par1, par2, par3);
	}
	
	
	@SubscribeEvent
	public void loadSchematic(int number) {
		
		if(mc.theWorld != null && mc.thePlayer != null) {
	 	
			ClientProxy.schematic = null;
	        RenderSchematic.INSTANCE.setWorldAndLoadRenderers(null);
	        SchematicPrinter.INSTANCE.setSchematic(null);
	        
	        String name = null;
	        if (number == 0) {
	        	name = files[files.length - 1].getName();
	        }
	        if (number == 1) {
	        	name = files[files.length - 2].getName();
	        } 
	        if (number == 2) {
	        	name = files[files.length - 3].getName();
	        }
	        if (number == 3) {
	        	name = files[files.length - 4].getName();
	        }
	        if (number == 4) {
	        	name = files[files.length - 5].getName();
	        }

	        net.mattbenson.modules.types.mods.Schematica.loadRecentSettings(name);
			
			try {
			if (com.github.lunatrius.schematica.Schematica.proxy.loadSchematic(mc.thePlayer, ConfigurationHandler.schematicDirectory, net.mattbenson.modules.types.mods.Schematica.previousSchematic)) {
				SchematicWorld schematic = ClientProxy.schematic;
				if (schematic != null) {
					if(!net.mattbenson.modules.types.mods.Schematica.flipsAndRotations.isEmpty()) {
						String[] steps = net.mattbenson.modules.types.mods.Schematica.flipsAndRotations.split("\n");
						for(String step : steps) {
							String[] med = step.split(":");
							String type = med[0];
							String direction = med[1];
							EnumFacing facing = EnumFacing.byName(direction);
							if(type.equalsIgnoreCase("rotation")) {
								RotationHelper.INSTANCE.rotate(schematic, facing, false);
							}
							if(type.equalsIgnoreCase("flip")) {
								FlipHelper.INSTANCE.flip(schematic, facing, false);
							}
						}
					}
					schematic.position.x = net.mattbenson.modules.types.mods.Schematica.x;
					schematic.position.y = net.mattbenson.modules.types.mods.Schematica.y;
					schematic.position.z = net.mattbenson.modules.types.mods.Schematica.z;
					
					RenderSchematic.INSTANCE.refresh();
					SchematicPrinter.INSTANCE.refresh();
				}
			}
		
		} catch(Exception e) {
			  //  Block of code to handle errors try now i dunno what else was there
		}
		}
	}
	
	
}
