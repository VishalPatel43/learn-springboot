package com.springboot.coding.securityApplication.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class PasswordUtil {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()";

    private final SecureRandom random = new SecureRandom();


    public String generateRandomPassword() {
        int length = 8; // Fixed length of 8 characters

        // Ensure password has at least one of each type
        List<Character> passwordChars = new ArrayList<>();
        passwordChars.add(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        passwordChars.add(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        passwordChars.add(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        passwordChars.add(SYMBOLS.charAt(random.nextInt(SYMBOLS.length())));

        // Fill the remaining characters randomly from all character types
        String allCharacters = UPPERCASE + LOWERCASE + NUMBERS + SYMBOLS;
        for (int i = 4; i < length; i++)
            passwordChars.add(allCharacters.charAt(random.nextInt(allCharacters.length())));

        // Shuffle to ensure random order
        Collections.shuffle(passwordChars);

        // Convert list to a string
        StringBuilder password = new StringBuilder();
        for (char c : passwordChars)
            password.append(c);

        return password.toString();
    }

}
