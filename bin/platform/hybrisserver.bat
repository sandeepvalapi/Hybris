@echo off

setlocal

SET MODE=%1

IF  "%MODE%" == "" (
	SET MODE=run
)

IF "%1"=="debug" (
	SET _YWRAPPER_CONF=%~dp0tomcat/conf/wrapper-debug.conf
	SET MODE=run
)

IF "%1"=="minimal" (
	SET _YWRAPPER_CONF=%~dp0tomcat/conf/wrapper-minimal.conf
	SET MODE=run
)

IF "%1"=="jprofiler" (
	SET _YWRAPPER_CONF=%~dp0tomcat/conf/wrapper-jprofiler.conf
	SET MODE=run
)

IF "%1"=="" (
   set _YWRAPPER_CONF=%~dp0tomcat/conf/wrapper.conf
) 

IF "%1"=="-v" (
	call java -cp %~dps0tomcat\lib\catalina.jar org.apache.catalina.util.ServerInfo
	GOTO:EOF
)

call %~dps0tomcat\bin\catalina.bat %MODE%

endlocal

	
