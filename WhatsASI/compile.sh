export CLASSPATH=classes
export CLASSPATH=$CLASSPATH:lib/jfxrt.jar
export RESOURCESPATH=resources
serveur=src/whatsasi/serveur
client=src/whatsasi/client/*.java
conversations=$serveur/conversations/*.java
filtrage=$serveur/filtrage/*.java
utilisateurs=$serveur/utilisateurs/*.java

echo "Compilation des fichiers sources ..."
javac -sourcepath src -d classes $client $conversations $filtrage $utilisateurs
if [ $? -eq 0 ]; then
  echo '...compilation réussie !'
else
  echo "FATAL ERROR"
fi
