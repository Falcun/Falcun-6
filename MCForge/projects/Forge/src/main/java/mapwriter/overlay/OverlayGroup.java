package mapwriter.overlay;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Mouse;

import mapwriter.Mw;
import mapwriter.api.IMwChunkOverlay;
import mapwriter.api.IMwDataProvider;
import mapwriter.api.MwAPI;
import mapwriter.gui.MwGui;
import mapwriter.map.MapView;
import mapwriter.map.mapmode.MapMode;
import mapwriter.overlay.groups.ChunkOverlayGroup;
import net.mattbenson.Falcun;
import net.mattbenson.network.NetworkingClient;
import net.mattbenson.network.common.ChunkData;
import net.mattbenson.network.common.GroupData;
import net.mattbenson.network.network.packets.chunks.ChunkDataList;
import net.mattbenson.network.network.packets.chunks.data.ChunkColor;
import net.mattbenson.network.network.packets.chunks.data.ChunkName;
import net.mattbenson.network.network.packets.groups.GroupList;
import net.mattbenson.utils.NetworkUtils;
import net.minecraft.client.Minecraft;

public class OverlayGroup implements IMwDataProvider
{
	public static final int ALPHA = 100;
	
	public static int chunkX;
	public static int chunkZ;
	
	private static List<Point> pending;
	private static Map<Long, Boolean> cached;
	private static List<IMwChunkOverlay> chunks;

	public static List<ChunkColor> currentRenderingColor;
	public static List<ChunkName> currentRenderingName;
	
	public OverlayGroup() {
		pending = new CopyOnWriteArrayList<>();
		cached = new ConcurrentHashMap<>();
		chunks = new ArrayList<>();
		currentRenderingColor = new ArrayList<>();
		currentRenderingName = new ArrayList<>();
	}
	
	public static void removePending(int x, int y) {
		for(Point p : pending) {
			if(p.x == x && p.y == y) {
				pending.remove(p);
				break;
			}
		}
	}
	
	public static void updateMap() {
		try {
		if(Falcun.getInstance() == null || Minecraft.getMinecraft().thePlayer == null) {
			return;
		}
		
		if(cached != null) {
			cached.clear();
		}
		
		if(chunks != null && cached != null) {
			List<IMwChunkOverlay> chunks = new ArrayList<>();
			List<ChunkColor> currentRenderingColor = new ArrayList<>();
			List<ChunkName> currentRenderingName = new ArrayList<>();
			
			for(ChunkData data : ChunkDataList.getChunkDatas()) {
				if(cached == null || data == null) {
					continue;
				} else if(cached.containsKey(data.getGroup())) {
					if(!cached.get(data.getGroup())) {
						continue;
					}
				} else {
					GroupData group = GroupList.getGroupById(data.getGroup());
					
					if(group == null) {
						cached.put(data.getGroup(), false);
						continue;
					}
					
	
					
					cached.put(data.getGroup(), true);
					
		
				}
				
				if(data instanceof ChunkColor) {
					ChunkColor chunk = (ChunkColor) data;
					
					if(!NetworkUtils.getServer().equalsIgnoreCase(chunk.getServer()) || Minecraft.getMinecraft().thePlayer.dimension != chunk.getDimension()) {
						continue;
					}
					
					if(!chunks.stream().anyMatch(entry -> entry.equals(chunk.getOverlay()))) {
						chunks.add(chunk.getOverlay());
					}
					
					currentRenderingColor.add(chunk);
				} else if(data instanceof ChunkName) {
					ChunkName chunk = (ChunkName) data;
					
					if(!NetworkUtils.getServer().equalsIgnoreCase(chunk.getServer()) || Minecraft.getMinecraft().thePlayer.dimension != chunk.getDimension()) {
						continue;
					}
					
					if(!chunks.stream().anyMatch(entry -> entry.equals(chunk.getOverlay()))) {
						chunks.add(chunk.getOverlay());
					}
					
					currentRenderingName.add(chunk);
				}
			}
			
			OverlayGroup.currentRenderingColor = currentRenderingColor;
			OverlayGroup.currentRenderingName = currentRenderingName;
			OverlayGroup.chunks = chunks;
		}
		
		if(pending != null) {
			pending.clear();
		}
		} catch (Exception e) {
			
		}
	}

	@Override
	public List<IMwChunkOverlay> getChunksOverlay(int dim, double centerX, double centerZ, double minX, double minZ, double maxX, double maxZ)
	{
		List<IMwChunkOverlay> chunks = new ArrayList<IMwChunkOverlay>();
		
		int padding = 7;

		int minCX = ((int)Math.floor(minX) >> 4) - padding;
		int maxCX = ((int)Math.floor(maxX) >> 4) + padding;
		int minCZ = ((int)Math.floor(minZ) >> 4) - padding;
		int maxCZ = ((int)Math.floor(maxZ) >> 4) + padding;
		
		for(IMwChunkOverlay chunk : OverlayGroup.chunks) {
			ChunkOverlayGroup overlay = (ChunkOverlayGroup) chunk;
			
			if (minCX <= overlay.getCoordinates().getX() && minCZ <= overlay.getCoordinates().getY() && maxCX >= overlay.getCoordinates().getX() && maxCZ >= overlay.getCoordinates().getY()) {
				chunks.add(chunk);
			}
		}
		
		if(Minecraft.getMinecraft().currentScreen instanceof MwGui) {
			int x = chunkX >> 4;
			int z = chunkZ >> 4;
			
			chunks.add(new ChunkOverlayGroup(x, z));
		}
		
		return chunks;
	}

	@Override
	public String getStatusString(int dim, int bX, int bY, int bZ)
	{
		String base = MwAPI.getDataProvider("Faction").getStatusString(dim, bX, bY, bZ);
		
		chunkX = bX;
		chunkZ = bZ;
		
		int xPos = (int)bX >> 4;
		int zPos = (int)bZ >> 4;
		
		long groupId = -1;
		
		for(ChunkColor chunk : currentRenderingColor) {
			int chunkX = chunk.getX() >> 4;
			int chunkZ = chunk.getZ() >> 4;
			
			if(chunkX == xPos && chunkZ == zPos) {
				groupId = chunk.getGroup();
				break;
			}
		}
		
		for(ChunkName chunk : currentRenderingName) {
			int chunkX = chunk.getX() >> 4;
			int chunkZ = chunk.getZ() >> 4;
			
			if(chunkX == xPos && chunkZ == zPos) {
				groupId = chunk.getGroup();
				break;
			}
		}
		
		if(groupId >= 0) {
			GroupData data = GroupList.getGroupById(groupId);
			
			if(data != null) {
				return base + ", group: " + data.getName();
			}
		}
		
		return base;
	}
	@Override
	public void onDimensionChanged(int dimension, MapView mapview)
	{
	}

	@Override
	public void onMapCenterChanged(double vX, double vZ, MapView mapview)
	{

	}

	@Override
	public void onZoomChanged(int level, MapView mapview)
	{

	}

	@Override
	public void onOverlayActivated(MapView mapview)
	{

	}

	@Override
	public void onOverlayDeactivated(MapView mapview)
	{

	}

	@Override
	public void onDraw(MapView mapview, MapMode mapmode)
	{

	}

	@Override
	public boolean onMouseInput(MapView mapview, MapMode mapmode)
	{
		return onClick();
	}
	
	public boolean onClick() {
		if(!Mouse.isButtonDown(1) && !Mouse.isButtonDown(2)) {
			return false;
		}
		
		boolean delete = Mouse.isButtonDown(2) && !Mouse.isButtonDown(1);
		int bX = chunkX;
		int bZ = chunkZ;
		
		boolean foundName = false;
		boolean foundColor = false;
		
		int xPos = bX >> 4;
		int zPos = bZ >> 4;
		
		ChunkData found = null;
		
		for(ChunkColor chunk : currentRenderingColor) {
			int chunkX = chunk.getX() >> 4;
			int chunkZ = chunk.getZ() >> 4;
			
			if(chunkX == xPos && chunkZ == zPos) {
				found = chunk;
				foundColor = true;
				break;
			}
		}
		
		for(ChunkName chunk : currentRenderingName) {
			int chunkX = chunk.getX() >> 4;
			int chunkZ = chunk.getZ() >> 4;
			
			if(chunkX == xPos && chunkZ == zPos) {
				found = chunk;
				foundName = true;
				break;
			}
		}
		
		if(delete) {
			if(found != null) {
				NetworkingClient.sendLine("RemoveChunkData", found.getId() + "");
				
				if(!Mw.getInstance().groupEdited.contains(found.getGroup())) {
					Mw.getInstance().groupEdited.add(found.getGroup());
				}
			}
			
			return true;
		}
		
		if(foundColor && foundName) {
			return true;
		}
		
		Point point = new Point(chunkX, chunkZ);
		
		if((!foundColor && Mw.getInstance().type == 0) || (!foundName && Mw.getInstance().type == 1)) {	
			for(Point p : pending) {
				int pChunkX = p.x >> 4;
				int pChunkZ = p.y >> 4;
				int pointChunkX = point.x >> 4;
				int pointChunkZ = point.y >> 4;
				
				if(pChunkX == pointChunkX && pChunkZ == pointChunkZ) {
					return true;
				}
			}
		}
		
		GroupData group = GroupList.getGroupByName(Mw.getInstance().group);
		
		if(group != null) {
			if(Mw.getInstance().type == 0 && !foundColor) {
				pending.add(point);
				
				Color pre = new Color(Mw.getInstance().color, true);
				Color color = new Color(pre.getRed(), pre.getGreen(), pre.getBlue(), ALPHA);
				ChunkDataList.sendChunkColor(bX, 0, bZ, color.getRGB(), Minecraft.getMinecraft().thePlayer.dimension, group.getId());
				return true;
			} else if(Mw.getInstance().type == 1 && !foundName) {
				if(Mw.getInstance().lastText != null && Mw.getInstance().lastText.trim().length() > 0) {
					pending.add(point);
					
					ChunkDataList.sendNameChunk(bX, 0, bZ, Mw.getInstance().lastText, Minecraft.getMinecraft().thePlayer.dimension, group.getId());
				}
				
				return true;
			}
			
			if(!Mw.getInstance().groupEdited.contains(group.getId())) {
				Mw.getInstance().groupEdited.add(group.getId());
			}
		}
		
		return true;
	}

	@Override
	public void onMiddleClick(int dim, int bX, int bZ, MapView mapview, MwGui gui) {
		ChunkData found = null;
		
		int xPos = bX >> 4;
		int zPos = bZ >> 4;
		
		for(ChunkName chunk : currentRenderingName) {
			int chunkX = chunk.getX() >> 4;
			int chunkZ = chunk.getZ() >> 4;
			
			if(chunkX == xPos && chunkZ == zPos) {
				found = chunk;
				break;
			}
		}
		
		if(found == null) {
			for(ChunkColor chunk : currentRenderingColor) {
				int chunkX = chunk.getX() >> 4;
				int chunkZ = chunk.getZ() >> 4;
				
				if(chunkX == xPos && chunkZ == zPos) {
					found = chunk;
					break;
				}
			}
		}
		
		if(found == null) {
			MwAPI.getDataProvider("Faction").onMiddleClick(dim, bX, bZ, mapview, gui);;
		}
	}

	@Override
	public void onLeftClick(int dim, int bX, int bZ, MapView mapview, MwGui gui) {
		MwAPI.getDataProvider("Faction").onLeftClick(dim, bX, bZ, mapview, gui);
	}

	@Override
	public void onRightClick(int dim, int bX, int bZ, MapView mapview, MwGui gui) {
		MwAPI.getDataProvider("Faction").onRightClick(dim, bX, bZ, mapview, gui);
	}

}