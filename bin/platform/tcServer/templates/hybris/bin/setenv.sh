# Edit this file to CATALINA_BASE/bin/setenv.sh to set custom options
# Tomcat accepts two parameters JAVA_OPTS and CATALINA_OPTS
# JAVA_OPTS are used during START/STOP/RUN
# CATALINA_OPTS are used during START/RUN

# JVM memory settings - general
GENERAL_JVM_OPTS="-Xmx512m -Xss256k"

# JVM Sun specific settings
# For a complete list http://blogs.sun.com/watt/resource/jvm-options-list.html
#SUN_JVM_OPTS="-XX:MaxGCPauseMillis=500 \
#              -XX:+HeapDumpOnOutOfMemoryError"

#SUN_JVM_OPTS="-XX:NewSize=128m \
#              -XX:MaxNewSize=256m \
#              -XX:MaxGCPauseMillis=500 \
#              -XX:HeapDumpOnOutOfMemoryError \
#              -XX:+PrintGCApplicationStoppedTime \
#              -XX:+PrintGCTimeStamps \
#              -XX:+PrintGCDetails \
#              -XX:+PrintHeapAtGC \
#              -Xloggc:gc.log"
              
# JVM IBM specific settings
#IBM_JVM_OPTS=""

# Set any custom application options here
APPLICATION_OPTS="`grep 'wrapper.java.additional' $INSTANCE_NAME/conf/wrapper.conf | grep -v 'stripquotes' | sed 's/"//g' | sed 's/=/@unique@/1'  | awk -F"@unique@" '{print $2}' | sed 's/.$//' | tr '\n' ' '`"

# Must contain all JVM Options.  Used by AMS.
JVM_OPTS="$GENERAL_JVM_OPTS $SUN_JVM_OPTS"

CATALINA_OPTS="$JVM_OPTS $APPLICATION_OPTS"

if [[ -z "${JAVA_HOME}" ]] ; then
  JAVA_HOME=/opt/java/jre/
fi
#JRE_HOME=setme
