# Problem Statement & Project Scope

## 1. Problem Statement
Traditional and manual banking systems often suffer from human errors, slow processing times, lack of real-time auditability, and vulnerabilities in credential storage. Furthermore, many small institutions, credit unions, or academic prototype environments struggle with overly complex, heavyweight frameworks that introduce unnecessary security vulnerabilities and deployment overhead. 

There is a critical need for a lightweight, robust, self-contained, and secure **Banking Management System** that demonstrates core software engineering principles:
- Safe, decimal-precision transaction processing with zero balance corruption.
- Strict multi-role segregation (Customer self-service vs. Administrative oversight).
- Cryptographically secure authentication that never exposes or stores plain-text passwords or PINs.
- Autonomous data persistence ensuring reliable recovery across restarts without requiring third-party database servers.

---

## 2. Scope of the Project
The project scope encompasses the complete lifecycle of core banking operations:
- **Customer Account Management**: Account opening, multi-tier account types (Savings Account with interest and minimum balance rules, and Current Account with configurable overdraft limits).
- **Financial Transaction Processing**: Real-time deposits, PIN-authorized cash withdrawals, and atomic inter-account fund transfers with rollback on failure.
- **Security & Integrity**: 128-bit salted SHA-256 cryptographic hashing for all user PINs and administrator credentials.
- **Reporting & Auditing**: Sequential immutable transaction history, bank liquidity monitoring, and an automated built-in PDF project report generator conforming to PDF-1.4 standards.
- **Data Persistence**: Fail-safe binary file persistence ensuring persistent state across sessions.

---

## 3. Target Users
1. **Bank Customers / Account Holders**:
   - Individuals seeking self-service capabilities to deposit, withdraw, transfer funds, view account statements, and manage their security PIN.
2. **Bank Administrators / Branch Managers**:
   - Branch personnel responsible for onboarding new accounts, adjusting overdraft limits, freezing suspicious accounts, triggering batch interest credits, and monitoring overall bank liquidity.
3. **Academic Evaluators & Software Engineers**:
   - Students, faculty, and engineers studying pure Object-Oriented Programming (OOP) paradigms, security engineering, and system design in Java SE.

---

## 4. High-Level Features
- **Dual-Role Authentication**: Distinct entry portals for Customers (Account No. + 4-digit PIN) and Administrators (Username + Password).
- **Polymorphic Account Hierarchy**: Abstract `Account` base class with specialized `SavingsAccount` and `CurrentAccount` implementations.
- **Atomic Two-Phase Transfers**: Debit and credit operations executed atomically to guarantee ledger consistency.
- **Audit Trails**: Every deposit, withdrawal, and transfer records an immutable `Transaction` voucher with unique ID, timestamp, and memo.
- **Zero-Dependency Architecture**: 100% pure Java implementation requiring no external JARs or runtime dependencies.
- **Automated Report Generation**: Native generation of an official 8-page `projectreport.pdf` adhering to institutional guidelines.
