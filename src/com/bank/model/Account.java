package com.bank.model;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

/**
 * Abstract base class representing a bank account.
 */
public abstract class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    protected static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String accountNumber;
    private String accountHolderName;
    private String email;
    private String phone;
    private String salt;
    private String hashedPin;
    protected double balance;
    private final LocalDateTime creationDate;
    private AccountStatus status;
    private final List<Transaction> transactions;

    public Account(String accountNumber, String accountHolderName, String email, String phone, String plainPin, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.email = email;
        this.phone = phone;
        this.salt = generateSalt();
        this.hashedPin = hashPin(plainPin, this.salt);
        this.balance = initialDeposit;
        this.creationDate = LocalDateTime.now();
        this.status = AccountStatus.ACTIVE;
        this.transactions = new ArrayList<>();

        if (initialDeposit > 0) {
            recordTransaction(TransactionType.DEPOSIT, initialDeposit, "Initial account opening deposit", null);
        }
    }

    public abstract String getAccountType();

    /**
     * Checks if the requested withdrawal amount is permitted by account rules.
     * @param amount the withdrawal amount
     * @param reasonBuffer out-parameter for failure explanation
     * @return true if permitted, false otherwise
     */
    public abstract boolean canWithdraw(double amount, StringBuilder reasonBuffer);

    public synchronized boolean deposit(double amount, String description) {
        if (status != AccountStatus.ACTIVE) {
            return false;
        }
        if (amount <= 0) {
            return false;
        }
        this.balance += amount;
        recordTransaction(TransactionType.DEPOSIT, amount, description, null);
        return true;
    }

    public synchronized boolean withdraw(double amount, String description) {
        if (status != AccountStatus.ACTIVE) {
            return false;
        }
        StringBuilder reason = new StringBuilder();
        if (!canWithdraw(amount, reason)) {
            return false;
        }
        this.balance -= amount;
        recordTransaction(TransactionType.WITHDRAWAL, amount, description, null);
        return true;
    }

    public synchronized void recordTransaction(TransactionType type, double amount, String description, String referenceAccount) {
        String txId = "TXN" + System.currentTimeMillis() + (int)(Math.random() * 1000);
        Transaction tx = new Transaction(txId, LocalDateTime.now(), type, amount, this.balance, description, referenceAccount);
        this.transactions.add(tx);
    }

    public boolean verifyPin(String plainPin) {
        if (plainPin == null || this.salt == null || this.hashedPin == null) {
            return false;
        }
        String computed = hashPin(plainPin, this.salt);
        return computed.equals(this.hashedPin);
    }

    public void changePin(String newPin) {
        this.salt = generateSalt();
        this.hashedPin = hashPin(newPin, this.salt);
    }

    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    private static String hashPin(String pin, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(Base64.getDecoder().decode(salt));
            byte[] hash = digest.digest(pin.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 hashing algorithm not available", e);
        }
    }

    // Getters and Setters
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public synchronized double getBalance() {
        return balance;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getFormattedCreationDate() {
        return creationDate.format(DATE_FORMATTER);
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }
}
