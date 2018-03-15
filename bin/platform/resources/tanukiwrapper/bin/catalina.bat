@echo off

if ""%1"" == ""run"" goto doRun
if ""%1"" == ""start"" goto doStart
if ""%1"" == ""stop"" goto doStop

echo catalina.bat [hybris mode]
echo Usage:  catalina.bat ( commands ... )
echo commands:
echo   run               Start Tomcat in the current window
echo   start             Start Tomcat as windows service
echo   stop              Stop Catalina 
echo ---
echo Note: The Java Service Wrapper (wrapper.tanukisoftware.org) is bundled and integrated.
echo *** Call wrapper.bat to see more options for installing / deinstalling windows services. ***
echo *** To start Tomcat (hybris Server) in standalone mode, we are recommending the use of   ***
echo *** platformhome/hybrisserver.bat.                                                       ***

goto end


:doRun
call "%~dps0wrapper.bat" console
goto end

:doStart
call "%~dps0wrapper.bat" start
goto end

:doStop
call "%~dps0wrapper.bat" stop
goto end

:end
