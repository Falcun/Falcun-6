package net.mattbenson.network.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

import com.github.lunatrius.schematica.Schematica;
import com.github.lunatrius.schematica.client.printer.SchematicPrinter;
import com.github.lunatrius.schematica.client.renderer.RenderSchematic;
import com.github.lunatrius.schematica.client.util.FlipHelper;
import com.github.lunatrius.schematica.client.util.RotationHelper;
import com.github.lunatrius.schematica.client.world.SchematicWorld;
import com.github.lunatrius.schematica.handler.ConfigurationHandler;
import com.github.lunatrius.schematica.proxy.ClientProxy;

import net.mattbenson.file.FileHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;

public class SchemShareRequest {
	private GroupData group;
	private String name;
	private int x;
	private int y;
	private int z;
	private String schematica;
	private List<EnumFacing> flips;
	private List<EnumFacing> rotations;

	private long id;
	private String server;
	
	private long time = -1;
	private boolean active;
	
	public SchemShareRequest(GroupData group, String name, int x, int y, int z, String schematica, List<EnumFacing> flips, List<EnumFacing> rotations) {
		this(-1, group, name, x, y, z, schematica, flips, rotations, null);
	}

	public SchemShareRequest(long id, GroupData group, String name, int x, int y, int z, String schematica, List<EnumFacing> flips, List<EnumFacing> rotations, String server) {
		this.group = group;
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.schematica = schematica;
		this.flips = flips;
		this.rotations = rotations;
		
		this.id = id;
		this.server = server;
	}
	
	public GroupData getGroup() {
		return group;
	}

	public String getName() {
		return name;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public String getSchematica() {
		return schematica;
	}
	
	public List<EnumFacing> getFlips() {
		return flips;
	}
	
	public List<EnumFacing> getRotations() {
		return rotations;
	}
	
	public long getId() {
		return id;
	}
	
	public String getServer() {
		return server;
	}
	
	public long getStart() {
		return time;
	}
	
	public boolean isActive() {
		return time >= 0;
	}
	
	public void setActive() {
		this.time = System.currentTimeMillis();
	}
	
	public boolean isAccepted() {
		return active;
	}
	
	public void accept() {
		active = true;
		
		String suffix = ".schematic";
		
		if(name.toLowerCase().endsWith(suffix)) {
			name = name.substring(0, name.length() - suffix.length());
		}
		
		String prefix = "shared.";
		
		if(name.startsWith(prefix)) {
			name = name.substring(prefix.length());
		}
		
		File file = Paths.get(ConfigurationHandler.schematicDirectory.getPath(), "shared." + name + ".schematic").toFile();
		FileHandler handler = new FileHandler(file);
		
		try {
			handler.init();
			handler.writeToFile(Base64.getDecoder().decode(schematica));
		} catch (IOException | IllegalArgumentException e) {
			e.printStackTrace();
			return;
		}
		
		ClientProxy.schematic = null;
		ClientProxy.loadedFile = null;
		
		RenderSchematic.INSTANCE.setWorldAndLoadRenderers(null);
		SchematicPrinter.INSTANCE.setSchematic(null);
		
		if(Schematica.proxy.loadSchematic(Minecraft.getMinecraft().thePlayer, ConfigurationHandler.schematicDirectory, file.getName())) {
			SchematicWorld schematic = ClientProxy.schematic;
			
			if(schematic != null) {
				for(EnumFacing rotation : rotations) {
					RotationHelper.INSTANCE.rotate(schematic, rotation, false);
				}
				
				for(EnumFacing flip : flips) {
					FlipHelper.INSTANCE.flip(schematic, flip, false);
				}
			}
			
			schematic.position.x = x;
			schematic.position.y = y;
			schematic.position.z = z;
			
            if (net.mattbenson.modules.types.mods.Schematica.autoY1) {
                ClientProxy.schematic.position.y = 1;
            }
            
            if (net.mattbenson.modules.types.mods.Schematica.autoY1auto) {
            	if (schematic.getHeight() >= 254) {
            		ClientProxy.schematic.position.y = 1;
            	}
            }
			
			RenderSchematic.INSTANCE.refresh();
			SchematicPrinter.INSTANCE.refresh();
		}
	}
}
