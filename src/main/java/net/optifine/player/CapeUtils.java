package net.optifine.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.optifine.Config;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.regex.Pattern;

public class CapeUtils {
    private final static Minecraft mc = Minecraft.getMinecraft();
    private static final Pattern PATTERN_USERNAME = Pattern.compile("[a-zA-Z0-9_]+");

    public static void downloadCape(AbstractClientPlayer player) {
        String playerName = player.getNameClear(), capeName = mc.customCapeName != null ? mc.customCapeName : playerName;

        ResourceLocation resourcelocation = new ResourceLocation("capeof/" + playerName);
        TextureManager texturemanager = Config.getTextureManager();
        ITextureObject itextureobject = texturemanager.getTexture(resourcelocation);

        if (itextureobject instanceof SimpleTexture) {
            SimpleTexture simpletexture = (SimpleTexture)itextureobject;
            simpletexture.deleteGlTexture();
            texturemanager.deleteTexture(resourcelocation);
        }

        player.setLocationOfCape(null);
        player.setElytraOfCape(false);

        if (playerName != null && !playerName.isEmpty() && !playerName.contains("\u0000") && PATTERN_USERNAME.matcher(playerName).matches()) {
            String url = "http://s.optifine.net/capes/" + capeName + ".png";
            TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
            ITextureObject textureObject = textureManager.getTexture(resourcelocation);

            if (textureObject instanceof ThreadDownloadImageData) {
                ThreadDownloadImageData imageData = (ThreadDownloadImageData) textureObject;

                if (imageData.imageFound != null) {
                    if (imageData.imageFound) {
                        player.setLocationOfCape(resourcelocation);

                        if (imageData.getImageBuffer() instanceof CapeImageBuffer) {
                            CapeImageBuffer capeImageBuffer = (CapeImageBuffer) imageData.getImageBuffer();
                            player.setElytraOfCape(capeImageBuffer.isElytraOfCape());
                        }
                    }

                    return;
                }
            }

            CapeImageBuffer capeimagebuffer = new CapeImageBuffer(player, resourcelocation);
            ThreadDownloadImageData imageData = new ThreadDownloadImageData(null, url, null, capeimagebuffer);
            imageData.pipeline = true;
            textureManager.loadTexture(resourcelocation, imageData);

            player.setReloadCapeTimeMs(0);
        }
    }

    public static BufferedImage parseCape(BufferedImage img) {
        int i = 64, j = 32, k = img.getWidth();

        for (int l = img.getHeight(); i < k || j < l; j *= 2) {
            i *= 2;
        }

        BufferedImage bufferedimage = new BufferedImage(i, j, 2);
        Graphics graphics = bufferedimage.getGraphics();
        graphics.drawImage(img, 0, 0, null);
        graphics.dispose();
        return bufferedimage;
    }

    public static boolean isElytraCape(BufferedImage imageRaw, BufferedImage imageFixed) {
        return imageRaw.getWidth() > imageFixed.getHeight();
    }
}