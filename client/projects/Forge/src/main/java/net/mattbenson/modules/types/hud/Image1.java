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
import net.mattbenson.gui.hud.HUDElement;
import net.mattbenson.hud.HUD;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.minecraft.util.ResourceLocation;

public class Image1 extends Module {

  public static HUDElement hud;
  public static ResourceLocation location;
  public static boolean run;

  public Image1() {
    super("Image1", ModuleCategory.RENDER);

    hud = new HUDElement("Image1", 58, 18) {
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
      if (hud.getName().equals("file")) {
        size++;
      }
    }

    if (size > 0) {

      for (HUD hud1: Falcun.getInstance().newHudManager.getComponents()) {
        if (hud1.getImage().contains("image1")) {
          hud1.setposX(hud.getX());
          hud1.setposY(hud.getY());
          hud1.setWidth(hud.getWidth());
          hud1.setHeight(hud.getHeight());
          run = true;
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
      if (hud1.getImage().contains("image1")) {

        text = hud1.getFile();
        height = hud1.getPosY();
        width = hud1.getPosX();
        color = new Color(hud1.getr(), hud1.getg(), hud1.getb());
        opacity = hud1.getOpacity();
        run = true;

      }
    }

    Wrapper.getInstance().drawImage(location, hud.getX(), hud.getY(), hud.getWidth(), hud.getHeight(), color, opacity);

    GL11.glPopMatrix();
  }

}