package com.github.s8u.annocommand;

import lombok.Data;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class AnnoCommand {

    private final String ARGUMENT_REGEX = "<.[^>]*>|\\[.[^\\]]*\\]"; // <argument> or [argument]
    private final Pattern ARGUMENT_PATTERN = Pattern.compile(ARGUMENT_REGEX);


    private String parent;
    private List<String> name;

    private String arguments;
    private int argumentsLength;

    private String description;

    private String permission;

    private boolean playerOnly;
    private boolean consoleOnly;

    private boolean helpCommand;

    private Method executeMethod;


    public void setArguments(String arguments) {
        this.arguments = arguments;

        Matcher matcher = ARGUMENT_PATTERN.matcher(arguments);

        argumentsLength = 0;
        while (matcher.find()) {
            argumentsLength++;
        }
    }

    public boolean hasParent() {
        return parent != null;
    }

    public String getCommandLine() {
        if (!hasParent()) {
            return name.get(0);
        } else {
            return parent + " " + name.get(0);
        }
    }

    public int getCommandLength() {
        String commandLine = getCommandLine();

        int length = 1;
        int lastIndex = 0;
        while ((lastIndex = commandLine.indexOf(" ", lastIndex)) != -1) {
            length++;
            lastIndex++;
        }

        return length;
    }

    public String getUsage(String entered) {
        String usage;
        if (entered == null) {
            usage = "/" + getCommandLine();
        } else {
            usage = "/" + entered + " " + name.get(0);
        }

        if (arguments != null) {
            usage += " " + arguments;
        }

        if (description != null) {
            usage += " - " + description;
        }

        return usage;
    }

    @SneakyThrows
    public void execute(CommandResult result) {
        executeMethod.invoke(null, result.getSender(), result);
    }

}
