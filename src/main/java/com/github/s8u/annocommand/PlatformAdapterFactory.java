package com.github.s8u.annocommand;

import com.github.s8u.annocommand.bukkit.BukkitPlatformAdapter;
import com.github.s8u.annocommand.bungee.BungeePlatformAdapter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PlatformAdapterFactory {

    public static PlatformAdapter createPlatformAdapter() {
        if (existsClass("org.bukkit.Bukkit")) return new BukkitPlatformAdapter();
        else if (existsClass("net.md_5.bungee.api.ProxyServer")) return new BungeePlatformAdapter();

        return null;
    }

    private static boolean existsClass(String className) {
        try {
            Class.forName(className);

            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
