package com.ithena.jsplogin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/** Small real images for tests. */
public final class TestImages {

    private TestImages() {
    }

    public static byte[] png() {
        BufferedImage image = new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
