#!/bin/bash

set -e

TMP_DIR="$(mktemp -d)"
TMP_JAVA_FILE="$TMP_DIR/Netcat.java"

echo "import java.io.IOException;" > "$TMP_JAVA_FILE"
echo "class Netcat {" >> "$TMP_JAVA_FILE"
echo "    public static void main(String[] args) {" >> "$TMP_JAVA_FILE"
echo "        if (args.length != 2) {" >> "$TMP_JAVA_FILE"
echo "        System.exit(1); }" >> "$TMP_JAVA_FILE"
echo "    try { run(args[0], Integer.parseInt(args[1])); }" >> "$TMP_JAVA_FILE"
echo "    catch (IOException e) { System.exit(1); }" >> "$TMP_JAVA_FILE"
echo "    }" >> "$TMP_JAVA_FILE"
echo "    public static void run(String host, int port) throws IOException {" >> "$TMP_JAVA_FILE"
echo "    java.net.Socket socket = new java.net.Socket(host, port);" >> "$TMP_JAVA_FILE"
echo "    socket.getOutputStream();" >> "$TMP_JAVA_FILE"
echo "    socket.getInputStream();" >> "$TMP_JAVA_FILE"
echo "    }" >> "$TMP_JAVA_FILE"
echo "}" >> "$TMP_JAVA_FILE"

javac "$TMP_JAVA_FILE"


#!/bin/bash

function doLog {
        echo "[ywaitForPort] $1"
}

function doWait {
        doLog "Waiting for $1"
        hostPort=(${1//:/ })
        for a in {1..1000}
        do
            java -cp "$TMP_DIR" Netcat ${hostPort[0]} ${hostPort[1]}
                if [ $? -eq 0 ]
                        then
                                return 0
                        else
                                doLog "Attempt $a failed" && sleep 2
                fi
        done
        return 1
}

for i in ${@:1}
do
        if doWait $i;
                then
                        doLog "$i is open"
                else
                        doLog "Giving up waiting on $i"
                        exit 1
        fi
done

exit 0
