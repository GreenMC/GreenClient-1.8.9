package net.minecraft.client.gui;

import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GuiWinGame extends GuiScreen {

    private int totalScrollLength, time;

    private final boolean poem;
    private final float scrollSpeed;
    private final Logger logger;
    private final List<String> lines;
    private final ResourceLocation minecraftLogo, vignetteTexture;

    public GuiWinGame(boolean poem) {
        this.poem = poem;
        this.scrollSpeed = poem ? .75F : 1F;
        this.logger = LogManager.getLogger();
        this.lines = new ArrayList<>();
        this.minecraftLogo = new ResourceLocation("textures/gui/title/minecraft.png");
        this.vignetteTexture = new ResourceLocation("textures/misc/vignette.png");
    }

    public void updateScreen() {
        MusicTicker musicTicker = mc.getMusicTicker();
        SoundHandler soundHandler = mc.getSoundHandler();

        if (time == 0) {
            musicTicker.stopCurrentMusic();
            musicTicker.playMusic(MusicTicker.MusicType.CREDITS);
            soundHandler.resumeSounds();
        }

        soundHandler.update();
        ++time;
        float f = (float) (totalScrollLength + height + height + 24) / scrollSpeed;

        if ((float) time > f) {

            sendRespawnPacket();
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            sendRespawnPacket();
        }
    }

    private void sendRespawnPacket() {
        if (mc.isIntegratedServerRunning()) {
            mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
        }

        mc.displayGuiScreen(null);
    }

    public boolean doesGuiPauseGame() {
        return true;
    }

    public void initGui() {
        try {
            int i = 274;

            if (poem) {
                String s;
                String s1 = "" + EnumChatFormatting.WHITE + EnumChatFormatting.OBFUSCATED + EnumChatFormatting.GREEN + EnumChatFormatting.AQUA;
                InputStream inputstream = this.mc.getResourceManager().getResource(new ResourceLocation("texts/end.txt")).getInputStream();
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, Charsets.UTF_8));
                Random random = new Random(8124371L);

                while ((s = bufferedreader.readLine()) != null) {
                    String s2;
                    String s3;

                    for (s = s.replaceAll("PLAYERNAME", this.mc.getSession().getUsername()); s.contains(s1); s = s2 + EnumChatFormatting.WHITE + EnumChatFormatting.OBFUSCATED + "XXXXXXXX".substring(0, random.nextInt(4) + 3) + s3) {
                        int j = s.indexOf(s1);
                        s2 = s.substring(0, j);
                        s3 = s.substring(j + s1.length());
                    }

                    this.lines.addAll(this.mc.fontRendererObj.listFormattedStringToWidth(s, i));
                    this.lines.add("");
                }

                inputstream.close();

                for (int k = 0; k < 8; ++k) {
                    lines.add("");
                }
            }

            InputStream inputstream = this.mc.getResourceManager().getResource(new ResourceLocation("texts/credits.txt")).getInputStream();
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, Charsets.UTF_8));
            String line;

            while ((line = bufferedreader.readLine()) != null) {
                line = line.replaceAll("PLAYERNAME", mc.getSession().getUsername());
                line = line.replaceAll("\t", "    ");
                lines.addAll(mc.fontRendererObj.listFormattedStringToWidth(line, i));
                lines.add("");
            }

            inputstream.close();
            this.totalScrollLength = lines.size() * 12;
        } catch (Exception exception) {
            logger.error("Couldn't load credits", exception);
        }
    }

    private void drawWinGameScreen(float partialTicks) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        int i = this.width;
        float f = 0.0F - ((float)this.time + partialTicks) * 0.5F * this.scrollSpeed;
        float f1 = (float)this.height - ((float)this.time + partialTicks) * 0.5F * this.scrollSpeed;
        float f2 = 0.015625F;
        float f3 = ((float)this.time + partialTicks - 0.0F) * 0.02F;
        float f4 = (float)(this.totalScrollLength + this.height + this.height + 24) / this.scrollSpeed;
        float f5 = (f4 - 20.0F - ((float)this.time + partialTicks)) * 0.005F;

        if (f5 < f3) {
            f3 = f5;
        }

        if (f3 > 1.0F) {
            f3 = 1.0F;
        }

        f3 = f3 * f3;
        f3 = f3 * 96.0F / 255.0F;
        worldrenderer.pos(0.0D, this.height, this.zLevel).tex(0.0D, (f * f2)).color(f3, f3, f3, 1.0F).endVertex();
        worldrenderer.pos(i, this.height, this.zLevel).tex(((float)i * f2), (f * f2)).color(f3, f3, f3, 1.0F).endVertex();
        worldrenderer.pos(i, 0.0D, this.zLevel).tex(((float)i * f2), (f1 * f2)).color(f3, f3, f3, 1.0F).endVertex();
        worldrenderer.pos(0.0D, 0.0D, this.zLevel).tex(0.0D, (f1 * f2)).color(f3, f3, f3, 1.0F).endVertex();
        tessellator.draw();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawWinGameScreen(partialTicks);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        int i = 274;
        int j = this.width / 2 - i / 2;
        int k = this.height + 50;
        float f = -((float)this.time + partialTicks) * this.scrollSpeed;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, f, 0.0F);
        this.mc.getTextureManager().bindTexture(minecraftLogo);

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        drawTexturedModalRect(j, k, 0, 0, 155, 44);
        drawTexturedModalRect(j + 155, k, 0, 45, 155, 44);

        GlStateManager.disableBlend();

        int l = k + 200;

        for (int i1 = 0; i1 < this.lines.size(); ++i1)
        {
            if (i1 == this.lines.size() - 1)
            {
                float f1 = (float)l + f - (float)(this.height / 2 - 6);

                if (f1 < 0.0F)
                {
                    GlStateManager.translate(0.0F, -f1, 0.0F);
                }
            }

            if ((float)l + f + 12.0F + 8.0F > 0.0F && (float)l + f < (float) height) {
                String s = this.lines.get(i1);

                fontRendererObj.fontRandom.setSeed((long)i1 * 4238972211L + (long) (time / 4));
                fontRendererObj.drawStringWithShadow(s, (float)(j + (i - this.fontRendererObj.getStringWidth(s)) / 2), (float) l, 16777215);
            }

            l += 12;
        }

        GlStateManager.popMatrix();
        this.mc.getTextureManager().bindTexture(vignetteTexture);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(0, 769);
        int j1 = this.width;
        int k1 = this.height;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0D, k1, this.zLevel).tex(0.0D, 1.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(j1, k1, this.zLevel).tex(1.0D, 1.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(j1, 0.0D, this.zLevel).tex(1.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(0.0D, 0.0D, this.zLevel).tex(0.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}