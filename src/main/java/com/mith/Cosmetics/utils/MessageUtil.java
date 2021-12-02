package com.mith.Cosmetics.utils;

import com.mith.Cosmetics.manager.Messages;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtil {

    public static void privateMessage(CommandSender sender, String prop, Object... args) {
        String message = Messages.getString(prop, args);

        if (sender instanceof Player) {
            sender.sendMessage(message);
        } else {
            message = ChatColor.stripColor(message);
            sender.sendMessage(message);
        }
    }

    public static void privateMessage(CommandSender sender, BaseComponent... comp) {
        if (sender instanceof Player) {
            ((Player) sender).spigot().sendMessage(comp);
        } else {
            sender.sendMessage(ChatColor.stripColor(new TextComponent(comp).toPlainText()));
        }
    }
}
