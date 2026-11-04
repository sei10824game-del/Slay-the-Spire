#!/bin/bash
# usage: tools/preview.sh <agent-name> <fq.ClassName> <out.png> [clip=idle] [times=0,10,20] [views=f34,side,front,back,top,b34] [w=0.6 h=1.8] [walk=1] [hp=1] [phase=0] [var=0] [size=280]
# renders the creature's ModelSpec (needs `public static ModelSpec model()`) to a PNG contact sheet; look at it with the Read tool.
cd "$(dirname "$0")/.."
A=$1; shift
java -cp "tools/out/$A:build/classes/java/main:$(cat tools/classpath.txt)" -Djava.awt.headless=true com.spiremod.tools.Preview "$@" 2>&1 | grep -v "^Picked up"
