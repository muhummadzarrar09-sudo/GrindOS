# ==========================================
# GrindOS - Environment Setup Script
# Checks and installs prerequisites
# ==========================================

param(
    [switch]$Help
)

$ErrorActionPreference = "Stop"

function Show-Banner {
    Write-Host ""
    Write-Host "  ╔══════════════════════════════════════╗" -ForegroundColor Magenta
    Write-Host "  ║      GrindOS Setup Wizard 🛠️        ║" -ForegroundColor Magenta
    Write-Host "  ╚══════════════════════════════════════╝" -ForegroundColor Magenta
    Write-Host ""
}

function Show-Help {
    Write-Host "GrindOS Setup Script" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "This script verifies all prerequisites for building GrindOS." -ForegroundColor White
    Write-Host ""
    Write-Host "Prerequisites:" -ForegroundColor Yellow
    Write-Host "  1. JDK 17+ (JAVA_HOME must be set)" -ForegroundColor Gray
    Write-Host "  2. Android SDK (ANDROID_HOME must be set)" -ForegroundColor Gray
    Write-Host "  3. Android SDK Build-Tools 35.0.0+" -ForegroundColor Gray
    Write-Host "  4. Android SDK Platform API 36" -ForegroundColor Gray
    Write-Host "  5. Gradle 8.13 (auto-downloaded via wrapper)" -ForegroundColor Gray
    Write-Host ""
    Write-Host "Quick Setup (Windows):" -ForegroundColor Cyan
    Write-Host "  1. Install JDK 17: https://adoptium.net/" -ForegroundColor Gray
    Write-Host "  2. Install Android cmdline tools: https://developer.android.com/studio#command-line-tools-only" -ForegroundColor Gray
    Write-Host "  3. Set environment variables (see below)" -ForegroundColor Gray
    Write-Host "  4. Run: .\setup.ps1" -ForegroundColor Gray
    Write-Host ""
    Write-Host "Environment Variables:" -ForegroundColor Cyan
    Write-Host '  $env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17..."' -ForegroundColor Gray
    Write-Host '  $env:ANDROID_HOME = "$env:LOCALAPPDATA\Android\Sdk"' -ForegroundColor Gray
    Write-Host '  $env:Path += ";$env:JAVA_HOME\bin;$env:ANDROID_HOME\platform-tools"' -ForegroundColor Gray
    Write-Host ""
    exit 0
}

function Test-Java {
    Write-Host "[1/4] Checking Java..." -ForegroundColor Yellow

    if (-not $env:JAVA_HOME) {
        Write-Host "  ❌ JAVA_HOME not set" -ForegroundColor Red
        Write-Host "  ➜ Install JDK 17+ from https://adoptium.net/" -ForegroundColor Yellow
        Write-Host "  ➜ Then set: [System.Environment]::SetEnvironmentVariable('JAVA_HOME', 'path\to\jdk', 'User')" -ForegroundColor Gray
        return $false
    }

    try {
        $version = & "$env:JAVA_HOME\bin\java" -version 2>&1 | Select-Object -First 1
        $majorVersion = if ($version -match '"(\d+)') { [int]$Matches[1] } else { 0 }

        if ($majorVersion -ge 17) {
            Write-Host "  ✅ Java $majorVersion found ($env:JAVA_HOME)" -ForegroundColor Green
            return $true
        } else {
            Write-Host "  ❌ Java $majorVersion found, need 17+" -ForegroundColor Red
            return $false
        }
    } catch {
        Write-Host "  ❌ Java not found at JAVA_HOME: $env:JAVA_HOME" -ForegroundColor Red
        return $false
    }
}

function Test-AndroidSdk {
    Write-Host "[2/4] Checking Android SDK..." -ForegroundColor Yellow

    $sdkPath = $env:ANDROID_HOME
    if (-not $sdkPath) { $sdkPath = $env:ANDROID_SDK_ROOT }

    if (-not $sdkPath) {
        Write-Host "  ❌ ANDROID_HOME not set" -ForegroundColor Red
        Write-Host "  ➜ Install Android SDK command-line tools:" -ForegroundColor Yellow
        Write-Host "     https://developer.android.com/studio#command-line-tools-only" -ForegroundColor Gray
        Write-Host "  ➜ Then set: [System.Environment]::SetEnvironmentVariable('ANDROID_HOME', 'path\to\sdk', 'User')" -ForegroundColor Gray
        return $false
    }

    if (-not (Test-Path $sdkPath)) {
        Write-Host "  ❌ SDK path does not exist: $sdkPath" -ForegroundColor Red
        return $false
    }

    Write-Host "  ✅ Android SDK found ($sdkPath)" -ForegroundColor Green

    # Check for required SDK components
    $platforms = Join-Path $sdkPath "platforms"
    $buildTools = Join-Path $sdkPath "build-tools"
    $platformTools = Join-Path $sdkPath "platform-tools"

    # Check platforms
    $hasApi36 = $false
    if (Test-Path $platforms) {
        $hasApi36 = Get-ChildItem $platforms -Directory | Where-Object { $_.Name -match "android-3[56]" }
    }
    if ($hasApi36) {
        Write-Host "  ✅ SDK Platform API 35/36 found" -ForegroundColor Green
    } else {
        Write-Host "  ⚠️  SDK Platform API 36 not found" -ForegroundColor Yellow
        Write-Host "  ➜ Install with: sdkmanager ""platforms;android-36""" -ForegroundColor Gray
    }

    # Check build tools
    if (Test-Path $buildTools) {
        $tools = Get-ChildItem $buildTools -Directory | Sort-Object Name -Descending | Select-Object -First 1
        Write-Host "  ✅ Build-Tools $($tools.Name) found" -ForegroundColor Green
    } else {
        Write-Host "  ⚠️  Build-Tools not found" -ForegroundColor Yellow
        Write-Host "  ➜ Install with: sdkmanager ""build-tools;35.0.0""" -ForegroundColor Gray
    }

    # Check platform-tools (adb)
    if (Test-Path $platformTools) {
        Write-Host "  ✅ Platform-Tools (adb) found" -ForegroundColor Green
    } else {
        Write-Host "  ⚠️  Platform-Tools not found" -ForegroundColor Yellow
        Write-Host "  ➜ Install with: sdkmanager ""platform-tools""" -ForegroundColor Gray
    }

    return $true
}

function Test-Gradle {
    Write-Host "[3/4] Checking Gradle..." -ForegroundColor Yellow

    $wrapperProps = Join-Path $PSScriptRoot "gradle\wrapper\gradle-wrapper.properties"
    if (Test-Path $wrapperProps) {
        $distUrl = Get-Content $wrapperProps | Where-Object { $_ -match "distributionUrl" }
        $gradleVersion = if ($distUrl -match "gradle-([\d.]+)") { $Matches[1] } else { "unknown" }
        Write-Host "  ✅ Gradle wrapper configured (v$gradleVersion)" -ForegroundColor Green
        Write-Host "  ℹ️  Gradle will auto-download on first build" -ForegroundColor Gray
        return $true
    } else {
        Write-Host "  ❌ gradle-wrapper.properties not found" -ForegroundColor Red
        return $false
    }
}

function Initialize-GradleWrapper {
    Write-Host "[4/4] Initializing Gradle wrapper..." -ForegroundColor Yellow

    $gradlew = Join-Path $PSScriptRoot "gradlew.bat"
    if (Test-Path $gradlew) {
        Write-Host "  ✅ gradlew.bat already exists" -ForegroundColor Green
        return
    }

    # Create gradlew.bat
    $gradlewContent = @'
@rem
@rem Gradle startup script for Windows
@rem
@if "%DEBUG%"=="" @echo off
@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
@rem This is normally unused
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

set DEFAULT_JVM_OPTS="-Xmx64m" "-Xms64m"

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH. 1>&2
goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME% 1>&2
goto fail

:execute
@rem Execute Gradle
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% "-Dorg.gradle.appname=%APP_BASE_NAME%" -classpath "%APP_HOME%\gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*

:end
@rem End local scope for the variables with windows NT shell
if %OS%==Windows_NT endlocal

:omega
exit /b %ERRORLEVEL%

:fail
exit /b 1
'@
    Set-Content -Path $gradlew -Value $gradlewContent -Encoding UTF8
    Write-Host "  ✅ Created gradlew.bat" -ForegroundColor Green

    # Create gradlew (bash)
    $gradlewBash = Join-Path $PSScriptRoot "gradlew"
    $gradlewBashContent = @'
#!/bin/sh
DIRNAME=$(dirname "$0")
APP_HOME=$(cd "$DIRNAME" && pwd)
exec "$JAVA_HOME/bin/java" $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS \
    "-Dorg.gradle.appname=gradlew" \
    -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" \
    org.gradle.wrapper.GradleWrapperMain "$@"
'@
    Set-Content -Path $gradlewBash -Value $gradlewBashContent -Encoding UTF8
    Write-Host "  ✅ Created gradlew (bash)" -ForegroundColor Green

    # Check if gradle-wrapper.jar exists
    $wrapperJar = Join-Path $PSScriptRoot "gradle\wrapper\gradle-wrapper.jar"
    if (-not (Test-Path $wrapperJar)) {
        Write-Host "  ⚠️  gradle-wrapper.jar missing!" -ForegroundColor Yellow
        Write-Host "  ➜ Download from: https://raw.githubusercontent.com/gradle/gradle/v8.13.0/gradle/wrapper/gradle-wrapper.jar" -ForegroundColor Gray
        Write-Host "  ➜ Place it in: gradle\wrapper\gradle-wrapper.jar" -ForegroundColor Gray
        Write-Host ""
        Write-Host "  OR install Gradle locally and run: gradle wrapper" -ForegroundColor Gray
    } else {
        Write-Host "  ✅ gradle-wrapper.jar found" -ForegroundColor Green
    }
}

# ---- Main ----
Show-Banner

if ($Help) { Show-Help }

$javaOk = Test-Java
$sdkOk = Test-AndroidSdk
$gradleOk = Test-Gradle
Initialize-GradleWrapper

Write-Host ""
Write-Host "  ─────────────────────────────────────" -ForegroundColor DarkGray
Write-Host ""

if ($javaOk -and $sdkOk -and $gradleOk) {
    Write-Host "  ✅ All prerequisites met! Ready to build." -ForegroundColor Green
    Write-Host ""
    Write-Host "  Run: .\build.ps1" -ForegroundColor Cyan
    Write-Host ""
} else {
    Write-Host "  ⚠️  Some prerequisites are missing." -ForegroundColor Yellow
    Write-Host "  Fix the issues above, then run .\build.ps1" -ForegroundColor Yellow
    Write-Host ""
}
