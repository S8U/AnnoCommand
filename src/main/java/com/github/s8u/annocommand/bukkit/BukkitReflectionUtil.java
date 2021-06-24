package com.github.s8u.annocommand.bukkit;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

@UtilityClass
public class BukkitReflectionUtil {

    @SneakyThrows(Exception.class)
    public static CommandMap getCommandMap() {
        Field f = SimplePluginManager.class.getDeclaredField("commandMap");
        f.setAccessible(true);

        return (CommandMap) f.get(Bukkit.getPluginManager());
    }

    @SneakyThrows
    public static PluginCommand getCommand(String name, Plugin plugin) {
        Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
        c.setAccessible(true);

        return c.newInstance(name, plugin);
    }

}
