package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/**
 * @author Despical
 * <p>
 * Created at 6.07.2022
 */
public class GuiButtonUpload extends GuiButton {

	private final ResourceLocation uploadIcon;

	public GuiButtonUpload(int buttonID, int xPos, int yPos) {
		super (buttonID, xPos, yPos, 20, 20, "");
		this.uploadIcon = new ResourceLocation("textures/gui/upload_icon.png");
	}

	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		super.drawButton(mc, mouseX, mouseY);

		mc.getTextureManager().bindTexture(uploadIcon);

		Gui.drawScaledCustomSizeModalRect(xPosition + 2, yPosition + 2, 0.0F, 0.0F, 16, 16, 16, 16, 16, 16);
	}
}