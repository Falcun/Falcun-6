package net.mattbenson.modules.types.render;

import java.util.Collection;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import net.mattbenson.Falcun;
import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.render.RenderEvent;
import net.mattbenson.events.types.render.RenderType;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;

public class ScoreboardHUD extends Module {
	@ConfigValue.Boolean(name = "Show Numbers")
	private boolean showNumbers = true;
	
	@ConfigValue.Boolean(name = "Enable free move")
	private boolean freeMove = false;
	
    private int sidebarX;
    private int sidebarY;
    private int sidebarWidth;
    private int sidebarHeight;
    private int offsetX = 0;
    private int offsetY = 0;
    private float mscale = 1;
	
	private HUDElement hud;
	private int width = -1;
	private int height = -1;
	
	public ScoreboardHUD() {
		super("Scoreboard", ModuleCategory.RENDER);
		
		hud = new HUDElement("saturation", width, height) {
			@Override
			public void onRender() {
				render();
			}
		};
		
		addHUD(hud);
	}
	
	@SubscribeEvent
	public void onRender(RenderEvent event) {
		if(event.getRenderType() != RenderType.SCOREBOARD) {
			return;
		}
		
		event.setCancelled(true);
	}
	
	public void render() {
		ScaledResolution scaledresolution = new ScaledResolution(this.mc);
		
		Scoreboard scoreboard = this.mc.theWorld.getScoreboard();
		ScoreObjective scoreobjective = null;
		ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(this.mc.thePlayer.getCommandSenderName());

		if(scoreplayerteam != null) {
			int i1 = scoreplayerteam.getChatFormat().getColorIndex();

			if(i1 >= 0) {
				scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
			}
		}

		ScoreObjective scoreobjective1 = scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);

		if(scoreobjective1 != null) {
			renderScoreboard(scoreobjective1, scaledresolution);
		}
	}
	
	private void renderScoreboard(ScoreObjective p_180475_1_, ScaledResolution p_180475_2_) {
		Scoreboard scoreboard = p_180475_1_.getScoreboard();
		Collection<Score> collection = scoreboard.getSortedScores(p_180475_1_);
		List<Score> list = Lists.newArrayList(Iterables.filter(collection, new Predicate<Score>() {
			public boolean apply(Score p_apply_1_) {
				return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
			}
		}));

		if (list.size() > 15) {
			collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
		} else {
			collection = list;
		}

		int i = mc.fontRendererObj.getStringWidth(p_180475_1_.getDisplayName());

		for (Score score : collection) {
			ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
			String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName());
			
			if(showNumbers) {
				s += ": " + score.getScorePoints();
			} else {
				s += ":";
			}
			
			i = Math.max(i, mc.fontRendererObj.getStringWidth(s));
		}

		int i1 = collection.size() * mc.fontRendererObj.FONT_HEIGHT;
		int j1 = p_180475_2_.getScaledHeight() / 2 + i1 / 3;
		int k1 = 3;
		int l1 = j1;
		int j = 0;
		int yPos = 0;
		
		if(freeMove) {
			l1 = hud.getX() + 2;
			yPos += hud.getY();
		} else {
			l1 = p_180475_2_.getScaledWidth() - i;
			yPos = p_180475_2_.getScaledHeight() / 2 - i1 / 2;
			
			hud.setX(l1 - 2);
			hud.setY(yPos);
		}
		
		for (Score score1 : collection) {
			++j;
			ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
			String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
			String s2 = EnumChatFormatting.RED + "" + score1.getScorePoints();
			int k = yPos + (i1 + 1 - ((j - 1) * mc.fontRendererObj.FONT_HEIGHT));
			
			int l = hud.getX() + hud.getWidth();

			GuiScreen.drawRectangle(l1 - 2, k, l, k + mc.fontRendererObj.FONT_HEIGHT, 1342177280);
			
			mc.fontRendererObj.drawString(s1, l1, k, 553648);
			
			if(showNumbers) {
				mc.fontRendererObj.drawString(s2, l - mc.fontRendererObj.getStringWidth(s2), k, 553648);
			}
			
			if (j == collection.size()) {
				String s3 = p_180475_1_.getDisplayName();
				GuiScreen.drawRectangle(l1 - 2, k - mc.fontRendererObj.FONT_HEIGHT - 1, l, k - 1, 1610612736);
				GuiScreen.drawRectangle(l1 - 2, k - 1, l, k, 1342177280);
				mc.fontRendererObj.drawString(s3, l1 + i / 2 - mc.fontRendererObj.getStringWidth(s3) / 2, k - mc.fontRendererObj.FONT_HEIGHT, 553648);
			}
		}
		
		int height = (j + 1) * mc.fontRendererObj.FONT_HEIGHT;
		
		hud.setWidth(i);
		hud.setHeight(height + 1);
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
	}
}
