# GrindOS Clean Script
# Removes all build artifacts

$ErrorActionPreference = "Stop"
$ProjectRoot = $PSScriptRoot

Write-Host ""
Write-Host "  🧹 Cleaning GrindOS build artifacts..." -ForegroundColor Yellow
Write-Host ""

Push-Location $ProjectRoot
try {
    # Run Gradle clean
    if (Test-Path ".\gradlew.bat") {
        & ".\gradlew.bat" clean --no-daemon 2>&1 | ForEach-Object {
            if ($_ -match "BUILD SUCCESSFUL") {
                Write-Host "  ✅ Gradle clean successful" -ForegroundColor Green
            }
        }
    }

    # Remove build directories
    $buildDirs = @(
        "app\build",
        "build",
        ".gradle"
    )

    foreach ($dir in $buildDirs) {
        $fullPath = Join-Path $ProjectRoot $dir
        if (Test-Path $fullPath) {
            Remove-Item $fullPath -Recurse -Force
            Write-Host "  🗑️  Removed: $dir" -ForegroundColor Gray
        }
    }

    # Remove copied APK
    $apk = Join-Path $ProjectRoot "GrindOS.apk"
    if (Test-Path $apk) {
        Remove-Item $apk -Force
        Write-Host "  🗑️  Removed: GrindOS.apk" -ForegroundColor Gray
    }

    Write-Host ""
    Write-Host "  ✅ Clean complete!" -ForegroundColor Green
    Write-Host ""
}
finally {
    Pop-Location
}
