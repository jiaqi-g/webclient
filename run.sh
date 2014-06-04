#!/bin/bash
echo "[Main] make sure you have run ./publish.sh to publish needed libs to your local repos"
#mvn assembly:assembly -DdescriptorId=jar-with-dependencies -Dmaven.test.skip=true
mvn package -Dmaven.test.skip=true
cd target
java -jar boost_client-1.0-SNAPSHOT-jar-with-dependencies.jar ../config
