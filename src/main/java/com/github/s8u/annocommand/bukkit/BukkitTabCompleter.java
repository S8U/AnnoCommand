package com.github.s8u.annocommand.bukkit;

import com.github.s8u.annocommand.CommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class BukkitTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String commandLine = label;
        if (args.length > 0) {
            commandLine += " " + String.join(" ", args);
        }

        List<String> suggestions = CommandManager.getSuggestions(sender, commandLine);
        if (suggestions.size() > 0) {
            return suggestions;
        }

        return null;
    }

}
