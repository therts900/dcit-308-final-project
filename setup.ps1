param(
    [string]$JavaVersion = "17",
    [string]$MavenVersion = "3.9.15"
)

$ErrorActionPreference = "Continue"

Write-Host "=== Smart Campus Application Setup ===" -ForegroundColor Cyan

# Create installation directories
$javaDir = "$env:USERPROFILE\.jdk\java$JavaVersion"
$mavenDir = "$env:USERPROFILE\.maven\maven-$MavenVersion"

Write-Host "Setting up directories..." -ForegroundColor Yellow

# Check if Java 17 is already installed
if (Test-Path "$javaDir\bin\java.exe") {
    Write-Host "JDK $JavaVersion already installed at: $javaDir" -ForegroundColor Green
    $JAVA_HOME = $javaDir
}
else {
    Write-Host "Downloading OpenJDK $JavaVersion..." -ForegroundColor Yellow
    $jdkUrl = "https://github.com/adoptium/temurin$($JavaVersion)-binaries/releases/download/jdk-$($JavaVersion).0.13+11/OpenJDK$($JavaVersion)U-jdk_x64_windows_hotspot_$($JavaVersion).0.13_11.zip"
    $jdkZip = "$env:TEMP\jdk$JavaVersion.zip"
    
    try {
        Invoke-WebRequest -Uri $jdkUrl -OutFile $jdkZip -TimeoutSec 120
        Write-Host "Extracting JDK..." -ForegroundColor Yellow
        
        $tempExtract = "$env:TEMP\jdk_temp"
        Expand-Archive -Path $jdkZip -DestinationPath $tempExtract -Force
        
        $extractedFolder = Get-ChildItem -Path $tempExtract -Directory | Select-Object -First 1
        
        New-Item -ItemType Directory -Path $javaDir -Force | Out-Null
        Move-Item -Path "$($extractedFolder.FullName)\*" -Destination $javaDir -Force
        
        Remove-Item -Path $tempExtract -Recurse -Force
        Remove-Item -Path $jdkZip -Force
        
        Write-Host "JDK $JavaVersion installed successfully" -ForegroundColor Green
        $JAVA_HOME = $javaDir
    }
    catch {
        Write-Host "Failed to install JDK: $_" -ForegroundColor Red
    }
}

# Check if Maven is already installed
if (Test-Path "$mavenDir\bin\mvn.cmd") {
    Write-Host "Maven $MavenVersion already installed at: $mavenDir" -ForegroundColor Green
    $MAVEN_HOME = $mavenDir
}
else {
    Write-Host "Downloading Maven $MavenVersion..." -ForegroundColor Yellow
    $mavenUrl = "https://archive.apache.org/dist/maven/maven-3/$MavenVersion/binaries/apache-maven-$MavenVersion-bin.zip"
    $mavenZip = "$env:TEMP\maven.zip"
    
    try {
        Invoke-WebRequest -Uri $mavenUrl -OutFile $mavenZip -TimeoutSec 120
        Write-Host "Extracting Maven..." -ForegroundColor Yellow
        
        $tempExtract = "$env:TEMP\maven_temp"
        Expand-Archive -Path $mavenZip -DestinationPath $tempExtract -Force
        
        $extractedFolder = Get-ChildItem -Path $tempExtract -Directory | Select-Object -First 1
        
        New-Item -ItemType Directory -Path $mavenDir -Force | Out-Null
        Move-Item -Path "$($extractedFolder.FullName)\*" -Destination $mavenDir -Force
        
        Remove-Item -Path $tempExtract -Recurse -Force
        Remove-Item -Path $mavenZip -Force
        
        Write-Host "Maven $MavenVersion installed successfully" -ForegroundColor Green
        $MAVEN_HOME = $mavenDir
    }
    catch {
        Write-Host "Failed to install Maven: $_" -ForegroundColor Red
    }
}

# Set environment variables
Write-Host "Setting environment variables..." -ForegroundColor Yellow
$env:JAVA_HOME = $JAVA_HOME
$env:MAVEN_HOME = $MAVEN_HOME
$env:PATH = "$MAVEN_HOME\bin;$JAVA_HOME\bin;$env:PATH"

# Verify installations
Write-Host "`n=== Verification ===" -ForegroundColor Cyan
& "$JAVA_HOME\bin\java.exe" -version 2>&1 | Write-Host
& "$MAVEN_HOME\bin\mvn.cmd" -version 2>&1 | Select-Object -First 3 | Write-Host

Write-Host "`n=== Setup Complete ===" -ForegroundColor Green
Write-Host "Environment variables are set for this session"
