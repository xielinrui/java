#!/bin/bash
# Create by chaojilaji, to delete the yarn's application of hadoop
# If you name this file like 'xxxx.sh'. you can use 'bash ./xxxx.sh 任务时间戳 开始任务号 结束任务号' to delete the application.


for((i=$2;i<=$3;i++));
do
if [ "$i" -lt 10 ];
then
cmd="yarn application -kill application_"$1"_000"$i
$cmd
fi
if [ "$i" -lt 100 ];
then
    if [ "$i" -gt 9 ];
    then
    cmd1="yarn application -kill application_"$1"_00"$i
    $cmd1
    fi
fi
if [ "$i" -lt 1000 ];
then
    if [ "$i" -gt 99 ];
    then
    cmd2="yarn application -kill application_"$1"_0"$i
    $cmd2
    fi
fi
done
