package io.github.greenmc.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * @author Despical
 * <p>
 * Created at 7.07.2022
 */
public class GuiButtonDelete extends GuiButton {

	private final ResourceLocation deleteIcon;

	public GuiButtonDelete(int buttonID, int xPos, int yPos) {
		super (buttonID, xPos, yPos, 20, 20, "");
		this.deleteIcon = new ResourceLocation("textures/gui/trash_bin.png");
	}

	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		super.drawButton(mc, mouseX, mouseY);

		mc.getTextureManager().bindTexture(deleteIcon);

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(0, 769);

		Gui.drawScaledCustomSizeModalRect(xPosition + 2, yPosition + 2, 0.0F, 0.0F, 16, 16, 16, 16, 16, 16);
	}
}