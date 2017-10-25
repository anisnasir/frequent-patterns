#!/bin/bash
JAR="target/Densest-0.0.1-SNAPSHOT.jar"
input_dir="/home/anis/Datasets/"
#input_dir="/Users/anis/Datasets/Densest/"
#input="test_case10.txt"
#input="snap_facebook.txt"
#input="com-dblp.ungraph.txt"
#input="com-lj.ungraph.txt"
input="twitter_combined_modified.txt"
windowSize="1000001"
LOGGING="false"
k=5
command="java -jar ${JAR} ${input_dir} ${input} ${windowSize} ${LOGGING} ${k} "

$command

