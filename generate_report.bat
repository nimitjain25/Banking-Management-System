@echo off
setlocal enabledelayedexpansion

echo ===================================================
echo   Generating Project Report (PDF and DOCX)
echo ===================================================

set "JAVA_CMD=java"
if exist "%USERPROFILE%\.jdk\jdk-25.0.2\bin\java.exe" (
    set "JAVA_CMD=%USERPROFILE%\.jdk\jdk-25.0.2\bin\java.exe"
)

if not exist bin\com\bank\Main.class (
    echo [INFO] Compiling project first...
    call compile.bat
)

"!JAVA_CMD!" -cp bin com.bank.Main --generate-report-only

echo.
echo [DONE] Generated:
echo   - 'projectreport.pdf'  (Print-ready PDF)
echo   - 'projectreport.docx' (Editable Microsoft Word document)
pause
endlocal
