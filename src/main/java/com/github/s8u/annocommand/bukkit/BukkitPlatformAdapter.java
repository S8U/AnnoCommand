package com.github.s8u.annocommand.bukkit;

import com.github.s8u.annocommand.AnnoCommand;
import com.github.s8u.annocommand.PlatformAdapter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class BukkitPlatformAdapter implements PlatformAdapter {

    private BukkitCommandExecutor commandExecutor = new BukkitCommandExecutor();
    private BukkitTabCompleter tabCompleter = new BukkitTabCompleter();

    private Plugin plugin;


    @Override
    public void init(Object plugin) {
        this.plugin = (Plugin) plugin;
    }

    @Override
    public boolean registerCommand(AnnoCommand command) {
        /* Player Only */
        if (command.getExecuteMethod().getParameterTypes()[0].equals(Player.class)) {
            command.setPlayerOnly(true);
        }
        /* Console Only */
        else if (command.getExecuteMethod().getParameterTypes()[0].equals(ConsoleCommandSender.class)) {
            command.setConsoleOnly(true);
        }
        /* Invalid Parameter Type */
        else if (!command.getExecuteMethod().getParameterTypes()[0].isAssignableFrom(CommandSender.class)) return false;


        /* Register to Bukkit */
        if (!command.hasParent()) {
            PluginCommand pluginCommand = BukkitReflectionUtil.getCommand(command.getCommandLine(), plugin);

            /* Alias */
            if (command.getName().size() > 1) {
                List<String> aliases = new ArrayList<>(command.getName());
                aliases.remove(0);

                pluginCommand.setAliases(aliases);
            }

            /* Description */
            if (command.getDescription() != null) {
                pluginCommand.setDescription(command.getDescription());
            }

            /* Usage */
            pluginCommand.setUsage(command.getUsage(null));

            /* Permission */
            pluginCommand.setPermission(command.getPermission());

            /* Command Executor */
            pluginCommand.setExecutor(commandExecutor);

            /* Tab Completer */
            pluginCommand.setTabCompleter(tabCompleter);

            BukkitReflectionUtil.getCommandMap().register(plugin.getName(), pluginCommand);
        }

        return true;
    }

    @Override
    public boolean hasPermission(Object sender, String permission) {
        return ((CommandSender) sender).hasPermission(permission);
    }

    @Override
    public boolean isPlayer(Object sender) {
        return sender instanceof Player;
    }

    @Override
    public boolean isConsole(Object sender) {
        return sender instanceof ConsoleCommandSender;
    }

    @Override
    public void sendMessage(Object sender, String message) {
        ((CommandSender) sender).sendMessage(message);
    }

}
