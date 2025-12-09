@echo off
set "FILES=-f docker-compose.yml"
set "ARGS="
for %%a in (%*) do (
    if "%%a"=="dev" set "FILES=%FILES% -f docker-compose.dev.yml"
    if "%%a"=="build" set "ARGS=--build"
)
docker-compose %FILES% up %ARGS%