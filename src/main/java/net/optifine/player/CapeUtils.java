package net.optifine.player;

import com.google.common.io.Files;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.optifine.Config;
import org.apache.commons.io.Charsets;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CapeUtils {

    private final static Minecraft mc;
    private final static Pattern PATTERN_USERNAME;
    private final static List<String> customCapeOwners;

    static {
        mc = Minecraft.getMinecraft();
        PATTERN_USERNAME = Pattern.compile("[a-zA-Z0-9_]+");
        customCapeOwners = new ArrayList<>();

        try {
            ReadableByteChannel channel = Channels.newChannel(new URL("https://raw.githubusercontent.com/GreenMC/CustomCapes/master/capes.txt").openStream());
            FileOutputStream stream = new FileOutputStream("capes.txt");

            stream.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);

            channel.close();
            stream.close();

            File temp = new File("capes.txt");

            customCapeOwners.addAll(Files.readLines(temp, Charsets.UTF_8));

            temp.delete();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void downloadCape(AbstractClientPlayer player) {
        if (player == null) return;

        String playerName = player.getNameClear(), capeName = mc.customCapeName, capeUrl = mc.customCapeUrl;

        ResourceLocation resourcelocation = new ResourceLocation("capeof/" + playerName);
        TextureManager texturemanager = Config.getTextureManager();
        ITextureObject itextureobject = texturemanager.getTexture(resourcelocation);

        if (itextureobject instanceof SimpleTexture) {
            SimpleTexture simpletexture = (SimpleTexture)itextureobject;
            simpletexture.deleteGlTexture();
            texturemanager.deleteTexture(resourcelocation);
        }

        player.setLocationOfCape(null);

        if (playerName != null && !playerName.isEmpty() && !playerName.contains("\u0000") && PATTERN_USERNAME.matcher(playerName).matches()) {
            String url;

            if (capeUrl != null) {
                url = capeUrl;
            } else if (capeName != null) {
                url = "http://s.optifine.net/capes/" + capeName + ".png";
            } else if (isCustomCape(playerName)) {
                url = "https://raw.githubusercontent.com/GreenMC/CustomCapes/master/" + getCustomName(playerName) + ".png";
            } else {
                return;
            }

            TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
            ITextureObject textureObject = textureManager.getTexture(resourcelocation);

            if (textureObject instanceof ThreadDownloadImageData) {
                ThreadDownloadImageData imageData = (ThreadDownloadImageData) textureObject;

                if (imageData.imageFound != null) {
                    if (imageData.imageFound) {
                        player.setLocationOfCape(resourcelocation);
                    }

                    return;
                } else {
                    player.setLocationOfCape(null);
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

    private static boolean isCustomCape(String name) {
        return customCapeOwners.contains(name);
    }

    private static String getCustomName(String name) {
        if (isCustomCape(name)) {
            name = name.toLowerCase(java.util.Locale.ENGLISH);
            return name.endsWith("_") ? name + "cape" : name + "_cape";
        }
        return null;
    }
}