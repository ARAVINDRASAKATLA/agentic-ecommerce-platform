# ====================================================================
# AGENTIC E-COMMERCE PLATFORM - LOCAL SERVICE RUNNER
# ====================================================================

param(
    [string]$DbPassword = ""
)

$ErrorActionPreference = "Continue"

Write-Host "=========================================================" -ForegroundColor Cyan
Write-Host " Starting Agentic E-Commerce Microservices Platform      " -ForegroundColor Cyan
Write-Host "=========================================================" -ForegroundColor Cyan

# 1. Load .env file if present
$envFile = Join-Path $PSScriptRoot ".env"
if (Test-Path $envFile) {
    Write-Host "[INFO] Loading environment variables from .env..." -ForegroundColor Green
    Get-Content $envFile | ForEach-Object {
        $line = $_.Trim()
        if ($line -and -not $line.StartsWith("#") -and $line.Contains("=")) {
            $parts = $line.Split("=", 2)
            $varName = $parts[0].Trim()
            $varVal = $parts[1].Trim()
            [System.Environment]::SetEnvironmentVariable($varName, $varVal, "Process")
        }
    }
}

# 2. Check or set parameters
if ($DbPassword) {
    $env:DB_PASSWORD = $DbPassword
}

if (-not $env:DB_URL) {
    $env:DB_URL = "jdbc:mysql://mysql-23d061c1-aravindrasakatla7-b9ba.e.aivencloud.com:19407/defaultdb?sslMode=REQUIRED"
}
if (-not $env:DB_USERNAME) {
    $env:DB_USERNAME = "avnadmin"
}
if (-not $env:JWT_SECRET) {
    $env:JWT_SECRET = "SuperSecretDefaultKeyForDevelopmentOnly1234567890"
}
if (-not $env:GEMINI_API_KEY) {
    $env:GEMINI_API_KEY = "AIzaSyAmwwNDmlDgB2yDvYu5s2_kAQ7fU4ulUss"
}
if (-not $env:KAFKA_AUTO_STARTUP) {
    $env:KAFKA_AUTO_STARTUP = "false"
}

if (-not $env:DB_PASSWORD -or $env:DB_PASSWORD -eq "CHANGE_ME") {
    Write-Host "[WARNING] DB_PASSWORD is not set or set to 'CHANGE_ME'." -ForegroundColor Yellow
    Write-Host "          JPA services (User, Product, Inventory, Delivery, Tracking)" -ForegroundColor Yellow
    Write-Host "          will fail to connect to Aiven MySQL without your DB password." -ForegroundColor Yellow
    Write-Host "          You can provide it via: .\start-all.ps1 -DbPassword '<your-password>'" -ForegroundColor Yellow
    Write-Host "          or save it in .env file as DB_PASSWORD=<your-password>" -ForegroundColor Yellow
    Write-Host ""
}

# 3. Create logs directory
$logDir = Join-Path $PSScriptRoot "logs"
if (-not (Test-Path $logDir)) {
    New-Item -ItemType Directory -Path $logDir | Out-Null
}

$jvmArgs = "-Xms128m -Xmx256m"

function Start-Microservice {
    param(
        [string]$Name,
        [string]$Folder,
        [int]$Port
    )

    Write-Host "[STARTING] $Name on port $Port..." -ForegroundColor Cyan
    $servicePath = Join-Path $PSScriptRoot $Folder
    $logFile = Join-Path $logDir "$Name.log"

    $cmd = "cd /d `"$servicePath`" && set DB_URL=$($env:DB_URL) && set DB_USERNAME=$($env:DB_USERNAME) && set DB_PASSWORD=$($env:DB_PASSWORD) && set JWT_SECRET=$($env:JWT_SECRET) && set GEMINI_API_KEY=$($env:GEMINI_API_KEY) && set KAFKA_AUTO_STARTUP=$($env:KAFKA_AUTO_STARTUP) && .\mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments=`"$jvmArgs`" > `"$logFile`" 2>&1"

    $process = Start-Process "cmd.exe" -ArgumentList "/c $cmd" -PassThru -WindowStyle Hidden
    Write-Host "[OK] $Name launched (PID: $($process.Id)). Log: logs/$Name.log" -ForegroundColor Green
    return $process
}

# 4. Step 1: Start Eureka Service
$eureka = Start-Microservice -Name "EurekaService" -Folder "EurekaService" -Port 8761
Write-Host "Waiting 12 seconds for Eureka Service to initialize..." -ForegroundColor Yellow
Start-Sleep -Seconds 12

# 5. Step 2: Start Core Domain Microservices
$services = @(
    @{ Name = "UserManagement"; Folder = "UserManagement"; Port = 9000 },
    @{ Name = "InventoryManagement"; Folder = "InventoryManagement"; Port = 8083 },
    @{ Name = "ProductManagement"; Folder = "ProductManagement"; Port = 8082 },
    @{ Name = "DeliveryManagement"; Folder = "DeliveryManagement"; Port = 8084 },
    @{ Name = "TrackingService"; Folder = "TrackingService"; Port = 8085 },
    @{ Name = "CustomerCareService"; Folder = "CustomerCareService"; Port = 8086 }
)

foreach ($svc in $services) {
    Start-Microservice -Name $svc.Name -Folder $svc.Folder -Port $svc.Port
    Start-Sleep -Seconds 2
}

# 6. Step 3: Start API Gateway
Write-Host "Waiting 8 seconds before launching API Gateway..." -ForegroundColor Yellow
Start-Sleep -Seconds 8
$gateway = Start-Microservice -Name "APIGateway" -Folder "APIGateway" -Port 9500

Write-Host "=========================================================" -ForegroundColor Green
Write-Host " All 8 Microservices are starting in background!         " -ForegroundColor Green
Write-Host " API Gateway:        http://localhost:9500               " -ForegroundColor Cyan
Write-Host " Eureka Dashboard:   http://localhost:8761               " -ForegroundColor Cyan
Write-Host " Logs folder:        .\logs\                             " -ForegroundColor Yellow
Write-Host " To stop all:        .\stop-all.ps1                      " -ForegroundColor Magenta
Write-Host "=========================================================" -ForegroundColor Green
