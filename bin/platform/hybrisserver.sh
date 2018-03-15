#!/bin/sh
# -----------------------------------------------------------------------------
# Run Script for the CATALINA Server
#

DIR=`dirname $0`
TOMCAT_DIR="${DIR}/tomcat"

if [ -f "$DIR/active-role-env.properties" ]; then
	. "$DIR/active-role-env.properties"
	# we need to do that to passthrough CATALINA_BASE to env
	export CATALINA_BASE=$CATALINA_BASE
else
	export CATALINA_BASE="`pwd`/tomcat"
fi

MODE=$1

if [ "$MODE" = "" ] ; then
	MODE="run"
fi

case $MODE in
	"minimal" | "-m" )
		export WRAPPER_CONF="$CATALINA_BASE/conf/wrapper-minimal.conf"
		COMMAND="./catalina.sh run"
		;;
	"debug" | "-d" )
		export WRAPPER_CONF="$CATALINA_BASE/conf/wrapper-debug.conf"
		COMMAND="./catalina.sh run"
		;;
	"jprofiler" | "-j" )
		export WRAPPER_CONF="$CATALINA_BASE/conf/wrapper-jprofiler.conf"
		COMMAND="./catalina.sh run"
		;;
    "version" | "-v" )
		COMMAND="java -cp ../lib/catalina.jar org.apache.catalina.util.ServerInfo"
		;;
	* )
		export WRAPPER_CONF="$CATALINA_BASE/conf/wrapper.conf"
		COMMAND="./catalina.sh ${MODE}"
		;;
esac

cd ${TOMCAT_DIR}/bin
exec $COMMAND
