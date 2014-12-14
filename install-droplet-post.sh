#!/bin/bash

mkdir ~/apps/apache-shiro
git clone https://github.com/haxwell/apache-shiro-1.2.3.git
cd apache-shiro-1.2.3
git checkout 1.2.x
mvn clean package -DskipTests
echo
echo Expected a failure above.. as long as CORE and WEB built, Quizki can build

cd ~/apps/quizki
mvn clean package
