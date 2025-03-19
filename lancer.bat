@echo off
echo Compilation du projet...
javac src/model/*.java src/algorithms/*.java src/utils/*.java src/ui/*.java src/Main.java
if errorlevel 1 (
    echo Erreur lors de la compilation
    pause
    exit /b 1
)
echo.
echo Lancement de l'application...
java -cp src Main
pause 