package mapwriter.api;

import java.util.List;

import mapwriter.gui.MwGui;
import mapwriter.map.MapView;
import mapwriter.map.mapmode.MapMode;

public interface IMwDataProvider {
	List<IMwChunkOverlay> getChunksOverlay(int dim, double centerX, double centerZ, double minX, double minZ, double maxX, double maxZ);

	// Returns what should be added to the status bar by the addon.
	String getStatusString(int dim, int bX, int bY, int bZ);
	void onMiddleClick(int dim, int bX, int bZ, MapView mapview, MwGui gui);
	void onLeftClick(int dim, int bX, int bZ, MapView mapview, MwGui gui);
	void onRightClick(int dim, int bX, int bZ, MapView mapview, MwGui gui);
	void onDimensionChanged(int dimension, MapView mapview);
	void onMapCenterChanged(double vX, double vZ, MapView mapview);
	void onZoomChanged(int level, MapView mapview);
	void onOverlayActivated(MapView mapview);
	void onOverlayDeactivated(MapView mapview);
	void onDraw(MapView mapview, MapMode mapmode);
	boolean onMouseInput(MapView mapview, MapMode mapmode);
}