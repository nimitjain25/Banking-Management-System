package com.bank.model;

/**
 * Represents a Savings Account with interest rate and minimum balance restrictions.
 */
public class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;

    public static final double DEFAULT_INTEREST_RATE = 4.0; // 4.0% per annum
    public static final double DEFAULT_MINIMUM_BALANCE = 100.0;

    private final double interestRate;
    private final double minimumBalance;

    public SavingsAccount(String accountNumber, String accountHolderName, String email, String phone,
                          String plainPin, double initialDeposit) {
        this(accountNumber, accountHolderName, email, phone, plainPin, initialDeposit,
                DEFAULT_INTEREST_RATE, DEFAULT_MINIMUM_BALANCE);
    }

    public SavingsAccount(String accountNumber, String accountHolderName, String email, String phone,
                          String plainPin, double initialDeposit, double interestRate, double minimumBalance) {
        super(accountNumber, accountHolderName, email, phone, plainPin, initialDeposit);
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
    }

    @Override
    public String getAccountType() {
        return "Savings Account";
    }

    @Override
    public boolean canWithdraw(double amount, StringBuilder reasonBuffer) {
        if (amount <= 0) {
            if (reasonBuffer != null) reasonBuffer.append("Withdrawal amount must be greater than zero.");
            return false;
        }
        if (balance - amount < minimumBalance) {
            if (reasonBuffer != null) {
                reasonBuffer.append(String.format("Insufficient funds. Minimum balance of $%.2f must be maintained. Current balance: $%.2f",
                        minimumBalance, balance));
            }
            return false;
        }
        return true;
    }

    /**
     * Calculates and credits monthly interest to the savings balance.
     * @return the interest amount credited
     */
    public synchronized double applyMonthlyInterest() {
        if (getStatus() != AccountStatus.ACTIVE || balance <= 0) {
            return 0.0;
        }
        double monthlyRate = (interestRate / 100.0) / 12.0;
        double interest = Math.round(balance * monthlyRate * 100.0) / 100.0;
        if (interest > 0) {
            this.balance += interest;
            recordTransaction(TransactionType.INTEREST_CREDIT, interest,
                    String.format("Monthly interest credit (%.2f%% p.a.)", interestRate), null);
        }
        return interest;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public double getMinimumBalance() {
        return minimumBalance;
    }
}
