package io.github.greenmc.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

/**
 * @author Despical
 * <p>
 * Created at 10.07.2022
 */
public class GuiButtonUploadType extends GuiButton {

	private final String leftSide, rightSide;
	private final int leftSideWidth, rightSideWidth;

	private float sliderValue;
	private boolean dragging;

	public GuiButtonUploadType(int buttonId, int x, int y, String leftSide, String rightSide) {
		super(buttonId, x, y, 40, 20, "");
		this.leftSide = leftSide;
		this.rightSide = rightSide;
		this.leftSideWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(leftSide);
		this.rightSideWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(rightSide);
		this.sliderValue = .5F;
	}

	protected int getHoverState(boolean mouseOver) {
		return 0;
	}

	protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
		if (visible) {
			if (dragging) {
				sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
				sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0F, 1.0F);
			}

			if (sliderValue > .5F) {
				sliderValue = 1F;
			} else {
				sliderValue = 0F;
			}

			mc.getTextureManager().bindTexture(buttonTextures);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)), this.yPosition, 0, 66, 4, 20);
			drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);

			drawString(mc.fontRendererObj, sliderValue == 1F ? rightSide : leftSide, sliderValue == 1F ?
					this.xPosition + rightSideWidth :
					this.xPosition + (int) ((float) (this.width - 8)) - leftSideWidth, this.yPosition + this.height / 4, 0xFFFFFF);
		}
	}

	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		if (super.mousePressed(mc, mouseX, mouseY)) {
			this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
			this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0F, 1.0F);
			this.dragging = true;
			return true;
		}

		return false;
	}

	public void mouseReleased(int mouseX, int mouseY) {
		this.dragging = false;
	}

	public String getValue() {
		return sliderValue > .5F ? "git@github.com:GreenMC/CustomCapes.git" : "https://github.com/GreenMC/CustomCapes.git";
	}
}