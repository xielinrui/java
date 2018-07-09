#!/bin/bash
# the hadoop cluster by running wordcount

# create input directory on HDFS
hadoop fs -mkdir -p inputPhone

# put input files to HDFS
hdfs dfs -put ./inputPhone/* inputPhone

# run wordcount 
hadoop jar $HADOOP_HOME/share/hadoop/mapreduce/sources/Phone-1.0.0.jar Phone inputPhone outputPhone


# print the input files
echo -e "\ninput file1.txt:"
hdfs dfs -cat inputPhone/filephone.txt


# print the output of wordcount
echo -e "\nphone output:"
hdfs dfs -cat outputPhone/part-r-00000

