package com.github.s8u.annocommand;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommandManager {

    @Getter(AccessLevel.PACKAGE) private static PlatformAdapter platformAdapter;

    @Setter @Getter private static int helpCommandCountPerPage = 7;

    @Getter private static List<AnnoCommand> commands = new ArrayList<>();


    public static void init(Object plugin) {
        platformAdapter = PlatformAdapterFactory.createPlatformAdapter();

        platformAdapter.init(plugin);
    }

    public static void registerCommand(AnnoCommand command) {
        if (!platformAdapter.registerCommand(command)) return;

        commands.add(command);
    }

    public static void registerCommands(Class commandClass) {
        for (Method method : ReflectionUtil.getMethodsInOrder(commandClass)) {
            Command anno = method.getAnnotation(Command.class);
            if (anno == null) continue;

            /* Parameter Check */
            if (method.getParameterCount() != 2) continue;
            if (!CommandResult.class.isAssignableFrom(method.getParameterTypes()[1])) continue;

            AnnoCommand command = new AnnoCommand();

            /* Parent */
            if (anno.parent().length() > 0) {
                command.setParent(anno.parent());
            }

            /* Name */
            command.setName(Arrays.asList(anno.name()));

            /* Arguments */
            if (anno.arguments().length() > 0) {
                command.setArguments(anno.arguments());
            }

            /* Description */
            if (anno.description().length() > 0) {
                command.setDescription(anno.description());
            }

            /* Permission */
            if (anno.permission().length() > 0) {
                command.setPermission(anno.permission());
            }

            /* Help Command */
            command.setHelpCommand(anno.helpCommand());

            /* Method */
            command.setExecuteMethod(method);

            registerCommand(command);
        }
    }

    public static AnnoCommand getCommand(String commandLine) {
        String[] split = commandLine.split(" ");

        String label = split[0];

        /* No Arguments */
        if (split.length < 2) {
            for (AnnoCommand command : commands) {
                if (command.hasParent()) continue;

                for (String name : command.getName()) {
                    if (name.equalsIgnoreCase(label)) return command;
                }
            }
        }
        /* Have Arguments */
        else {
            AnnoCommand foundCommand = getCommand(label);
            if (foundCommand == null) return null;

            for (int i = 1; i < split.length; i++) {
                String argument = split[i];

                for (AnnoCommand command : commands) {
                    if (!command.hasParent()) continue;
                    if (command.getCommandLength() != i + 1) continue;

                    if (command.getCommandLine().startsWith(foundCommand.getCommandLine() + " ")) {
                        for (String name : command.getName()) {
                            if (name.equalsIgnoreCase(argument)) {
                                foundCommand =command;
                                break;
                            }
                        }
                    }
                }
            }

            return foundCommand;
        }

        return null;
    }

    public static List<AnnoCommand> getSubCommands(AnnoCommand parentCommand, int deep) {
        String parentCommandLine = parentCommand.getCommandLine() + " ";
        int commandLengthLimit = parentCommand.getCommandLength() + deep;

        List<AnnoCommand> subCommands = new ArrayList<>();

        for (AnnoCommand command : commands) {
            if (command.getCommandLine().startsWith(parentCommandLine) && command.getCommandLength() <= commandLengthLimit) {
                subCommands.add(command);
            }
        }

        return subCommands;
    }

    public static List<String> getSuggestions(Object sender, String commandLine) {
        List<String> suggestions = new ArrayList<>();

        String lastArgument = commandLine.substring(commandLine.lastIndexOf(" ")).trim();

        AnnoCommand parentCommand = getCommand(commandLine);
        getSubCommands(parentCommand, 1)
                .stream()
                .filter(subCommand -> subCommand.getPermission() == null || platformAdapter.hasPermission(sender, subCommand.getPermission()))
                .map(subCommand -> subCommand.getName())
                .forEach(name -> suggestions.addAll(name));

        return suggestions;
    }

    public static boolean executeCommand(Object sender, String commandLine) {
        AnnoCommand command = CommandManager.getCommand(commandLine);
        if (command != null) {
            /* Player Only */
            if (command.isPlayerOnly() && !platformAdapter.isPlayer(sender)) {
                platformAdapter.sendMessage(sender, "§c플레이어만 사용할 수 있는 명령어입니다.");
                return true;
            }
            /* Console Only */
            else if (command.isConsoleOnly() && !platformAdapter.isConsole(sender)) {
                platformAdapter.sendMessage(sender, "§c콘솔에서만 사용할 수 있는 명령어입니다.");
                return true;
            }
            /* Permission Check */
            else if (command.getPermission() != null && !platformAdapter.hasPermission(sender, command.getPermission())) {
                platformAdapter.sendMessage(sender, "§c명령어를 사용할 권한이 없습니다.");
                return true;
            }
            /* Argument Length Check */
            else if (commandLine.split(" ").length < command.getCommandLength() + command.getArgumentsLength()) {
                platformAdapter.sendMessage(sender, command.getUsage(null));
                return true;
            }
            /* Help Command */
            else if (command.isHelpCommand()) {
                HelpCommandResult result = new HelpCommandResult(command, sender, commandLine);

                List<AnnoCommand> subCommands = CommandManager.getSubCommands(command, 1)
                        .stream()
                        .filter(subCommand -> subCommand.getPermission() == null || CommandManager.getPlatformAdapter().hasPermission(sender, subCommand.getPermission()))
                        .collect(Collectors.toList());

                Integer page = result.getArgumentAsInt(0);
                if (page == null) {
                    page = 1;
                }
                int maxPage = (int) Math.ceil((float) subCommands.size() / helpCommandCountPerPage);

                if (page < 1 || page > maxPage) {
                    platformAdapter.sendMessage(sender, "§c페이지는 1~" + maxPage + "만 입력할 수 있습니다.");
                    return true;
                }

                result.setPage(page);
                result.setMaxPage(maxPage);

                command.execute(result);

                for (int i = (page - 1) * helpCommandCountPerPage; i < page * helpCommandCountPerPage; i++) {
                    if (i >= subCommands.size()) break;

                    platformAdapter.sendMessage(sender, subCommands.get(i).getUsage(null));
                }
            }
            /* Execute Command */
            else {
                command.execute(new CommandResult(command, sender, commandLine));
            }

            return true;
        }

        return false;
    }

}