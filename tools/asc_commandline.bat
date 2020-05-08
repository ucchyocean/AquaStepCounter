@echo off
rem  author    ucchy
rem  license   LGPLv3
rem  Copyright ucchy 2020

rem  Aqua Step Counter コマンドライン実行用batスクリプト
rem  コマンドライン実行をしたい場合は、このbatスクリプトを利用してください。

setlocal
cd /d %~dp0


rem java.exeが見つからない場合は、この行を使用してください。
rem set PATH=%PATH%;C:\Program Files\Java\jre1.8.0_251\bin


rem 以降の行は変更不要です。

set JAR_FILES=AquaStepCounter.jar;lib/org.apache.commons.jrcs.diff.jar;lib/snakeyaml-1.16.jar;lib/swt.jar
java -classpath %JAR_FILES% com.github.ucchyocean.aqua.AquaStepCounter %*

endlocal
