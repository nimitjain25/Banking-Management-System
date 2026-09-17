package com.bank.report;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates the official project report matching the exact format, layout, and simplicity
 * of reference.pdf.
 * Written 100% in pure Java standard libraries without external dependencies.
 */
public class ProjectReportGenerator {

    private static class SimplePdfPage {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();

        void write(String s) {
            try {
                stream.write(s.getBytes(StandardCharsets.ISO_8859_1));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        void text(String font, double size, double x, double y, String text) {
            write("BT\n");
            write(String.format(java.util.Locale.US, "/%s %.2f Tf\n", font, size));
            write("0 0 0 rg\n"); // Plain black text
            write(String.format(java.util.Locale.US, "%.2f %.2f Td\n", x, y));
            write("(" + escapePdf(text) + ") Tj\n");
            write("ET\n");
        }

        private String escapePdf(String input) {
            if (input == null) return "";
            return input.replace("\\", "\\\\")
                        .replace("(", "\\(")
                        .replace(")", "\\)");
        }

        byte[] getBytes() {
            return stream.toByteArray();
        }
    }

    public static File generatePdfReport(String outputPath) throws IOException {
        List<SimplePdfPage> pages = new ArrayList<>();

        // Generate the 5 pages following the exact sequence and structure of reference.pdf
        pages.add(buildPage1());
        pages.add(buildPage2());
        pages.add(buildPage3());
        pages.add(buildPage4());
        pages.add(buildPage5());

        File outputFile = new File(outputPath);
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            writePdfDocument(pages, fos);
        }
        return outputFile;
    }

    // =========================================================================
    // Page 1: Student Header, Centered Institution, Title & Introduction
    // =========================================================================
    private static SimplePdfPage buildPage1() {
        SimplePdfPage p = new SimplePdfPage();

        double y = 760;
        double left = 72;

        // Student & Faculty Header (Bold as in reference.pdf)
        p.text("F2", 13, left, y, "Name :- Nimit jain");
        y -= 26;
        p.text("F2", 13, left, y, "Reg.No :- 25BAI10840");
        y -= 26;
        p.text("F2", 13, left, y, "Branch :- CSE(AIML)");
        y -= 26;
        p.text("F2", 13, left, y, "Faculty :-Dr J. Sharmila Joseph");
        y -= 46;

        // Centered Institution & Title
        p.text("F2", 15, 230, y, "VIT BHOPAL");
        y -= 30;
        p.text("F1", 13, 215, y, "PROJECT REPORT");
        y -= 50;

        // Project Title
        p.text("F2", 14, left, y, "Title :- Banking-Management-System");
        y -= 36;

        // Introduction
        p.text("F2", 13, left, y, "Introduction:-");
        y -= 24;

        String[] introLines = {
                "This project, the Banking Management System is a console-based application",
                "developed using the Java programming language. It is designed to simulate",
                "a fundamental core banking and account management system for a small branch,",
                "credit union, or financial unit. The core objective is to demonstrate the mastery of",
                "introductory and intermediate Computer Science principles, including modular",
                "programming, object-oriented design, fundamental data structures (Collections and",
                "Maps), and persistent data management through file input/output (I/O). A key feature",
                "of the application is the robust implementation of explicit input validation and business",
                "rule enforcement using control flow, ensuring data integrity without relying on external",
                "database software. The application allows users to open accounts, deposit funds,",
                "withdraw cash, perform inter-account transfers, and generate transaction statements,",
                "providing essential balance alerts and financial tracking."
        };

        for (String line : introLines) {
            p.text("F1", 11.5, left, y, line);
            y -= 19;
        }

        return p;
    }

    // =========================================================================
    // Page 2: Problem Statement, Proposed Solution & Data Structure Design
    // =========================================================================
    private static SimplePdfPage buildPage2() {
        SimplePdfPage p = new SimplePdfPage();

        double y = 760;
        double left = 72;

        // Problem Statement
        p.text("F2", 15, left, y, "Problem Statement");
        y -= 24;

        String[] probLines = {
                "Manual banking record keeping and financial tracking (using spreadsheets or paper",
                "ledgers) is highly inefficient, prone to human calculation errors, and lacks the ability",
                "to provide real-time status updates. Errors in tracking balances and transactions can",
                "lead to critical discrepancies, unauthorized overdrafts, and operational losses."
        };
        for (String line : probLines) {
            p.text("F1", 11.5, left, y, line);
            y -= 19;
        }

        y -= 16;
        // Proposed Solution
        p.text("F2", 15, left, y, "Proposed Solution");
        y -= 24;

        String[] solLines = {
                "The Banking Management System provides a simple, menu-driven interface",
                "accessible via the console. This solution automates the processes of recording banking",
                "transactions and utilizes a data file for persistence. This approach prioritizes",
                "simplicity, reliability, and clear demonstration of core programming logic over complex",
                "graphical interfaces or external database management systems."
        };
        for (String line : solLines) {
            p.text("F1", 11.5, left, y, line);
            y -= 19;
        }

        y -= 16;
        // Data Structure Design
        p.text("F2", 15, left, y, "Data Structure Design");
        y -= 24;

        String[] dsLines = {
                "The banking data is stored within Java as structured object models, leveraging the",
                "efficiency of the Java Collections Framework:",
                "- Outer Structure (Accounts): A master Map where the Account Number (a string",
                "  such as \"SB-1001\" or \"CA-2001\") acts as the unique key.",
                "- Inner Structure (Details): The value associated with the key is an Account object",
                "  containing encapsulated account details:",
                "  o 'accountHolderName': A string representing customer name.",
                "  o 'balance': A double tracking current monetary balance.",
                "  o 'hashedPin': A string storing salted SHA-256 password hash.",
                "  o 'transactions': A List storing chronological Transaction records."
        };
        for (String line : dsLines) {
            p.text("F1", 11.5, left, y, line);
            y -= 19;
        }

        return p;
    }

    // =========================================================================
    // Page 3: Goal of this program & Explanation of the Program (Part 1)
    // =========================================================================
    private static SimplePdfPage buildPage3() {
        SimplePdfPage p = new SimplePdfPage();

        double y = 760;
        double left = 72;

        // Goal of this program
        p.text("F2", 15, left, y, "Goal of this program:-");
        y -= 24;

        String[] goalLines = {
                "The main goal of the program is to provide an account management system for small",
                "banking units, cooperative branches, and clients to keep track of their balances and",
                "transactions. Using a simple menu-driven approach and direct accessing methods, this",
                "becomes an easy to understand program and thus is of practical real world use too. This",
                "system of management reduces the errors made by humans and keeps track of any",
                "account activity to fulfill requests immediately."
        };
        for (String line : goalLines) {
            p.text("F1", 11.5, left, y, line);
            y -= 19;
        }

        y -= 16;
        // Explanation of the Program
        p.text("F2", 15, left, y, "Explanation of the Program:-");
        y -= 24;

        String[] expLines = {
                "This program begins by organizing domain logic across clean Java classes. The",
                "java.io package provides classes to interact with files on the filesystem in a portable",
                "way. Here we use object streams to interact with the data files of the operating system",
                "to store the state of accounts and access them as and when required by us. We then create",
                "several methods that contain the working of how accounts are accessed to get the",
                "balances or to store and update the values of transactions in them. The program is",
                "mainly built with the help of loops, if-else constructs, and basic console input/output",
                "statements to get the values. The program also verifies security PINs before releasing",
                "funds to prevent unauthorized access. The application is entirely menu-driven and",
                "implements the following essential banking features:",
                "",
                "Data Persistence: Utilizes a standard file (bank_data.dat) to load and save all account",
                "data, ensuring that account information is retained between program executions."
        };
        for (String line : expLines) {
            if (line.isEmpty()) {
                y -= 8;
                continue;
            }
            if (line.startsWith("Data Persistence:")) {
                p.text("F2", 11.5, left, y, "Data Persistence:");
                p.text("F1", 11.5, left + 105, y, "Utilizes a standard file (bank_data.dat) to load and save all account");
            } else {
                p.text("F1", 11.5, left, y, line);
            }
            y -= 19;
        }

        return p;
    }

    // =========================================================================
    // Page 4: Explanation Continued, Results & Conclusion
    // =========================================================================
    private static SimplePdfPage buildPage4() {
        SimplePdfPage p = new SimplePdfPage();

        double y = 760;
        double left = 72;

        p.text("F1", 11.5, left, y, "executions.");
        y -= 24;

        // Feature 1: Account Management
        p.text("F2", 11.5, left, y, "Account Management:");
        p.text("F1", 11.5, left + 130, y, "Allows users to open new accounts (Savings Account with");
        y -= 19;
        p.text("F1", 11.5, left, y, "initial deposit and interest rate, or Current Account with overdraft limit) and update");
        y -= 19;
        p.text("F1", 11.5, left, y, "balances via deposits, cash withdrawals, and inter-account transfers.");
        y -= 24;

        // Feature 2: Input Validation
        p.text("F2", 11.5, left, y, "Input Validation:");
        p.text("F1", 11.5, left + 100, y, "Employs explicit if/else logic to ensure that all inputs, particularly");
        y -= 19;
        p.text("F1", 11.5, left, y, "transaction amounts, are valid positive numbers, preventing logical errors.");
        y -= 24;

        // Feature 3: Low Balance Alert
        p.text("F2", 11.5, left, y, "Balance Restrictions & Alerts:");
        p.text("F1", 11.5, left + 175, y, "Automatically checks if a savings balance falls below");
        y -= 19;
        p.text("F1", 11.5, left, y, "the minimum balance ($100.00) or if a current account exceeds its overdraft limit,");
        y -= 19;
        p.text("F1", 11.5, left, y, "aiding in maintaining financial solvency.");
        y -= 24;

        // Feature 4: Reporting
        p.text("F2", 11.5, left, y, "Reporting:");
        p.text("F1", 11.5, left + 65, y, "Displays account statements and transaction histories in a clear, formatted");
        y -= 19;
        p.text("F1", 11.5, left, y, "tabular view directly in the console.");
        y -= 28;

        // Results
        p.text("F2", 15, left, y, "Results");
        y -= 24;

        String[] resLines = {
                "This application successfully manages the banking operations, providing a functional,",
                "persistent, and reliable text interface that accurately reflects real-world banking",
                "scenarios."
        };
        for (String line : resLines) {
            p.text("F1", 11.5, left, y, line);
            y -= 19;
        }

        y -= 16;
        // Conclusion
        p.text("F2", 15, left, y, "Conclusion:-");
        y -= 24;

        String[] conLines = {
                "The Banking Management System project is a successful demonstration of core",
                "programming proficiency in Java. It effectively utilizes object-oriented principles for",
                "data organization, methods for modular design, and robust if/else conditional logic for",
                "comprehensive input and security validation. The successful implementation of data",
                "persistence confirms the student's understanding of File I/O and"
        };
        for (String line : conLines) {
            p.text("F1", 11.5, left, y, line);
            y -= 19;
        }

        return p;
    }

    // =========================================================================
    // Page 5: What I learnt & Future Scope
    // =========================================================================
    private static SimplePdfPage buildPage5() {
        SimplePdfPage p = new SimplePdfPage();

        double y = 760;
        double left = 72;

        p.text("F1", 11.5, left, y, "fundamental application design.");
        y -= 26;

        // What I learnt in this project
        p.text("F2", 15, left, y, "What I learnt in this project:-");
        y -= 24;

        String[] learntLines = {
                "While making this project on banking management, I learnt the use of the java.io",
                "package and how it interacts with the Java program to store values so that they can be",
                "accessed anytime when needed. I gained the knowledge of how to store files in the",
                "directories of the system and can be used as an information management system also.",
                "I also learnt a broader use of if-else and looping constructs. While working out the",
                "program, I also got to know about try-catch type of constructs and got to know how it",
                "handles exceptions cleanly without stopping the program abruptly, allowing error",
                "detection quickly without excessive nested logic statements."
        };
        for (String line : learntLines) {
            p.text("F1", 11.5, left, y, line);
            y -= 19;
        }

        y -= 20;
        // Future Scope
        p.text("F2", 15, left, y, "Future Scope");
        y -= 24;

        p.text("F1", 11.5, left, y, "To evolve this project beyond the introductory level, the following enhancements");
        y -= 19;
        p.text("F1", 11.5, left, y, "could be implemented:");
        y -= 24;

        String[] futureLines = {
                "1. Database Migration: Replace file storage with a relational database (e.g., SQLite",
                "   or MySQL) for better query performance and structured data management.",
                "",
                "2. Graphical User Interface (GUI): Implement a desktop interface using libraries",
                "   such as Swing or JavaFX to improve user experience.",
                "",
                "3. Advanced Alerting: Integrate functions to check and alert the user about low balances",
                "   or overdraft usage via email or SMS notifications.",
                "",
                "4. Reporting: Add the ability to generate printable reports (e.g., PDF or CSV) of all",
                "   recent transactions or bank liquidity audits."
        };
        for (String line : futureLines) {
            if (line.isEmpty()) {
                y -= 8;
                continue;
            }
            p.text("F1", 11.5, left, y, line);
            y -= 19;
        }

        return p;
    }

    // =========================================================================
    // Core PDF Serializer (PDF-1.4 spec, standard Times-Roman & Times-Bold)
    // =========================================================================
    private static void writePdfDocument(List<SimplePdfPage> pages, OutputStream out) throws IOException {
        List<Long> offsets = new ArrayList<>();
        CountingOutputStream cos = new CountingOutputStream(out);

        // Standard PDF Header
        cos.write("%PDF-1.4\n".getBytes(StandardCharsets.ISO_8859_1));
        cos.write(new byte[]{'%', (byte) 0xE2, (byte) 0xE3, (byte) 0xCF, (byte) 0xD3, '\n'});

        int totalPages = pages.size();
        int catalogId = 1;
        int pagesTreeId = 2;
        int fontF1Id = 3; // Times-Roman
        int fontF2Id = 4; // Times-Bold

        offsets.add(0L);

        // Obj 1: Catalog
        offsets.add(cos.getCount());
        cos.write(String.format("%d 0 obj\n<< /Type /Catalog /Pages %d 0 R >>\nendobj\n", catalogId, pagesTreeId)
                .getBytes(StandardCharsets.ISO_8859_1));

        // Obj 2: Pages Tree
        StringBuilder kids = new StringBuilder();
        for (int i = 0; i < totalPages; i++) {
            kids.append(5 + (i * 2)).append(" 0 R ");
        }
        offsets.add(cos.getCount());
        cos.write(String.format("%d 0 obj\n<< /Type /Pages /Kids [ %s] /Count %d >>\nendobj\n",
                pagesTreeId, kids.toString(), totalPages).getBytes(StandardCharsets.ISO_8859_1));

        // Fonts: Times-Roman and Times-Bold (exact matching font of reference.pdf)
        offsets.add(cos.getCount());
        cos.write(String.format("%d 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Times-Roman >>\nendobj\n", fontF1Id)
                .getBytes(StandardCharsets.ISO_8859_1));

        offsets.add(cos.getCount());
        cos.write(String.format("%d 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Times-Bold >>\nendobj\n", fontF2Id)
                .getBytes(StandardCharsets.ISO_8859_1));

        // Pages and Content Streams
        for (int i = 0; i < totalPages; i++) {
            int pageId = 5 + (i * 2);
            int streamId = pageId + 1;
            byte[] streamBytes = pages.get(i).getBytes();

            // Page Object
            offsets.add(cos.getCount());
            String pageDef = String.format(
                    "%d 0 obj\n" +
                    "<< /Type /Page\n" +
                    "   /Parent %d 0 R\n" +
                    "   /MediaBox [0 0 595.28 841.89]\n" +
                    "   /Resources << /Font << /F1 %d 0 R /F2 %d 0 R >> >>\n" +
                    "   /Contents %d 0 R\n" +
                    ">>\nendobj\n",
                    pageId, pagesTreeId, fontF1Id, fontF2Id, streamId
            );
            cos.write(pageDef.getBytes(StandardCharsets.ISO_8859_1));

            // Content Stream Object
            offsets.add(cos.getCount());
            String streamHeader = String.format("%d 0 obj\n<< /Length %d >>\nstream\n", streamId, streamBytes.length);
            cos.write(streamHeader.getBytes(StandardCharsets.ISO_8859_1));
            cos.write(streamBytes);
            cos.write("\nendstream\nendobj\n".getBytes(StandardCharsets.ISO_8859_1));
        }

        // XREF Table
        long xrefOffset = cos.getCount();
        int totalObjects = 5 + (totalPages * 2);
        cos.write(String.format("xref\n0 %d\n", totalObjects).getBytes(StandardCharsets.ISO_8859_1));
        cos.write("0000000000 65535 f \n".getBytes(StandardCharsets.ISO_8859_1));

        for (int i = 1; i < totalObjects; i++) {
            long off = offsets.get(i);
            cos.write(String.format(java.util.Locale.US, "%010d 00000 n \n", off).getBytes(StandardCharsets.ISO_8859_1));
        }

        // Trailer
        String trailer = String.format(
                "trailer\n<< /Size %d /Root %d 0 R >>\nstartxref\n%d\n%%%%EOF\n",
                totalObjects, catalogId, xrefOffset
        );
        cos.write(trailer.getBytes(StandardCharsets.ISO_8859_1));
        cos.flush();
    }

    private static class CountingOutputStream extends OutputStream {
        private final OutputStream out;
        private long count = 0;

        public CountingOutputStream(OutputStream out) {
            this.out = out;
        }

        @Override
        public void write(int b) throws IOException {
            out.write(b);
            count++;
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
            count += len;
        }

        @Override
        public void flush() throws IOException {
            out.flush();
        }

        @Override
        public void close() throws IOException {
            out.close();
        }

        public long getCount() {
            return count;
        }
    }
}
