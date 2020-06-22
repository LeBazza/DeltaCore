package me.bazzadev.deltacore.listeners;

import me.bazzadev.deltacore.staffmode.StaffModeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import static me.bazzadev.deltacore.staffmode.StaffModeItems.viewPlayerList;

public class PlayerDropItemListener implements Listener {

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (!StaffModeManager.getStatus(player)) return;
        if (!event.getItemDrop().getItemStack().equals(viewPlayerList)) return;

        event.setCancelled(true);
    }

}