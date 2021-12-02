package com.mith.Cosmetics.utils;

import com.mith.Cosmetics.Cosmetics;
import com.mith.Cosmetics.CosmeticsPlayer;
import com.mith.Cosmetics.InventoryManager;
import com.mith.Cosmetics.manager.Messages;
import com.mith.Cosmetics.objects.CosmeticItem;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class CosmeticsHandler {
    /**
     * The Item Mappings.
     */
    private final List<CosmeticItem> cosmeticMap;

    /**
     * Cosmetics main class instance.
     */
    private final Cosmetics plugin;

    /**
     * Creates the cosmetics handler with the main class instance.
     *
     * @param plugin The Cosmetics main class instance.
     */
    public CosmeticsHandler(Cosmetics plugin) {
        this.plugin = plugin;
        cosmeticMap = new ArrayList<>();
        load(plugin);
    }

    /**
     * Clears the item maps.
     */
    public void disable() {
        cosmeticMap.clear();
    }

    /**
     * Loads the cosmetic handler data.
     *
     * @param plugin The Cosmetic main class instance.
     */
    public void load(Cosmetics plugin) {
        List<String> mappingsTypesList = Arrays.asList(
                "Axe",
                "Hoe",
                "Pickaxe",
                "Shovel",
                "Sword",
                "Bow",
                "Shield",
                "Flint_And_Steel");
            // will turn this back on after trident fix    "Trident");
        disable();
        loadMappings(mappingsTypesList);
    }


    public static String stripString(Material m)
    {
        String name = m.name();

        name = name.replace("NETHERITE_", "");
        name = name.replace("DIAMOND_","");
        name = name.replace("STONE_","");
        name = name.replace("GOLDEN_","");
        name = name.replace("WOODEN_","");
        name = name.replace("IRON_","");

        return name;
    }

    private void loadMappings(List<String> mappingsTypesList) {

        for (String s: mappingsTypesList) {

            try {
                InputStream listInput = getClass().getResourceAsStream("/mappings/" + s + ".txt");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(listInput));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.startsWith("#")) {
                        continue;
                    }

                    String[] parts = line.split(":");
                    cosmeticMap.add(new CosmeticItem(parts[0], s,  Integer.parseInt(parts[1])));
                }
                bufferedReader.close();
                listInput.close();
            } catch (Exception e) {
                plugin.getLogger().warning("An error occurred while loading cosmetic. More info below.");
                e.printStackTrace();
            }

        }
    }

    public List<CosmeticItem> getCosmetics() {
        return cosmeticMap;
    }


    public void addItemToInventory(Player p, ItemStack is, int amount) {
        List<ItemStack> items = new ArrayList<>();

        int maxStackSize = is.getMaxStackSize();

        while (amount > maxStackSize) {
            ItemStack cloned = is.clone();
            cloned.setAmount(maxStackSize);

            items.add(cloned);
            amount -= maxStackSize;
        }

        if (amount != 0) {
            ItemStack cloned = is.clone();
            cloned.setAmount(amount);

            items.add(cloned);
        }

        ItemStack[] array = new ItemStack[items.size()];
        array = items.toArray(array);

        Map<Integer, ItemStack> leftover = p.getInventory().addItem(array);

        if (!leftover.isEmpty()) {
            leftover.values().forEach(item -> p.getWorld().dropItemNaturally(p.getLocation(), item));
            sendPropMessage(p,"offlinereward.full_inventory");
        }
    }

    public void rewardOffline(CosmeticsPlayer ap) {

        Player p = ap.getOnlinePlayer();

        for (ItemStack is : ap.getOfflineItems()) {
            addItemToInventory(p, is, is.getAmount());
            sendPropMessage(p, "command.recieved_tag",is.getItemMeta().getDisplayName());
        }

        ap.getOfflineItems().clear();
        InventoryManager.getInstance().saveGson();
    }

    void sendPropMessage(Player sender, String property, Object... args) {
        MessageUtil.privateMessage(sender, property, args);
    }
    private void sendPropMessage(Player sender, String property) {
        String message = Messages.getString(property);
        sender.sendMessage(message);
    }

}
