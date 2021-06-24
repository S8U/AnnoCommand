package com.github.s8u.annocommand.bungee;

import com.github.s8u.annocommand.CommandManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class BungeeCommandExecutor extends Command {

    public BungeeCommandExecutor(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String commandLine = getName();
        if (args.length > 0) {
            commandLine += " " + String.join(" ", args);
        }

        CommandManager.executeCommand(sender, commandLine);
    }

}
