package com.mith.Cosmetics;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
class CosmeticsChatTabComplete implements TabCompleter {
    /**
     * The list of /cosmetic sub-commands.
     */
    private final List<String> SUB_COMMANDS = new ArrayList<>(Arrays.asList("help", "reload"));

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        // The possible completions
        List<String> completions = new ArrayList<>();
        // Gets the matches
        StringUtil.copyPartialMatches(args[0], SUB_COMMANDS, completions);
        // Sort the completions
        Collections.sort(completions);
        return completions;
    }
}
