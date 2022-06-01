package mapwriter.fac;

import java.awt.Point;
import java.awt.geom.Point2D.Double;
import java.util.LinkedList;
import java.util.List;

import mapwriter.Mw;
import mapwriter.api.IMwChunkOverlay;
import mapwriter.map.MapView;
import mapwriter.map.mapmode.MapMode;
import mapwriter.util.Render;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class Faction {
	private List claims = new LinkedList();
	private int mColor;
	private int bColor;
	private String colorHex;
	private String factionName;
	private boolean customColor;
	private ResourceLocation texture;
	
	Faction(String name, String colorHex) {
		this(name, colorHex, false);
	}
	
	Faction(String name, String colorHex, boolean customColor) {
		this(name, colorHex, customColor, "");
	}
	
	public Faction(String name, String colorHex, boolean customColor, String textureLocation) {
		this.factionName = name;
		this.colorHex = colorHex;
		this.setColors(colorHex);
		this.customColor = customColor;
		this.texture = textureLocation.isEmpty() ? null : new ResourceLocation(textureLocation);
	}
	
	public String getColor() {
		return this.colorHex;
	}
	
	public String getName() {
		return this.factionName;
	}
	
	public void setName(String name) {
		this.factionName = name;
	}
	
	public int claimAmount() {
		return this.claims.size();
	}
	
	public boolean hasCustomColor() {
		return this.customColor;
	}
	
	public boolean usingTexture() {
		return this.texture != null;
	}
	
	public void setTexture(String loc) {
		this.texture = loc.isEmpty() ? null : new ResourceLocation(loc);
	}
	
	public ResourceLocation getTexture() {
		return this.texture;
	}
	
	public void addClaim(int x, int z) {
		//Check if it already exists
		for (Claim claim : this.getClaims()) {
			if (claim.getX() == x && claim.getZ() == z) return;
		}
		
		//Add to list if it doesn't exist.
		this.claims.add(new Claim(x, z));
		Mw.getInstance().facInput.setClaim(this.getName(), x, z);
	}
	
	public void remClaim(int x, int z) {
		Mw.getInstance().facInput.setClaim(null, x, z);
		for(Claim claim : this.getClaims()) {
			if (claim.getX() == x && claim.getZ() == z) {
				this.getClaims().remove(claim);
				break;
			}
		}
	}
	
	public void updateColor(String newColor, String fillColor, String bordColor) {
		this.colorHex = newColor;
		this.setColors(newColor);
	}
	
	public void updateColor(String newColor) {
		this.updateColor(newColor, "33", "CC");
	}
	
	public List<IMwChunkOverlay> getOverlays() {
		return (List<IMwChunkOverlay>)this.claims;
	}
	
	public List<Claim> getClaims() {
		return (List<Claim>)this.claims;
	}
	
	public Claim getClaim(int x, int z) {
		for (Claim claim : this.getClaims()) {
			if (claim.getX() == x && claim.getZ() == z) return claim;
		}
		return null;
	}
	
	public void setColors(String color) {
		this.setColors(color, "33", "CC");
	}
	
	public void setColors(String color, String fillColor, String bordColor) {
		this.mColor = (int)Long.parseLong(fillColor + color, 16);
		this.bColor = (int)Long.parseLong(bordColor + color, 16);
	}
	
	public static Faction createCopy(Faction oldFaction) {
		Faction newFaction = new Faction(oldFaction.factionName, oldFaction.colorHex, oldFaction.customColor);
		
		if(oldFaction.getTexture() != null) newFaction.setTexture(oldFaction.getTexture().toString());
		for(Claim claim : oldFaction.getClaims())
			newFaction.addClaim(claim.getX(), claim.getZ());
		return newFaction;
	}
	
	public class Claim implements IMwChunkOverlay {
		private final Point coord;
		private Boolean[] sidesConnected;
		private boolean update;
		
		public Claim(int x, int z) {
			this.coord = new Point(x, z);
			this.update();
		}
		
		//TODO: Fix this for v2.0
		public void update() {
			//if(!this.shouldUpdate()) return;
			
			this.setUpdate(false);
			
			this.sidesConnected = new Boolean[] {
				this.isOwnClaimNextTo(0, -1),// North
				this.isOwnClaimNextTo(1, 0), // East
				this.isOwnClaimNextTo(0, 1), // South
				this.isOwnClaimNextTo(-1, 0) // West
			};
		}
		
		protected boolean isOwnClaimNextTo(int xOff, int zOff) {
			if (Faction.this.getClaim(this.coord.x + xOff, this.coord.y + zOff) == null) {
				//TODO: Update other faction claim if needs update.
				return false;
			}
			return true;
		}
		
		public int getX() { return this.coord.x; }
		public int getZ() { return this.coord.y; }
		public boolean shouldUpdate() { return this.update; }
		public void setUpdate(boolean update) { this.update = update; }
		
		@Override public Point getCoordinates() { return this.coord; }
		@Override public int getColor() { return Faction.this.mColor; }
		@Override public float getFilling() { return 1.0f; }
		@Override public boolean hasBorder() { return true; }
		@Override public float getBorderWidth() { return 0.5f; }
		@Override public int getBorderColor() { return Faction.this.bColor; }
		@Override public boolean customBorder() { return true; }
		@Override public boolean custom() { return Faction.this.usingTexture(); }
		
		@Override
		public void drawCustomBorder(Point.Double topCorner, Point.Double botCorner, MapView view, MapMode mode, IMwChunkOverlay chunk) {
			float bw = this.getBorderWidth();
			double x = topCorner.x, y = topCorner.y;
			double w = botCorner.x - topCorner.x - bw, h = botCorner.y - topCorner.y - bw;
			
			Render.setColour(this.getBorderColor());
			
			// N | S | W | E
			if (!this.sidesConnected[0]) Render.drawRect(x, y, w + bw, bw);
			if (!this.sidesConnected[1]) Render.drawRect(x + w, y, bw, h + bw);
			if (!this.sidesConnected[2]) Render.drawRect(x, y + h, w + bw, bw);
			if (!this.sidesConnected[3]) Render.drawRect(x, y, bw, h + bw);
		}

		@Override
		public void drawCustom(Double topCorner, Double botCorner, MapView view, MapMode mode, IMwChunkOverlay chunk) {
			double sizeX 	=  (botCorner.x - topCorner.x) * this.getFilling();
			double sizeY 	=  (botCorner.y - topCorner.y) * this.getFilling();
			double offsetX 	= ((botCorner.x - topCorner.x) - sizeX) / 2;
			double offsetY 	= ((botCorner.y - topCorner.y) - sizeY) / 2;
			
			Render.setColour(0xffffffff);
			Minecraft.getMinecraft().renderEngine.bindTexture(Faction.this.getTexture());
			Render.drawTexturedRect(topCorner.x + offsetX, topCorner.y + offsetY, sizeX, sizeY);
		}

		@Override
		public void drawCustomPost(Double topCorner, Double botCorner, MapView view, MapMode mode, IMwChunkOverlay chunk) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void drawCustomBorderPost(Double topCorner, Double botCorner, MapView view, MapMode mode, IMwChunkOverlay chunk) {
			// TODO Auto-generated method stub
			
		}
	}
}