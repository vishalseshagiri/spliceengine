#!/bin/bash

currentDateTime=$(date +'%m-%d-%Y:%H:%M:%S')
cd structured_derby

mvn exec:java -Dzoo > zoo.log &

if ps ax | grep -v grep | grep 'exec:java' > /dev/null 
then
 sleep 300
else
 echo "zoo is not running!!"
fi


mvn exec:exec -DspliceCI > server.log &

if ps ax | grep -v grep | grep 'exec:exec' > /dev/null 
then
 sleep 300
else
 echo "splice is not running!!"
fi
