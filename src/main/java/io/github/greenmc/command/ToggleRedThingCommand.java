package io.github.greenmc.command;

public class ToggleRedThingCommand extends Command {

    private final String name;
    private final String[] aliases;

    public ToggleRedThingCommand(String name, String[] aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean execute() {
        mc.toggleRedThing();
        sendMessage("Scoreboarddaki kırmızı skorları görme durumu artık: " + mc.redThing);
        return true;
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }

}
