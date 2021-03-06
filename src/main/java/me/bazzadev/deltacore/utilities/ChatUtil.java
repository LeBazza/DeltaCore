package me.bazzadev.deltacore.utilities;

import me.bazzadev.deltacore.DeltaCore;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ChatUtil {

    private final PlayerUtil playerUtil;

    public ChatUtil(PlayerUtil playerUtil) {
        this.playerUtil = playerUtil;
    }


    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendEmptyLines(int lines, Player player) {

        for (int i=0; i <= lines; i++) {
            player.sendMessage(" ");
        }

    }

    public static String getPrefix(Player player) {
        return DeltaCore.getChat().getPlayerPrefix(player);
    }

    public static String playerNameWithPrefix(Player player) {
        return getPrefix(player) + player.getName();
    }

    public String coloredAFKStatus(Player player) {
        if (playerUtil.isAFK(player)) {
            return ChatUtil.color("&a&l✔");
        }
        return ChatUtil.color("&c&l✘");
    }

    public String coloredStaffModeStatus(Player player) {
        if (playerUtil.isStaffMode(player)) {
            return ChatUtil.color("&a&l✔");
        }
        return ChatUtil.color("&c&l✘");
    }

    public String coloredVanishStatus(Player player) {
        if (playerUtil.isVanished(player)) {
            return ChatUtil.color("&a&l✔");
        }
        return ChatUtil.color("&c&l✘");
    }

    public static String coloredWorld(Player player) {
        World.Environment env = player.getWorld().getEnvironment();
        return getColoredWorld(env);

    }

    public static String getColoredWorld(World.Environment environment) {
        if (environment==World.Environment.NORMAL) {
            return ChatUtil.color("&2The Overworld");
        } else if (environment==World.Environment.NETHER) {
            return ChatUtil.color("&4The Nether");
        } else {
            return ChatUtil.color("&5The End");
        }


    }

}
