package io.github.greenmc;

import io.github.greenmc.command.Command;
import io.github.greenmc.command.Commands;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiVideoSettings;

public class GuiChatCommandListener extends GuiChat {

    private final Commands commands;

    public GuiChatCommandListener(GuiChat guiChat) {
        super(GuiVideoSettings.getGuiChatText(guiChat));
        this.commands = Minecraft.getMinecraft().getCommands();
    }


    public void sendChatMessage(String msg) {
        if (this.checkCustomCommand(msg)) {
            this.mc.ingameGUI.getChatGUI().addToSentMessages(msg);
            return;
        }

        super.sendChatMessage(msg);
    }

    private boolean checkCustomCommand(String msg)
    {
        if (msg == null) return false;

        // GreenMC start - add custom cmd system
        String cmd = msg.trim().substring(1);

        for (Command command : commands.getCommands()) {
            for (String alias : command.getAliases()) {
                if (alias.equalsIgnoreCase(cmd)) {
                    command.execute();
                    return true;
                }
            }
        }
        // GreenMC end

        return false;
    }

}