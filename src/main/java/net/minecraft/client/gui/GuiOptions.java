package net.minecraft.client.gui;

import io.github.greenmc.ChatEffects;
import io.github.greenmc.gui.GuiCapeEditor;
import io.github.greenmc.gui.GuiGreenOptions;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundEventAccessorComposite;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.EnumDifficulty;

import java.io.IOException;

public class GuiOptions extends GuiScreen implements GuiYesNoCallback {

    private static final GameSettings.Options[] gameOptions = new GameSettings.Options[] {GameSettings.Options.FOV};
    private final GuiScreen parentScreen;
    private final GameSettings gameSettings;
    private GuiButton lockButton, capeEditorButton, greenOptionsButton;
    private GuiLockIconButton guiLockIconButton;
    protected String title = "Options";

    public GuiOptions(GuiScreen parentScreen, GameSettings gameSettings) {
        this.parentScreen = parentScreen;
        this.gameSettings = gameSettings;
    }

    public void initGui() {
        int i = 0;
        this.title = I18n.format("options.title");

        for (GameSettings.Options gamesettings$options : gameOptions) {
            if (gamesettings$options.getEnumFloat()) {
                this.buttonList.add(new GuiOptionSlider(gamesettings$options.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), gamesettings$options));
            } else {
                GuiOptionButton guioptionbutton = new GuiOptionButton(gamesettings$options.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), gamesettings$options, this.gameSettings.getKeyBinding(gamesettings$options));
                this.buttonList.add(guioptionbutton);
            }

            ++i;
        }

        if (mc.theWorld != null) {
            EnumDifficulty enumdifficulty = this.mc.theWorld.getDifficulty();
            this.lockButton = new GuiButton(111, this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), 150, 20, this.func_175355_a(enumdifficulty));
            this.buttonList.add(this.lockButton);

            if (this.mc.isSingleplayer() && !this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
                this.lockButton.setWidth(this.lockButton.getButtonWidth() - 20);
                this.guiLockIconButton = new GuiLockIconButton(109, this.lockButton.xPosition + this.lockButton.getButtonWidth(), this.lockButton.yPosition);
                this.buttonList.add(this.guiLockIconButton);
                this.guiLockIconButton.func_175229_b(this.mc.theWorld.getWorldInfo().isDifficultyLocked());
                this.guiLockIconButton.enabled = !this.guiLockIconButton.func_175230_c();
                this.lockButton.enabled = !this.guiLockIconButton.func_175230_c();
            } else {
                this.lockButton.enabled = false;
            }
        }

        this.buttonList.add(new GuiButton(110, this.width / 2 - 155, this.height / 6 + 48 - 6, 150, 20, I18n.format("options.skinCustomisation")));
        this.buttonList.add(new GuiButton(8675309, this.width / 2 + 5, this.height / 6 + 48 - 6, 150, 20, "Super Secret Settings...")
        {
            public void playPressSound(SoundHandler soundHandlerIn)
            {
                SoundEventAccessorComposite soundeventaccessorcomposite = soundHandlerIn.getRandomSoundFromCategories(SoundCategory.ANIMALS, SoundCategory.BLOCKS, SoundCategory.MOBS, SoundCategory.PLAYERS, SoundCategory.WEATHER);

                if (soundeventaccessorcomposite != null)
                {
                    soundHandlerIn.playSound(PositionedSoundRecord.create(soundeventaccessorcomposite.getSoundEventLocation(), 0.5F));
                }
            }
        });

        this.buttonList.add(new GuiButton(106, this.width / 2 - 155, this.height / 6 + 72 - 6, 150, 20, I18n.format("options.sounds")));
        this.buttonList.add(new GuiButton(101, this.width / 2 - 155, this.height / 6 + 96 - 6, 150, 20, I18n.format("options.video")));
        this.buttonList.add(new GuiButton(100, this.width / 2 + 5, this.height / 6 + 72 - 6, 150, 20, I18n.format("options.controls")));
        this.buttonList.add(new GuiButton(102, this.width / 2 + 5, this.height / 6 + 120 - 6, 150, 20, I18n.format("options.language")));
        this.buttonList.add(new GuiButton(103, this.width / 2 + 5, this.height / 6 + 96 - 6, 150, 20, I18n.format("options.chat.title")));
        this.buttonList.add(new GuiButton(105, this.width / 2 - 155, this.height / 6 + 120 - 6, 150, 20, I18n.format("options.resourcepack")));
        this.buttonList.add(capeEditorButton = new GuiButton(107, this.width / 2 - 155, this.height / 6 + 144 - 6, 150, 20, "Cape Editor", ChatEffects.rainbowEffect(1, 1).getRGB()));
        this.buttonList.add(greenOptionsButton = new GuiButton(120, this.width / 2 + 5, this.height / 6 + 144 - 6, 150, 20, "Green Options", ChatEffects.rainbowEffect(1, 1).getRGB()));
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, I18n.format("gui.done")));
    }

    public String func_175355_a(EnumDifficulty p_175355_1_)
    {
        IChatComponent ichatcomponent = new ChatComponentText("");
        ichatcomponent.appendSibling(new ChatComponentTranslation("options.difficulty"));
        ichatcomponent.appendText(": ");
        ichatcomponent.appendSibling(new ChatComponentTranslation(p_175355_1_.getDifficultyResourceKey()));
        return ichatcomponent.getFormattedText();
    }

    public void confirmClicked(boolean result, int id)
    {
        this.mc.displayGuiScreen(this);

        if (id == 109 && result && this.mc.theWorld != null)
        {
            this.mc.theWorld.getWorldInfo().setDifficultyLocked(true);
            this.guiLockIconButton.func_175229_b(true);
            this.guiLockIconButton.enabled = false;
            this.lockButton.enabled = false;
        }
    }

    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id < 100 && button instanceof GuiOptionButton)
            {
                GameSettings.Options gamesettings$options = ((GuiOptionButton)button).returnEnumOptions();
                this.gameSettings.setOptionValue(gamesettings$options, 1);
                button.displayString = this.gameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
            }

            if (button.id == 108)
            {
                this.mc.theWorld.getWorldInfo().setDifficulty(EnumDifficulty.getDifficultyEnum(this.mc.theWorld.getDifficulty().getDifficultyId() + 1));
                this.lockButton.displayString = this.func_175355_a(this.mc.theWorld.getDifficulty());
            }

            if (button.id == 109)
            {
                this.mc.displayGuiScreen(new GuiYesNo(this, (new ChatComponentTranslation("difficulty.lock.title")).getFormattedText(), (new ChatComponentTranslation("difficulty.lock.question", new ChatComponentTranslation(this.mc.theWorld.getWorldInfo().getDifficulty().getDifficultyResourceKey()))).getFormattedText(), 109));
            }

            if (button.id == 110)
            {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiCustomizeSkin(this));
            }

            if (button.id == 8675309)
            {
                this.mc.entityRenderer.activateNextShader();
            }

            if (button.id == 101)
            {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiVideoSettings(this, this.gameSettings));
            }

            if (button.id == 100)
            {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiControls(this, this.gameSettings));
            }

            if (button.id == 102)
            {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiLanguage(this, this.gameSettings, this.mc.getLanguageManager()));
            }

            if (button.id == 103)
            {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new ScreenChatOptions(this, this.gameSettings));
            }

            if (button.id == 107) {
                mc.displayGuiScreen(new GuiCapeEditor(this));
            }

            if (button.id == 120) {
                mc.displayGuiScreen(new GuiGreenOptions(this));
            }

            if (button.id == 200)
            {
                mc.gameSettings.saveOptions();
                mc.displayGuiScreen(this.parentScreen);
            }

            if (button.id == 105)
            {
                mc.gameSettings.saveOptions();
                mc.displayGuiScreen(new GuiScreenResourcePacks(this));
            }

            if (button.id == 106)
            {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiScreenOptionsSounds(this, this.gameSettings));
            }

            if (button.id == 107) {
                mc.gameSettings.saveOptions();
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        final int color = ChatEffects.rainbowEffect(1, 1).getRGB();
        capeEditorButton.color = greenOptionsButton.color = color;

        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 15, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}