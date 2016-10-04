#!/bin/bash

export JAVA_PATH=/opt/jdk1.8.0_102
export PATH=$PATH:$JAVA_HOME/bin:/home/idbs/uts/cfg

#java -Dconfig.file=cfg/uts.properties -Djava.util.logging.config.file=cfg/log4j.properties -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=6006,suspend=y -cp lib/uts_wsdl_soap-3.1.7-jar-with-dependencies.jar  com.vectails.main.Application
#java -Dconfig.file=cfg/uts.properties -Djava.util.logging.config.file=cfg/log4j.properties -agentlib:jdwp=transport=dt_socket,server=y,address=6006,suspend=y -cp lib/uts_wsdl_soap-3.1.7-jar-with-dependencies.jar  com.vectails.main.Application
java -Dconfig.file=/home/idbs/uts/cfg/uts.properties -Djava.util.logging.config.file=cfg/log4j.properties -Dcom.sun.management.jmxremote.port=7001 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -cp lib/uts_wsdl_soap-3.1.7-jar-with-dependencies.jar  com.vectails.main.Application

