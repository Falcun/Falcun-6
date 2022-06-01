package net.mattbenson.modules.types.mods;

import java.awt.Color;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.ARBDepthClamp.GL_DEPTH_CLAMP;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.entity.PlayerTickEvent;
import net.mattbenson.events.types.input.KeyDownEvent;
import net.mattbenson.events.types.render.RenderEvent;
import net.mattbenson.events.types.render.RenderType;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.network.NetworkingClient;
import net.mattbenson.network.common.GroupData;
import net.mattbenson.network.network.packets.groups.GroupList;
import net.mattbenson.utils.DrawUtils;
import net.mattbenson.utils.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class GroupPingLocation extends Module {

    public static final int MAX_LIGHT_X = 0xF000F0;
    public static final int MAX_LIGHT_Y = 0xF000F0;

    public Timer timer = new Timer();

	@ConfigValue.Integer(name = "Display Timer (sec)", min = 1, max = 120)
	public int age = 15;
    
    @ConfigValue.Integer(name = "Scale", min = 1, max = 10)
    public int scale = 5;
    
    @ConfigValue.Keybind(name = "Sends coordinates to your group")
	public int keyBind = 0;
	
    @ConfigValue.Keybind(name = "Ping Location Key Horizontal")
	public int keyBind1 = 0;
	
    @ConfigValue.Boolean(name = "Ping Mouse Location")
	public boolean isMouseLocation = false;
	
	public GroupPingLocation() {
		super("Group Ping Location", ModuleCategory.GROUPS);
		setEnabled(true);
	}

	  private List<Ping> pings = new CopyOnWriteArrayList<>();

	    class Ping {
	        private final BlockPos location;
	        private final String playername1;
	        private final GroupData group;
	        private final long currentMS;
	        private final String direction;
	        private final String face;
	        
	        public Ping(String direction, String face, BlockPos location, GroupData group, String playername1) {
	            this.location = location;
	            this.group = group;
	            this.playername1 = playername1;
	            this.direction = direction;
	            this.face = face;
	            currentMS = System.currentTimeMillis();
	        }

	        public BlockPos getLocation() {
	            return location;
	        }

	        public String getname1() {
	            return playername1;
	        }
	        
	        public String getDirection() {
	            return direction;
	        }
	        
	        public String getFace() {
	        	return face;
	        }
	        
	        public GroupData getGroup() {
	        	return group;
	        }

	        public boolean hasReached(long ms) {
	            return System.currentTimeMillis() - currentMS >= ms;
	        }
	    }

	    public void addPing(String direction, String face, BlockPos pos, GroupData group, String name1) {
			if (pings.stream().anyMatch(x-> x.playername1.equals(name1) && x.direction.equalsIgnoreCase(direction))) return;
			
			Minecraft.getMinecraft().thePlayer.playSound("random.orb", 100, 1.0F);
	        pings.add(new Ping(direction, face, pos, group, name1));
	    }

	    public void updatePings() {
	        pings.removeIf(x-> x.hasReached(age * 1000L));
	    }

		@SubscribeEvent
		public void onWorldRenderLast(RenderEvent event) {
			if (event.getRenderType() != RenderType.WORLD) {
				return;
			}
	        if (mc.theWorld != null) {
	            float partial = event.partialTicks;
	            float px = (float) mc.thePlayer.posX;
	            float py = (float) mc.thePlayer.posY;
	            float pz = (float) mc.thePlayer.posZ;
	            float mx = (float) mc.thePlayer.prevPosX;
	            float my = (float) mc.thePlayer.prevPosY;
	            float mz = (float) mc.thePlayer.prevPosZ;
	            float dx = mx + (px - mx) * partial;
	            float dy = my + (py - my) * partial;
	            float dz = mz + (pz - mz) * partial;


	            pings.forEach(ping -> {
	                if (ping.getDirection().equals("0")) {
	            	Color beamColor = new Color(ping.getGroup().getColor(), true);
	                float red = beamColor.getRed() / 255.0f;
	                float green = beamColor.getGreen() / 255.0f;
	                float blue = beamColor.getBlue() / 255.0f;
	                float alpha = beamColor.getAlpha() / 255.0f;
	                if (beamColor.getBlue() == 5 && beamColor.getRed() == 5 && beamColor.getGreen() == 5) {
	                    float hue = System.currentTimeMillis() % 20000L / 20000.0f;
	                    int chroma = Color.HSBtoRGB(hue, 1.0f, 1.0f);
	                    alpha = (chroma >> 24 & 0xFF) / 255.0f;
	                    red = (chroma >> 16 & 0xFF) / 255.0f;
	                    green = (chroma >> 8 & 0xFF) / 255.0f;
	                    blue = (chroma & 0xFF) / 255.0f;
	                }


	                float[] colors = new float[]{red, green, blue, alpha};
	                
	            	GL11.glPushMatrix();

	                renderLaser(ping.getLocation().getX(), 1, ping.getLocation().getZ(), ping.getLocation().getX(), ping.getLocation().getY() + 255, ping.getLocation().getZ(), 120, 1F, 0.2F, colors);
	              
	                GL11.glPopMatrix();
	                GL11.glPushMatrix();
	                rendername1Tag(ping.getGroup(), ping.getname1() + " - " + (int) mc.thePlayer.getDistance(ping.getLocation().getX(), mc.thePlayer.posY, ping.getLocation().getZ()) + "m", ping.getLocation().getX() - dx, (mc.thePlayer.posY + 10) - dy, ping.getLocation().getZ() - dz);
	                GL11.glPopMatrix();
	                } else {
	                	if (ping.face.equals("north") || ping.face.equals("south")) {
	                    	Color beamColor = new Color(ping.getGroup().getColor(), true);
	                        float red = beamColor.getRed() / 255.0f;
	                        float green = beamColor.getGreen() / 255.0f;
	                        float blue = beamColor.getBlue() / 255.0f;
	                        float alpha = beamColor.getAlpha() / 255.0f;
	                        if (beamColor.getBlue() == 5 && beamColor.getRed() == 5 && beamColor.getGreen() == 5) {
	                            float hue = System.currentTimeMillis() % 20000L / 20000.0f;
	                            int chroma = Color.HSBtoRGB(hue, 1.0f, 1.0f);
	                            alpha = (chroma >> 24 & 0xFF) / 255.0f;
	                            red = (chroma >> 16 & 0xFF) / 255.0f;
	                            green = (chroma >> 8 & 0xFF) / 255.0f;
	                            blue = (chroma & 0xFF) / 255.0f;
	                        }


	                        float[] colors = new float[]{red, green, blue, alpha};
	                        
	                    	GL11.glPushMatrix();
	                        renderLaser(ping.getLocation().getX(), ping.getLocation().getY() + 1, ping.getLocation().getZ() - 10000, ping.getLocation().getX(), ping.getLocation().getY() + 1, ping.getLocation().getZ() + 10000, 120, 1F, 0.2F, colors);
	                      
	                        GL11.glPopMatrix();
	                        GL11.glPushMatrix();
	                        if (mc.thePlayer.getHorizontalFacing().toString().equals("north")) {
	                        	  rendername1Tag1(ping.getGroup(), ping.getname1(), ping.getLocation().getX() - dx, (ping.getLocation().getY()) - dy, mc.thePlayer.posZ - 20 - dz);
	                        } else if (mc.thePlayer.getHorizontalFacing().toString().equals("south")) {
	                        	  rendername1Tag1(ping.getGroup(), ping.getname1(), ping.getLocation().getX() - dx, (ping.getLocation().getY()) - dy, mc.thePlayer.posZ + 20 - dz);
	                        } else {
	                      	  rendername1Tag1(ping.getGroup(), ping.getname1(), ping.getLocation().getX() - dx, (ping.getLocation().getY()) - dy, ping.getLocation().getZ() - dz);

	                        }
	                      
	                        GL11.glPopMatrix();
	                	} else {
	                    	
	                    	Color beamColor = new Color(ping.getGroup().getColor(), true);
	                        float red = beamColor.getRed() / 255.0f;
	                        float green = beamColor.getGreen() / 255.0f;
	                        float blue = beamColor.getBlue() / 255.0f;
	                        float alpha = beamColor.getAlpha() / 255.0f;
	                        if (beamColor.getBlue() == 5 && beamColor.getRed() == 5 && beamColor.getGreen() == 5) {
	                            float hue = System.currentTimeMillis() % 20000L / 20000.0f;
	                            int chroma = Color.HSBtoRGB(hue, 1.0f, 1.0f);
	                            alpha = (chroma >> 24 & 0xFF) / 255.0f;
	                            red = (chroma >> 16 & 0xFF) / 255.0f;
	                            green = (chroma >> 8 & 0xFF) / 255.0f;
	                            blue = (chroma & 0xFF) / 255.0f;
	                        }


	                        float[] colors = new float[]{red, green, blue, alpha};
	                        
	                    	GL11.glPushMatrix();
	                        renderLaser(ping.getLocation().getX() - 10000, ping.getLocation().getY() + 1, ping.getLocation().getZ(), ping.getLocation().getX() + 10000, ping.getLocation().getY() + 1, ping.getLocation().getZ(), 120, 1F, 0.2F, colors);
	                      
	                        GL11.glPopMatrix();
	                        GL11.glPushMatrix();
	                        if (mc.thePlayer.getHorizontalFacing().toString().equals("east")) {
	                        	  rendername1Tag1(ping.getGroup(), ping.getname1(), mc.thePlayer.posX + 20 - dx, (ping.getLocation().getY()) - dy, ping.getLocation().getZ() - dz);
	                        } else if (mc.thePlayer.getHorizontalFacing().toString().equals("west")) {
	                        	  rendername1Tag1(ping.getGroup(), ping.getname1(), mc.thePlayer.posX - 20 - dx, (ping.getLocation().getY()) - dy, ping.getLocation().getZ() - dz);
	                        } else {
	                      	  rendername1Tag1(ping.getGroup(), ping.getname1(), ping.getLocation().getX() - dx, (ping.getLocation().getY()) - dy, ping.getLocation().getZ() - dz);

	                        }
	                      
	                        GL11.glPopMatrix();
	                		
	                		
	                		
	                		
	                		
	                		
	                	}
	                	

	                }
	            });
	           
	        }
	    }

	    @SubscribeEvent
	    public void onTick(PlayerTickEvent event) {
	    	
	        if (mc.theWorld != null) {
	            updatePings();
	        }
	    }
	    

	    
		@SubscribeEvent
		public void onKeyPress(KeyDownEvent event) {
			if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().thePlayer != null
					&& Minecraft.getMinecraft().theWorld != null) {
				
				
				int key = Keyboard.getEventKey();
				boolean isDown = Keyboard.getEventKeyState();
				if (keyBind != 0 && key == keyBind && isDown) {
					
					
					for (GroupData group : GroupList.getGroups()) {
						if (this.enabled) {
							if (isMouseLocation) {
								float blockReachDistance = 8192;
								MovingObjectPosition mouseOver = mc.thePlayer.rayTrace(blockReachDistance, 1.0F);
								
								if (mouseOver.typeOfHit == MovingObjectType.MISS || mouseOver.typeOfHit == MovingObjectType.BLOCK) {
									BlockPos pos = mouseOver.getBlockPos();
									
									int blockHitX = pos.getX();
									int blockHitY = pos.getY();
									int blockHitZ = pos.getZ();
									
									System.out.println("sent ping");
									
									NetworkingClient.sendLine("Ping", group.getId() + "", 0 + "", mc.thePlayer.getHorizontalFacing() + "",
											blockHitX + "", blockHitY + "", blockHitZ + "");
								}
							} else {
								NetworkingClient.sendLine("Ping", group.getId() + "", 0 + "", mc.thePlayer.getHorizontalFacing() + "",
										mc.thePlayer.posX + "", mc.thePlayer.posY + "", mc.thePlayer.posZ + "");
							}

						}
					}
				}
				
				
				if (keyBind1 != 0 && key == keyBind1 && isDown) {
					for (GroupData group : GroupList.getGroups()) {
						if (this.enabled) {
							
							if (pings.stream().anyMatch(x-> x.playername1.equals(name) && x.direction.equalsIgnoreCase("1"))) return;
							
								NetworkingClient.sendLine("Ping", group.getId() + "", 1 + "", mc.thePlayer.getHorizontalFacing() + "",
										mc.thePlayer.posX + "", mc.thePlayer.posY + "", mc.thePlayer.posZ + "");
							}

					}
				}
				
				
				
			}
		}

	    public static void renderLaser(double firstX, double firstY, double firstZ, double secondX, double secondY, double secondZ, double rotationTime, float alpha, double beamWidth, float[] color) {
	    	Tessellator tessy = Tessellator.getInstance();
	        WorldRenderer render = tessy.getWorldRenderer();
	        World world = Minecraft.getMinecraft().theWorld;

	        float r = color[0];
	        float g = color[1];
	        float b = color[2];

	        Vec3 vec1 = new Vec3(firstX, firstY, firstZ);
	        Vec3 vec2 = new Vec3(secondX, secondY, secondZ);
	        Vec3 combinedVec = vec2.subtract(vec1);

	        double rot = rotationTime > 0 ? 360D * (world.getTotalWorldTime() % rotationTime / rotationTime) : 0;
	        double pitch = Math.atan2(combinedVec.yCoord, Math.sqrt(combinedVec.xCoord * combinedVec.xCoord + combinedVec.zCoord * combinedVec.zCoord));
	        double yaw = Math.atan2(-combinedVec.zCoord, combinedVec.xCoord);

	        double length = combinedVec.lengthVector();

	        GlStateManager.pushMatrix();
	        GlStateManager.disableDepth();
	        GlStateManager.disableLighting();
	        GlStateManager.enableBlend();
	        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	        int func = GL11.glGetInteger(GL11.GL_ALPHA_TEST_FUNC);
	        float ref = GL11.glGetFloat(GL11.GL_ALPHA_TEST_REF);
	        GlStateManager.alphaFunc(GL11.GL_ALWAYS, 0);
	        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
	        GlStateManager.translate(firstX - TileEntityRendererDispatcher.staticPlayerX + 0.5D, firstY - TileEntityRendererDispatcher.staticPlayerY + 0.5D, firstZ - TileEntityRendererDispatcher.staticPlayerZ + 0.5D);
	        GlStateManager.rotate((float) (180 * yaw / Math.PI), 0, 1, 0);
	        GlStateManager.rotate((float) (180 * pitch / Math.PI), 0, 0, 1);
	        GlStateManager.rotate((float) rot, 1, 0, 0);

	        GL11.glEnable(GL_DEPTH_CLAMP);
	        GlStateManager.disableTexture2D();
	        render.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
	        for (double i = 0; i < 4; i++) {
	            double width = beamWidth * (i / 4.0);
	            render.pos(length, width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
	            render.pos(0, width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
	            render.pos(0, -width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
	            render.pos(length, -width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();

	            render.pos(length, -width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
	            render.pos(0, -width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
	            render.pos(0, width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
	            render.pos(length, width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();

	            render.pos(length, width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
	            render.pos(0, width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
	            render.pos(0, width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
	            render.pos(length, width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();

	            render.pos(length, -width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
	            render.pos(0, -width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
	            render.pos(0, -width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
	            render.pos(length, -width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
	        }
	        tessy.draw();
	        GL11.glDisable(GL_DEPTH_CLAMP);
	        GlStateManager.enableTexture2D();
	        //}

	        GlStateManager.alphaFunc(func, ref);
	        GlStateManager.blendFunc(GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE_MINUS_DST_ALPHA);
	        GlStateManager.disableBlend();
	        GlStateManager.enableLighting();
	        GlStateManager.enableDepth();
	        GlStateManager.popMatrix();
	    }

	    public void rendername1Tag(GroupData group, String str, double x, double y, double z) {
	    	Color iColor = new Color(group.getColor(), true);
	        float red = iColor.getRed() / 255.0f;
	        float green = iColor.getGreen() / 255.0f;
	        float blue = iColor.getBlue() / 255.0f;
	        float alpha = iColor.getAlpha() / 255.0f;

	        if (iColor.getBlue() == 5 && iColor.getRed() == 5 && iColor.getGreen() == 5) {
	            float hue = System.currentTimeMillis() % 20000L / 20000.0f;
	            int chroma = Color.HSBtoRGB(hue, 1.0f, 1.0f);
	            alpha = (chroma >> 24 & 0xFF) / 255.0f;
	            red = (chroma >> 16 & 0xFF) / 255.0f;
	            green = (chroma >> 8 & 0xFF) / 255.0f;
	            blue = (chroma & 0xFF) / 255.0f;
	        }
	        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
	        float dis = 2F;
	        
	        double ss = (((double) scale) / 10) * 7;
	        dis = (float) (dis * ss);
	        if (dis > 300) {
	            dis = 100;
	        }
	        GlStateManager.pushMatrix();
	        GlStateManager.translate((float) x + 0.0F, (float) y + 1, (float) z);
	        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
	        GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
	        GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
	        float scale = dis / 100;
	        GlStateManager.scale(-scale, -scale, scale);
	        GlStateManager.disableLighting();
	        GlStateManager.depthMask(false);
	        GlStateManager.disableDepth();
	        GlStateManager.enableBlend();
	        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	        GL11.glEnable(GL_DEPTH_CLAMP);
	        Tessellator tessellator = Tessellator.getInstance();
	        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
	        int i = 0;

	        int j = (int) (Fonts.ubuntuFont.getStringWidth(str) / 2);
	        GlStateManager.disableTexture2D();
	        DrawUtils.drawBorderedRect(-j - 6, y - 15, j + 6, i + (Fonts.ubuntuFont.getStringHeight(str)), 2F, new Color(0, 0, 0, 255).getRGB(), new Color(0, 0, 0, 100).getRGB());

	        GlStateManager.enableTexture2D();
	        Fonts.ubuntuFont.drawString(str, -Fonts.ubuntuFont.getStringWidth(str) / 2, i - 3, new Color(red, green, blue, alpha).getRGB());
	        GlStateManager.enableDepth();
	        GlStateManager.depthMask(true);
	        Fonts.ubuntuFont.drawString(str, -Fonts.ubuntuFont.getStringWidth(str) / 2, i - 3, new Color(red, green, blue, alpha).getRGB());

	        GL11.glDisable(GL_DEPTH_CLAMP);
	        GlStateManager.enableDepth();
	        GlStateManager.enableLighting();
	        GlStateManager.disableBlend();
	        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	        GlStateManager.popMatrix();
	    }
	    
	    
	    public void rendername1Tag1(GroupData group, String str, double x, double y, double z) {
	    	y = y + 2;
	    	Color iColor = new Color(group.getColor(), true);
	        float red = iColor.getRed() / 255.0f;
	        float green = iColor.getGreen() / 255.0f;
	        float blue = iColor.getBlue() / 255.0f;
	        float alpha = iColor.getAlpha() / 255.0f;

	        if (iColor.getBlue() == 5 && iColor.getRed() == 5 && iColor.getGreen() == 5) {
	            float hue = System.currentTimeMillis() % 20000L / 20000.0f;
	            int chroma = Color.HSBtoRGB(hue, 1.0f, 1.0f);
	            alpha = (chroma >> 24 & 0xFF) / 255.0f;
	            red = (chroma >> 16 & 0xFF) / 255.0f;
	            green = (chroma >> 8 & 0xFF) / 255.0f;
	            blue = (chroma & 0xFF) / 255.0f;
	        }
	        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
	        float dis = 2F;
	        
	        double ss = (((double) scale) / 10) * 7;
	        dis = (float) (dis * ss);
	        if (dis > 300) {
	            dis = 100;
	        }
	        GlStateManager.pushMatrix();
	        GlStateManager.translate((float) x + 0.0F, (float) y + 1, (float) z);
	        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
	        GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
	        GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
	        float scale = dis / 100;
	        GlStateManager.scale(-scale, -scale, scale);
	        GlStateManager.disableLighting();
	        GlStateManager.depthMask(false);
	        GlStateManager.disableDepth();
	        GlStateManager.enableBlend();
	        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	        GL11.glEnable(GL_DEPTH_CLAMP);
	        Tessellator tessellator = Tessellator.getInstance();
	        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
	        int i = 0;

	        int j = (int) (Fonts.ubuntuFont.getStringWidth(str) / 2);
	        GlStateManager.disableTexture2D();
	        DrawUtils.drawBorderedRect(-j - 6, y - 4, j + 6, i + (Fonts.ubuntuFont.getStringHeight(str)), 2F, new Color(0, 0, 0, 255).getRGB(), new Color(0, 0, 0, 100).getRGB());

	        GlStateManager.enableTexture2D();
	        Fonts.ubuntuFont.drawString(str, -Fonts.ubuntuFont.getStringWidth(str) / 2, i - 3, new Color(red, green, blue, alpha).getRGB());
	        GlStateManager.enableDepth();
	        GlStateManager.depthMask(true);
	        Fonts.ubuntuFont.drawString(str, -Fonts.ubuntuFont.getStringWidth(str) / 2, i - 3, new Color(red, green, blue, alpha).getRGB());

	        GL11.glDisable(GL_DEPTH_CLAMP);
	        GlStateManager.enableDepth();
	        GlStateManager.enableLighting();
	        GlStateManager.disableBlend();
	        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	        GlStateManager.popMatrix();
	    }
	
	
	
	
}
