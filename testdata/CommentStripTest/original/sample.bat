@echo off
setlocal
cd /d %~dp0

rem 現在のフォルダを表示する
echo 現在のフォルダは %cd% です。

@REM ループ処理で10回繰り返す
for /l %%n in (1,1,10) do (
  rem ループ回数を画面に表示する
  echo n = %%n
)

rem 処理終了
pause
endlocal
exit /b 0