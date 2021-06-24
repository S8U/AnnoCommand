package com.github.s8u.annocommand;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
public class CommandResult {

    private AnnoCommand command;

    private Object sender;

    private String entered;
    private String enteredArguments;

    private List<String> arguments = new ArrayList<>();


    public CommandResult(AnnoCommand command, Object sender, String commandLine) {
        this.command = command;
        this.sender = sender;
        this.entered = "";
        this.enteredArguments = "";

        int commandLength = command.getCommandLength();

        String[] split = commandLine.split(" ");
        for (int i = 0; i < split.length; i++) {
            if (i < commandLength) {
                this.entered += this.entered.length() < 1 ? split[i] : " " + split[i];
            } else {
                this.enteredArguments += this.enteredArguments.length() < 1 ? split[i] : " " + split[i];
                arguments.add(split[i]);
            }
        }
    }

    public String getArgument(int index) {
        if (arguments.size() <= index) return null;

        return arguments.get(index);
    }

    public Integer getArgumentAsInt(int index) {
        String value = getArgument(index);
        if (value == null) return null;

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Long getArgumentAsLong(int index) {
        String value = getArgument(index);
        if (value == null) return null;

        try {
            return Long.parseLong(getArgument(index));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Float getArgumentAsFloat(int index) {
        String value = getArgument(index);
        if (value == null) return null;

        try {
            return Float.parseFloat(getArgument(index));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Double getArgumentAsDouble(int index) {
        String value = getArgument(index);
        if (value == null) return null;

        try {
            return Double.parseDouble(getArgument(index));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Boolean getArgumentAsBoolean(int index) {
        String value = getArgument(index);
        if (value == null) return null;
        else if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) return null;

        return Boolean.parseBoolean(getArgument(index));
    }

    public String subArgument(int beginIndex) {
        String value = "";

        for (int i = beginIndex; i < arguments.size(); i++) {
            value += value.length() < 1 ? arguments.get(i) : " " + arguments.get(i);
        }

        return value;
    }

    public String subArgument(int beginIndex, int endIndex) {
        String value = "";

        for (int i = beginIndex; i <= endIndex; i++) {
            value += value.length() < 1 ? arguments.get(i) : " " + arguments.get(i);
        }

        return value;
    }

    public int getArgumentsLength() {
        return arguments.size();
    }

}
