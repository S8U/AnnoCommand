# AnnoCommand

Bukkit & BungeeCord command library

## Using AnnoCommand
```
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.S8U:AnnoCommand:<version>'
}
```

## Example
### TestPlugin.java
``` Java
public class TestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        CommandManager.init(this);
        CommandManager.registerCommands(TestCommand.class);
    }

}
```
### TestCommand.java
``` Java
public class TestCommand {

    @Command(
            name = "test",
            helpCommand = true
    )
    public static void test(CommandSender sender, HelpCommandResult result) {
        sender.sendMessage("§e[ Test ( " + result.getPage() + " / " + result.getMaxPage() + " ) ]");
    }

    @Command(
            parent = "test",
            name = "player",
            description = "§ePlayer only test command"
    )
    public static void test_player(Player player, CommandResult result) {
        player.sendMessage("Hello " + player.getName() + "!");
    }

    @Command(
            parent = "test",
            name = "permPlayer",
            description = "§ePlayer only permission test command",
            permission = "test.admin"
    )
    public static void test_permPlayer(Player player, CommandResult result) {
        player.sendMessage("Hello " + player.getName() + "!");
    }

    @Command(
            parent = "test",
            name = "console",
            description = "§eConsole only test command"
    )
    public static void test_console(ConsoleCommandSender sender, CommandResult result) {
        sender.sendMessage("Hello Console!");
    }

    @Command(
            parent = "test",
            name = "number",
            arguments = "<number>",
            description = "§eNumber argument test command"
    )
    public static void test_number(CommandSender sender, CommandResult result) {
        sender.sendMessage("number: " + result.getArgumentAsInt(0));
    }

}
```
