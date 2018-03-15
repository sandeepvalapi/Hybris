@echo off

setlocal

SET _YWRAPPER_CONF=%~dp0tomcat/conf/wrapper-minimal.conf
SET MODE=run

call %~dps0tomcat\bin\catalina.bat %MODE%

endlocal

	