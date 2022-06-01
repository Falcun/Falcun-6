package mapwriter.api;

import java.awt.Point;
import java.awt.geom.Point2D.Double;

import mapwriter.map.MapView;
import mapwriter.map.mapmode.MapMode;

public interface IMwChunkOverlay {
	Point getCoordinates();

	int getColor();

	float getFilling();

	boolean hasBorder();

	float getBorderWidth();

	int getBorderColor();

	// Faction additions
	boolean customBorder();

	boolean custom();
	
	void drawCustomBorder(Point.Double topCorner, Point.Double botCorner, MapView view, MapMode mode, IMwChunkOverlay chunk);
		
	void drawCustomBorderPost(Double topCorner, Double botCorner, MapView mapView, MapMode mapMode, IMwChunkOverlay chunk);

	void drawCustom(Double topCorner, Double botCorner, MapView mapView, MapMode mapMode, IMwChunkOverlay chunk);
	
	void drawCustomPost(Double topCorner, Double botCorner, MapView mapView, MapMode mapMode, IMwChunkOverlay chunk);
}