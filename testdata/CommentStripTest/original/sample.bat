@echo off
setlocal
cd /d %~dp0

rem ���݂̃t�H���_��\������
echo ���݂̃t�H���_�� %cd% �ł��B

@REM ���[�v������10��J��Ԃ�
for /l %%n in (1,1,10) do (
  rem ���[�v�񐔂���ʂɕ\������
  echo n = %%n
)

rem �����I��
pause
endlocal
exit /b 0