export CLASSPATH=classes
export RESOURCESPATH=resources
serveur=src/whatsasi/serveur/*.java
client=src/whatsasi/client/*.java
conversations=$serveur/conversations/*.java
filtrage=$serveur/filtrage/*.java
utilisateurs=$serveur/utilisateurs/*.java

echo "Compilation des fichiers sources ..."
javac -sourcepath src -d classes $client $serveur $conversations $filtrage $utilisateurs
if [ $? -eq 0 ]; then
  echo '...compilation réussie !'
else
  echo "FATAL ERROR"
fi
