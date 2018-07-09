#!/bin/bash

# test the hadoop cluster by running wordcount

# create input files 

# create input directory on HDFS
hadoop fs -mkdir -p inputlog

# put input files to HDFS
hdfs dfs -put ./inputlog/* inputlog

# run wordcount 
hadoop jar $HADOOP_HOME/share/hadoop/mapreduce/sources/logcount.jar logcount inputlog outputlog

# print the input files
echo -e "\ninput filelog.txt:"
hdfs dfs -cat inputlog/filelog.txt

# print the output of wordcount
echo -e "\nwordcount output:"
hdfs dfs -cat outputlog/part-r-00000

