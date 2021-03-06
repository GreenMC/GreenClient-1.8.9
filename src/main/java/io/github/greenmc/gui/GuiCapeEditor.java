package io.github.greenmc.gui;

import io.github.greenmc.ChatEffects;
import io.github.greenmc.Git;
import io.github.greenmc.util.FileUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.optifine.player.CapeUtils;

import java.awt.*;
import java.io.IOException;
import java.nio.file.*;

/**
 * @author Despical
 * <p>
 * Created at 4.07.2022
 */
public class GuiCapeEditor extends GuiScreen {

	private String current = "", repoLink;
	private GuiTextField capeNameField, capeUrlField;

	private final GuiScreen parentScreen;

	public GuiCapeEditor(GuiScreen parentScreen) {
		this.parentScreen = parentScreen;
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (capeNameField.isFocused()) {
			capeNameField.textboxKeyTyped(typedChar, keyCode);
		}

		if (capeUrlField.isFocused()) {
			capeUrlField.textboxKeyTyped(typedChar, keyCode);
		}

		if (keyCode == 28 || keyCode == 156) {
			actionPerformed(this.buttonList.get(0));
		} else if (keyCode == 1) {
			mc.displayGuiScreen(parentScreen);
		}

		buttonList.get(0).enabled = capeNameField.getText().length() > 0 || capeUrlField.getText().length() > 0;
	}
	@Override
	public void initGui() {
		capeNameField = new GuiTextField(0, fontRendererObj, width / 2 - 100, height / 6, 200, 20);
		capeNameField.setFocused(true);

		capeUrlField = new GuiTextField(1, fontRendererObj, width / 2 - 100, height / 6 + 25, 200, 20);
		capeUrlField.setMaxStringLength(Integer.MAX_VALUE);
		capeUrlField.setFocused(false);

		GuiButton downloadButton = new GuiButton(0, width / 2 - 100, height / 6 + 55, "Download Cape");
		downloadButton.enabled = mc.thePlayer != null;

		GuiButton resetButton = new GuiButton(1, width / 2 - 100, height / 4 + 100, "Reset Cape");
		resetButton.enabled = !mc.isIntegratedServerRunning() || mc.thePlayer != null && mc.thePlayer.hasCape();

		GuiButtonUpload uploadButton = new GuiButtonUpload(3, width / 2 - 130, height / 6);
		uploadButton.enabled = mc.thePlayer != null && mc.thePlayer.hasCape();

		GuiButton deleteButton = new GuiButtonDelete(4, width / 2 - 130, height / 6 + 25);
		deleteButton.enabled = Files.exists(Paths.get("C:\\TEMP\\CustomCapes"));

		GuiButtonUploadType sshOrGitButton = new GuiButtonUploadType(5, width / 2 - 180, height / 6, "SSH", "Git");

		buttonList.add(resetButton);
		buttonList.add(uploadButton);
		buttonList.add(deleteButton);
		buttonList.add(sshOrGitButton);
		buttonList.add(downloadButton);
		buttonList.add(new GuiButton(2, width / 2 - 100, height / 4 + 122, "Cancel"));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			String text = capeNameField.getText(), url = capeUrlField.getText();

			if (url != null && !url.isEmpty()) {
				mc.customCapeName = null;
				mc.customCapeUrl = url;
				CapeUtils.downloadCape(mc.thePlayer);

				current = "Cape URL'si de??i??tirildi.";
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

			current = "Cape s??f??rland??.";
		}

		if (button.id == 2) {
			mc.displayGuiScreen(parentScreen);
		}

		if (button.id == 3) {
			CapeUploader uploadThread = new CapeUploader(this);
			uploadThread.start();
		}

		if (button.id == 4) {
			FileUtils.deleteDirWithFiles(Paths.get("C:\\TEMP"));
			buttonList.get(2).enabled = false;

			current = "Dosyalar ba??ar??yla silindi.";
		}

		if (button.id == 5) {
			repoLink = ((GuiButtonUploadType) buttonList.get(3)).getValue();
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, "Cape Editor", width / 2, 15, 16777215);
		drawCenteredString(fontRendererObj, "URL Cape, ??sim Cape'den daha ??nceliklidir.", width / 2, height / 4 + 75, ChatEffects.rainbowEffect(1, 1).getRGB());

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

	private final class CapeUploader extends Thread {

		private final GuiCapeEditor editor;

		private CapeUploader(GuiCapeEditor editor) {
			super("Cape Upload Thread");
			this.editor = editor;
		}

		public void run() {
			try {
				Path directory = Paths.get("C:\\TEMP\\CustomCapes");
				String name = editor.mc.thePlayer.getNameClear();

				if (!Files.exists(directory)) {
					Files.createDirectories(directory);

					editor.current = "Repo klonlan??yor...";
					Git.gitClone(directory, repoLink);
				}

				editor.current = "URL'deki veriler dosyaya yaz??l??yor...";
				FileUtils.copyURLTo(CapeUtils.getURL(name), directory.resolve(CapeUtils.getCustomName(name) + ".png").toFile());
				editor.current = "Cape sahiplerine ismin ekleniyor...";
				FileUtils.writeIfNotExists(directory.resolve("capes.txt"), name, StandardOpenOption.APPEND);
				editor.current = "Veriler git'e ekleniyor...";
				Git.gitStage(directory);
				editor.current = "Commit at??l??yor...";
				Git.gitCommit(directory, "Added new custom cape owner " + name);
				editor.current = "Dosya pushlan??yor...";
				Git.gitPush(directory);
				editor.current = "Cape uploadland??.";

				buttonList.get(2).enabled = true;

				mc.customCapeName = null;
				mc.customCapeUrl = null;
			} catch (IOException | InterruptedException exception) {
				editor.current = "Upload s??ras??nda bir hata olu??tu. Loglara g??z at??n.";
				buttonList.get(2).enabled = false;

				System.out.println("Exception during the Git process!");
				exception.printStackTrace();
			}
		}
	}
}