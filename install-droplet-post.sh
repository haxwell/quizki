#!/bin/bash

# 
# This script should be run as sudo
#
if [ "$(id -u)" != 0 ]; then
    echo "You should run this as root."
    exit 1
fi

mkdir ~/apps/apache-shiro
git clone https://github.com/haxwell/apache-shiro-1.2.3.git
cd apache-shiro-1.2.3
git checkout 1.2.x
mvn clean install -DskipTests
echo
echo --------------------------------------------------------------------------
echo Expected a failure above.. as long as CORE and WEB built, Quizki can build
echo --------------------------------------------------------------------------
echo

cd ~/apps/quizki
mvn clean package

echo ----------------------
echo setting up Tomcat
echo ----------------------

bash -c 'echo "#! /bin/sh" >> /home/quizki/apps/apache-tomcat-7.0.57/bin/setenv.sh'
bash -c 'echo export JAVA_OPTS=\"$JAVA_OPTS -DQUIZKI_JDBC_URL=jdbc:mysql://localhost:3306/quizki_db -DSHIRO_SERVER_NAME=localhost\" >> /home/quizki/apps/apache-tomcat-7.0.57/bin/setenv.sh'

rm $TOMCAT_HOME/webapps/ROOT -rf
cp target/quizki-1.3.war $TOMCAT_HOME/webapps/ROOT.war

cd $TOMCAT_HOME
./bin/catalina.sh start
