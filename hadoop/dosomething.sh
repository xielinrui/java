#!/bin/bash
docker cp ./run-Phone.sh da07bda3bcb4:/root/
docker cp ./run-logcount.sh da07bda3bcb4:/root/
docker cp ./logcount.jar da07bda3bcb4:/usr/local/hadoop/share/hadoop/mapreduce/sources/
docker cp ./Phone-1.0.0.jar da07bda3bcb4:/usr/local/hadoop/share/hadoop/mapreduce/sources/
docker cp ./filelog.txt da07bda3bcb4:/root/inputlog/
docker cp ./filephone.txt da07bda3bcb4:/root/inputPhone/
docker cp ./qingchuhuanjing.sh da07bda3bcb4:/root/
