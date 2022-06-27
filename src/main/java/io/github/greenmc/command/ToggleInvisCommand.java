package io.github.greenmc.command;

public class ToggleInvisCommand implements Command {

    private final String name;
    private final String[] aliases;

    public ToggleInvisCommand(String name, String[] aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean execute() {
        minecraft.seeInvis = !minecraft.seeInvis;
        sendMessage("Görünmezleri görme durumu artık: " + minecraft.seeInvis);
        return true;
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }

}