#!/bin/bash
hadoop fs -rmr output
hadoop fs -rmr input
hadoop fs -rmr outputPhone
hadoop fs -rmr inputPhone
hadoop fs -rmr outputlog
hadoop fs -rmr inputlog
echo "begin wordcount is begin!!!"
./run-wordcount.sh
echo -e "\n"
echo "the wordcount is end!!!"
echo -e "\n"
echo "begin logcount is begin!!!"
bash ./run-logcount.sh
echo -e "\n"
echo "the logcount is end!!!"
echo -e "\n"
echo "begin phone is begin!!!"
bash ./run-Phone.sh
echo -e "\n"
echo "the phone is end!!!"
echo -e "\n"
