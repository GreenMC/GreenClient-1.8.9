package net.minecraft.client.gui;

import net.optifine.player.CapeUtils;

import java.io.IOException;

/**
 * @author Despical
 * <p>
 * Created at 4.07.2022
 */
public class GuiCapeEditor extends GuiScreen {

	private GuiTextField capeNameField;

	private final GuiScreen parentScreen;

	public GuiCapeEditor(GuiScreen parentScreen) {
		this.parentScreen = parentScreen;
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (this.capeNameField.isFocused()) {
			this.capeNameField.textboxKeyTyped(typedChar, keyCode);
		}

		if (keyCode == 28 || keyCode == 156) {
			this.actionPerformed(this.buttonList.get(0));
		}

		buttonList.get(0).enabled = this.capeNameField.getText().length() > 0;
	}
	@Override
	public void initGui() {
		capeNameField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, this.height / 6, 200, 20);
		capeNameField.setFocused(true);

		GuiButton downloadButton = new GuiButton(0, this.width / 2 - 100, this.height / 6 + 25, "Download Cape");
		downloadButton.enabled = mc.thePlayer != null;

		buttonList.add(downloadButton);
		buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 100, "Reset Cape"));
		buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 122, "Cancel"));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			String text = capeNameField.getText();

			if (text != null) {
				mc.customCapeName = text;
				CapeUtils.downloadCape(mc.thePlayer);

				mc.displayGuiScreen(parentScreen);
			}
		}

		if (button.id == 1) {
			mc.customCapeName = null;
			CapeUtils.downloadCape(mc.thePlayer);

			mc.displayGuiScreen(parentScreen);
		}

		if (button.id == 2) {
			mc.displayGuiScreen(parentScreen);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(this.fontRendererObj, "Cape Editor", this.width / 2, 15, 16777215);

		capeNameField.drawTextBox();

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void updateScreen() {
		capeNameField.updateCursorCounter();
	}
}