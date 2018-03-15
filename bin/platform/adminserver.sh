#!/bin/sh
# -----------------------------------------------------------------------------
# Run Script for the CATALINA Server
#

DIR=`dirname $0`
TOMCAT_DIR="${DIR}/tomcat"

export WRAPPER_CONF="../conf/wrapper-minimal.conf"
COMMAND="./catalina.sh run"

cd ${TOMCAT_DIR}/bin
exec $COMMAND
