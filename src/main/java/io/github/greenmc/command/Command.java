package io.github.greenmc.command;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public abstract class Command {

    public Minecraft minecraft = Minecraft.getMinecraft();

    public abstract String getName();

    public abstract boolean execute();

    public abstract String[] getAliases();

    public void sendMessage(String message) {
        minecraft.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(message));
    }

}