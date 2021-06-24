package com.github.s8u.annocommand.bungee;

import com.github.s8u.annocommand.AnnoCommand;
import com.github.s8u.annocommand.PlatformAdapter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.command.ConsoleCommandSender;

public class BungeePlatformAdapter implements PlatformAdapter {

    private Plugin plugin;


    @Override
    public void init(Object plugin) {
        this.plugin = (Plugin) plugin;

        ProxyServer.getInstance().getPluginManager().registerListener(this.plugin, new BungeeTabCompleter());
    }

    @Override
    public boolean registerCommand(AnnoCommand command) {
        /* Player Only */
        if (command.getExecuteMethod().getParameterTypes()[0].equals(ProxiedPlayer.class)) {
            command.setPlayerOnly(true);
        }
        /* Console Only */
        else if (command.getExecuteMethod().getParameterTypes()[0].equals(ConsoleCommandSender.class)) {
            command.setConsoleOnly(true);
        }
        /* Invalid Parameter Type */
        else if (!command.getExecuteMethod().getParameterTypes()[0].isAssignableFrom(CommandSender.class)) return false;

        /* Register to BungeeCord */
        if (!command.hasParent()) {
            BungeeCommandExecutor bungeeCommand = new BungeeCommandExecutor(command.getName().get(0), command.getPermission(), command.getName().toArray(new String[0]));

            ProxyServer.getInstance().getPluginManager().registerCommand(plugin, bungeeCommand);
        }

        return true;
    }

    @Override
    public boolean hasPermission(Object sender, String permission) {
        return ((CommandSender) sender).hasPermission(permission);
    }

    @Override
    public boolean isPlayer(Object sender) {
        return sender instanceof ProxiedPlayer;
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
