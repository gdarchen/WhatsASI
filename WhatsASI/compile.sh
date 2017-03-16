export CLASSPATH=classes
serveur=src/serveur
conversations=$serveur/conversations/*.java
filtrage=$serveur/filtrage/*.java
utilisateurs=$serveur/utilisateurs/*.java

echo "Compilation des fichiers sources ..."
javac -sourcepath src -d classes $serveur/*.java $conversations $filtrage $utilisateurs
if [ $? -eq 0 ]; then
  echo '...compilation réussie !'
else
  echo "FATAL ERROR"
fi
