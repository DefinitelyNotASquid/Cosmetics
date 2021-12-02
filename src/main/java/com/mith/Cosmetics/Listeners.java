package com.mith.Cosmetics;
import com.mith.Cosmetics.manager.Messages;
import com.mith.Cosmetics.utils.MessageUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.mith.Cosmetics.utils.CosmeticsHandler.stripString;

public class Listeners implements Listener {

    private final Cosmetics plugin;

    Listeners(Cosmetics plugin) {
        this.plugin = plugin;
    }

    List<Material> availableMaterials = new ArrayList<Material>(Arrays.asList(
            Material.NETHERITE_AXE,
            Material.NETHERITE_SHOVEL,
            Material.NETHERITE_SWORD,
            Material.NETHERITE_HOE,
            Material.NETHERITE_PICKAXE,
            Material.DIAMOND_AXE,
            Material.DIAMOND_SHOVEL,
            Material.DIAMOND_SWORD,
            Material.DIAMOND_HOE,
            Material.DIAMOND_PICKAXE,
            Material.WOODEN_AXE,
            Material.WOODEN_SHOVEL,
            Material.WOODEN_SWORD,
            Material.WOODEN_HOE,
            Material.WOODEN_PICKAXE,
            Material.IRON_AXE,
            Material.IRON_SHOVEL,
            Material.IRON_SWORD,
            Material.IRON_HOE,
            Material.IRON_PICKAXE,
            Material.GOLDEN_AXE,
            Material.GOLDEN_SHOVEL,
            Material.GOLDEN_SWORD,
            Material.GOLDEN_HOE,
            Material.GOLDEN_PICKAXE,
            Material.STONE_AXE,
            Material.STONE_SHOVEL,
            Material.STONE_SWORD,
            Material.STONE_HOE,
            Material.STONE_PICKAXE,
            Material.BOW,
            Material.SHIELD,
            Material.FLINT_AND_STEEL
    ));

    /***
     * Event Handler for the Chat gui - specifically used for the forwards and backwards buttons.
     * @param event
     */
    @EventHandler
    void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().contains("Cosmetic")) {

            Player player = (Player) event.getWhoClicked();

            if(event.getCurrentItem() != null){
                if(availableMaterials.contains(event.getCurrentItem().getType()))
                {
                    if(player.hasPermission("cosmetics.skin"))
                    {
                        if(stripString(player.getInventory().getItemInMainHand().getType()).equals(stripString(event.getCurrentItem().getType())))
                        {
                            ItemMeta im = player.getInventory().getItemInMainHand().getItemMeta();
                            im.setCustomModelData(event.getCurrentItem().getItemMeta().getCustomModelData());
                            player.getInventory().getItemInMainHand().setItemMeta(im);
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }

            event.setCancelled(true);
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE
                    && event.getCurrentItem().hasItemMeta()
                    && event.getCurrentItem().getItemMeta().hasDisplayName()) {
                try {
                    int currentPage = Integer.parseInt(event.getView().getTitle().split(" ")[2]) - 1;
                    if (event.getCurrentItem().getItemMeta().getDisplayName().contains("<-")) { // Back button
                        event.getWhoClicked().openInventory(plugin.cosmeticsChatGui.getInventory(currentPage - 1, stripString(player.getInventory().getItemInMainHand().getType())));
                    } else { // Next button
                        event.getWhoClicked().openInventory(plugin.cosmeticsChatGui.getInventory(currentPage + 1, stripString(player.getInventory().getItemInMainHand().getType())));
                    }
                } catch (Exception e) { // Something happened, not sure what, so just reset their page to 0
                    event.getWhoClicked().openInventory(plugin.cosmeticsChatGui.getInventory(0, stripString(player.getInventory().getItemInMainHand().getType())));
                }
            }
        }
    }

    @EventHandler
    public void onItemClick(InventoryClickEvent ipie)
    {
        if(!(ipie.getInventory().getHolder() instanceof Player)) { return; }
        if(((Player)ipie.getInventory().getHolder()).hasPermission("cosmetics.skin")){return;}
        if(ipie.getCurrentItem() == null){ return;}
        if(!ipie.getCurrentItem().hasItemMeta()){ return;}
        if(!ipie.getCurrentItem().getItemMeta().hasCustomModelData()){ return;}

        if(!availableMaterials.contains(ipie.getCurrentItem().getType())){return;}

        ItemStack original = ipie.getCurrentItem();
        ItemMeta originalMeta = original.getItemMeta();

        originalMeta.setCustomModelData(null);
        original.setItemMeta(originalMeta);

        ipie.getCurrentItem().setItemMeta(originalMeta);
    }


    @EventHandler
    public void onItemPickUp(EntityPickupItemEvent ipie)
    {
        if(!(ipie.getEntity() instanceof Player)) { return; }
        if((((Player)ipie.getEntity()).getInventory().getHolder()).hasPermission("cosmetics.skin")){return;}
        if(!ipie.getItem().getItemStack().hasItemMeta()){return;}
        if(!ipie.getItem().getItemStack().getItemMeta().hasCustomModelData()){ return;}
        if(!availableMaterials.contains(ipie.getItem().getItemStack().getType())){return;}

        ItemStack original = ipie.getItem().getItemStack();
        ItemMeta originalMeta = original.getItemMeta();

        originalMeta.setCustomModelData(null);
        original.setItemMeta(originalMeta);

        ipie.getItem().setItemStack(original);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UUID id = p.getUniqueId();

        InventoryManager.getInstance().createPlayer(id);

        CosmeticsPlayer cp = InventoryManager.getInstance().getPlayer(id);

        if (cp.getOfflineItems().isEmpty())
            return;

        plugin.getCosmeticsHandler().rewardOffline(cp);
    }

    void sendPropMessage(Player player, String property, Object... args) {
        MessageUtil.privateMessage(player, property, args);
    }
    private void sendPropMessage(Player player, String property) {
        String message = Messages.getString(property);
        player.sendMessage(message);
    }
}
