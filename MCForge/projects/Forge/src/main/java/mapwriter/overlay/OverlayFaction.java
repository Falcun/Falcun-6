package mapwriter.overlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import mapwriter.Mw;
import mapwriter.api.IMwChunkOverlay;
import mapwriter.api.IMwDataProvider;
import mapwriter.fac.Faction;
import mapwriter.fac.Faction.Claim;
import mapwriter.gui.MwGui;
import mapwriter.gui.MwGuiFactionDialog;
import mapwriter.map.MapView;
import mapwriter.map.mapmode.MapMode;
import net.minecraft.client.Minecraft;

public class OverlayFaction implements IMwDataProvider {
	int chunkX = 0;
	int chunkZ = 0;
	@Override
	public ArrayList<IMwChunkOverlay> getChunksOverlay(int dim, double centerX, double centerZ, double minX, double minZ, double maxX, double maxZ) {
		int padding = 7;

		int minCX = ((int)Math.floor(minX) >> 4) - padding;
		int maxCX = ((int)Math.floor(maxX) >> 4) + padding;
		int minCZ = ((int)Math.floor(minZ) >> 4) - padding;
		int maxCZ = ((int)Math.floor(maxZ) >> 4) + padding;

		ArrayList<IMwChunkOverlay> chunks = new ArrayList<IMwChunkOverlay>();

		Map<String, Faction> factions = new HashMap<>();
		factions.putAll(Mw.getInstance().facInput.factions);
		Iterator<String> itfactions = factions.keySet().iterator();
		while(itfactions.hasNext()) {
			String name = itfactions.next();
			Faction fac = Mw.getInstance().facInput.factions.get(name);
			if(fac != null) {
				for (int i = 0; i < fac.claimAmount(); i++) {
					Claim claim = fac.getClaims().get(i);
					if (minCX <= claim.getX() && minCZ <= claim.getZ() && maxCX >= claim.getX() && maxCZ >= claim.getZ()) {
						chunks.add(claim);
					}
				}
			}
		}
		return chunks;
	}

	@Override
	public String getStatusString(int dim, int bX, int bY, int bZ) {
		String hoverName = "Wilderness";
		String claims = "Unknown";

		int chunkX = (bX >> 4);
		int chunkZ = (bZ >> 4);


		Faction fac = Mw.getInstance().facInput.getFaction(chunkX, chunkZ);

		if (fac != null) {
			hoverName = fac.getName();
			claims = "" + fac.claimAmount();
		}
		//return "";
		return ", Chunk: (" + chunkX + ", " + chunkZ + ")\u00A7r, \u00A7bFaction: \u00A76" + hoverName + "\u00A7r (\u00A72" + claims + "\u00A7r)";
	}

	@Override
	public void onLeftClick(int dim, int bX, int bZ, MapView mapview, MwGui gui) {
		//		TODO: SOON TM; v2.0?
		//		int cX = bX >> 4;
		//		int cZ = bZ >> 4;
		//		
		//		Faction faction = Mw.getInstance().facInput.getFaction(cX, cZ);
		//		if (faction != null) Minecraft.getMinecraft().thePlayer.sendChatMessage("/f f " + faction.getName());
	}

	@Override
	public void onMiddleClick(int dim, int bX, int bZ, MapView mapview, MwGui gui) {
		String hoverName = "Wilderness";

		int cX = (bX >> 4);
		int cZ = (bZ >> 4);

		Faction faction = Mw.getInstance().facInput.getFaction(cX, cZ);
		if (faction != null) hoverName = faction.getName();

		if (!hoverName.equalsIgnoreCase("Wilderness")) {
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
				Mw.getInstance().facInput.removeFaction(faction);
			else Minecraft.getMinecraft().displayGuiScreen(new MwGuiFactionDialog(gui, faction));
		}
	}

	@Override public void onDimensionChanged(int dimension, MapView mapview) {}
	@Override public void onMapCenterChanged(double vX, double vZ, MapView mapview) {}
	@Override public void onZoomChanged(int level, MapView mapview) {
	}
	@Override public void onOverlayActivated(MapView mapview) {}
	@Override public void onOverlayDeactivated(MapView mapview) {}
	@Override public void onDraw(MapView mapview, MapMode mapmode) {
	}
	@Override public boolean onMouseInput(MapView mapview, MapMode mapmode) { return false; }
	@Override public void onRightClick(int dim, int bX, int bZ, MapView mapview, MwGui gui) {}

}