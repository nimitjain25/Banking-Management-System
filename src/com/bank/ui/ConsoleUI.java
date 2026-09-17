package com.bank.ui;

import com.bank.model.*;
import com.bank.service.AuthService;
import com.bank.service.BankService;
import com.bank.report.ProjectReportGenerator;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * Interactive Console User Interface for Customer and Administrator banking operations.
 */
public class ConsoleUI {
    private final BankService bankService;
    private final AuthService authService;
    private final Scanner scanner;

    public ConsoleUI(BankService bankService, AuthService authService) {
        this.bankService = bankService;
        this.authService = authService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean exit = false;
        while (!exit) {
            printHeader();
            System.out.println("  ========================================================");
            System.out.println("  |                  MAIN CONTROL PORTAL                 |");
            System.out.println("  ========================================================");
            System.out.println("  [1] Customer Banking Portal (Login / Self-Service)");
            System.out.println("  [2] Administrator Portal (Staff Management & Audits)");
            System.out.println("  [3] View Sample Demo Accounts & Quick Guide");
            System.out.println("  [4] Generate Official Project Report PDF");
            System.out.println("  [5] Exit System");
            System.out.println("  --------------------------------------------------------");
            System.out.print("  Select an option [1-5]: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    handleCustomerPortal();
                    break;
                case "2":
                    handleAdminPortal();
                    break;
                case "3":
                    showDemoAccounts();
                    break;
                case "4":
                    generateReportAction();
                    break;
                case "5":
                    System.out.println("\n  Thank you for using the Banking Management System. Goodbye!\n");
                    exit = true;
                    break;
                default:
                    System.out.println("  [!] Invalid selection. Please enter a number between 1 and 5.");
                    pressEnterToContinue();
            }
        }
    }

    // ==========================================
    // Customer Portal
    // ==========================================
    private void handleCustomerPortal() {
        System.out.println("\n  --- CUSTOMER LOGIN ---");
        System.out.print("  Enter Account Number (e.g. SB-1001, CA-2001): ");
        String accNum = scanner.nextLine().trim();

        Account account = bankService.getAccount(accNum);
        if (account == null) {
            System.out.println("  [!] Account does not exist!");
            pressEnterToContinue();
            return;
        }

        System.out.print("  Enter 4-Digit Security PIN: ");
        String pin = scanner.nextLine().trim();

        if (!authService.authenticateAccount(account, pin)) {
            System.out.println("  [!] Authentication Failed: Invalid PIN.");
            pressEnterToContinue();
            return;
        }

        if (account.getStatus() == AccountStatus.FROZEN) {
            System.out.println("  [!] Account is FROZEN. Please contact your branch administrator.");
            pressEnterToContinue();
            return;
        } else if (account.getStatus() == AccountStatus.CLOSED) {
            System.out.println("  [!] Account is CLOSED.");
            pressEnterToContinue();
            return;
        }

        boolean customerSession = true;
        while (customerSession) {
            System.out.println("\n  ========================================================");
            System.out.printf("   WELCOME, %s (%s)\n", account.getAccountHolderName(), account.getAccountNumber());
            System.out.printf("   Account Type: %-18s Current Balance: $%,.2f\n", account.getAccountType(), account.getBalance());
            System.out.println("  ========================================================");
            System.out.println("  [1] Check Account Details & Balance");
            System.out.println("  [2] Deposit Funds");
            System.out.println("  [3] Withdraw Cash");
            System.out.println("  [4] Transfer Funds to Another Account");
            System.out.println("  [5] View Transaction Statement");
            System.out.println("  [6] Change Security PIN");
            System.out.println("  [7] Logout to Main Menu");
            System.out.println("  --------------------------------------------------------");
            System.out.print("  Choose an action [1-7]: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    displayAccountSummary(account);
                    break;
                case "2":
                    customerDeposit(account);
                    break;
                case "3":
                    customerWithdraw(account);
                    break;
                case "4":
                    customerTransfer(account);
                    break;
                case "5":
                    displayStatement(account);
                    break;
                case "6":
                    customerChangePin(account);
                    break;
                case "7":
                    System.out.println("  Logged out successfully.");
                    customerSession = false;
                    break;
                default:
                    System.out.println("  [!] Invalid choice.");
            }
        }
    }

    private void displayAccountSummary(Account account) {
        System.out.println("\n  ---------------- ACCOUNT SUMMARY ----------------");
        System.out.println("  Account Number : " + account.getAccountNumber());
        System.out.println("  Holder Name    : " + account.getAccountHolderName());
        System.out.println("  Account Type   : " + account.getAccountType());
        System.out.printf("  Current Balance: $%,.2f\n", account.getBalance());
        System.out.println("  Email Address  : " + account.getEmail());
        System.out.println("  Phone Number   : " + account.getPhone());
        System.out.println("  Created Date   : " + account.getFormattedCreationDate());
        System.out.println("  Account Status : " + account.getStatus());
        if (account instanceof SavingsAccount) {
            SavingsAccount sa = (SavingsAccount) account;
            System.out.printf("  Interest Rate  : %.2f%% per annum\n", sa.getInterestRate());
            System.out.printf("  Min. Required  : $%,.2f\n", sa.getMinimumBalance());
        } else if (account instanceof CurrentAccount) {
            CurrentAccount ca = (CurrentAccount) account;
            System.out.printf("  Overdraft Limit: $%,.2f\n", ca.getOverdraftLimit());
            System.out.printf("  Available Funds: $%,.2f\n", (ca.getBalance() + ca.getOverdraftLimit()));
        }
        System.out.println("  -------------------------------------------------");
        pressEnterToContinue();
    }

    private void customerDeposit(Account account) {
        System.out.print("\n  Enter deposit amount ($): ");
        try {
            double amt = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("  Enter memo / description (optional): ");
            String memo = scanner.nextLine().trim();
            if (memo.isEmpty()) memo = "Cash Deposit";

            bankService.deposit(account.getAccountNumber(), amt, memo);
            System.out.printf("  [SUCCESS] Successfully deposited $%,.2f. New Balance: $%,.2f\n", amt, account.getBalance());
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid amount entered.");
        } catch (Exception e) {
            System.out.println("  [!] Deposit failed: " + e.getMessage());
        }
        pressEnterToContinue();
    }

    private void customerWithdraw(Account account) {
        System.out.print("\n  Enter withdrawal amount ($): ");
        try {
            double amt = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("  Enter your PIN to authorize: ");
            String pin = scanner.nextLine().trim();

            bankService.withdraw(account.getAccountNumber(), amt, pin, "ATM / Counter Cash Withdrawal");
            System.out.printf("  [SUCCESS] Successfully withdrew $%,.2f. New Balance: $%,.2f\n", amt, account.getBalance());
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid amount entered.");
        } catch (Exception e) {
            System.out.println("  [!] Withdrawal failed: " + e.getMessage());
        }
        pressEnterToContinue();
    }

    private void customerTransfer(Account account) {
        System.out.println("\n  --- INTER-ACCOUNT FUND TRANSFER ---");
        System.out.print("  Enter Beneficiary Account Number: ");
        String toAcc = scanner.nextLine().trim();

        System.out.print("  Enter Transfer Amount ($): ");
        try {
            double amt = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("  Enter transfer reference note: ");
            String note = scanner.nextLine().trim();
            System.out.print("  Enter your PIN to authorize transfer: ");
            String pin = scanner.nextLine().trim();

            bankService.transfer(account.getAccountNumber(), toAcc, amt, pin, note);
            System.out.printf("  [SUCCESS] Transferred $%,.2f to account %s. Remaining Balance: $%,.2f\n",
                    amt, toAcc.toUpperCase(), account.getBalance());
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid amount entered.");
        } catch (Exception e) {
            System.out.println("  [!] Transfer failed: " + e.getMessage());
        }
        pressEnterToContinue();
    }

    private void displayStatement(Account account) {
        List<Transaction> txs = account.getTransactions();
        System.out.println("\n  ======================= TRANSACTION STATEMENT =======================");
        System.out.printf("   Account: %s | Holder: %s\n", account.getAccountNumber(), account.getAccountHolderName());
        System.out.println("  ---------------------------------------------------------------------");
        if (txs.isEmpty()) {
            System.out.println("  No transactions found.");
        } else {
            System.out.printf("  %-16s | %-19s | %-13s | %-10s | %-10s | %s\n",
                    "TX ID", "Date & Time", "Type", "Amount", "Balance", "Memo / Reference");
            System.out.println("  ---------------------------------------------------------------------");
            for (Transaction tx : txs) {
                System.out.printf("  %-16s | %-19s | %-13s | $%-9.2f | $%-9.2f | %s\n",
                        tx.getTransactionId(), tx.getFormattedTimestamp(), tx.getType(),
                        tx.getAmount(), tx.getBalanceAfter(), tx.getDescription());
            }
        }
        System.out.println("  =====================================================================");
        pressEnterToContinue();
    }

    private void customerChangePin(Account account) {
        System.out.print("\n  Enter current PIN: ");
        String oldPin = scanner.nextLine().trim();
        System.out.print("  Enter NEW 4-digit PIN: ");
        String newPin = scanner.nextLine().trim();
        System.out.print("  Confirm NEW 4-digit PIN: ");
        String confirm = scanner.nextLine().trim();

        if (!newPin.equals(confirm)) {
            System.out.println("  [!] Passwords do not match.");
            pressEnterToContinue();
            return;
        }

        try {
            bankService.changePin(account.getAccountNumber(), oldPin, newPin);
            System.out.println("  [SUCCESS] Security PIN updated successfully!");
        } catch (Exception e) {
            System.out.println("  [!] PIN change failed: " + e.getMessage());
        }
        pressEnterToContinue();
    }

    // ==========================================
    // Administrator Portal
    // ==========================================
    private void handleAdminPortal() {
        System.out.println("\n  --- ADMINISTRATOR AUTHENTICATION ---");
        System.out.print("  Enter Admin Username: ");
        String user = scanner.nextLine().trim();
        System.out.print("  Enter Admin Password: ");
        String pass = scanner.nextLine().trim();

        if (!authService.authenticateAdmin(user, pass)) {
            System.out.println("  [!] Access Denied: Invalid administrator credentials.");
            System.out.println("      (Default credentials: username 'admin', password 'admin123')");
            pressEnterToContinue();
            return;
        }

        boolean adminSession = true;
        while (adminSession) {
            System.out.println("\n  ========================================================");
            System.out.println("  |             BANK MANAGEMENT ADMIN PANEL              |");
            System.out.println("  ========================================================");
            System.out.println("  [1] List All Registered Accounts");
            System.out.println("  [2] Open New Savings Account");
            System.out.println("  [3] Open New Current Account");
            System.out.println("  [4] Freeze / Unfreeze / Close Account");
            System.out.println("  [5] Apply Monthly Interest to All Savings Accounts");
            System.out.println("  [6] View Total Bank Liquidity & Reserve Summary");
            System.out.println("  [7] Return to Main Menu");
            System.out.println("  --------------------------------------------------------");
            System.out.print("  Choose an admin task [1-7]: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    listAllAccounts();
                    break;
                case "2":
                    adminOpenSavings();
                    break;
                case "3":
                    adminOpenCurrent();
                    break;
                case "4":
                    adminManageAccountStatus();
                    break;
                case "5":
                    adminApplyInterest();
                    break;
                case "6":
                    adminLiquiditySummary();
                    break;
                case "7":
                    adminSession = false;
                    break;
                default:
                    System.out.println("  [!] Invalid choice.");
            }
        }
    }

    private void listAllAccounts() {
        Collection<Account> accounts = bankService.getAllAccounts();
        System.out.println("\n  ================================ ALL BANK ACCOUNTS ================================");
        System.out.printf("  %-10s | %-24s | %-16s | %-12s | %-8s\n",
                "Acc No.", "Holder Name", "Type", "Balance", "Status");
        System.out.println("  ----------------------------------------------------------------------------------");
        for (Account acc : accounts) {
            System.out.printf("  %-10s | %-24s | %-16s | $%,11.2f | %-8s\n",
                    acc.getAccountNumber(),
                    acc.getAccountHolderName().length() > 24 ? acc.getAccountHolderName().substring(0, 21) + "..." : acc.getAccountHolderName(),
                    acc.getAccountType(),
                    acc.getBalance(),
                    acc.getStatus());
        }
        System.out.println("  ==================================================================================");
        System.out.printf("  Total Accounts: %d | Total Liquidity: $%,.2f\n",
                accounts.size(), bankService.getTotalBankLiquidity());
        pressEnterToContinue();
    }

    private void adminOpenSavings() {
        System.out.println("\n  --- OPEN NEW SAVINGS ACCOUNT ---");
        System.out.print("  Customer Full Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("  Email Address: ");
        String email = scanner.nextLine().trim();
        System.out.print("  Phone Number: ");
        String phone = scanner.nextLine().trim();
        System.out.print("  Create 4-Digit Security PIN: ");
        String pin = scanner.nextLine().trim();
        System.out.print("  Initial Deposit ($ min 100.00): ");
        try {
            double deposit = Double.parseDouble(scanner.nextLine().trim());
            SavingsAccount acc = bankService.openSavingsAccount(name, email, phone, pin, deposit);
            System.out.printf("\n  [SUCCESS] Savings Account %s opened for %s with balance $%,.2f!\n",
                    acc.getAccountNumber(), acc.getAccountHolderName(), acc.getBalance());
        } catch (Exception e) {
            System.out.println("  [!] Error opening account: " + e.getMessage());
        }
        pressEnterToContinue();
    }

    private void adminOpenCurrent() {
        System.out.println("\n  --- OPEN NEW CURRENT ACCOUNT ---");
        System.out.print("  Business / Individual Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("  Email Address: ");
        String email = scanner.nextLine().trim();
        System.out.print("  Phone Number: ");
        String phone = scanner.nextLine().trim();
        System.out.print("  Create 4-Digit Security PIN: ");
        String pin = scanner.nextLine().trim();
        System.out.print("  Initial Deposit ($): ");
        try {
            double deposit = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("  Overdraft Limit ($ default 1000.00): ");
            String odStr = scanner.nextLine().trim();
            double odLimit = odStr.isEmpty() ? CurrentAccount.DEFAULT_OVERDRAFT_LIMIT : Double.parseDouble(odStr);

            CurrentAccount acc = bankService.openCurrentAccount(name, email, phone, pin, deposit, odLimit);
            System.out.printf("\n  [SUCCESS] Current Account %s opened for %s (Overdraft Limit: $%,.2f)!\n",
                    acc.getAccountNumber(), acc.getAccountHolderName(), acc.getOverdraftLimit());
        } catch (Exception e) {
            System.out.println("  [!] Error opening account: " + e.getMessage());
        }
        pressEnterToContinue();
    }

    private void adminManageAccountStatus() {
        System.out.print("\n  Enter Account Number: ");
        String accNum = scanner.nextLine().trim();
        Account acc = bankService.getAccount(accNum);
        if (acc == null) {
            System.out.println("  [!] Account not found.");
            pressEnterToContinue();
            return;
        }

        System.out.printf("  Current Status for %s is [%s]\n", acc.getAccountNumber(), acc.getStatus());
        System.out.println("  Select New Status:");
        System.out.println("  [1] ACTIVE");
        System.out.println("  [2] FROZEN");
        System.out.println("  [3] CLOSED");
        System.out.print("  Choice: ");
        String ch = scanner.nextLine().trim();
        switch (ch) {
            case "1":
                bankService.setAccountStatus(accNum, AccountStatus.ACTIVE);
                System.out.println("  [SUCCESS] Account set to ACTIVE.");
                break;
            case "2":
                bankService.setAccountStatus(accNum, AccountStatus.FROZEN);
                System.out.println("  [SUCCESS] Account is now FROZEN.");
                break;
            case "3":
                bankService.setAccountStatus(accNum, AccountStatus.CLOSED);
                System.out.println("  [SUCCESS] Account marked as CLOSED.");
                break;
            default:
                System.out.println("  [!] No change made.");
        }
        pressEnterToContinue();
    }

    private void adminApplyInterest() {
        System.out.print("\n  Apply monthly interest to all active savings accounts? (y/n): ");
        String confirm = scanner.nextLine().trim();
        if ("y".equalsIgnoreCase(confirm)) {
            int count = bankService.applyInterestToAllSavings();
            System.out.printf("  [SUCCESS] Monthly interest applied across %d eligible Savings Accounts.\n", count);
        } else {
            System.out.println("  Operation cancelled.");
        }
        pressEnterToContinue();
    }

    private void adminLiquiditySummary() {
        System.out.println("\n  ---------------- BANK LIQUIDITY & RESERVE AUDIT ----------------");
        System.out.printf("  Total Accounts Managed : %d\n", bankService.getTotalAccountsCount());
        System.out.printf("  Total Liquid Capital   : $%,.2f\n", bankService.getTotalBankLiquidity());
        System.out.println("  Currency               : USD ($)");
        System.out.println("  Ledger Verification    : In-Sync and Verified");
        System.out.println("  ----------------------------------------------------------------");
        pressEnterToContinue();
    }

    // ==========================================
    // Quick Demo Accounts & Reports
    // ==========================================
    private void showDemoAccounts() {
        System.out.println("\n  ====================== DEMO ACCOUNTS FOR TESTING ======================");
        System.out.println("  1. Customer Account (Savings):");
        System.out.println("     - Account Number : SB-1001");
        System.out.println("     - Holder Name    : Alice Johnson");
        System.out.println("     - PIN            : 1234");
        System.out.println();
        System.out.println("  2. Customer Account (Current / Business):");
        System.out.println("     - Account Number : CA-2001");
        System.out.println("     - Holder Name    : Bob Martinez (Nexus Corp)");
        System.out.println("     - PIN            : 4321");
        System.out.println();
        System.out.println("  3. Administrator Credentials:");
        System.out.println("     - Username       : admin");
        System.out.println("     - Password       : admin123");
        System.out.println("  =======================================================================");
        pressEnterToContinue();
    }

    private void generateReportAction() {
        System.out.println("\n  Generating 'projectreport.pdf' and editable 'projectreport.docx'...");
        try {
            File pdf = ProjectReportGenerator.generatePdfReport("projectreport.pdf");
            File docx = com.bank.report.ProjectReportDocxGenerator.generateDocxReport("projectreport.docx");
            System.out.printf("  [SUCCESS] PDF Report generated at: %s (%d bytes)\n",
                    pdf.getAbsolutePath(), pdf.length());
            System.out.printf("  [SUCCESS] DOCX Report generated at: %s (%d bytes)\n",
                    docx.getAbsolutePath(), docx.length());
        } catch (Exception e) {
            System.out.println("  [!] Failed to generate reports: " + e.getMessage());
            e.printStackTrace();
        }
        pressEnterToContinue();
    }

    private void printHeader() {
        System.out.println("\n**********************************************************");
        System.out.println("*            BANKING MANAGEMENT SYSTEM (v2.0)            *");
        System.out.println("*          Secure, Scalable Enterprise Java Core         *");
        System.out.println("**********************************************************");
    }

    private void pressEnterToContinue() {
        System.out.print("\n  Press [Enter] to return to menu...");
        scanner.nextLine();
    }
}
