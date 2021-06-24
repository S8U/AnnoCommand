package com.github.s8u.annocommand.bungee;

import com.github.s8u.annocommand.CommandManager;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class BungeeTabCompleter implements Listener {

    @EventHandler
    public void onTabComplete(TabCompleteEvent e) {
        if (e.getCursor().equals("/")) return;

        String commandLine = e.getCursor().substring(1, e.getCursor().length());

        List<String> suggestions = CommandManager.getSuggestions(e.getSender(), commandLine);
        e.getSuggestions().addAll(suggestions);
    }

}
