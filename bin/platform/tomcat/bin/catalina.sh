#!/bin/sh
# -----------------------------------------------------------------------------
# Start/Stop Script for the CATALINA Server
#

if [ "$1" = "start" ] ; then

 exec ./wrapper.sh start
 
elif [ "$1" = "run" ]; then

 exec ./wrapper.sh console 

elif [ "$1" = "stop" ]; then

 exec ./wrapper.sh stop 
 
else

  echo "catalina.sh (hybris mode)"
  echo "Usage: catalina.sh ( commands ... )"
  echo "commands:"
  echo "  run               Start Tomcat in current console"
  echo "  start             Start Tomcat as a background process"
  echo "  stop              Stop Tomcat"
  echo "---"
  echo "Note: The Java Service Wrapper (wrapper.tanukisoftware.org) is bundled and integrated."
  echo "Call wrapper.sh to see more options"

  exit 1

fi
