#!/bin/bash
cd "$(dirname "$0")/.."
java -cp "build/classes/java/main:$(cat tools/classpath.txt)" -Djava.awt.headless=true com.spiremod.tools.GenAssets "$@" 2>&1 | grep -v "^Picked up"
