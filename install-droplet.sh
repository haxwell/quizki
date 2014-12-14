#!/bin/bash

# Assumptions: 
#   a user with sudo privilege, named 'quizki' exists.

# 
# This script should be run as sudo
#
if [ "$(id -u)" != 0 ]; then
    echo "You should run this as root."
    exit 1
fi

echo *************************
echo Doin\' setup stuff
echo *************************
echo
cd ~
apt-get update 
apt-get install unzip git dpkg-dev maven
mkdir work

echo **************************
echo Installing Oracle 8
echo **************************
echo
# install oracle java 8 (remember to tip this guy)
mkdir apps/java -p
cd work
git clone git://github.com/rraptorr/oracle-java8.git
cd oracle-java8
sh ./prepare.sh
dpkg-buildpackage -uc -us
cp x64/* ~/apps/java -R

echo "JAVA_HOME=/home/quizki/apps/java" >> ~/quizki_env_vars
export JAVA_HOME=/home/quizki/apps/java
export PATH=$JAVA_HOME/bin:$PATH

echo ****************************
echo installing Tomcat
echo ****************************
echo
# install tomcat
cd ~
mkdir apps/tomcat -p
cd work
curl -# -L http://apache.mirrors.lucidnetworks.net/tomcat/tomcat-7/v7.0.57/bin/apache-tomcat-7.0.57.tar.gz > apache-tomcat-7.0.57.tar.gz
tar -xvf apache-tomcat-7.0.57.tar.gz
mv apache-tomcat-7.0.57 ~/apps/

echo "TOMCAT_HOME=/home/quizki/apps/apache-tomcat-7.0.57" >> ~/quizki_env_vars
export TOMCAT_HOME=/home/quizki/apps/apache-tomcat-7.0.57
export PATH=$TOMCAT_HOME/bin:$PATH

echo ******************************
echo Installing MySQL
echo ******************************
echo 
# install mysql
apt-get install mysql-server

mysql -u root -p < ./src/main/resources/META-INF/sql/mysql/init_quizki_user.sql
mysql -u quizki -p < ./src/main/resources/META-INF/sql/mysql/populate_database_quizkiDotCom_20140513.sql

# install quizki
#   cd ~/apps
#   git clone https://github.com/haxwell/quizki.git
#   cd quizki
#   mvn clean package

echo **********************************
echo

