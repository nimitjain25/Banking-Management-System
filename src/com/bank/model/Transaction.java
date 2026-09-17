package com.bank.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single financial transaction record.
 */
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String transactionId;
    private final LocalDateTime timestamp;
    private final TransactionType type;
    private final double amount;
    private final double balanceAfter;
    private final String description;
    private final String referenceAccount;

    public Transaction(String transactionId, LocalDateTime timestamp, TransactionType type,
                       double amount, double balanceAfter, String description, String referenceAccount) {
        this.transactionId = transactionId;
        this.timestamp = timestamp;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.referenceAccount = referenceAccount != null ? referenceAccount : "N/A";
    }

    public String getTransactionId() {
        return transactionId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getFormattedTimestamp() {
        return timestamp.format(FORMATTER);
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public String getDescription() {
        return description;
    }

    public String getReferenceAccount() {
        return referenceAccount;
    }

    @Override
    public String toString() {
        return String.format("[%s] %-14s | %-16s | Amount: $%-10.2f | Balance: $%-10.2f | %s (Ref: %s)",
                transactionId, getFormattedTimestamp(), type, amount, balanceAfter, description, referenceAccount);
    }
}
