@echo off
setlocal
cd /d %~dp0


echo ���݂̃t�H���_�� %cd% �ł��B


for /l %%n in (1,1,10) do (

  echo n = %%n
)


pause
endlocal
exit /b 0