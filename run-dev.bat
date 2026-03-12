@echo off
setlocal

set "ROOT=%~dp0"

if "%SMTP_USERNAME%"=="" (
  echo [WARN] SMTP_USERNAME is not set.
)

if "%SMTP_PASSWORD%"=="" (
  echo [WARN] SMTP_PASSWORD is not set.
)

if "%SMTP_USERNAME%"=="" (
  echo [INFO] OTP email sending will fail until SMTP env vars are configured.
)

echo Starting backend on http://localhost:8081 ...
start "Smart Home Backend" cmd /k "cd /d %ROOT%backend && mvn spring-boot:run"

timeout /t 6 /nobreak >nul

echo Starting frontend on http://localhost:3001 ...
start "Smart Home Frontend" cmd /k "cd /d %ROOT%frontend && npm start"

echo.
echo Both services launched in separate terminals.
echo Keep both windows open while using the app.

endlocal
