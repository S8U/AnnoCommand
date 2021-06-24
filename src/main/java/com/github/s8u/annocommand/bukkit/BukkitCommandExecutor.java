package com.github.s8u.annocommand.bukkit;

import com.github.s8u.annocommand.CommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BukkitCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String commandLine = label;
        if (args.length > 0) {
            commandLine += " " + String.join(" ", args);
        }

        return CommandManager.executeCommand(sender, commandLine);
    }

}
