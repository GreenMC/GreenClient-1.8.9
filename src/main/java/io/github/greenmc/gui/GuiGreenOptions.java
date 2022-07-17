package io.github.greenmc.gui;

import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;

import java.io.IOException;

/**
 * @author Despical
 * <p>
 * Created at 17.07.2022
 */
public class GuiGreenOptions extends GuiScreen{

	private final GuiScreen parentScreen;

	public GuiGreenOptions(GuiScreen parentScreen) {
		this.parentScreen = parentScreen;
	}

	public void initGui() {
		this.buttonList.add(new GuiButton(100, this.width / 2 - 155, 45, 150, 20, "Toggle sneak: " + (mc.gameSettings.toggleSneak ? "" : "not ") + "toggled"));
		this.buttonList.add(new GuiButton(200, this.width / 2 - 100, 80, I18n.format("gui.done")));
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.enabled) {
			if (button.id == 100) {
				boolean toggled = mc.gameSettings.toggleSneak;

				mc.gameSettings.toggleSneak = !toggled;
				button.displayString = "Sneak: " + (toggled ? "not " : "") + "toggled";
			}

			if (button.id == 200) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(this.parentScreen);
			}
		}
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, "Green Options", this.width / 2, 15, 16777215);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}