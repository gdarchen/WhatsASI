#!/usr/bin/env bash
export CLASSPATH=classes
export CLASSPATH=$CLASSPATH:lib/jfxrt.jar
if [ $# -eq 1 ] && [ $1 == "terminal" ]
then
    java -cp $CLASSPATH whatsasi.client.ClientTerminal
else
    if [ "$(uname)" == "Darwin" ]; then
        # Mac OS X platform
        java -Xdock:icon=./icon.png -Djava.rmi.server.hostname=$1 -cp $CLASSPATH  whatsasi.client.MessagerieClient $1
    elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
        java -Djava.rmi.server.hostname=$1 -cp $CLASSPATH whatsasi.client.MessagerieClient $1
    elif [ "$(expr substr $(uname -s) 1 10)" == "MINGW32_NT" ]; then
        java -Djava.rmi.server.hostname=$1 -cp $CLASSPATH whatsasi.client.MessagerieClient $1
    elif [ "$(expr substr $(uname -s) 1 10)" == "MINGW64_NT" ]; then
        java -Djava.rmi.server.hostname=$1 -cp $CLASSPATH whatsasi.client.MessagerieClient $1
    fi
fi
