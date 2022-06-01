package net.mattbenson.modules.types.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import io.netty.channel.ChannelPipeline;
import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.entity.AttackEntityEvent;
import net.mattbenson.events.types.world.OnTickEvent;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.utils.DrawUtils;
import net.mattbenson.utils.legacy.ComboPacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S19PacketEntityStatus;

public class Combo extends Module {
	@ConfigValue.Boolean(name = "Background")
	private boolean backGround = true;
	
	@ConfigValue.Color(name = "Color")
	private Color color = Color.WHITE;
	
	@ConfigValue.Color(name = "Background Color")
	private Color background = new Color(0, 0, 0, 150);
	
	@ConfigValue.Boolean(name = "Custom Font")
	private boolean customFont = false;
	
	@ConfigValue.Boolean(name = "Static Chroma")
	private boolean isUsingStaticChroma = false;
	
	@ConfigValue.Boolean(name = "Wave Chroma")
	private boolean isUsingWaveChroma = false;
	
	private long sentAttackTime;
	private long lastHitTime;
	private int lastAttackId;
	private int currentCombo;
	private int sentAttack = -1;
	
	private HUDElement hud;
	private int width = 56;
	private int height = 18;
	
	public Combo() {
		super("Combo", ModuleCategory.RENDER);
		
		hud = new HUDElement("combo", width, height) {
			@Override
			public void onRender() {
				render();
			}
		};
		
		hud.setX(1);
		hud.setY(127);
		
		addHUD(hud);
	}
	
	public void render() {
		if (this.mc.gameSettings.showDebugInfo) {
			return;
		}
		
		GL11.glPushMatrix();
		
		String string = "Combo " + currentCombo;
		int width = hud.getWidth();
		int height = hud.getHeight();
		
		if(backGround) {
			DrawUtils.drawGradientRect(hud.getX(), hud.getY(), hud.getX() + width, hud.getY() + height, background.getRGB(), background.getRGB());
		}
		
		float posY = hud.getY() + 2;
		float posX = hud.getX() + 9;
		
		if(customFont) {
			hud.setWidth((int)Fonts.RobotoHUD.getStringWidth(string) + 16);
			hud.setHeight((int)Fonts.RobotoHUD.getStringHeight(string) + 7);
			
			if(isUsingStaticChroma) {
				DrawUtils.drawCustomFontChromaString(Fonts.RobotoHUD, string, (int) (posX), (int) posY, true, true);
			} else if(isUsingWaveChroma) {
				DrawUtils.drawCustomFontChromaString(Fonts.RobotoHUD, string, (int) (posX), (int) posY, false, true);
			} else {
				Fonts.RobotoHUD.drawString(string,(int) (posX), (int)posY, color.getRGB());
			}
		} else {
			hud.setWidth(mc.fontRendererObj.getStringWidth(string) + 16);
			hud.setHeight(mc.fontRendererObj.FONT_HEIGHT + 9);
			

			if(isUsingStaticChroma) {
				DrawUtils.drawChromaString(string, posX, posY + 3, true ,true);
			} else if(isUsingWaveChroma) {
				DrawUtils.drawChromaString(string, posX, posY+ 3, false ,true);
			} else {
				mc.fontRendererObj.drawStringWithShadow(string, (float) (posX), (float) posY+ 3, color.getRGB());
			}
		}
		GL11.glColor3f(1, 1, 1);
		GL11.glPopMatrix();
	}
	
	@SubscribeEvent
	public void onClientTick(OnTickEvent event) {
		ChannelPipeline pipeline = this.mc.thePlayer.sendQueue.getNetworkManager().channel.pipeline();
		
		if (pipeline.get("combo_handler") == null && pipeline.get("packet_handler") != null) {
			try {
				pipeline.addBefore("packet_handler", "combo_handler", new ComboPacketHandler(this));
			} catch (IllegalArgumentException var6) {
			}
		}
		
		if (this.mc.currentScreen == null) {
			if (System.currentTimeMillis() - this.lastHitTime > 2000L) {
				this.currentCombo = 0;
			}
		}
	}

	@SubscribeEvent
	public void onAttack(AttackEntityEvent event) {
		if (!event.isCancelled()) {
			if (event.getEntity() == this.mc.thePlayer) {
				this.sentAttack = event.getTarget().getEntityId();
				this.sentAttackTime = System.currentTimeMillis();
			}
		}
	}

	public void onEntityStatusPacket(S19PacketEntityStatus packet) {
		if (packet.getOpCode() == 2) {
			Entity target = packet.getEntity(this.mc.theWorld);
			if (target != null) {
				if (this.sentAttack != -1 && target.getEntityId() == this.sentAttack) {
					this.sentAttack = -1;
					if (System.currentTimeMillis() - this.sentAttackTime > 2000L) {
						this.sentAttackTime = 0L;
						this.currentCombo = 0;
						return;
					}

					if (this.lastAttackId == target.getEntityId()) {
						++this.currentCombo;
					} else {
						this.currentCombo = 1;
					}

					this.lastHitTime = System.currentTimeMillis();
					this.lastAttackId = target.getEntityId();
				} else if (target.getEntityId() == this.mc.thePlayer.getEntityId()) {
					this.currentCombo = 0;
				}
			}
		}
	}
}
