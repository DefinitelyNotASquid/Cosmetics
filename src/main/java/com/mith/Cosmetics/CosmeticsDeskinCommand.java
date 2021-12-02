package com.mith.Cosmetics;

import com.mith.Cosmetics.manager.Messages;
import com.mith.Cosmetics.utils.CosmeticsHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CosmeticsDeskinCommand implements CommandExecutor {
    /**
     * Cosmetics main class instance.
     */
    private final Cosmetics plugin;

    public List<Material> availableMaterials = new ArrayList<Material>(Arrays.asList(
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

    /**
     * Creates the Cosmetics command class with the main class instance.
     *
     * @param plugin The Cosmetics main class instance.
     */
    CosmeticsDeskinCommand(Cosmetics plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

                if(!(sender instanceof  Player)){
                    sendPropMessage(sender,"command.player_only");
                    return true;
                }

                if (!sender.hasPermission("cosmetics.skin")) {
                    sendPropMessage(sender, "command.no_perm");
                    return true;
                }

                if(!availableMaterials.contains(((Player) sender).getInventory().getItemInMainHand().getType()))
                {
                    sendPropMessage(sender, "command.not_deskinable");
                    return true;
                }

                else {
                    Player player = ((Player) sender);
                    ItemMeta im = player.getInventory().getItemInMainHand().getItemMeta();
                    im.setCustomModelData(0);
                    player.getInventory().getItemInMainHand().setItemMeta(im);
                }

                return true;
    }

    private void sendPropMessage(CommandSender sender, String property) {
        String message = Messages.getString(property);

        if (sender instanceof Player)
            sender.sendMessage(message);
        else
            sender.sendMessage(ChatColor.stripColor(message));
    }

}
