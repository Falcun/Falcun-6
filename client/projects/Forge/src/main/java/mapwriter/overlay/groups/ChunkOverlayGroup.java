package mapwriter.overlay.groups;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D.Double;

import mapwriter.Mw;
import mapwriter.api.IMwChunkOverlay;
import mapwriter.api.MwAPI;
import mapwriter.gui.MwGui;
import mapwriter.map.MapView;
import mapwriter.map.mapmode.MapMode;
import mapwriter.overlay.OverlayGroup;
import mapwriter.util.Render;
import net.mattbenson.modules.types.mods.MapWriter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class ChunkOverlayGroup implements IMwChunkOverlay
{

	private Point coord;
	private String text;
	private int color;
	public boolean hover;
	
	public ChunkOverlayGroup(int x, int z, int color)
	{
		this.coord = new Point(x, z);
		this.color = color;
	}
	
	public ChunkOverlayGroup(int x, int z, String text)
	{
		this.coord = new Point(x, z);
		this.text = text;
	}
	
	public ChunkOverlayGroup(int x, int z)
	{
		this.coord = new Point(x, z);
		this.hover = true;
	}
	
	public boolean isHover() {
		return hover;
	}
	
	public boolean isText() {
		return text != null;
	}
	
	public boolean isColor() {
		return !isText() && !hover;
	}

	public String getText() {
		return text;
	}
	
	public int getChunkColor() {
		return color;
	}
	
	@Override
	public Point getCoordinates()
	{
		return this.coord;
	}

	@Override
	public int getColor()
	{
		return 0x00ffffff;
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
		return (float) MapWriter.gridWidth;
	}

	@Override
	public int getBorderColor()
	{
		return MapWriter.gridColor.getRGB();
	}

	@Override
	public boolean customBorder() {
		return false;
	}

	@Override
	public void drawCustomBorder(Double topCorner, Double botCorner, MapView view, MapMode mode, IMwChunkOverlay chunk) {}

	@Override
	public boolean custom() {
		return true;
	}

	@Override
	public void drawCustom(Double topCorner, Double botCorner, MapView mapView, MapMode mapMode, IMwChunkOverlay chunk) {
		if(!(chunk instanceof ChunkOverlayGroup)) {
			return;
		}
		
		ChunkOverlayGroup obj = (ChunkOverlayGroup) chunk;
		
		if(!obj.isColor()) {
			return;
		}
		
		double x = topCorner.x;
		double y = topCorner.y;
		double width = botCorner.x - topCorner.x;
		double height = botCorner.y - topCorner.y;
		
		Render.setColour(obj.getChunkColor());
		Render.drawRect(x, y, width, height);
	}
	
	@Override
	public void drawCustomBorderPost(Double topCorner, Double botCorner, MapView mapView, MapMode mapMode, IMwChunkOverlay chunk) {
	}

	@Override
	public void drawCustomPost(Double topCorner, Double botCorner, MapView mapView, MapMode mapMode, IMwChunkOverlay chunk) {
		if(!(chunk instanceof ChunkOverlayGroup)) {
			return;
		}
		
		ChunkOverlayGroup obj = (ChunkOverlayGroup) chunk;
		
		double x = topCorner.x;
		double y = topCorner.y;
		double width = botCorner.x - topCorner.x;
		double height = botCorner.y - topCorner.y;
		
		if(obj.isColor()) {
			return;
		} else if(obj.isText()) {
			Render.drawString((int)Math.round(x + width / 2 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(obj.getText()) / 2), (int)Math.round(y + height / 2 - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT / 2), Color.WHITE.getRGB(), obj.getText());
		}
		
		if(obj.isHover()) {
			if(Minecraft.getMinecraft().currentScreen instanceof MwGui) {
				if(MwAPI.getCurrentDataProvider() != null && MwAPI.getCurrentDataProvider() instanceof OverlayGroup) {
					OverlayGroup overlay = (OverlayGroup) MwAPI.getCurrentDataProvider();
					overlay.onClick();
				}
	        }
			
			if(Mw.getInstance().type == 0) {
				Color pre = new Color(Mw.getInstance().color, true);
				Color color = new Color(pre.getRed(), pre.getGreen(), pre.getBlue(), Math.max(0, OverlayGroup.ALPHA - 50));
				Render.setColour(color.getRGB());
				Render.drawRect(x, y, width, height);
			} else if(Mw.getInstance().type == 1) {
				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				Color color = new Color(255, 255, 255, Math.max(0, OverlayGroup.ALPHA));
				Render.drawString((int)Math.round(x + width / 2 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(Mw.getInstance().lastText) / 2), (int)Math.round(y + height / 2 - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT / 2), color.getRGB(), Mw.getInstance().lastText);
				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
			}
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) {
			return true;
		}
		
		if(o instanceof ChunkOverlayGroup) {
			ChunkOverlayGroup obj = (ChunkOverlayGroup) o;
			
			if(obj.coord.x == coord.x && obj.coord.y == coord.y) {
				if(obj.isText() && isText()) {
					return true;
				}
				
				if(obj.isColor() && isColor()) {
					return true;
				}
				
				if(obj.isHover() && isHover()) {
					return true;
				}
			}
		}
		
		return false;
	}
}
