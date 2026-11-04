#!/bin/bash
# usage: tools/validate.sh <agent-name> <fq.ClassName...>   or   tools/validate.sh <agent-name> --group <fq.GroupClass>
cd "$(dirname "$0")/.."
A=$1; shift
java -cp "tools/out/$A:build/classes/java/main:$(cat tools/classpath.txt)" -Djava.awt.headless=true com.spiremod.tools.Validate "$@" 2>&1 | grep -v "^Picked up"
