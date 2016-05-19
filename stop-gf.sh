#!/bin/bash
BASEDIR=/tmp
HOSTNAME=localhost
echo -e "\033[1m=> Stopping all GemFire servers.\033[0m"
gfsh stop server --dir=$BASEDIR/server1
gfsh stop server --dir=$BASEDIR/server2

echo -e "\033[1m=> Stopping the GemFire locator service.\033[0m"
gfsh stop locator --dir=$BASEDIR/locator