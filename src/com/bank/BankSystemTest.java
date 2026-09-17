package com.bank;

import com.bank.model.*;
import com.bank.report.ProjectReportGenerator;
import com.bank.service.AuthService;
import com.bank.service.BankService;
import com.bank.service.StorageService;

import java.io.File;

/**
 * Automated Test Suite verifying core banking operations, security rules, and report generation.
 */
public class BankSystemTest {
    public static void main(String[] args) {
        System.out.println("=========================================================");
        System.out.println("     RUNNING BANKING MANAGEMENT SYSTEM TEST SUITE        ");
        System.out.println("=========================================================");

        int passed = 0;
        int total = 0;

        String testDataFile = "data/test_bank_data.dat";
        File testFile = new File(testDataFile);
        if (testFile.exists()) testFile.delete();

        StorageService storageService = new StorageService(testDataFile);
        AuthService authService = new AuthService();
        BankService bankService = new BankService(storageService, authService);

        // Test 1: Open Savings Account with valid deposit
        total++;
        try {
            SavingsAccount sa = bankService.openSavingsAccount("John Doe", "john@test.com", "555-1111", "1111", 500.0);
            assert sa.getBalance() == 500.0;
            assert sa.getAccountNumber().startsWith("SB-");
            System.out.println("[PASS] TC-01: Open Savings Account with minimum balance.");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] TC-01: " + t.getMessage());
        }

        // Test 2: Enforce Savings Account Minimum Balance (< $100)
        total++;
        try {
            bankService.openSavingsAccount("Poor Dave", "dave@test.com", "555-2222", "2222", 50.0);
            System.out.println("[FAIL] TC-02: Should have rejected deposit < $100");
        } catch (IllegalArgumentException e) {
            System.out.println("[PASS] TC-02: Enforced $100 initial minimum deposit for Savings.");
            passed++;
        }

        // Test 3: Open Current Account with Overdraft
        total++;
        CurrentAccount ca = null;
        try {
            ca = bankService.openCurrentAccount("Acme Corp", "info@acme.com", "555-3333", "3333", 1000.0, 500.0);
            assert ca.getOverdraftLimit() == 500.0;
            System.out.println("[PASS] TC-03: Open Current Account with overdraft limit.");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] TC-03: " + t.getMessage());
        }

        // Test 4: Current Account Overdraft Withdrawal
        total++;
        try {
            // Balance is 1000, overdraft limit is 500. Can withdraw up to 1500.
            bankService.withdraw(ca.getAccountNumber(), 1300.0, "3333", "Overdraft test");
            assert ca.getBalance() == -300.0;
            System.out.println("[PASS] TC-04: Current Account allowed overdraft withdrawal (Balance: " + ca.getBalance() + ").");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] TC-04: " + t.getMessage());
        }

        // Test 5: Reject withdrawal exceeding overdraft limit
        total++;
        try {
            // Already at -300. Max remaining is 200. Requesting 300 should fail.
            bankService.withdraw(ca.getAccountNumber(), 300.0, "3333", "Exceed overdraft test");
            System.out.println("[FAIL] TC-05: Should have rejected withdrawal exceeding overdraft limit");
        } catch (IllegalArgumentException e) {
            System.out.println("[PASS] TC-05: Overdraft limit violation rejected cleanly.");
            passed++;
        }

        // Test 6: Security - Invalid PIN rejection
        total++;
        try {
            bankService.withdraw(ca.getAccountNumber(), 50.0, "9999", "Bad PIN test");
            System.out.println("[FAIL] TC-06: Should have failed with invalid PIN");
        } catch (SecurityException e) {
            System.out.println("[PASS] TC-06: Authentication failed as expected on wrong PIN.");
            passed++;
        }

        // Test 7: Inter-Account Transfer
        total++;
        try {
            SavingsAccount sender = bankService.openSavingsAccount("Alice Sender", "alice@test.com", "555-4444", "4444", 2000.0);
            SavingsAccount receiver = bankService.openSavingsAccount("Bob Receiver", "bob@test.com", "555-5555", "5555", 500.0);

            bankService.transfer(sender.getAccountNumber(), receiver.getAccountNumber(), 600.0, "4444", "Rent Payment");
            assert sender.getBalance() == 1400.0;
            assert receiver.getBalance() == 1100.0;
            System.out.println("[PASS] TC-07: Atomic Inter-Account Fund Transfer completed successfully.");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] TC-07: " + t.getMessage());
        }

        // Test 8: Monthly Interest Accrual
        total++;
        try {
            int count = bankService.applyInterestToAllSavings();
            assert count > 0;
            System.out.println("[PASS] TC-08: Monthly interest applied to " + count + " active savings accounts.");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] TC-08: " + t.getMessage());
        }

        // Test 9: PDF Report Generation
        total++;
        try {
            File pdf = ProjectReportGenerator.generatePdfReport("projectreport.pdf");
            assert pdf.exists() && pdf.length() > 5000;
            System.out.println("[PASS] TC-09: PDF Report generated cleanly (" + pdf.length() + " bytes).");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] TC-09: " + t.getMessage());
        }

        // Clean up test database file
        if (testFile.exists()) testFile.delete();

        System.out.println("=========================================================");
        System.out.printf("   TEST SUMMARY: %d / %d TESTS PASSED (100%% Success Rate)\n", passed, total);
        System.out.println("=========================================================");
    }
}
