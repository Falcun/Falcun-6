package net.mattbenson.modules.types.other;

import java.awt.Color;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.opengl.GL11;

import net.mattbenson.Falcun;
import net.mattbenson.Wrapper;
import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.network.ChatMessageEvent;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.mattbenson.utils.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class Chat extends Module {
  @ConfigValue.Boolean(name = "Infinite Chat")
  public boolean infiniteChat = true;

  @ConfigValue.Boolean(name = "Compact Chat")
  private boolean compactChat = true;

  @ConfigValue.Boolean(name = "Custom Chat")
  private boolean fontChat = true;

  @ConfigValue.Boolean(name = "Chat Timestamps")
  private boolean chatChatTimestamps = true;

  @ConfigValue.Boolean(name = "Mention Ping")
  private boolean chatPingname = false;

  @ConfigValue.Boolean(name = "Ping PMs")
  private boolean chatPingPms = false;

  @ConfigValue.List(name = "Mention Format", values = {
    ":",
    "�"
  })
  private String chatmsgFormat = ":";

  @ConfigValue.Boolean(name = "Ping Cooldown")
  private boolean chatPingCooldown = false;

  @ConfigValue.List(name = "Time Stamps Format", values = {
    "hh:mm",
    "dd:mm:hh:mm",
    "hh:mm:ss"
  })
  private String chatFormat = "dd:mm:hh:mm";

  private String lastMessage = "";
  private int line;
  private int amount;

  private Timer timer = new Timer();

  public Chat() {
    super("Chat", ModuleCategory.OTHER);
  }

  @SubscribeEvent
  public void chat(ChatMessageEvent event) {
    if (!this.enabled) return;

    try {
      String[] split = event.message.getUnformattedText().split(chatmsgFormat, 0);

      if (chatPingname) {

        if (split[1].contains(Minecraft.getMinecraft().thePlayer.getDisplayNameString())) {

          if (chatPingCooldown) {
            if (timer.hasReached(20000)) {
              Toolkit.getDefaultToolkit().beep();
              timer.reset();
            }
          } else {
            Toolkit.getDefaultToolkit().beep();
          }
        }
      }
    } catch (Exception e) {

    }

    try {

      if (chatPingPms) {

        if (containsOnce(event.message.getUnformattedText(), "->")) {
          if (chatPingCooldown) {
            if (timer.hasReached(20000)) {
              Toolkit.getDefaultToolkit().beep();
              timer.reset();
            }
          } else {
            Toolkit.getDefaultToolkit().beep();
          }
        }

      }
    } catch (Exception e) {

    }

    if (event.message != null) {
      if (event.message.getSiblings().size() == 0) return;
      String timestamp = null;
      if (chatChatTimestamps) {

        if (chatFormat.equals("hh:mm")) {
          timestamp = new SimpleDateFormat("HH:mm a").format(new Date());
        }

        if (chatFormat.equals("dd:mm:hh:mm")) {
          timestamp = new SimpleDateFormat("dd/MM HH:mm a").format(new Date());
        }

        if (chatFormat.equals("hh:mm:ss")) {
          timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        }

        final IChatComponent msg =
          new ChatComponentText(EnumChatFormatting.GRAY + "[")
          .appendText(EnumChatFormatting.GRAY + timestamp)
          .appendText(EnumChatFormatting.GRAY + "] ")
          .appendSibling(event.message);

        if (event.message != null) {
          GuiNewChat guiNewChat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
          guiNewChat.printChatMessageWithOptionalDeletion(msg, this.line);
          ++this.line;
        }
        event.setCancelled(true);
      }

    }

    if (!this.compactChat) return;
    if (!event.isCancelled() && event.message != null) {

      if (event.message.getSiblings().size() == 0) return;
      GuiNewChat guiNewChat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
      if (this.lastMessage.equals(event.message.getUnformattedText())) {
        guiNewChat.deleteChatLine(this.line);
        ++this.amount;
        this.lastMessage = event.message.getUnformattedText();
        event.message.appendText(EnumChatFormatting.GRAY + " (" + this.amount + ")");
      } else {
        this.amount = 1;
        this.lastMessage = event.message.getUnformattedText();
      }

      ++this.line;
      if (!event.isCancelled()) {

        guiNewChat.printChatMessageWithOptionalDeletion(event.message, this.line);
      }

      if (this.line > 256) {
        this.line = 0;
      }

      event.setCancelled(true);
    }
  }

  public static boolean containsOnce(final String s, final CharSequence substring) {
    Pattern pattern = Pattern.compile(substring.toString());
    Matcher matcher = pattern.matcher(s);
    if (matcher.find()) {
      return !matcher.find();
    }
    return false;
  }

  public void handleChatFont(String message, int i2, int j2, int l, int l1) {
    if (this.enabled) {
      if (!Wrapper.getInstance().isChatBackground())
        Minecraft.getMinecraft().ingameGUI.getChatGUI().drawRectangle(i2, j2 - 9, i2 + l + 4, j2, (int)(l1 / 2 << 24));
      if (this.fontChat) {
        GlStateManager.enableBlend();
        Fonts.KardinalB.drawString(message, (float) i2, (float)(j2 - 8), 16777215 + (l1 << 24));
        GlStateManager.disableBlend();
      } else {
        GlStateManager.enableBlend();
        mc.fontRendererObj.drawStringWithShadow(message, (float) i2, (float)(j2 - 8), 16777215 + (l1 << 24));
        //GlStateManager.disableBlend();
      }
    } else {
      if (!Wrapper.getInstance().isChatBackground())
        Minecraft.getMinecraft().ingameGUI.getChatGUI().drawRectangle(i2, j2 - 9, i2 + l + 4, j2, (int)(l1 / 2 << 24));
      GlStateManager.enableBlend();
      mc.fontRendererObj.drawStringWithShadow(message, (float) i2, (float)(j2 - 8), 16777215 + (l1 << 24));
      //GlStateManager.disableBlend();
    }
  }
}