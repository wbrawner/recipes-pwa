package com.wbrawner.recipes;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public final class Utils {
    private Utils() {}

    private static final Random random;

    static {
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String randomString(int length) {
        var s = new StringBuilder();
        for (int i = 0; i < length; i++) {
            s.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return s.toString();
    }
}
