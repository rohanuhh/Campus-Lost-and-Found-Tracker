@echo off
cd /d "%~dp0"
where javac >nul 2>nul
if errorlevel 1 (
    echo A Java JDK 17 or newer is required.
    pause
    exit /b 1
)
if not exist out mkdir out
javac -encoding UTF-8 -d out src\campus\model\*.java src\campus\storage\*.java src\campus\ui\*.java
if errorlevel 1 (
    echo Compilation failed. Check the error messages above.
    pause
    exit /b 1
)
java -cp out campus.ui.CampusLostAndFoundTracker
if errorlevel 1 pause
