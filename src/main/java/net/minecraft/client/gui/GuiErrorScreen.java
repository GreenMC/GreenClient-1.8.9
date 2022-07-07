package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;

import java.io.IOException;

public class GuiErrorScreen extends GuiScreen {

    private final String errorTitle, errorMessage;

    public GuiErrorScreen(String errorTitle, String errorMessage) {
        this.errorTitle = errorTitle;
        this.errorMessage = errorMessage;
    }

    public void initGui() {
        super.initGui();
        buttonList.add(new GuiButton(0, this.width / 2 - 100, 140, I18n.format("gui.cancel")));
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawGradientRect(0, 0, this.width, this.height, -12574688, -11530224);
        drawCenteredString(this.fontRendererObj, this.errorTitle, this.width / 2, 90, 16777215);
        drawCenteredString(this.fontRendererObj, this.errorMessage, this.width / 2, 110, 16777215);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        mc.displayGuiScreen(null);
    }
}