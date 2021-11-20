package fr.lessagasmp3.core.common.constant;

import org.apache.tika.parser.txt.CharsetDetector;

public class Strings {

    public static final String EMPTY = "";

    private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final CharsetDetector DETECTOR = new CharsetDetector();

    public static String randomString() {
        int count = 12;
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public static String convertToUtf8(String str) {
        return DETECTOR.getString(str.getBytes(), "utf-8");
    }

}
