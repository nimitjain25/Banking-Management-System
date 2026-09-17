package com.bank.service;

import com.bank.model.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Core business service coordinating all banking operations.
 */
public class BankService {
    private final Map<String, Account> accounts;
    private final StorageService storageService;
    private final AuthService authService;
    private final AtomicInteger savingsSeq = new AtomicInteger(1005);
    private final AtomicInteger currentSeq = new AtomicInteger(2005);

    public BankService(StorageService storageService, AuthService authService) {
        this.storageService = storageService;
        this.authService = authService;
        this.accounts = storageService.loadAccounts();
    }

    public synchronized SavingsAccount openSavingsAccount(String holderName, String email, String phone,
                                                         String pin, double initialDeposit) {
        if (initialDeposit < SavingsAccount.DEFAULT_MINIMUM_BALANCE) {
            throw new IllegalArgumentException(String.format("Initial deposit must be at least $%.2f",
                    SavingsAccount.DEFAULT_MINIMUM_BALANCE));
        }
        String accNum = "SB-" + savingsSeq.getAndIncrement();
        SavingsAccount account = new SavingsAccount(accNum, holderName, email, phone, pin, initialDeposit);
        accounts.put(accNum, account);
        storageService.saveAccounts(accounts);
        return account;
    }

    public synchronized CurrentAccount openCurrentAccount(String holderName, String email, String phone,
                                                         String pin, double initialDeposit, double overdraftLimit) {
        if (initialDeposit < 0) {
            throw new IllegalArgumentException("Initial deposit cannot be negative.");
        }
        String accNum = "CA-" + currentSeq.getAndIncrement();
        CurrentAccount account = new CurrentAccount(accNum, holderName, email, phone, pin, initialDeposit, overdraftLimit);
        accounts.put(accNum, account);
        storageService.saveAccounts(accounts);
        return account;
    }

    public Account getAccount(String accountNumber) {
        if (accountNumber == null) return null;
        return accounts.get(accountNumber.trim().toUpperCase());
    }

    public Collection<Account> getAllAccounts() {
        return Collections.unmodifiableCollection(accounts.values());
    }

    public synchronized boolean deposit(String accountNumber, double amount, String description) {
        Account account = getAccount(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be strictly positive.");
        }
        boolean success = account.deposit(amount, description != null ? description : "Cash/Direct Deposit");
        if (success) {
            storageService.saveAccounts(accounts);
        }
        return success;
    }

    public synchronized boolean withdraw(String accountNumber, double amount, String pin, String description) {
        Account account = getAccount(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        if (!authService.authenticateAccount(account, pin)) {
            throw new SecurityException("Invalid PIN entered.");
        }
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account is " + account.getStatus() + " and cannot process withdrawals.");
        }
        StringBuilder reason = new StringBuilder();
        if (!account.canWithdraw(amount, reason)) {
            throw new IllegalArgumentException(reason.toString());
        }
        boolean success = account.withdraw(amount, description != null ? description : "Cash Withdrawal");
        if (success) {
            storageService.saveAccounts(accounts);
        }
        return success;
    }

    /**
     * Atomically transfers funds from one account to another.
     */
    public synchronized boolean transfer(String fromAccNum, String toAccNum, double amount, String pin, String note) {
        if (fromAccNum.equalsIgnoreCase(toAccNum)) {
            throw new IllegalArgumentException("Sender and beneficiary accounts cannot be the same.");
        }
        Account fromAccount = getAccount(fromAccNum);
        Account toAccount = getAccount(toAccNum);

        if (fromAccount == null) {
            throw new IllegalArgumentException("Sender account not found: " + fromAccNum);
        }
        if (toAccount == null) {
            throw new IllegalArgumentException("Beneficiary account not found: " + toAccNum);
        }
        if (!authService.authenticateAccount(fromAccount, pin)) {
            throw new SecurityException("Invalid PIN for sender account.");
        }
        if (fromAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Sender account is " + fromAccount.getStatus() + ".");
        }
        if (toAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Beneficiary account is " + toAccount.getStatus() + ".");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be strictly positive.");
        }

        StringBuilder reason = new StringBuilder();
        if (!fromAccount.canWithdraw(amount, reason)) {
            throw new IllegalArgumentException("Transfer rejected: " + reason);
        }

        // Perform atomic transfer
        fromAccount.withdraw(amount, "Transfer to " + toAccount.getAccountNumber() + (note != null ? " (" + note + ")" : ""));
        toAccount.deposit(amount, "Transfer from " + fromAccount.getAccountNumber() + (note != null ? " (" + note + ")" : ""));

        storageService.saveAccounts(accounts);
        return true;
    }

    public synchronized boolean changePin(String accountNumber, String oldPin, String newPin) {
        Account account = getAccount(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        if (!authService.authenticateAccount(account, oldPin)) {
            throw new SecurityException("Current PIN is incorrect.");
        }
        if (newPin == null || newPin.trim().length() < 4) {
            throw new IllegalArgumentException("New PIN must be at least 4 digits.");
        }
        account.changePin(newPin.trim());
        storageService.saveAccounts(accounts);
        return true;
    }

    public synchronized void setAccountStatus(String accountNumber, AccountStatus status) {
        Account account = getAccount(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        account.setStatus(status);
        storageService.saveAccounts(accounts);
    }

    public synchronized int applyInterestToAllSavings() {
        int count = 0;
        for (Account acc : accounts.values()) {
            if (acc instanceof SavingsAccount && acc.getStatus() == AccountStatus.ACTIVE) {
                ((SavingsAccount) acc).applyMonthlyInterest();
                count++;
            }
        }
        storageService.saveAccounts(accounts);
        return count;
    }

    public double getTotalBankLiquidity() {
        return accounts.values().stream().mapToDouble(Account::getBalance).sum();
    }

    public int getTotalAccountsCount() {
        return accounts.size();
    }
}
