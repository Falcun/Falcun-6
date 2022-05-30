package falcun.net.modules.hypixel.skyblock.utils;

import com.google.common.collect.Sets;
import falcun.net.Falcun;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.common.MinecraftForge;

import java.util.regex.Pattern;

public final class EventListener {

    public static EventListener instance = new EventListener();
    private EventListener() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public boolean isOnHypixel() {
        Minecraft mc = Falcun.minecraft;
        if (mc == null || mc.thePlayer == null) return false;
        if (mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel")) {
            return true;
        }
        return false;
    }

    public boolean isOnSkyblock() {
        ScoreObjective scoreObjective = Falcun.minecraft.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1);
        System.out.println("1");
        if (scoreObjective == null) return false;
        System.out.println(2);
        String objective = Pattern.compile("(?i)§[0-9A-FK-OR]").matcher(scoreObjective.getDisplayName()).replaceAll("");
        System.out.println(3);
        for (String skyblock : Sets.newHashSet("SKYBLOCK")) {
            if (objective.startsWith(skyblock)) {
                return true;
            }
        }
        return false;
    }
}
