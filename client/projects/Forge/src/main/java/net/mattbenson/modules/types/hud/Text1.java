package net.mattbenson.modules.types.hud;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.mattbenson.utils.HUDAssetUtils;
import net.mattbenson.Falcun;
import net.mattbenson.Wrapper;
import net.mattbenson.config.Config;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.falcun.ConfigChangeEvent;
import net.mattbenson.events.types.world.OnTickEvent;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.hud.HUD;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.minecraft.util.ResourceLocation;

public class Text1 extends Module {

  public static HUDElement hud;

  public Text1() {
    super("Text1", ModuleCategory.RENDER);

    hud = new HUDElement("Text1", 58, 18) {
      @Override
      public void onRender() {
        render();
      }
    };

    addHUD(hud);
  }

  @Override
  public void onEnable() {}

  @Override
  public void onDisable() {}

  @SubscribeEvent
  public void onTick(OnTickEvent event) {

  }

  public static void imageRun() {

    int size = 0;
    for (HUD hud: Falcun.getInstance().newHudManager.getComponents()) {
      if (hud.getName().equals("text")) {
        size++;
      }
    }

    if (size > 0) {

      for (HUD hud1: Falcun.getInstance().newHudManager.getComponents()) {
        if (hud1.getImage().contains("text1")) {
          hud1.setposX(hud.getX());
          hud1.setposY(hud.getY());
          hud1.setWidth(hud.getWidth());
          hud1.setHeight(hud.getHeight());
        }
      }

    }
  }

  public void render() {

    if (this.mc.gameSettings.showDebugInfo) {
      return;
    }

    GL11.glPushMatrix();


      String text = null;
      int height = 0;
      int width = 0;
      Color color = null;
      int opacity = 0;

      for (HUD hud1: Falcun.getInstance().newHudManager.getComponents()) {
        if (hud1.getImage().contains("text1")) {
          text = hud1.getFile();
          height = hud1.getPosY();
          width = hud1.getPosX();
          color = new Color(hud1.getr(), hud1.getg(), hud1.getb());
          opacity = hud1.getOpacity();
        }
      }

      if (opacity == 1) {
        Fonts.KardinalBSmall.drawString(text, width, height, color.getRGB());
      } else if (opacity == 2) {
        Fonts.KardinalBMed.drawString(text, width, height, color.getRGB());
      } else if (opacity == 3) {
        Fonts.KardinalBLarge.drawString(text, width, height, color.getRGB());
      }

   

    GL11.glPopMatrix();
  }

}