@echo off
set SCRIPT_NAME="roles.bat"
set PLATFORM_HOME=%~dp0
set HYBRIS_ROLES_DIR=%~dp0..\..\roles
set ACTIVE_ROLE_PROPS=%~dp0active-role-env.properties
set activate_instance=
set create_instance=
set remove_instance=
set roleName=
set httpPort=
set sslPort=
set ajpPort=
set jmxPort=
set jmxServerPort=

if exist %ACTIVE_ROLE_PROPS% (
  for /F "tokens=1,2 delims==" %%G in (%ACTIVE_ROLE_PROPS%) do (
    set %%G=%%H
  )
)
if not "%1"=="" goto parseOptions "%*"
:usage
echo Usage: roles.bat [options...]^

^

Create role:^

^

-c, --create-role roleName              creates role named by roleName.^

-C, --create-instance instanceName      creates role instance named by instanceName. This option requires specific tcp ports configuration:^

    --role-name=roleName                name of the role for which instance will be created (mandatory)^

    --http-port=portNumber              tomcat http port for newly created role (mandatory)^

    --ssl-port=portNumber               tomcat ssl port for newly created role (mandatory)^

    --ajp-port=portNumber               ajp port for newly created role (mandatory)^

    --jmx-port=portNumber               jmx port for newly created role (mandatory)^

    --jmx-server-port=portNumber        jmx server port for newly created role (mandatory)^

^

Other options:^

^

-a, --activate roleName                 activates role named by roleName^

-A, --activate-instance instanceName    activates role instance named by instanceName. This option requires additional option:^

    --role-name=roleName                name of the role for which instance must be activated^

-d, --deactivate                        deactivates current active role or instance^

-r, --remove roleName                   removes role roleName (*warning* it will remove whole role and all instances)^

-R, --remove-instance instanceName      removes instance (*warning* instance must not be running). This option requires additional option:^

    --role-name=roleName                name of the role for which instance must be removed^

-l, --list                              list installed roles (the role marked with * is current active role)^

-h, --help                              print help
goto end
:parseOptions
if "%1"=="" goto checkParameters
if "%1"=="-h" goto usage
if "%1"=="-?" goto usage
if "%1"=="--help" goto usage
if "%1"=="/?" goto usage
if "%1"=="/h" goto usage
if "%1"=="/help" goto usage
if "%1"=="-c" goto createRole
if "%1"=="--create-role" goto createRole
if "%1"=="-C" goto createInstance
if "%1"=="--create-instance" goto createInstance
if "%1"=="-a" goto activateRole
if "%1"=="--activateRole" goto activateRole
if "%1"=="-A" goto activateInstance
if "%1"=="--activate-instance" goto activateInstance
if "%1"=="-d" goto deactivate
if "%1"=="--deactivate" goto deactivate
if "%1"=="-r" goto remove
if "%1"=="--remove" goto remove
if "%1"=="-R" goto removeInstance
if "%1"=="--remove-instance" goto removeInstance
if "%1"=="-l" goto list
if "%1"=="--list" goto list
if "%1"=="--role-name" goto roleName
if "%1"=="--http-port" goto httpPort
if "%1"=="--ssl-port" goto sslPort
if "%1"=="--ajp-port" goto ajpPort
if "%1"=="--jmx-port" goto jmxPort
if "%1"=="--jmx-server-port" goto jmxServerPort
shift
goto parseOptions
:remove
if "%2"=="" (
  echo ERROR: Must specify a non-empty role name
  goto error
) 
if exist %HYBRIS_ROLES_DIR%\%2 (
  echo Removing %HYBRIS_ROLES_DIR%\%2 ....
  rmdir /Q /S %HYBRIS_ROLES_DIR%\%2
  if exist %ACTIVE_ROLE_PROPS% (
    echo Removing %ACTIVE_ROLE_PROPS% file ....
    del %ACTIVE_ROLE_PROPS%
  )
) else (
  echo Role %2 not present in the system. Skipping
)
goto end
:removeInstance
if "%2"=="" (
  echo ERROR: Must specify a non-empty instance name
  goto error
) else (
  set remove_instance=1
  set instance_name=%2
  shift
  shift
  goto parseOptions
)
goto end
:deactivate
if exist %ACTIVE_ROLE_PROPS% (
  echo Deactivating current active role
  del %ACTIVE_ROLE_PROPS%
) else (
  echo None of the roles were active at the moment. Skipping
)
goto end
:activateRole
if "%2"=="" (
  echo ERROR: Must specify a non-empty role name
  goto error
)
if exist %HYBRIS_ROLES_DIR%/%2 (
  call %PLATFORM_HOME%/setantenv.bat
  set return=
  set return=ret1
  goto unsetProperties
:ret1
  ant activateRole -Drole.name=%2
  goto end
) else (
  echo Role %2 not present in the system. Skipping
  goto error
)
:activateInstance
if "%2"=="" (
  echo ERROR: Must specify a non-empty instance name
  goto error
) else (
  set activate_instance=1
  set instance_name=%2
  shift
  shift
  goto parseOptions
)
goto end
:createRole
if "%2"=="" (
  echo ERROR: Must specify a non-empty role name
  goto error
) else (
  echo Creating role '%2'
  call %PLATFORM_HOME%/setantenv.bat
  set return=
  set return=ret2
  goto unsetProperties
:ret2
  ant createRole -Drole.name=%2
  goto end
)
goto end
:createInstance
if "%2"=="" (
  echo ERROR: Must specify a non-empty instance name
  goto error
) else (
  set create_instance=1
  set instance_name=%2
  shift
  shift
  goto parseOptions
)
:roleName
  set roleName=%2
  shift
  shift
  goto parseOptions
:httpPort
  set httpPort=%2
  shift
  shift
  goto parseOptions
:sslPort
  set sslPort=%2
  shift
  shift
  goto parseOptions
:ajpPort
  set ajpPort=%2
  shift
  shift
  goto parseOptions
:jmxPort
  set jmxPort=%2
  shift
  shift
  goto parseOptions
:jmxServerPort
  set jmxServerPort=%2
  shift
  shift
  goto parseOptions
:list
echo Listing roles
call %PLATFORM_HOME%\setantenv.bat
set return=
set return=ret5
goto unsetProperties
ant listRoles
:ret5
ant listRoles
goto end
:checkParameters
if "%create_instance%"=="1" (
  if "%roleName%"=="" goto missingParameter1
  if "%httpPort%"=="" goto missingParameter1
  if "%sslPort%"=="" goto missingParameter1
  if "%ajpPort%"=="" goto missingParameter1
  if "%jmxPort%"=="" goto missingParameter1
  if "%jmxServerPort%"=="" goto missingParameter1
  echo Creating role instance '%instance_name%'
  call %PLATFORM_HOME%\setantenv.bat
  set return=
  set return=ret3
  goto unsetProperties
:ret3
  ant createInstance -Drole.name=%roleName% -Dinstance.name=%instance_name% -Dhttp.port=%httpPort% -Dssl.port=%sslPort% -Dajp.port=%ajpPort% -Djmx.port=%jmxPort% -Djmx.server.port=%jmxServerPort%
  goto end
)
if "%activate_instance%"=="1" (
  if "%roleName%"=="" goto missingParameter2
  echo Activating role instance '%instance_name%'
  call %PLATFORM_HOME%\setantenv.bat
  set return=
  set return=ret4
  goto unsetProperties
:ret4
  ant activateInstance -Drole.name=%roleName% -Dinstance.name=%instance_name%
  goto end
)
if "%remove_instance%"=="1" (
  if "%roleName%"=="" (    
    goto missingParameter2
  )
rem )
rem if "%remove_instance%"=="1" (
  if exist %HYBRIS_ROLES_DIR%\%roleName%\%instance_name% (
    set INSTANCE_TOMCAT_PID_FILE=%HYBRIS_ROLES_DIR%\%roleName%\%instance_name%\tomcat\bin\hybrisPlatform.pid
    if exist %INSTANCE_TOMCAT_PID_FILE% (
      echo ERROR: Cannot remove instance %instance_name% while it is still running
      goto error
    )
    echo Removing %HYBRIS_ROLES_DIR%\%roleName%\%instance_name% ....
    rmdir /Q /S %HYBRIS_ROLES_DIR%\%roleName%\%instance_name%
    if exist %ACTIVE_ROLE_PROPS% (
      if "%ACTIVE_ROLE_INSTANCE%"=="%instance_name%" (
        echo Removing %ACTIVE_ROLE_PROPS% file ....
        del %ACTIVE_ROLE_PROPS%
      )
    )
  ) else (
    echo Role %roleName% instance %instance_name% not present in the system. Skipping
    goto end
  )
)
goto end
:missingParameter1
echo ERROR: options --role-name --http-port --ssl-port --ajp-port --jmx-port --jmx-server-port not given. See --help
goto error
:missingParameter2
echo ERROR: option --role-name not given. See --help
goto error
:end
set return=
set return=ret98
goto unsetProperties
:ret98
exit /b 0
:error
set return=
set return=ret99
goto unsetProperties
:ret99
exit /b 1
:unsetProperties
set HYBRIS_BIN_DIR=
set HYBRIS_CONFIG_DIR=
set HYBRIS_LOG_DIR=
set HYBRIS_DATA_DIR=
set HYBRIS_TEMP_DIR=
set HYBRIS_BOOTSTRAP_BIN_DIR=
set HYBRIS_ROLES_DIR=
set ACTIVE_ROLE_INSTANCE=
set ACTIVE_ROLE=
set CATALINA_BASE=
goto %return%
