# ====================================================================
# AGENTIC E-COMMERCE PLATFORM - STOP ALL RUNNING SERVICES
# ====================================================================

Write-Host "Stopping all Spring Boot microservices..." -ForegroundColor Yellow

$ports = @(8761, 9500, 9000, 8082, 8083, 8084, 8085, 8086)

foreach ($port in $ports) {
    try {
        $connections = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue
        if ($connections) {
            foreach ($conn in $connections) {
                $pidToKill = $conn.OwningProcess
                if ($pidToKill -gt 0) {
                    Write-Host "Killing process on port $port (PID: $pidToKill)..." -ForegroundColor Cyan
                    Stop-Process -Id $pidToKill -Force -ErrorAction SilentlyContinue
                }
            }
        }
    } catch {
        # ignore
    }
}

Write-Host "All microservices stopped." -ForegroundColor Green
