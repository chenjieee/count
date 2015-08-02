#!/bin/sh

echo "---- hadoop jar $HOME/wordcount.jar WordCount $@ ----"
hadoop jar $HOME/wordcount.jar WordCount $@

echo "---- hdfs dfs -get $2 $2 ----"
hdfs dfs -get $2 $2
