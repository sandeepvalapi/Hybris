#!/bin/bash

DIR=`dirname $0`
PLATFORM_DIR="${DIR}/../.."

cd ${PLATFORM_DIR}
exec ./hybrisserver.sh debug
 