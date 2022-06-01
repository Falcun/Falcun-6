package net.mattbenson.modules.types.other;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import net.mattbenson.Falcun;
import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.falcun.ConfigChangeEvent;
import net.mattbenson.events.types.world.OnTickEvent;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.minecraft.client.Minecraft;

public class ConsoleClient extends Module {
	
	@ConfigValue.Text(name = "Server IP")
	public String serverIP = "";
	
	@ConfigValue.List(name = "Minecraft Version", values = {"auto", "1.7.9", "1.8.9", "1.17.1"})
	public String mcVersion = "auto";
	
	@ConfigValue.Boolean(name = "Terrain And Movements")
	private boolean terrainandmovements = false;
	
	@ConfigValue.Boolean(name = "Chat Timestamps")
	private boolean timeStamps = true;
	
	@ConfigValue.Boolean(name = "AntiAFK Command Toggle")
	private boolean antiAFK = false;
	
	@ConfigValue.Text(name = "AntiAFK Command")
	public String antiafkCommandString = "";
	
	@ConfigValue.Integer(name = "AntiAFK Command Delay (Minutes)", min = 1, max = 60)
	public int antiafkDelay = 30;
	
	 private static final int MAPSIZE = 4 * 1024 ; 
	
	
	public ConsoleClient() {
		super("Console Client", ModuleCategory.OTHER);
	}
	
	@SubscribeEvent
	public void onConfigChange(ConfigChangeEvent event) {

		
	    try (FileReader fr = new FileReader(Falcun.ASSETS_DIR + "/MinecraftClient.ini");
	             FileWriter fw = new FileWriter(Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/MinecraftClient.ini")) {
	            int c = fr.read();
	            while(c!=-1) {
	                fw.write(c);
	                c = fr.read();
	            }
	        } catch(IOException e) {
	            e.printStackTrace();
	        }
		
		try {
			int mcversionline = find("mcversion=");
			if (mcversionline > 0) {
				setVariable(mcversionline, "mcversion=" + mcVersion);
			}

			int terrainandmovementsline = find("terrainandmovements=");
			if (terrainandmovementsline > 0) {
				if (terrainandmovements) {
					setVariable(terrainandmovementsline, "terrainandmovements=true");
				} else {
					setVariable(terrainandmovementsline, "terrainandmovements=false");
				}
			}
			
			int timestampsline = find("timestamps=");
			if (timestampsline > 0) {
				if (timeStamps) {
					setVariable(timestampsline, "timestamps=true");
				} else {
					setVariable(timestampsline, "timestamps=false");
				}
			}
			
			int antiafkline = find("[AntiAFK]");
			if (antiafkline > 0) {
				if (antiAFK) {
					setVariable(antiafkline + 3, "enabled=true");
					setVariable(antiafkline + 4, "delay=" + antiafkDelay * 60);
					setVariable(antiafkline + 5, "command=" + antiafkCommandString);
				} else {
					setVariable(antiafkline + 3, "enabled=false");
					setVariable(antiafkline + 4, "delay=" + antiafkDelay * 60);
					setVariable(antiafkline + 5, "command=" + antiafkCommandString);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
	
	}
	
	public static void setVariable(int lineNumber, String data) throws IOException {
	    Path path = Paths.get(Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/MinecraftClient.ini");
	    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
	    lines.set(lineNumber - 1, data);
	    Files.write(path, lines, StandardCharsets.UTF_8);
	}

	public int find(String word) throws IOException {
		System.out.println(Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/MinecraftClient.ini");
		File file = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/MinecraftClient.ini");
		
		int counter = 1;
		LineIterator it = null;
		try {
			it = FileUtils.lineIterator(file, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
		    while (it.hasNext()) {
		    String line = it.nextLine();
		    if (line.contains(word)) {
		    	return counter;
		    } 
		    counter++;
		    }
		} finally {
		    it.close();
		}
		return 0;

	}
	
}
