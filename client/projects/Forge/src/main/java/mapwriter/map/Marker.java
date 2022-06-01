package mapwriter.map;

import java.awt.Point;
import java.util.Map.Entry;
import java.util.UUID;

import mapwriter.map.mapmode.MapMode;
import mapwriter.util.Render;
import mapwriter.util.Utils;
import net.mattbenson.network.common.GroupData;
import net.mattbenson.network.network.packets.groups.GroupList;
import net.mattbenson.playerlocations.LocationManager;
import net.mattbenson.playerlocations.PlayerLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class Marker {
	public String name;
	public String groupName;
	public int x;
	public int y;
	public int z;
	public int dimension;
	public int colour;
	public boolean visible;
	public String server;

	public Point.Double screenPos = new Point.Double(0, 0);

	public Marker(String name, String groupName, int x, int y, int z,
			int dimension, int colour, String server) {
		this.name = Utils.mungeStringForConfig(name);
		this.x = x;
		this.y = y;
		this.z = z;
		this.dimension = dimension;
		this.colour = colour;
		this.groupName = Utils.mungeStringForConfig(groupName);
		visible = true;
		this.server = server;
	}

	public String getString() {
		return String.format("%s %s (%d, %d, %d) %d %06x", this.name,
				this.groupName, this.x, this.y, this.z, this.dimension,
				this.colour & 0xffffff, this.visible);
	}

	public void colourNext() {
		this.colour = Utils.getNextColour();
	}

	public void colourPrev() {
		this.colour = Utils.getPrevColour();
	}

	public void draw(MapMode mapMode, MapView mapView, int borderColour)
    {
		for(Entry<UUID, PlayerLocation> entry : LocationManager.locations.entrySet()) {
			GroupData group = entry.getValue().getGroup();
			
			double xPos = entry.getValue().getX();
			double zPos = entry.getValue().getZ();
		
			if(Minecraft.getMinecraft().thePlayer.dimension != entry.getValue().getDimension()) {
				continue;
			}
			
			if(Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().equals(entry.getValue().getName())) {
				continue;
			}
			
	        double scale = mapView.getDimensionScaling(this.dimension);
	        Point.Double p = mapMode.getClampedScreenXY(mapView, xPos * scale, zPos * scale);
	        this.screenPos.setLocation(p.x + mapMode.xTranslation, p.y + mapMode.yTranslation);

	        
	        double mSize = mapMode.config.markerSize;
	        double halfMSize = mapMode.config.markerSize / 2.0;
	        Render.setColour(entry.getValue().getGroup().getColor());
	     
	        NetworkPlayerInfo info = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(entry.getKey());
	        
	    	if(info != null) {
				drawSkinHead(info.getLocationSkin(), (int)(p.x - halfMSize), (int)(p.y - halfMSize), 12, 1F);
			}
		}
		
        double scale = mapView.getDimensionScaling(this.dimension);
        Point.Double p = mapMode.getClampedScreenXY(mapView, this.x * scale, this.z * scale);
        this.screenPos.setLocation(p.x + mapMode.xTranslation, p.y + mapMode.yTranslation);

        // draw a coloured rectangle centered on the calculated (x, y)
        double mSize = mapMode.config.markerSize;
        double halfMSize = mapMode.config.markerSize / 2.0;
        Render.setColour(borderColour);
        Render.drawRect(p.x - halfMSize, p.y - halfMSize, mSize, mSize);
        Render.setColour(this.colour);
        Render.drawRect((p.x - halfMSize) + 0.5, (p.y - halfMSize) + 0.5, mSize - 1.0, mSize - 1.0);
    }

	private void drawSkinHead(ResourceLocation skin, int x, int y, int dimension, float opacity) {
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		Minecraft.getMinecraft().getTextureManager().bindTexture(skin);
		GlStateManager.color(1, 1, 1, opacity);
		float multiplier = dimension / 18F;
		GuiIngame.drawModalRectWithCustomSizedTexture(x, y, 19 * multiplier, 19 * multiplier, Math.round(18 * multiplier), Math.round(18 * multiplier), 150 * multiplier, 150 * multiplier);
		GuiIngame.drawModalRectWithCustomSizedTexture(x, y, (19 * 5 * multiplier) - multiplier, 19 * multiplier, Math.round(18 * multiplier), Math.round(18 * multiplier), 150 * multiplier, 150 * multiplier);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
	}

	// arraylist.contains was producing unexpected results in some situations
	// rather than figure out why i'll just control how two markers are compared
	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o instanceof Marker) {
			Marker m = (Marker) o;
			return (this.name == m.name) && (this.groupName == m.groupName)
					&& (this.x == m.x) && (this.y == m.y) && (this.z == m.z)
					&& (this.dimension == m.dimension);
		}
		return false;
	}

	public double getDistanceToMarker(Entity entityIn) {
		if(entityIn == null) return 0;
		double d0 = this.x - entityIn.posX;
		double d1 = this.y - entityIn.posY;
		double d2 = this.z - entityIn.posZ;
		return MathHelper.sqrt_double((d0 * d0) + (d1 * d1) + (d2 * d2));
	}

	public float getRed() {
		return (((colour >> 16) & 0xff) / 255.0f);
	}

	public float getGreen() {
		return (((colour >> 8) & 0xff) / 255.0f);
	}

	public float getBlue() {
		return (((colour) & 0xff) / 255.0f);
	}
}