package me.bazzadev.deltacore.utilities;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.bazzadev.deltacore.DeltaCore;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;


/**
 * A library for the Bukkit API to create player skulls
 * from names, base64 strings, and texture URLs.
 *
 * Does not use any NMS code, and should work across all versions.
 *
 * @author Dean B on 12/28/2016.
 */
public class SkullCreator {

    public final ChatUtil chatUtil;

    public SkullCreator(ChatUtil chatUtil) {
        this.chatUtil = chatUtil;
    }

    public ItemStack getHeadWithPlayerData(Player player) {
        return new ItemBuilder(SkullCreator.getPlayerHead(player))
                .setName(ChatUtil.color(DeltaCore.getChat().getPlayerPrefix(player) + player.getName()))
                .addLoreLine(ChatUtil.color("&7UUID: &8" + player.getUniqueId().toString()))
                .addLoreLine(ChatUtil.color("&7Rank: " + DeltaCore.getChat().getPrimaryGroup(player)))
                .addLoreLine("")
                .addLoreLine(ChatUtil.color("&7AFK: " + chatUtil.coloredAFKStatus(player)))
                .addLoreLine(ChatUtil.color("&7Staff Mode: " + chatUtil.coloredStaffModeStatus(player)))
                .addLoreLine(ChatUtil.color("&7Vanished: " + chatUtil.coloredVanishStatus(player)))
                .addLoreLine("")
                .addLoreLine(ChatUtil.color("&7Currently in: " + ChatUtil.coloredWorld(player)))
                .removeNBTTag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack();
    }

    // Constants
    public static final ItemStack CRYSTAL_BALL = itemFromUrl("https://textures.minecraft.net/texture/2cf0ccf5a5f97097262e773bbf65c4d7d9a39938015bf00de4918620f034f9b0");


    public static ItemStack getPlayerHead(Player player) {
        final OfflinePlayer p = Bukkit.getOfflinePlayer(player.getUniqueId());
        final ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        final SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(p);
        head.setItemMeta(meta);
        return head;
    }

    public static ItemStack getPlayerHead(UUID uuid) {
        final OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
        final ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        final SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(p);
        head.setItemMeta(meta);
        return head;
    }

    public static ItemStack getSkull(String username) {

        String url = "http://skins.minecraft.net/MinecraftSkins/" + username + ".png";

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        profileField.setAccessible(true);
        try {
            profileField.set(skullMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        skull.setItemMeta(skullMeta);
        return skull;
    }


    public static ItemStack getCustomHeadfromURL(String url) {

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (url.isEmpty()) return head;

        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());

        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));

        try {
            Method mtd = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            mtd.setAccessible(true);
            mtd.invoke(skullMeta, profile);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }

        head.setItemMeta(skullMeta);
        return head;
    }







    /**
     * Creates a player skull based on a player's name.
     *
     * @param name The Player's name
     * @return The head of the Player
     *
     * @deprecated names don't make for good identifiers
     */
    @Deprecated
    public static ItemStack itemFromName(String name) {
        ItemStack item = getPlayerSkullItem();

        return itemWithName(item, name);
    }

    /**
     * Creates a player skull based on a player's name.
     *
     * @param item The item to apply the name to
     * @param name The Player's name
     * @return The head of the Player
     *
     * @deprecated names don't make for good identifiers
     */
    @Deprecated
    public static ItemStack itemWithName(ItemStack item, String name) {
        notNull(item, "item");
        notNull(name, "name");

        return Bukkit.getUnsafe().modifyItemStack(item,
                "{SkullOwner:\"" + name + "\"}"
        );
    }

    /**
     * Creates a player skull with a UUID. 1.13 only.
     *
     * @param id The Player's UUID
     * @return The head of the Player
     */
    public static ItemStack itemFromUuid(UUID id) {
        ItemStack item = getPlayerSkullItem();

        return itemWithUuid(item, id);
    }

    /**
     * Creates a player skull based on a UUID. 1.13 only.
     *
     * @param item The item to apply the name to
     * @param id The Player's UUID
     * @return The head of the Player
     */
    public static ItemStack itemWithUuid(ItemStack item, UUID id) {
        notNull(item, "item");
        notNull(id, "id");

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(id));
        item.setItemMeta(meta);

        return item;
    }

    /**
     * Creates a player skull based on a Mojang server URL.
     *
     * @param url The URL of the Mojang skin
     * @return The head associated with the URL
     */
    public static ItemStack itemFromUrl(String url) {
        ItemStack item = getPlayerSkullItem();

        return itemWithUrl(item, url);
    }


    /**
     * Creates a player skull based on a Mojang server URL.
     *
     * @param item The item to apply the skin to
     * @param url The URL of the Mojang skin
     * @return The head associated with the URL
     */
    public static ItemStack itemWithUrl(ItemStack item, String url) {
        notNull(item, "item");
        notNull(url, "url");

        return itemWithBase64(item, urlToBase64(url));
    }

    /**
     * Creates a player skull based on a base64 string containing the link to the skin.
     *
     * @param base64 The base64 string containing the texture
     * @return The head with a custom texture
     */
    public static ItemStack itemFromBase64(String base64) {
        ItemStack item = getPlayerSkullItem();
        return itemWithBase64(item, base64);
    }

    /**
     * Applies the base64 string to the ItemStack.
     *
     * @param item The ItemStack to put the base64 onto
     * @param base64 The base64 string containing the texture
     * @return The head with a custom texture
     */
    public static ItemStack itemWithBase64(ItemStack item, String base64) {
        notNull(item, "item");
        notNull(base64, "base64");

        UUID hashAsId = new UUID(base64.hashCode(), base64.hashCode());
        return Bukkit.getUnsafe().modifyItemStack(item,
                "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + base64 + "\"}]}}}"
        );
    }

    /**
     * Sets the block to a skull with the given name.
     *
     * @param block The block to set
     * @param name The player to set it to
     *
     * @deprecated names don't make for good identifiers
     */
    @Deprecated
    public static void blockWithName(Block block, String name) {
        notNull(block, "block");
        notNull(name, "name");

        setBlockType(block);
        ((Skull) block.getState()).setOwningPlayer(Bukkit.getOfflinePlayer(name));
    }

    /**
     * Sets the block to a skull with the given UUID.
     *
     * @param block The block to set
     * @param id The player to set it to
     */
    public static void blockWithUuid(Block block, UUID id) {
        notNull(block, "block");
        notNull(id, "id");

        setBlockType(block);
        ((Skull) block.getState()).setOwningPlayer(Bukkit.getOfflinePlayer(id));
    }

    /**
     * Sets the block to a skull with the given UUID.
     *
     * @param block The block to set
     * @param url The mojang URL to set it to use
     */
    public static void blockWithUrl(Block block, String url) {
        notNull(block, "block");
        notNull(url, "url");

        blockWithBase64(block, urlToBase64(url));
    }

    /**
     * Sets the block to a skull with the given UUID.
     *
     * @param block The block to set
     * @param base64 The base64 to set it to use
     */
    public static void blockWithBase64(Block block, String base64) {
        notNull(block, "block");
        notNull(base64, "base64");

        UUID hashAsId = new UUID(base64.hashCode(), base64.hashCode());

        String args = String.format(
                "%d %d %d %s",
                block.getX(),
                block.getY(),
                block.getZ(),
                "{Owner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + base64 + "\"}]}}}"
        );

        if (newerApi()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "data merge block " + args);
        } else {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"blockdata " + args);
        }
    }

    private static boolean newerApi() {
        try {

            Material.valueOf("PLAYER_HEAD");
            return true;

        } catch (IllegalArgumentException e) { // If PLAYER_HEAD doesn't exist
            return false;
        }
    }

    private static ItemStack getPlayerSkullItem() {
        if (newerApi()) {
            return new ItemStack(Material.valueOf("PLAYER_HEAD"));
        } else {
            return new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (byte) 3);
        }
    }

    private static void setBlockType(Block block) {
        try {
            block.setType(Material.valueOf("PLAYER_HEAD"), false);
        } catch (IllegalArgumentException e) {
            block.setType(Material.valueOf("SKULL"), false);
        }
    }

    private static void notNull(Object o, String name) {
        if (o == null) {
            throw new NullPointerException(name + " should not be null!");
        }
    }

    private static String urlToBase64(String url) {

        URI actualUrl;
        try {
            actualUrl = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String toEncode = "{\"textures\":{\"SKIN\":{\"url\":\"" + actualUrl.toString() + "\"}}}";
        return Base64.encodeBase64String(toEncode.getBytes());
    }
}

/* Format for skull
{
   display:{
      Name:"Cheese"
   },
   SkullOwner:{
      Id:"9c919b83-f3fe-456f-a824-7d1d08cc8bd2",
      Properties:{
         textures:[
            {
               Value:"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTU1ZDYxMWE4NzhlODIxMjMxNzQ5YjI5NjU3MDhjYWQ5NDI2NTA2NzJkYjA5ZTI2ODQ3YTg4ZTJmYWMyOTQ2In19fQ=="
            }
         ]
      }
   }
}
 */
