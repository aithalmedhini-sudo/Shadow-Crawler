@echo off
setlocal
cd /d "%~dp0"

if defined JAVA_HOME (
    set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
) else if exist "C:\Program Files\JetBrains\IntelliJ IDEA 2026.3\jbr\bin\java.exe" (
    set "JAVA_EXE=C:\Program Files\JetBrains\IntelliJ IDEA 2026.3\jbr\bin\java.exe"
) else (
    set "JAVA_EXE=java"
)

echo ========================================================
echo   Starting 3D Dungeon Crawler JavaFX Edition (FPS)
echo ========================================================

"%JAVA_EXE%" --module-path target\fx-libs --add-modules javafx.controls,javafx.graphics,javafx.swing -jar target\dungeon-crawler-javafx-1.0.0.jar

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Game exited with error code %ERRORLEVEL%.
    pause
)
endlocal
