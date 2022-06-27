package io.github.greenmc.command;

import java.util.ArrayList;
import java.util.List;

public class Commands {

    private final List<Command> commands = new ArrayList<>();

    public Commands() {
        commands.add(new ToggleInvisCommand("toggleinvis", new String[]{"toggleinvis", "invistoggle", "seeinvis", "invissee"}));
        commands.add(new ToggleRedThingCommand("toggleredthing", new String[]{"toggleredthing", "redthing", "redtoggle", "togglered"}));
    }

    public List<Command> getCommands() {
        return commands;
    }

}