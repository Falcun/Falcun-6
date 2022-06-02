package falcun.net.modules.hypixel.skyblock.utils;

import com.google.common.collect.Sets;
import falcun.net.Falcun;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Locale;
import java.util.regex.Pattern;

public final class SkyblockPlayer {

    public static boolean onHypixel = false;
    public static boolean onSkyblock = false;

    public SkyblockPlayer() {
        MinecraftForge.EVENT_BUS.register(this);
        System.out.println("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTREGISTERED");
    }

    @SubscribeEvent
    public void onClientTickEvent(TickEvent.ClientTickEvent event) {
        if (Falcun.minecraft.thePlayer == null || Falcun.minecraft.theWorld == null) return;
        if(System.currentTimeMillis() % 5000 == 0) {
            hypixelCheck();
            skyblockCheck();

            if (onHypixel) System.out.println("on GHHYPIXEL");
            if (onSkyblock) System.out.println("on skybllock!!!!");
            if (!onHypixel && !onSkyblock) System.out.println("not on anything :((((");
        }
    }

    void hypixelCheck() {
        Minecraft mc = Falcun.minecraft;
        if (mc != null || mc.thePlayer != null) {
            if (mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel")) {
                onHypixel = true;
                return;
            }
        }
        onHypixel = false;
    }

    void skyblockCheck() {
        if (onHypixel) {
            ScoreObjective scoreObjective = Falcun.minecraft.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1);
            if (scoreObjective != null) {
                String objective = Pattern.compile("(?i)§[0-9A-FK-OR]").matcher(scoreObjective.getDisplayName()).replaceAll("");
                for (String skyblock : Sets.newHashSet("SKYBLOCK")) {
                    if (objective.startsWith(skyblock)) {
                        onSkyblock = true;
                        return;
                    }
                }
            }
        }
        onSkyblock = false;
    }



    public enum Location {
        PRIVATE_ISLAND,
        HUB,
    }
}
