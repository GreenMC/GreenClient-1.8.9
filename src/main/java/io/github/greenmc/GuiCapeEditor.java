package io.github.greenmc;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.optifine.player.CapeUtils;

import java.awt.*;
import java.io.IOException;

/**
 * @author Despical
 * <p>
 * Created at 4.07.2022
 */
public class GuiCapeEditor extends GuiScreen {

	private GuiTextField capeNameField;
	private GuiTextField capeUrlField;

	private final GuiScreen parentScreen;

	private String current = "";

	public GuiCapeEditor(GuiScreen parentScreen) {
		this.parentScreen = parentScreen;
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (this.capeNameField.isFocused()) {
			this.capeNameField.textboxKeyTyped(typedChar, keyCode);
		}

		if (this.capeUrlField.isFocused()) {
			this.capeUrlField.textboxKeyTyped(typedChar, keyCode);
		}

		if (keyCode == 28 || keyCode == 156) {
			this.actionPerformed(this.buttonList.get(0));
		} else if (keyCode == 1) {
			mc.displayGuiScreen(parentScreen);
		}

		buttonList.get(0).enabled = this.capeNameField.getText().length() > 0 || this.capeUrlField.getText().length() > 0;
	}
	@Override
	public void initGui() {
		capeNameField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, this.height / 6, 200, 20);
		capeNameField.setFocused(true);

		capeUrlField = new GuiTextField(1, this.fontRendererObj, this.width / 2 - 100, this.height / 6 + 25, 200, 20);
		capeUrlField.setMaxStringLength(Integer.MAX_VALUE);
		capeUrlField.setFocused(false);

		GuiButton downloadButton = new GuiButton(0, this.width / 2 - 100, this.height / 6 + 55, "Download Cape");
		downloadButton.enabled = mc.thePlayer != null;

		GuiButton resetButton = new GuiButton(1, this.width / 2 - 100, this.height / 4 + 100, "Reset Cape");
		resetButton.enabled = !mc.isIntegratedServerRunning() || mc.thePlayer != null && mc.thePlayer.hasCape();

		buttonList.add(downloadButton);
		buttonList.add(resetButton);
		buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 122, "Cancel"));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			String text = capeNameField.getText(), url = capeUrlField.getText();

			if (url != null && !url.isEmpty()) {
				mc.customCapeName = null;
				mc.customCapeUrl = url;
				CapeUtils.downloadCape(mc.thePlayer);

				current = "Cape URL'si değiştirildi.";
				return;
			}

			if (text != null && !text.isEmpty()) {
				mc.customCapeUrl = null;
				mc.customCapeName = text;
				CapeUtils.downloadCape(mc.thePlayer);

				current = "Yeni cape isminiz: " + text;
			}
		}

		if (button.id == 1) {
			mc.customCapeName = null;
			mc.customCapeUrl = null;
			CapeUtils.downloadCape(mc.thePlayer);

			current = "Cape sıfırlandı.";
		}

		if (button.id == 2) {
			mc.displayGuiScreen(parentScreen);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, "Cape Editor", width / 2, 15, 16777215);
		drawCenteredString(fontRendererObj, "URL Cape, İsim Cape'den daha önceliklidir.", width / 2, height / 4 + 75, ChatEffects.rainbowEffect(1, 1).getRGB());

		capeNameField.drawTextBox();

		if (capeNameField.getText().isEmpty()) {
			drawString(fontRendererObj, "Optifine Cape Name", width / 2 - 96, height / 6 + 7, 16777215);
		}

		capeUrlField.drawTextBox();

		if (capeUrlField.getText().isEmpty()) {
			drawString(fontRendererObj, "Cape URL", width / 2 - 96, height / 6 + 32, 16777215);
		}

		drawCenteredString(fontRendererObj, current, width / 2, height / 4 + 150, Color.decode("#03e3fc").getRGB());

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.capeUrlField.mouseClicked(mouseX, mouseY, mouseButton);
		this.capeNameField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void updateScreen() {
		capeNameField.updateCursorCounter();
		capeUrlField.updateCursorCounter();
	}
}