@echo off
cd /d "%~dp0"
where java >nul 2>nul
if errorlevel 1 (
    echo Java 17 or newer is required. Please install Java and try again.
    pause
    exit /b 1
)
if exist CampusLostAndFoundTracker.jar (
    java -jar CampusLostAndFoundTracker.jar
) else (
    call run_from_source.bat
)
if errorlevel 1 pause
