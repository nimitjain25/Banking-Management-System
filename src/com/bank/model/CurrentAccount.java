package com.bank.model;

/**
 * Represents a Current (Checking) Account with overdraft facility.
 */
public class CurrentAccount extends Account {
    private static final long serialVersionUID = 1L;

    public static final double DEFAULT_OVERDRAFT_LIMIT = 1000.0; // $1,000 overdraft limit

    private double overdraftLimit;

    public CurrentAccount(String accountNumber, String accountHolderName, String email, String phone,
                          String plainPin, double initialDeposit) {
        this(accountNumber, accountHolderName, email, phone, plainPin, initialDeposit, DEFAULT_OVERDRAFT_LIMIT);
    }

    public CurrentAccount(String accountNumber, String accountHolderName, String email, String phone,
                          String plainPin, double initialDeposit, double overdraftLimit) {
        super(accountNumber, accountHolderName, email, phone, plainPin, initialDeposit);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public String getAccountType() {
        return "Current Account";
    }

    @Override
    public boolean canWithdraw(double amount, StringBuilder reasonBuffer) {
        if (amount <= 0) {
            if (reasonBuffer != null) reasonBuffer.append("Withdrawal amount must be greater than zero.");
            return false;
        }
        double available = balance + overdraftLimit;
        if (amount > available) {
            if (reasonBuffer != null) {
                reasonBuffer.append(String.format("Overdraft limit exceeded. Available credit: $%.2f (Balance: $%.2f, Overdraft Limit: $%.2f).",
                        available, balance, overdraftLimit));
            }
            return false;
        }
        return true;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }
}
