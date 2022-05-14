package net.mattbenson.modules.types.mods;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.github.lunatrius.core.reference.Reference;
import com.github.lunatrius.core.util.BlockPosHelper;
import com.github.lunatrius.core.util.MBlockPos;
import com.github.lunatrius.schematica.block.state.BlockStateHelper;
import com.github.lunatrius.schematica.client.printer.SchematicPrinter;
import com.github.lunatrius.schematica.client.renderer.RenderSchematic;
import com.github.lunatrius.schematica.client.util.BlockList;
import com.github.lunatrius.schematica.client.util.BlockList.WrappedItemStack;
import com.github.lunatrius.schematica.client.util.FlipHelper;
import com.github.lunatrius.schematica.client.util.RotationHelper;
import com.github.lunatrius.schematica.client.world.SchematicWorld;
import com.github.lunatrius.schematica.command.client.CommandSchematicaLoad;
import com.github.lunatrius.schematica.command.client.CommandSchematicaReplace;
import com.github.lunatrius.schematica.handler.ConfigurationHandler;
import com.github.lunatrius.schematica.handler.client.InputHandler;
import com.github.lunatrius.schematica.proxy.ClientProxy;
import com.github.lunatrius.schematica.proxy.CommonProxy;

import net.mattbenson.Falcun;
import net.mattbenson.Wrapper;
import net.mattbenson.chat.ChatUtils;
import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.commands.CommandRegisterEvent;
import net.mattbenson.events.types.entity.EntityCreateEvent;
import net.mattbenson.events.types.input.KeyDownEvent;
import net.mattbenson.events.types.network.ChatMessageEvent;
import net.mattbenson.events.types.render.RenderEvent;
import net.mattbenson.events.types.render.RenderType;
import net.mattbenson.events.types.world.OnTickEvent;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.input.KeybindManager;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.utils.DrawUtils;
import net.mattbenson.utils.ForgeUtils;
import net.mattbenson.utils.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.event.ClickEvent;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Schematica extends Module {

	Timer timer = new Timer();

	@ConfigValue.Boolean(name = "[Beta] Mode", beta = true)
	public static boolean betaMode = false;
	
	@ConfigValue.Boolean(name = "[Beta] Use smart string placing")
	public static boolean betaString = false;
	
	@ConfigValue.Boolean(name = "[Beta] Only Destory Placed Blocks", beta = true)
	public static boolean destoryPlaced = true;
	
	@ConfigValue.Integer(name = "[Beta] Auto Tick Delay milliseconds", min = 25, max = 1000)
	public static int autotickDelay = 50;
	
	@ConfigValue.Boolean(name = "[Beta] Only print in Creative")
	public static boolean BetaOnlyCreative = true;
	
	@ConfigValue.Integer(name = "[Beta] Block Break Delay milliseconds", min = 50, max = 1000)
	public static int betaBreakDelay = 200;
	
	@ConfigValue.Integer(name = "Place Delay", min = 0, max = 20)
	public static int placeDelay = 0;

	@ConfigValue.Integer(name = "Timeout", min = 0, max = 100)
	public static int timeout = 0;
	
	@ConfigValue.Integer(name = "Tick Delay", min = 0, max = 10)
	public static int tickDelay = 0;

	@ConfigValue.Integer(name = "Place Distance", min = 1, max = 8)
	public static int placeDistance = 5;

	@ConfigValue.Integer(name = "Render Distance", min = 2, max = 16)
	public static int renderDistance = 8;
	
	@ConfigValue.Integer(name = "Max Tracers Y", min = 1, max = 256)
	public int maxTracersY = 256;

	@ConfigValue.Boolean(name = "Place Instantly")
	public static boolean placeInstantly = true;

	@ConfigValue.Boolean(name = "Destroy Blocks")
	public static boolean destryBlocks = false;

	@ConfigValue.Boolean(name = "Destroy Instantly")
	public static boolean destryBlocksInstantly = false;

	@ConfigValue.Boolean(name = "Place Adjacent")
	public static boolean placeAdjacent = true;

	@ConfigValue.Boolean(name = "Highlight")
	public static boolean higlight = true;

	@ConfigValue.Boolean(name = "Highlight Air")
	public static boolean higlightAir = false;

	@ConfigValue.Boolean(name = "Trace Repeaters")
	public boolean traceRepeaters = true;

	@ConfigValue.Boolean(name = "Missing ESP")
	public boolean missingESP = false;

	@ConfigValue.Boolean(name = "Missing Tracers")
	public boolean missingTracers = true;
	
	@ConfigValue.Float(name = "Line Width", min = 0.1F, max = 5)
	public float lineWidth = 1;
	
	@ConfigValue.Color(name = "ESP/Tracer Color")
	public Color espColor = Color.RED;

	@ConfigValue.Boolean(name = "Fix Dispensers")
	public static boolean fixDispensers = true;

	@ConfigValue.Boolean(name = "Auto tick repeaters")
	public static boolean autoTickRepeaters = false;

	@ConfigValue.Boolean(name = "Move with arrow keys")
	public boolean moveKeys = false;

	@ConfigValue.Boolean(name = "Auto Save/Load Schematics")
	public static boolean autoSaveAndLoad = false;
	
	@ConfigValue.Boolean(name = "Save Recent Schematics Data")
	public static boolean autoSaveRecent = true;
	
	@ConfigValue.Boolean(name = "360 Mode")
	public static boolean infintieMode = false;
	
	@ConfigValue.Boolean(name = "Break Bad Dispensers")
	public static boolean breakBadDispensers = true;
	
	@ConfigValue.Boolean(name = "Break Bad Pistons")
	public static boolean breakBadPistons = true;
	
	@ConfigValue.Boolean(name = "Break Bad Slabs")
	public static boolean breakBadSlabs = true;
	
	@ConfigValue.Boolean(name = "Auto Y1")
	public static boolean autoY1 = false;
	
	@ConfigValue.Boolean(name = "Auto Y1 if height 255")
	public static boolean autoY1auto = false;

	@ConfigValue.Boolean(name = "Slot 1")
	public static boolean slot1 = false;
	
	@ConfigValue.Boolean(name = "Slot 2")
	public static boolean slot2 = true;
	
	@ConfigValue.Boolean(name = "Slot 3")
	public static boolean slot3 = true;
	
	@ConfigValue.Boolean(name = "Slot 4")
	public static boolean slot4 = true;
	
	@ConfigValue.Boolean(name = "Slot 5")
	public static boolean slot5 = true;
	
	@ConfigValue.Boolean(name = "Slot 6")
	public static boolean slot6 = true;
	
	@ConfigValue.Boolean(name = "Slot 7")
	public static boolean slot7 = true;
	
	@ConfigValue.Boolean(name = "Slot 8")
	public static boolean slot8 = true;
	
	@ConfigValue.Boolean(name = "Slot 9")
	public static boolean slot9 = true;
	
	@ConfigValue.Keybind(name = "Trace All Key", description = "Trace all blocks key")
	public int keyBind = 0;
	
	
	public static boolean traceall;

	public static int x = 0;
	public static int y = 0;
	public static int z = 0;
	public static String previousSchematic = "None";
	
	public static List<BlockPos> repeaters = new ArrayList();
	public static List<BlockPos> missinglocation = new ArrayList();
	
	public static String path;

	public static String flipsAndRotations = "";

	public static long LENGTH = -1;
	public static long CUR = -1;
	

	@Override
	public void onEnable() {
		loadSettings();
		repeaters.clear();
		missinglocation.clear();
	}
	
	@Override
	public void onDisable() {
		loadSettings();
		repeaters.clear();
		missinglocation.clear();
	}
	
	public Schematica() {
		super("Schematica", ModuleCategory.MODS);
        loadSettings();
	}
	
	@SubscribeEvent
	public void chat(ChatMessageEvent event) {
		String uT = event.getMessage().getFormattedText();
		
		if(!uT.isEmpty() && uT.contains("{name:")
				&& uT.contains(", x:")
						&& uT.contains(", y:")
								&& uT.contains(", z:")) {

			Pattern pattern = Pattern.compile("\\{(.*?)\\}");
			Matcher m = pattern.matcher(uT);
			
			while(m.find()) {
			    IChatComponent component = new ChatComponentText(uT.replace(m.group(1), "").replace("{", "").replace("}", ""));
			    IChatComponent newComponent = new ChatComponentText("{" + m.group(1) +"}");

				newComponent.getChatStyle().setColor(EnumChatFormatting.AQUA);
				newComponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.LOAD_SCHEM,"{" + m.group(1) +"}"));
				component.appendSibling(newComponent);
				mc.thePlayer.addChatMessage(component);
			}
			event.setCancelled(true);
		}
	}
	
	public static void updateSchematic(String s) {
		SchematicWorld schematic = ClientProxy.schematic;
		if(schematic == null) {
			Wrapper.getInstance().addChat("Please load the schematic up!");
			return;
		}
		try {
		String[] types = s.split(", ");
		String name = types[0].replace("{name:", "");
		int x1 = Integer.parseInt(types[1].split(":")[1]);
		int y1 = Integer.parseInt(types[2].split(":")[1]);
		int z1  = Integer.parseInt(types[3].split(":")[1]);
		String fp = types[4].replace("}", "").replace("fr:", "").replace("R", "ROTATION").replace("F", "FLIP");
	
		if(!fp.isEmpty()) {
			String[] steps = fp.split(",");
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

		schematic.position.x = x1;
		schematic.position.y = y1;
		schematic.position.z = z1;
		
		
		RenderSchematic.INSTANCE.refresh();
		SchematicPrinter.INSTANCE.refresh();
		x = x1;
		y = y1;
		z = z1;
		flipsAndRotations = fp.replace(",", "\n");
		previousSchematic = name + ".schematic";
		saveToFile();
		
			Wrapper.getInstance().addChat("Successfully updated " + name + " schematic!");
		}
		catch(Exception ex) {
			Wrapper.getInstance().addChat("An error occurred trying to parse schematic!");
		}
	}
	
    public void calculatePercentage() {
    	int total = 0;
    	int placed = 0;
    	
    	SchematicWorld world = SchematicPrinter.INSTANCE.getSchematic();
    	
        if (world == null) {
            return;
        }
        
        MBlockPos mcPos = new MBlockPos();

        for (final MBlockPos pos : BlockPosHelper.getAllInBox(BlockPos.ORIGIN, new BlockPos(world.getWidth() - 1, world.getHeight() - 1, world.getLength() - 1))) {
        	if (!world.layerMode.shouldUseLayer(world, pos.getY())) {
                continue;
            }

            final IBlockState blockState = world.getBlockState(pos);
            final Block block = blockState.getBlock();

            if (block == Blocks.air || world.isAirBlock(pos)) {
                continue;
            }

            mcPos.set(world.position.add(pos));

            final IBlockState mcBlockState = mc.theWorld.getBlockState(mcPos);
            final boolean isPlaced = BlockStateHelper.areBlockStatesEqual(blockState, mcBlockState);
            
            if (isPlaced) {
                placed++;
            }
            
            total++;
        }
        
        LENGTH = total;
        CUR = placed;
    }
    
    @SubscribeEvent
	public void onTick(OnTickEvent event) {
		
		
		if(!enabled) return;
		if(mc != null && mc.thePlayer != null && mc.theWorld != null) {
			try {
			if (betaMode) {
				placeDelay = 0;
				timeout = 0;
				tickDelay = 0;
				destryBlocks = true;
				placeInstantly = true;
			} else {
				
				
			ConfigurationHandler.placeDelay = placeDelay;
			ConfigurationHandler.timeout = timeout;
			ConfigurationHandler.placeDistance = placeDistance;
			ConfigurationHandler.placeInstantly = placeInstantly;
			ConfigurationHandler.destroyBlocks = destryBlocks;
			ConfigurationHandler.destroyInstantly = destryBlocksInstantly;
			ConfigurationHandler.placeAdjacent = placeAdjacent;
			ConfigurationHandler.highlightAir = higlightAir;
			ConfigurationHandler.highlight = higlight;
			ConfigurationHandler.renderDistance = renderDistance;
			if(ConfigurationHandler.swapSlots[0] != slot1
					|| ConfigurationHandler.swapSlots[1] != slot2
					|| ConfigurationHandler.swapSlots[2] != slot3
					|| ConfigurationHandler.swapSlots[3] != slot4
					|| ConfigurationHandler.swapSlots[4] != slot5
					|| ConfigurationHandler.swapSlots[5] != slot6
					|| ConfigurationHandler.swapSlots[6] != slot7
					|| ConfigurationHandler.swapSlots[7] != slot8
					|| ConfigurationHandler.swapSlots[8] != slot9
					
					
					) {
			ConfigurationHandler.swapSlotsQueue.clear();
			ConfigurationHandler.swapSlots[0] = slot1;
			ConfigurationHandler.swapSlots[1] = slot2;
			ConfigurationHandler.swapSlots[2] = slot3;
			ConfigurationHandler.swapSlots[3] = slot4;
			ConfigurationHandler.swapSlots[4] = slot5;
			ConfigurationHandler.swapSlots[5] = slot6;
			ConfigurationHandler.swapSlots[6] = slot7;
			ConfigurationHandler.swapSlots[7] = slot8;
			ConfigurationHandler.swapSlots[8] = slot9;
			for(int i = 0; i < ConfigurationHandler.swapSlots.length; i++) {
				 if (ConfigurationHandler.swapSlots[i]) {
					 ConfigurationHandler.swapSlotsQueue.offer(i);
		            }
			}
			}
			}
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			if(!traceRepeaters) {
				repeaters.clear();
			}
		}
	}

	@SubscribeEvent
	public void onDrawScreen(RenderEvent event) {

	      if(event.getRenderType() != RenderType.WORLD) {
	            return;
	        }
		
		if(!enabled) return;
		final SchematicWorld schematic = ClientProxy.schematic;
		if(mc.theWorld != null && mc.thePlayer != null && schematic != null && schematic.isRendering ) {
			float partial = event.partialTicks;
			float px = (float)mc.thePlayer.posX;
			float py = (float)mc.thePlayer.posY;
			float pz = (float)mc.thePlayer.posZ;
			float mx = (float)mc.thePlayer.prevPosX;
			float my = (float)mc.thePlayer.prevPosY;
			float mz = (float)mc.thePlayer.prevPosZ;
			float dx = mx + ( px - mx ) * partial;
			float dy = my + ( py - my ) * partial;
			float dz = mz + ( pz - mz ) * partial;

			if(traceRepeaters && repeaters.size() > 0) {
				for(int i = 0; i < repeaters.size(); i++) {
					BlockPos pos = repeaters.get(i);
					DrawUtils.tracerLine(espColor.getRed() / 255.0F ,espColor.getGreen() / 255.0F ,espColor.getBlue() / 255.0F ,espColor.getAlpha() / 255.0F ,3F,pos.getX(),pos.getY(),pos.getZ());
				}
			}
			if(missinglocation.size() > 0) {
				for(int i = 0; i < missinglocation.size(); i++) {
					BlockPos pos = missinglocation.get(i);
					if(missingTracers) {
						if (pos.getY() > maxTracersY) return;
						DrawUtils.tracerLine(espColor.getRed() / 255.0F ,espColor.getGreen() / 255.0F ,espColor.getBlue() / 255.0F, espColor.getAlpha() / 255.0F, lineWidth,(float)pos.getX(),pos.getY() +0.5F,(float)pos.getZ());
					}
					if(missingESP) {
						if (pos.getY() > maxTracersY) return;
						DrawUtils.render2DEsp(pos.getX(), pos.getY(), pos.getZ(), lineWidth, espColor);
					}
				}
			}

		}
	}

	@SubscribeEvent
	public void onInput(KeyDownEvent event) {
		
		if(!enabled) return;
		if(!moveKeys) return;
		if (mc.currentScreen == null) {
			final SchematicWorld schematic = ClientProxy.schematic;
			if(schematic == null) return;
			EnumFacing facing = mc.thePlayer.getHorizontalFacing();
			if(Keyboard.getEventKey() == Keyboard.KEY_UP && Keyboard.getEventKeyState() && mc.gameSettings.keyBindSneak.isKeyDown()) {
				schematic.position.y += 1;
				repeaters.clear();
				missinglocation.clear();
				RenderSchematic.INSTANCE.refresh();
				SchematicPrinter.INSTANCE.refresh();
				x = schematic.position.x;
				y = schematic.position.y;
				z = schematic.position.z;
				saveToFile();

				return;
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_DOWN && Keyboard.getEventKeyState() && mc.gameSettings.keyBindSneak.isKeyDown()) {
				schematic.position.y -= 1;
				repeaters.clear();
				missinglocation.clear();
				RenderSchematic.INSTANCE.refresh();
				SchematicPrinter.INSTANCE.refresh();
				x = schematic.position.x;
				y = schematic.position.y;
				z = schematic.position.z;
				
				saveToFile();

				return;
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_UP && Keyboard.getEventKeyState()) {
				if(facing == EnumFacing.NORTH) {
					schematic.position.z -= 1;
				}
				if(facing == EnumFacing.WEST) {
					schematic.position.x -= 1;
				}
				if(facing == EnumFacing.SOUTH) {
					schematic.position.z += 1;
				}
				if(facing == EnumFacing.EAST) {
					schematic.position.x += 1;
				}
				repeaters.clear();
				missinglocation.clear();
				x = schematic.position.x;
				y = schematic.position.y;
				z = schematic.position.z;
				saveToFile();

				RenderSchematic.INSTANCE.refresh();
				SchematicPrinter.INSTANCE.refresh();
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_DOWN && Keyboard.getEventKeyState()) {
				if(facing == EnumFacing.NORTH) {
					schematic.position.z += 1;
				}
				if(facing == EnumFacing.WEST) {
					schematic.position.x += 1;
				}
				if(facing == EnumFacing.SOUTH) {
					schematic.position.z -= 1;
				}
				if(facing == EnumFacing.EAST) {
					schematic.position.x -= 1;
				}
				repeaters.clear();
				missinglocation.clear();
				x = schematic.position.x;
				y = schematic.position.y;
				z = schematic.position.z;
				saveToFile();

				RenderSchematic.INSTANCE.refresh();
				SchematicPrinter.INSTANCE.refresh();
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_LEFT && Keyboard.getEventKeyState()) {
				if(facing == EnumFacing.NORTH) {
					schematic.position.x -= 1;
				}
				if(facing == EnumFacing.WEST) {
					schematic.position.z += 1;
				}
				if(facing == EnumFacing.SOUTH) {
					schematic.position.x += 1;
				}
				if(facing == EnumFacing.EAST) {
					schematic.position.z -= 1;
				}
				repeaters.clear();
				missinglocation.clear();
				x = schematic.position.x;
				y = schematic.position.y;
				z = schematic.position.z;
				saveToFile();

				RenderSchematic.INSTANCE.refresh();
				SchematicPrinter.INSTANCE.refresh();
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_RIGHT && Keyboard.getEventKeyState()) {
				if(facing == EnumFacing.NORTH) {
					schematic.position.x += 1;
				}
				if(facing == EnumFacing.WEST) {
					schematic.position.z -= 1;
				}
				if(facing == EnumFacing.SOUTH) {
					schematic.position.x -= 1;
				}
				if(facing == EnumFacing.EAST) {
					schematic.position.z += 1;
				}
				repeaters.clear();
				missinglocation.clear();
				x = schematic.position.x;
				y = schematic.position.y;
				z = schematic.position.z;
				saveToFile();

				RenderSchematic.INSTANCE.refresh();
				SchematicPrinter.INSTANCE.refresh();
			}
	
		}
	}

	@SubscribeEvent
	public void onLogin(EntityJoinWorldEvent event) {
		if(!enabled) return;
		if(!autoSaveAndLoad) return;
		if(event.isCanceled()) return;
		if(mc.theWorld != null && mc.thePlayer != null && event.entity == mc.thePlayer && !previousSchematic.equalsIgnoreCase("none") && !previousSchematic.equalsIgnoreCase("none.schematic")) {
	 
			ClientProxy.schematic = null;
	        RenderSchematic.INSTANCE.setWorldAndLoadRenderers(null);
	        SchematicPrinter.INSTANCE.setSchematic(null);
			loadSettings();
			
			try {
			if (com.github.lunatrius.schematica.Schematica.proxy.loadSchematic(mc.thePlayer, ConfigurationHandler.schematicDirectory, previousSchematic)) {
				SchematicWorld schematic = ClientProxy.schematic;
				if (schematic != null) {
					if(!flipsAndRotations.isEmpty()) {
						String[] steps = flipsAndRotations.split("\n");
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
					schematic.position.x = x;
					schematic.position.y = y;
					schematic.position.z = z;
					
					RenderSchematic.INSTANCE.refresh();
					SchematicPrinter.INSTANCE.refresh();
				}
			}
		
		} catch(Exception e) {
			  //  Block of code to handle errors try now i dunno what else was there
		}
		}
	}

	public static void loadSettings() {
		if(!Falcun.MAIN_DIR.exists()) {
			Falcun.MAIN_DIR.mkdir();
		}
		
		File schemFile = new File(Falcun.MAIN_DIR + "/schematic.txt");	
		
		if(schemFile.exists()) {
			previousSchematic = "None";
			x = 0;
			y = 0; 
			z = 0;
			flipsAndRotations = "";
			try {
				BufferedReader bf = new BufferedReader(new FileReader(schemFile));
				String line;
				while ((line = bf.readLine()) != null) {
					String[] args = line.split("::");
					if(args[0].equalsIgnoreCase("Schematic")) {
						previousSchematic = args[1];
					} 
					if(args[0].equalsIgnoreCase("Position")) {
						String[] coords = args[1].split(",");
						x = Integer.parseInt(coords[0]);
						y = Integer.parseInt(coords[1]);
						z = Integer.parseInt(coords[2]);
					}
					if(args[0].equalsIgnoreCase("RF")) {
						String[] steps = args[1].split(",");
						for(String step : steps) {
							flipsAndRotations += step + "\n";
						}
					}
				}
			}
			catch(Exception ex) {

			}
		} else {
			try { 
				schemFile.createNewFile();
			}
			catch(Exception ex) {
				
			}
			x = 0;
			y = 0;
			z = 0;
			previousSchematic = "None";
			flipsAndRotations = "";
		}	
	}
	
	
	public static void loadRecentSettings(String name) {
		if(!Falcun.MAIN_DIR.exists()) {
			Falcun.MAIN_DIR.mkdir();
		}
		
		File directory = new File(Falcun.MAIN_DIR + File.separator + "schematics");
		if(!directory.exists()) {
			directory.mkdir();
		}
		
		File schemFileRecent = new File(Falcun.MAIN_DIR + File.separator + "schematics" + File.separator + name);	
		
		
		if(schemFileRecent.exists()) {
			previousSchematic = "None";
			x = 0;
			y = 0; 
			z = 0;
			flipsAndRotations = "";
			try {
				BufferedReader bf = new BufferedReader(new FileReader(schemFileRecent));
				String line;
				while ((line = bf.readLine()) != null) {
					String[] args = line.split("::");
					if(args[0].equalsIgnoreCase("Schematic")) {
						previousSchematic = args[1];
					} 
					if(args[0].equalsIgnoreCase("Position")) {
						String[] coords = args[1].split(",");
						x = Integer.parseInt(coords[0]);
						y = Integer.parseInt(coords[1]);
						z = Integer.parseInt(coords[2]);
					}
					if(args[0].equalsIgnoreCase("RF")) {
						String[] steps = args[1].split(",");
						for(String step : steps) {
							flipsAndRotations += step + "\n";
						}
					}
				}
			}
			catch(Exception ex) {

			}
		} else {
			try { 
				schemFileRecent.createNewFile();
			}
			catch(Exception ex) {
				
			}
			x = 0;
			y = 0;
			z = 0;
			previousSchematic = "None";
			flipsAndRotations = "";
		}	
	}

	public static void saveToFile() {
		if(autoSaveAndLoad) {
		if(!Falcun.MAIN_DIR.exists()) {
			Falcun.MAIN_DIR.mkdir();
		}
		File schemFile = new File(Falcun.MAIN_DIR + "/schematic.txt");	
		
		try {
			if(schemFile.exists()) {
				schemFile.delete();
				schemFile.createNewFile();
			} else {
				schemFile.createNewFile();
			}
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(schemFile));
			StringBuilder builder = new StringBuilder();
			builder.append("Schematic::");
			builder.append(previousSchematic + "\n");
			builder.append("Position");
			builder.append("::");
			builder.append(x);
			builder.append(",");
			builder.append(y);
			builder.append(",");
			builder.append(z + "\n");			
			builder.append("RF::");
			String[] steps = flipsAndRotations.split("\n");
			for(int i = 0; i < steps.length; i++) {
				builder.append(steps[i]);
				if(i != steps.length - 1) {
					builder.append(",");
				}
			}
			writer.write(builder.toString());
			writer.newLine();
			writer.close();
		} catch(Exception ex) {

		}
		}
		
		if (autoSaveRecent) {
		File directory = new File(Falcun.MAIN_DIR + File.separator + "schematics");
		if(!directory.exists()) {
			directory.mkdir();
		}
		
		File schemFileRecent = new File(Falcun.MAIN_DIR + File.separator + "schematics" + File.separator + previousSchematic);	
		
		try {
			if(schemFileRecent.exists()) {
				schemFileRecent.delete();
				schemFileRecent.createNewFile();
			} else {
				schemFileRecent.createNewFile();
			}
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(schemFileRecent));
			StringBuilder builder = new StringBuilder();
			builder.append("Schematic::");
			builder.append(previousSchematic + "\n");
			builder.append("Position");
			builder.append("::");
			builder.append(x);
			builder.append(",");
			builder.append(y);
			builder.append(",");
			builder.append(z + "\n");			
			builder.append("RF::");
			String[] steps = flipsAndRotations.split("\n");
			for(int i = 0; i < steps.length; i++) {
				builder.append(steps[i]);
				if(i != steps.length - 1) {
					builder.append(",");
				}
			}
			writer.write(builder.toString());
			writer.newLine();
			writer.close();
		} catch(Exception ex) {

		}
		}
	}

	
	@SubscribeEvent 
	public void onKeyPress(KeyDownEvent event) {
		
		if(!this.enabled) return;
		if(keyBind == 0) return;
		if(this.mc == null || this.mc.thePlayer == null || this.mc.theWorld == null) return;
		if (Keyboard.getEventKey() == keyBind)
		{
			if (Keyboard.getEventKeyState())
			{
				
				if (traceall) {
					final SchematicWorld schematic = ClientProxy.schematic;
					List<BlockList.WrappedItemStack> blockList = null;
					blockList = new BlockList().getList(mc.thePlayer, schematic, mc.theWorld);
					net.mattbenson.modules.types.mods.Schematica.missinglocation.clear();
					for(int i = 0; i < blockList.size(); i++) {
						WrappedItemStack wis = blockList.get(i);
						if(wis != null) {
							for(int j = 0; j < wis.positions.size(); j++) {
								BlockPos toAdd = wis.positions.get(j);
								net.mattbenson.modules.types.mods.Schematica.missinglocation.add(toAdd);
							}
						}
					}
					traceall = false;
				} else {
					net.mattbenson.modules.types.mods.Schematica.missinglocation.clear();
					traceall = true;
				}
				
				
			}
		}
	}
	
}
