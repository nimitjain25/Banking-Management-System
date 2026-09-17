package com.bank;

import com.bank.report.ProjectReportGenerator;
import com.bank.service.AuthService;
import com.bank.service.BankService;
import com.bank.service.StorageService;
import com.bank.ui.ConsoleUI;

import java.io.File;

/**
 * Application Entry Point for the Banking Management System.
 */
public class Main {
    private static final String DATA_FILE = "data/bank_data.dat";
    private static final String REPORT_PDF_FILE = "projectreport.pdf";
    private static final String REPORT_DOCX_FILE = "projectreport.docx";

    public static void main(String[] args) {
        // Automatically ensure the official PDF and DOCX reports are generated at project root
        ensureProjectReportsGenerated();

        if (args.length > 0 && "--generate-report-only".equalsIgnoreCase(args[0])) {
            System.out.println("Project report generation complete. Exiting.");
            return;
        }

        // Initialize Core System Services
        StorageService storageService = new StorageService(DATA_FILE);
        AuthService authService = new AuthService();
        BankService bankService = new BankService(storageService, authService);

        // Launch Console User Interface
        ConsoleUI ui = new ConsoleUI(bankService, authService);
        ui.start();
    }

    private static void ensureProjectReportsGenerated() {
        File pdf = new File(REPORT_PDF_FILE);
        try {
            ProjectReportGenerator.generatePdfReport(REPORT_PDF_FILE);
            System.out.println("[INFO] Verified PDF report: " + pdf.getAbsolutePath() + " (" + pdf.length() + " bytes)");
        } catch (Exception e) {
            System.err.println("[WARNING] Could not auto-generate projectreport.pdf: " + e.getMessage());
        }

        File docx = new File(REPORT_DOCX_FILE);
        try {
            com.bank.report.ProjectReportDocxGenerator.generateDocxReport(REPORT_DOCX_FILE);
            System.out.println("[INFO] Verified editable DOCX report: " + docx.getAbsolutePath() + " (" + docx.length() + " bytes)");
        } catch (Exception e) {
            System.err.println("[WARNING] Could not auto-generate projectreport.docx: " + e.getMessage());
        }
    }
}
