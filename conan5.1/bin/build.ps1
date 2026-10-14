# Compiles Roast into $env:ROAST_HOME/classes and ConAn into $env:CONAN_HOME/classes.
# Run this after changing any Roast or ConAn source file.
# Usage:  build.ps1        (after running setenv.ps1)

if (-not $env:CONAN_HOME -or -not $env:ROAST_HOME) {
    Write-Host 'CONAN_HOME and ROAST_HOME must be set. Run setenv.ps1 first.'
    exit 1
}

function Reset-Directory([string]$path) {
    if (Test-Path $path) { Remove-Item $path -Recurse -Force }
    New-Item -ItemType Directory -Path $path | Out-Null
}

function Get-JavaFiles([string[]]$dirs) {
    # Expanded here rather than passing wildcards to javac
    foreach ($d in $dirs) { (Get-ChildItem -Path $d -Filter '*.java' -File).FullName }
}

# ---- Roast (old code: warnings suppressed)
$roastClasses = Join-Path $env:ROAST_HOME 'classes'
$roastSrc     = Join-Path $env:ROAST_HOME 'roast'
$viewerSrc    = Join-Path $roastSrc 'viewer'
Reset-Directory $roastClasses
& javac -nowarn -XDsuppressNotes -d $roastClasses -cp $roastClasses @(Get-JavaFiles $roastSrc, $viewerSrc)
if ($LASTEXITCODE -ne 0) { Write-Host 'Build failed.'; exit 1 }
Copy-Item -Path (Join-Path $viewerSrc '*.gif') -Destination (Join-Path (Join-Path $roastClasses 'roast') 'viewer')
Write-Host "Roast built into $roastClasses"

# ---- ConAn: Conan.java pulls in the translator via -sourcepath;
#      runtime classes are listed explicitly
$conanClasses = Join-Path $env:CONAN_HOME 'classes'
$conanSrc     = Join-Path $env:CONAN_HOME 'conan'
Reset-Directory $conanClasses
& javac -d $conanClasses -cp $conanClasses -sourcepath $env:CONAN_HOME `
    (Join-Path $conanSrc 'Conan.java') @(Get-JavaFiles (Join-Path $conanSrc 'runtime'))
if ($LASTEXITCODE -ne 0) { Write-Host 'Build failed.'; exit 1 }
Write-Host "ConAn built into $conanClasses"
exit 0
