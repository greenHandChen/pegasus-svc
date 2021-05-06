#!/bin/bash

echo ">>> git pull"
git pull

echo ">>> mvn clean package"
mvn clean install  package

echo ">>> cd target"
cd target

JAR=oauth.jar
MPORT=8020

echo ">>> kill -9 $(jps -ml | grep $JAR | awk '{print $1}')"
kill -9 $(jps -ml | grep $JAR | awk '{print $1}')

echo ">>> kill -9 $(lsof -n -P -t -i:$MPORT)"
kill -9 $(lsof -n -P -t -i:$MPORT)

mv app.jar $JAR

echo ">>> nohup java -jar -Xms128m -Xmx256m $JAR >app.log &"
BUILD_ID=dontKillMe nohup java -jar -Xms128m -Xmx256m $JAR > /root/app/logs/oauth.log &
