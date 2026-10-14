# Sets environment variables for ConAn in the current PowerShell session.
# Usage (from conan5.1):  .\bin\setenv.ps1
# CONAN_HOME is worked out from this file's location (the folder above bin).
$sep = [IO.Path]::PathSeparator
$env:CONAN_HOME = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
$env:ROAST_HOME = Join-Path $env:CONAN_HOME 'roast2.0'
$env:PATH = $env:PATH + $sep + (Join-Path $env:CONAN_HOME 'bin')
$env:CLASSPATH = (@($env:CLASSPATH,
                    (Join-Path $env:CONAN_HOME 'classes'),
                    (Join-Path $env:ROAST_HOME 'classes'),
                    '.') | Where-Object { $_ }) -join $sep
Write-Host "CONAN_HOME=$env:CONAN_HOME"
