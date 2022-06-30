package io.github.greenmc.command;

public class ToggleSidebarCommand extends Command {

    private final String name;
    private final String[] aliases;

    public ToggleSidebarCommand(String name, String[] aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean execute() {
        minecraft.toggleSidebar();
        sendMessage("Scoreboardı görmeme durumu artık: " + minecraft.sidebarDisabled);
        return true;
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }

}