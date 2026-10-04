package com.ithena.jsplogin.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ithena.jsplogin.TestImages;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ImageTypesTest {

    @Test
    void detectsCommonImageFormatsByTheirMagicBytes() {
        assertEquals(Optional.of("image/png"), ImageTypes.detect(TestImages.png()));
        assertEquals(Optional.of("image/jpeg"), ImageTypes.detect(bytes(0xFF, 0xD8, 0xFF, 0xE0, 0, 0, 0, 0, 0, 0, 0, 0)));
        assertEquals(Optional.of("image/gif"), ImageTypes.detect("GIF89a......".getBytes(StandardCharsets.US_ASCII)));
        assertEquals(Optional.of("image/webp"), ImageTypes.detect("RIFF\0\0\0\0WEBPVP8 ".getBytes(StandardCharsets.US_ASCII)));
    }

    @Test
    void rejectsNonImagesEvenIfTheyClaimToBeImages() {
        assertTrue(ImageTypes.detect("<html><script>alert(1)</script></html>".getBytes(StandardCharsets.UTF_8)).isEmpty());
        assertTrue(ImageTypes.detect(new byte[3]).isEmpty());
        assertTrue(ImageTypes.detect(null).isEmpty());
    }

    private static byte[] bytes(int... values) {
        byte[] out = new byte[values.length];
        for (int i = 0; i < values.length; i++) {
            out[i] = (byte) values[i];
        }
        return out;
    }
}
