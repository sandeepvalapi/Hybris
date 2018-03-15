@echo off
SET BASE_DIR=%~dp0
SET CORE_DIR=%BASE_DIR%/ext/core
SET BOOTSTRAP_DIR=%BASE_DIR%/bootstrap

SET CORE_LIB=%CORE_DIR%/lib/*
SET CORE_SERVER=%CORE_DIR%/bin/coreserver.jar
SET CORE_CLASSES=%CORE_DIR%/classes
SET BOOTSTRAP_JAR=%BOOTSTRAP_DIR%/bin/ybootstrap.jar
SET BOOTSTRAP_CLASSES=%BOOTSTRAP_DIR%/classes
SET CORE_RESOURCES=%CORE_DIR%/resources

java.exe -classpath "%classpath%";"%CORE_LIB%";"%CORE_SERVER%";"%CORE_CLASSES%";"%BOOTSTRAP_JAR%";"%BOOTSTRAP_CLASSES%";"%CORE_RESOURCES%"^
 -Dpcd.home=%BASE_DIR% de.hybris.platform.licence.sap.HybrisAdmin %1 %2 %3 %4
