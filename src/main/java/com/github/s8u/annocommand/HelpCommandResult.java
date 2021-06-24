package com.github.s8u.annocommand;

import lombok.Data;


@Data
public class HelpCommandResult extends CommandResult {

    private int page;
    private int maxPage;


    public HelpCommandResult(AnnoCommand command, Object sender, String commandLine) {
        super(command, sender, commandLine);
    }

}
