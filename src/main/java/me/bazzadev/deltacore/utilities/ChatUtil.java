package me.bazzadev.deltacore.utilities;

import me.bazzadev.deltacore.DeltaCore;
import me.bazzadev.deltacore.afk.AFKManager;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ChatUtil {

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendEmptyLines(int lines, Player player) {

        for (int i=0; i <= lines; i++) {
            player.sendMessage(" ");
        }

    }

    public static String playerNameWithPrefix(Player player) {
        return DeltaCore.getChat().getPlayerPrefix(player) + player.getName();
    }

    public static String coloredAFKStatus(Player player) {
        if (AFKManager.getStatus(player)) {
            return ChatUtil.color("&a&l✔");
        }
        return ChatUtil.color("&c&l✘");
    }

    public static String coloredWorld(Player player) {
        World.Environment env = player.getWorld().getEnvironment();
        if (env==World.Environment.NORMAL) {
            return ChatUtil.color("&2Overworld");
        } else if (env==World.Environment.NETHER) {
            return ChatUtil.color("&4Nether");
        } else {
            return ChatUtil.color("&5The End");
        }
    }
}