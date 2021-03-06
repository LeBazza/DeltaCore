package me.bazzadev.deltacore.commands;

import me.bazzadev.deltacore.managers.PlayerDataManager;
import me.bazzadev.deltacore.managers.PlayerInventoryManager;
import me.bazzadev.deltacore.utilities.ChatUtil;
import me.bazzadev.deltacore.utilities.Vars;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InventoryCMD implements CommandExecutor {

    private final PlayerInventoryManager playerInventoryManager;
    private final PlayerDataManager playerDataManager;

    public InventoryCMD(PlayerInventoryManager playerInventoryManager, PlayerDataManager playerDataManager) {
        this.playerInventoryManager = playerInventoryManager;
        this.playerDataManager = playerDataManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (command.getName().equalsIgnoreCase("saveinv")) {

            if (sender instanceof Player) {

                Player player = (Player) sender;

                if (player.hasPermission("deltacore.saveinv")) {
                    playerInventoryManager.saveContents(player, playerDataManager.getTestInvMap());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Vars.PLUGIN_PREFIX + "&7Your inventory has been &bsaved&7."));

                } else {
                    player.sendMessage(Vars.NO_PERMISSION);
                }

                return true;

            }

        } else if (command.getName().equalsIgnoreCase("loadinv")) {

            if (sender instanceof Player) {

                Player player = (Player) sender;

                if (player.hasPermission("deltacore.loadinv")) {
                    playerInventoryManager.loadContents(player, playerDataManager.getTestInvMap());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Vars.PLUGIN_PREFIX + "&7Your inventory has been &arestored&7."));

                } else {
                    player.sendMessage(Vars.NO_PERMISSION);
                }

                return true;

            }


        } else if (command.getName().equalsIgnoreCase("restoreinv")) {

            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.hasPermission("deltacore.restoreinv.fromdeath")) {
                    player.sendMessage(Vars.NO_PERMISSION);
                    return true;
                }
            }

            if (args.length==2 && args[0].equalsIgnoreCase("fromdeath")) {
                restoreDeathInventory(args[1]);
                return true;
            }

        } else if (command.getName().equalsIgnoreCase("clearinv")) {

            if (sender instanceof Player) {

                Player player = (Player) sender;

                if (player.hasPermission("deltacore.clearinv")) {
                    player.getInventory().clear();
                    player.sendMessage(ChatUtil.color(Vars.PLUGIN_PREFIX + "&7Your inventory has been &fcleared&7."));

                } else {
                    player.sendMessage(Vars.NO_PERMISSION);
                }

                return true;

            }

        }

        return false;

    }

    private void restoreDeathInventory(String targetName) {

        Player target = Bukkit.getPlayer(targetName);
        playerInventoryManager.loadContents(target, playerDataManager.getLastDeathInvMap());

        target.sendMessage(ChatUtil.color(Vars.PLUGIN_PREFIX + "&7Your inventory has been &a&lRESTORED &7to its pre-death contents."));
    }

}
