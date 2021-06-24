package com.github.s8u.annocommand;

public interface PlatformAdapter {

    void init(Object plugin);

    boolean registerCommand(AnnoCommand command);

    boolean hasPermission(Object sender, String permission);

    boolean isPlayer(Object sender);

    boolean isConsole(Object sender);

    void sendMessage(Object sender, String message);

}
