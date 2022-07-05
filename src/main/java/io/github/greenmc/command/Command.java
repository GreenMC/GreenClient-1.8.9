package io.github.greenmc.command;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public abstract class Command {

    protected Minecraft mc = Minecraft.getMinecraft();

    public abstract String getName();

    public abstract boolean execute();

    public abstract String[] getAliases();

    protected void sendMessage(String message) {
        mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(message));
    }
}