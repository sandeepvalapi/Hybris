#!/bin/bash

set -e

TMP_DIR="$(mktemp -d)"
TMP_JAVA_FILE="$TMP_DIR/UUIDGenerator.java"

echo "import java.util.UUID;" > "$TMP_JAVA_FILE"
echo "class UUIDGenerator {" >> "$TMP_JAVA_FILE"
echo "    public static void main(String[] args) {" >> "$TMP_JAVA_FILE"
echo "        System.out.println(UUID.randomUUID().toString());" >> "$TMP_JAVA_FILE"
echo "    }" >> "$TMP_JAVA_FILE"
echo "}" >> "$TMP_JAVA_FILE"

javac "$TMP_JAVA_FILE"
java -cp "$TMP_DIR" UUIDGenerator