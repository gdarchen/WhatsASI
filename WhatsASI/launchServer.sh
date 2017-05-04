#!/usr/bin/env bash
export CLASSPATH=classes
export CLASSPATH=$CLASSPATH:lib/jfxrt.jar
java -cp $CLASSPATH whatsasi.serveur.Serveur $1
