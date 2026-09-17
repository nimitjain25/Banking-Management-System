@echo off
setlocal enabledelayedexpansion

echo ===================================================
echo   Launching Banking Management System
echo ===================================================

:: Check for Java runtime
set "JAVA_CMD=java"
if exist "%USERPROFILE%\.jdk\jdk-25.0.2\bin\java.exe" (
    set "JAVA_CMD=%USERPROFILE%\.jdk\jdk-25.0.2\bin\java.exe"
)

if not exist bin\com\bank\Main.class (
    echo [INFO] Compiled classes not found. Invoking compile.bat first...
    call compile.bat
)

echo Starting application...
"!JAVA_CMD!" -cp bin com.bank.Main

endlocal
