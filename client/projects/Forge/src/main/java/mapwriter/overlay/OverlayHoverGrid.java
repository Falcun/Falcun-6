package mapwriter.overlay;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import mapwriter.api.IMwChunkOverlay;
import mapwriter.api.IMwDataProvider;
import mapwriter.gui.MwGui;
import mapwriter.map.MapView;
import mapwriter.map.mapmode.MapMode;
import net.minecraft.client.Minecraft;

public class OverlayHoverGrid implements IMwDataProvider
{
	int chunkX = 0;
	int chunkZ = 0;
	
	public class ChunkOverlay implements IMwChunkOverlay
	{

		Point coord;

		public ChunkOverlay(int x, int z)
		{
			this.coord = new Point(x, z);
		}

		@Override
		public Point getCoordinates()
		{
			return this.coord;
		}

		@Override
		public int getColor()
		{
			return new Color(0, 0, 0, 125).getRGB();
		}

		@Override
		public float getFilling()
		{
			return 1.0f;
		}

		@Override
		public boolean hasBorder()
		{
			return false;
		}

		@Override
		public float getBorderWidth()
		{
			return 0;
		}

		@Override
		public int getBorderColor()
		{
			return 0xff000000;
		}

		@Override
		public boolean customBorder() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void drawCustomBorder(Double topCorner, Double botCorner, MapView view, MapMode mode, IMwChunkOverlay chunk) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean custom() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void drawCustom(Double topCorner, Double botCorner, MapView mapView, MapMode mapMode, IMwChunkOverlay chunk) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void drawCustomBorderPost(Double topCorner, Double botCorner, MapView mapView, MapMode mapMode, IMwChunkOverlay chunk) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void drawCustomPost(Double topCorner, Double botCorner, MapView mapView, MapMode mapMode, IMwChunkOverlay chunk) {
			// TODO Auto-generated method stub
			
		}

	}
	
	@Override
	public String getStatusString(int dim, int bX, int bY, int bZ) {
		chunkX = bX >> 4;
		chunkZ = bZ >> 4;

		return "";
	}


	@Override
	public ArrayList<IMwChunkOverlay> getChunksOverlay(int dim, double centerX, double centerZ, double minX, double minZ, double maxX, double maxZ)
	{
		ArrayList<IMwChunkOverlay> chunks = new ArrayList<>();
		
		if(Minecraft.getMinecraft().currentScreen instanceof MwGui) {
			chunks.add(new ChunkOverlay(chunkX, chunkZ));
		}

		return chunks;
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

		return false;
	}

	@Override
	public void onMiddleClick(int dim, int bX, int bZ, MapView mapview, MwGui gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLeftClick(int dim, int bX, int bZ, MapView mapview, MwGui gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRightClick(int dim, int bX, int bZ, MapView mapview, MwGui gui) {
		// TODO Auto-generated method stub
		
	}

}
