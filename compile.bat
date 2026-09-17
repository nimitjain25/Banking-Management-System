@echo off
setlocal enabledelayedexpansion

echo ===================================================
echo   Compiling Banking Management System (Java)
echo ===================================================

:: Check for JDK javac
set "JAVAC_CMD=javac"
where javac >nul 2>nul
if %errorlevel% neq 0 (
    if exist "%USERPROFILE%\.jdk\jdk-25.0.2\bin\javac.exe" (
        set "JAVAC_CMD=%USERPROFILE%\.jdk\jdk-25.0.2\bin\javac.exe"
    ) else (
        echo [ERROR] javac compiler not found on PATH or in %%USERPROFILE%%\.jdk\jdk-25.0.2\bin!
        echo Please ensure a Java Development Kit (JDK) is installed.
        pause
        exit /b 1
    )
)

if not exist bin mkdir bin

echo Using compiler: !JAVAC_CMD!
echo Compiling source files from src/...

:: Find all java files and compile
dir /s /b src\*.java > sources.tmp
"!JAVAC_CMD!" -encoding UTF-8 -d bin @sources.tmp
set COMPILE_STATUS=%errorlevel%
del sources.tmp

if %COMPILE_STATUS% equ 0 (
    echo.
    echo [SUCCESS] Compilation successful! Class files are ready in bin\
) else (
    echo.
    echo [ERROR] Compilation failed with error code %COMPILE_STATUS%.
)

endlocal
pause
