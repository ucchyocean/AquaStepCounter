@echo off
rem  author    ucchy
rem  license   LGPLv3
rem  Copyright ucchy 2020

rem  Aqua Step Counter �R�}���h���C�����s�pbat�X�N���v�g
rem  �R�}���h���C�����s���������ꍇ�́A����bat�X�N���v�g�𗘗p���Ă��������B

setlocal
cd /d %~dp0


rem java.exe��������Ȃ��ꍇ�́A���̍s���g�p���Ă��������B
rem set PATH=%PATH%;C:\Program Files\Java\jre1.8.0_251\bin


rem �ȍ~�̍s�͕ύX�s�v�ł��B

set JAR_FILES=AquaStepCounter.jar;lib/org.apache.commons.jrcs.diff.jar;lib/snakeyaml-1.16.jar;lib/swt.jar
java -classpath %JAR_FILES% com.github.ucchyocean.aqua.AquaStepCounter %*

endlocal
