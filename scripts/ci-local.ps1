param(
    [switch]$WithIntegrationTests
)

$ErrorActionPreference = "Stop"

$env:MAVEN_USER_HOME = Join-Path (Get-Location) ".m2"

if ($WithIntegrationTests) {
    .\mvnw.cmd -B -Pintegration-tests verify
} else {
    .\mvnw.cmd -B -Pci verify
}
