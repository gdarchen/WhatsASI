export CLASSPATH=classes
export CLASSPATH=$CLASSPATH:lib/jfxrt.jar
export CLASSPATH=$CLASSPATH:lib/jfxswt.jar
export RESOURCESPATH=resources
serveurPath=src/whatsasi/serveur
client=src/whatsasi/client/*.java
serveur=$serveurPath/Serveur.java
conversations=$serveurPath/conversations/*.java
filtrage=$serveurPath/filtrage/*.java
utilisateurs=$serveurPath/utilisateurs/*.java

echo "Compilation des fichiers sources ..."
javac -Xlint:unchecked -sourcepath src -d classes $serveur $client $conversations $filtrage $utilisateurs
if [ $? -eq 0 ]; then
  echo '...compilation réussie !'
else
  echo "FATAL ERROR"
fi
