package com.bank.service;

import com.bank.model.Account;
import com.bank.model.SavingsAccount;
import com.bank.model.CurrentAccount;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles persistent binary storage of bank accounts and system data.
 */
public class StorageService {
    private final String dataFilePath;

    public StorageService(String dataFilePath) {
        this.dataFilePath = dataFilePath;
        ensureDirectoryExists();
    }

    private void ensureDirectoryExists() {
        try {
            Path path = Paths.get(dataFilePath).getParent();
            if (path != null && !Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not create data storage directory: " + e.getMessage());
        }
    }

    /**
     * Saves accounts map to persistent disk storage.
     */
    public synchronized boolean saveAccounts(Map<String, Account> accounts) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFilePath))) {
            oos.writeObject(accounts);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to persist bank data: " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads accounts map from disk storage, or initializes sample data if file does not exist.
     */
    @SuppressWarnings("unchecked")
    public synchronized Map<String, Account> loadAccounts() {
        File file = new File(dataFilePath);
        if (!file.exists()) {
            return initializeSampleData();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                return new ConcurrentHashMap<>((Map<String, Account>) obj);
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not load existing data (" + e.getMessage() + "). Initializing fresh demo data.");
        }
        return initializeSampleData();
    }

    private Map<String, Account> initializeSampleData() {
        Map<String, Account> demo = new ConcurrentHashMap<>();

        // Demo Savings Account 1: Alice Johnson
        SavingsAccount alice = new SavingsAccount(
                "SB-1001",
                "Alice Johnson",
                "alice.j@example.com",
                "+1-555-0192",
                "1234",
                5500.00
        );
        alice.deposit(1200.00, "Salary direct deposit");
        alice.withdraw(350.00, "Grocery and utility bills");

        // Demo Current Account 2: Bob Martinez (Business)
        CurrentAccount bob = new CurrentAccount(
                "CA-2001",
                "Bob Martinez (Nexus Corp)",
                "bob.m@nexuscorp.com",
                "+1-555-0144",
                "4321",
                12500.00,
                3000.00
        );
        bob.deposit(4000.00, "Client invoice payment #INV-882");

        // Demo Savings Account 3: Charlie Davis
        SavingsAccount charlie = new SavingsAccount(
                "SB-1002",
                "Charlie Davis",
                "charlie.d@example.com",
                "+1-555-0177",
                "9999",
                1850.00
        );

        demo.put(alice.getAccountNumber(), alice);
        demo.put(bob.getAccountNumber(), bob);
        demo.put(charlie.getAccountNumber(), charlie);

        saveAccounts(demo);
        return demo;
    }
}
