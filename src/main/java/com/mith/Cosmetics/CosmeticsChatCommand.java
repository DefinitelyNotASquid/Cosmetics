package com.mith.Cosmetics;
import com.mith.Cosmetics.manager.Messages;
import com.mith.Cosmetics.utils.MessageUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

class CosmeticsChatCommand implements CommandExecutor {
    /**
     * Cosmetics main class instance.
     */
    private final Cosmetics plugin;

    /**
     * Creates the Cosmetics command class with the main class instance.
     *
     * @param plugin The Cosmetics main class instance.
     */
    CosmeticsChatCommand(Cosmetics plugin) {
        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings("Deprecated")
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length < 1) {
            sendPropMessage(sender, "command.plugin_info");
            sendPropMessage(sender,"command.help_reference");
            return true;
        }

        // Sub commands
        switch (args[0].toLowerCase()) {
            case "help":
                if (!sender.hasPermission("cosmetics.help")) {
                    sendPropMessage(sender, "command.no_perm");
                    return true;
                }
                sendPropMessage(sender, "command.help_title");
                sendPropMessage(sender, "command.help");

                if(sender.hasPermission("cosmetics.reload")){
                    sendPropMessage(sender,"command.reload");
                }
                return true;
            case "reload":
                if (!sender.hasPermission("cosmetics.reload")) {
                    sendPropMessage(sender, "command.no_perm");
                    return true;
                }
                Messages.getInstance().reload();
                plugin.reloadConfig();
                plugin.getCosmeticsHandler().load(plugin);
                sender.sendMessage(ChatColor.GREEN + "Cosmetics config reloaded.");
                return true;
            case "test":
                if (!sender.hasPermission("cosmetics.test")) {
                    sendPropMessage(sender, "command.no_perm");
                    return true;
                }

                int i = Integer.parseInt(args[1]);
                Player p = (Player)sender;
                ItemStack itemInMainHand = p.getInventory().getItemInMainHand();
                ItemMeta im = itemInMainHand.getItemMeta();
                im.setCustomModelData(i);
                itemInMainHand.setItemMeta(im);
                p.getInventory().setItemInMainHand(itemInMainHand);
                return true;
            case "debugitem":
                if (!sender.hasPermission("cosmetics.debugitem")) {
                    sendPropMessage(sender, "command.no_perm");
                    return true;
                }
                if(!(sender instanceof  Player)){
                    sendPropMessage(sender, "command.player_only");
                }

                Player debugitemItemPlayer = (Player)sender;
                ItemStack debugItem = debugitemItemPlayer.getInventory().getItemInMainHand();
                sender.sendMessage("Item Custom Model : " + (debugItem.getItemMeta().hasCustomModelData() ? debugItem.getItemMeta().getCustomModelData() : "No custom Model"));
                sender.sendMessage("Item Name : " + (debugItem.getItemMeta().hasDisplayName() ? debugItem.getItemMeta().getDisplayName() : "No custom Name"));
                return true;
            default:
                sendPropMessage(sender, "command.invalid_sub");
                return true;
        }
    }
    void sendPropMessage(CommandSender sender, String property, Object... args) {
        MessageUtil.privateMessage(sender, property, args);
    }
    private void sendPropMessage(CommandSender sender, String property) {
        String message = Messages.getString(property);

        if (sender instanceof Player)
            sender.sendMessage(message);
        else
            sender.sendMessage(ChatColor.stripColor(message));
    }

}
