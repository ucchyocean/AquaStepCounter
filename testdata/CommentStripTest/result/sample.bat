@echo off
setlocal
cd /d %~dp0


echo 現在のフォルダは %cd% です。


for /l %%n in (1,1,10) do (

  echo n = %%n
)


pause
endlocal
exit /b 0