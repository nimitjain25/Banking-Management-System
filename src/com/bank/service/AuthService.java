package com.bank.service;

import com.bank.model.Account;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Handles security, authentication, and password/PIN validations.
 */
public class AuthService {
    // Admin credentials
    private static final String ADMIN_USERNAME = "admin";
    // Fixed salt for admin demonstration:
    private static final String ADMIN_SALT = "B@nkS3cur1tyS@lt2026";
    // Hash of "admin123" with ADMIN_SALT
    private static final String ADMIN_HASH = hashPassword("admin123", ADMIN_SALT);

    public boolean authenticateAdmin(String username, String password) {
        if (username == null || password == null) return false;
        if (!ADMIN_USERNAME.equalsIgnoreCase(username.trim())) return false;
        String computed = hashPassword(password, ADMIN_SALT);
        return ADMIN_HASH.equals(computed);
    }

    public boolean authenticateAccount(Account account, String plainPin) {
        if (account == null || plainPin == null) return false;
        return account.verifyPin(plainPin);
    }

    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }
}
