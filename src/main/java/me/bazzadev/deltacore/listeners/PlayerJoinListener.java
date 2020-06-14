package me.bazzadev.deltacore.listeners;
import com.mongodb.client.model.Filters;
import me.bazzadev.deltacore.utilities.PlayerDataManager;

import org.bson.Document;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static com.mongodb.client.model.Updates.set;

public class PlayerJoinListener implements Listener {

    private final PlayerDataManager playerDataManager;

    private final String joinPrefix = ChatColor.translateAlternateColorCodes('&', "&8[&a+&8] ");

    public PlayerJoinListener(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {

        Player player = event.getPlayer();
        String playerName = player.getName();
        String playerUUIDString = player.getUniqueId().toString();

        event.setJoinMessage(joinPrefix + ChatColor.GOLD + playerName + " has joined the server.");

        if ( playerDataManager.getDatabaseCollection().countDocuments(new Document("uuid", playerUUIDString)) == 0) {
            playerDataManager.initializePlayer(player);
        } else {
            Document filter = new Document("uuid", playerUUIDString);
            Document playerData = playerDataManager.getDatabaseCollection().find(filter).first();

            if ( !(playerData.getString("IGN").equalsIgnoreCase(playerName)) ) {
                playerDataManager.getDatabaseCollection().updateOne(
                        Filters.eq(filter),
                        set("IGN", playerName));
            }
        }

    }

}