package com.ithena.jsplogin.validation;

import java.util.Optional;

/**
 * Detects image types from their first bytes ("magic numbers") instead of trusting the
 * browser-supplied content type, so only real images are stored and served back.
 */
public final class ImageTypes {

    private ImageTypes() {
    }

    public static Optional<String> detect(byte[] b) {
        if (b == null || b.length < 12) {
            return Optional.empty();
        }
        if (u(b[0]) == 0xFF && u(b[1]) == 0xD8 && u(b[2]) == 0xFF) {
            return Optional.of("image/jpeg");
        }
        if (u(b[0]) == 0x89 && b[1] == 'P' && b[2] == 'N' && b[3] == 'G') {
            return Optional.of("image/png");
        }
        if (b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8') {
            return Optional.of("image/gif");
        }
        if (b[0] == 'R' && b[1] == 'I' && b[2] == 'F' && b[3] == 'F'
                && b[8] == 'W' && b[9] == 'E' && b[10] == 'B' && b[11] == 'P') {
            return Optional.of("image/webp");
        }
        return Optional.empty();
    }

    private static int u(byte value) {
        return value & 0xFF;
    }
}
