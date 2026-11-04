#!/bin/bash
# usage: tools/check.sh <agent-name> <java files...>   -> compiles your creature files into tools/out/<agent-name> (never touches gradle)
set -e
cd "$(dirname "$0")/.."
A=$1; shift
mkdir -p tools/out/$A
javac -proc:none -nowarn -encoding UTF-8 -cp "$(cat tools/classpath.txt):build/classes/java/main" -d tools/out/$A "$@" 2>&1 | grep -v "^Picked up\|^Note:" || true
echo "compile finished (errors, if any, are listed above)"
