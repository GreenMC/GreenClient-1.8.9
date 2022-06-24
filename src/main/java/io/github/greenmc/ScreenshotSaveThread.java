package io.github.greenmc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ScreenshotSaveThread extends Thread {

    private final BufferedImage image;
    private final File file;

    public ScreenshotSaveThread(BufferedImage shot, File file) {
        super("ScreenshotSave");
        this.image = shot;
        this.file = file;
    }

    public void run() {
        Objects.requireNonNull(image, "Image cannot be null!");
        Objects.requireNonNull(file, "File cannot be null!");

        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}