#!/usr/bin/env bash
export CLASSPATH=classes
export CLASSPATH=$CLASSPATH:lib/jfxrt.jar
if [ $# -eq 1 ] && [ $1 == "terminal" ]
then
    java -cp $CLASSPATH whatsasi.client.ClientTerminal
else
    java -cp $CLASSPATH whatsasi.client.MessagerieClient
fi
