@echo off
rem  author    ucchy
rem  license   LGPLv3
rem  Copyright ucchy 2020

rem  Aqua Step Counter Windows�ł̃����[�X�p�b�P�[�W�쐬�X�N���v�g
rem  �C���e�O���[�V�����p�X�N���v�g�ł��B

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


echo release�t�H���_���N���A���ĐV�K�쐬���܂��B
del AquaStepCounter_win32.zip
del AquaStepCounter_win64.zip
if exist "%RELEASE_WIN32%" rmdir /s /q "%RELEASE_WIN32%"
mkdir "%RELEASE_WIN32%\lib"
if exist "%RELEASE_WIN64%" rmdir /s /q "%RELEASE_WIN64%"
mkdir "%RELEASE_WIN64%\lib"

echo 32bit�p�t�@�C����z�u���܂��B
copy /y "%TARGET_FOLDER%\AquaStepCounter_win32.exe" "%RELEASE_WIN32%"
copy /y "%TARGET_FOLDER%\AquaStepCounter.jar" "%RELEASE_WIN32%"
copy /y "%TOOLS_FOLDER%\asc_commandline.bat" "%RELEASE_WIN32%"
copy /y "%BASE_FOLDER%\lib\org.apache.commons.jrcs.diff.jar" "%RELEASE_WIN32%\lib"
copy /y "%BASE_FOLDER%\lib\snakeyaml-1.16.jar" "%RELEASE_WIN32%\lib"
copy /y "%BASE_FOLDER%\lib\swt_windows_x86\swt.jar" "%RELEASE_WIN32%\lib"

echo 64bit�p�t�@�C����z�u���܂��B
copy /y "%TARGET_FOLDER%\AquaStepCounter_win64.exe" "%RELEASE_WIN64%"
copy /y "%TARGET_FOLDER%\AquaStepCounter.jar" "%RELEASE_WIN64%"
copy /y "%TOOLS_FOLDER%\asc_commandline.bat" "%RELEASE_WIN64%"
copy /y "%BASE_FOLDER%\lib\org.apache.commons.jrcs.diff.jar" "%RELEASE_WIN64%\lib"
copy /y "%BASE_FOLDER%\lib\snakeyaml-1.16.jar" "%RELEASE_WIN64%\lib"
copy /y "%BASE_FOLDER%\lib\swt.jar" "%RELEASE_WIN64%\lib"

echo zip���k���܂�
pushd %TARGET_FOLDER%
"%ARCHIVER%" a AquaStepCounter_win32.zip release_win32
"%ARCHIVER%" a AquaStepCounter_win64.zip release_win64
popd

endlocal
pause
