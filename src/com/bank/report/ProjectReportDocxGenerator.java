package com.bank.report;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Generates an editable Microsoft Word (.docx) document of the project report
 * matching the exact format of reference.pdf using pure Java and standard OpenXML.
 */
public class ProjectReportDocxGenerator {

    public static File generateDocxReport(String outputPath) throws IOException {
        File file = new File(outputPath);
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file))) {
            addZipEntry(zos, "[Content_Types].xml", buildContentTypesXml());
            addZipEntry(zos, "_rels/.rels", buildRelsXml());
            addZipEntry(zos, "word/_rels/document.xml.rels", buildDocumentRelsXml());
            addZipEntry(zos, "word/styles.xml", buildStylesXml());
            addZipEntry(zos, "word/document.xml", buildDocumentXml());
        }
        return file;
    }

    private static void addZipEntry(ZipOutputStream zos, String entryName, String xmlContent) throws IOException {
        ZipEntry entry = new ZipEntry(entryName);
        zos.putNextEntry(entry);
        zos.write(xmlContent.getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }

    private static String buildContentTypesXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\">\n" +
                "  <Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/>\n" +
                "  <Default Extension=\"xml\" ContentType=\"application/xml\"/>\n" +
                "  <Override PartName=\"/word/document.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml\"/>\n" +
                "  <Override PartName=\"/word/styles.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.styles+xml\"/>\n" +
                "</Types>";
    }

    private static String buildRelsXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">\n" +
                "  <Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" Target=\"word/document.xml\"/>\n" +
                "</Relationships>";
    }

    private static String buildDocumentRelsXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">\n" +
                "  <Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles\" Target=\"styles.xml\"/>\n" +
                "</Relationships>";
    }

    private static String buildStylesXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<w:styles xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">\n" +
                "  <w:docDefaults>\n" +
                "    <w:rPrDefault>\n" +
                "      <w:rPr>\n" +
                "        <w:rFonts w:ascii=\"Times New Roman\" w:hAnsi=\"Times New Roman\" w:cs=\"Times New Roman\"/>\n" +
                "        <w:sz w:val=\"23\"/>\n" +
                "        <w:szCs w:val=\"23\"/>\n" +
                "        <w:lang w:val=\"en-US\"/>\n" +
                "      </w:rPr>\n" +
                "    </w:rPrDefault>\n" +
                "    <w:pPrDefault>\n" +
                "      <w:pPr>\n" +
                "        <w:spacing w:line=\"280\" w:lineRule=\"auto\" w:after=\"120\"/>\n" +
                "      </w:pPr>\n" +
                "    </w:pPrDefault>\n" +
                "  </w:docDefaults>\n" +
                "</w:styles>";
    }

    private static String buildDocumentXml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        sb.append("<w:document xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">\n");
        sb.append("  <w:body>\n");

        // ================= PAGE 1 =================
        sb.append(pBold("Name :- Nimit jain", 26, 0, 60));
        sb.append(pBold("Reg.No :- 25BAI10840", 26, 0, 60));
        sb.append(pBold("Branch :- CSE(AIML)", 26, 0, 60));
        sb.append(pBold("Faculty :-Dr J. Sharmila Joseph", 26, 0, 300));

        sb.append(pCenteredBold("VIT BHOPAL", 30, 0, 100));
        sb.append(pCentered("PROJECT REPORT", 26, 0, 400));

        sb.append(pBold("Title :- Banking-Management-System", 28, 0, 240));

        sb.append(pBold("Introduction:-", 26, 0, 100));
        sb.append(pText("This project, the Banking Management System is a console-based application developed using the Java programming language. It is designed to simulate a fundamental core banking and account management system for a small branch, credit union, or financial unit. The core objective is to demonstrate the mastery of introductory and intermediate Computer Science principles, including modular programming, object-oriented design, fundamental data structures (Collections and Maps), and persistent data management through file input/output (I/O). A key feature of the application is the robust implementation of explicit input validation and business rule enforcement using control flow, ensuring data integrity without relying on external database software. The application allows users to open accounts, deposit funds, withdraw cash, perform inter-account transfers, and generate transaction statements, providing essential balance alerts and financial tracking."));

        sb.append(pageBreak());

        // ================= PAGE 2 =================
        sb.append(pHeading("Problem Statement"));
        sb.append(pText("Manual banking record keeping and financial tracking (using spreadsheets or paper ledgers) is highly inefficient, prone to human calculation errors, and lacks the ability to provide real-time status updates. Errors in tracking balances and transactions can lead to critical discrepancies, unauthorized overdrafts, and operational losses."));

        sb.append(pHeading("Proposed Solution"));
        sb.append(pText("The Banking Management System provides a simple, menu-driven interface accessible via the console. This solution automates the processes of recording banking transactions and utilizes a data file for persistence. This approach prioritizes simplicity, reliability, and clear demonstration of core programming logic over complex graphical interfaces or external database management systems."));

        sb.append(pHeading("Data Structure Design"));
        sb.append(pText("The banking data is stored within Java as structured object models, leveraging the efficiency of the Java Collections Framework:"));
        sb.append(pBullet("Outer Structure (Accounts): A master Map where the Account Number (a string such as \"SB-1001\" or \"CA-2001\") acts as the unique key."));
        sb.append(pBullet("Inner Structure (Details): The value associated with the key is an Account object containing encapsulated account details:"));
        sb.append(pSubBullet("o 'accountHolderName': A string representing customer name."));
        sb.append(pSubBullet("o 'balance': A double tracking current monetary balance."));
        sb.append(pSubBullet("o 'hashedPin': A string storing salted SHA-256 password hash."));
        sb.append(pSubBullet("o 'transactions': A List storing chronological Transaction records."));

        sb.append(pageBreak());

        // ================= PAGE 3 =================
        sb.append(pHeading("Goal of this program:-"));
        sb.append(pText("The main goal of the program is to provide an account management system for small banking units, cooperative branches, and clients to keep track of their balances and transactions. Using a simple menu-driven approach and direct accessing methods, this becomes an easy to understand program and thus is of practical real world use too. This system of management reduces the errors made by humans and keeps track of any account activity to fulfill requests immediately."));

        sb.append(pHeading("Explanation of the Program:-"));
        sb.append(pText("This program begins by organizing domain logic across clean Java classes. The java.io package provides classes to interact with files on the filesystem in a portable way. Here we use object streams to interact with the data files of the operating system to store the state of accounts and access them as and when required by us. We then create several methods that contain the working of how accounts are accessed to get the balances or to store and update the values of transactions in them. The program is mainly built with the help of loops, if-else constructs, and basic console input/output statements to get the values. The program also verifies security PINs before releasing funds to prevent unauthorized access. The application is entirely menu-driven and implements the following essential banking features:"));

        sb.append(pTextWithBoldPrefix("Data Persistence: ", "Utilizes a standard file (bank_data.dat) to load and save all account data, ensuring that account information is retained between program"));

        sb.append(pageBreak());

        // ================= PAGE 4 =================
        sb.append(pText("executions."));
        sb.append(pTextWithBoldPrefix("Account Management: ", "Allows users to open new accounts (Savings Account with initial deposit and interest rate, or Current Account with overdraft limit) and update balances via deposits, cash withdrawals, and inter-account transfers."));
        sb.append(pTextWithBoldPrefix("Input Validation: ", "Employs explicit if/else logic to ensure that all inputs, particularly transaction amounts, are valid positive numbers, preventing logical errors."));
        sb.append(pTextWithBoldPrefix("Balance Restrictions & Alerts: ", "Automatically checks if a savings balance falls below the minimum balance ($100.00) or if a current account exceeds its overdraft limit, aiding in maintaining financial solvency."));
        sb.append(pTextWithBoldPrefix("Reporting: ", "Displays account statements and transaction histories in a clear, formatted tabular view directly in the console."));

        sb.append(pHeading("Results"));
        sb.append(pText("This application successfully manages the banking operations, providing a functional, persistent, and reliable text interface that accurately reflects real-world banking scenarios."));

        sb.append(pHeading("Conclusion:-"));
        sb.append(pText("The Banking Management System project is a successful demonstration of core programming proficiency in Java. It effectively utilizes object-oriented principles for data organization, methods for modular design, and robust if/else conditional logic for comprehensive input and security validation. The successful implementation of data persistence confirms the student's understanding of File I/O and"));

        sb.append(pageBreak());

        // ================= PAGE 5 =================
        sb.append(pText("fundamental application design."));

        sb.append(pHeading("What I learnt in this project:-"));
        sb.append(pText("While making this project on banking management, I learnt the use of the java.io package and how it interacts with the Java program to store values so that they can be accessed anytime when needed. I gained the knowledge of how to store files in the directories of the system and can be used as an information management system also. I also learnt a broader use of if-else and looping constructs. While working out the program, I also got to know about try-catch type of constructs and got to know how it handles exceptions cleanly without stopping the program abruptly, allowing error detection quickly without excessive nested logic statements."));

        sb.append(pHeading("Future Scope"));
        sb.append(pText("To evolve this project beyond the introductory level, the following enhancements could be implemented:"));
        sb.append(pNumbered("1. Database Migration: Replace file storage with a relational database (e.g., SQLite or MySQL) for better query performance and structured data management."));
        sb.append(pNumbered("2. Graphical User Interface (GUI): Implement a desktop interface using libraries such as Swing or JavaFX to improve user experience."));
        sb.append(pNumbered("3. Advanced Alerting: Integrate functions to check and alert the user about low balances or overdraft usage via email or SMS notifications."));
        sb.append(pNumbered("4. Reporting: Add the ability to generate printable reports (e.g., PDF or CSV) of all recent transactions or bank liquidity audits."));

        // Page setup
        sb.append("    <w:sectPr>\n");
        sb.append("      <w:pgSz w:w=\"11906\" w:h=\"16838\"/>\n"); // Standard A4 (in dxa)
        sb.append("      <w:pgMar w:top=\"1440\" w:right=\"1440\" w:bottom=\"1440\" w:left=\"1440\"/>\n"); // 1 inch margins
        sb.append("    </w:sectPr>\n");

        sb.append("  </w:body>\n");
        sb.append("</w:document>");
        return sb.toString();
    }

    // ==========================================
    // OpenXML Paragraph Helpers
    // ==========================================
    private static String escapeXml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }

    private static String pHeading(String text) {
        return String.format(
                "    <w:p>\n" +
                "      <w:pPr>\n" +
                "        <w:spacing w:before=\"240\" w:after=\"120\"/>\n" +
                "      </w:pPr>\n" +
                "      <w:r>\n" +
                "        <w:rPr>\n" +
                "          <w:b/>\n" +
                "          <w:sz w:val=\"30\"/>\n" +
                "        </w:rPr>\n" +
                "        <w:t>%s</w:t>\n" +
                "      </w:r>\n" +
                "    </w:p>\n", escapeXml(text));
    }

    private static String pText(String text) {
        return String.format(
                "    <w:p>\n" +
                "      <w:pPr>\n" +
                "        <w:spacing w:after=\"160\" w:line=\"280\" w:lineRule=\"auto\"/>\n" +
                "      </w:pPr>\n" +
                "      <w:r>\n" +
                "        <w:t>%s</w:t>\n" +
                "      </w:r>\n" +
                "    </w:p>\n", escapeXml(text));
    }

    private static String pTextWithBoldPrefix(String boldPrefix, String text) {
        return String.format(
                "    <w:p>\n" +
                "      <w:pPr>\n" +
                "        <w:spacing w:after=\"160\" w:line=\"280\" w:lineRule=\"auto\"/>\n" +
                "      </w:pPr>\n" +
                "      <w:r>\n" +
                "        <w:rPr><w:b/></w:rPr>\n" +
                "        <w:t>%s</w:t>\n" +
                "      </w:r>\n" +
                "      <w:r>\n" +
                "        <w:t>%s</w:t>\n" +
                "      </w:r>\n" +
                "    </w:p>\n", escapeXml(boldPrefix), escapeXml(text));
    }

    private static String pBold(String text, int halfPoints, int before, int after) {
        return String.format(
                "    <w:p>\n" +
                "      <w:pPr>\n" +
                "        <w:spacing w:before=\"%d\" w:after=\"%d\"/>\n" +
                "      </w:pPr>\n" +
                "      <w:r>\n" +
                "        <w:rPr>\n" +
                "          <w:b/>\n" +
                "          <w:sz w:val=\"%d\"/>\n" +
                "        </w:rPr>\n" +
                "        <w:t>%s</w:t>\n" +
                "      </w:r>\n" +
                "    </w:p>\n", before, after, halfPoints, escapeXml(text));
    }

    private static String pCenteredBold(String text, int halfPoints, int before, int after) {
        return String.format(
                "    <w:p>\n" +
                "      <w:pPr>\n" +
                "        <w:jc w:val=\"center\"/>\n" +
                "        <w:spacing w:before=\"%d\" w:after=\"%d\"/>\n" +
                "      </w:pPr>\n" +
                "      <w:r>\n" +
                "        <w:rPr>\n" +
                "          <w:b/>\n" +
                "          <w:sz w:val=\"%d\"/>\n" +
                "        </w:rPr>\n" +
                "        <w:t>%s</w:t>\n" +
                "      </w:r>\n" +
                "    </w:p>\n", before, after, halfPoints, escapeXml(text));
    }

    private static String pCentered(String text, int halfPoints, int before, int after) {
        return String.format(
                "    <w:p>\n" +
                "      <w:pPr>\n" +
                "        <w:jc w:val=\"center\"/>\n" +
                "        <w:spacing w:before=\"%d\" w:after=\"%d\"/>\n" +
                "      </w:pPr>\n" +
                "      <w:r>\n" +
                "        <w:rPr>\n" +
                "          <w:sz w:val=\"%d\"/>\n" +
                "        </w:rPr>\n" +
                "        <w:t>%s</w:t>\n" +
                "      </w:r>\n" +
                "    </w:p>\n", before, after, halfPoints, escapeXml(text));
    }

    private static String pBullet(String text) {
        return String.format(
                "    <w:p>\n" +
                "      <w:pPr>\n" +
                "        <w:ind w:left=\"360\"/>\n" +
                "        <w:spacing w:after=\"80\"/>\n" +
                "      </w:pPr>\n" +
                "      <w:r>\n" +
                "        <w:t>%s</w:t>\n" +
                "      </w:r>\n" +
                "    </w:p>\n", escapeXml(text));
    }

    private static String pSubBullet(String text) {
        return String.format(
                "    <w:p>\n" +
                "      <w:pPr>\n" +
                "        <w:ind w:left=\"720\"/>\n" +
                "        <w:spacing w:after=\"60\"/>\n" +
                "      </w:pPr>\n" +
                "      <w:r>\n" +
                "        <w:t>%s</w:t>\n" +
                "      </w:r>\n" +
                "    </w:p>\n", escapeXml(text));
    }

    private static String pNumbered(String text) {
        return String.format(
                "    <w:p>\n" +
                "      <w:pPr>\n" +
                "        <w:ind w:left=\"360\"/>\n" +
                "        <w:spacing w:after=\"120\"/>\n" +
                "      </w:pPr>\n" +
                "      <w:r>\n" +
                "        <w:t>%s</w:t>\n" +
                "      </w:r>\n" +
                "    </w:p>\n", escapeXml(text));
    }

    private static String pageBreak() {
        return "    <w:p><w:r><w:br w:type=\"page\"/></w:r></w:p>\n";
    }
}
