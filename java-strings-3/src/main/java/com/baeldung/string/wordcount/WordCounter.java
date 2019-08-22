package com.baeldung.string.wordcount;

import java.util.StringTokenizer;

public class WordCounter {
    static final int WORD = 0;
    static final int SEPARATOR = 1;

    public static int countWordsUsingRegex(String arg) {
        if (arg == null) {
            return 0;
        }
        final String[] words = arg.split("[\\pP\\s&&[^']]+");
        return words.length;
    }

    public static int countWordsUsingTokenizer(String arg) {
        if (arg == null) {
            return 0;
        }
        final StringTokenizer stringTokenizer = new StringTokenizer(arg);
        return stringTokenizer.countTokens();
    }

    public static int countWordsManually(String arg) {
        if (arg == null) {
            return 0;
        }
        int flag = SEPARATOR;
        int count = 0;
        int stringLength = arg.length();
        int characterCounter = 0;

        while (characterCounter < stringLength) {
            if ((Character.isLetter(arg.charAt(characterCounter)) || isAllowedWordPunct(arg.charAt(characterCounter))) && flag == SEPARATOR) {
                flag = WORD;
                count++;
            } else if (!Character.isLetter(arg.charAt(characterCounter))) {
                flag = SEPARATOR;
            }
            characterCounter++;
        }
        return count;
    }

    private static boolean isAllowedWordPunct(char charAt) {
        return charAt == '\'';
    }
}
