#!/bin/bash

JAR_PATH="build/libs/june-all.jar"

if [ ! -f "$JAR_PATH" ]; then
    echo "Error: JAR file not found at $JAR_PATH"
    exit 1
fi

java -jar "$JAR_PATH" "$@"