#!/bin/bash

cd $TOMCAT_HOME
./bin/catalina.sh stop

cd ~/apps/quizki
git checkout master
git pull

mvn clean package

rm $TOMCAT_HOME/webapps/ROOT -rf
cp target/quizki-1.3.war $TOMCAT_HOME/webapps/ROOT.war

cd $TOMCAT_HOME
./bin/catalina.sh start
