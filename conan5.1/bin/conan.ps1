# Builds a ConAn test driver.
# Usage:  conan.ps1 ScriptName   (reads ScriptName.conan, writes and compiles ScriptName.java)
# Execute the resultant driver using the "java" command.
param(
    [Parameter(Mandatory = $true)]
    [string]$Name
)

if (-not $env:CONAN_HOME -or -not $env:ROAST_HOME) {
    Write-Host 'CONAN_HOME and ROAST_HOME must be set. Run setenv.ps1 first.'
    exit 1
}

# Runs a program with stdin (and optionally stdout) connected to files, copying
# bytes unchanged. PowerShell's own "<" isn't supported, ">" re-encodes output
# (UTF-16 in Windows PowerShell 5.1), and Start-Process alters line endings.
function Invoke-Redirected([string]$program, [string[]]$arguments, [string]$inFile, [string]$outFile) {
    $psi = New-Object System.Diagnostics.ProcessStartInfo
    $psi.FileName = $program
    $psi.Arguments = ($arguments | ForEach-Object { if ($_ -match '\s') { '"' + $_ + '"' } else { $_ } }) -join ' '
    $psi.WorkingDirectory = (Get-Location).Path
    $psi.UseShellExecute = $false
    $psi.RedirectStandardInput = $true
    $psi.RedirectStandardOutput = [bool]$outFile

    $proc = [System.Diagnostics.Process]::Start($psi)
    $outStream = $null
    $copyOut = $null
    if ($outFile) {
        # read stdout while stdin is written, so neither side can block
        $outStream = [System.IO.File]::Create($outFile)
        $copyOut = $proc.StandardOutput.BaseStream.CopyToAsync($outStream)
    }
    $inStream = [System.IO.File]::OpenRead($inFile)
    try {
        $inStream.CopyTo($proc.StandardInput.BaseStream)
    } catch [System.IO.IOException] {
        # program exited without reading all of its input
    } finally {
        $inStream.Dispose()
        try { $proc.StandardInput.Close() } catch [System.IO.IOException] { }
    }
    if ($copyOut) { $copyOut.Wait(); $outStream.Dispose() }
    $proc.WaitForExit()
    return $proc.ExitCode
}

$here       = (Get-Location).Path
$conanFile  = Join-Path $here "$Name.conan"
$scriptFile = Join-Path $here "$Name.script"
if (-not (Test-Path $conanFile)) { Write-Host "Cannot find $conanFile"; exit 1 }

$conanClass = Join-Path $env:CONAN_HOME (Join-Path 'classes' (Join-Path 'conan' 'Conan.class'))
$roastClass = Join-Path $env:ROAST_HOME (Join-Path 'classes' (Join-Path 'roast' 'Roast.class'))
if (-not (Test-Path $conanClass) -or -not (Test-Path $roastClass)) {
    & (Join-Path $PSScriptRoot 'build.ps1')
    if ($LASTEXITCODE -ne 0) { exit 1 }
}

$roast = Join-Path (Join-Path $env:ROAST_HOME 'bin') 'roast'
if ((Invoke-Redirected 'perl' @($roast, '-nocheck') $conanFile $scriptFile) -ne 0) { exit 1 }
if ((Invoke-Redirected 'java' @('conan.Conan', $Name) $scriptFile $null) -ne 0) { exit 1 }
& javac "$Name.java"
exit $LASTEXITCODE
