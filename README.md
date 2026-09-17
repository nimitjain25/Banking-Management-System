# Banking Management System

A console-based Banking Management System developed in Java for the VITyarthi project evaluation. The application simulates everyday banking operations including account creation, deposits, withdrawals, fund transfers, transaction history tracking, and administrative controls.

---

## Student Information
- **Name:** Nimit Jain
- **Registration No:** 25BAI10840
- **Branch:** CSE (AIML)
- **Institution:** VIT Bhopal University

---

## Features

### Customer Portal
- **Account Summary:** Check account balance, type, and current status.
- **Deposit & Withdrawal:** Deposit money or withdraw cash with PIN verification and balance rules.
- **Fund Transfer:** Transfer funds between accounts with balance verification and transaction logs.
- **Mini-Statement / Transaction History:** View all past deposits, withdrawals, and transfers.
- **Change PIN:** Update the 4-digit security PIN.

### Admin Portal
- **View All Accounts:** List all registered accounts, customer details, and balances.
- **Open New Account:** Create Savings Accounts (with minimum deposit requirement) or Current Accounts (with overdraft limit).
- **Account Lifecycle:** Freeze, unfreeze, or close customer accounts.
- **Apply Interest:** Automatically calculate and credit monthly interest to all active savings accounts.
- **Bank Liquidity Snapshot:** View total deposits and funds managed by the system.

### Technical Highlights
- **Object-Oriented Programming:** Implements abstraction, inheritance, polymorphism, and encapsulation (`Account` base class with specialized `SavingsAccount` and `CurrentAccount`).
- **Data Persistence:** Automatically saves all account data and transactions to a local file (`data/bank_data.dat`) using Java object serialization.
- **PIN Security:** Passwords and PINs are hashed with salted SHA-256.
- **Report Generation:** Generates the required project report directly in PDF (`projectreport.pdf`) and Word (`projectreport.docx`) formats.

---

## Project Structure

```
BankingManagementSystem/
├── src/
│   └── com/bank/
│       ├── Main.java               # Main entry point
│       ├── BankSystemTest.java     # Automated test suite
│       ├── model/                  # Domain classes (Account, Customer, Transaction, etc.)
│       ├── service/                # Business logic, auth, and file storage
│       ├── ui/                     # Terminal user interface
│       └── report/                 # PDF and DOCX report generator
├── compile.bat                     # Windows script to compile
├── run.bat                         # Windows script to run the app
├── generate_report.bat             # Generates report files
├── statement.md                    # Problem statement and scope
├── projectreport.pdf               # Project report document
└── README.md                       # Project documentation
```

---

## How to Run

### Prerequisites
- Java JDK 17 or higher (tested with JDK 25)

### Running on Windows
1. **Compile:**
   ```cmd
   compile.bat
   ```
2. **Run:**
   ```cmd
   run.bat
   ```
3. **Generate Reports:**
   ```cmd
   generate_report.bat
   ```

### Running via Terminal / PowerShell
```powershell
# Compile
javac -d bin src/com/bank/model/*.java src/com/bank/service/*.java src/com/bank/ui/*.java src/com/bank/report/*.java src/com/bank/*.java

# Run
java -cp bin com.bank.Main

# Run Tests
java -ea -cp bin com.bank.BankSystemTest
```

---

## Default Demo Accounts

The system creates the following demo accounts on initial startup:

### Customer Accounts
| Account No | Account Holder | Type | Default PIN |
|---|---|---|---|
| `SB-1001` | Alice Johnson | Savings | `1234` |
| `CA-2001` | Bob Martinez | Current | `4321` |
| `SB-1002` | Charlie Davis | Savings | `9999` |

### Admin Account
- **Username:** `admin`
- **Password:** `admin123`
