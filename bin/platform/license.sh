#!/bin/sh
BASE_DIR=`dirname $0`
CORE_DIR="$BASE_DIR/ext/core"
BOOTSTRAP_DIR="$BASE_DIR/bootstrap"

CORE_LIB="$CORE_DIR/lib/*"
CORE_SERVER="$CORE_DIR/bin/coreserver.jar"
CORE_CLASSES="$CORE_DIR/classes"
BOOTSTRAP_CLASSES="$BOOTSTRAP_DIR/classes"
BOOTSTRAP_JAR="$BOOTSTRAP_DIR/bin/ybootstrap.jar"
CORE_RESOURCES="$CORE_DIR/resources"

java -classpath ${CORE_LIB}:${CORE_SERVER}:${CORE_CLASSES}:${BOOTSTRAP_JAR}:${BOOTSTRAP_CLASSES}:${CORE_RESOURCES} \
    -Dpcd.home=${BASE_DIR} \
    de.hybris.platform.licence.sap.HybrisAdmin $1 $2 $3 $4
