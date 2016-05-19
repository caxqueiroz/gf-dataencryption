#!/bin/bash
BASEDIR=/tmp
CLIENTPORT=1527
SERVERPORT=40004
NUMSERVERS=2
HOSTNAME=localhost

function startLocator {
    if [ ! -d $BASEDIR/locator ]; then
        mkdir -p $BASEDIR/locator
    fi
    echo -e "\033[1m=> Starting the GemFire locator service\033[0m"
    gfsh start locator --name=locator1 --dir=$BASEDIR/locator --bind-address=$HOSTNAME --port=$CLIENTPORT --classpath=$BASEDIR/dataencryption-0.0.1-SNAPSHOT-shared.jar
}
function startServers {
    for i in $(seq 1 $NUMSERVERS) ; do
        if [ ! -d $BASEDIR/server${i} ]; then
            mkdir -p $BASEDIR/server${i}
        fi
        echo -e "\033[1m=> Starting GemFire server number ${i} \033[0m"
        SERVERPORT=$[SERVERPORT + 1]
        gfsh start server --name=server${i} --dir=$BASEDIR/server${i} --locators=$HOSTNAME[$CLIENTPORT] --server-port=$SERVERPORT --initial-heap=4G --max-heap=4G --J=-Dencryption.passphrase=123456 --cache-xml-file=$BASEDIR/cache.xml --classpath=$BASEDIR/dataencryption-0.0.1-SNAPSHOT-shared
        .jar:$BASEDIR/bcprov-jdk15on-154.jar
    done
}

function createDirectories {
    mkdir $BASEDIR
    echo -e "Creating locator directory $BASEDIR/locator"
        mkdir -p $BASEDIR/locator
        for i in  $(seq 1 $NUMSERVERS) ; do
        echo -e "Creating server directory $BASEDIR/server${i}"
                mkdir -p $BASEDIR/server${i}
        done

}

echo -e "\033[1mStarting GemFire and associated services\033[0m"
echo -e "\033[1m-------------------------------------------\033[0m"

startLocator
startServers