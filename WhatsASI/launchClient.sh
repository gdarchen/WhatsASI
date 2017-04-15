export CLASSPATH=classes
export CLASSPATH=$CLASSPATH:lib/jfxrt.jar
if [ $1 == "terminal" ]
then
    java -cp $CLASSPATH whatsasi.client.ClientTerminal
else
    java -cp $CLASSPATH whatsasi.client.Client
fi
