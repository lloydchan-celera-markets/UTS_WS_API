#!/bin/sh
java -Djava.library.path=/usr/local/lib -classpath /usr/local/lib/libjzmq:../../main/c++/zmq.jar:zmq-perf.jar local_lat tcp://127.0.0.1:5555 1 100
#java -classpath "../src/zmq.jar:zmq-perf.jar" local_lat $@
