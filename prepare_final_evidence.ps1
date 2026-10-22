# CAB302 Week 13 — final evidence commands for Windows PowerShell
# Run at Farmbook repo root after git pull --ff-only origin main.
# Requires Git, Java 21 and project's Maven wrapper.
$ErrorActionPreference = "Stop"
$repo = (Get-Location).Path
Write-Host "Repo: $repo"
if (!(Test-Path ".git")) { throw "This folder is not a full Git clone. The final ZIP must contain .git." }
if (!(Test-Path ".\mvnw.cmd")) { throw "Maven wrapper missing. Run at project root." }

$evidence = ".\docs\07-submission2\team-reports"
New-Item -ItemType Directory -Path $evidence -Force | Out-Null

# Exact outputs QUT explicitly requested; include with final ZIP.
git shortlog --summary --numbered --all --no-merges | Out-File -FilePath "$evidence\git-shortlog.txt" -Encoding utf8
git log --pretty=format:'%h was %an (%ae), %at, %ar, message: %s' | Out-File -FilePath "$evidence\git-commit-history.txt" -Encoding utf8
git branch -a -vv | Out-File -FilePath "$evidence\git-branches.txt" -Encoding utf8
git log -1 --format='%H %ad %s' --date=iso-strict | Out-File -FilePath "$evidence\main-head.txt" -Encoding utf8

# Run clean tests, then generate Javadoc. Capture console output separately if needed.
& .\mvnw.cmd clean test
if ($LASTEXITCODE -ne 0) { throw "Maven tests failed. Do not claim final green build." }
& .\mvnw.cmd javadoc:javadoc
if ($LASTEXITCODE -ne 0) { throw "Javadoc generation failed." }
if (!(Test-Path ".\target\docs\javadoc\index.html")) { throw "Javadoc index missing." }

# This folder is for the final ZIP, not a required Git commit. It may contain many generated files.
$dest = ".\docs\07-submission2\generated-javadoc"
if (Test-Path $dest) { Remove-Item -Recurse -Force $dest }
Copy-Item -Path ".\target\docs\javadoc" -Destination $dest -Recurse -Force
if (!(Test-Path "$dest\index.html")) { throw "Copy failed." }
Write-Host "Verified reports and Javadoc. Next: inspect $dest\index.html and pack the FULL repository including .git."
