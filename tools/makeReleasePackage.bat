@echo off
rem  author    ucchy
rem  license   LGPLv3
rem  Copyright ucchy 2020

rem  Aqua Step Counter Windows版のリリースパッケージ作成スクリプト
rem  インテグレーション用スクリプトです。

setlocal

set ARCHIVER=C:\Program Files\7-Zip\7z.exe



cd /d %~dp0
pushd ..
set BASE_FOLDER=%cd%
set TARGET_FOLDER=%cd%\target
set TOOLS_FOLDER=%cd%\tools
set RELEASE_WIN32=%TARGET_FOLDER%\release_win32
set RELEASE_WIN64=%TARGET_FOLDER%\release_win64
popd


echo releaseフォルダをクリアして新規作成します。
del AquaStepCounter_win32.zip
del AquaStepCounter_win64.zip
if exist "%RELEASE_WIN32%" rmdir /s /q "%RELEASE_WIN32%"
mkdir "%RELEASE_WIN32%\lib"
if exist "%RELEASE_WIN64%" rmdir /s /q "%RELEASE_WIN64%"
mkdir "%RELEASE_WIN64%\lib"

echo 32bit用ファイルを配置します。
copy /y "%TARGET_FOLDER%\AquaStepCounter_win32.exe" "%RELEASE_WIN32%"
copy /y "%TARGET_FOLDER%\AquaStepCounter.jar" "%RELEASE_WIN32%"
copy /y "%TOOLS_FOLDER%\asc_commandline.bat" "%RELEASE_WIN32%"
copy /y "%BASE_FOLDER%\lib\org.apache.commons.jrcs.diff.jar" "%RELEASE_WIN32%\lib"
copy /y "%BASE_FOLDER%\lib\snakeyaml-1.16.jar" "%RELEASE_WIN32%\lib"
copy /y "%BASE_FOLDER%\lib\swt_windows_x86\swt.jar" "%RELEASE_WIN32%\lib"

echo 64bit用ファイルを配置します。
copy /y "%TARGET_FOLDER%\AquaStepCounter_win64.exe" "%RELEASE_WIN64%"
copy /y "%TARGET_FOLDER%\AquaStepCounter.jar" "%RELEASE_WIN64%"
copy /y "%TOOLS_FOLDER%\asc_commandline.bat" "%RELEASE_WIN64%"
copy /y "%BASE_FOLDER%\lib\org.apache.commons.jrcs.diff.jar" "%RELEASE_WIN64%\lib"
copy /y "%BASE_FOLDER%\lib\snakeyaml-1.16.jar" "%RELEASE_WIN64%\lib"
copy /y "%BASE_FOLDER%\lib\swt.jar" "%RELEASE_WIN64%\lib"

echo zip圧縮します
pushd %TARGET_FOLDER%
"%ARCHIVER%" a AquaStepCounter_win32.zip release_win32
"%ARCHIVER%" a AquaStepCounter_win64.zip release_win64
popd

endlocal
pause
