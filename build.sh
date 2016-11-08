#!/bin/bash

#git fetch origin
#git reset --hard origin/master

mvn clean compile  assembly:single
#mvn install

#cp -p celera_uts_wsdl_soap/target/uts_wsdl_soap-3.1.7-jar-with-dependencies.jar $UTS_HOME/lib
#cp -p celera_uts_wsdl_soap/target/uts_wsdl_soap-3.1.7-jar $UTS_HOME/lib
