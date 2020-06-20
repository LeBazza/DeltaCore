package me.bazzadev.deltacore.inventory;

import com.mongodb.client.model.Filters;
import me.bazzadev.deltacore.utilities.PlayerDataManager;
import me.bazzadev.deltacore.utilities.Vars;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.IOException;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static me.bazzadev.deltacore.utilities.InventoryUtil.itemStackArrayFromBase64;
import static me.bazzadev.deltacore.utilities.InventoryUtil.playerInventoryToBase64;

public class PlayerInventoryManager {

    private final PlayerDataManager playerDataManager;

    public PlayerInventoryManager(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }


    public void saveContents(Player player) {

        PlayerInventory playerInv = player.getInventory();
        String playerUUIDString = player.getUniqueId().toString();

        String[] playerInventoryToBase64 = playerInventoryToBase64(playerInv);

        String pathToInv = "inventory.inv";
        String pathToArmor = "inventory.armor";

        playerDataManager.getDatabaseCollection().updateOne(
                Filters.eq("uuid", playerUUIDString),
                combine(set(pathToInv, playerInventoryToBase64[0]),
                        set(pathToArmor, playerInventoryToBase64[1])));

    }

    public void loadContents(Player player) {

        String playerUUIDString = player.getUniqueId().toString();

        Document filter = new Document("uuid", playerUUIDString);
        Document playerData = playerDataManager.getDatabaseCollection().find(filter).first();
        Document inventory = (Document) playerData.get("inventory");

        String invBase64 = inventory.getString("inv");
        String armorBase64 = inventory.getString("armor");

        try {

            ItemStack[] inv = itemStackArrayFromBase64(invBase64);
            ItemStack[] armor = itemStackArrayFromBase64(armorBase64);

            player.getInventory().setContents(inv);
            player.getInventory().setArmorContents(armor);

        } catch (IOException e) {
            player.sendMessage(Vars.PLUGIN_PREFIX + "An error occurred.");
        }

        }

}
