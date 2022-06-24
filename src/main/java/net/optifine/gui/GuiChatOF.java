package net.optifine.gui;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.util.ChatComponentText;
import net.optifine.Config;
import net.optifine.shaders.Shaders;

public class GuiChatOF extends GuiChat {

    public GuiChatOF(GuiChat guiChat)
    {
        super(GuiVideoSettings.getGuiChatText(guiChat));
    }

    public void sendChatMessage(String msg)
    {
        if (this.checkCustomCommand(msg))
        {
            this.mc.ingameGUI.getChatGUI().addToSentMessages(msg);
        }
        else
        {
            super.sendChatMessage(msg);
        }
    }

    private boolean checkCustomCommand(String msg)
    {
        if (msg == null) {
            return false;}
        else {
            msg = msg.trim();

            // GreenMC start - add hack
            if (msg.equals("/invistoggle")) {
                this.mc.toggleNoInvis();
                this.mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(this.mc.seeInvis + " invis"));
                return true;
            }

            else if (msg.equals("/toggleredthing")) {
                this.mc.toggleRedThing();
                this.mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(this.mc.redThing + " redThing"));
                return true;
            }
            // GreenMC end - add hack
        }

        return false;
    }

}