# ==========================================
# GrindOS - PowerShell Build Script
# Builds the GrindOS.apk from command line
# ==========================================
# Usage:
#   .\build.ps1                  # Debug APK
#   .\build.ps1 -Release         # Release APK
#   .\build.ps1 -Clean           # Clean then build
#   .\build.ps1 -Install         # Build and install to connected device
#   .\build.ps1 -Release -Install  # Release build + install
# ==========================================

param(
    [switch]$Release,
    [switch]$Clean,
    [switch]$Install,
    [switch]$Help
)

$ErrorActionPreference = "Stop"
$ProjectRoot = $PSScriptRoot

# ---- Banner ----
function Show-Banner {
    Write-Host ""
    Write-Host "  ╔══════════════════════════════════════╗" -ForegroundColor Cyan
    Write-Host "  ║        GrindOS Build System ⚡       ║" -ForegroundColor Cyan
    Write-Host "  ║     Personal Command Center v1.0     ║" -ForegroundColor Cyan
    Write-Host "  ╚══════════════════════════════════════╝" -ForegroundColor Cyan
    Write-Host ""
}

# ---- Help ----
function Show-Help {
    Write-Host "GrindOS Build Script" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Usage:" -ForegroundColor White
    Write-Host "  .\build.ps1                  Build debug APK" -ForegroundColor Gray
    Write-Host "  .\build.ps1 -Release         Build release APK" -ForegroundColor Gray
    Write-Host "  .\build.ps1 -Clean           Clean build artifacts first" -ForegroundColor Gray
    Write-Host "  .\build.ps1 -Install         Build and install to device" -ForegroundColor Gray
    Write-Host "  .\build.ps1 -Release -Install  Release build + install" -ForegroundColor Gray
    Write-Host "  .\build.ps1 -Help            Show this help" -ForegroundColor Gray
    Write-Host ""
    Write-Host "Prerequisites:" -ForegroundColor Yellow
    Write-Host "  - JDK 17+ installed and JAVA_HOME set" -ForegroundColor Gray
    Write-Host "  - Android SDK installed and ANDROID_HOME set" -ForegroundColor Gray
    Write-Host "  - Android SDK Build-Tools 35.0.0+" -ForegroundColor Gray
    Write-Host "  - Android SDK Platform API 36" -ForegroundColor Gray
    Write-Host ""
    exit 0
}

# ---- Prerequisites Check ----
function Test-Prerequisites {
    Write-Host "[1/5] Checking prerequisites..." -ForegroundColor Yellow

    # Check JAVA_HOME
    if (-not $env:JAVA_HOME) {
        Write-Host "  ❌ JAVA_HOME not set!" -ForegroundColor Red
        Write-Host "  Install JDK 17+ and set JAVA_HOME environment variable." -ForegroundColor Red
        Write-Host "  Download: https://adoptium.net/" -ForegroundColor Gray
        exit 1
    }

    $javaVersion = & "$env:JAVA_HOME\bin\java" -version 2>&1 | Select-Object -First 1
    Write-Host "  ✅ Java: $javaVersion" -ForegroundColor Green

    # Check ANDROID_HOME
    if (-not $env:ANDROID_HOME -and -not $env:ANDROID_SDK_ROOT) {
        Write-Host "  ❌ ANDROID_HOME not set!" -ForegroundColor Red
        Write-Host "  Install Android SDK and set ANDROID_HOME environment variable." -ForegroundColor Red
        Write-Host "  Download: https://developer.android.com/studio#command-line-tools-only" -ForegroundColor Gray
        exit 1
    }

    $sdkPath = if ($env:ANDROID_HOME) { $env:ANDROID_HOME } else { $env:ANDROID_SDK_ROOT }
    Write-Host "  ✅ Android SDK: $sdkPath" -ForegroundColor Green

    # Check Gradle wrapper exists
    $gradlewPath = Join-Path $ProjectRoot "gradlew.bat"
    if (-not (Test-Path $gradlewPath)) {
        Write-Host "  ⚠️  gradlew.bat not found, generating wrapper..." -ForegroundColor Yellow
        # Generate gradle wrapper if missing
        $gradleWrapperJar = Join-Path $ProjectRoot "gradle\wrapper\gradle-wrapper.jar"
        if (-not (Test-Path $gradleWrapperJar)) {
            Write-Host "  ❌ Gradle wrapper JAR missing. Run 'gradle wrapper' first." -ForegroundColor Red
            Write-Host "  Install Gradle: https://gradle.org/install/" -ForegroundColor Gray
            exit 1
        }
    }

    Write-Host "  ✅ Gradle wrapper found" -ForegroundColor Green
    Write-Host ""
}

# ---- Clean ----
function Invoke-Clean {
    Write-Host "[2/5] Cleaning build artifacts..." -ForegroundColor Yellow

    Push-Location $ProjectRoot
    try {
        & ".\gradlew.bat" clean 2>&1 | ForEach-Object {
            if ($_ -match "BUILD SUCCESSFUL") {
                Write-Host "  ✅ Clean successful" -ForegroundColor Green
            } elseif ($_ -match "FAILED") {
                Write-Host "  ❌ Clean failed" -ForegroundColor Red
            }
        }
    }
    finally {
        Pop-Location
    }
    Write-Host ""
}

# ---- Build ----
function Invoke-Build {
    $buildType = if ($Release) { "Release" } else { "Debug" }
    $task = "assemble$buildType"

    Write-Host "[3/5] Building $buildType APK..." -ForegroundColor Yellow
    Write-Host "  Task: $task" -ForegroundColor Gray
    Write-Host "  This may take a few minutes on first build..." -ForegroundColor Gray
    Write-Host ""

    Push-Location $ProjectRoot
    try {
        $buildOutput = & ".\gradlew.bat" $task --no-daemon --stacktrace 2>&1
        $buildOutput | ForEach-Object {
            $line = $_.ToString()
            if ($line -match "BUILD SUCCESSFUL") {
                Write-Host "  ✅ $line" -ForegroundColor Green
            } elseif ($line -match "FAILED") {
                Write-Host "  ❌ $line" -ForegroundColor Red
            } elseif ($line -match "^>") {
                Write-Host "  $line" -ForegroundColor Gray
            } elseif ($line -match "error:" -or $line -match "Error") {
                Write-Host "  ⚠️  $line" -ForegroundColor Yellow
            }
        }

        if ($LASTEXITCODE -ne 0) {
            Write-Host ""
            Write-Host "  ❌ Build FAILED (exit code: $LASTEXITCODE)" -ForegroundColor Red
            Write-Host "  Check the error messages above for details." -ForegroundColor Red
            Write-Host ""
            exit 1
        }
    }
    finally {
        Pop-Location
    }
    Write-Host ""
}

# ---- Locate APK ----
function Get-ApkPath {
    $buildType = if ($Release) { "release" } else { "debug" }
    $apkDir = Join-Path $ProjectRoot "app\build\outputs\apk\$buildType"

    if (-not (Test-Path $apkDir)) {
        Write-Host "  ❌ APK output directory not found: $apkDir" -ForegroundColor Red
        return $null
    }

    $apk = Get-ChildItem -Path $apkDir -Filter "*.apk" | Select-Object -First 1
    if (-not $apk) {
        Write-Host "  ❌ No APK file found in: $apkDir" -ForegroundColor Red
        return $null
    }

    return $apk.FullName
}

# ---- Show Results ----
function Show-Results {
    Write-Host "[4/5] Build Results" -ForegroundColor Yellow

    $apkPath = Get-ApkPath
    if ($apkPath) {
        $apkSize = [math]::Round((Get-Item $apkPath).Length / 1MB, 2)
        Write-Host "  ✅ APK built successfully!" -ForegroundColor Green
        Write-Host "  📦 Location: $apkPath" -ForegroundColor White
        Write-Host "  📏 Size: ${apkSize} MB" -ForegroundColor White

        # Copy to project root for easy access
        $destApk = Join-Path $ProjectRoot "GrindOS.apk"
        Copy-Item $apkPath $destApk -Force
        Write-Host "  📋 Copied to: $destApk" -ForegroundColor Cyan
    }
    Write-Host ""
}

# ---- Install ----
function Install-Apk {
    if (-not $Install) { return }

    Write-Host "[5/5] Installing to device..." -ForegroundColor Yellow

    $apkPath = Get-ApkPath
    if (-not $apkPath) { return }

    # Check for adb
    $sdkPath = if ($env:ANDROID_HOME) { $env:ANDROID_HOME } else { $env:ANDROID_SDK_ROOT }
    $adbPath = Join-Path $sdkPath "platform-tools\adb.exe"

    if (-not (Test-Path $adbPath)) {
        Write-Host "  ❌ adb not found at: $adbPath" -ForegroundColor Red
        Write-Host "  Install Android SDK Platform-Tools." -ForegroundColor Red
        return
    }

    # Check connected devices
    $devices = & $adbPath devices | Select-Object -Skip 1 | Where-Object { $_ -match "device$" }
    if (-not $devices) {
        Write-Host "  ❌ No device connected! Enable USB debugging and connect device." -ForegroundColor Red
        return
    }

    Write-Host "  📱 Installing GrindOS.apk..." -ForegroundColor Gray
    & $adbPath install -r $apkPath
    if ($LASTEXITCODE -eq 0) {
        Write-Host "  ✅ Installed successfully!" -ForegroundColor Green

        # Launch the app
        & $adbPath shell am start -n com.grindos.app/.MainActivity
        Write-Host "  🚀 App launched!" -ForegroundColor Green
    } else {
        Write-Host "  ❌ Installation failed!" -ForegroundColor Red
    }
    Write-Host ""
}

# ---- Main ----
Show-Banner

if ($Help) { Show-Help }

Test-Prerequisites

if ($Clean) { Invoke-Clean }

Invoke-Build
Show-Results
Install-Apk

Write-Host "  ╔══════════════════════════════════════╗" -ForegroundColor Green
Write-Host "  ║         Build Complete! 🔥           ║" -ForegroundColor Green
Write-Host "  ╚══════════════════════════════════════╝" -ForegroundColor Green
Write-Host ""
