package io.github.greenmc.command;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public interface Command {

    Minecraft minecraft = Minecraft.getMinecraft();

    String getName();

    boolean execute();

    String[] getAliases();

    default void sendMessage(String message) {
        minecraft.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(message));
    }


}