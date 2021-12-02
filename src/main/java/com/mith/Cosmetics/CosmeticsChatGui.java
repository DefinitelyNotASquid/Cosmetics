package com.mith.Cosmetics;
import com.mith.Cosmetics.objects.CosmeticItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

class CosmeticsChatGui {

    /**
     * Cosmetics main class instance.
     */
    private final Cosmetics plugin;
    public static boolean applySkin;

    /**
     * Creates the Cosmetics gui class with the main class instance.
     *
     * @param plugin The Cosmetics main class instance.
     */
    CosmeticsChatGui(Cosmetics plugin) {
        this.plugin = plugin;
    }


     /**
     * Gets the cosmetic item list gui {@link org.bukkit.inventory.Inventory} using the specified page number.
     *
     * @param page The page number to view (starts at 0).
     * @return The cosmetic list gui {@link org.bukkit.inventory.Inventory} with the page specified.
     */
    Inventory getInventory(int page, String type) {
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.DARK_AQUA + "Cosmetic Page " + (page + 1));
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            List<CosmeticItem> cosmeticItems = plugin.getCosmeticsHandler().getCosmetics().stream().filter(ci -> ci.getGenericItem().toLowerCase().equals(type.toLowerCase())).collect(Collectors.toList());
            for (int inventoryPosition = 0, cosmeticMapPosition = page * 54; inventoryPosition < 54 && cosmeticMapPosition < cosmeticItems.size(); inventoryPosition++, cosmeticMapPosition++) {
                if (inventoryPosition == 45 && page != 0) {
                    addBackArrow(gui);
                    cosmeticMapPosition--;
                } else if (cosmeticMapPosition == cosmeticItems.size() - 1) {
                    if(page != 0)
                    {
                        addBackArrow(gui);
                    }
                    addItem(gui, cosmeticItems, cosmeticMapPosition);
                } else if (inventoryPosition == 53 && cosmeticMapPosition != cosmeticItems.size() - 1) {
                    addNextArrow(gui);
                    cosmeticMapPosition--;
                } else {
                    addItem(gui, cosmeticItems, cosmeticMapPosition);
                }
            }
            cosmeticItems.clear();
        });
        return gui;
    }

    /**
     * Adds the back arrow to the gui specified.
     *
     * @param gui The gui {@link org.bukkit.inventory.Inventory} to add the back arrow to.
     */
    private void addBackArrow(Inventory gui) {
        ItemStack backArrowStack = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta meta = backArrowStack.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED + "<- Back");
        backArrowStack.setItemMeta(meta);
        gui.setItem(45, backArrowStack);
    }

    /**
     * Adds the next arrow to the gui specified.
     *
     * @param gui The gui {@link org.bukkit.inventory.Inventory} to add the next arrow to.
     */
    private void addNextArrow(Inventory gui) {
        ItemStack nextArrowStack = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta meta = nextArrowStack.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN + "Next ->");
        nextArrowStack.setItemMeta(meta);
        gui.setItem(53, nextArrowStack);
    }

    /**
     * Adds an Cosmetic item to the gui specified.
     *
     * @param gui The gui {@link org.bukkit.inventory.Inventory}.
     * @param keyList The list of keys.
     * @param cosmeticMapPosition The current position in the.
     */
    private void addItem(Inventory gui, List<CosmeticItem> keyList, int cosmeticMapPosition) {

        CosmeticItem key = keyList.get(cosmeticMapPosition);
        Material m;

        if(key.getGenericItem().equals("Sword")){
            m = Material.DIAMOND_SWORD;
        }
        else if(key.getGenericItem().equals("Axe")){
            m = Material.DIAMOND_AXE;
        }
        else if(key.getGenericItem().equals("Hoe")){
            m = Material.DIAMOND_HOE;
        }
        else if(key.getGenericItem().equals("Pickaxe")){
            m = Material.DIAMOND_PICKAXE;
        }
        else if(key.getGenericItem().equals("Shovel") || key.getGenericItem().equals("Spade")){
            m = Material.DIAMOND_SHOVEL;
        }
        else if(key.getGenericItem().equals("Bow")){
            m = Material.BOW;
        }
        else if(key.getGenericItem().equals("Shield")){
            m = Material.SHIELD;
        }
        else if(key.getGenericItem().equals("Trident")){
            m = Material.TRIDENT;
        }
        else if(key.getGenericItem().equals("Flint_And_Steel")){
            m = Material.FLINT_AND_STEEL;
        }
        else {
            m = Material.PURPLE_STAINED_GLASS_PANE;
        }
        int customModelData = key.getCustomModel();
        ItemStack stack = new ItemStack(m, 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setCustomModelData(customModelData);
        meta.setDisplayName(ChatColor.AQUA + key.getName() + " " + key.getGenericItem() + ChatColor.RESET);
        stack.setItemMeta(meta);
        gui.addItem(stack);
    }

}